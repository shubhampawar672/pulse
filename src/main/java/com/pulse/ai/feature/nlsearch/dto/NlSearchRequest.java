package com.pulse.ai.feature.nlsearch.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record NlSearchRequest(@NotBlank @Size(max = 300) String query) {
}