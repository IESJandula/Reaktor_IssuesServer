package es.iesjandula.reaktor.issues_server.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoriaDto
{
    /**
     * Nombre de la categoría
     */
    private String nombre ;
}