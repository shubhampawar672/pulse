package com.pulse.integration.github;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GitHubRepoDto(long id, String name, @JsonProperty("full_name") String fullName,
		@JsonProperty("private") boolean isPrivate, boolean archived, @JsonProperty("html_url") String htmlUrl) {
}