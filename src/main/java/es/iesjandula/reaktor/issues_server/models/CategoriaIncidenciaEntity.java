package es.iesjandula.reaktor.issues_server.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "categorias")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaIncidenciaEntity {

    @Id
    @Column(nullable = false)
    private String nombreCategoria;

    /** 
     * Relación uno a muchos con UsuarioCategoria 
     */
    @OneToMany(mappedBy = "categoria",  cascade = CascadeType.ALL)
    @JsonIgnore 
    private List<UsuarioCategoriaEntity> responsables;
    
    @Override
    public String toString() {
        return "CategoriaIncidenciaEntity{" +
               "nombreCategoria='" + nombreCategoria + '\'' +
               '}';
    }
}
