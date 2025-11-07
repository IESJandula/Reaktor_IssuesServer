package es.iesjandula.reaktor.issues_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import es.iesjandula.reaktor.issues_server.entity.CategoriaIncidenciaEntity;

public interface ICategoriaIncidenciaRepository extends JpaRepository<CategoriaIncidenciaEntity, Long> {
}
