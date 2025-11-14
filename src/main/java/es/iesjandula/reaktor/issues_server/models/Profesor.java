package es.iesjandula.reaktor.issues_server.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Profesor
{
	@Id
	private String email;
	
	@Column
	private String nombre;
	
	@Column 
	private String apellido;
	

}
