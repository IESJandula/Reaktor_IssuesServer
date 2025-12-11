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
import es.iesjandula.reaktor.base.utils.BaseConstants;
import es.iesjandula.reaktor.issues_server.dtos.FiltroBusquedaDto;
import es.iesjandula.reaktor.issues_server.dtos.IncidenciaDto;
import es.iesjandula.reaktor.issues_server.dtos.UsuarioCategoriaDto;
import es.iesjandula.reaktor.issues_server.models.Categoria;
import es.iesjandula.reaktor.issues_server.models.Incidencia;
import es.iesjandula.reaktor.issues_server.models.Ubicacion;
import es.iesjandula.reaktor.issues_server.repository.ICategoriaRepository;
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

	/**
	 * Repositorio de ubicaciones.
	 */
	@Autowired
	private IUbicacionRepository ubicacionRepository;
	
	/**
	 * Repositorio de incidencias.
	 */
	@Autowired
	private IIncidenciaRepository incidenciaRepository;
	
	/**
	 * Repositorio de categorías.
	 */
	@Autowired
	private ICategoriaRepository categoriaIncidenciaRepository;
	
	/**
	 * Repositorio de usuarios-categoría.
	 */
	@Autowired
	private IUsuarioCategoriaRepository usuarioCategoriaRepository;

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

			// Buscamos la categoría
			Optional<Categoria> optionalCategoria = this.categoriaIncidenciaRepository.findById(nombreCategoria) ;

			// Validamos que la categoría exista
			if (!optionalCategoria.isPresent())
			{
				log.error(Constants.ERR_CATEGORIA_NO_ENCONTRADA_MESSAGE);
				throw new IssuesServerError(Constants.ERR_CATEGORIA_NO_ENCONTRADA_CODE, Constants.ERR_CATEGORIA_NO_ENCONTRADA_MESSAGE);
			}

			// Obtenemos la categoría
			Categoria categoria = optionalCategoria.get();

            // Buscamos los responsables de la categoría (el primero de la lista)
            List<UsuarioCategoriaDto> responsablesCategoriaDto = this.usuarioCategoriaRepository.buscarResponsablesPorCategoria(nombreCategoria);

			// Validamos que los responsables existan
			if (responsablesCategoriaDto.isEmpty())
			{
				log.error(Constants.ERR_USUARIO_CATEGORIA_NO_ENCONTRADA_MESSAGE);
				throw new IssuesServerError(Constants.ERR_USUARIO_CATEGORIA_NO_ENCONTRADA_CODE, Constants.ERR_USUARIO_CATEGORIA_NO_ENCONTRADA_MESSAGE);
			}

			// Obtenemos el primer responsable
			UsuarioCategoriaDto responsableCategoriaDto = responsablesCategoriaDto.get(0);

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
			nuevaIncidencia.setCategoria(categoria);
			nuevaIncidencia.setEmailResponsable(responsableCategoriaDto.getEmailResponsable());

            // Guardamos la incidencia en la base de datos
            this.incidenciaRepository.saveAndFlush(nuevaIncidencia);

			// Logueamos la incidencia creada
            log.info("Incidencia creada correctamente: {}", nuevaIncidencia);

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
	 * Actualiza una incidencia en el sistema por parte del responsable de la incidencia.
	 * 
	 * Este método recibe un objeto {@link IncidenciaDto} que contiene la información de la incidencia a
	 * actualizar. Se realiza la validación de los datos y, si son válidos, se actualiza la incidencia en la base de datos.
	 * 
	 * @param id El ID de la incidencia.
	 * @param estado El estado de la incidencia.
	 * @param solucion La solución de la incidencia.
	 * @param emailResponsable El email del responsable de la incidencia.
	 * @return Un objeto {@link ResponseEntity} que puede contener:
	 *         <ul>
	 *         <li>Un código de estado 200 (OK) si la incidencia se actualiza correctamente.</li>
	 *         <li>Un código de estado 400 (Bad Request) si los datos de la incidencia no son válidos.</li>
	 *         <li>Un código de estado 500 (Internal Server Error) si ocurre un error inesperado.</li>
	 *         </ul>
	 */
    @PreAuthorize("hasRole('" + BaseConstants.ROLE_ADMINISTRADOR + "')")
    @PutMapping("/")
    public ResponseEntity<?> modificarIncidencia(@RequestHeader Long id,
												 @RequestHeader String estado,
												 @RequestHeader String solucion,
												 @RequestHeader String emailResponsable)
    {
        try
        {
			// Validamos los datos de la incidencia
			this.validarModificarIncidencia(id, estado, solucion, emailResponsable);

            // Buscamos la incidencia a actualizar
            Optional<Incidencia> optionalIncidencia = this.incidenciaRepository.findById(id);

			// Validamos que la incidencia exista
            if (!optionalIncidencia.isPresent())
            {
                log.error(Constants.ERR_INCIDENCIA_NO_ENCONTRADA_MESSAGE);
                throw new IssuesServerError(Constants.ERR_INCIDENCIA_NO_ENCONTRADA_CODE, Constants.ERR_INCIDENCIA_NO_ENCONTRADA_MESSAGE);
            }

			// Obtenemos la incidencia
			Incidencia incidencia = optionalIncidencia.get();

            // Actualizamos los campos que vienen en el dto
			incidencia.setEstado(estado);
			incidencia.setSolucion(solucion);
			incidencia.setEmailResponsable(emailResponsable);

            // Guardamos la incidencia en la base de datos
            this.incidenciaRepository.saveAndFlush(incidencia);

            log.info("Incidencia modificada correctamente: {}", incidencia);
            return ResponseEntity.ok("Incidencia modificada con éxito");
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
			log.error("Excepción genérica al modificar la incidencia", issuesServerError.getBodyErrorMessage());

			// Devolvemos la respuesta
			return ResponseEntity.status(500).body(issuesServerError.getBodyErrorMessage());
        }
    }

	/**
	 * Valida los datos de la incidencia.
	 * @param id El ID de la incidencia.
	 * @param estado El estado de la incidencia.
	 * @param solucion La solución de la incidencia.
	 * @param emailResponsable El email del responsable de la incidencia.
	 * @throws IssuesServerError si los datos de la incidencia no son válidos
	 */
	private void validarModificarIncidencia(Long id, String estado, String solucion, String emailResponsable) throws IssuesServerError
	{
		// Realizamos las validaciones comunes
		if (id == null || id <= 0)
		{
			log.error(Constants.ERR_INCIDENCIA_ID_NO_INTRODUCIDO_MESSAGE);
			throw new IssuesServerError(Constants.ERR_INCIDENCIA_ID_NO_INTRODUCIDO_CODE, Constants.ERR_INCIDENCIA_ID_NO_INTRODUCIDO_MESSAGE);
		}

		// Validamos el email del responsable
		if (emailResponsable == null || emailResponsable.isEmpty())
		{
			log.error(Constants.ERR_INCIDENCIA_EMAIL_RESPONSABLE_NO_INTRODUCIDO_MESSAGE);
			throw new IssuesServerError(Constants.ERR_INCIDENCIA_EMAIL_RESPONSABLE_NO_INTRODUCIDO_CODE, Constants.ERR_INCIDENCIA_EMAIL_RESPONSABLE_NO_INTRODUCIDO_MESSAGE);
		}

		// Validamos el estado de la incidencia
		if (estado == null || estado.isEmpty())
		{
			log.error(Constants.ERR_INCIDENCIA_ESTADO_NO_INTRODUCIDO_MESSAGE);
			throw new IssuesServerError(Constants.ERR_INCIDENCIA_ESTADO_NO_INTRODUCIDO_CODE, Constants.ERR_INCIDENCIA_ESTADO_NO_INTRODUCIDO_MESSAGE);
		}
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
			log.error("Excepción genérica al listar los estados de las incidencias", issuesServerError.getBodyErrorMessage());

			// Devolvemos la respuesta
			return ResponseEntity.status(500).body(issuesServerError.getBodyErrorMessage());
	    }
	}

	/**
	 * Lista las incidencias ordenadas por fecha.
	 * 
	 * Este método devuelve una lista de incidencias ordenadas por fecha.
	 * 
	 * @param pageable La página de incidencias a listar.
	 * @return Un objeto {@link ResponseEntity} que puede contener:
	 *         <ul>
	 *         <li>Un código de estado 200 (OK) si la lista de incidencias se devuelve correctamente.</li>
	 *         <li>Un código de estado 500 (Internal Server Error) si ocurre un error inesperado.</li>
	 *         </ul>
	 */
	@PreAuthorize("hasRole('" + BaseConstants.ROLE_PROFESOR + "')")
	@GetMapping("/") 	
	public ResponseEntity<?> listarIncidenciasOrdenadasPorFecha(Pageable pageable)
	{ 	   
		try
		{     
			// Buscamos las incidencias ordenadas por fecha
			Page<IncidenciaDto> incidencias = this.incidenciaRepository.buscarIncidenciaOrdenadaFecha(pageable); 	    

			// Devolvemos la respuesta
			return ResponseEntity.ok().body(incidencias);
		}
		catch (Exception exception)
		{
            // Creamos una excepción genérica para devolver al cliente
			IssuesServerError issuesServerError =  new IssuesServerError(Constants.ERR_GENERICO_CODE, Constants.ERR_GENERICO_MESSAGE, exception);

			// Log de la excepción
			log.error("Excepción genérica al listar las incidencias ordenadas por fecha", issuesServerError.getBodyErrorMessage());

			// Devolvemos la respuesta
			return ResponseEntity.status(500).body(issuesServerError.getBodyErrorMessage());
		}
	}

	/**
	 * Elimina una incidencia de la base de datos.
	 * 
	 * Este método recibe un objeto {@link IncidenciaDto} que contiene los detalles de la incidencia a eliminar.
	 * Se realiza la validación de los datos y, si son válidos, se elimina la incidencia en la base de datos.
	 *
	 * @param usuario El usuario que elimina la incidencia.
	 * @param id El ID de la incidencia.
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
			Optional<Incidencia> optionalIncidencia = this.incidenciaRepository.findById(id);

			// Validamos que la incidencia exista
			if (!optionalIncidencia.isPresent())
			{
				log.error(Constants.ERR_INCIDENCIA_NO_ENCONTRADA_MESSAGE);
				throw new IssuesServerError(Constants.ERR_INCIDENCIA_NO_ENCONTRADA_CODE, Constants.ERR_INCIDENCIA_NO_ENCONTRADA_MESSAGE);
			}

			// Obtenemos la incidencia
			Incidencia incidencia = optionalIncidencia.get();

			// Si el rol es de profesor, validamos que el usuario sea el que creó la incidencia
			if (!usuario.getRoles().contains(BaseConstants.ROLE_ADMINISTRADOR) && !usuario.getEmail().equals(incidencia.getEmail()))
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
			log.error("Excepción genérica al eliminar la incidencia", issuesServerError.getBodyErrorMessage());

			// Devolvemos la respuesta
			return ResponseEntity.status(500).body(issuesServerError.getBodyErrorMessage());
		}
	}
}