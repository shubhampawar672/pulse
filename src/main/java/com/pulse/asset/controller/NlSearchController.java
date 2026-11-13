package com.pulse.asset.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pulse.ai.feature.NlSearchService;
import com.pulse.ai.feature.nlsearch.dto.NlSearchRequest;
import com.pulse.ai.feature.nlsearch.dto.NlSearchResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/ai")
public class NlSearchController {

	private final NlSearchService nlSearchService;

	public NlSearchController(NlSearchService nlSearchService) {
		this.nlSearchService = nlSearchService;
	}

	@PostMapping("/nl-search")
	public NlSearchResponse search(@Valid @RequestBody NlSearchRequest request,
			@PageableDefault(size = 20, sort = "createdAt") Pageable pageable) {
		return nlSearchService.search(request.query(), pageable);
	}
}