package es.iesjandula.reaktor.issues_server.rest;

import java.util.List;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import es.iesjandula.reaktor.base.utils.BaseConstants;
import es.iesjandula.reaktor.issues_server.models.CategoriaIncidenciaEntity;
import es.iesjandula.reaktor.issues_server.repository.ICategoriaIncidenciaRepository;
import es.iesjandula.reaktor.issues_server.repository.IIncidenciaRepository;
import es.iesjandula.reaktor.issues_server.repository.IUsuarioCategoriaRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@CrossOrigin("*")
@RequestMapping("/issues/categorias")
public class CategoriaIncidenciaController {

    @Autowired
    private ICategoriaIncidenciaRepository categoriaRepository;

    @Autowired
    private IUsuarioCategoriaRepository usuarioCategoriaRepository;

    @Autowired
    private IIncidenciaRepository incidenciaRepository;

    /** Listar categorías */
    @PreAuthorize("hasRole('" + BaseConstants.ROLE_ADMINISTRADOR + "')")
    @GetMapping
    public List<CategoriaIncidenciaEntity> listar() {
        log.info("Petición para listar todas las categorías");

        List<CategoriaIncidenciaEntity> categorias = this.categoriaRepository.findAll();

        log.info("Se han encontrado {} categorías", categorias.size());
        return categorias;
    }

    /** Crear categoría (solo nombreCategoria) */
    @PreAuthorize("hasRole('" + BaseConstants.ROLE_ADMINISTRADOR + "')")
    @PostMapping
    public CategoriaIncidenciaEntity crear(@RequestBody CategoriaIncidenciaEntity categoria) {
        log.info("Petición para crear nueva categoría con nombre '{}'",
                 categoria != null ? categoria.getNombreCategoria() : null);

        CategoriaIncidenciaEntity guardada = this.categoriaRepository.save(categoria);

        log.info("Categoría '{}' creada correctamente", guardada.getNombreCategoria());
        return guardada;
    }

    /** Borrar categoría (solo si no tiene incidencias asociadas) */
    @PreAuthorize("hasAuthority('" + BaseConstants.ROLE_ADMINISTRADOR + "')")
    @DeleteMapping("/{nombreCategoria}")
    public ResponseEntity<?> borrar(@PathVariable String nombreCategoria) {
        log.info("Petición para borrar categoría '{}'", nombreCategoria);

        if (!categoriaRepository.existsById(nombreCategoria)) {
            log.warn("No se encontró la categoría '{}' para borrar", nombreCategoria);
            return ResponseEntity.notFound().build();
        }

        boolean tieneIncidencias =
                incidenciaRepository.existsByCategoria_NombreCategoria(nombreCategoria);

        if (tieneIncidencias) {
            String msg = "No se puede borrar la categoría '" + nombreCategoria
                       + "' porque existen incidencias asociadas.";
            log.warn("No se ha podido borrar la categoría '{}': {}", nombreCategoria, msg);
            return ResponseEntity.status(HttpStatus.SC_CONFLICT).body(msg);
        }

        categoriaRepository.deleteById(nombreCategoria);
        log.info("Categoría '{}' borrada correctamente", nombreCategoria);

        return ResponseEntity.noContent().build();
    }

}
