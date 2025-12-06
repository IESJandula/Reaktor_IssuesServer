package es.iesjandula.reaktor.issues_server.models.ids;

import java.io.Serializable;
import java.time.LocalDateTime;

import es.iesjandula.reaktor.issues_server.models.Incidencia;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que representa un identificador compuesto para la entidad {@link Incidencia}.
 * 
 * <p>
 * Esta clase implementa la interfaz {@link Serializable} y se utiliza para definir un 
 * identificador único que consiste en múltiples atributos: número de aula, email del 
 * docente y fecha de la incidencia. Es utilizada para garantizar la unicidad de las 
 * incidencias en la base de datos.
 * </p>
 * 
 * <p>
 * Se recomienda que esta clase se utilice junto con la clase {@link Incidencia} 
 * para poder identificar de forma única cada incidencia registrada.
 * </p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IncidenciaId implements Serializable
{
	private static final long serialVersionUID = 1L;

	/**
	 * Atributo Identificativo - Aula en la que se da la incidencia.
	 */
	private String ubicacion;

	/**
	 * Atributo Identificativo - Email del docente que informa de la incidencia.
	 */
	private String email;

	/**
	 * Atributo Identificativo - Fecha de creación de la señalación.
	 */
	private LocalDateTime fecha;

}
