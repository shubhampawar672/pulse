package com.pulse.ai.core.provider;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.model.ModelOptions;

import com.pulse.ai.core.client.LlmRequest;
import com.pulse.ai.core.client.ModelOption;

public abstract class AbstractSpringAiProvider implements LlmProvider {
	private ChatClient chatClient;

	protected AbstractSpringAiProvider(ChatModel chatModel) {
		this.chatClient = ChatClient.builder(chatModel).build();
	}

	protected abstract ChatOptions.Builder<?> buildOptions(ModelOption options);

	@Override
	public <T> T extract(LlmRequest req, ModelOption opts, Class<T> type) {
		return chatClient.prompt().options(buildOptions(opts)).system(req.systemPrompt()).user(req.userInput()).call()
				.entity(type);
	}

	@Override
	public String complete(LlmRequest req, ModelOption opts) {
		return chatClient.prompt().options(buildOptions(opts)).system(req.systemPrompt()).user(req.userInput()).call()
				.content();
	}

}
