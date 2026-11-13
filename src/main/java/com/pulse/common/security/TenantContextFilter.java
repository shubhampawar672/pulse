package com.pulse.common.security;

import java.io.IOException;

import org.springframework.web.filter.OncePerRequestFilter;

import com.pulse.common.context.TenantContext;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;

public class TenantContextFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof AuthenticatedUser user) {
				TenantContext.set(user.getTenantId());
			}
			chain.doFilter(request, response);
		} finally {
			TenantContext.clear(); // threads are pooled, always clear
		}
	}
	
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
	    String path = request.getRequestURI();
	    return path.equals("/api/v1/tenants") || path.startsWith("/actuator/health");
	}
}
