package es.iesjandula.reaktor.issues_server.models.ids;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Identificador compuesto para la entidad UsuarioCategoria.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioCategoriaId implements Serializable
{
    /**
	 * SerialVersionUID para la serialización.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Nombre de la categoría.
	 */	
	private String nombreCategoria;

	/**
	 * Nombre del responsable.
	 */
	private String nombreResponsable;

	/**
	 * Email del responsable.
	 */
    private String emailResponsable;
}
