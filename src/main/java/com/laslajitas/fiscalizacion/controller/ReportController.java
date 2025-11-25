package com.laslajitas.fiscalizacion.controller;

import com.laslajitas.fiscalizacion.model.EstadoTramite;
import com.laslajitas.fiscalizacion.model.TipoTramite;
import com.laslajitas.fiscalizacion.repository.TramiteRepository;
import com.laslajitas.fiscalizacion.service.JasperReportService;
import com.laslajitas.fiscalizacion.service.QRCodeService;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/reportes")
public class ReportController {

        @Autowired
        private JasperReportService jasperReportService;

        @Autowired
        private TramiteRepository tramiteRepository;

        @Autowired
        private QRCodeService qrCodeService;

        // ============ HABILITACIONES ENDPOINTS ============

        @GetMapping("/exportar/habilitaciones")
        public ResponseEntity<byte[]> exportarHabilitaciones(@RequestParam(defaultValue = "pdf") String format)
                        throws FileNotFoundException, JRException {
                Map<String, Object> parameters = new HashMap<>();
                parameters.put("createdBy", "Fiscalizacion Municipal");

                var habilitaciones = tramiteRepository.findAll().stream()
                                .filter(t -> t.getTipo() == TipoTramite.COMERCIAL)
                                .collect(Collectors.toList());

                byte[] reportContent = jasperReportService.exportReport("habilitaciones", parameters, habilitaciones,
                                format);

                HttpHeaders headers = new HttpHeaders();
                headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=habilitaciones." + format);

                return ResponseEntity.ok()
                                .headers(headers)
                                .contentType(MediaType.APPLICATION_PDF)
                                .body(reportContent);
        }

        @GetMapping("/exportar/habilitaciones/{id}")
        public ResponseEntity<byte[]> exportarHabilitacion(@PathVariable Long id,
                        @RequestParam(defaultValue = "pdf") String format) throws FileNotFoundException, JRException {
                Map<String, Object> parameters = new HashMap<>();
                parameters.put("createdBy", "Fiscalizacion Municipal");

                var tramite = tramiteRepository.findById(id)
                                .orElseThrow(() -> new IllegalArgumentException("Invalid tramite Id:" + id));

                byte[] reportContent = jasperReportService.exportReport("habilitacion_detalle", parameters,
                                java.util.Collections.singletonList(tramite), format);

                HttpHeaders headers = new HttpHeaders();
                headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=habilitacion_" + id + "." + format);

                return ResponseEntity.ok()
                                .headers(headers)
                                .contentType(MediaType.APPLICATION_PDF)
                                .body(reportContent);
        }

        @GetMapping("/certificado/habilitaciones/{id}")
        public ResponseEntity<byte[]> generarCertificado(@PathVariable Long id) throws Exception {
                var tramite = tramiteRepository.findById(id)
                                .orElseThrow(() -> new IllegalArgumentException("Invalid tramite Id:" + id));

                if (tramite.getEstado() != EstadoTramite.FINALIZADO) {
                        throw new IllegalStateException(
                                        "Solo se pueden generar certificados para habilitaciones finalizadas");
                }

                String verificationUrl = "http://localhost:8080/verificar/" + id;
                byte[] qrCodeBytes = qrCodeService.generateQRCodeImage(verificationUrl, 200, 200);
                ByteArrayInputStream qrCodeStream = new ByteArrayInputStream(qrCodeBytes);

                Map<String, Object> parameters = new HashMap<>();
                parameters.put("qrCodeImage", qrCodeStream);

                byte[] certificateContent = jasperReportService.exportReport("certificado_habilitacion", parameters,
                                java.util.Collections.singletonList(tramite), "pdf");

                HttpHeaders headers = new HttpHeaders();
                headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=certificado_" + id + ".pdf");

                return ResponseEntity.ok()
                                .headers(headers)
                                .contentType(MediaType.APPLICATION_PDF)
                                .body(certificateContent);
        }

        // ============ ALCOHOL ENDPOINTS ============

