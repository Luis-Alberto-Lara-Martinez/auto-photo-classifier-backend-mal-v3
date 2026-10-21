package com.autophotoclassifier.backend.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JwtFilterError {

    MISSING_TOKEN("Missing or empty jwt"),
    EXPIRED("Expired jwt"),
    MALFORMED("Malformed jwt"),
    INVALID_SIGNATURE("Invalid jwt signature"),
    UNKNOWN("Unknown jwt error");

    private final String message;
}
