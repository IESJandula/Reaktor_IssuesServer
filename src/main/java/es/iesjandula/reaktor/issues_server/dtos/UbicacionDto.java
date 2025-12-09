package es.iesjandula.reaktor.issues_server.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que representa una ubicación.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UbicacionDto
{
    /**
     * Nombre de la ubicación.
     */
    private String nombre;
}
