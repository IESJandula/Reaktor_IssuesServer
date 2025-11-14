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

@Entity
@Table(name = "usuario_categoria")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(UsuarioCategoriaId.class)
public class UsuarioCategoriaEntity {

    @Id
    private String nombreCategoria;

    @Id
    private String nombreResponsable;

    @Id
    private String correoResponsable;

    @ManyToOne
    @JoinColumn(name = "nombreCategoria", referencedColumnName = "nombreCategoria", insertable= false, updatable= false)
    private CategoriaIncidenciaEntity categoria;
    
    
    @Override
    public String toString() {
        return "UsuarioCategoriaEntity{" +
               "correoResponsable='" + correoResponsable + '\'' +'}';
    }
    
    
    
}
