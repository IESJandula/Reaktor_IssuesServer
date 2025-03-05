package es.iesjandula.ReaktorIssuesServer.rest;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.iesjandula.ReaktorIssuesServer.dto.CrearIncidenciaDTO;
import es.iesjandula.ReaktorIssuesServer.dto.FiltroBusqueda;
import es.iesjandula.ReaktorIssuesServer.dto.IncidenciaDTO;
import es.iesjandula.ReaktorIssuesServer.dto.ModificarIncidenciaDto;
import es.iesjandula.ReaktorIssuesServer.entity.IncidenciaEntity;
import es.iesjandula.ReaktorIssuesServer.repository.IIncidenciaRepository;
import es.iesjandula.ReaktorIssuesServer.services.EmailService;
import es.iesjandula.ReaktorIssuesServer.utils.Constants;
import es.iesjandula.ReaktorIssuesServer.utils.IssuesServerError;
import lombok.extern.slf4j.Slf4j;

/**
 * Controlador REST para gestionar incidencias en el sistema.
 * 
 * Esta clase proporciona endpoints para crear, actualizar, buscar y eliminar
 * incidencias en la base de datos. Utiliza un repositorio para interactuar con
 * los datos de las incidencias y un mapeador para convertir entre objetos DTO y
 * entidades de base de datos.
 * 
 * Los métodos de esta clase devuelven respuestas adecuadas basadas en el
 * resultado de las operaciones, incluyendo códigos de estado HTTP para informar
 * sobre el éxito o fracaso de las solicitudes.
 * 
 * Las operaciones que se pueden realizar incluyen:
 * <ul>
 * <li><strong>Crear Incidencia:</strong> Permite la creación de nuevas
 * incidencias.</li>
 * <li><strong>Actualizar Incidencia:</strong> Permite la actualización de
 * incidencias existentes.</li>
 * <li><strong>Buscar Incidencias:</strong> Permite buscar incidencias basadas
 * en criterios específicos.</li>
 * <li><strong>Eliminar Incidencia:</strong> Permite la eliminación de
 * incidencias existentes.</li>
 * </ul>
 * 
 * Se requiere que los encabezados y los cuerpos de las solicitudes contengan
 * información válida para que las operaciones se ejecuten correctamente. En
 * caso de errores, se devuelven mensajes informativos y códigos de estado HTTP
 * adecuados.
 * 
 * @see FiltroBusqueda
 * @see IncidenciaDTO
 * @see IncidenciaEntity
 * @see IIncidenciaRepository
 * @see IncidenciaMapper
 */
@Slf4j 
@RestController
@CrossOrigin("*")
@RequestMapping(value = "/incidencias")
public class IncidenciaController
{

	@Autowired
	// Auto-inyeccion de repositorio.
	private IIncidenciaRepository iIncidenciaRepository;
	

    @Autowired
    private EmailService emailService;
    
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

