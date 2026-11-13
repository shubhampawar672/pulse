package com.pulse.ai.core.provider;

import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;

import com.pulse.ai.core.client.ModelOption;

public class OpenAiProvider extends AbstractSpringAiProvider {

	public OpenAiProvider(OpenAiChatModel chatModel) {
		super(chatModel);
	}

	@Override
	public String id() {
		return "openai";
	}

	@Override
	protected ChatOptions.Builder<?> buildOptions(ModelOption options) {
		return OpenAiChatOptions.builder().model(options.model()).temperature(options.temperature())
				.maxTokens(options.maxTokens());
	}

}
