package com.pulse.tenant.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pulse.common.tenant.Tenant;

public interface TenantRepository extends JpaRepository<Tenant, UUID> {

	boolean existsBySlug(String slug);

	Optional<Tenant> findBySlug(String slug);
}