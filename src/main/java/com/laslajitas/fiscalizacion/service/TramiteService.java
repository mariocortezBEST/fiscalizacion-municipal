package com.laslajitas.fiscalizacion.service;

import com.laslajitas.fiscalizacion.model.EstadoTramite;
import com.laslajitas.fiscalizacion.model.TipoTramite;
import com.laslajitas.fiscalizacion.model.Tramite;
import com.laslajitas.fiscalizacion.repository.TramiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TramiteService {

    @Autowired
    private TramiteRepository tramitesRepository;

    public long countHabilitacionesPendientes() {
        // Asumimos que "Habilitaciones" son de tipo COMERCIAL y estado
        // INSPECCION_PENDIENTE o PENDIENTE
        // Para simplificar según el dashboard, contaremos INSPECCION_PENDIENTE
        return tramitesRepository.countByEstado(EstadoTramite.INSPECCION_PENDIENTE);
    }

    public long countTramitesAlcoholDemorados() {
        return tramitesRepository.countByTipoAndEstado(TipoTramite.ALCOHOL, EstadoTramite.EN_POLICIA);
    }

    public long countEventosProximos() {
        return tramitesRepository.findProximosEventos().size();
    }

    public Tramite getProximoEvento() {
        List<Tramite> eventos = tramitesRepository.findProximosEventos();
        return eventos.isEmpty() ? null : eventos.get(0);
    }

    public List<Tramite> getRecentActivity() {
        return tramitesRepository.findTop5ByOrderByFechaDesc();
    }

    public void verificarYCrearNotificacion(Tramite tramite) {
        if (tramite.getEstado() == EstadoTramite.FINALIZADO) {
            Tramite notificacion = new Tramite();
            notificacion.setTipo(TipoTramite.NOTIFICACION);
            notificacion.setSolicitante(tramite.getSolicitante());
            notificacion.setDni(tramite.getDni());
            notificacion.setEmail(tramite.getEmail());
            notificacion.setTelefono(tramite.getTelefono());
            notificacion.setLocalidad(tramite.getLocalidad());
            notificacion.setDescripcion("Su trámite de tipo " + tramite.getTipo()
                    + " ha finalizado. Por favor pase a retirar su certificado.");
            notificacion.setEstado(EstadoTramite.PENDIENTE);
            notificacion.setFecha(java.time.LocalDate.now());
            tramitesRepository.save(notificacion);
        }
    }
}
