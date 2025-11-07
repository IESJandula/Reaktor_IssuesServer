package es.iesjandula.reaktor.issues_server.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "categorias_incidencia")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaIncidenciaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Tipo de la categoría: "TIC", "DIRECCION", etc. */
    @Column(nullable = false)
    private String tipo;

    /** Nombre del responsable */
    @Column(nullable = false)
    private String nombreResponsable;

    /** Correo del responsable */
    @Column(nullable = false)
    private String correoResponsable;
}
