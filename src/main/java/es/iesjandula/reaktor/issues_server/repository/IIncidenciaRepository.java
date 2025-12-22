package es.iesjandula.reaktor.issues_server.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.iesjandula.reaktor.issues_server.dtos.IncidenciaDto;
import es.iesjandula.reaktor.issues_server.models.Incidencia;

/**
 * Repositorio para gestionar incidencias en la base de datos.
 * <p>
 * Esta interfaz extiende {@link JpaRepository} y proporciona métodos para realizar operaciones
 * de búsqueda y verificación sobre la entidad {@link Incidencia} utilizando identificadores compuestos.
 * </p>
 */
@Repository
public interface IIncidenciaRepository extends JpaRepository<Incidencia, Long>
{
	/**
	 * Busca incidencias en la base de datos ordenado por fecha de forma decreciente  	 
	 * <p> 	 
	 * @param pageable 
	 *  	 
	 * @return lista de Incidencias ordenadas por fecha de forma decreciente  	 
	 */ 	
	@Query("""
	        SELECT new es.iesjandula.reaktor.issues_server.dtos.IncidenciaDto(i.id, 
			                                                                  i.ubicacion.nombre,
																			  i.email,
																			  i.nombre,
																			  i.apellidos,
																			  CAST(FUNCTION('DATE_FORMAT', i.fecha, '%d/%m/%Y %H:%i') AS string),
																			  i.problema,
																			  i.estado,
																			  i.solucion,
																			  i.usuarioCategoria.id.emailResponsable,
																			  i.usuarioCategoria.nombreResponsable,
																			  i.usuarioCategoria.id.nombreCategoria)
			FROM Incidencia i
			ORDER BY i.fecha DESC
		   """) 	
	Page<IncidenciaDto> buscarIncidenciaOrdenadaFechaPorAdmin(Pageable pageable);


	/**
	 * Busca incidencias en la base de datos ordenado por fecha de forma decreciente por usuario
	 * <p>
	 * @param email El email del usuario.
	 * @param pageable La página y el tamaño de la página.
	 * @return lista de Incidencias ordenadas por fecha de forma decreciente por usuario
	 */
	@Query("""
			SELECT new es.iesjandula.reaktor.issues_server.dtos.IncidenciaDto(i.id, 
																			  i.ubicacion.nombre,
																			  i.email,
																			  i.nombre,
																			  i.apellidos,
																			  CAST(FUNCTION('DATE_FORMAT', i.fecha, '%d/%m/%Y %H:%i') AS string),
																			  i.problema,
																			  i.estado,
																			  i.solucion,
																			  i.usuarioCategoria.id.emailResponsable,
																			  i.usuarioCategoria.nombreResponsable,
																			  i.usuarioCategoria.id.nombreCategoria)
			FROM Incidencia i
			WHERE i.email = :email or i.usuarioCategoria.id.emailResponsable = :email
			ORDER BY i.fecha DESC
		""")
	Page<IncidenciaDto> buscarIncidenciaOrdenadaFechaPorUsuario(Pageable pageable, @Param("email") String email);

	/**
	 * Verifica si existen incidencias asociadas a una categoría.
	 * <p>
	 * Este método verifica si existen incidencias asociadas a una categoría en la base de datos.
	 * </p>
	 * @param nombre El nombre de la categoría.
	 * @return {@code true} si existen incidencias asociadas a la categoría; {@code false} en caso contrario.
	 */
	@Query("SELECT COUNT(i) > 0 FROM Incidencia i WHERE i.usuarioCategoria.id.nombreCategoria = :nombreCategoria")
	boolean validarSiExistenIncidenciasAsociadasACategoria(@Param("nombreCategoria") String nombreCategoria);
}
