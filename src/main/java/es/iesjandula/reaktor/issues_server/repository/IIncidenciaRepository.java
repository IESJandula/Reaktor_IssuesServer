package es.iesjandula.reaktor.issues_server.repository;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.iesjandula.reaktor.issues_server.dto.IncidenciaDto;
import es.iesjandula.reaktor.issues_server.models.Incidencia;
import es.iesjandula.reaktor.issues_server.models.ids.IncidenciaId;

/**
 * Repositorio para gestionar incidencias en la base de datos.
 * <p>
 * Esta interfaz extiende {@link JpaRepository} y proporciona métodos para realizar operaciones
 * de búsqueda y verificación sobre la entidad {@link Incidencia} utilizando identificadores compuestos.
 * </p>
 */
@Repository
public interface IIncidenciaRepository extends JpaRepository<Incidencia, IncidenciaId>
{
	
	/**Busca incidencias en la base de datos ordenado por fecha de forma decreciente  	 
	 * <p> 	 
	 * @param pageable 
	 *  	 
	 * @return lista de Incidencias ordenadas por fecha de forma decreciente  	 
	 */ 	
	@Query("SELECT i FROM Incidencia i ORDER BY i.fecha DESC") 	
	Page<Incidencia> buscarIncidenciaOrdenadaFecha(Pageable pageable);
	
	/**
	 * Verifica si existe una incidencia en la base de datos utilizando un identificador compuesto.
	 * <p>
	 * Este método crea un identificador compuesto a partir del número de aula, el correo del docente y la fecha de la incidencia,
	 * y luego utiliza este identificador para comprobar si la incidencia correspondiente ya está registrada en la base de datos.
	 * </p>
	 *
	 * @param ubicacion     La ubicacion asociada a la incidencia.
	 * @param correoDocente El correo del docente que reportó la incidencia.
	 * @param fecha         La fecha y hora en que ocurrió la incidencia.
	 * @return              {@code true} si la incidencia existe en la base de datos; {@code false} en caso contrario.
	 */
	public default boolean existsByCompositeId(String ubicacion, String correoDocente, LocalDateTime fechaIncidencia  )
	{
		IncidenciaId id = new IncidenciaId( ubicacion, correoDocente, fechaIncidencia  );
		return this.existsById(id);
	}
		
	
	/**
	 * Busca incidencias en la base de datos según los criterios especificados.
	 * <p>
	 * Este método utiliza una consulta JPQL para seleccionar incidencias y devolver una lista de objetos {@link IncidenciaDto}.
	 * Los criterios de búsqueda son opcionales y se aplican si se proporcionan valores diferentes de null.
	 * </p>
	 * 
	 * Cada parametro especificado a continuación puede ser nulo. De serlo será ignorado en la busqueda.	  
	 * @param ubicacion     El número del aula de la incidencia.
	 * @param correoDocente El correo del docente que reportó la incidencia.
	 * @param fechaInicio   La fecha y hora de inicio para filtrar incidencias.
	 * @param fechaFin      La fecha y hora de fin para filtrar incidencias.
	 * @param descripcion    Parte de la descripción de la incidencia a buscar.
	 * @param estado        El estado de la incidencia.
	 * @param comentario    Parte del comentario de la incidencia a buscar.
	 * @return              Una lista de objetos {@link IncidenciaDto} que cumplen con los criterios de búsqueda.
     */
	@Query("SELECT i FROM Incidencia i WHERE i.ubicacion = :ubicacion AND i.email = :email AND i.fecha = :fecha")
	Incidencia EncontrarByUbicacionAndEmailAndFecha(@Param("ubicacion") String ubicacion, 
	                                                @Param("email") String email, 
	                                                @Param("fecha") LocalDateTime localDateTime);
	
	/**
	 * Verifica si existen incidencias asociadas a una categoría.
	 * <p>
	 * Este método verifica si existen incidencias asociadas a una categoría en la base de datos.
	 * </p>
	 * @param nombre El nombre de la categoría.
	 * @return {@code true} si existen incidencias asociadas a la categoría; {@code false} en caso contrario.
	 */
	@Query("SELECT COUNT(i) > 0 FROM Incidencia i WHERE i.categoria.nombre = :nombreCategoria")
	boolean validarSiExistenIncidenciasAsociadasACategoria(@Param("nombreCategoria") String nombreCategoria);


	
	
}
