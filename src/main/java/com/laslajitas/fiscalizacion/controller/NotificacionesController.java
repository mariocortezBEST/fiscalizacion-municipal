package com.laslajitas.fiscalizacion.controller;

import com.laslajitas.fiscalizacion.enums.TipoTramite;
import com.laslajitas.fiscalizacion.entity.Tramite;
import com.laslajitas.fiscalizacion.service.TramiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/notificaciones")
public class NotificacionesController {

    @Autowired
    private TramiteService tramiteService;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("pageTitle", "Notificaciones");
        model.addAttribute("notificaciones", tramiteService.findByTipo(TipoTramite.NOTIFICACION));
        return "notificaciones/index";
    }

    @GetMapping("/nueva")
    public String nuevaNotificacion(Model model) {
        model.addAttribute("pageTitle", "Nueva Notificación");
        model.addAttribute("tramite", new Tramite());
        return "notificaciones/form";
    }

    @PostMapping("/guardar")
    public String guardarNotificacion(Tramite tramite) {
        tramiteService.guardarNotificacion(tramite);
        return "redirect:/notificaciones";
    }

    @GetMapping("/ver/{id}")
    public String verNotificacion(@PathVariable Long id, Model model) {
        model.addAttribute("pageTitle", "Ver Notificación");
        model.addAttribute("tramite", tramiteService.findById(id));
        model.addAttribute("readonly", true);
        return "notificaciones/form";
    }

    @GetMapping("/editar/{id}")
    public String editarNotificacion(@PathVariable Long id, Model model) {
        model.addAttribute("pageTitle", "Editar Notificación");
        model.addAttribute("tramite", tramiteService.findById(id));
        return "notificaciones/form";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarNotificacion(@PathVariable Long id) {
        tramiteService.eliminar(id);
        return "redirect:/notificaciones";
    }
}