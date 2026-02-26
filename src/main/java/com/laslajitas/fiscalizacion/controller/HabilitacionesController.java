package com.laslajitas.fiscalizacion.controller;

import com.laslajitas.fiscalizacion.enums.TipoTramite;
import com.laslajitas.fiscalizacion.entity.Tramite;
import com.laslajitas.fiscalizacion.service.TramiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/habilitaciones")
public class HabilitacionesController {

    @Autowired
    private TramiteService tramiteService;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("pageTitle", "Habilitaciones Comerciales");
        model.addAttribute("habilitaciones", tramiteService.findByTipo(TipoTramite.COMERCIAL));
        return "habilitaciones/index";
    }

    @GetMapping("/nueva")
    public String nuevaHabilitacion(Model model) {
        model.addAttribute("pageTitle", "Nueva Habilitación");
        model.addAttribute("tramite", new Tramite());
        return "habilitaciones/form";
    }

    @PostMapping("/guardar")
    public String guardarHabilitacion(Tramite tramite) {
        tramiteService.guardarHabilitacion(tramite);
        return "redirect:/habilitaciones";
    }

    @GetMapping("/ver/{id}")
    public String verHabilitacion(@PathVariable Long id, Model model) {
        model.addAttribute("pageTitle", "Ver Habilitación");
        model.addAttribute("tramite", tramiteService.findById(id));
        model.addAttribute("readonly", true);
        return "habilitaciones/form";
    }

    @GetMapping("/editar/{id}")
    public String editarHabilitacion(@PathVariable Long id, Model model) {
        model.addAttribute("pageTitle", "Editar Habilitación");
        model.addAttribute("tramite", tramiteService.findById(id));
        return "habilitaciones/form";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarHabilitacion(@PathVariable Long id) {
        tramiteService.eliminar(id);
        return "redirect:/habilitaciones";
    }
}