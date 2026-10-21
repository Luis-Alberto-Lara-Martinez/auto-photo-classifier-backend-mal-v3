package com.autophotoclassifier.backend.security;

import com.autophotoclassifier.backend.constant.SecurityConstant;
import com.autophotoclassifier.backend.enumeration.JwtFilterError;
import com.autophotoclassifier.backend.property.CookieProperty;
import com.autophotoclassifier.backend.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

@RequiredArgsConstructor
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final CookieProperty cookieProperty;
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            processJwtAuthentication(request);
        } finally {
            filterChain.doFilter(request, response);
        }
    }

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        return SecurityConstant.PUBLIC_ROUTES.contains(request.getServletPath());
    }

    private void processJwtAuthentication(HttpServletRequest request) {
        Cookie jwtCookie = WebUtils.getCookie(request, cookieProperty.jwt());
        if (jwtCookie == null) {
            handleJwtAuthenticationError(request, JwtFilterError.MISSING_TOKEN);
            return;
        }
        String jwt = jwtCookie.getValue();
        if (jwt == null || jwt.isBlank()) {
            handleJwtAuthenticationError(request, JwtFilterError.MALFORMED);
            return;
        }
        try {
            Claims claims = jwtService.verifyAndParse(jwt);
            String googleSub = claims.getSubject();
            if (googleSub != null && !googleSub.isBlank()) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        googleSub, null, Collections.emptyList()
                );
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                handleJwtAuthenticationError(request, JwtFilterError.MALFORMED);
            }
        } catch (ExpiredJwtException e) {
            handleJwtAuthenticationError(request, JwtFilterError.EXPIRED);
        } catch (SignatureException e) {
            handleJwtAuthenticationError(request, JwtFilterError.INVALID_SIGNATURE);
        } catch (JwtException | IllegalArgumentException e) {
            handleJwtAuthenticationError(request, JwtFilterError.MALFORMED);
        }
    }

    private void handleJwtAuthenticationError(HttpServletRequest request, JwtFilterError jwtFilterError) {
        request.setAttribute(SecurityConstant.JWT_ERROR_ATTRIBUTE, jwtFilterError);
        SecurityContextHolder.clearContext();
    }
}
