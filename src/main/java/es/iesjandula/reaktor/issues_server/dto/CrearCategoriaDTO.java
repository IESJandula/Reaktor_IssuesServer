package es.iesjandula.reaktor.issues_server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CrearCategoriaDTO {

    /** Nombre de la categoría (ej: "TIC", "DIRECCION", etc.) */
    private String nombreCategoria;

    /** Nombre del responsable */
    private String nombreResponsable;

    /** Correo del responsable */
    private String correoResponsable;
}
