package es.iesjandula.reaktor.issues_server.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModificarIncidenciaResponsableDto
{
	/**
	 * Aula en la que ocurre la incidencia.
	 */
	private String ubicacion;

	/**
	 * Correo del docente que informa de la incidencia.
	 */
	private String emailDocente;

	/**
	 * Estado de la incidencia.
	 */
	private String estado;

	/**
	 * Correo del responsable de la incidencia.
	 */
	private String emailResponsable;

	/**
	 * Comentario de la incidencia.
	 */
	private String comentario;

	/**
	 * Fecha de la incidencia.
	 */
	@JsonFormat(shape = JsonFormat.Shape.ARRAY)
	private LocalDateTime fecha;
}