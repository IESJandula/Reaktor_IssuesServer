package es.iesjandula.reaktor.issues_server.models;

import es.iesjandula.reaktor.issues_server.models.ids.UsuarioCategoriaId;
import jakarta.persistence.Entity;
import jakarta.persistence.EmbeddedId;
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
public class UsuarioCategoria
{
    @EmbeddedId
    private UsuarioCategoriaId id;

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
        // Obtenemos la categoría si es que existe, si no, se devuelve ""
        String categoria = this.categoria != null ? this.categoria.getNombre() : "";

        // Devolvemos la cadena de texto con la información del usuario responsable de la categoría de incidencia
        return "UsuarioCategoria [nombreCategoria=" + this.id.getNombreCategoria() + 
                               ", nombreResponsable=" + this.id.getNombreResponsable() + 
                               ", emailResponsable="  + this.id.getEmailResponsable() + 
                               ", categoria="         + categoria + "]";
    }
}
