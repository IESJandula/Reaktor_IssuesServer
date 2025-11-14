package es.iesjandula.reaktor.issues_server.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import es.iesjandula.reaktor.base.utils.BaseConstants;
import es.iesjandula.reaktor.issues_server.dto.UsuarioCategoriaDTO;
import es.iesjandula.reaktor.issues_server.models.CategoriaIncidenciaEntity;
import es.iesjandula.reaktor.issues_server.models.UsuarioCategoriaEntity;
import es.iesjandula.reaktor.issues_server.models.ids.UsuarioCategoriaId;
import es.iesjandula.reaktor.issues_server.repository.ICategoriaIncidenciaRepository;
import es.iesjandula.reaktor.issues_server.repository.IUsuarioCategoriaRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@CrossOrigin("*")
@RequestMapping("/issues/usuarios-categoria")
public class UsuarioCategoriaController {

    @Autowired
    private IUsuarioCategoriaRepository usuarioCategoriaRepository;

    @Autowired
    private ICategoriaIncidenciaRepository categoriaRepository;

    /** Listar todos los responsables */
    @PreAuthorize("hasRole('" + BaseConstants.ROLE_ADMINISTRADOR + "')")
    @GetMapping
    public List<UsuarioCategoriaEntity> listarTodos() {
        log.info("Petición para listar todos los usuarios-responsables de categoría");

        List<UsuarioCategoriaEntity> lista = this.usuarioCategoriaRepository.findAll();

        log.info("Se han encontrado {} usuarios-responsables", lista.size());
        return lista;
    }

    /** Crear un responsable para una categoría */
    @PreAuthorize("hasRole('" + BaseConstants.ROLE_ADMINISTRADOR + "')")
    @PostMapping
    public ResponseEntity<UsuarioCategoriaDTO> crear(@RequestBody UsuarioCategoriaDTO dto) {
        log.info(
            "Petición para crear usuario-responsable '{}' ({}) para la categoría '{}'",
            dto.getNombreResponsable(),
            dto.getCorreoResponsable(),
            dto.getNombreCategoria()
        );

        // 1. Cargamos la categoría desde BD (entidad PERSISTENTE)
        CategoriaIncidenciaEntity categoria = categoriaRepository
                .findById(dto.getNombreCategoria())
                .orElseThrow(() -> {
                    String msg = "La categoría '" + dto.getNombreCategoria() + "' no existe";
                    log.error("Error creando usuario-responsable: {}", msg);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, msg);
                });

        // 2. Creamos la relación usuario-categoría
        UsuarioCategoriaEntity usuario = new UsuarioCategoriaEntity();
        usuario.setNombreCategoria(dto.getNombreCategoria());
        usuario.setNombreResponsable(dto.getNombreResponsable());
        usuario.setCorreoResponsable(dto.getCorreoResponsable());
        usuario.setCategoria(categoria); // entidad gestionada

        // 3. Guardamos
        usuarioCategoriaRepository.save(usuario);

        log.info(
            "Usuario-responsable '{}' ({}) creado correctamente para la categoría '{}'",
            dto.getNombreResponsable(),
            dto.getCorreoResponsable(),
            dto.getNombreCategoria()
        );

        // Devolvemos el DTO plano
        return ResponseEntity.ok(dto);
    }

    /** Borrar un responsable concreto */
    @PreAuthorize("hasRole('" + BaseConstants.ROLE_ADMINISTRADOR + "')")
    @DeleteMapping
    public ResponseEntity<?> borrar(@RequestBody UsuarioCategoriaDTO dto) {
        UsuarioCategoriaId id = new UsuarioCategoriaId(
                dto.getNombreCategoria(),
                dto.getNombreResponsable(),
                dto.getCorreoResponsable()
        );

        log.info(
            "Petición para borrar usuario-responsable '{}' ({}) de la categoría '{}'",
            dto.getNombreResponsable(),
            dto.getCorreoResponsable(),
            dto.getNombreCategoria()
        );

        if (!usuarioCategoriaRepository.existsById(id)) {
            log.warn(
                "No se encontró el usuario-responsable '{}' ({}) para la categoría '{}' al intentar borrar",
                dto.getNombreResponsable(),
                dto.getCorreoResponsable(),
                dto.getNombreCategoria()
            );
            return ResponseEntity.notFound().build();
        }

        usuarioCategoriaRepository.deleteById(id);

        log.info(
            "Usuario-responsable '{}' ({}) borrado correctamente de la categoría '{}'",
            dto.getNombreResponsable(),
            dto.getCorreoResponsable(),
            dto.getNombreCategoria()
        );

        return ResponseEntity.noContent().build();
    }
}
