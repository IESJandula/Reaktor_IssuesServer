package es.iesjandula.reaktor.issues_server.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

import es.iesjandula.reaktor.issues_server.models.Incidencia;
import es.iesjandula.reaktor.issues_server.utils.Constants;
import es.iesjandula.reaktor.issues_server.utils.IssuesServerError;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class PdfParteDesperfectosService
{
    /**
     * El motor de Thymeleaf para procesar el HTML.
     */
    private final TemplateEngine templateEngine;

    /**
     * Constructor de la clase.
     * @param templateEngine El motor de Thymeleaf para procesar el HTML.
     */
    public PdfParteDesperfectosService(TemplateEngine templateEngine)
    {
        this.templateEngine = templateEngine;
    }

    /**
     * Genera un PDF del parte de desperfectos.
     * @param cursoAcademico El curso académico.
     * @param incidencia La incidencia.
     * @return El PDF generado.
     * @throws IssuesServerError si hay un error al generar el PDF.
     */
    public byte[] generarPdfParteDesperfectos(String cursoAcademico, Incidencia incidencia) throws IssuesServerError
    {
        byte[] outcome = null;

        ByteArrayOutputStream byteArrayOutputStream = null;

        try
        {
            // Creamos el contexto
            Context context = this.crearContextoVariables(cursoAcademico, incidencia);
    
            // Procesamos el HTML
            String html = templateEngine.process(Constants.PLANTILLA_PDF_DESPERFECTOS, context);
    
            // Creamos el output stream
            byteArrayOutputStream = new ByteArrayOutputStream();
    
            // Creamos el builder
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.withHtmlContent(html, null);

            // Escribimos el PDF en el output stream
            builder.toStream(byteArrayOutputStream);
            builder.run();
            
            // Obtenemos el PDF en bytes
            outcome = byteArrayOutputStream.toByteArray();
        }
        catch (IOException ioException)
        {
            String errorMessage = "Error al generar el PDF del parte de desperfectos";

            // Log de la excepción
            log.error(errorMessage, ioException);

            // Lanzamos la excepción
            throw new IssuesServerError(Constants.ERR_PDF_NO_GENERADO_CODE, errorMessage, ioException);
        }
        catch (Exception exception)
        {
            String errorMessage = "Error al generar el PDF del parte de desperfectos";

            // Log de la excepción
            log.error(errorMessage, exception);

            // Lanzamos la excepción (incluye errores de parseo XML/XHTML de openhtmltopdf)
            throw new IssuesServerError(Constants.ERR_PDF_NO_GENERADO_CODE, errorMessage, exception);
        }
        finally
        {
            if (byteArrayOutputStream != null)
            {
                try
                {
                    byteArrayOutputStream.close();
                }
                catch (IOException ioException)
                {
                    String errorMessage = "Error al cerrar el output stream del PDF del parte de desperfectos";

                    // Log de la excepción
                    log.error(errorMessage, ioException);

                    // Lanzamos la excepción
                    throw new IssuesServerError(Constants.ERR_PDF_NO_GENERADO_CODE, errorMessage, ioException);
                }
            }
        }   

        return outcome;
    }

    /**
     * Crea el contexto con las variables necesarias.
     * @param cursoAcademico El curso académico.
     * @param incidencia La incidencia.
     * @return El contexto creado.
     */
    private Context crearContextoVariables(String cursoAcademico, Incidencia incidencia)
    {
        // Obtenemos los datos de la incidencia
        String fechaDeteccion = incidencia.getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String instalacion = incidencia.getUbicacion().getNombre() ;
        String detectadaPor = incidencia.getNombre() + " " + incidencia.getApellidos();
        String descripcionAveria = incidencia.getProblema();

        // Creamos el contexto
        Context context = new Context();

        // Añadimos las variables al contexto
        context.setVariable(Constants.CURSO_ACADEMICO, cursoAcademico);
        context.setVariable(Constants.FECHA_DETECCION, fechaDeteccion);
        context.setVariable(Constants.INSTALACION, instalacion);
        context.setVariable(Constants.DETECTADA_POR, detectadaPor);
        context.setVariable(Constants.DESCRIPCION_AVERIA, descripcionAveria);

        return context;
    }
}
