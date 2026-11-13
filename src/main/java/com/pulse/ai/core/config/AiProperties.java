package com.pulse.ai.core.config;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.ai")
public class AiProperties {
	private boolean enabled = true;
	private Map<String, Feature> features = new HashMap<>();

	public static class Feature {
		private boolean enabled = true;
		private java.util.List<Route> routes = new ArrayList<>();

		public boolean isEnabled() {
			return enabled;
		}

		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
		}

		public java.util.List<Route> getRoutes() {
			return routes;
		}

		public void setRoutes(java.util.List<Route> routes) {
			this.routes = routes;
		}

	}

	public static class Route {
		private String provider;
		private String model;
		private Duration timeout = Duration.ofSeconds(80);
		private Double temperature = 0.0;
		private Integer maxTokens = 1000;
		private boolean enabled = true;

		public String getProvider() {
			return provider;
		}

		public void setProvider(String provider) {
			this.provider = provider;
		}

		public String getModel() {
			return model;
		}

		public void setModel(String model) {
			this.model = model;
		}

		public Duration getTimeout() {
			return timeout;
		}

		public void setTimeout(Duration timeout) {
			this.timeout = timeout;
		}

		public Double getTemperature() {
			return temperature;
		}

		public void setTemperature(Double temperature) {
			this.temperature = temperature;
		}

		public Integer getMaxTokens() {
			return maxTokens;
		}

		public void setMaxTokens(Integer maxTokens) {
			this.maxTokens = maxTokens;
		}

		public boolean isEnabled() {
			return enabled;
		}

		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
		}

	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Map<String, Feature> getFeatures() {
		return features;
	}

	public void setFeatures(Map<String, Feature> features) {
		this.features = features;
	}

}
