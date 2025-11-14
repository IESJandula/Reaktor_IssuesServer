package es.iesjandula.reaktor.issues_server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioCategoriaDTO {
    private String nombreCategoria;
    private String nombreResponsable;
    private String correoResponsable;
}
