package es.iesjandula.ReaktorIssuesServer.dto;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModificarIncidenciaDto
{

    private String numeroAula;
    private String correoDocente;
    private Date fechaIncidencia;
    private String estado;
    private String comentario;
}