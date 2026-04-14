package es.iesjandula.reaktor.issues_server.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EstadisticasEstadoDto
{
	private String estado;
	private Long cantidad;
}
