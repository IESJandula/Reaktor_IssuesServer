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

	/********************************************/
	/**************** Errores *******************/
	/********************************************/

	/** Error genérico */
	public final static int ERR_GENERICO_CODE = 1;
	public final static String ERR_GENERICO_MESSAGE = "Error genérico";

	/********************************************/
	/********** Errores de Categorías ***********/
	/********************************************/

	/** Error de categoría no encontrada */
	public final static int ERR_CATEGORIA_NO_ENCONTRADA_CODE = 201;

	/** Error de categoría no encontrada - Mensaje */
	public final static String ERR_CATEGORIA_NO_ENCONTRADA_MESSAGE = "La categoría no encontrada.";

	/** Error de categoría no borrable */
	public final static int ERR_CATEGORIA_NO_BORRABLE_CODE = 202;

	/********************************************/
	/********** Errores de Ubicaciones ***********/
	/********************************************/

	/** Error de ubicación no encontrada */
	public final static int ERR_UBICACION_NO_ENCONTRADA_CODE = 301;

	/** Error de ubicación no borrable */
	public final static int ERR_UBICACION_NO_BORRABLE_CODE = 302;

	/** Error de ubicación nombre obligatorio */
	public final static int ERR_UBICACION_NOMBRE_OBLIGATORIO_CODE = 303;

	/** Error de ubicación ya existe */
	public final static int ERR_UBICACION_YA_EXISTE_CODE = 304;

	/********************************************/
	/****** Errores de Usuarios Categoría *******/
	/********************************************/

	/** Error de usuario categoría no encontrada - Codigo */
	public final static int ERR_USUARIO_CATEGORIA_NO_ENCONTRADA_CODE = 401;

	/** Error de usuario categoría no encontrada - Mensaje */
	public final static String ERR_USUARIO_CATEGORIA_NO_ENCONTRADA_MESSAGE = "No se encontró ningún responsable para la categoría.";

	/********************************************/
	/********** Errores de Incidencias **********/
	/********************************************/

	/** Error de incidencia ubicación no introducida - Código */
	public final static int ERR_INCIDENCIA_UBICACION_NO_INTRODUCIDA_CODE = 501;

	/** Error de incidencia ubicación no introducida - Mensaje */
	public final static String ERR_INCIDENCIA_UBICACION_NO_INTRODUCIDA_MESSAGE = "La ubicación de la incidencia es obligatoria.";

	/** Error de incidencia descripción no introducida - Código */
	public final static int ERR_INCIDENCIA_DESCRIPCION_NO_INTRODUCIDA_CODE = 502;
	
	/** Error de incidencia descripción no introducida - Mensaje */
	public final static String ERR_INCIDENCIA_DESCRIPCION_NO_INTRODUCIDA_MESSAGE = "La descripción de la incidencia es obligatoria.";

	/** Error de incidencia categoría no introducida - Código */
	public final static int ERR_INCIDENCIA_CATEGORIA_NO_INTRODUCIDA_CODE = 503;

	/** Error de incidencia categoría no introducida - Mensaje */
	public final static String ERR_INCIDENCIA_CATEGORIA_NO_INTRODUCIDA_MESSAGE = "La categoría de la incidencia es obligatoria.";

	/** Error de incidencia no encontrada - Código */
	public final static int ERR_INCIDENCIA_NO_ENCONTRADA_CODE = 504;

	/** Error de incidencia no encontrada - Mensaje */
	public final static String ERR_INCIDENCIA_NO_ENCONTRADA_MESSAGE = "La incidencia no ha sido encontrada.";
}
