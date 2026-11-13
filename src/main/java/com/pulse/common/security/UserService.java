package com.pulse.common.security;

import java.util.UUID;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import jakarta.transaction.Transactional;

@Service
public class UserService {

	private final AppUserRepository users;
	private final PasswordEncoder passwordEncoder;

	public UserService(AppUserRepository users, PasswordEncoder passwordEncoder) {
		this.users = users;
		this.passwordEncoder = passwordEncoder;
	}

	@Transactional
	public UserResponse createUser(UUID tenantId, String username, String rawPassword,
	                               UserRole role, boolean mustChangePassword) {
	    String name = username.trim().toLowerCase();
	    if (users.existsByUsername(name)) {
	        throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
	    }
	    AppUser saved = users.saveAndFlush(new AppUser(
	            tenantId, name, passwordEncoder.encode(rawPassword), role.name(), mustChangePassword));
	    return new UserResponse(saved.getId(), saved.getTenantId(),
	            saved.getUsername(), saved.getRole(), saved.isEnabled());
	}

	@Transactional
	public void changePassword(String username, ChangePasswordRequest r) {
	    AppUser user = users.findByUsername(username)
	            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

	    if (!passwordEncoder.matches(r.currentPassword(), user.getPasswordHash())) {
	        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Current password is incorrect");
	    }
	    if (passwordEncoder.matches(r.newPassword(), user.getPasswordHash())) {
	        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
	                "New password must differ from the current one");
	    }
	    user.changePassword(passwordEncoder.encode(r.newPassword()));
	}
}