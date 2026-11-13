package com.pulse.common.security;

import java.util.UUID;

public record UserResponse(UUID id, UUID tenantId, String username, String role, boolean enabled) {
}
