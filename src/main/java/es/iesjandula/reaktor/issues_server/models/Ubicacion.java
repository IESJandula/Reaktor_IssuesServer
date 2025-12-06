package es.iesjandula.reaktor.issues_server.models;


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
}
