package es.iesjandula.reaktor.issues_server.dto;

import java.time.LocalDateTime;

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
    private LocalDateTime  fechaIncidencia;
    private String estadoIncidencia;
    private String comentario;
}