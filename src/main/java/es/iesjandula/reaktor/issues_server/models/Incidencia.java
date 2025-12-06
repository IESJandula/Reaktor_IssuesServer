package es.iesjandula.reaktor.issues_server.models;

import java.time.LocalDateTime;

import es.iesjandula.reaktor.issues_server.models.ids.IncidenciaId;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Entidad que representa una incidencia en el sistema.
 * 
 * <p>
 * Esta clase define el objeto de incidencia que se almacena en la base de datos. 
 * Utiliza un identificador compuesto definido por {@link IncidenciaId} que 
 * incluye el número de aula, el correo del docente y la fecha de la incidencia.
 * </p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "incidencia")
@IdClass(IncidenciaId.class)
public class Incidencia 
{
    /**
     * Atributo - Aula en la que se da la incidencia.
     * 
     * Este atributo es parte del identificador compuesto de la incidencia.
     */
	@Id
	private String ubicacion;

    /**
     * Atributo - Email del docente que informa de la incidencia.
     * 
     * Este atributo es parte del identificador compuesto de la incidencia.
     */
	@Id
	@Column
	private String email;

    /**
     * Atributo - Fecha de creación de la señalación.
     * 
     * Este atributo es parte del identificador compuesto de la incidencia.
     */
	@Id
	private LocalDateTime fecha;
	
    /**
     * Atributo - Detalla el problema relacionado a la incidencia.
     * 
     * Este atributo contiene una descripción del problema que se ha reportado.
     */
	@Column(columnDefinition = "TEXT")
	private String descripcion;
	
	/**
     * Atributo - Detalla al correo que se le envia la incidencia a la incidencia.
     * 
     * Este atributo contiene un correo del destinatario del problema que se ha reportado.
     */
	@Column
	private String estado;
	
    /**
     * Atributo - Comentario relacionado a la solución de la incidencia.
     * 
     * Este atributo permite incluir notas adicionales sobre la resolución de 
     * la incidencia.
     */
	@Column(columnDefinition = "TEXT")
    private String comentario;
            
    /**
     * Atributo - Email del responsable de la incidencia.
     * 
     * Este atributo contiene el email del responsable de la incidencia.
     */
	@Column(name = "email_responsable")
	private String emailResponsable;
	
    /**
     * Relación muchos a uno con Categoria
     * @return Categoría de incidencia
     */
    @ManyToOne
    @JoinColumn(name = "nombre",
                referencedColumnName = "nombre",
                nullable = false)
    private Categoria categoria;
}
