package com.pulse.ai.core.provider;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

@Component
public class LlmProviderRegistry {

	private final Map<String, LlmProvider> providers;

	public LlmProviderRegistry(List<LlmProvider> all) {
		this.providers = all.stream().collect(Collectors.toMap(LlmProvider::id, Function.identity()));
	}

	public Optional<LlmProvider> get(String id) {
		return Optional.ofNullable(providers.get(id));
	}
}