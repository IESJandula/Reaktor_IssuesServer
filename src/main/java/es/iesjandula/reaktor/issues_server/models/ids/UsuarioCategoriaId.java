package es.iesjandula.reaktor.issues_server.models.ids;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Identificador compuesto para la entidad UsuarioCategoria.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
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
	 * Email del responsable.
	 */
    private String emailResponsable;
}
