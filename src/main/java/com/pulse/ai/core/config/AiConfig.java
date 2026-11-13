package com.pulse.ai.core.config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AiProperties.class)
public class AiConfig {

	@Bean(destroyMethod = "shutdown")
	ExecutorService aiExecutor() {
		return Executors.newFixedThreadPool(16);
	}
}