	/**
	 * Crear o actualizar una incidencia en el sistema.
	 * 
	 * Este método recibe un DTO que contiene la información de la incidencia a
	 * crear o actualizar y un encabezado que incluye el correo del docente. Se realiza la
	 * validación de los parámetros recibidos y, si son válidos, se crea una nueva
	 * incidencia o se actualiza en la base de datos. Si los datos no son válidos, se devuelve un
	 * código de estado HTTP 400 (Bad Request). En caso de un error inesperado, se
	 * devuelve un código de estado HTTP 500 (Internal Server Error).
	 * 
	 * @param correoDocente      El correo electrónico del docente, que se espera en
	 *                           el encabezado de la solicitud. Este parámetro es
	 *                           requerido y no puede ser nulo.
	 * @param incidenciaDTO El objeto DTO que contiene la información de la
	 *                           incidencia a crear o actualizar. Este parámetro es requerido y
	 *                           no puede ser nulo.
	 * @return ResponseEntity<String> La respuesta que indica el resultado de la
	 *         operación. Si la creación o actualización es exitosa, se devuelve un código de estado
	 *         201 (Created) junto con un mensaje de éxito en caso de haber sido creada o 200(OK) 
	 *         en caso de ser actualizada.Si hay un error de validación, se devuelve 
	 *         un código de estado 400 (Bad Request) con un mensaje de error. 
	 *         Si ocurre un error inesperado, se devuelve un
	 *         código de estado 500 (Internal Server Error) con un mensaje de error.
	 */
	@PutMapping("/modificar_incidencia")
	public ResponseEntity<?> modificarIncidencia(@RequestBody ModificarIncidenciaDto modificarIncidenciaDto)
	{
	    try
	    {
	    	log.info(modificarIncidenciaDto.toString());
	        // Validación del estado
	        if (modificarIncidenciaDto.getEstadoIncidencia() == null || modificarIncidenciaDto.getEstadoIncidencia().isBlank()) 
	        {
	            String errorString = "El estado es obligatorio.";
	            log.error(errorString);
	            throw new IssuesServerError(4, errorString);
	        }

	        // Validación del comentario
	        if (modificarIncidenciaDto.getComentario() == null || modificarIncidenciaDto.getComentario().isBlank()) 
	        {
	            String errorString = "El comentario de la incidencia es obligatorio.";
	            log.error(errorString);
	            throw new IssuesServerError(5, errorString);
	        }

	        // Buscar la incidencia en la base de datos
	        IncidenciaEntity incidencia = this.iIncidenciaRepository.EncontrarByNumeroAulaAndCorreoDocenteAndFechaIncidencia(modificarIncidenciaDto.getNumeroAula(),modificarIncidenciaDto.getCorreoDocente(),modificarIncidenciaDto.getFechaIncidencia());
	        log.info("Datos recibidos: numeroAula={}, correoDocente={}, fechaIncidencia={}",
	        	    modificarIncidenciaDto.getNumeroAula(),
	        	    modificarIncidenciaDto.getCorreoDocente(),
	        	    modificarIncidenciaDto.getFechaIncidencia()
	        	);

	        if (incidencia == null) 
	        {
	            String errorString = "La incidencia no existe.";
	            log.error(errorString);
	            throw new IssuesServerError(6, errorString);
	        }

	        // Actualizar los campos de estado y comentario
	        incidencia.setEstadoIncidencia(modificarIncidenciaDto.getEstadoIncidencia());
	        incidencia.setComentario(modificarIncidenciaDto.getComentario());

	        // Guardar los cambios en la base de datos
	        this.iIncidenciaRepository.saveAndFlush(incidencia);

	        // Información para registro
	        log.info("La incidencia ha sido modificada correctamente: " + incidencia.toString());

	        // Respuesta exitosa
	        return ResponseEntity.ok("Incidencia modificada con éxito\"}");

	    } 
	    catch (IssuesServerError exception) 
	    {
	        return ResponseEntity.status(400).body(exception.getMapError());
	    } 
	    catch (Exception exception) 
	    {
	        String message = "Excepción capturada en modificarIncidencia(): " + exception.getMessage();
	        log.error(message, exception);
	        IssuesServerError serverError = new IssuesServerError(0, message, exception);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(serverError.getMapError());
	    }
	}


