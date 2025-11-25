package com.laslajitas.fiscalizacion;

import com.laslajitas.fiscalizacion.model.EstadoTramite;
import com.laslajitas.fiscalizacion.model.TipoTramite;
import com.laslajitas.fiscalizacion.model.Tramite;
import com.laslajitas.fiscalizacion.repository.TramiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;
import java.math.BigDecimal;

@Component
public class DataInitializer implements CommandLineRunner {

        @Autowired
        private TramiteRepository tramiteRepository;

        @Override
        public void run(String... args) throws Exception {
                if (tramiteRepository.count() == 0) {
                        Tramite t1 = new Tramite(null, TipoTramite.COMERCIAL, "Juan Pérez (Kiosco El Paso)", null, null,
                                        null, null,
                                        "Habilitación comercial", EstadoTramite.INSPECCION_PENDIENTE,
                                        LocalDate.now().minusDays(2), null, "Av. San Martín 123", "40m2", false, true,
                                        true, null);
                        Tramite t2 = new Tramite(null, TipoTramite.ALCOHOL, "Bar La Esquina", null, null, null, null,
                                        "Renovación licencia alcohol",
                                        EstadoTramite.EN_POLICIA, LocalDate.now().minusDays(4), null, null, null, null,
                                        null, null, null);

                        Tramite t3 = new Tramite(null, TipoTramite.MULTA, "Terreno Baldío (Calle San Martín 400)", null,
                                        null, null, null,
                                        "Multa por falta de limpieza", EstadoTramite.VENCIDA,
                                        LocalDate.now().minusDays(9),
                                        LocalDate.now().minusDays(2), null, null, null, null, null,
                                        BigDecimal.valueOf(15000.00));
                        Tramite t4 = new Tramite(null, TipoTramite.EVENTO, "Festival del Maíz", null, null, null, null,
                                        "Permiso para evento masivo",
                                        EstadoTramite.PENDIENTE, LocalDate.of(2025, 12, 15), null, null, null, null,
                                        null, null, null);
                        Tramite t5 = new Tramite(null, TipoTramite.COMERCIAL, "Librería Central", null, null, null,
                                        null, "Renovación anual",
                                        EstadoTramite.FINALIZADO, LocalDate.now().minusDays(1), null,
                                        "Calle Belgrano 45", "80m2", true, true, true, null);

                        // Add more dummy data to fill counts
                        Tramite t6 = new Tramite(null, TipoTramite.COMERCIAL, "Despensa Los Amigos", null, null, null,
                                        null,
                                        "Habilitación nueva",
                                        EstadoTramite.INSPECCION_PENDIENTE, LocalDate.now(), null, "Barrio Obrero Mz C",
                                        "30m2", true, true, false, null);
                        Tramite t7 = new Tramite(null, TipoTramite.ALCOHOL, "Vinería El Tonel", null, null, null, null,
                                        "Licencia nueva",
                                        EstadoTramite.EN_POLICIA, LocalDate.now().minusDays(35), null, null, null, null,
                                        null, null, null); // Demorado
                        Tramite t8 = new Tramite(null, TipoTramite.ALCOHOL, "Club Social", null, null, null, null,
                                        "Permiso eventual",
                                        EstadoTramite.EN_POLICIA, LocalDate.now().minusDays(32), null, null, null, null,
                                        null, null, null); // Demorado
                        Tramite t9 = new Tramite(null, TipoTramite.ALCOHOL, "Restaurante El Sol", null, null, null,
                                        null, "Renovación",
                                        EstadoTramite.EN_POLICIA, LocalDate.now().minusDays(5), null, null, null, null,
                                        null, null, null);
                        Tramite t10 = new Tramite(null, TipoTramite.EVENTO, "Fiesta de la Cerveza", null, null, null,
                                        null, "Evento privado",
                                        EstadoTramite.PENDIENTE, LocalDate.of(2025, 12, 20), null, null, null, null,
                                        null, null, null);
                        Tramite t11 = new Tramite(null, TipoTramite.EVENTO, "Feria de Artesanos", null, null, null,
                                        null,
                                        "Uso de espacio público",
                                        EstadoTramite.PENDIENTE, LocalDate.of(2025, 12, 22), null, null, null, null,
                                        null, null, null);
                        Tramite t12 = new Tramite(null, TipoTramite.NOTIFICACION, "Supermercado El Ahorro", null, null,
                                        null, null,
                                        "Notificación por ruidos molestos",
                                        EstadoTramite.FINALIZADO, LocalDate.now().minusDays(10), null,
                                        "Av. San Martín 800", null, null, null, null, null);
                        Tramite t13 = new Tramite(null, TipoTramite.NOTIFICACION, "Taller Mecánico Tito", null, null,
                                        null, null,
                                        "Aviso de vencimiento de habilitación",
                                        EstadoTramite.INSPECCION_PENDIENTE, LocalDate.now().minusDays(1), null,
                                        "Calle Salta 200", null, null, null, null, null);

                        tramiteRepository
                                        .saveAll(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13));
                        System.out.println("Datos de prueba inicializados!");
                }
        }
}
