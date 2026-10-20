package com.autophotoclassifier.backend.property;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import java.net.URI;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "google")
public record GoogleProperty(
        @NotBlank String clientId,
        @NotBlank String clientSecret,
        @NotBlank String signInRedirectUri,
        @NotBlank String pickerRedirectUri
) {
    @AssertTrue(message = "Sign-in redirect URI must be a valid HTTPS URI with a host and a path")
    public boolean isSignInRedirectUriValid() {
        return isRedirectUriValid(signInRedirectUri);
    }

    @AssertTrue(message = "Picker redirect URI must be a valid HTTPS URI with a host and a path")
    public boolean isPickerRedirectUriValid() {
        return isRedirectUriValid(pickerRedirectUri);
    }

    public URI authUri() {
        return URI.create("https://accounts.google.com/o/oauth2/v2/auth");
    }

    public String pickerScope() {
        return "https://www.googleapis.com/auth/photospicker.mediaitems.readonly";
    }

    public String signInScope() {
        return "openid email profile";
    }

    public URI tokenUri() {
        return URI.create("https://oauth2.googleapis.com/token");
    }

    private boolean isRedirectUriValid(String redirectUri) {
        try {
            URI uri = URI.create(redirectUri);
            return "https".equalsIgnoreCase(uri.getScheme()) &&
                    uri.getHost() != null &&
                    uri.getUserInfo() == null &&
                    uri.getQuery() == null &&
                    uri.getFragment() == null &&
                    uri.getPath() != null;
        } catch (IllegalArgumentException _) {
            return false;
        }
    }
}