        @GetMapping("/exportar/alcohol")
        public ResponseEntity<byte[]> exportarAlcohol(@RequestParam(defaultValue = "pdf") String format)
                        throws FileNotFoundException, JRException {
                Map<String, Object> parameters = new HashMap<>();
                parameters.put("createdBy", "Fiscalizacion Municipal");

                var alcoholes = tramiteRepository.findAll().stream()
                                .filter(t -> t.getTipo() == TipoTramite.ALCOHOL)
                                .collect(Collectors.toList());

                byte[] reportContent = jasperReportService.exportReport("alcohol", parameters, alcoholes, format);

                HttpHeaders headers = new HttpHeaders();
                headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=alcohol." + format);

                return ResponseEntity.ok()
                                .headers(headers)
                                .contentType(MediaType.APPLICATION_PDF)
                                .body(reportContent);
        }

        @GetMapping("/exportar/alcohol/{id}")
        public ResponseEntity<byte[]> exportarLicenciaAlcohol(@PathVariable Long id,
                        @RequestParam(defaultValue = "pdf") String format) throws FileNotFoundException, JRException {
                Map<String, Object> parameters = new HashMap<>();
                parameters.put("createdBy", "Fiscalizacion Municipal");

                var tramite = tramiteRepository.findById(id)
                                .orElseThrow(() -> new IllegalArgumentException("Invalid tramite Id:" + id));

                byte[] reportContent = jasperReportService.exportReport("alcohol_detalle", parameters,
                                java.util.Collections.singletonList(tramite), format);

                HttpHeaders headers = new HttpHeaders();
                headers.set(HttpHeaders.CONTENT_DISPOSITION,
                                "attachment; filename=licencia_alcohol_" + id + "." + format);

                return ResponseEntity.ok()
                                .headers(headers)
                                .contentType(MediaType.APPLICATION_PDF)
                                .body(reportContent);
        }

        @GetMapping("/certificado/alcohol/{id}")
        public ResponseEntity<byte[]> generarCertificadoAlcohol(@PathVariable Long id) throws Exception {
                var tramite = tramiteRepository.findById(id)
                                .orElseThrow(() -> new IllegalArgumentException("Invalid tramite Id:" + id));

                if (tramite.getEstado() != EstadoTramite.FINALIZADO) {
                        throw new IllegalStateException(
                                        "Solo se pueden generar certificados para licencias finalizadas");
                }

                String verificationUrl = "http://localhost:8080/verificar/" + id;
                byte[] qrCodeBytes = qrCodeService.generateQRCodeImage(verificationUrl, 200, 200);
                ByteArrayInputStream qrCodeStream = new ByteArrayInputStream(qrCodeBytes);

                Map<String, Object> parameters = new HashMap<>();
                parameters.put("qrCodeImage", qrCodeStream);

                byte[] certificateContent = jasperReportService.exportReport("certificado_alcohol", parameters,
                                java.util.Collections.singletonList(tramite), "pdf");

                HttpHeaders headers = new HttpHeaders();
                headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=certificado_alcohol_" + id + ".pdf");

                return ResponseEntity.ok()
                                .headers(headers)
                                .contentType(MediaType.APPLICATION_PDF)
                                .body(certificateContent);
        }

        // ============ EVENTOS ENDPOINTS ============

        @GetMapping("/exportar/eventos")
        public ResponseEntity<byte[]> exportarEventos(@RequestParam(defaultValue = "pdf") String format)
                        throws FileNotFoundException, JRException {
                Map<String, Object> parameters = new HashMap<>();
                parameters.put("createdBy", "Fiscalizacion Municipal");

                var eventos = tramiteRepository.findAll().stream()
                                .filter(t -> t.getTipo() == TipoTramite.EVENTO)
                                .collect(Collectors.toList());

                byte[] reportContent = jasperReportService.exportReport("eventos", parameters, eventos, format);

                HttpHeaders headers = new HttpHeaders();
                headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=eventos." + format);

                return ResponseEntity.ok()
                                .headers(headers)
                                .contentType(MediaType.APPLICATION_PDF)
                                .body(reportContent);
        }

        @GetMapping("/exportar/eventos/{id}")
        public ResponseEntity<byte[]> exportarEvento(@PathVariable Long id,
                        @RequestParam(defaultValue = "pdf") String format) throws FileNotFoundException, JRException {
                Map<String, Object> parameters = new HashMap<>();
                parameters.put("createdBy", "Fiscalizacion Municipal");

                var tramite = tramiteRepository.findById(id)
                                .orElseThrow(() -> new IllegalArgumentException("Invalid tramite Id:" + id));

                byte[] reportContent = jasperReportService.exportReport("eventos_detalle", parameters,
                                java.util.Collections.singletonList(tramite), format);

                HttpHeaders headers = new HttpHeaders();
                headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=evento_" + id + "." + format);

                return ResponseEntity.ok()
                                .headers(headers)
                                .contentType(MediaType.APPLICATION_PDF)
                                .body(reportContent);
        }

