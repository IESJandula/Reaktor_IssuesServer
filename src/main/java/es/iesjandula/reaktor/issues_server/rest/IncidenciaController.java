package es.iesjandula.reaktor.issues_server.rest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.iesjandula.reaktor.base.security.models.DtoUsuarioExtended;
import es.iesjandula.reaktor.base.utils.BaseConstants;
import es.iesjandula.reaktor.issues_server.dto.CrearIncidenciaDTO;
import es.iesjandula.reaktor.issues_server.dto.FiltroBusqueda;
import es.iesjandula.reaktor.issues_server.dto.IncidenciaDTO;
import es.iesjandula.reaktor.issues_server.dto.ModificarIncidenciaDto;
import es.iesjandula.reaktor.issues_server.models.CategoriaIncidenciaEntity;
import es.iesjandula.reaktor.issues_server.models.IncidenciaEntity;
import es.iesjandula.reaktor.issues_server.models.UsuarioCategoriaEntity;
import es.iesjandula.reaktor.issues_server.repository.ICategoriaIncidenciaRepository;
import es.iesjandula.reaktor.issues_server.repository.IIncidenciaRepository;
import es.iesjandula.reaktor.issues_server.repository.IUsuarioCategoriaRepository;
import es.iesjandula.reaktor.issues_server.utils.Constants;
import es.iesjandula.reaktor.issues_server.utils.IssuesServerError;
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
@RequestMapping(value = "/issues")
public class IncidenciaController
{

	@Autowired
	// Auto-inyeccion de repositorio.
	private IIncidenciaRepository iIncidenciaRepository;
	
	
	@Autowired
	private ICategoriaIncidenciaRepository iCategoriaIncidenciaRepository;
	
	
	@Autowired
	private IUsuarioCategoriaRepository iUsuarioCategoriaRepository;
	
    
//    @Value("${reaktor.http_connection_timeout}")
//	private int httpConnectionTimeout;
//    
//    @Value("${reaktor.firebase_server_url}")
//	private String firebaseServerUrl;


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

