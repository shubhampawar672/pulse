package com.pulse.asset.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.pulse.asset.dto.AssetFilterRequest;
import com.pulse.asset.dto.AssetRequest;
import com.pulse.asset.dto.AssetResponse;
import com.pulse.asset.entity.Asset;
import com.pulse.asset.repository.AssetRepository;
import com.pulse.common.exception.BadRequestException;
import com.pulse.common.exception.ResourceNotFoundException;
import com.pulse.specification.AssetSpecification;

@Service
public class AssetService {

	private AssetRepository assetRepository;

	public AssetService(AssetRepository assetRepository) {
		this.assetRepository = assetRepository;
	}

	public AssetResponse createAsset(String tenantId, AssetRequest assetRequest) {
		Asset asset = new Asset.Builder().tenantId(tenantId).externalId(assetRequest.externalId())
				.AssteStatus(assetRequest.status()).AssteType(assetRequest.type()).tagsCsv(assetRequest.tagsCsv())
				.name(assetRequest.name()).build();
		asset = assetRepository.save(asset);
		return toResponse(asset);
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

	public Page<AssetResponse> search(String tenantId, AssetFilterRequest request, Pageable pageable) {
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
