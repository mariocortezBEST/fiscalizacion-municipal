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
@RequestMapping("/eventos")
public class EventosController {

    @Autowired
    private TramiteRepository tramiteRepository;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("pageTitle", "Permisos de Eventos");
        model.addAttribute("eventos", tramiteRepository.findAll().stream()
                .filter(t -> t.getTipo() == TipoTramite.EVENTO)
                .collect(Collectors.toList()));
        return "eventos/index";
    }

    @GetMapping("/nuevo")
    public String nuevoEvento(Model model) {
        model.addAttribute("pageTitle", "Nuevo Permiso de Evento");
        model.addAttribute("tramite", new Tramite());
        return "eventos/form";
    }

    @PostMapping("/guardar")
    public String guardarEvento(Tramite tramite) {
        tramite.setTipo(TipoTramite.EVENTO);

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
        return "redirect:/eventos";
    }

    @GetMapping("/ver/{id}")
    public String verEvento(@PathVariable Long id, Model model) {
        model.addAttribute("pageTitle", "Ver Permiso de Evento");
        model.addAttribute("tramite", tramiteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid tramite Id:" + id)));
        model.addAttribute("readonly", true);
        return "eventos/form";
    }

    @GetMapping("/editar/{id}")
    public String editarEvento(@PathVariable Long id, Model model) {
        model.addAttribute("pageTitle", "Editar Permiso de Evento");
        model.addAttribute("tramite", tramiteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid tramite Id:" + id)));
        return "eventos/form";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarEvento(@PathVariable Long id) {
        Tramite tramite = tramiteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid tramite Id:" + id));
        tramiteRepository.delete(tramite);
        return "redirect:/eventos";
    }
}
