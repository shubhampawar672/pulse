package com.pulse.asset.dto;

import java.time.Instant;

import com.pulse.asset.entity.AssetStatus;
import com.pulse.asset.entity.AssetType;

public record AssetResponse(Long id, String externalId, AssetType type, String name, AssetStatus status, String tagsCsv,
		Instant createdAt) {

}
