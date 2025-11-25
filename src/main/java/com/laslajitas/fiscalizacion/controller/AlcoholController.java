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
@RequestMapping("/alcohol")
public class AlcoholController {

    @Autowired
    private TramiteRepository tramiteRepository;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("pageTitle", "Licencias de Venta de Alcohol");
        model.addAttribute("alcoholes", tramiteRepository.findAll().stream()
                .filter(t -> t.getTipo() == TipoTramite.ALCOHOL)
                .collect(Collectors.toList()));
        return "alcohol/index";
    }

    @GetMapping("/nueva")
    public String nuevaLicencia(Model model) {
        model.addAttribute("pageTitle", "Nueva Licencia de Alcohol");
        model.addAttribute("tramite", new Tramite());
        return "alcohol/form";
    }

    @PostMapping("/guardar")
    public String guardarLicencia(Tramite tramite) {
        tramite.setTipo(TipoTramite.ALCOHOL);

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
        return "redirect:/alcohol";
    }

    @GetMapping("/ver/{id}")
    public String verLicencia(@PathVariable Long id, Model model) {
        model.addAttribute("pageTitle", "Ver Licencia de Alcohol");
        model.addAttribute("tramite", tramiteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid tramite Id:" + id)));
        model.addAttribute("readonly", true);
        return "alcohol/form";
    }

    @GetMapping("/editar/{id}")
    public String editarLicencia(@PathVariable Long id, Model model) {
        model.addAttribute("pageTitle", "Editar Licencia de Alcohol");
        model.addAttribute("tramite", tramiteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid tramite Id:" + id)));
        return "alcohol/form";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarLicencia(@PathVariable Long id) {
        Tramite tramite = tramiteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid tramite Id:" + id));
        tramiteRepository.delete(tramite);
        return "redirect:/alcohol";
    }
}
