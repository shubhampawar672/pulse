package com.pulse.tenant.dto;

import java.time.Instant;
import java.util.UUID;

import com.pulse.common.tenant.Tenant;

import java.time.Instant;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record TenantResponse(UUID id, String name, String slug, String status, Instant createdAt, String adminUsername, // only
																														// set
																														// on
																														// registration
		String temporaryPassword) { 

	public static TenantResponse from(Tenant t) {
		return new TenantResponse(t.getId(), t.getName(), t.getSlug(), t.getStatus().name(), t.getCreatedAt(), null,
				null);
	}
	public static TenantResponse registered(Tenant t, String adminUsername, String temporaryPassword) {
		return new TenantResponse(t.getId(), t.getName(), t.getSlug(), t.getStatus().name(), t.getCreatedAt(),
				adminUsername, temporaryPassword);
	}
}
