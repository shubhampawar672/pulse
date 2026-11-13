package com.pulse.ai.core.client;

import org.springframework.ai.model.ModelOptions;

public record ModelOption(String model, Double temperature, Integer maxTokens) implements ModelOptions {

}
