package com.autophotoclassifier.backend.service;

import com.autophotoclassifier.backend.property.JwtProperty;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class JwtService {

    private final JwtProperty jwtProperty;

    public Claims verifyAndParse(String jwt) {
        return Jwts.parser()
                .verifyWith(jwtProperty.buildSecretKey())
                .requireAudience(jwtProperty.audience())
                .requireIssuer(jwtProperty.issuer())
                .build()
                .parseSignedClaims(jwt)
                .getPayload();
    }
}
