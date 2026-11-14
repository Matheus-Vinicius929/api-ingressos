package com.projeto.apiingressos.controller;

import com.projeto.apiingressos.dto.LoginRequest;
import com.projeto.apiingressos.dto.RegistroRequest;
import com.projeto.apiingressos.entity.Usuario;
import com.projeto.apiingressos.service.AuthService;
import com.projeto.apiingressos.service.JwtService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    public AuthController(AuthService authService, JwtService jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(@Valid @RequestBody RegistroRequest request) {
        try {
            Usuario usuario = authService.registrar(
                    request.getUsername(),
                    request.getPassword()
            );

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of(
                            "id", usuario.getId(),
                            "username", usuario.getUsername()
                    ));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            Usuario usuario = authService.login(
                    request.getUsername(),
                    request.getPassword()
            );

            String token = jwtService.gerarToken(usuario.getUsername());

            return ResponseEntity.ok(Map.of("token", token));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
