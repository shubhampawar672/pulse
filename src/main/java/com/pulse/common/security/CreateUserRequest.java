package com.pulse.common.security;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(@NotBlank @Email @Size(max = 200) String username,
		@NotBlank @Size(min = 12, max = 100) String password, @NotNull UserRole role) {
}
