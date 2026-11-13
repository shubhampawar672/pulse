package com.pulse.common.security;

import java.util.Collection;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class AuthenticatedUser extends User {
	private final UUID userId;
	private final UUID tenantId;

	public AuthenticatedUser(UUID userId, UUID tenantId, String username, String password, boolean enabled,
			Collection<? extends GrantedAuthority> authorities) {
		super(username, password, enabled, true, true, true, authorities);
		this.userId = userId;
		this.tenantId = tenantId;
	}

	public UUID getUserId() {
		return userId;
	}

	public UUID getTenantId() {
		return tenantId;
	}
}