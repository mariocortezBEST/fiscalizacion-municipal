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
        // If it's a new user or password is changed (logic handled in controller
        // usually, but here we ensure encoding)
        // For simplicity, we assume the controller sets the password only if it's new
        // or changed.
        // However, to be safe, we should check if the password is already encoded or
        // not,
        // but BCrypt strings look specific.
        // Better approach: Controller handles "if password not empty, encode it".
        // Here we just save. But wait, the plan said "CRUD logic + Password Encoding".

        // Let's rely on the controller to pass the raw password if it needs updating,
        // and we encode it here if it's not null/empty.

        // Actually, a better pattern for update is:
        // 1. Fetch existing user.
        // 2. Update fields.
        // 3. If new password provided, encode and set.
        // 4. Save.

        // Since we are creating a simple save method, let's assume the controller
        // prepares the object.
        // But to be helpful, let's add a specific method for creating/updating with
        // password handling.
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
