package com.autophotoclassifier.backend.property;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "encryption")
public record EncryptionProperty(
        @NotBlank String activeKey,
        @NotEmpty Map<@NotBlank String, @NotBlank String> keys
) {
    private static final String SECRET_KEY_ALGORITHM = "AES";
    private static final Integer MAX_KEY_NAME_LENGTH_BYTES = 255;
    private static final Integer REQUIRED_AES_KEY_LENGTH_BYTES = 32;

    @AssertTrue(message = "The active key must exist within the defined keys map")
    public boolean isActiveKeyInKeys() {
        return keys.containsKey(activeKey);
    }

    @AssertTrue(message = "Key names length in bytes must not exceed 255 bytes")
    public boolean isAllKeyNamesValid() {
        return keys.keySet().stream().allMatch(this::isKeyNameWithinByteLimit);
    }

    @AssertTrue(message = "Key values must be valid Base64 encoded strings resulting in a 32 byte AES key")
    public boolean isAllKeyValuesValid() {
        return keys.values().stream().allMatch(this::keyValueHasRequiredAesKeyLength);
    }

    public SecretKey buildSecretKey(String keyName) {
        try {
            return new SecretKeySpec(Base64.getDecoder().decode(keys.get(keyName)), SECRET_KEY_ALGORITHM);
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new IllegalArgumentException("Invalid key name: " + keyName);
        }
    }

    private boolean isKeyNameWithinByteLimit(String keyName) {
        return keyName.getBytes(StandardCharsets.UTF_8).length <= MAX_KEY_NAME_LENGTH_BYTES;
    }

    private boolean keyValueHasRequiredAesKeyLength(String keyValue) {
        try {
            return Base64.getDecoder().decode(keyValue).length == REQUIRED_AES_KEY_LENGTH_BYTES;
        } catch (IllegalArgumentException _) {
            return false;
        }
    }
}
