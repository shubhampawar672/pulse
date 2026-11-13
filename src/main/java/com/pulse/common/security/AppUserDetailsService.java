package com.pulse.common.security;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.pulse.common.tenant.TenantStatus;
import com.pulse.tenant.repository.TenantRepository;

@Service
public class AppUserDetailsService implements UserDetailsService {

	private final AppUserRepository users;
	private final TenantRepository tenants;

	public AppUserDetailsService(AppUserRepository users, TenantRepository tenants) {
		this.users = users;
		this.tenants = tenants;
	}

	@Override
	public UserDetails loadUserByUsername(String username) {
		AppUser u = users.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Bad credentials"));

		boolean tenantActive = tenants.findById(u.getTenantId()).map(t -> t.getStatus() == TenantStatus.ACTIVE)
				.orElse(false);

		return new AuthenticatedUser(u.getId(), u.getTenantId(), u.getUsername(), u.getPasswordHash(),
				u.isEnabled() && tenantActive, List.of(new SimpleGrantedAuthority("ROLE_" + u.getRole())));
	}
}
