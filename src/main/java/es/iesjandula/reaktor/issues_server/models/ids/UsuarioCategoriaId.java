package es.iesjandula.reaktor.issues_server.models.ids;

import java.io.Serializable;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioCategoriaId implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private String nombreCategoria;
    private String nombreResponsable;
    private String correoResponsable;
}
