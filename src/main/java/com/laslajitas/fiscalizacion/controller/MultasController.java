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
@RequestMapping("/multas")
public class MultasController {

    @Autowired
    private TramiteRepository tramiteRepository;

    @Autowired
    private com.laslajitas.fiscalizacion.service.TramiteService tramiteService;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("pageTitle", "Multas y Sanciones");
        model.addAttribute("multas", tramiteRepository.findAll().stream()
                .filter(t -> t.getTipo() == TipoTramite.MULTA)
                .collect(Collectors.toList()));
        return "multas/index";
    }

    @GetMapping("/nueva")
    public String nuevaMulta(Model model) {
        model.addAttribute("pageTitle", "Nueva Multa");
        model.addAttribute("tramite", new Tramite());
        return "multas/form";
    }

    @PostMapping("/guardar")
    public String guardarMulta(Tramite tramite) {
        tramite.setTipo(TipoTramite.MULTA);

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
        tramiteService.verificarYCrearNotificacion(tramite);
        return "redirect:/multas";
    }

    @GetMapping("/ver/{id}")
    public String verMulta(@PathVariable Long id, Model model) {
        model.addAttribute("pageTitle", "Ver Multa");
        model.addAttribute("tramite", tramiteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid tramite Id:" + id)));
        model.addAttribute("readonly", true);
        return "multas/form";
    }

    @GetMapping("/editar/{id}")
    public String editarMulta(@PathVariable Long id, Model model) {
        model.addAttribute("pageTitle", "Editar Multa");
        model.addAttribute("tramite", tramiteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid tramite Id:" + id)));
        return "multas/form";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarMulta(@PathVariable Long id) {
        Tramite tramite = tramiteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid tramite Id:" + id));
        tramiteRepository.delete(tramite);
        return "redirect:/multas";
    }
}
