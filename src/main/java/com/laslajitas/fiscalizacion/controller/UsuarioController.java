package com.laslajitas.fiscalizacion.controller;

import com.laslajitas.fiscalizacion.entity.Usuario;
import com.laslajitas.fiscalizacion.enums.Rol;
import com.laslajitas.fiscalizacion.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("usuarios", usuarioService.findAll());
        model.addAttribute("pageTitle", "Gestión de Usuarios");
        return "usuarios/index";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("roles", Rol.values());
        model.addAttribute("pageTitle", "Nuevo Usuario");
        return "usuarios/form";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Usuario usuario, @RequestParam(required = false) String rawPassword,
            RedirectAttributes redirectAttributes) {
        try {
            // Validation: Username must be unique for new users
            if (usuario.getId() == null && usuarioService.existsByUsername(usuario.getUsername())) {
                redirectAttributes.addFlashAttribute("error", "El nombre de usuario ya existe.");
                return "redirect:/usuarios/nuevo";
            }

            // For existing users, if password is empty, it means we don't want to change
            // it.
            // But my service logic for 'save(usuario, rawPassword)' handles encoding if
            // rawPassword is not empty.
            // If it is empty, we need to make sure we don't overwrite the existing password
            // with null/empty if we are just calling save(usuario).
            // However, the form submission will map fields to 'usuario'. If 'password'
            // field is empty in form, 'usuario.password' might be null or empty.

            if (usuario.getId() != null) {
                Usuario existing = usuarioService.findById(usuario.getId()).orElse(null);
                if (existing != null) {
                    if (rawPassword == null || rawPassword.isEmpty()) {
                        usuario.setPassword(existing.getPassword()); // Keep existing password
                    }
                }
            }

            usuarioService.save(usuario, rawPassword);
            redirectAttributes.addFlashAttribute("success", "Usuario guardado correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar el usuario: " + e.getMessage());
        }
        return "redirect:/usuarios";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Usuario usuario = usuarioService.findById(id).orElse(null);
        if (usuario == null) {
            return "redirect:/usuarios";
        }
        model.addAttribute("usuario", usuario);
        model.addAttribute("roles", Rol.values());
        model.addAttribute("pageTitle", "Editar Usuario");
        return "usuarios/form";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            usuarioService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Usuario eliminado correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el usuario.");
        }
        return "redirect:/usuarios";
    }
}
