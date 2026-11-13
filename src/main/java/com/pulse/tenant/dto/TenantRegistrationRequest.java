package com.pulse.tenant.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record TenantRegistrationRequest(
        @NotBlank @Size(max = 200) String name,
        @NotBlank @Pattern(regexp = "[a-z0-9-]{3,100}") String slug,
        @Email String adminUsername) {} 