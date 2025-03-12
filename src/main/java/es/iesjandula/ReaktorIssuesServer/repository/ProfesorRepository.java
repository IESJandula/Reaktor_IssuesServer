package es.iesjandula.ReaktorIssuesServer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.iesjandula.ReaktorIssuesServer.entity.Profesor;

@Repository
public interface ProfesorRepository extends JpaRepository<Profesor, String>
{

}
