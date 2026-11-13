package com.pulse.ai.core.provider;

import org.springframework.ai.anthropic.AnthropicChatModel;
import org.springframework.ai.anthropic.AnthropicChatOptions;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.ollama.api.OllamaChatOptions;

import com.pulse.ai.core.client.ModelOption;

public class AnthropicProvider extends AbstractSpringAiProvider {

	public AnthropicProvider(AnthropicChatModel chatModel) {
		super(chatModel);

	}

	@Override
	public String id() {
		return "anthropic";
	}

	@Override
	protected ChatOptions.Builder<?> buildOptions(ModelOption options) {
		return AnthropicChatOptions.builder().model(options.model()).temperature(options.temperature());
	}

}
