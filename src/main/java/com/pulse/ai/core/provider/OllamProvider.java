package com.pulse.ai.core.provider;

import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaChatOptions;
import org.springframework.stereotype.Component;

import com.pulse.ai.core.client.ModelOption;
@Component
public class OllamProvider extends AbstractSpringAiProvider {

	protected OllamProvider(OllamaChatModel chatModel) {
		super(chatModel);

	}

	@Override
	public String id() {
		return "ollama";
	}

	@Override
	protected ChatOptions.Builder<?> buildOptions(ModelOption options) {
		return OllamaChatOptions.builder().model(options.model()).temperature(options.temperature());
	}

}
