package es.iesjandula.reaktor.issues_server.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import es.iesjandula.reaktor.issues_server.models.UsuarioCategoriaEntity;
import es.iesjandula.reaktor.issues_server.models.ids.UsuarioCategoriaId;

public interface IUsuarioCategoriaRepository
        extends JpaRepository<UsuarioCategoriaEntity, UsuarioCategoriaId>
{
    List<UsuarioCategoriaEntity> findByNombreCategoria(String nombreCategoria);

    void deleteByNombreCategoria(String nombreCategoria);
}
