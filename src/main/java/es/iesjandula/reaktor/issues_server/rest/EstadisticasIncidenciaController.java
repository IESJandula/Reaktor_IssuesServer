package es.iesjandula.reaktor.issues_server.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.iesjandula.reaktor.base.utils.BaseConstants;
import es.iesjandula.reaktor.issues_server.dtos.EstadisticasCategoriaDto;
import es.iesjandula.reaktor.issues_server.dtos.EstadisticasEstadoDto;
import es.iesjandula.reaktor.issues_server.dtos.EstadisticasUbicacionDto;
import es.iesjandula.reaktor.issues_server.repository.IIncidenciaRepository;
import es.iesjandula.reaktor.issues_server.utils.Constants;
import es.iesjandula.reaktor.issues_server.utils.IssuesServerError;

@RequestMapping("/issues/estadisticas")
@RestController
public class EstadisticasIncidenciaController
{
	private static final Logger log = LoggerFactory.getLogger(EstadisticasIncidenciaController.class);

	@Autowired
	private IIncidenciaRepository incidenciaRepository;

	@PreAuthorize("hasRole('" + BaseConstants.ROLE_PROFESOR + "')")
	@GetMapping("/por-categoria")
	public ResponseEntity<?> obtenerEstadisticasPorCategoria()
	{
		try
		{
			log.info("Petición para obtener estadísticas de incidencias por categoría");

			List<EstadisticasCategoriaDto> resultados = incidenciaRepository.obtenerEstadisticasPorCategoria();

			return ResponseEntity.ok(resultados);
		}
		catch (Exception exception)
		{
			String mensajeError = "Error inesperado al obtener estadísticas por categoría";
			log.error(mensajeError, exception);

			IssuesServerError issuesError = new IssuesServerError(Constants.ERR_GENERICO_CODE, mensajeError, exception);
			return ResponseEntity.status(500).body(issuesError.getBodyErrorMessage());
		}
	}

	@PreAuthorize("hasRole('" + BaseConstants.ROLE_PROFESOR + "')")
	@GetMapping("/por-estado")
	public ResponseEntity<?> obtenerEstadisticasPorEstado()
	{
		try
		{
			log.info("Petición para obtener estadísticas de incidencias por estado");

			List<EstadisticasEstadoDto> resultados = incidenciaRepository.obtenerEstadisticasPorEstado();

			return ResponseEntity.ok(resultados);
		}
		catch (Exception exception)
		{
			String mensajeError = "Error inesperado al obtener estadísticas por estado";
			log.error(mensajeError, exception);

			IssuesServerError issuesError = new IssuesServerError(Constants.ERR_GENERICO_CODE, mensajeError, exception);
			return ResponseEntity.status(500).body(issuesError.getBodyErrorMessage());
		}
	}

	@PreAuthorize("hasRole('" + BaseConstants.ROLE_PROFESOR + "')")
	@GetMapping("/por-ubicacion")
	public ResponseEntity<?> obtenerEstadisticasPorUbicacion()
	{
		try
		{
			log.info("Petición para obtener estadísticas de incidencias por ubicación");

			List<EstadisticasUbicacionDto> resultados = incidenciaRepository.obtenerEstadisticasPorUbicacion();

			return ResponseEntity.ok(resultados);
		}
		catch (Exception exception)
		{
			String mensajeError = "Error inesperado al obtener estadísticas por ubicación";
			log.error(mensajeError, exception);

			IssuesServerError issuesError = new IssuesServerError(Constants.ERR_GENERICO_CODE, mensajeError, exception);
			return ResponseEntity.status(500).body(issuesError.getBodyErrorMessage());
		}
	}
}