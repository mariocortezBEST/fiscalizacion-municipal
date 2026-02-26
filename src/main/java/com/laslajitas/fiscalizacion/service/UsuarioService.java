package com.laslajitas.fiscalizacion.service;

import com.laslajitas.fiscalizacion.entity.Usuario;
import com.laslajitas.fiscalizacion.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> findById(Long id) {
        return usuarioRepository.findById(id);
    }

    public Usuario save(Usuario usuario) {

        return usuarioRepository.save(usuario);
    }

    public void save(Usuario usuario, String rawPassword) {
        if (rawPassword != null && !rawPassword.isEmpty()) {
            usuario.setPassword(passwordEncoder.encode(rawPassword));
        }
        usuarioRepository.save(usuario);
    }

    public void deleteById(Long id) {
        usuarioRepository.deleteById(id);
    }

    public boolean existsByUsername(String username) {
        return usuarioRepository.findByUsername(username).isPresent();
    }
}
