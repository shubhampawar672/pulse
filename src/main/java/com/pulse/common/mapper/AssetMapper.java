package com.pulse.common.mapper;

import org.springframework.stereotype.Component;

import com.pulse.asset.dto.AssetResponse;
import com.pulse.asset.entity.Asset;

@Component
public class AssetMapper {

	public AssetResponse toResponse(Asset a) {
		return new AssetResponse(a.getId(), a.getExternalId(), a.getType(), a.getName(), a.getStatus(), a.getTagsCsv(),
				a.getCreatedAt());
	}
}