    @PreAuthorize("hasRole('" + BaseConstants.ROLE_ADMINISTRADOR + "')")
    @PutMapping("/modificar_incidencia")
    public ResponseEntity<?> modificarIncidencia(@RequestBody ModificarIncidenciaDto dto)
    {
        try
        {
            log.info("Modificar incidencia DTO: {}", dto);

            // Buscar incidencia por clave compuesta
            IncidenciaEntity incidencia = this.iIncidenciaRepository
                    .EncontrarByUbicacionAndCorreoDocenteAndFechaIncidencia(
                            dto.getUbicacion(),
                            dto.getCorreoDocente(),
                            dto.getFechaIncidencia()   // 👈 ya es LocalDateTime
                    );

            if (incidencia == null)
            {
                String msg = "La incidencia no existe.";
                log.error(msg);
                throw new IssuesServerError(6, msg);
            }

            // Actualizar estado (si viene)
            if (dto.getEstadoIncidencia() != null) {
                incidencia.setEstadoIncidencia(dto.getEstadoIncidencia());
            }

            // Actualizar comentario (si viene)
            if (dto.getComentario() != null) {
                incidencia.setComentario(dto.getComentario());
            }

            // Actualizar responsable (si viene)
            if (dto.getCorreoResponsable() != null) {
                incidencia.setCorreoResponsable(dto.getCorreoResponsable());
            }

            this.iIncidenciaRepository.saveAndFlush(incidencia);

            log.info("Incidencia modificada correctamente: {}", incidencia);
            return ResponseEntity.ok("Incidencia modificada con éxito");
        }
        catch (IssuesServerError e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMapError());
        }
        catch (Exception e)
        {
            String msg = "Error inesperado en modificarIncidencia(): " + e.getMessage();
            log.error(msg, e);
            IssuesServerError err = new IssuesServerError(0, msg, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(err.getMapError());
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
    @PreAuthorize("hasRole('" + BaseConstants.ROLE_PROFESOR + "')")
    @PostMapping("/crear_incidencia")
    public ResponseEntity<?> crearIncidencia(
            @AuthenticationPrincipal DtoUsuarioExtended usuario,
            @RequestBody CrearIncidenciaDTO crearIncidenciaDTO) 
    {
        try 
        {
            // Validaciones
            if (crearIncidenciaDTO.getUbicacion() == null || crearIncidenciaDTO.getUbicacion().isEmpty()) {
                throw new IssuesServerError(1, "El número de aula es obligatorio.");
            }

            if (crearIncidenciaDTO.getDescripcionIncidencia() == null 
                    || crearIncidenciaDTO.getDescripcionIncidencia().isEmpty()) {
                throw new IssuesServerError(3, "La descripción de la incidencia es obligatoria.");
            }

            if (crearIncidenciaDTO.getNombreCategoria() == null 
                    || crearIncidenciaDTO.getNombreCategoria().isEmpty()) {
                throw new IssuesServerError(4, "La categoría es obligatoria.");
            }

            // Buscar categoría
            CategoriaIncidenciaEntity categoria = this.iCategoriaIncidenciaRepository
                .findById(crearIncidenciaDTO.getNombreCategoria())
                .orElseThrow(() -> new IssuesServerError(5, "La categoría especificada no existe."));

            // Buscar responsable de categoría (el primero de la lista)
            List<UsuarioCategoriaEntity> responsables =
                    this.iUsuarioCategoriaRepository.findByNombreCategoria(crearIncidenciaDTO.getNombreCategoria());

            String correoResponsable = null;
            if (!responsables.isEmpty()) {
                correoResponsable = responsables.get(0).getCorreoResponsable(); // ← PRIMER RESPONSABLE
            }

            // Crear entidad Incidencia
            IncidenciaEntity nuevaIncidencia = new IncidenciaEntity();
            nuevaIncidencia.setUbicacion(crearIncidenciaDTO.getUbicacion());
            nuevaIncidencia.setCorreoDocente(usuario.getEmail());
            nuevaIncidencia.setFechaIncidencia(LocalDateTime.now());
            nuevaIncidencia.setDescripcionIncidencia(crearIncidenciaDTO.getDescripcionIncidencia());
            nuevaIncidencia.setEstadoIncidencia(Constants.ESTADO_PENDIENTE);
            nuevaIncidencia.setComentario(null);
            nuevaIncidencia.setCategoria(categoria);

            // Asignar responsable automático
            nuevaIncidencia.setCorreoResponsable(correoResponsable);

            // Guardar en BD
            this.iIncidenciaRepository.saveAndFlush(nuevaIncidencia);

            log.info("Incidencia creada correctamente: {}", nuevaIncidencia);

            return ResponseEntity.ok().build();
        }
        catch (IssuesServerError ex)
        {
            return ResponseEntity.status(400).body(ex.getMapError());
        }
        catch (Exception ex) 
        {
            String message = "ERROR al crear incidencia: " + ex.getMessage();
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

	@PreAuthorize("hasRole('" + BaseConstants.ROLE_PROFESOR + "')")
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
	 *            incidencia a eliminar. Los campos {@code ubicacion},
	 *            {@code correoDocente} y {@code fechaIncidencia} son obligatorios.
	 * @return {@link ResponseEntity} con el código de estado correspondiente: - 204
	 *         (NO_CONTENT) si la incidencia fue eliminada correctamente. - 404
	 *         (NOT_FOUND) si la incidencia no fue encontrada en la base de datos. -
	 *         400 (BAD_REQUEST) si los parámetros del DTO no son válidos. - 500
	 *         (INTERNAL_SERVER_ERROR) en caso de errores inesperados.
	 * @throws IllegalArgumentException si los parámetros del DTO son inválidos.
	 */
	@PreAuthorize("hasRole('" + BaseConstants.ROLE_PROFESOR + "')")
	@DeleteMapping("/borrarIncidencia")
	public ResponseEntity<?> borraIncidencia(@RequestBody(required = true) IncidenciaDTO incidenciaDTO)
	{
		try
		{
			 IncidenciaEntity incidencia = this.iIncidenciaRepository.EncontrarByUbicacionAndCorreoDocenteAndFechaIncidencia(incidenciaDTO.getUbicacion(),incidenciaDTO.getCorreoDocente(),incidenciaDTO.getFechaIncidencia());
		        log.info("Datos recibidos: numeroAula={}, correoDocente={}, fechaIncidencia={}",
		        		incidenciaDTO.getUbicacion(),
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
	
//	/**
//	 * @param usuario usuario
//	 * @param email   email
//	 * @return el profesor encontrado
//	 * @throws IssuesServerError con un error
//	 */
//	private Profesor buscarProfesor(Optional<Profesor> profesor2, String email) throws IssuesServerError
//	{
//		Profesor profesor = null;
//
//		
//			// Primero buscamos si ya tenemos a ese profesor en nuestra BBDD
//			Optional<Profesor> optionalProfesor = this.profesorRepository.findById(email);
//
//			// Si lo encontramos ...
//			if (!optionalProfesor.isEmpty())
//			{
//				// Lo cogemos del optional
//				profesor = optionalProfesor.get();
//			}
//			else
//			{
//				// Si no lo encontramos, le pedimos a Firebase que nos lo dé
//				profesor = this.buscarProfesorEnFirebase( email);
//		
//				// Si el usuario no es administrador, cogemos la información del usuario
//				profesor = new Profesor(profesor.getEmail(), profesor.getNombre(), profesor.getApellido());
//			}
//			// Lo almacenamos en BBDD en caso de que no exista
//			this.profesorRepository.saveAndFlush(profesor);
//		
//
//		return profesor;
//	}
//
//	/**
//	 * @param jwtAdmin JWT del usuario admin
//	 * @param email    email del profesor que va a realizar la reserva
//	 * @return el profesor encontrado enfirebase
//	 * @throws IssuesServerError con un error
//	 */
//	private Profesor buscarProfesorEnFirebase( String email) throws IssuesServerError
//	{
//		Profesor profesor = null;
//
//		// Creamos un HTTP Client con Timeout
//		CloseableHttpClient closeableHttpClient = HttpClientUtils.crearHttpClientConTimeout(this.httpConnectionTimeout);
//
//		CloseableHttpResponse closeableHttpResponse = null;
//
//		try
//		{
//			HttpGet httpGet = new HttpGet(this.firebaseServerUrl + "/firebase/queries/user");
//
//			
//			httpGet.addHeader("email", email);
//
//			// Hacemos la peticion
//			closeableHttpResponse = closeableHttpClient.execute(httpGet);
//
//			// Comprobamos si viene la cabecera. En caso afirmativo, es porque trae un
//			// profesor
//			if (closeableHttpResponse.getEntity() == null)
//			{
//				String mensajeError = "Profesor no encontrado en BBDD Global";
//
//				log.error(mensajeError);
//				throw new IssuesServerError(Constants.PROFESOR_NO_ENCONTRADO, mensajeError);
//			}
//
//			// Convertimos la respuesta en un objeto DtoInfoUsuario
//			ObjectMapper objectMapper = new ObjectMapper();
//
//			// Obtenemos la respuesta de Firebase
//			DtoUsuarioBase dtoUsuarioBase = objectMapper.readValue(closeableHttpResponse.getEntity().getContent(),
//					DtoUsuarioBase.class);
//
//			// Creamos una instancia de profesor con la respuesta de Firebase
//			profesor = new Profesor();
//			profesor.setNombre(dtoUsuarioBase.getNombre());
//			profesor.setApellido(dtoUsuarioBase.getApellidos());
//			profesor.setEmail(dtoUsuarioBase.getEmail());
//
//			// Almacenamos al profesor en nuestra BBDD
//			this.profesorRepository.saveAndFlush(profesor);
//		}
//		catch (SocketTimeoutException socketTimeoutException)
//		{
//			String errorString = "SocketTimeoutException de lectura o escritura al comunicarse con el servidor (búsqueda del profesor asociado a la reserva)";
//
//			log.error(errorString, socketTimeoutException);
//			throw new IssuesServerError(Constants.ERROR_CONEXION_FIREBASE, errorString, socketTimeoutException);
//		}
//		catch (ConnectTimeoutException connectTimeoutException)
//		{
//			String errorString = "ConnectTimeoutException al intentar conectar con el servidor (búsqueda del profesor asociado a la reserva)";
//
//			log.error(errorString, connectTimeoutException);
//			throw new IssuesServerError(Constants.TIMEOUT_CONEXION_FIREBASE, errorString, connectTimeoutException);
//		}
//		catch (IOException ioException)
//		{
//			String errorString = "IOException mientras se buscaba el profesor asociado a la reserva";
//
//			log.error(errorString, ioException);
//			throw new IssuesServerError(Constants.IO_EXCEPTION_FIREBASE, errorString, ioException);
//		}
//		finally
//		{
//			// Cierre de flujos
//			this.buscarProfesorEnFirebaseCierreFlujos(closeableHttpResponse);
//		}
//
//		return profesor;
//	}
//
//	/**
//	 * @param closeableHttpResponse closeable HTTP response
//	 * @throws PrinterClientException printer client exception
//	 */
//	private void buscarProfesorEnFirebaseCierreFlujos(CloseableHttpResponse closeableHttpResponse)
//			throws IssuesServerError
//	{
//		if (closeableHttpResponse != null)
//		{
//			try
//			{
//				closeableHttpResponse.close();
//			}
//			catch (IOException ioException)
//			{
//				String errorString = "IOException mientras se cerraba el closeableHttpResponse en el método que busca al profesor de la reserva";
//
//				log.error(errorString, ioException);
//				throw new IssuesServerError(Constants.IO_EXCEPTION_FIREBASE, errorString, ioException);
//			}
//		}
//	}
}