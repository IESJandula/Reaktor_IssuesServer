package es.iesjandula.reaktor.issues_server.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class IssuesServerError extends Exception
{
	/**
	 * Auto-generated ID
	 */
	private static final long serialVersionUID = 8144321039123138732L;

	private int code;

	/***
	 * Constructor sin la Excepción
	 * @param code Código del error
	 * @param message Mensaje del error
	 */
	public IssuesServerError(int code, String message)
	{
		super(message);

		this.code = code;
	}
	
	/**
	 * Constructor completo
	 * @param code Código del error
	 * @param message Mensaje del error
	 * @param exception Excepción
	 */
	public IssuesServerError(int code, String message, Exception exception)
	{
		super(message, exception);

		this.code = code;
	}

	/**
	 * Método que devuelve un Mapa con el mensaje de la Excepción propia
	 * @return Mapa con la Excepción propia
	 */
	public Map<String, String> getBodyErrorMessage()
	{
		Map<String, String> mapError = new HashMap<String, String>();

		mapError.put("code", "" + code);
		mapError.put("message", this.getMessage());

		if (this.getCause() != null)
		{
			String stacktrace = ExceptionUtils.getStackTrace(this.getCause());
			mapError.put("exception", stacktrace);
		}
		
		return mapError;
	}
}