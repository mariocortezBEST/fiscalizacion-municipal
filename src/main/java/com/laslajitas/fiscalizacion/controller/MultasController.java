package com.laslajitas.fiscalizacion.controller;

import com.laslajitas.fiscalizacion.model.TipoTramite;
import com.laslajitas.fiscalizacion.model.Tramite;
import com.laslajitas.fiscalizacion.service.TramiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/multas")
public class MultasController {

    @Autowired
    private TramiteService tramiteService;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("pageTitle", "Multas y Sanciones");
        model.addAttribute("multas", tramiteService.findByTipo(TipoTramite.MULTA));
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
        tramiteService.guardarMulta(tramite);
        return "redirect:/multas";
    }

    @GetMapping("/ver/{id}")
    public String verMulta(@PathVariable Long id, Model model) {
        model.addAttribute("pageTitle", "Ver Multa");
        model.addAttribute("tramite", tramiteService.findById(id));
        model.addAttribute("readonly", true);
        return "multas/form";
    }

    @GetMapping("/editar/{id}")
    public String editarMulta(@PathVariable Long id, Model model) {
        model.addAttribute("pageTitle", "Editar Multa");
        model.addAttribute("tramite", tramiteService.findById(id));
        return "multas/form";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarMulta(@PathVariable Long id) {
        tramiteService.eliminar(id);
        return "redirect:/multas";
    }
}