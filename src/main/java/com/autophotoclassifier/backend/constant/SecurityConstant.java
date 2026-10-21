package com.autophotoclassifier.backend.constant;

import java.util.Set;

public class SecurityConstant {

    public static final String JWT_ERROR_ATTRIBUTE = "jwt_filter_error";
    public static final Set<String> PUBLIC_ROUTES = Set.of(
            "/google/oauth2/sign-in",
            "/google/oauth2/callback",
            "/google/picker/authorize",
            "/google/picker/callback",
            "/auth/refresh",
            "/auth/logout"
    );

    private SecurityConstant() {
    }
}
