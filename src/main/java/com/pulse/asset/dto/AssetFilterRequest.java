package com.pulse.asset.dto;

import com.pulse.asset.entity.AssetStatus;
import com.pulse.asset.entity.AssetType;

public record AssetFilterRequest(AssetType type, AssetStatus status, String tags, String query, boolean hidden) {

}
