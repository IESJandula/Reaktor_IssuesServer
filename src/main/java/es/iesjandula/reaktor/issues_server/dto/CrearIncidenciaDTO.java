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
public class CrearIncidenciaDTO 
{
    /**
     * Aula en la que ocurre la incidencia.
     */
    private String ubicacion;

    /**
     * Descripción de la incidencia.
     */
    private String descripcionIncidencia;

    /**
     * Categoría asignada a la incidencia.
     * Esta categoría determina automáticamente los responsables destino.
     */
    private String nombreCategoria;


    public void setDescripcionIncidencia(String descripcionIncidencia)
    {
        if (descripcionIncidencia == null || descripcionIncidencia.trim().isEmpty())
        {
            throw new IllegalArgumentException("La descripción no debería estar vacía");
        }

        this.descripcionIncidencia = descripcionIncidencia;
    }
}
