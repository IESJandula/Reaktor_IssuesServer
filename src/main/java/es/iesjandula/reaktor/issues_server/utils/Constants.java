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
	/********************************************/
	/**************** Estados *******************/
	/********************************************/

	/** Estado de incidencia pendiente */
	public static final String ESTADO_PENDIENTE = "PENDIENTE";

	/** Estado de incidencia en progreso */
	public static final String ESTADO_EN_PROGRESO = "EN PROGRESO";

	/** Estado de incidencia cancelada */
	public static final String ESTADO_CANCELADA = "CANCELADA";

	/** Estado de incidencia resuelta */
	public static final String ESTADO_RESUELTA = "RESUELTA";

	/** Estado de incidencia duplicada */
	public static final String ESTADO_DUPLICADA = "DUPLICADA";

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
	/********** Errores de Ubicaciones **********/
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

	/** Error de incidencia problema no introducido - Código */
	public final static int ERR_INCIDENCIA_PROBLEMA_NO_INTRODUCIDO_CODE = 502;

	/** Error de incidencia problema no introducido - Mensaje */
	public final static String ERR_INCIDENCIA_PROBLEMA_NO_INTRODUCIDO_MESSAGE = "El problema de la incidencia es obligatorio.";

	/** Error de incidencia categoría no introducida - Código */
	public final static int ERR_INCIDENCIA_CATEGORIA_NO_INTRODUCIDA_CODE = 503;

	/** Error de incidencia categoría no introducida - Mensaje */
	public final static String ERR_INCIDENCIA_CATEGORIA_NO_INTRODUCIDA_MESSAGE = "La categoría de la incidencia es obligatoria.";

	/** Error de incidencia no encontrada - Código */
	public final static int ERR_INCIDENCIA_NO_ENCONTRADA_CODE = 504;

	/** Error de incidencia no encontrada - Mensaje */
	public final static String ERR_INCIDENCIA_NO_ENCONTRADA_MESSAGE = "La incidencia no ha sido encontrada.";

	/** Error de incidencia email responsable no introducido - Código */
	public final static int ERR_INCIDENCIA_EMAIL_RESPONSABLE_NO_INTRODUCIDO_CODE = 505;

	/** Error de incidencia email responsable no introducido - Mensaje */
	public final static String ERR_INCIDENCIA_EMAIL_RESPONSABLE_NO_INTRODUCIDO_MESSAGE = "El email del responsable de la incidencia es obligatorio.";

	/** Error de incidencia estado no introducido - Código */
	public final static int ERR_INCIDENCIA_ESTADO_NO_INTRODUCIDO_CODE = 506;

	/** Error de incidencia estado no introducido - Mensaje */
	public final static String ERR_INCIDENCIA_ESTADO_NO_INTRODUCIDO_MESSAGE = "El estado de la incidencia es obligatorio.";

	/** Error de incidencia ubicación no encontrada - Código */
	public final static int ERR_INCIDENCIA_UBICACION_NO_ENCONTRADA_CODE = 507;

	/** Error de incidencia ubicación no encontrada - Mensaje */
	public final static String ERR_INCIDENCIA_UBICACION_NO_ENCONTRADA_MESSAGE = "La ubicación de la incidencia no ha sido encontrada.";

	/** Error de incidencia ID no introducido - Código */
	public final static int ERR_INCIDENCIA_ID_NO_INTRODUCIDO_CODE = 508;

	/** Error de incidencia ID no introducido - Mensaje */
	public final static String ERR_INCIDENCIA_ID_NO_INTRODUCIDO_MESSAGE = "El ID de la incidencia es obligatorio.";

	/** Error de incidencia usuario no permitido - Código */
	public final static int ERR_INCIDENCIA_USUARIO_NO_PERMITIDO_CODE = 509;

	/** Error de incidencia usuario no permitido - Mensaje */
	public final static String ERR_INCIDENCIA_USUARIO_NO_PERMITIDO_MESSAGE = "El usuario no tiene permisos para realizar esta acción.";	
}
