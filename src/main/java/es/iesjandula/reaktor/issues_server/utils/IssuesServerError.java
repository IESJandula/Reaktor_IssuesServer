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

	private int id;

	private String message;

	private Exception exception;

	// Constructor completo
	public IssuesServerError(int id, String message, Exception exception)
	{
		super();
		this.id = id;
		this.message = message;
		this.exception = exception;
	}

	// Constructor sin la Excepcion
	public IssuesServerError(int id, String message)
	{
		super();
		this.id = id;
		this.message = message;
	}

	// Metodo que devuelve un Mapa con la Excepción propia
	public Map<String, String> getMapError()
	{
		Map<String, String> mapError = new HashMap<String, String>();

		mapError.put("id", "" + id);
		mapError.put("message", message);

		if (this.exception != null)
		{
			String stacktrace = ExceptionUtils.getStackTrace(this.exception);
			mapError.put("exception", stacktrace);
		}
		
		return mapError;
	}
}