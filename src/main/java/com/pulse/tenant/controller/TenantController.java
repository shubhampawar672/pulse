package com.pulse.tenant.controller;

import java.net.URI;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pulse.outbox.service.TenantService;
import com.pulse.tenant.dto.TenantRegistrationRequest;
import com.pulse.tenant.dto.TenantResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/tenants")
public class TenantController {

	private final TenantService tenantService;

	public TenantController(TenantService tenantService) {
		this.tenantService = tenantService;
	}

	@PostMapping
	public ResponseEntity<TenantResponse> register(@Valid @RequestBody TenantRegistrationRequest request) {
		TenantResponse response = tenantService.register(request);
		return ResponseEntity.created(URI.create("/api/v1/tenants/" + response.id())).body(response);
	}

	@GetMapping("/{id}")
	public TenantResponse get(@PathVariable UUID id) {
		return tenantService.getById(id);
	}
}