package com.pulse.ai.feature.query;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Component;

import com.pulse.ai.feature.nlsearch.domain.ParsedFilters;

import jakarta.validation.ValidationException;

@Component
public class FilterSanitizer {

	private static final int MAX_TAGS = 10;

	public ParsedFilters sanitize(ParsedFilters f) {
		LocalDate from = f.createdFrom();
		LocalDate to = f.createdTo();
		if (from != null && to != null && from.isAfter(to)) {
			LocalDate tmp = from;
			from = to;
			to = tmp;
		}

		List<String> tags = f.tags() == null ? null
				: f.tags().stream().filter(Objects::nonNull).map(t -> t.trim().toLowerCase()).filter(t -> !t.isBlank())
						.distinct().limit(MAX_TAGS).toList();

		ParsedFilters clean = new ParsedFilters(f.type(), f.status(), (tags == null || tags.isEmpty()) ? null : tags, from, to,
				blankToNull(f.keywords()));

		if (clean.isEmpty()) {
			throw new ValidationException("No usable filters extracted");
		}
		return clean;
	}

	private static String blankToNull(String s) {
		return (s == null || s.isBlank()) ? null : s.trim();
	}
}