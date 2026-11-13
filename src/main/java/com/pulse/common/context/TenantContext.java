package com.pulse.common.context;

import java.util.UUID;

public final class TenantContext {
	private static final ThreadLocal<UUID> CURRENT = new ThreadLocal<>();

	public static void set(UUID id) {
		CURRENT.set(id);
	}

	public static UUID get() {
		UUID id = CURRENT.get();
		if (id == null)
			throw new IllegalStateException("No tenant in context");
		return id;
	}

	public static void clear() {
		CURRENT.remove();
	}
}
