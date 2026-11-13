package com.pulse.ai.feature;

import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.endpoint.Sanitizer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.pulse.ai.core.client.AiException;
import com.pulse.ai.feature.nlsearch.domain.ParsedFilters;
import com.pulse.ai.feature.nlsearch.dto.NlSearchResponse;
import com.pulse.ai.feature.query.FilterSanitizer;
import com.pulse.ai.feature.query.NlQueryParser;
import com.pulse.asset.dto.AssetResponse;
import com.pulse.asset.repository.AssetRepository;
import com.pulse.asset.service.AssetService;
import com.pulse.common.context.TenantContext;
import com.pulse.common.mapper.AssetMapper;
import com.pulse.specification.AssetSpecification;

import ch.qos.logback.core.subst.Parser;
import io.micrometer.core.instrument.config.validate.ValidationException;

@Service
public class NlSearchService {

	private static final Logger log = LoggerFactory.getLogger(NlSearchService.class);
	private static final int MAX_RESULTS = 50;

	private final NlQueryParser parser;
	private final FilterSanitizer sanitizer;
	private final AssetRepository assetRepository;
	private final AssetMapper assetMapper;
	private final ZoneId zone = ZoneId.systemDefault();
	private AssetService assetService;

	public NlSearchService(NlQueryParser parser, FilterSanitizer sanitizer, AssetRepository assetRepository,
			AssetMapper assetMapper, AssetService assetService) {
		this.parser = parser;
		this.sanitizer = sanitizer;
		this.assetRepository = assetRepository;
		this.assetMapper = assetMapper;
		this.assetService = assetService;
	}

	public NlSearchResponse search(String text, Pageable pageable) {
		UUID tenantId = TenantContext.get();
		ParsedFilters filters;
		boolean aiInterpreted = true;
		try {
			filters = sanitizer.sanitize(parser.parse(text));
		} catch (AiException | ValidationException e) {
			log.warn("NL parsing unavailable, using keyword search: {}", e.toString());
			filters = ParsedFilters.keywordOnly(text);
			aiInterpreted = false;
		}

		Page<AssetResponse> page = assetService.search(filters.toFilterRequest(), pageable);
		return NlSearchResponse.of(text, filters, aiInterpreted, page);
	}

}
