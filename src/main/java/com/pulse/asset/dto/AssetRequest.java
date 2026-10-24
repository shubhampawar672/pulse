package com.pulse.asset.dto;

import com.pulse.asset.entity.AssetStatus;
import com.pulse.asset.entity.AssetType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AssetRequest(@NotBlank String externalId, @NotNull AssetType type, @NotBlank String name,
		@NotNull AssetStatus status, String tagsCsv) {

}
