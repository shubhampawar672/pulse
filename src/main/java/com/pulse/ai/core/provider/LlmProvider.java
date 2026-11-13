package com.pulse.ai.core.provider;


import com.pulse.ai.core.client.LlmRequest;
import com.pulse.ai.core.client.ModelOption;

public interface LlmProvider {
	String id();

	<T> T extract(LlmRequest request, ModelOption options, Class<T> type);

	String complete(LlmRequest request, ModelOption options);
}