	/**
	 * Maneja las solicitudes GET para buscar incidencias en base a los criterios
	 * proporcionados en el filtro de búsqueda.
	 * 
	 * Este método recibe un objeto {@link FiltroBusqueda} que contiene los
	 * criterios de búsqueda para filtrar las incidencias almacenadas. Realiza la
	 * búsqueda utilizando el repositorio correspondiente y devuelve una lista de
	 * incidencias que cumplen con los criterios. En caso de que no se encuentren
	 * incidencias, se devuelve un mensaje informativo con un código de estado 404
	 * (Not Found). Si ocurre algún error durante el proceso, se devuelve un mensaje
	 * de error con un código de estado 500 (Internal Server Error).
	 *
	 * @param filtro El objeto {@link FiltroBusqueda} que contiene los criterios de
	 *          búsqueda para filtrar las incidencias.
	 * @return Un objeto {@link ResponseEntity} que puede contener:
	 *         <ul>
	 *         <li>Una lista de {@link IncidenciaDTO} en caso de que se encuentren
	 *         incidencias, con código de estado 200 (OK).</li>
	 *         <li>Un mensaje de error si no se encuentran incidencias, con código
	 *         de estado 404 (Not Found).</li>
	 *         <li>Un mensaje de error general, en caso de excepciones inesperadas,
	 *         con código de estado 500 (Internal Server Error).</li>
	 *         </ul>
	 */
	@PostMapping("/crear_incidencia")
	public ResponseEntity<?> crearIncidencia(@RequestBody CrearIncidenciaDTO crearIncidenciaDTO) 
	{
	    try 
	    {
	        // Validar que los datos obligatorios estén presentes
	        if (crearIncidenciaDTO.getNumeroAula() == null || crearIncidenciaDTO.getNumeroAula().isEmpty()) 
	        {
	        	String errorString = "El número de aula es obligatorio." ;
	        	
	        	log.error(errorString) ;
	        	throw new IssuesServerError(1, errorString) ;
	        }

	        if (crearIncidenciaDTO.getCorreoDocente() == null || crearIncidenciaDTO.getCorreoDocente().isEmpty())
	        {
	        	String errorString = "El correo del docente es obligatorio." ;
	        	
	        	log.error(errorString) ;
	        	throw new IssuesServerError(2, errorString) ;
	        }

	        if (crearIncidenciaDTO.getDescripcionIncidencia() == null || crearIncidenciaDTO.getDescripcionIncidencia().isEmpty()) 
	        {
	        	String errorString = "La descripción de la incidencia es obligatoria." ;
	        	
	        	log.error(errorString) ;
	        	throw new IssuesServerError(3, errorString) ;	        
        	}
	        
	        if (crearIncidenciaDTO.getCorreoDestinatario() == null || crearIncidenciaDTO.getCorreoDestinatario().isEmpty()) 
	        {
	        	String errorString = "El correo del destinatario es obligatorio." ;
	        	
	        	log.error(errorString) ;
	        	throw new IssuesServerError(3, errorString) ;	        
        	}
	        
	        // Crear un nuevo objeto entidad para guardar en la base de datos
	        IncidenciaEntity nuevaIncidencia = new IncidenciaEntity();
	        nuevaIncidencia.setNumeroAula(crearIncidenciaDTO.getNumeroAula());
	        nuevaIncidencia.setCorreoDocente(crearIncidenciaDTO.getCorreoDocente());
	        nuevaIncidencia.setFechaIncidencia(LocalDateTime.now());
	        nuevaIncidencia.setCorreoDestinatario(crearIncidenciaDTO.getCorreoDestinatario());
	        nuevaIncidencia.setDescripcionIncidencia(crearIncidenciaDTO.getDescripcionIncidencia());
	        nuevaIncidencia.setEstadoIncidencia(Constants.ESTADO_PENDIENTE);
	        
	        // Guardar la incidencia en la base de datos
	        this.iIncidenciaRepository.saveAndFlush(nuevaIncidencia);

	        // Loguea el éxito de la operación
	        log.info("Incidencia creada correctamente: {}", nuevaIncidencia);

	        Date fecha = java.sql.Timestamp.valueOf(nuevaIncidencia.getFechaIncidencia());
	        
	        String fechaFormateada = simpleDateFormat.format(fecha);

	        // Enviar correo de notificación
            String asunto = "Nueva Incidencia en el Aula " + crearIncidenciaDTO.getNumeroAula();
            String cuerpo = "Detalles de la Incidencia:\n\n" +
                    "Aula: " + crearIncidenciaDTO.getNumeroAula() + "\n" +
                    "‍Docente: " + crearIncidenciaDTO.getCorreoDocente() + "\n" +
                    "Descripción: " + crearIncidenciaDTO.getDescripcionIncidencia() + "\n" +
                    "Fecha: " + fechaFormateada + "\n\n" +
                    
                    "Este correo ha sido generado automáticamente.";

            emailService.sendEmail(crearIncidenciaDTO.getCorreoDestinatario(), asunto, cuerpo, crearIncidenciaDTO.getCorreoDocente());
            
	        
	        // Devuelve la respuesta exitosa
	        return ResponseEntity.ok().build();
	    }
	    catch (IssuesServerError exception)
		{
			// Si llega aquí es porque ha habido un error de validación de datos de cliente
			return ResponseEntity.status(400).body(exception.getMapError()) ;
		}
	    catch (Exception ex) 
	    {
	        String message = "ERROR: Error al crear la incidencia:\n " + ex.getMessage();
	        log.error(message, ex);

	        IssuesServerError serverError = new IssuesServerError(3, message, ex);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(serverError.getMapError());
	    }
	}
	
