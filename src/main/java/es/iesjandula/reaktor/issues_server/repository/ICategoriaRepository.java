package es.iesjandula.reaktor.issues_server.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import es.iesjandula.reaktor.issues_server.dtos.CategoriaDto;
import es.iesjandula.reaktor.issues_server.models.Categoria;

/**
 * Interfaz que define el repositorio para la entidad Categoria
 */
@Repository
public interface ICategoriaRepository extends JpaRepository<Categoria, String>
{
    /**
     * Busca todas las categorías ordenadas por nombre
     * @return Lista de categorías ordenadas por nombre
     */
    @Query("SELECT new es.iesjandula.reaktor.issues_server.dtos.CategoriaDto(c.nombre, c.imprimirInforme) FROM Categoria c ORDER BY c.nombre ASC")
    List<CategoriaDto> buscarTodasLasCategorias();
}
