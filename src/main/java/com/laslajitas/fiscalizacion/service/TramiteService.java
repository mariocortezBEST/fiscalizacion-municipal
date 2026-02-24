package com.laslajitas.fiscalizacion.service;

import com.laslajitas.fiscalizacion.model.EstadoTramite;
import com.laslajitas.fiscalizacion.model.TipoTramite;
import com.laslajitas.fiscalizacion.model.Tramite;
import com.laslajitas.fiscalizacion.repository.TramiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class TramiteService {

    @Autowired
    private TramiteRepository tramitesRepository;

    // ─── Consultas generales ───────────────────────────────────────────────────

    public List<Tramite> findByTipo(TipoTramite tipo) {
        return tramitesRepository.findAll().stream()
                .filter(t -> t.getTipo() == tipo)
                .toList();
    }

    public Tramite findById(Long id) {
        return tramitesRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Trámite no encontrado con id: " + id));
    }

    public void eliminar(Long id) {
        Tramite tramite = findById(id);
        tramitesRepository.delete(tramite);
    }

    // ─── Guardado por tipo ─────────────────────────────────────────────────────

    public Tramite guardarHabilitacion(Tramite tramite) {
        tramite.setTipo(TipoTramite.COMERCIAL);
        if (tramite.getId() != null) {
            tramite.setFecha(findById(tramite.getId()).getFecha());
        } else {
            if (tramite.getEstado() == null) tramite.setEstado(EstadoTramite.INSPECCION_PENDIENTE);
            tramite.setFecha(LocalDate.now());
        }
        Tramite guardado = tramitesRepository.save(tramite);
        verificarYCrearNotificacion(guardado);
        return guardado;
    }

    public Tramite guardarAlcohol(Tramite tramite) {
        tramite.setTipo(TipoTramite.ALCOHOL);
        if (tramite.getId() != null) {
            tramite.setFecha(findById(tramite.getId()).getFecha());
        } else {
            if (tramite.getEstado() == null) tramite.setEstado(EstadoTramite.PENDIENTE);
            tramite.setFecha(LocalDate.now());
            tramite.setFechaVencimiento(LocalDate.now().plusYears(1));
        }
        Tramite guardado = tramitesRepository.save(tramite);
        verificarYCrearNotificacion(guardado);
        return guardado;
    }

    public Tramite guardarEvento(Tramite tramite) {
        tramite.setTipo(TipoTramite.EVENTO);
        if (tramite.getId() != null) {
            tramite.setFecha(findById(tramite.getId()).getFecha());
        } else {
            if (tramite.getEstado() == null) tramite.setEstado(EstadoTramite.PENDIENTE);
            tramite.setFecha(LocalDate.now());
        }
        Tramite guardado = tramitesRepository.save(tramite);
        verificarYCrearNotificacion(guardado);
        return guardado;
    }

    public Tramite guardarMulta(Tramite tramite) {
        tramite.setTipo(TipoTramite.MULTA);
        if (tramite.getId() != null) {
            tramite.setFecha(findById(tramite.getId()).getFecha());
        } else {
            if (tramite.getEstado() == null) tramite.setEstado(EstadoTramite.PENDIENTE);
            tramite.setFecha(LocalDate.now());
        }
        Tramite guardado = tramitesRepository.save(tramite);
        verificarYCrearNotificacion(guardado);
        return guardado;
    }

    public Tramite guardarNotificacion(Tramite tramite) {
        tramite.setTipo(TipoTramite.NOTIFICACION);
        if (tramite.getId() != null) {
            tramite.setFecha(findById(tramite.getId()).getFecha());
        } else {
            if (tramite.getEstado() == null) tramite.setEstado(EstadoTramite.PENDIENTE);
            tramite.setFecha(LocalDate.now());
        }
        return tramitesRepository.save(tramite);
    }

    // ─── Lógica de negocio ─────────────────────────────────────────────────────

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
            notificacion.setFecha(LocalDate.now());
            tramitesRepository.save(notificacion);
        }
    }

    // ─── Estadísticas para dashboard y reportes ────────────────────────────────

    public long countHabilitacionesPendientes() {
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

    public long countTramitesByTipo(TipoTramite tipo) {
        return tramitesRepository.countByTipo(tipo);
    }

    public long countTramitesByEstado(EstadoTramite estado) {
        return tramitesRepository.countByEstado(estado);
    }

    public BigDecimal sumMontoMultasFinalizadas() {
        return tramitesRepository.findAll().stream()
                .filter(t -> t.getTipo() == TipoTramite.MULTA
                        && t.getEstado() == EstadoTramite.FINALIZADO
                        && t.getMonto() != null)
                .map(Tramite::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}