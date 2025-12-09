package es.iesjandula.reaktor.issues_server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que representa un usuario y una categoría.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioCategoriaDto
{
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
