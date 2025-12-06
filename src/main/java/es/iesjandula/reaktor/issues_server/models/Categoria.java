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

/**
 * Entidad que representa una categoría de incidencia
 */
@Entity
@Table(name = "categoria")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Categoria
{
    /**
     * Nombre de la categoría de incidencia (clave primaria)
     */
    @Id
    @Column(nullable = false)
    private String nombre;

    /**
     * Relación uno a muchos con UsuarioCategoria
     * @return Lista de usuarios responsables de la categoría de incidencia
     */
    @OneToMany(mappedBy = "categoria",  cascade = CascadeType.ALL)
    @JsonIgnore 
    private List<UsuarioCategoria> responsables;
    
    /**
     * Método que devuelve una cadena de texto con el nombre de la categoría de incidencia
     * @return Cadena de texto con el nombre de la categoría de incidencia
     */
    @Override
    public String toString()
    {
        return "CategoriaIncidenciaEntity{" + "nombre='" + this.nombre + '\'' + '}';
    }
}
