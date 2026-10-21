package com.autophotoclassifier.backend.security;

import com.autophotoclassifier.backend.constant.SecurityConstant;
import com.autophotoclassifier.backend.dto.ErrorDto;
import com.autophotoclassifier.backend.enumeration.JwtFilterError;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@RequiredArgsConstructor
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final HttpStatus RESPONSE_STATUS = HttpStatus.UNAUTHORIZED;

    private final ObjectMapper objectMapper;

    @Override
    public void commence(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull AuthenticationException authException
    ) throws IOException {
        JwtFilterError jwtFilterError = (JwtFilterError) request.getAttribute(SecurityConstant.JWT_ERROR_ATTRIBUTE);
        String errorMessage = jwtFilterError != null ? jwtFilterError.getMessage() : JwtFilterError.UNKNOWN.getMessage();
        log.error("JWT authentication attempt failed at [{}]: {}", request.getRequestURI(), errorMessage, authException);
        response.setStatus(RESPONSE_STATUS.value());
        response.setCharacterEncoding(StandardCharsets.UTF_8);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(
                response.getOutputStream(), ErrorDto.of(RESPONSE_STATUS, errorMessage, request.getRequestURI())
        );
    }
}
