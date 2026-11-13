package com.pulse.ai.core.client;

public interface LlmClient {
	<T> T extract(LlmRequest request, Class<T> type);

	String complete(LlmRequest request);
}
