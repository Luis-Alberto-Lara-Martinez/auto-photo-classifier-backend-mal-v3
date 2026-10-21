package com.autophotoclassifier.backend.dto;

import java.time.Instant;
import lombok.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Builder
public record ErrorDto(
        Integer status, String error, String message, String path, Instant timestamp
) {
    public static ErrorDto of(HttpStatus status, String message, String path) {
        return ErrorDto.builder()
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(path)
                .timestamp(Instant.now())
                .build();
    }

    public static ResponseEntity<ErrorDto> responseOf(HttpStatus status, String message, String path) {
        return ResponseEntity.status(status).body(of(status, message, path));
    }
}
