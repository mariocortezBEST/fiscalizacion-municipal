package com.laslajitas.fiscalizacion.controller;

import com.laslajitas.fiscalizacion.service.TramiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/reportes")
public class ReportesController {

    @Autowired
    private TramiteService tramiteService;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("pageTitle", "Reportes y Estadísticas");

        // KPIs
        model.addAttribute("totalTramites", tramiteService.countTramitesByTipo(null));


        long totalComercial = tramiteService
                .countTramitesByTipo(com.laslajitas.fiscalizacion.enums.TipoTramite.COMERCIAL);
        long totalAlcohol = tramiteService.countTramitesByTipo(com.laslajitas.fiscalizacion.enums.TipoTramite.ALCOHOL);
        long totalEventos = tramiteService.countTramitesByTipo(com.laslajitas.fiscalizacion.enums.TipoTramite.EVENTO);
        long totalMultas = tramiteService.countTramitesByTipo(com.laslajitas.fiscalizacion.enums.TipoTramite.MULTA);
        long totalNotificaciones = tramiteService
                .countTramitesByTipo(com.laslajitas.fiscalizacion.enums.TipoTramite.NOTIFICACION);

        model.addAttribute("totalTramites",
                totalComercial + totalAlcohol + totalEventos + totalMultas + totalNotificaciones);
        model.addAttribute("totalRecaudado", tramiteService.sumMontoMultasFinalizadas());
        model.addAttribute("totalPendientes",
                tramiteService.countTramitesByEstado(com.laslajitas.fiscalizacion.enums.EstadoTramite.PENDIENTE));

        // Chart Data (Pie - Types)
        model.addAttribute("countComercial", totalComercial);
        model.addAttribute("countAlcohol", totalAlcohol);
        model.addAttribute("countEventos", totalEventos);
        model.addAttribute("countMultas", totalMultas);
        model.addAttribute("countNotificaciones", totalNotificaciones);

        // Chart Data (Bar - Status)
        model.addAttribute("countPendiente",
                tramiteService.countTramitesByEstado(com.laslajitas.fiscalizacion.enums.EstadoTramite.PENDIENTE));
        model.addAttribute("countEnProceso",
                tramiteService.countTramitesByEstado(com.laslajitas.fiscalizacion.enums.EstadoTramite.EN_PROCESO));
        model.addAttribute("countFinalizado",
                tramiteService.countTramitesByEstado(com.laslajitas.fiscalizacion.enums.EstadoTramite.FINALIZADO));
        model.addAttribute("countVencida",
                tramiteService.countTramitesByEstado(com.laslajitas.fiscalizacion.enums.EstadoTramite.VENCIDA));

        return "reportes/index";
    }
}
