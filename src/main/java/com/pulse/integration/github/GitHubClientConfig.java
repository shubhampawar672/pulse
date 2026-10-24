package com.pulse.integration.github;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
@EnableConfigurationProperties(GitHubProperties.class)
public class GitHubClientConfig {

	@Bean
	RestClient gitHubRestClient(RestClient.Builder builder, GitHubProperties props) {
		SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
		factory.setConnectTimeout(props.connectTimeoutMs());
		factory.setReadTimeout(props.readTimeoutMs());

		RestClient.Builder b = builder.baseUrl(props.baseUrl()).requestFactory(factory)
				.defaultHeader(HttpHeaders.ACCEPT, "application/vnd.github+json")
				.defaultHeader("X-GitHub-Api-Version", "2022-11-28").defaultHeader(HttpHeaders.USER_AGENT, "pulse-app");

		if (props.token() != null && !props.token().isBlank()) {
			b.defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + props.token());
		}
		return b.build();
	}
}
