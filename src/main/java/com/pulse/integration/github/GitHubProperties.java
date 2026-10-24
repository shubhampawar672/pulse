package com.pulse.integration.github;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "pulse.github")
public record GitHubProperties(String baseUrl, String token, int connectTimeoutMs, int readTimeoutMs, int pageSize) {
}
