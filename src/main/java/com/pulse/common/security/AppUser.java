package com.pulse.common.security;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "app_user")
public class AppUser {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(name = "tenant_id", nullable = false, updatable = false)
	private UUID tenantId;

	@Column(nullable = false, unique = true)
	private String username;

	@Column(name = "password_hash", nullable = false)
	private String passwordHash;

	@Column(nullable = false)
	private String role = "USER";

	@Column(nullable = false)
	private boolean enabled = true;

	@Column(name = "must_change_password", nullable = false)
	private boolean mustChangePassword = false;

	public AppUser(UUID tenantId, String username, String passwordHash, String role, boolean mustChangePassword) {
		this(tenantId, username, passwordHash, role);
		this.mustChangePassword = mustChangePassword;
	}

	public boolean isMustChangePassword() {
		return mustChangePassword;
	}

	public void changePassword(String newHash) {
		this.passwordHash = newHash;
		this.mustChangePassword = false;
	}

	protected AppUser() {
	}

	public AppUser(UUID tenantId, String username, String passwordHash, String role) {
		this.tenantId = tenantId;
		this.username = username;
		this.passwordHash = passwordHash;
		this.role = role;
	}

	public UUID getId() {
		return id;
	}

	public UUID getTenantId() {
		return tenantId;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public String getRole() {
		return role;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public String getUsername() {
		return username;
	}

}