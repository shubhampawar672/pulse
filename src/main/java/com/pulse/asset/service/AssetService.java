package com.pulse.asset.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.pulse.asset.dto.AssetFilterRequest;
import com.pulse.asset.dto.AssetRequest;
import com.pulse.asset.dto.AssetResponse;
import com.pulse.asset.entity.Asset;
import com.pulse.asset.repository.AssetRepository;
import com.pulse.common.context.TenantContext;
import com.pulse.common.exception.BadRequestException;
import com.pulse.common.exception.ResourceNotFoundException;
import com.pulse.specification.AssetSpecification;
import com.pulse.tenant.repository.TenantRepository;

import jakarta.transaction.Transactional;

@Service
public class AssetService {

	private AssetRepository assetRepository;
	private TenantRepository tenantRepository;

	public AssetService(AssetRepository assetRepository, TenantRepository tenantRepository) {
		this.assetRepository = assetRepository;
		this.tenantRepository = tenantRepository;
	}

	@Transactional
	public AssetResponse createAsset(AssetRequest request) {
		UUID tenantId = TenantContext.get();
		Asset asset = new Asset.Builder().tenantId(tenantRepository.getReferenceById(tenantId))
				.externalId(request.externalId()).AssteStatus(request.status()).AssetType(request.type())
				.tagsCsv(request.tagsCsv()).name(request.name()).build();
		return toResponse(assetRepository.save(asset));
	}

	public AssetResponse getById(String tenantId, Long id) throws ResourceNotFoundException {
		Asset asset = assetRepository.findById(id).filter(a -> a.getTenantId().equals(tenantId))
				.orElseThrow(() -> new ResourceNotFoundException("Asset with id %s not found!".formatted(id)));
		return toResponse(asset);

	}

	private AssetResponse toResponse(Asset a) {
		return new AssetResponse(a.getId(), a.getExternalId(), a.getType(), a.getName(), a.getStatus(), a.getTagsCsv(),
				a.getCreatedAt());
	}

	public Page<AssetResponse> search(AssetFilterRequest request, Pageable pageable) {
		UUID tenantId = TenantContext.get();
		if (pageable.getPageNumber() < 0) {
			throw new BadRequestException("Invalid page number");

		}
		if (pageable.getPageSize() > 100) {
			throw new BadRequestException("Maximum page size is 100");
		}
		return assetRepository.findAll(AssetSpecification.withFilters(request, tenantId), pageable)
				.map(this::toResponse);
	}

}
