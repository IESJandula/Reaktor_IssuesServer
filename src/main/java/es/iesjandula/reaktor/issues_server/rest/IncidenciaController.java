package es.iesjandula.reaktor.issues_server.rest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.Optional;
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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.iesjandula.reaktor.base.security.models.DtoUsuarioExtended;
import es.iesjandula.reaktor.base_client.dtos.NotificationEmailDto;
import es.iesjandula.reaktor.base_client.requests.notificaciones.RequestNotificacionesEnviarEmail;
import es.iesjandula.reaktor.base_client.utils.BaseClientException;
import es.iesjandula.reaktor.base.utils.BaseConstants;
import es.iesjandula.reaktor.issues_server.dtos.FiltroBusquedaDto;
import es.iesjandula.reaktor.issues_server.dtos.IncidenciaDto;
import es.iesjandula.reaktor.issues_server.models.Incidencia;
import es.iesjandula.reaktor.issues_server.models.Ubicacion;
import es.iesjandula.reaktor.issues_server.models.UsuarioCategoria;
import es.iesjandula.reaktor.issues_server.models.ids.UsuarioCategoriaId;
import es.iesjandula.reaktor.issues_server.repository.IUbicacionRepository;
import es.iesjandula.reaktor.issues_server.repository.IIncidenciaRepository;
import es.iesjandula.reaktor.issues_server.repository.IUsuarioCategoriaRepository;
import es.iesjandula.reaktor.issues_server.utils.Constants;
import es.iesjandula.reaktor.issues_server.utils.IssuesServerError;
import lombok.extern.log4j.Log4j2;

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
 * <li><strong>Actualizar Categoría de Incidencia:</strong> Permite la actualización de la categoría de una incidencia.</li>
 * <li><strong>Actualizar Estado de Incidencia:</strong> Permite la actualización del estado de una incidencia.</li>
 * <li><strong>Actualizar Solución de Incidencia:</strong> Permite la actualización de la solución de una incidencia.</li>
 * <li><strong>Actualizar Responsable de Incidencia:</strong> Permite la actualización del responsable de una incidencia.</li>
 * <li><strong>Listado de Estados de Incidencias:</strong> Permite obtener el listado de estados de las incidencias.</li>
 * <li><strong>Listado de Incidencias Ordenadas por Fecha:</strong> Permite obtener el listado de incidencias ordenadas por fecha.</li>
 * <li><strong>Borrar Incidencia:</strong> Permite la eliminación de una incidencia.</li>
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
 * @see FiltroBusquedaDto
 * @see IncidenciaDto
 * @see Incidencia
 * @see IIncidenciaRepository
 * @see IncidenciaMapper
 */
@Log4j2 
@RestController
@RequestMapping(value = "/issues/incidencias")
public class IncidenciaController
{
	/** Repositorio de ubicaciones */
	@Autowired
	private IUbicacionRepository ubicacionRepository;
	
	/** Repositorio de incidencias */
	@Autowired
	private IIncidenciaRepository incidenciaRepository;
	
	/** Repositorio de usuarios-categoría */
	@Autowired
	private IUsuarioCategoriaRepository usuarioCategoriaRepository;

	@Autowired
	private RequestNotificacionesEnviarEmail requestNotificacionesEnviarEmail;

