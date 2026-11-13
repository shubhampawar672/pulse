package com.pulse.ai.feature.nlsearch.domain;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.pulse.asset.dto.AssetFilterRequest;
import com.pulse.asset.entity.AssetStatus;
import com.pulse.asset.entity.AssetType;

public record ParsedFilters(

		@JsonPropertyDescription("Asset type. Set only if the user clearly names a type.") AssetType type,

		@JsonPropertyDescription("Asset status. Set only if the user clearly names a status.") AssetStatus status,

		@JsonPropertyDescription("Tags the asset must have, lowercase, one entry per tag.") List<String> tags,

		@JsonPropertyDescription("Created on or after this date, ISO format yyyy-MM-dd.") LocalDate createdFrom,

		@JsonPropertyDescription("Created on or before this date, ISO format yyyy-MM-dd.") LocalDate createdTo,

		@JsonPropertyDescription("Asset name text, reference code, or any words that fit no other field.") String keywords) {

	public static ParsedFilters keywordOnly(String text) {
		return new ParsedFilters(null, null, null, null, null, text);
	}

	public boolean isEmpty() {
		return type == null && status == null && (tags == null || tags.isEmpty()) && createdFrom == null
				&& createdTo == null && keywords == null;
	}

	public AssetFilterRequest toFilterRequest() {
		String tagsCsv = (tags == null || tags.isEmpty()) ? null : String.join(",", tags);
		return new AssetFilterRequest(type, status, tagsCsv, keywords, false);
	}
}