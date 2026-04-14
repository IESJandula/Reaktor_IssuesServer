package es.iesjandula.reaktor.issues_server.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstadisticasUbicacionDto
{
	private String nombreUbicacion;
	private Long cantidad;
}
