package com.autophotoclassifier.backend.property;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import java.net.URI;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "frontend")
public record FrontendProperty(
        @NotBlank String baseUrl
) {
    @AssertTrue(message = "Base url must be an absolute URL with scheme and host (e.g. https://example.com)")
    public boolean isBaseUrlValid() {
        try {
            URI uri = URI.create(baseUrl);
            String scheme = uri.getScheme();
            return ("http".equalsIgnoreCase(scheme) || "https".equalsIgnoreCase(scheme)) &&
                    uri.getHost() != null &&
                    uri.getUserInfo() == null &&
                    uri.getQuery() == null &&
                    uri.getFragment() == null &&
                    (uri.getPath() == null || uri.getPath().isBlank() || "/".equals(uri.getPath()));
        } catch (IllegalArgumentException _) {
            return false;
        }
    }
}
