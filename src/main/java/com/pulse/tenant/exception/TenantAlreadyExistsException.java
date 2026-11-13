package com.pulse.tenant.exception;

public class TenantAlreadyExistsException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TenantAlreadyExistsException(String slug) {
		super("Tenant with slug '" + slug + "' already exists");
	}
}