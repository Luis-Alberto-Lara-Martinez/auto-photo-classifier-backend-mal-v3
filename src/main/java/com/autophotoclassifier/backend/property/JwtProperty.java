package com.autophotoclassifier.backend.property;

import io.jsonwebtoken.security.Keys;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import java.util.Base64;
import java.util.Set;
import javax.crypto.SecretKey;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "jwt")
public record JwtProperty(
        @NotBlank String audience,
        @NotNull Duration duration,
        @NotBlank String issuer,
        @NotBlank String secret
) {
    private static final Set<Integer> SUPPORTED_HMAC_KEY_LENGTH_BYTES = Set.of(32, 48, 64);

    @AssertTrue(message = "JWT secret must be a valid Base64 string that decodes to 32, 48 or 64 bytes")
    public boolean isJwtSecretValid() {
        try {
            return SUPPORTED_HMAC_KEY_LENGTH_BYTES.contains(Base64.getDecoder().decode(secret).length);
        } catch (IllegalArgumentException _) {
            return false;
        }
    }

    public SecretKey buildSecretKey() {
        return Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret));
    }
}
