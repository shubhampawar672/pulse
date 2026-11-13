package com.pulse.outbox.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.pulse.common.security.TempPasswordGenerator;
import com.pulse.common.security.UserRole;
import com.pulse.common.security.UserService;
import com.pulse.common.tenant.Tenant;
import com.pulse.tenant.dto.TenantRegistrationRequest;
import com.pulse.tenant.dto.TenantResponse;
import com.pulse.tenant.repository.TenantRepository;

@Service
public class TenantService {

	private final TenantRepository tenantRepository;
	private final UserService userService;

	public TenantService(TenantRepository tenantRepository, UserService userService) {
		this.tenantRepository = tenantRepository;
		this.userService = userService;
	}

	@Transactional
	public TenantResponse register(TenantRegistrationRequest r) {
		Tenant tenant = tenantRepository.save(new Tenant(r.name(), r.slug()));

		String username = (r.adminUsername() == null || r.adminUsername().isBlank()) ? "admin@" + r.slug()
				: r.adminUsername().trim().toLowerCase();
		String tempPassword = TempPasswordGenerator.generate(16);

		userService.createUser(tenant.getId(), username, tempPassword, UserRole.ADMIN, true);

		return TenantResponse.registered(tenant, username, tempPassword);
	}

	@Transactional(readOnly = true)
	public TenantResponse getById(java.util.UUID id) {
		return tenantRepository.findById(id).map(TenantResponse::from)
				.orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(
						org.springframework.http.HttpStatus.NOT_FOUND, "Tenant not found"));
	}
}