	/**
	 * Crea una nueva incidencia en el sistema por parte del usuario.
	 * 
	 * Este método recibe un objeto {@link CrearIncidenciaUsuarioDto} que contiene los datos de la incidencia a crear.
	 * Realiza la validación de los datos y crea una nueva incidencia en la base de datos.
	 * 
	 * @param usuario El usuario que crea la incidencia.
	 * @param ubicacion La ubicación de la incidencia.
	 * @param problema La descripción del problema de la incidencia.
	 * @param nombreCategoria El nombre de la categoría de la incidencia.
	 * @return Un objeto {@link ResponseEntity} que puede contener:
	 *         <ul>
	 *         <li>Un código de estado 200 (OK) si la incidencia se crea correctamente.</li>
	 *         <li>Un código de estado 400 (Bad Request) si los datos de la incidencia no son válidos.</li>
	 *         <li>Un código de estado 500 (Internal Server Error) si ocurre un error inesperado.</li>
	 *         </ul>
	 */
    @PreAuthorize("hasRole('" + BaseConstants.ROLE_PROFESOR + "')")
    @PostMapping("/")
    public ResponseEntity<?> crearIncidencia(@AuthenticationPrincipal DtoUsuarioExtended usuario,
											 @RequestHeader String nombreUbicacion,
											 @RequestHeader String problema,
											 @RequestHeader String nombreCategoria)
    {
        try 
        {
            // Validaciones previas sobre los datos de la incidencia
			this.validarCrearIncidencia(nombreUbicacion, problema, nombreCategoria);

			// Buscamos la ubicación
			Optional<Ubicacion> optionalUbicacion = this.ubicacionRepository.findById(nombreUbicacion);

			// Validamos que la ubicación exista
			if (!optionalUbicacion.isPresent())
			{
				log.error(Constants.ERR_INCIDENCIA_UBICACION_NO_ENCONTRADA_MESSAGE);
				throw new IssuesServerError(Constants.ERR_INCIDENCIA_UBICACION_NO_ENCONTRADA_CODE, Constants.ERR_INCIDENCIA_UBICACION_NO_ENCONTRADA_MESSAGE);
			}

			// Obtenemos la ubicación
			Ubicacion ubicacion = optionalUbicacion.get();

			// Buscamos el primer responsable de la categoría
			UsuarioCategoria responsableCategoria = this.buscarPrimerResponsablePorCategoria(nombreCategoria);

            // Creamos la nueva incidencia
            Incidencia nuevaIncidencia = new Incidencia();
			nuevaIncidencia.setUbicacion(ubicacion);
			nuevaIncidencia.setEmail(usuario.getEmail());
			nuevaIncidencia.setNombre(usuario.getNombre());
			nuevaIncidencia.setApellidos(usuario.getApellidos());
			nuevaIncidencia.setFecha(LocalDateTime.now());
			nuevaIncidencia.setProblema(problema);
			nuevaIncidencia.setEstado(Constants.ESTADO_PENDIENTE);
			nuevaIncidencia.setSolucion(null);
			nuevaIncidencia.setUsuarioCategoria(responsableCategoria);

            // Guardamos la incidencia en la base de datos
            this.incidenciaRepository.saveAndFlush(nuevaIncidencia);

			// Logueamos la incidencia creada
            log.info("Incidencia creada correctamente: {}", nuevaIncidencia);

			// Enviamos la notificación email al responsable de la categoría
			this.enviarEmailCreacionIncidencia(nuevaIncidencia);

			// Devolvemos la respuesta
            return ResponseEntity.ok().build();
        }
        catch (IssuesServerError issuesServerError)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(issuesServerError.getBodyErrorMessage());
        }
        catch (Exception exception) 
        {
            // Creamos una excepción genérica para devolver al cliente
			IssuesServerError issuesServerError =  new IssuesServerError(Constants.ERR_GENERICO_CODE, Constants.ERR_GENERICO_MESSAGE, exception);

			// Log de la excepción
			log.error("Excepción genérica al crear la incidencia", issuesServerError);

			// Devolvemos la respuesta
			return ResponseEntity.status(500).body(issuesServerError.getBodyErrorMessage());
        }
    }

	/**
	 * Valida los datos de la incidencia.
	 * <p>
	 * Este método valida los datos de la incidencia para asegurar que todos los campos requeridos estén presentes y sean válidos.
	 * </p>
	 *
	 * @param nombreUbicacion El nombre de la ubicación de la incidencia.
	 * @param problema La descripción del problema de la incidencia.
	 * @param nombreCategoria El nombre de la categoría de la incidencia.
	 * @throws IssuesServerError si los datos de la incidencia no son válidos
	 */
	private void validarCrearIncidencia(String nombreUbicacion, String problema, String nombreCategoria) throws IssuesServerError
	{
		// Validamos el nombre de la ubicación
		if (nombreUbicacion == null || nombreUbicacion.isEmpty())
		{
			log.error(Constants.ERR_INCIDENCIA_UBICACION_NO_INTRODUCIDA_MESSAGE);
			throw new IssuesServerError(Constants.ERR_INCIDENCIA_UBICACION_NO_INTRODUCIDA_CODE, Constants.ERR_INCIDENCIA_UBICACION_NO_INTRODUCIDA_MESSAGE);
		}

		// Validamos el problema
		if (problema == null || problema.isEmpty())
		{
			log.error(Constants.ERR_INCIDENCIA_PROBLEMA_NO_INTRODUCIDO_MESSAGE);
			throw new IssuesServerError(Constants.ERR_INCIDENCIA_PROBLEMA_NO_INTRODUCIDO_CODE, Constants.ERR_INCIDENCIA_PROBLEMA_NO_INTRODUCIDO_MESSAGE);
		}

		// Validamos la categoría
		if (nombreCategoria == null || nombreCategoria.isEmpty())
		{
			log.error(Constants.ERR_INCIDENCIA_CATEGORIA_NO_INTRODUCIDA_MESSAGE);
			throw new IssuesServerError(Constants.ERR_INCIDENCIA_CATEGORIA_NO_INTRODUCIDA_CODE, Constants.ERR_INCIDENCIA_CATEGORIA_NO_INTRODUCIDA_MESSAGE);
		}
	}

	/**
	 * Actualiza la categoría de una incidencia en el sistema por parte del administrador.
	 * 
	 * Este método recibe el ID de la incidencia y el nombre de la categoría a actualizar.
	 * Se realiza la validación de los datos y, si son válidos, se actualiza la categoría de la incidencia en la base de datos.
	 * 
	 * @param id El ID de la incidencia.
	 * @param nombreCategoria El nombre de la categoría de la incidencia.
	 * @return Un objeto {@link ResponseEntity} que puede contener:
	 *         <ul>
	 *         <li>Un código de estado 200 (OK) si la categoría de la incidencia se actualiza correctamente.</li>
	 *         <li>Un código de estado 400 (Bad Request) si los datos de la incidencia no son válidos.</li>
	 *         <li>Un código de estado 500 (Internal Server Error) si ocurre un error inesperado.</li>
	 *         </ul>
	 * @throws IssuesServerError si los datos de la incidencia no son válidos
	 */
	@PreAuthorize("hasRole('" + BaseConstants.ROLE_ADMINISTRADOR + "')")
	@PutMapping("/categoria/")
	public ResponseEntity<?> actualizarCategoria(@RequestHeader Long id, @RequestHeader String nombreCategoria)
	{
		try
		{
			// Buscamos la incidencia a actualizar
			Incidencia incidencia = this.buscarIncidenciaPorId(id);

			// Buscamos el primer responsable de la categoría
			UsuarioCategoria responsableCategoria = this.buscarPrimerResponsablePorCategoria(nombreCategoria);

			// Asignamos el primer responsable que se encuentre
			incidencia.setUsuarioCategoria(responsableCategoria);

			// Guardamos la incidencia en la base de datos
			this.incidenciaRepository.saveAndFlush(incidencia);

			// Logueamos la incidencia actualizada
			log.info("Categoría de la incidencia actualizada correctamente: {}", incidencia);

			// Enviamos la notificación email de creación de incidencia, ya que ha cambiado de categoría
			this.enviarEmailCreacionIncidencia(incidencia);

			// Devolvemos la respuesta
			return ResponseEntity.ok().build();
		}
		catch (IssuesServerError issuesServerError)
		{
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(issuesServerError.getBodyErrorMessage());
		}
		catch (Exception exception)
		{
			// Creamos una excepción genérica para devolver al cliente
			IssuesServerError issuesServerError =  new IssuesServerError(Constants.ERR_GENERICO_CODE, Constants.ERR_GENERICO_MESSAGE, exception);

			// Log de la excepción
			log.error("Excepción genérica al actualizar la categoría de la incidencia", issuesServerError);

			// Devolvemos la respuesta
			return ResponseEntity.status(500).body(issuesServerError.getBodyErrorMessage());
		}
	}

	/**
	 * Envia una notificación email al responsable elegido para la categoría de la incidencia.
	 * @param incidencia La incidencia a notificar.
	 */
	private void enviarEmailCreacionIncidencia(Incidencia incidencia)
	{
		try
		{
			// Creamos la lista de destinatarios
			List<String> destinatarios = Arrays.asList(incidencia.getUsuarioCategoria().getId().getEmailResponsable());

			// Creamos el asunto de la notificación
			String asunto = "Nueva incidencia creada por usuario " + incidencia.getNombre() + " " + incidencia.getApellidos() + 
			                " en la ubicación " + incidencia.getUbicacion().getNombre() ;
							
			// Creamos el cuerpo de la notificación
			String cuerpo = "Problema encontrado: \n\n " + incidencia.getProblema() ;

			// Creamos el DTO de la notificación email
			NotificationEmailDto notificationEmailDto = new NotificationEmailDto(destinatarios, null, null, asunto, cuerpo);

			// Enviamos la notificación email
			this.requestNotificacionesEnviarEmail.enviarNotificacionEmail(notificationEmailDto);
		}
		catch (BaseClientException exception)
		{
			// Ya ha sido logueada previamente
		}
		catch (Exception exception)
		{
			// Ya ha sido logueada previamente
			log.error("Error al enviar la notificación email (creación de incidencia)", exception);
		}
	}

	/**
	 * Busca el primer responsable de una categoría.
	 * @param nombreCategoria El nombre de la categoría.
	 * @return El primer responsable de la categoría.
	 * @throws IssuesServerError si el primer responsable no existe
	 */
	private UsuarioCategoria buscarPrimerResponsablePorCategoria(String nombreCategoria) throws IssuesServerError
	{
		// Validamos el nombre de la categoría
		if (nombreCategoria == null || nombreCategoria.isEmpty())
		{
			log.error(Constants.ERR_CATEGORIA_NO_INFORMADA_MESSAGE);
			throw new IssuesServerError(Constants.ERR_CATEGORIA_NO_INFORMADA_CODE, Constants.ERR_CATEGORIA_NO_INFORMADA_MESSAGE);
		}

		// Buscamos los responsables de la categoría (el primero de la lista)
		List<UsuarioCategoria> responsablesCategoria = this.usuarioCategoriaRepository.buscarResponsablesPorCategoria(nombreCategoria);

		// Validamos que los responsables existan
		if (responsablesCategoria.isEmpty())
		{
			log.error(Constants.ERR_USUARIO_CATEGORIA_NO_ENCONTRADA_MESSAGE);
			throw new IssuesServerError(Constants.ERR_USUARIO_CATEGORIA_NO_ENCONTRADA_CODE, Constants.ERR_USUARIO_CATEGORIA_NO_ENCONTRADA_MESSAGE);
		}

		// Devolvemos el primer responsable
		return responsablesCategoria.get(0);
	}

	/**
	 * Actualiza el estado de una incidencia en el sistema por parte del administrador.
	 * 
	 * Este método recibe el ID de la incidencia y el estado a actualizar.
	 * Se realiza la validación de los datos y, si son válidos, se actualiza el estado de la incidencia en la base de datos.
	 * 
	 * @param id El ID de la incidencia.
	 * @param estado El estado de la incidencia.
	 * @return Un objeto {@link ResponseEntity} que puede contener:
	 *         <ul>
	 *         <li>Un código de estado 200 (OK) si el estado de la incidencia se actualiza correctamente.</li>
	 *         <li>Un código de estado 400 (Bad Request) si los datos de la incidencia no son válidos.</li>
	 *         <li>Un código de estado 500 (Internal Server Error) si ocurre un error inesperado.</li>
	 *         </ul>
	 */
	@PreAuthorize("hasRole('" + BaseConstants.ROLE_ADMINISTRADOR + "')")
	@PutMapping("/estado/")
	public ResponseEntity<?> actualizarEstado(@RequestHeader Long id, @RequestHeader String estado)
	{
		try
		{
			// Validamos si el estado es válido (no nulo ni vacío)
			if (estado == null || estado.isEmpty())
			{
				log.error(Constants.ERR_INCIDENCIA_ESTADO_NO_INTRODUCIDO_MESSAGE);
				throw new IssuesServerError(Constants.ERR_INCIDENCIA_ESTADO_NO_INTRODUCIDO_CODE, Constants.ERR_INCIDENCIA_ESTADO_NO_INTRODUCIDO_MESSAGE);
			}

			// Validamos si el estado es válido (está en la lista de estados válidos)
			if (!Constants.ESTADOS_VALIDOS.contains(estado))
			{
				log.error(Constants.ERR_INCIDENCIA_ESTADO_NO_VALIDO_MESSAGE);
				throw new IssuesServerError(Constants.ERR_INCIDENCIA_ESTADO_NO_VALIDO_CODE, Constants.ERR_INCIDENCIA_ESTADO_NO_VALIDO_MESSAGE);
			}

			// Buscamos la incidencia a actualizar
			Incidencia incidencia = this.buscarIncidenciaPorId(id);

			// Actualizamos el estado de la incidencia
			incidencia.setEstado(estado);

			// Guardamos la incidencia en la base de datos
			this.incidenciaRepository.saveAndFlush(incidencia);

			// Logueamos la incidencia actualizada
			log.info("Estado de la incidencia actualizado correctamente: {}", incidencia);

			// Enviamos la notificación email al usuario y al responsable de la categoría de la incidencia
			this.enviarEmailActualizacionEstado(incidencia);

			// Devolvemos la respuesta
			return ResponseEntity.ok().build();
		}
		catch (IssuesServerError issuesServerError)
		{
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(issuesServerError.getBodyErrorMessage());
		}
		catch (Exception exception)
		{
			// Creamos una excepción genérica para devolver al cliente
			IssuesServerError issuesServerError =  new IssuesServerError(Constants.ERR_GENERICO_CODE, Constants.ERR_GENERICO_MESSAGE, exception);

			// Log de la excepción
			log.error("Excepción genérica al actualizar el estado de la incidencia", issuesServerError);

			// Devolvemos la respuesta
			return ResponseEntity.status(500).body(issuesServerError.getBodyErrorMessage());
		}
	}

	/**
	 * Envia una notificación email al usuario para que sepa que el estado de su incidencia ha cambiado.
	 * @param incidencia La incidencia a notificar.
	 */
	private void enviarEmailActualizacionEstado(Incidencia incidencia)
	{
		try
		{
			// Creamos la lista de destinatarios
			List<String> destinatarios = Arrays.asList(incidencia.getEmail());

			// Creamos el asunto de la notificación
			String asunto = "El estado de la incidencia ha cambiado" ;
							
			// Creamos el cuerpo de la notificación
			String cuerpo = "Sobre la incidencia creada por ti en la ubicación " + incidencia.getUbicacion().getNombre() + 
			                ", el usuario " + incidencia.getUsuarioCategoria().getNombreResponsable() + " " + 
			                " ha cambiado el estado a " + incidencia.getEstado() ;

			// Creamos el DTO de la notificación email
			NotificationEmailDto notificationEmailDto = new NotificationEmailDto(destinatarios, null, null, asunto, cuerpo);

			// Enviamos la notificación email
			this.requestNotificacionesEnviarEmail.enviarNotificacionEmail(notificationEmailDto);
		}
		catch (BaseClientException exception)
		{
			// Ya ha sido logueada previamente
		}
		catch (Exception exception)
		{
			// Ya ha sido logueada previamente
			log.error("Error al enviar la notificación email (actualización de estado)", exception);
		}
	}
	/**
	 * Actualiza la solución de una incidencia en el sistema por parte del administrador.
	 * 
	 * Este método recibe el ID de la incidencia y la solución a actualizar.
	 * Se realiza la validación de los datos y, si son válidos, se actualiza la solución de la incidencia en la base de datos.
	 * 
	 * @param id El ID de la incidencia a actualizar.
	 * @param solucion La solución de la incidencia a actualizar.
	 * @return Un objeto {@link ResponseEntity} que puede contener:
	 *         <ul>
	 *         <li>Un código de estado 200 (OK) si la solución de la incidencia se actualiza correctamente.</li>
	 *         <li>Un código de estado 400 (Bad Request) si los datos de la incidencia no son válidos.</li>
	 *         <li>Un código de estado 500 (Internal Server Error) si ocurre un error inesperado.</li>
	 *         </ul>
	 */
	@PreAuthorize("hasRole('" + BaseConstants.ROLE_ADMINISTRADOR + "')")
	@PutMapping("/solucion/")
	public ResponseEntity<?> actualizarSolucion(@RequestHeader Long id, @RequestHeader String solucion)
	{
		try
		{
			// Buscamos la incidencia a actualizar
			Incidencia incidencia = this.buscarIncidenciaPorId(id);

			// Actualizamos la solución de la incidencia
			incidencia.setSolucion(solucion);

			// Guardamos la incidencia en la base de datos
			this.incidenciaRepository.saveAndFlush(incidencia);

			// Logueamos la incidencia actualizada
			log.info("Solución de la incidencia actualizada correctamente: {}", incidencia);

			// Enviamos la notificación email al usuario para que sepa que la solución de su incidencia ha cambiado
			this.enviarEmailActualizacionSolucion(incidencia);

			// Devolvemos la respuesta
			return ResponseEntity.ok().build();
		}
		catch (IssuesServerError issuesServerError)
		{
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(issuesServerError.getBodyErrorMessage());
		}
		catch (Exception exception)
		{
			// Creamos una excepción genérica para devolver al cliente
			IssuesServerError issuesServerError =  new IssuesServerError(Constants.ERR_GENERICO_CODE, Constants.ERR_GENERICO_MESSAGE, exception);

			// Log de la excepción
			log.error("Excepción genérica al actualizar la solución de la incidencia", issuesServerError);

			// Devolvemos la respuesta
			return ResponseEntity.status(500).body(issuesServerError.getBodyErrorMessage());
		}
	}

	/**
	 * Envia una notificación email al usuario para que sepa que la solución de su incidencia ha cambiado.
	 * @param incidencia La incidencia a notificar.
	 */
	private void enviarEmailActualizacionSolucion(Incidencia incidencia)
	{
		try
		{
			// Creamos la lista de destinatarios
			List<String> destinatarios = Arrays.asList(incidencia.getEmail());

			// Creamos el asunto de la notificación
			String asunto = "La solución de la incidencia ha cambiado" ;
							
			// Creamos el cuerpo de la notificación
			String cuerpo = "Sobre la incidencia creada por ti en la ubicación " + incidencia.getUbicacion().getNombre() + 
			                ", el usuario " + incidencia.getUsuarioCategoria().getNombreResponsable() + " " + 
			                " ha comentado la siguiente solución: " + incidencia.getSolucion() ;

			// Creamos el DTO de la notificación email
			NotificationEmailDto notificationEmailDto = new NotificationEmailDto(destinatarios, null, null, asunto, cuerpo);

			// Enviamos la notificación email
			this.requestNotificacionesEnviarEmail.enviarNotificacionEmail(notificationEmailDto);
		}
		catch (BaseClientException exception)
		{
			// Ya ha sido logueada previamente
		}
		catch (Exception exception)
		{
			// Ya ha sido logueada previamente
			log.error("Error al enviar la notificación email (actualización de solución)", exception);
		}
	}
	/**
	 * Modifica el responsable de una incidencia en el sistema por parte del administrador.
	 * 
	 * Este método recibe el ID de la incidencia, el nombre de la categoría y el email del responsable a actualizar.
	 * Se realiza la validación de los datos y, si son válidos, se actualiza el responsable de la incidencia en la base de datos.
	 * 
	 * @param id El ID de la incidencia a actualizar.
	 * @param nombreCategoria El nombre de la categoría a actualizar.
	 * @param emailResponsable El email del responsable de la categoría a actualizar.
	 * @return Un objeto {@link ResponseEntity} que puede contener:
	 *         <ul>
	 *         <li>Un código de estado 200 (OK) si el responsable de la incidencia se actualiza correctamente.</li>
	 *         <li>Un código de estado 400 (Bad Request) si los datos de la incidencia no son válidos.</li>
	 *         <li>Un código de estado 500 (Internal Server Error) si ocurre un error inesperado.</li>
	 *         </ul>
	 */
	@PreAuthorize("hasRole('" + BaseConstants.ROLE_ADMINISTRADOR + "')")
	@PutMapping("/responsable/")
    public ResponseEntity<?> actualizarResponsable(@RequestHeader Long id, @RequestHeader String nombreCategoria, @RequestHeader String emailResponsable)
	{
		try
		{
			// Validamos y obtenemos la categoría
			UsuarioCategoria usuarioCategoria = this.validarYBuscarUsuarioCategoria(nombreCategoria, emailResponsable);
			
			// Buscamos la incidencia a actualizar
			Incidencia incidencia = this.buscarIncidenciaPorId(id);

			// Actualizamos el responsable de la incidencia
			incidencia.setUsuarioCategoria(usuarioCategoria);

			// Guardamos la incidencia en la base de datos
			this.incidenciaRepository.saveAndFlush(incidencia);

			// Logueamos la incidencia actualizada	
			log.info("Responsable de la incidencia {} actualizado correctamente: {}", id, incidencia);

			// Devolvemos la respuesta
			return ResponseEntity.ok().build();
		}
		catch (IssuesServerError issuesServerError)
		{
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(issuesServerError.getBodyErrorMessage());
		}
		catch (Exception exception)
		{
			// Creamos una excepción genérica para devolver al cliente
			IssuesServerError issuesServerError =  new IssuesServerError(Constants.ERR_GENERICO_CODE, Constants.ERR_GENERICO_MESSAGE, exception);

			// Log de la excepción
			log.error("Excepción genérica al modificar el responsable de la incidencia", issuesServerError);

			// Devolvemos la respuesta
			return ResponseEntity.status(500).body(issuesServerError.getBodyErrorMessage());
		}
	}

	/**
	 * Valida el nombre de la categoría.
	 * @param nombreCategoria El nombre de la categoría.
	 * @param emailResponsable El email del responsable de la categoría.
	 * @return El usuario-categoría encontrado.
	 * @throws IssuesServerError si el nombre de la categoría o el email del responsable no son válidos
	 */
	private UsuarioCategoria validarYBuscarUsuarioCategoria(String nombreCategoria, String emailResponsable) throws IssuesServerError
	{
		// Validamos el nombre de la categoría
		if (nombreCategoria == null || nombreCategoria.isEmpty())
		{
			log.error(Constants.ERR_INCIDENCIA_CATEGORIA_NO_INTRODUCIDA_MESSAGE);
			throw new IssuesServerError(Constants.ERR_INCIDENCIA_CATEGORIA_NO_INTRODUCIDA_CODE, Constants.ERR_INCIDENCIA_CATEGORIA_NO_INTRODUCIDA_MESSAGE);
		}

        // Creamos el ID del usuario-categoría
        UsuarioCategoriaId id = new UsuarioCategoriaId(nombreCategoria, emailResponsable);
		
		// Validamos si el usuario-categoría existe
		Optional<UsuarioCategoria> optionalUsuarioCategoria = this.usuarioCategoriaRepository.findById(id);
		if (!optionalUsuarioCategoria.isPresent())
		{
			log.error(Constants.ERR_USUARIO_CATEGORIA_NO_ENCONTRADA_MESSAGE);
			throw new IssuesServerError(Constants.ERR_USUARIO_CATEGORIA_NO_ENCONTRADA_CODE, Constants.ERR_USUARIO_CATEGORIA_NO_ENCONTRADA_MESSAGE);
		}

		return optionalUsuarioCategoria.get();
	}

	/**
	 * Lista los estados de las incidencias.
	 * 
	 * Este método devuelve una lista de los estados de las incidencias.
	 * 
	 * @return Un objeto {@link ResponseEntity} que puede contener:
	 *         <ul>
	 *         <li>Un código de estado 200 (OK) si la lista de estados se devuelve correctamente.</li>
	 *         <li>Un código de estado 500 (Internal Server Error) si ocurre un error inesperado.</li>
	 *         </ul>
	 */
	@PreAuthorize("hasRole('" + BaseConstants.ROLE_ADMINISTRADOR + "')")
	@GetMapping("/estados/")
	public ResponseEntity<?> listadoEstadoIncidencias()
	{
	    try
	    {
	        // Responder con la lista de estados
	        return ResponseEntity.ok().body(Arrays.asList(Constants.ESTADO_PENDIENTE,
														  Constants.ESTADO_RESUELTA,
														  Constants.ESTADO_CANCELADA,
														  Constants.ESTADO_DUPLICADA,
														  Constants.ESTADO_EN_PROGRESO));
	    }
	    catch (Exception exception)
	    {
            // Creamos una excepción genérica para devolver al cliente
			IssuesServerError issuesServerError =  new IssuesServerError(Constants.ERR_GENERICO_CODE, Constants.ERR_GENERICO_MESSAGE, exception);

			// Log de la excepción
			log.error("Excepción genérica al listar los estados de las incidencias", issuesServerError);

			// Devolvemos la respuesta
			return ResponseEntity.status(500).body(issuesServerError.getBodyErrorMessage());
	    }
	}

	/**
	 * Lista las incidencias ordenadas por fecha.
	 * 
	 * Este método devuelve una lista de incidencias ordenadas por fecha, tanto para el profesor como para el administrador.
	 * 
	 * @param usuario El usuario que lista las incidencias (profesor o administrador).
	 * @param pageable La página de incidencias a listar (paginación).
	 * @return Un objeto {@link ResponseEntity} que puede contener:
	 *         <ul>
	 *         <li>Un código de estado 200 (OK) si la lista de incidencias se devuelve correctamente.</li>
	 *         <li>Un código de estado 500 (Internal Server Error) si ocurre un error inesperado.</li>
	 *         </ul>
	 */
	@PreAuthorize("hasRole('" + BaseConstants.ROLE_PROFESOR + "')")
	@GetMapping("/") 	
	public ResponseEntity<?> listarIncidenciasOrdenadasPorFecha(@AuthenticationPrincipal DtoUsuarioExtended usuario, Pageable pageable)
	{ 	   
		try
		{    
			// Creamos una variable para las incidencias
			Page<IncidenciaDto> incidencias = null ;

			// Si el rol es de profesor, solo buscamos las incidencias del usuario
			if (!usuario.getRoles().contains(BaseConstants.ROLE_ADMINISTRADOR))
			{
				// Buscamos las incidencias del usuario
				incidencias = this.incidenciaRepository.buscarIncidenciaOrdenadaFechaPorUsuario(pageable, usuario.getEmail());
			}
			else
			{
				// Buscamos todas las incidencias
				incidencias = this.incidenciaRepository.buscarIncidenciaOrdenadaFechaPorAdmin(pageable); 	    
			}

			// Devolvemos la respuesta
			return ResponseEntity.ok().body(incidencias);
		}
		catch (Exception exception)
		{
            // Creamos una excepción genérica para devolver al cliente
			IssuesServerError issuesServerError =  new IssuesServerError(Constants.ERR_GENERICO_CODE, Constants.ERR_GENERICO_MESSAGE, exception);

			// Log de la excepción
			log.error("Excepción genérica al listar las incidencias ordenadas por fecha", issuesServerError);

			// Devolvemos la respuesta
			return ResponseEntity.status(500).body(issuesServerError.getBodyErrorMessage());
		}
	}

	/**
	 * Elimina una incidencia de la base de datos.
	 * 
	 * Este método recibe el ID de la incidencia a eliminar.
	 * Se realiza la validación de los datos y, si son válidos, se elimina la incidencia en la base de datos.
	 *
	 * @param usuario El usuario que elimina la incidencia (profesor o administrador).
	 * @param id El ID de la incidencia a eliminar.
	 * @return Un objeto {@link ResponseEntity} que puede contener:
	 *         <ul>
	 *         <li>Un código de estado 200 (OK) si la incidencia se elimina correctamente.</li>
	 *         <li>Un código de estado 400 (Bad Request) si los datos de la incidencia no son válidos.</li>
	 *         <li>Un código de estado 500 (Internal Server Error) si ocurre un error inesperado.</li>
	 *         </ul>
	 */
	@PreAuthorize("hasAnyRole('" + BaseConstants.ROLE_PROFESOR + "', '" + BaseConstants.ROLE_ADMINISTRADOR + "')")
	@DeleteMapping("/")
	public ResponseEntity<?> borrarIncidencia(@AuthenticationPrincipal DtoUsuarioExtended usuario, @RequestHeader Long id)
	{
		try
		{
			// Buscamos la incidencia a eliminar
			Incidencia incidencia = this.buscarIncidenciaPorId(id);

			// Si el rol es de profesor, validamos que el usuario sea el que creó la incidencia o el responsable de la incidencia
			if (!usuario.getRoles().contains(BaseConstants.ROLE_ADMINISTRADOR) && 
			    !usuario.getEmail().equals(incidencia.getEmail()) &&
				!usuario.getEmail().equals(incidencia.getUsuarioCategoria().getId().getEmailResponsable()))
			{
				// Si llegamos aquí es porque el usuario no es el que creó la incidencia
				log.error(Constants.ERR_INCIDENCIA_USUARIO_NO_PERMITIDO_MESSAGE);
				throw new IssuesServerError(Constants.ERR_INCIDENCIA_USUARIO_NO_PERMITIDO_CODE, Constants.ERR_INCIDENCIA_USUARIO_NO_PERMITIDO_MESSAGE);
			}

			// Eliminamos la incidencia de la base de datos
			this.incidenciaRepository.delete(incidencia);

			// Elimina la incidencia de la base de datos y loguea la accion.
			log.info("Incidencia eliminada con éxito: {}", incidencia);

			// Devolvemos la respuesta
			return ResponseEntity.ok().build();
		}
		catch (IssuesServerError exception)
		{
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getBodyErrorMessage()) ;
		}
		catch (Exception exception)
		{
			// Creamos una excepción genérica para devolver al cliente
			IssuesServerError issuesServerError =  new IssuesServerError(Constants.ERR_GENERICO_CODE, Constants.ERR_GENERICO_MESSAGE, exception);

			// Log de la excepción
			log.error("Excepción genérica al eliminar la incidencia", issuesServerError);

			// Devolvemos la respuesta
			return ResponseEntity.status(500).body(issuesServerError.getBodyErrorMessage());
		}
	}

	/**
	 * Busca una incidencia por su ID.
	 * 
	 * Este método recibe el ID de la incidencia a buscar.
	 * Se realiza la validación de los datos y, si son válidos, se busca la incidencia en la base de datos.
	 * 
	 * @param id El ID de la incidencia a buscar.
	 * @return La incidencia encontrada.
	 * @throws IssuesServerError si la incidencia no existe
	 */
	private Incidencia buscarIncidenciaPorId(Long id) throws IssuesServerError
	{
		// Validamos el ID de la incidencia
		if (id == null || id <= 0)
		{
			log.error(Constants.ERR_INCIDENCIA_ID_NO_INTRODUCIDO_MESSAGE);
			throw new IssuesServerError(Constants.ERR_INCIDENCIA_ID_NO_INTRODUCIDO_CODE, Constants.ERR_INCIDENCIA_ID_NO_INTRODUCIDO_MESSAGE);
		}

		// Buscamos la incidencia por su ID
		Optional<Incidencia> optionalIncidencia = this.incidenciaRepository.findById(id);

		// Validamos que la incidencia exista
		if (!optionalIncidencia.isPresent())
		{
			log.error(Constants.ERR_INCIDENCIA_NO_ENCONTRADA_MESSAGE);
			throw new IssuesServerError(Constants.ERR_INCIDENCIA_NO_ENCONTRADA_CODE, Constants.ERR_INCIDENCIA_NO_ENCONTRADA_MESSAGE);
		}

		// Obtenemos la incidencia
		return optionalIncidencia.get();
	}
}