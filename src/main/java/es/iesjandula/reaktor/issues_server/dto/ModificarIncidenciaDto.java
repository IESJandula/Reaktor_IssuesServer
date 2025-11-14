package es.iesjandula.reaktor.issues_server.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModificarIncidenciaDto
{
	private String ubicacion;
	private String correoDocente;
	private String estadoIncidencia;
	private String correoResponsable; 
	private String comentario;
	
    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    private LocalDateTime fechaIncidencia;
}