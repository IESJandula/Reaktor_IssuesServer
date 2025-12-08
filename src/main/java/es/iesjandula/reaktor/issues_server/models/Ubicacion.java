package es.iesjandula.reaktor.issues_server.models;


import java.util.List;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entidad que representa una ubicación
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ubicacion")
public class Ubicacion
{
    /**
     * Nombre de la ubicación (clave única)
     */
    @Id
    private String nombre;

    /**
     * Incidencias asociadas a la ubicación.
     */
    @OneToMany(mappedBy = "ubicacion")
    private List<Incidencia> incidencias;

    @Override
    public String toString()
    {
        return "Ubicacion [nombre=" + this.nombre + "]" ;
    }
}
