package es.iesjandula.reaktor.issues_server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que representa los datos necesarios para crear una incidencia.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CrearIncidenciaUsuarioDto 
{
    /**
     * Aula en la que ocurre la incidencia.
     */
    private String ubicacion;

    /**
     * Descripción de la incidencia.
     */
    private String descripcion;

    /**
     * Categoría asignada a la incidencia.
     * Esta categoría determina automáticamente los responsables destino.
     */
    private String nombreCategoria;
}
