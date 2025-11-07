package es.iesjandula.reaktor.issues_server.dto;


import java.util.Date;

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
public class CrearIncidenciaDTO 
{
	/**
	 * Atribtuo - Aula en la que se da la incidencia.
	 */
	private String ubicacion;

	/**
	 * Atribtuo - Correo del docente que informa de la incidencia.
	 */
	private String correoDocente;

	/**
	 * Atribtuo - Fecha de creación de la señalación.
	 */
	//@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
	private Date fechaIncidencia;

	/**
	 * Atribtuo - Detalla el problema relacionado a la incidencia.
	 */
	private String descripcionIncidencia;

	/**
	 * @param descripcionIncidencia descripción de la incidencia
	 */
	public void setDescripcionIncidencia(String descripcionIncidencia)
    {
        if (descripcionIncidencia == null || descripcionIncidencia.trim().isEmpty())
        {
            throw new IllegalArgumentException("La descripcion del tic no debería de estar vacía");
        }
        
        this.descripcionIncidencia = descripcionIncidencia;
    }
	
	private String correoDestinatario;
	
}