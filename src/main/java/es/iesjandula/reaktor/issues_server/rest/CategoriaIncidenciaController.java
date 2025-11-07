package es.iesjandula.reaktor.issues_server.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import es.iesjandula.reaktor.base.utils.BaseConstants;
import es.iesjandula.reaktor.issues_server.entity.CategoriaIncidenciaEntity;
import es.iesjandula.reaktor.issues_server.repository.ICategoriaIncidenciaRepository;

@RestController
@CrossOrigin("*")
@RequestMapping("/issues/categorias")
public class CategoriaIncidenciaController {

    @Autowired
    private ICategoriaIncidenciaRepository categoriaIncidenciaRepository;

    /** Listar todas las categorías */
    @PreAuthorize("hasRole('" + BaseConstants.ROLE_ADMINISTRADOR + "')")
    @GetMapping
    public List<CategoriaIncidenciaEntity> listar() {
        return this.categoriaIncidenciaRepository.findAll();
    }

    /** Crear nueva categoría */
    @PreAuthorize("hasRole('" + BaseConstants.ROLE_ADMINISTRADOR + "')")
    @PostMapping
    public CategoriaIncidenciaEntity crear(@RequestBody CategoriaIncidenciaEntity categoria) {
        return this.categoriaIncidenciaRepository.save(categoria);
    }

    /** Borrar categoría */
    @PreAuthorize("hasRole('" + BaseConstants.ROLE_ADMINISTRADOR + "')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> borrar(@PathVariable Long id) {
        this.categoriaIncidenciaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
