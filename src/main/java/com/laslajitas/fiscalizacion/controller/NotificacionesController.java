package com.laslajitas.fiscalizacion.controller;

import com.laslajitas.fiscalizacion.model.TipoTramite;
import com.laslajitas.fiscalizacion.repository.TramiteRepository;
import com.laslajitas.fiscalizacion.model.Tramite;
import com.laslajitas.fiscalizacion.model.EstadoTramite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/notificaciones")
public class NotificacionesController {

    @Autowired
    private TramiteRepository tramiteRepository;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("pageTitle", "Notificaciones");
        model.addAttribute("notificaciones", tramiteRepository.findAll().stream()
                .filter(t -> t.getTipo() == TipoTramite.NOTIFICACION)
                .collect(Collectors.toList()));
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
        tramite.setTipo(TipoTramite.NOTIFICACION);

        if (tramite.getId() != null) {
            Tramite existingTramite = tramiteRepository.findById(tramite.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid tramite Id:" + tramite.getId()));
            tramite.setFecha(existingTramite.getFecha());
        } else {
            if (tramite.getEstado() == null) {
                tramite.setEstado(EstadoTramite.PENDIENTE);
            }
            tramite.setFecha(LocalDate.now());
        }

        tramiteRepository.save(tramite);
        return "redirect:/notificaciones";
    }

    @GetMapping("/ver/{id}")
    public String verNotificacion(@PathVariable Long id, Model model) {
        model.addAttribute("pageTitle", "Ver Notificación");
        model.addAttribute("tramite", tramiteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid tramite Id:" + id)));
        model.addAttribute("readonly", true);
        return "notificaciones/form";
    }

    @GetMapping("/editar/{id}")
    public String editarNotificacion(@PathVariable Long id, Model model) {
        model.addAttribute("pageTitle", "Editar Notificación");
        model.addAttribute("tramite", tramiteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid tramite Id:" + id)));
        return "notificaciones/form";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarNotificacion(@PathVariable Long id) {
        Tramite tramite = tramiteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid tramite Id:" + id));
        tramiteRepository.delete(tramite);
        return "redirect:/notificaciones";
    }
}
