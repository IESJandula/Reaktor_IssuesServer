package es.iesjandula.reaktor.issues_server.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstadisticasCategoriaDto
{
	private String nombreCategoria;
	private Long cantidad;
}