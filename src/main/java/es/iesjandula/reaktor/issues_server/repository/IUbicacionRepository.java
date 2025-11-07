package es.iesjandula.reaktor.issues_server.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import es.iesjandula.reaktor.issues_server.entity.UbicacionEntity;

public interface IUbicacionRepository extends JpaRepository<UbicacionEntity, Long>
{
    Optional<UbicacionEntity> findByNombreIgnoreCase(String nombre);

    List<UbicacionEntity> findAllByOrderByNombreAsc();
}
