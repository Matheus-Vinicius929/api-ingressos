package com.projeto.apiingressos.service;

import com.projeto.apiingressos.entity.Usuario;
import com.projeto.apiingressos.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Usuario registrar(String username, String password) {
        if (usuarioRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("USERNAME_JA_EXISTE");
        }

        String senhaCriptografada = passwordEncoder.encode(password);
        Usuario usuario = new Usuario(username, senhaCriptografada);

        return usuarioRepository.save(usuario);
    }

    public Usuario login(String username, String password) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("CREDENCIAIS_INVALIDAS"));

        if (!passwordEncoder.matches(password, usuario.getPassword())) {
            throw new IllegalArgumentException("CREDENCIAIS_INVALIDAS");
        }

        return usuario;
    }
}
