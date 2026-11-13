package com.pulse.common.tenant;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.pulse.common.context.TenantContext;
import com.pulse.common.exception.ResourceNotFoundException;
import com.pulse.tenant.repository.TenantRepository;

@Component
public class CurrentTenantProvider {
	private final TenantRepository tenantRepository;

	public CurrentTenantProvider(TenantRepository tenantRepository) {
		this.tenantRepository = tenantRepository;
	}

	public Tenant getTenant() {
		UUID tenantId = TenantContext.get();
		if (tenantId == null) {
			throw new ResourceNotFoundException("No tenant context found for this request");
		}
		return tenantRepository.findById(tenantId)
				.orElseThrow(() -> new ResourceNotFoundException("Tenant not found: " + tenantId));
	}
}
