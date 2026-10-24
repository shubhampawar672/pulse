package com.pulse.common.exception;

public class ResourceNotFoundException extends RuntimeException {

	public ResourceNotFoundException(String formatted) {
		super(formatted);
	}

}
