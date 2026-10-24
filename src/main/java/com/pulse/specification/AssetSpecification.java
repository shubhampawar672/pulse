package com.pulse.specification;

import org.springframework.data.jpa.domain.Specification;

import com.pulse.asset.dto.AssetFilterRequest;
import com.pulse.asset.entity.Asset;
import com.pulse.asset.entity.AssetStatus;
import com.pulse.asset.entity.AssetType;

public class AssetSpecification {

	public static Specification<Asset> withFilters(AssetFilterRequest request, String tenantID) {

		Specification<Asset> spec = Specification.where(hastenant(tenantID));
		if (request.status() != null) {
			spec.and(hasStatus(request.status()));
		}
		if (request.type() != null) {
			spec.and(hasType(request.type()));
		}

		if (request.tags() != null && !request.tags().isBlank()) {
			spec.and(tags(request.tags()));
		}
		if (request.hidden()) {
			spec.and(isHidden(request.hidden()));
		}
		return spec;
	}

	public static Specification<Asset> hastenant(String tenantId) {
		return (root, query, cb) -> cb.equal(root.get("tenantId"), tenantId);
	}

	public static Specification<Asset> hasStatus(AssetStatus status) {
		return (root, query, cb) -> cb.equal(root.get("status"), status);
	}

	public static Specification<Asset> hasType(AssetType type) {
		return (root, query, cb) -> cb.equal(root.get("type"), type);
	}

	public static Specification<Asset> isHidden(boolean hidden) {
		return (root, query, cb) -> cb.equal(root.get("hidden"), hidden);
	}

	public static Specification<Asset> tags(String tags) {
		return (root, query, cb) -> cb.like(root.get("tagsCsv"), "%" + tags + "%");
	}
}
