package com.pulse.asset.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pulse.asset.dto.AssetFilterRequest;
import com.pulse.asset.dto.AssetRequest;
import com.pulse.asset.dto.AssetResponse;
import com.pulse.asset.service.AssetService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/asset")
public class AssetController {

	private AssetService assetService;

	public AssetController(AssetService assetService) {
		this.assetService = assetService;
	}

	@PostMapping
	public ResponseEntity<AssetResponse> create(@RequestHeader("X-tenant-id") String tenantId,
			@Valid @RequestBody AssetRequest request) {

		return ResponseEntity.status(HttpStatus.CREATED).body(assetService.createAsset(tenantId, request));

	}

	@GetMapping("/{id}")
	public ResponseEntity<AssetResponse> getById(@RequestHeader("X-tenant-id") String tenantId, @PathVariable Long id) {
		return ResponseEntity.status(HttpStatus.OK).body(assetService.getById(tenantId, id));
	}

	@GetMapping("/search")
	public Page<AssetResponse> search(@RequestHeader("X-tenant-id") String tenantId, AssetFilterRequest filterRequest,
			@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
		return assetService.search(tenantId, filterRequest, pageable);
	}

}
