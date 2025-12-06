package es.iesjandula.reaktor.issues_server.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import es.iesjandula.reaktor.issues_server.dto.UbicacionDto;
import es.iesjandula.reaktor.issues_server.models.Ubicacion;

/**
 * Interfaz que define el repositorio para la entidad Ubicacion
 */
@Repository
public interface IUbicacionRepository extends JpaRepository<Ubicacion, String>
{
    /**
     * Busca todas las ubicaciones ordenadas por nombre
     * @return Las ubicaciones encontradas
     */
    @Query("SELECT new es.iesjandula.reaktor.issues_server.dto.UbicacionDto(u.nombre) FROM Ubicacion u ORDER BY u.nombre ASC")
    List<UbicacionDto> buscarTodasLasUbicaciones();
}
