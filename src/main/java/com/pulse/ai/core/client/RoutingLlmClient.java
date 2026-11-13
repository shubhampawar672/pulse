package com.pulse.ai.core.client;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.model.ModelOptions;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.pulse.ai.core.config.AiProperties;
import com.pulse.ai.core.provider.LlmProvider;
import com.pulse.ai.core.provider.LlmProviderRegistry;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;

@Component
@Primary
public class RoutingLlmClient implements LlmClient {

	private static final Logger log = LoggerFactory.getLogger(RoutingLlmClient.class);

	private final AiProperties props;
	private final LlmProviderRegistry registry;
	private final CircuitBreakerRegistry breakers;
	private final ExecutorService executor;

	public RoutingLlmClient(AiProperties props, LlmProviderRegistry registry, CircuitBreakerRegistry breakers,
			ExecutorService aiExecutor) {
		this.props = props;
		this.registry = registry;
		this.breakers = breakers;
		this.executor = aiExecutor;
	}

	@Override
	public <T> T extract(LlmRequest req, Class<T> type) {
		return this.<T>execute(req, (provider, opts) -> provider.extract(req, opts, type));
	}

	@Override
	public String complete(LlmRequest req) {
		return this.<String>execute(req, (provider, opts) -> provider.complete(req, opts));
	}

	private <R> R execute(LlmRequest req, BiFunction<LlmProvider, ModelOption, R> call) {
		AiProperties.Feature feature = props.getFeatures().get(req.feature());
		if (!props.isEnabled() || feature == null || !feature.isEnabled()) {
			throw new AiException("AI feature disabled or not configured: " + req.feature());
		}

		AiException failure = new AiException("All model routes failed for " + req.feature());

		for (AiProperties.Route route : feature.getRoutes()) {
			if (!route.isEnabled())
				continue;

			Optional<LlmProvider> provider = registry.get(route.getProvider());
			if (provider.isEmpty()) {
				log.warn("Unknown provider '{}' in config", route.getProvider());
				continue;
			}

			ModelOption opts = new ModelOption(route.getModel(), route.getTemperature(), route.getMaxTokens());
			CircuitBreaker cb = breakers.circuitBreaker(route.getProvider() + ":" + route.getModel());

			try {
				return cb
						.executeSupplier(() -> withTimeout(() -> call.apply(provider.get(), opts), route.getTimeout()));
			} catch (CallNotPermittedException e) {
				log.warn("Circuit open, skipping {}:{}", route.getProvider(), route.getModel());
				failure.addSuppressed(e);
			} catch (Exception e) {
				log.warn("Route {}:{} failed for feature {}: {}", route.getProvider(), route.getModel(), req.feature(),
						e.toString());
				failure.addSuppressed(e);
			}
		}
		throw failure;
	}

	private <R> R withTimeout(Supplier<R> task, Duration timeout) {
		Future<R> future = executor.submit(task::get);
		try {
			return future.get(timeout.toMillis(), TimeUnit.MILLISECONDS);
		} catch (TimeoutException e) {
			future.cancel(true);
			throw new AiException("LLM call timed out after " + timeout, e);
		} catch (ExecutionException e) {
			throw new AiException("LLM call failed", e.getCause());
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new AiException("Interrupted", e);
		}
	}
}