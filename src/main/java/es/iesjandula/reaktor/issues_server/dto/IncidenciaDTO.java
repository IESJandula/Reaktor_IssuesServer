package es.iesjandula.reaktor.issues_server.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que representa un objeto de transferencia de datos para una incidencia.
 * 
 * <p>
 * Esta clase se utiliza para transportar información sobre una incidencia
 * en el sistema. Es un objeto ligero que facilita la comunicación entre
 * las distintas capas de la aplicación (por ejemplo, entre la capa de
 * servicio y la capa de presentación).
 * </p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IncidenciaDto 
{

    private String ubicacion;
    private String correoDocente;
    private LocalDateTime  fechaIncidencia;
    private String estado;
    private String comentario;

}
