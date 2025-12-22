package es.iesjandula.reaktor.issues_server.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
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
public class Incidencia 
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Atributo - Aula en la que se da la incidencia.
     * 
     * Este atributo es parte del identificador compuesto de la incidencia.
     */
	@ManyToOne
	@JoinColumn(name = "ubicacion", referencedColumnName = "nombre", nullable = false)
	private Ubicacion ubicacion;

    /**
     * Atributo - Email del docente que informa de la incidencia.
     * 
     * Este atributo es parte del identificador compuesto de la incidencia.
     */
	@Column(name = "email", nullable = false)
	private String email;

    /**
     * Atributo - Nombre del docente que informa de la incidencia.
     * 
     * Este atributo es parte del identificador compuesto de la incidencia.
     */
    @Column(name = "nombre", nullable = false)
    private String nombre;

    /**
     * Atributo - Apellidos del docente que informa de la incidencia.
     * 
     * Este atributo es parte del identificador compuesto de la incidencia.
     */
    @Column(name = "apellidos", nullable = false)
    private String apellidos;

    /**
     * Atributo - Fecha de creación de la señalación.
     * 
     * Este atributo es parte del identificador compuesto de la incidencia.
     */
	@Column(name = "fecha", nullable = false)
	private LocalDateTime fecha;
	
    /**
     * Atributo - Detalla el problema relacionado a la incidencia.
     * 
     * Este atributo contiene una descripción del problema que se ha reportado.
     */
	@Column(columnDefinition = "TEXT")
	private String problema;
	
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
    private String solucion;
            	
    /**
     * Relación muchos a uno con UsuarioCategoria
     * @return UsuarioCategoria de incidencia
     */
    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "nombreCategoria", referencedColumnName = "nombreCategoria", nullable = false),
        @JoinColumn(name = "emailResponsable", referencedColumnName = "emailResponsable", nullable = false)
    })
    private UsuarioCategoria usuarioCategoria;

    @Override
    public String toString()
    {
        // Obtenemos la ubicación si es que existe, si no, se devuelve ""
        String ubicacion = this.ubicacion != null ? this.ubicacion.getNombre() : "";

        // Devolvemos la cadena de texto con la información de la incidencia
        return "Incidencia [id="               + this.id + 
                         ", ubicacion="        + ubicacion + 
                         ", email="            + this.email + 
                         ", fecha="            + this.fecha + 
                         ", problema="         + this.problema + 
                         ", estado="           + this.estado + 
                         ", solucion="         + this.solucion + 
                         ", emailResponsable=" + (this.usuarioCategoria != null ? this.usuarioCategoria.getId().getEmailResponsable() : "") + 
                         ", nombreCategoria="  + (this.usuarioCategoria != null ? this.usuarioCategoria.getId().getNombreCategoria() : "") + "]";
    }
}
