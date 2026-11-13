package com.pulse.ai.feature.nlsearch.dto;

import java.util.List;

import org.springframework.data.domain.Page;

import com.pulse.ai.feature.nlsearch.domain.ParsedFilters;
import com.pulse.asset.dto.AssetResponse;

public record NlSearchResponse(String query, // original user text
		ParsedFilters interpretedFilters, // what we understood (render as editable chips)
		boolean aiInterpreted, // false => fallback keyword search was used
		int count, Page<AssetResponse> results) {
	public static NlSearchResponse of(String query, ParsedFilters filters, boolean aiInterpreted,
			Page<AssetResponse> results) {
		return new NlSearchResponse(query, filters, aiInterpreted, results.getNumberOfElements(), results);
	}
}