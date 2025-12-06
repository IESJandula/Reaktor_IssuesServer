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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.iesjandula.reaktor.base.security.models.DtoUsuarioExtended;
import es.iesjandula.reaktor.base.utils.BaseConstants;
import es.iesjandula.reaktor.issues_server.dto.CrearIncidenciaUsuarioDto;
import es.iesjandula.reaktor.issues_server.dto.FiltroBusquedaDto;
import es.iesjandula.reaktor.issues_server.dto.IncidenciaDto;
import es.iesjandula.reaktor.issues_server.dto.ModificarIncidenciaResponsableDto;
import es.iesjandula.reaktor.issues_server.dto.UsuarioCategoriaDto;
import es.iesjandula.reaktor.issues_server.models.Categoria;
import es.iesjandula.reaktor.issues_server.models.Incidencia;
import es.iesjandula.reaktor.issues_server.models.ids.IncidenciaId;
import es.iesjandula.reaktor.issues_server.repository.ICategoriaRepository;
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
 * @see FiltroBusquedaDto
 * @see IncidenciaDto
 * @see Incidencia
 * @see IIncidenciaRepository
 * @see IncidenciaMapper
 */
@Slf4j 
@RestController
@RequestMapping(value = "/issues/incidencias")
public class IncidenciaController
{
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
	 * @param crearIncidenciaUsuarioDto El objeto {@link CrearIncidenciaUsuarioDto} que contiene los datos de la incidencia a crear.
	 * @return Un objeto {@link ResponseEntity} que puede contener:
	 *         <ul>
	 *         <li>Un código de estado 200 (OK) si la incidencia se crea correctamente.</li>
	 *         <li>Un código de estado 400 (Bad Request) si los datos de la incidencia no son válidos.</li>
	 *         <li>Un código de estado 500 (Internal Server Error) si ocurre un error inesperado.</li>
	 *         </ul>
	 */
    @PreAuthorize("hasRole('" + BaseConstants.ROLE_PROFESOR + "')")
    @PostMapping("/")
    public ResponseEntity<?> crearIncidencia(@AuthenticationPrincipal DtoUsuarioExtended usuario, @RequestBody CrearIncidenciaUsuarioDto crearIncidenciaUsuarioDto) 
    {
        try 
        {
            // Validaciones previas sobre los datos de la incidencia
			this.validarIncidencia(crearIncidenciaUsuarioDto);

			// Buscamos la categoría
			Optional<Categoria> optionalCategoria = this.categoriaIncidenciaRepository.findById(crearIncidenciaUsuarioDto.getNombreCategoria()) ;

			// Validamos que la categoría exista
			if (!optionalCategoria.isPresent())
			{
				log.error(Constants.ERR_CATEGORIA_NO_ENCONTRADA_MESSAGE);
				throw new IssuesServerError(Constants.ERR_CATEGORIA_NO_ENCONTRADA_CODE, Constants.ERR_CATEGORIA_NO_ENCONTRADA_MESSAGE);
			}

			// Obtenemos la categoría
			Categoria categoria = optionalCategoria.get();

            // Buscamos los responsables de la categoría (el primero de la lista)
            List<UsuarioCategoriaDto> responsablesCategoriaDto = this.usuarioCategoriaRepository.buscarResponsablesPorCategoria(crearIncidenciaUsuarioDto.getNombreCategoria());

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
			nuevaIncidencia.setUbicacion(crearIncidenciaUsuarioDto.getUbicacion());
			nuevaIncidencia.setEmail(usuario.getEmail());
			nuevaIncidencia.setFecha(LocalDateTime.now());
			nuevaIncidencia.setDescripcion(crearIncidenciaUsuarioDto.getDescripcion());
			nuevaIncidencia.setEstado(Constants.ESTADO_PENDIENTE);
			nuevaIncidencia.setComentario(null);
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
	 * @param crearIncidenciaUsuarioDto El objeto {@link CrearIncidenciaUsuarioDto} que contiene los datos de la incidencia a validar.
	 * @throws IssuesServerError si los datos de la incidencia no son válidos
	 */
	private void validarIncidencia(CrearIncidenciaUsuarioDto crearIncidenciaUsuarioDto) throws IssuesServerError
	{
		if (crearIncidenciaUsuarioDto.getUbicacion() == null || crearIncidenciaUsuarioDto.getUbicacion().isEmpty())
		{
			log.error(Constants.ERR_INCIDENCIA_UBICACION_NO_INTRODUCIDA_MESSAGE);
			throw new IssuesServerError(Constants.ERR_INCIDENCIA_UBICACION_NO_INTRODUCIDA_CODE, Constants.ERR_INCIDENCIA_UBICACION_NO_INTRODUCIDA_MESSAGE);
		}

		if (crearIncidenciaUsuarioDto.getDescripcion() == null || crearIncidenciaUsuarioDto.getDescripcion().isEmpty())
		{
			log.error(Constants.ERR_INCIDENCIA_DESCRIPCION_NO_INTRODUCIDA_MESSAGE);
			throw new IssuesServerError(Constants.ERR_INCIDENCIA_DESCRIPCION_NO_INTRODUCIDA_CODE, Constants.ERR_INCIDENCIA_DESCRIPCION_NO_INTRODUCIDA_MESSAGE);
		}

		if (crearIncidenciaUsuarioDto.getNombreCategoria() == null || crearIncidenciaUsuarioDto.getNombreCategoria().isEmpty())
		{
			log.error(Constants.ERR_INCIDENCIA_CATEGORIA_NO_INTRODUCIDA_MESSAGE);
			throw new IssuesServerError(Constants.ERR_INCIDENCIA_CATEGORIA_NO_INTRODUCIDA_CODE, Constants.ERR_INCIDENCIA_CATEGORIA_NO_INTRODUCIDA_MESSAGE);
		}
	}

	/**
	 * Actualiza una incidencia en el sistema por parte del responsable de la incidencia.
	 * 
	 * Este método recibe un objeto {@link ModificarIncidenciaResponsableDto} que contiene la información de la incidencia a
	 * actualizar. Se realiza la validación de los datos y, si son válidos, se actualiza la incidencia en la base de datos.
	 * 
	 * @param modificarIncidenciaResponsableDto El objeto {@link ModificarIncidenciaResponsableDto} que contiene la información de la incidencia a actualizar.
	 * @return Un objeto {@link ResponseEntity} que puede contener:
	 *         <ul>
	 *         <li>Un código de estado 200 (OK) si la incidencia se actualiza correctamente.</li>
	 *         <li>Un código de estado 400 (Bad Request) si los datos de la incidencia no son válidos.</li>
	 *         <li>Un código de estado 500 (Internal Server Error) si ocurre un error inesperado.</li>
	 *         </ul>
	 */
    @PreAuthorize("hasRole('" + BaseConstants.ROLE_ADMINISTRADOR + "')")
    @PutMapping("/")
    public ResponseEntity<?> modificarIncidencia(@RequestBody ModificarIncidenciaResponsableDto modificarIncidenciaResponsableDto)
    {
        try
        {
			// Logueamos la incidencia a actualizar
            log.info("Modificar incidencia DTO: {}", modificarIncidenciaResponsableDto);

			// Creamos un identificador compuesto para la incidencia
			IncidenciaId incidenciaId = new IncidenciaId(modificarIncidenciaResponsableDto.getUbicacion(), modificarIncidenciaResponsableDto.getEmailDocente(), modificarIncidenciaResponsableDto.getFecha());

            // Buscamos la incidencia a actualizar
            Optional<Incidencia> optionalIncidencia = this.incidenciaRepository.findById(incidenciaId);

			// Validamos que la incidencia exista
            if (!optionalIncidencia.isPresent())
            {
                log.error(Constants.ERR_INCIDENCIA_NO_ENCONTRADA_MESSAGE);
                throw new IssuesServerError(Constants.ERR_INCIDENCIA_NO_ENCONTRADA_CODE, Constants.ERR_INCIDENCIA_NO_ENCONTRADA_MESSAGE);
            }

			// Obtenemos la incidencia
			Incidencia incidencia = optionalIncidencia.get();

            // Actualizamos los campos que vienen en el dto
			incidencia.setEstado(modificarIncidenciaResponsableDto.getEstado());
			incidencia.setComentario(modificarIncidenciaResponsableDto.getComentario());
			incidencia.setEmailResponsable(modificarIncidenciaResponsableDto.getEmailResponsable());

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
	@GetMapping("/estados")
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
	 * @param pageable
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
			Page<Incidencia> incidencias = this.incidenciaRepository.buscarIncidenciaOrdenadaFecha(pageable); 	    

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
	 * @param incidenciaDto El objeto {@link IncidenciaDto} que contiene los detalles de la incidencia a eliminar.
	 * @return Un objeto {@link ResponseEntity} que puede contener:
	 *         <ul>
	 *         <li>Un código de estado 200 (OK) si la incidencia se elimina correctamente.</li>
	 *         <li>Un código de estado 400 (Bad Request) si los datos de la incidencia no son válidos.</li>
	 *         <li>Un código de estado 500 (Internal Server Error) si ocurre un error inesperado.</li>
	 *         </ul>
	 */
	@PreAuthorize("hasRole('" + BaseConstants.ROLE_PROFESOR + "')")
	@DeleteMapping("/")
	public ResponseEntity<?> borraIncidencia(@RequestBody(required = true) IncidenciaDto incidenciaDTO)
	{
		try
		{
			// Creamos un identificador compuesto para la incidencia
			IncidenciaId incidenciaId = new IncidenciaId(incidenciaDTO.getUbicacion(), incidenciaDTO.getCorreoDocente(), incidenciaDTO.getFechaIncidencia());

			// Buscamos la incidencia a eliminar
			Optional<Incidencia> optionalIncidencia = this.incidenciaRepository.findById(incidenciaId);

			// Validamos que la incidencia exista
			if (!optionalIncidencia.isPresent())
			{
				log.error(Constants.ERR_INCIDENCIA_NO_ENCONTRADA_MESSAGE);
				throw new IssuesServerError(Constants.ERR_INCIDENCIA_NO_ENCONTRADA_CODE, Constants.ERR_INCIDENCIA_NO_ENCONTRADA_MESSAGE);
			}

			// Obtenemos la incidencia
			Incidencia incidencia = optionalIncidencia.get();

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