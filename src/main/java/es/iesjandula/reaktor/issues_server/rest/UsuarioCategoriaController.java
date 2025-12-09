package es.iesjandula.reaktor.issues_server.rest;

import java.util.List;
import java.util.Optional;

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
import es.iesjandula.reaktor.issues_server.dto.UsuarioCategoriaDto;
import es.iesjandula.reaktor.issues_server.models.Categoria;
import es.iesjandula.reaktor.issues_server.models.UsuarioCategoria;
import es.iesjandula.reaktor.issues_server.models.ids.UsuarioCategoriaId;
import es.iesjandula.reaktor.issues_server.repository.ICategoriaRepository;
import es.iesjandula.reaktor.issues_server.repository.IUsuarioCategoriaRepository;
import es.iesjandula.reaktor.issues_server.utils.Constants;
import es.iesjandula.reaktor.issues_server.utils.IssuesServerError;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/issues/usuarios_categoria")
public class UsuarioCategoriaController
{
    /**
     * Repositorio de usuarios-categoría.
     */
    @Autowired
    private IUsuarioCategoriaRepository usuarioCategoriaRepository;

    /**
     * Repositorio de categorías.
     */
    @Autowired
    private ICategoriaRepository categoriaRepository;

    /** 
     * Listar todos los responsables
     * <p>
     * Este método permite listar todos los responsables de categorías.
     * </p>
     *
     * @return ResponseEntity con la lista de usuarios-responsables
     */
    @PreAuthorize("hasRole('" + BaseConstants.ROLE_ADMINISTRADOR + "')")
    @GetMapping("/")
    public ResponseEntity<?> listarTodos()
    {
        try
        {
            // Log de la petición
            log.info("Petición para listar todos los usuarios-responsables de categoría");

            // Listamos todos los usuarios-responsables
            List<UsuarioCategoriaDto> usuariosCategoriasDto = this.usuarioCategoriaRepository.buscarTodos();

            // Log de la lista
            log.info("Se han encontrado {} usuarios-responsables", usuariosCategoriasDto.size());

            // Devolvemos la respuesta
            return ResponseEntity.ok(usuariosCategoriasDto);
        }
        catch (Exception exception)
        {
            // Creamos una excepción genérica para devolver al cliente
            IssuesServerError issuesServerError = new IssuesServerError(Constants.ERR_GENERICO_CODE, Constants.ERR_GENERICO_MESSAGE, exception);

            // Log de la excepción
            log.error("Excepción genérica al listar todos los usuarios-responsables de categoría", issuesServerError);

            // Devolvemos la respuesta
            return ResponseEntity.status(500).body(issuesServerError.getBodyErrorMessage());
        }
    }

    /**
     * Crear un responsable para una categoría
     * <p>
     * Este método permite crear un responsable para una categoría.
     * </p>
     *
     * @param nombreCategoria El nombre de la categoría a la que se le asignará el responsable
     * @param nombreResponsable El nombre del responsable a crear
     * @param emailResponsable El email del responsable a crear
     * @return ResponseEntity con el resultado de la operación
     */
    @PreAuthorize("hasRole('" + BaseConstants.ROLE_ADMINISTRADOR + "')")
    @PostMapping("/")
    public ResponseEntity<?> crear(@RequestHeader("nombreCategoria") String nombreCategoria, @RequestHeader("nombreResponsable") String nombreResponsable, @RequestHeader("emailResponsable") String emailResponsable)
    {
        try
        {
            // Log de la petición
            log.info("Petición para crear usuario-responsable '{}' ({}) para la categoría '{}'", nombreResponsable, emailResponsable, nombreCategoria);

            // Buscamos la categoría por su nombre
            Optional<Categoria> optionalCategoria = this.categoriaRepository.findById(nombreCategoria);
            
            // Validamos que la categoría exista
            if (!optionalCategoria.isPresent())
            {
                String errorString = "La categoría '" + nombreCategoria + "' no existe";

                log.error(errorString);
                throw new IssuesServerError(Constants.ERR_CATEGORIA_NO_ENCONTRADA_CODE, errorString);
            }

            // Obtenemos la categoría
            Categoria categoria = optionalCategoria.get();

            // Creamos la relación usuario-categoría
            UsuarioCategoria usuarioCategoria = new UsuarioCategoria();
            usuarioCategoria.setId(new UsuarioCategoriaId(nombreCategoria, nombreResponsable, emailResponsable));
            usuarioCategoria.setCategoria(categoria);

            // Guardamos la relación usuario-categoría
            this.usuarioCategoriaRepository.saveAndFlush(usuarioCategoria);

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
            log.error("Excepción genérica al crear usuario-responsable", issuesServerError);

            // Devolvemos la respuesta
            return ResponseEntity.status(500).body(issuesServerError.getBodyErrorMessage());
        }
    }

    /**
     * Borrar un responsable concreto
     * <p>
     * Este método permite borrar un responsable concreto de una categoría.
     * </p>
     *
     * @param usuarioCategoriaDto DTO con los datos del usuario-responsable a borrar
     * @return ResponseEntity con el resultado de la operación
     */
    @PreAuthorize("hasRole('" + BaseConstants.ROLE_ADMINISTRADOR + "')")
    @DeleteMapping("/")
    public ResponseEntity<?> borrar(@RequestHeader("nombreCategoria") String nombreCategoria, @RequestHeader("nombreResponsable") String nombreResponsable, @RequestHeader("emailResponsable") String emailResponsable)
    {
        try
        {
            // Creamos el ID de la relación usuario-categoría
            UsuarioCategoriaId id = new UsuarioCategoriaId(nombreCategoria, nombreResponsable, emailResponsable);

            // Validamos que la relación usuario-categoría exista
            if (!this.usuarioCategoriaRepository.existsById(id))
            {
                String errorString = "No se encontró el usuario-responsable '" + nombreResponsable + "' para la categoría '" + nombreCategoria + "'";

                log.error(errorString);
                throw new IssuesServerError(Constants.ERR_USUARIO_CATEGORIA_NO_ENCONTRADA_CODE, errorString);
            }

            // Borramos la relación usuario-categoría
            this.usuarioCategoriaRepository.deleteById(id);

            // Log de la eliminación
            log.info("Usuario-responsable '{}' ({}) borrado correctamente de la categoría '{}'", nombreResponsable, emailResponsable, nombreCategoria);

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
            log.error("Excepción genérica al borrar usuario-responsable", issuesServerError);

            // Devolvemos la respuesta
            return ResponseEntity.status(500).body(issuesServerError.getBodyErrorMessage());
        }
    }
}
