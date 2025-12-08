package es.iesjandula.reaktor.issues_server.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import es.iesjandula.reaktor.issues_server.dto.UsuarioCategoriaDto;
import es.iesjandula.reaktor.issues_server.models.UsuarioCategoria;
import es.iesjandula.reaktor.issues_server.models.ids.UsuarioCategoriaId;

/**
 * Interfaz que define el repositorio para la entidad UsuarioCategoria
 */
@Repository
public interface IUsuarioCategoriaRepository
        extends JpaRepository<UsuarioCategoria, UsuarioCategoriaId>
{
    /**
     * Busca todos los usuarios-categoría
     * @return Los usuarios-categoría encontrados
     */
    @Query("SELECT new es.iesjandula.reaktor.issues_server.dto.UsuarioCategoriaDto(uc.id.nombreCategoria, uc.id.nombreResponsable, uc.id.emailResponsable) FROM UsuarioCategoria uc")
    List<UsuarioCategoriaDto> buscarTodos();

    /**
     * Busca los responsables de una categoría
     * @param nombreCategoria El nombre de la categoría
     * @return Los responsables de la categoría encontrados
     */
    @Query("SELECT new es.iesjandula.reaktor.issues_server.dto.UsuarioCategoriaDto(uc.id.nombreCategoria, uc.id.nombreResponsable, uc.id.emailResponsable) FROM UsuarioCategoria uc WHERE uc.id.nombreCategoria = :nombreCategoria")
    List<UsuarioCategoriaDto> buscarResponsablesPorCategoria(String nombreCategoria);
}
