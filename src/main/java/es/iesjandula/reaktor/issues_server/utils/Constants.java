package es.iesjandula.reaktor.issues_server.utils;

/**
 * Clase que contiene constantes utilizadas en la aplicación.
 * <p>
 * Esta clase define constantes de estado para incidencias y límites máximos de longitud para varios campos.
 * Todas las constantes son estáticas y finales, lo que significa que no pueden ser modificadas después de su inicialización.
 * <p>
 * Las constantes de estado de incidencias son:
 * <ul>
 *     <li>{@link #ESTADO_EN_PROGRESO} - Indica que la incidencia está en proceso de resolución.</li>
 *     <li>{@link #ESTADO_CANCELADA} - Indica que la incidencia ha sido cancelada.</li>
 *     <li>{@link #ESTADO_RESUELTA} - Indica que la incidencia ha sido resuelta.</li>
 *     <li>{@link #ESTADO_PENDIENTE} - Indica que la incidencia está pendiente de atención.</li>
 * </ul>
 * <p>
 * Además, se definen constantes que representan las longitudes máximas permitidas para ciertos campos:
 * <ul>
 *     <li>{@link #MAX_LONG_COMENTARIO} - Longitud máxima para comentarios de incidencias (150 caracteres).</li>
 *     <li>{@link #MAX_LONG_ESTADO} - Longitud máxima para el estado de la incidencia (12 caracteres).</li>
 *     <li>{@link #MAX_LONG_CORREO} - Longitud máxima para el correo del docente (50 caracteres).</li>
 *     <li>{@link #MAX_LONG_DESCRIPCION} - Longitud máxima para la descripción de la incidencia (250 caracteres).</li>
 * </ul>
 * </p>
 */
public final class Constants
{
	// Constantes de estado de incidencias
	public static final String ESTADO_PENDIENTE = "PENDIENTE";
	public static final String ESTADO_EN_PROGRESO = "EN PROGRESO";
	public static final String ESTADO_CANCELADA = "CANCELADA";
	public static final String ESTADO_RESUELTA = "RESUELTA";
	public static final String ESTADO_DUPLICADA = "DUPLICADA";
	
	// Usuarios/Profesores
	public final static int PROFESOR_NO_ENCONTRADO = 40;
	
	// Errores Generales/De Conexión
	public final static int ERROR_CONEXION_FIREBASE = 101;
	public final static int TIMEOUT_CONEXION_FIREBASE = 102;
	public final static int IO_EXCEPTION_FIREBASE = 103;

}