        @GetMapping("/certificado/eventos/{id}")
        public ResponseEntity<byte[]> generarCertificadoEvento(@PathVariable Long id) throws Exception {
                var tramite = tramiteRepository.findById(id)
                                .orElseThrow(() -> new IllegalArgumentException("Invalid tramite Id:" + id));

                if (tramite.getEstado() != EstadoTramite.FINALIZADO) {
                        throw new IllegalStateException(
                                        "Solo se pueden generar certificados para permisos finalizados");
                }

                String verificationUrl = "http://localhost:8080/verificar/" + id;
                byte[] qrCodeBytes = qrCodeService.generateQRCodeImage(verificationUrl, 200, 200);
                ByteArrayInputStream qrCodeStream = new ByteArrayInputStream(qrCodeBytes);

                Map<String, Object> parameters = new HashMap<>();
                parameters.put("qrCodeImage", qrCodeStream);

                byte[] certificateContent = jasperReportService.exportReport("certificado_eventos", parameters,
                                java.util.Collections.singletonList(tramite), "pdf");

                HttpHeaders headers = new HttpHeaders();
                headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=certificado_evento_" + id + ".pdf");

                return ResponseEntity.ok()
                                .headers(headers)
                                .contentType(MediaType.APPLICATION_PDF)
                                .body(certificateContent);
        }

	// ============ MULTAS ENDPOINTS ============

	@GetMapping("/exportar/multas")
	public ResponseEntity<byte[]> exportarMultas(@RequestParam(defaultValue = "pdf") String format)
			throws FileNotFoundException, JRException {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("createdBy", "Fiscalizacion Municipal");

		var multas = tramiteRepository.findAll().stream()
				.filter(t -> t.getTipo() == TipoTramite.MULTA)
				.collect(Collectors.toList());

		byte[] reportContent = jasperReportService.exportReport("multas", parameters, multas, format);

		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=multas." + format);

		return ResponseEntity.ok()
				.headers(headers)
				.contentType(MediaType.APPLICATION_PDF)
				.body(reportContent);
	}

	@GetMapping("/exportar/multas/{id}")
	public ResponseEntity<byte[]> exportarMulta(@PathVariable Long id,
			@RequestParam(defaultValue = "pdf") String format) throws FileNotFoundException, JRException {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("createdBy", "Fiscalizacion Municipal");

		var tramite = tramiteRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid tramite Id:" + id));

		byte[] reportContent = jasperReportService.exportReport("multas_detalle", parameters,
				java.util.Collections.singletonList(tramite), format);

		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=multa_" + id + "." + format);

		return ResponseEntity.ok()
				.headers(headers)
				.contentType(MediaType.APPLICATION_PDF)
				.body(reportContent);
	}

	@GetMapping("/certificado/multas/{id}")
	public ResponseEntity<byte[]> generarComprobanteMulta(@PathVariable Long id) throws Exception {
		var tramite = tramiteRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid tramite Id:" + id));

		if (tramite.getEstado() != EstadoTramite.FINALIZADO) {
			throw new IllegalStateException("Solo se pueden generar comprobantes para multas pagadas");
		}

		String verificationUrl = "http://localhost:8080/verificar/" + id;
		byte[] qrCodeBytes = qrCodeService.generateQRCodeImage(verificationUrl, 200, 200);
		ByteArrayInputStream qrCodeStream = new ByteArrayInputStream(qrCodeBytes);

		Map<String, Object> parameters = new HashMap<>();
		parameters.put("qrCodeImage", qrCodeStream);

		byte[] certificateContent = jasperReportService.exportReport("certificado_multas", parameters,
				java.util.Collections.singletonList(tramite), "pdf");

		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=comprobante_multa_" + id + ".pdf");

		return ResponseEntity.ok()
				.headers(headers)
				.contentType(MediaType.APPLICATION_PDF)
				.body(certificateContent);
	}
}