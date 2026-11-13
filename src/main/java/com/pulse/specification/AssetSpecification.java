package com.pulse.specification;

import java.time.Instant;
import java.time.ZoneId;
import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

import com.pulse.ai.feature.nlsearch.domain.ParsedFilters;
import com.pulse.asset.dto.AssetFilterRequest;
import com.pulse.asset.entity.Asset;
import com.pulse.asset.entity.AssetStatus;
import com.pulse.asset.entity.AssetType;

public class AssetSpecification {

	public static Specification<Asset> withFilters(AssetFilterRequest request, UUID tenantId) {
		Specification<Asset> spec = hastenant(tenantId);
		if (request.status() != null) {
			spec = spec.and(hasStatus(request.status()));
		}
		if (request.type() != null) {
			spec = spec.and(hasType(request.type()));
		}
		if (request.tags() != null && !request.tags().isBlank()) {
			spec = spec.and(tags(request.tags()));
		}
		if (request.hidden()) {
			spec = spec.and(isHidden(true));
		}
		return spec;
	}

	public static Specification<Asset> withParsedFilters(ParsedFilters f, UUID tenantId, ZoneId zone) {

		Specification<Asset> spec = hastenant(tenantId);

		if (f.type() != null)
			spec = spec.and(hasType(f.type()));
		if (f.status() != null)
			spec = spec.and(hasStatus(f.status()));
//		if (f.externalId() != null)
//			spec = spec.and(externalIdEquals(f.externalId()));
//		if (f.name() != null)
//			spec = spec.and(nameContains(f.name()));

		if (f.tags() != null) {
			for (String tag : f.tags()) {
				spec = spec.and(hasTag(tag));
			}
		}
		if (f.createdFrom() != null) {
			spec = spec.and(createdFrom(f.createdFrom().atStartOfDay(zone).toInstant()));
		}
		if (f.createdTo() != null) {
			spec = spec.and(createdBefore(f.createdTo().plusDays(1).atStartOfDay(zone).toInstant()));
		}
		if (f.keywords() != null) {
			spec = spec.and(keywordMatches(f.keywords()));
		}
		return spec;
	}

	public static Specification<Asset> externalIdEquals(String externalId) {
		return (root, query, cb) -> cb.equal(cb.lower(root.get("externalId")), externalId.toLowerCase());
	}

	public static Specification<Asset> nameContains(String name) {
		return (root, query, cb) -> cb.like(cb.lower(root.get("name")), contains(name), '\\');
	}

	public static Specification<Asset> hasTag(String tag) {
		return (root, query, cb) -> cb.like(cb.concat(cb.concat(",", cb.lower(root.get("tagsCsv"))), ","),
				"%," + escape(tag.toLowerCase()) + ",%", '\\');
	}

	public static Specification<Asset> createdFrom(Instant from) {
		return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("createdAt"), from);
	}

	public static Specification<Asset> createdBefore(Instant before) {
		return (root, query, cb) -> cb.lessThan(root.get("createdAt"), before);
	}

	public static Specification<Asset> keywordMatches(String keywords) {
		return (root, query, cb) -> {
			String pattern = contains(keywords);
			return cb.or(cb.like(cb.lower(root.get("name")), pattern, '\\'),
					cb.like(cb.lower(root.get("tagsCsv")), pattern, '\\'),
					cb.like(cb.lower(root.get("externalId")), pattern, '\\'));
		};
	}

	private static String contains(String s) {
		return "%" + escape(s.toLowerCase()) + "%";
	}

	private static String escape(String s) {
		return s.replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_");
	}

	public static Specification<Asset> hastenant(UUID tenantId) {
	    return (root, query, cb) -> cb.equal(root.get("tenantId").get("id"), tenantId);
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
