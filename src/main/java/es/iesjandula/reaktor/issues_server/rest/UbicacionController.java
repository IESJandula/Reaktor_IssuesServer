package es.iesjandula.reaktor.issues_server.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.iesjandula.reaktor.base.utils.BaseConstants;
import es.iesjandula.reaktor.issues_server.models.Ubicacion;
import es.iesjandula.reaktor.issues_server.repository.IUbicacionRepository;
import es.iesjandula.reaktor.issues_server.utils.Constants;
import es.iesjandula.reaktor.issues_server.utils.IssuesServerError;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/issues/ubicaciones")
public class UbicacionController
{
    @Autowired
    private IUbicacionRepository ubicacionRepository;

    /** 
     * Listar todas las ubicaciones (para el desplegable, PROFESOR puede verlas)
     *
     * @return ResponseEntity con la lista de ubicaciones
     */
    @PreAuthorize("hasRole('" + BaseConstants.ROLE_PROFESOR + "')")
    @GetMapping("/")
    public ResponseEntity<?> listarUbicaciones()
    {
        try
        {
            return ResponseEntity.ok(this.ubicacionRepository.buscarTodasLasUbicaciones());
        }
        catch (Exception exception)
        {
            // Creamos una excepción genérica para devolver al cliente
            IssuesServerError issuesServerError = new IssuesServerError(Constants.ERR_GENERICO_CODE, Constants.ERR_GENERICO_MESSAGE, exception);

            // Log de la excepción
            log.error("Excepción genérica al listar las ubicaciones", issuesServerError);

            // Devolvemos la respuesta
            return ResponseEntity.status(500).body(issuesServerError.getBodyErrorMessage());
        }
    }

    /** 
     * Crear nueva ubicación (solo administración)
     * 
     * @param nombre El nombre de la ubicación a crear
     * @return ResponseEntity con el resultado de la creación
     */
    @PreAuthorize("hasRole('" + BaseConstants.ROLE_ADMINISTRADOR + "')")
    @PostMapping("/")
    public ResponseEntity<?> crearUbicacion(@RequestHeader("nombre") String nombre)
    {
        try
        {
            // Validamos que el nombre de la ubicación no sea nulo o en blanco
            if (nombre == null || nombre.isEmpty() || nombre.isBlank())
            {
                String errorString = "El nombre de la ubicación es obligatorio.";

                log.error(errorString);
                throw new IssuesServerError(Constants.ERR_UBICACION_NOMBRE_OBLIGATORIO_CODE, errorString);
            }

            // Validamos que la ubicación no exista
            if (this.ubicacionRepository.existsById(nombre))
            {
                String errorString = "Ya existe una ubicación con ese nombre.";

                log.error(errorString);
                throw new IssuesServerError(Constants.ERR_UBICACION_YA_EXISTE_CODE, errorString);
            }

            // Creamos la ubicación
            Ubicacion ubicacion = new Ubicacion();
            ubicacion.setNombre(nombre);
            
            // Guardamos la ubicación
            this.ubicacionRepository.saveAndFlush(ubicacion);

            // Log de la ubicación creada
            log.info("Ubicacion creada: {}", ubicacion);

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
            IssuesServerError issuesServerError = new IssuesServerError(Constants.ERR_GENERICO_CODE, Constants.ERR_GENERICO_MESSAGE, exception);

            // Log de la excepción
            log.error("Excepción genérica al crear la ubicación", issuesServerError);

            // Devolvemos la respuesta
            return ResponseEntity.status(500).body(issuesServerError.getBodyErrorMessage());
        }
    }
    
    /** 
     * Borrar ubicación (solo administración)
     * 
     * @param nombre El nombre de la ubicación a borrar
     * @return ResponseEntity con el resultado de la eliminación
     */ 
    @PreAuthorize("hasRole('" + BaseConstants.ROLE_ADMINISTRADOR + "')")
    @DeleteMapping("/")
    public ResponseEntity<?> borrarUbicacion(@RequestHeader("nombre") String nombre)
    {
        try
        {
            // Verificamos que el nombre de la ubicación no sea nulo o en blanco
            if (nombre == null || nombre.isEmpty())
            {
                log.error(Constants.ERR_UBICACION_NO_INFORMADA_MESSAGE);
                throw new IssuesServerError(Constants.ERR_UBICACION_NO_INFORMADA_CODE, Constants.ERR_UBICACION_NO_INFORMADA_MESSAGE);
            }

            // Log de la petición
            log.info("Petición para borrar ubicación '{}'", nombre);

            // Verificamos que la ubicación exista
            if (!this.ubicacionRepository.existsById(nombre))
            {
                String errorString = "No se encontró la ubicación con ese nombre.";

                log.error(errorString);
                throw new IssuesServerError(Constants.ERR_UBICACION_NO_ENCONTRADA_CODE, errorString);
            }

            // Borramos la ubicación
            this.ubicacionRepository.deleteById(nombre);

            // Log de la ubicación eliminada
            log.info("Ubicación eliminada con ID '{}'", nombre);

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
            IssuesServerError issuesServerError = new IssuesServerError(Constants.ERR_GENERICO_CODE, Constants.ERR_GENERICO_MESSAGE, exception);

            // Log de la excepción
            log.error("Excepción genérica al borrar la ubicación", issuesServerError);

            // Devolvemos la respuesta
            return ResponseEntity.status(500).body(issuesServerError.getBodyErrorMessage());
        }
    }
}
