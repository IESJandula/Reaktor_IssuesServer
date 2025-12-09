package es.iesjandula.reaktor.issues_server.dtos;

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
    /**
     * ID de la incidencia.
     */
    private Long id;

    /**
     * Aula en la que ocurre la incidencia.
     */     
    private String ubicacion;

    /**
     * Correo del docente que reportó la incidencia.
     */
    private String email;

    /**
     * Fecha y hora de la incidencia.
     */
    private LocalDateTime fecha;

    /**
     * Problema de la incidencia.
     */
    private String problema;

    /**
     * Estado de la incidencia.
     */
    private String estado;

    /**
     * Solución de la incidencia.
     */
    private String solucion;

    /**
     * Email del responsable de la incidencia.
     */
    private String emailResponsable;

    /**
     * Categoría de la incidencia.
     */
    private String categoria;
}
