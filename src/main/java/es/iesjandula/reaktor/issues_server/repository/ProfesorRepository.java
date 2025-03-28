package es.iesjandula.reaktor.issues_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.iesjandula.reaktor.issues_server.entity.Profesor;

@Repository
public interface ProfesorRepository extends JpaRepository<Profesor, String>
{

}
