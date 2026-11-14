package com.projeto.apiingressos.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    private final String SECRET =
            "MinhaChaveSecretaParaProjetoIngressos2026Segura";

    private final SecretKey secretKey = Keys.hmacShaKeyFor(
            SECRET.getBytes(StandardCharsets.UTF_8)
    );

    public String gerarToken(String username) {
        long tempoExpiracao = 1000 * 60 * 60 * 2;

        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + tempoExpiracao))
                .signWith(secretKey)
                .compact();
    }

    public String extrairUsername(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}
