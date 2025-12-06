package es.iesjandula.reaktor.issues_server.models;

import es.iesjandula.reaktor.issues_server.models.ids.UsuarioCategoriaId;
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
 * Entidad que representa un usuario responsable de una categoría de incidencia
 */
@Entity
@Table(name = "usuario_categoria")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(UsuarioCategoriaId.class)
public class UsuarioCategoria
{
    /**
     * Nombre de la categoría (clave primaria)
     */
    @Id
    private String nombreCategoria;

    /**
     * Nombre del responsable (clave primaria)
     */
    @Id
    private String nombreResponsable;

    /**
     * Correo del responsable (clave primaria)
     */
    @Id
    private String emailResponsable;

    /**
     * Relación muchos a uno con Categoria
     * @return Categoría de incidencia
     */
    @ManyToOne
    @JoinColumn(name = "nombreCategoria", referencedColumnName = "nombre", insertable= false, updatable= false)
    private Categoria categoria;
    
    
    /**
     * Método que devuelve una cadena de texto con el nombre del responsable de la categoría de incidencia
     * @return Cadena de texto con el nombre del responsable de la categoría de incidencia
     */
    @Override
    public String toString()
    {
        return "UsuarioCategoria{" + "nombreCategoria='" + this.nombreCategoria + '\'' + 
                                   ", nombreResponsable='" + this.nombreResponsable + '\'' + 
                                   ", emailResponsable='" + this.emailResponsable + '\'' + '}';
    }
}