	@GetMapping("/listadoEstado")
	public ResponseEntity<?> listadoEstadoIncidencias()
	{
	    try
	    {
	        List<String> listaEstadoIncidencias = new ArrayList<>();
	        
	        // Agregar estados de incidencias
	        listaEstadoIncidencias.add(Constants.ESTADO_PENDIENTE);
	        listaEstadoIncidencias.add(Constants.ESTADO_RESUELTA);
	        listaEstadoIncidencias.add(Constants.ESTADO_CANCELADA);
	        listaEstadoIncidencias.add(Constants.ESTADO_DUPLICADA);
	        listaEstadoIncidencias.add(Constants.ESTADO_EN_PROGRESO);
	        
	        // Responder con la lista de estados
	        return ResponseEntity.ok().body(listaEstadoIncidencias);

	    }
	    catch (IllegalArgumentException illegalArgumentException)
	    {
	        String message = "ERROR: Error en parámetros del objeto recibido en listadoEstadoIncidencias().\n" + illegalArgumentException.getMessage();
	        log.error(message, illegalArgumentException);
	        IssuesServerError serverError = new IssuesServerError(1, message, illegalArgumentException);
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(serverError.getMapError());
	    }
	    catch (Exception exception)
	    {
	        String message = "Error inesperado en listadoEstadoIncidencias().\nMensaje de error: " + exception.getMessage();
	        log.error(message, exception);
	        IssuesServerError serverError = new IssuesServerError(4, message, exception);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(serverError.getMapError());
	    }
	}

	
	@GetMapping("/listarIncidenciasOrdenadas") 	
	public Page<IncidenciaEntity> listarIncidenciasOrdenadasPorFecha(Pageable pageable)
	{ 	        
		return this.iIncidenciaRepository.buscarIncidenciaOrdenadaFecha(pageable); 	    
	}
	

	/**
	 * Elimina una incidencia de la base de datos basándose en los detalles
	 * proporcionados en el DTO. Verifica primero si la incidencia existe, y si no,
	 * retorna un código de estado 404 (NOT_FOUND). Si la incidencia existe, la
	 * elimina y retorna un código de estado 204 (NO_CONTENT) indicando éxito.
	 *
	 * @param dto El objeto {@link IncidenciaDTO} que contiene los detalles de la
	 *            incidencia a eliminar. Los campos {@code numeroAula},
	 *            {@code correoDocente} y {@code fechaIncidencia} son obligatorios.
	 * @return {@link ResponseEntity} con el código de estado correspondiente: - 204
	 *         (NO_CONTENT) si la incidencia fue eliminada correctamente. - 404
	 *         (NOT_FOUND) si la incidencia no fue encontrada en la base de datos. -
	 *         400 (BAD_REQUEST) si los parámetros del DTO no son válidos. - 500
	 *         (INTERNAL_SERVER_ERROR) en caso de errores inesperados.
	 * @throws IllegalArgumentException si los parámetros del DTO son inválidos.
	 */
	@DeleteMapping("/borrarIncidencia")
	public ResponseEntity<?> borraIncidencia(@RequestBody(required = true) IncidenciaDTO incidenciaDTO)
	{
		try
		{
			 IncidenciaEntity incidencia = this.iIncidenciaRepository.EncontrarByNumeroAulaAndCorreoDocenteAndFechaIncidencia(incidenciaDTO.getNumeroAula(),incidenciaDTO.getCorreoDocente(),incidenciaDTO.getFechaIncidencia());
		        log.info("Datos recibidos: numeroAula={}, correoDocente={}, fechaIncidencia={}",
		        		incidenciaDTO.getNumeroAula(),
		        	    incidenciaDTO.getCorreoDocente(),
		        	    incidenciaDTO.getFechaIncidencia()
		        	);

		        if (incidencia == null) 
		        {
		            String errorString = "La incidencia no existe.";
		            log.error(errorString);
		            throw new IssuesServerError(6, errorString);
		        }

			// Elimina la incidencia de la base de datos y loguea la accion.
			this.iIncidenciaRepository.delete(incidencia);
			log.info("INFO: Incidencia eliminada con exito.\n{}", incidencia.toString());

			// Respuesta HTTP de objeto borrado con exito.
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body("INFO:Incidencia eliminada con exito.");

		}
		catch (IssuesServerError exception)
		{
			return ResponseEntity.status(400).body(exception.getMapError()) ;
		}
		// Error en parametros DTO u objeto nulo.
		catch (IllegalArgumentException illegalArgumentException)
		{
			String message = "ERROR: Error en parametros del objeto recibido en borraIncidencia().\n{}" + illegalArgumentException.getMessage();
			log.error(message, illegalArgumentException);
			IssuesServerError serverError = new IssuesServerError(1, message, illegalArgumentException);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(serverError.getMapError());
		}
		// Captura de errores no esperados o calculados.
		catch (Exception deleteIssueException)
		{
			String message = "Error inesperado en borraIncidencia() .\nMensaje de error: " + deleteIssueException.getMessage();
			log.error(message, deleteIssueException);
			IssuesServerError serverError = new IssuesServerError(4, message, deleteIssueException);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(serverError.getMapError());
		}
	}
}