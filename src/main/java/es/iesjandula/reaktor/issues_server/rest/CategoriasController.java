package es.iesjandula.reaktor.issues_server.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.iesjandula.reaktor.base.utils.BaseConstants;
import es.iesjandula.reaktor.issues_server.dto.CategoriaDto;
import es.iesjandula.reaktor.issues_server.models.Categoria;
import es.iesjandula.reaktor.issues_server.repository.ICategoriaRepository;
import es.iesjandula.reaktor.issues_server.repository.IIncidenciaRepository;
import es.iesjandula.reaktor.issues_server.utils.Constants;
import es.iesjandula.reaktor.issues_server.utils.IssuesServerError;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/issues/categorias")
public class CategoriasController
{
    /***
     * Repositorio de categorías de incidencias
     */
    @Autowired
    private ICategoriaRepository categoriaRepository;

    /***
     * Repositorio de incidencias
     */
    @Autowired
    private IIncidenciaRepository incidenciaRepository;

    /***
     * Listar todas las categorías de incidencias
     *
     * @return ResponseEntity con la lista de categorías
     */
    @PreAuthorize("hasRole('" + BaseConstants.ROLE_ADMINISTRADOR + "')")
    @GetMapping
    public ResponseEntity<?> listar()
    {
		try
		{
            // Log de la petición   
            log.info("Petición para listar todas las categorías");

            // Buscamos todas las categorías
            List<CategoriaDto> categoriasDto = this.categoriaRepository.buscarTodasLasCategorias();

            // Log de la respuesta
            log.info("Se han encontrado {} categorías", categoriasDto.size());

            // Devolvemos la respuesta
            return ResponseEntity.ok(categoriasDto) ;
		}
		catch (Exception exception)
		{
            // Creamos una excepción genérica para devolver al cliente
			IssuesServerError issuesServerError =  new IssuesServerError(Constants.ERR_GENERICO_CODE, Constants.ERR_GENERICO_MESSAGE, exception);

			// Log de la excepción
			log.error("Excepción genérica al obtener las categorías", issuesServerError);

			// Devolvemos la respuesta
			return ResponseEntity.status(500).body(issuesServerError.getBodyErrorMessage());
		}
    }

    /***
     * Crear una nueva categoría de incidencia
     *
     * @param nombre Nombre de la categoría de incidencia a crear
     * @return ResponseEntity con la categoría creada
     */
    @PreAuthorize("hasRole('" + BaseConstants.ROLE_ADMINISTRADOR + "')")
    @PostMapping
    public ResponseEntity<?> crear(@RequestHeader("nombre") String nombre)
    {
        try
        {
            // Validamos que la categoría no exista
            log.info("Petición para crear nueva categoría con nombre '{}'", nombre);

            // Creamos la categoría
            Categoria categoria = new Categoria();
            categoria.setNombre(nombre);

            // Guardamos la categoría
            this.categoriaRepository.saveAndFlush(categoria);

            // Log correcto
            log.info("Categoría '{}' creada correctamente", nombre);

            // Devolvemos la respuesta
            return ResponseEntity.ok().build();
        }
        catch (Exception exception)
        {
            // Creamos una excepción genérica para devolver al cliente
            IssuesServerError issuesServerError = new IssuesServerError(Constants.ERR_GENERICO_CODE, Constants.ERR_GENERICO_MESSAGE, exception);

            // Log de la excepción
            log.error("Excepción genérica al crear la categoría", issuesServerError);

            // Devolvemos la respuesta
            return ResponseEntity.status(500).body(issuesServerError.getBodyErrorMessage());
        }
    }

    /***
     * Borrar una categoría de incidencia (solo si no tiene incidencias asociadas)
     * @param nombreCategoria Nombre de la categoría de incidencia a borrar
     * @return ResponseEntity con el resultado de la borrada
     */
    @PreAuthorize("hasRole('" + BaseConstants.ROLE_ADMINISTRADOR + "')")
    @DeleteMapping
    public ResponseEntity<?> borrar(@RequestHeader("nombre") String nombre)
    {
        try
        {
            // Log de la petición
            log.info("Petición para borrar categoría '{}'", nombre);

            // Verificamos que la categoría exista
            if (!this.categoriaRepository.existsById(nombre))
            {
                String errorMessage = "No se encontró la categoría '" + nombre + "' para borrar";
                
                log.error(errorMessage);
                throw new IssuesServerError(Constants.ERR_CATEGORIA_NO_ENCONTRADA_CODE, errorMessage);
            }
    
            // Verificamos si la categoría tiene incidencias asociadas
            if (this.incidenciaRepository.validarSiExistenIncidenciasAsociadasACategoria(nombre))
            {
                String errorMessage = "No se puede borrar la categoría '" + nombre + "' porque existen incidencias asociadas.";
                log.error(errorMessage);
                throw new IssuesServerError(Constants.ERR_CATEGORIA_NO_BORRABLE_CODE, errorMessage);
            }
    
            // Borrar la categoría
            this.categoriaRepository.deleteById(nombre);

            // Log de éxito
            log.info("Categoría '{}' borrada correctamente", nombre);
    
            // Devolvemos la respuesta correcta
            return ResponseEntity.noContent().build();
        }
        catch (IssuesServerError issuesServerError)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(issuesServerError.getBodyErrorMessage());
        }
        catch (Exception exception)
        {
            IssuesServerError issuesServerError = new IssuesServerError(Constants.ERR_GENERICO_CODE, Constants.ERR_GENERICO_MESSAGE, exception);

            log.error("Excepción genérica al borrar la categoría", issuesServerError);
            return ResponseEntity.status(500).body(issuesServerError.getBodyErrorMessage());
        }
    }
}
