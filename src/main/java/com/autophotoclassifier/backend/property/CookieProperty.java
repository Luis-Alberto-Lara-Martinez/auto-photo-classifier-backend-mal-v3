package com.autophotoclassifier.backend.property;

import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "cookie")
public record CookieProperty(
        @NotNull Duration longDuration,
        @NotNull Duration shortDuration
) {
    private static final String SECURE_PREFIX = "__Secure-";

    public String codeVerifier() {
        return SECURE_PREFIX + "code_verifier";
    }

    public String jwt() {
        return SECURE_PREFIX + "jwt";
    }

    public String refreshToken() {
        return SECURE_PREFIX + "refresh_token";
    }

    public String state() {
        return SECURE_PREFIX + "state";
    }
}
