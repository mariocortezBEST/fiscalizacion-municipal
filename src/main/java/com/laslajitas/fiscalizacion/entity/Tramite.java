package com.laslajitas.fiscalizacion.entity;

import com.laslajitas.fiscalizacion.enums.EstadoTramite;
import com.laslajitas.fiscalizacion.enums.TipoTramite;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.math.BigDecimal;

/**
 * Entidad central del sistema. Representa cualquier tipo de trámite municipal.
 * Los campos están agrupados por proceso funcional según el documento de
 * delimitación de proyecto (Proceso 3).
 *
 * Como el modelo unifica todos los tipos en una sola tabla, los campos
 * específicos de cada proceso serán null para los otros tipos de trámite.
 */
@Entity
@Table(name = "tramites")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tramite {

    // ─── Campos comunes a todos los procesos ──────────────────────────────────

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoTramite tipo;

    @Column(nullable = false)
    private String solicitante;

    @Column(length = 20)
    private String dni;

    @Column(length = 100)
    private String email;

    @Column(length = 20)
    private String telefono;

    @Column(length = 100)
    private String localidad;

    @Column(length = 500)
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoTramite estado;

    /** Fecha de ingreso del trámite al sistema */
    @Column(nullable = false)
    private LocalDate fecha;

    /** Fecha de vencimiento de la habilitación o permiso emitido */
    private LocalDate fechaVencimiento;

    // ─── Proceso 1: Habilitación Comercial ────────────────────────────────────

    @Column(length = 255)
    private String direccionLocal;

    @Column(length = 50)
    private String dimensiones;

    /** true = Propietario, false = Inquilino */
    private Boolean esPropietario;

    private Boolean tieneElectricidad;

    private Boolean tieneGas;

    /**
     * Resultado de la validación con Obras Públicas.
     * El sistema solo registra si es Apto o No Apto; la validación técnica
     * la realiza Obras Públicas externamente (límite del sistema).
     */
    private Boolean aptoObraPublica;

    /**
     * Indica si el contribuyente tiene deuda en Rentas.
     * El sistema consulta pero NO realiza el cobro (límite con Rentas).
     */
    private Boolean tieneDeudaRentas;

    /** Número de expediente o legajo comercial */
    @Column(length = 50)
    private String numeroExpediente;

    /** Rubro o actividad comercial declarada */
    @Column(length = 100)
    private String rubro;

    // ─── Proceso 2: Habilitación por Venta de Alcohol ─────────────────────────

    @Column(length = 50)
    private String categoria;

    @Column(length = 100)
    private String horarioVenta;

    /**
     * Fecha en que el expediente salió hacia la Policía Provincial.
     * Todo lo que ocurre desde esta fecha hasta el retorno es "caja negra"
     * (límite crítico del sistema).
     */
    private LocalDate fechaSalidaPolicia;

    /**
     * Fecha en que el expediente retornó de la Policía Provincial
     * con la firma del Jefe.
     */
    private LocalDate fechaRetornoPolicia;

    /**
     * Días transcurridos desde que el expediente salió hacia Policía.
     * Se calcula dinámicamente; se persiste para consultas rápidas.
     * Alerta crítica si supera los 90 días.
     */
    private Integer diasEnPolicia;

    /**
     * Resultado de la Policía Provincial.
     * true = aprobado, false = rechazado, null = pendiente de respuesta.
     */
    private Boolean resultadoPolicia;

    /**
     * Vigencia del certificado antisiniestral de Bomberos.
     * El sistema NO emite este certificado; solo registra su vigencia
     * (límite con Bomberos).
     */
    private LocalDate vencimientoCertificadoBomberos;

    // ─── Proceso 3: Habilitación de Eventos ───────────────────────────────────

    /** Fecha en que se realizará el evento */
    private LocalDate fechaEvento;

    /** Lugar donde se realizará el evento */
    @Column(length = 255)
    private String lugarEvento;

    /** Capacidad máxima de personas (aforo), dato provisto por Bomberos */
    private Integer aforoMaximo;

    /**
     * Resultado de la inspección de Bomberos para el evento.
     * El sistema NO realiza la inspección; solo registra el resultado.
     */
    private Boolean aptoEventoBomberos;

    /**
     * Cantidad de policías adicionales exigidos para el evento.
     * El sistema registra el requisito; el contrato es externo (límite con Policía).
     */
    private Integer cantidadPoliciaAdicional;

    /**
     * Indica si el organizador pagó los derechos de autor (SADAIC/AADI CAPIF).
     * El sistema puede recordar el requisito pero NO emite boletas de SADAIC
     * (límite externo).
     */
    private Boolean pagaSadaic;

    // ─── Proceso 4: Multas ────────────────────────────────────────────────────

    /** Número del acta de infracción labrada por el inspector en calle */
    @Column(length = 50)
    private String numeroActa;

    /**
     * Artículo de la ordenanza municipal bajo el cual se tipifica la infracción.
     */
    @Column(length = 100)
    private String articuloOrdenanza;

    /**
     * Monto base sugerido según la ordenanza.
     * El monto final lo define el Juez de Faltas (límite con Tribunal).
     */
    @Column(precision = 10, scale = 2)
    private BigDecimal monto;

    /**
     * Monto final confirmado por el Juez de Faltas.
     * Puede diferir del monto sugerido (el Juez puede atenuar o perdonar).
     */
    @Column(precision = 10, scale = 2)
    private BigDecimal montoFinalJuez;

    /** Fecha en que el expediente fue derivado al Tribunal de Faltas */
    private LocalDate fechaDerivacionJuzgado;

    /**
     * Descripción de la evidencia registrada (fotos, testigos, etc.).
     * La gestión del archivo físico de evidencia es externa al sistema.
     */
    @Column(length = 500)
    private String evidencia;

    // ─── Proceso 5: Notificaciones Oficiales ──────────────────────────────────

    /** Número de cédula de notificación generada */
    @Column(length = 50)
    private String numeroCedula;

    /** Nombre del agente notificador municipal asignado */
    @Column(length = 100)
    private String notificador;

    /**
     * Fecha en que el notificador realizó el intento de entrega.
     * A partir de esta fecha comienza el conteo del plazo administrativo.
     */
    private LocalDate fechaDiligenciamiento;

    /**
     * Resultado de la entrega: true = entregada, false = no se pudo entregar.
     */
    private Boolean entregada;

    /**
     * Observaciones del notificador sobre el resultado de la entrega
     * (ej: "Domicilio cerrado", "Se negó a firmar", "Recibió conforme").
     */
    @Column(length = 300)
    private String observacionesEntrega;

    /**
     * Fecha límite para que el notificado responda o cumpla.
     * El sistema genera alerta cuando esta fecha se supera sin respuesta
     * (estado PLAZO_VENCIDO).
     */
    private LocalDate fechaLimitePlazo;

    /**
     * Si el municipio envía carta documento por Correo Argentino,
     * se registra el número de seguimiento. El sistema NO integra con
     * el sistema de logística del Correo (límite externo).
     */
    @Column(length = 50)
    private String numeroSeguimientoCorreo;
}