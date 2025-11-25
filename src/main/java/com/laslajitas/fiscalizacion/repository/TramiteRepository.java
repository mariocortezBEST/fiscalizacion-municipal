package com.laslajitas.fiscalizacion.repository;

import com.laslajitas.fiscalizacion.model.EstadoTramite;
import com.laslajitas.fiscalizacion.model.TipoTramite;
import com.laslajitas.fiscalizacion.model.Tramite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TramiteRepository extends JpaRepository<Tramite, Long> {

    long countByEstado(EstadoTramite estado);

    long countByTipoAndEstado(TipoTramite tipo, EstadoTramite estado);

    @Query("SELECT t FROM Tramite t WHERE t.tipo = 'EVENTO' AND t.fecha >= CURRENT_DATE ORDER BY t.fecha ASC")
    List<Tramite> findProximosEventos();

    List<Tramite> findTop5ByOrderByFechaDesc();
}
