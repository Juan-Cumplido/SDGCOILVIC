package sdgcoilvic.utilidades;

import java.util.List;
import org.apache.log4j.Logger;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import sdgcoilvic.logicaDeNegocio.clases.ActividadColaborativa;
import sdgcoilvic.logicaDeNegocio.clases.PropuestaColaboracion;

public class InformeImplementacion {
    
    private static final Logger LOG=Logger.getLogger(InformeImplementacion.class);
    
    public InformeImplementacion(){
    }
    
    public Document crearInformeDeColaboracion(int idColaboracion, PropuestaColaboracion propuestaColaboracion, List<ActividadColaborativa> actividades, List<String> profesores) {
           Document informeColaboracion = new Document();
           informeColaboracion.setMargins(50, 50, 50, 50);
           String rutaDirectorio = "Informes";
           String rutaInforme = rutaDirectorio + "/informeDeColaboracion" + idColaboracion + ".pdf";
           try {
               File directorio = new File(rutaDirectorio);
                if (!directorio.exists()) {
                    if (!directorio.mkdirs()) {
                        LOG.error("No se pudo crear el directorio 'Informes'.");
                        return null;
                    }
                }
               PdfWriter.getInstance(informeColaboracion, new FileOutputStream(rutaInforme));
               informeColaboracion.open();

               agregarPortada(informeColaboracion);

               Paragraph cuerpoDeInforme = obtenerCuerpoDelInforme(propuestaColaboracion, profesores);
               PdfPTable tablaActividades = obtenerActividadesDeInforme(actividades);
               informeColaboracion.add(cuerpoDeInforme);
               informeColaboracion.add(tablaActividades);
               informeColaboracion.close();
           } catch (BadElementException | IOException excepcion) {
               LOG.error(excepcion.getMessage());
               informeColaboracion = null;
           } catch (DocumentException excepcion) {
               LOG.error(excepcion.getMessage());
               informeColaboracion = null;
           }
           return informeColaboracion;
       }

       private void agregarPortada(Document document) throws DocumentException, IOException {
           Paragraph titulo = new Paragraph("Collaboration Offer", FontFactory.getFont("Times-Roman", 30, Font.BOLD, BaseColor.BLACK));
           titulo.setAlignment(Element.ALIGN_CENTER);
           document.add(titulo);

           String fechaActual = new SimpleDateFormat("MMMM yyyy").format(new Date());
           Paragraph subtitulo = new Paragraph(fechaActual, FontFactory.getFont("Times-Roman", 18, Font.ITALIC, BaseColor.BLACK));
           subtitulo.setAlignment(Element.ALIGN_CENTER);
           subtitulo.setSpacingAfter(50);
           document.add(subtitulo);

           PdfPTable tablaPortada = new PdfPTable(2);
           tablaPortada.setWidthPercentage(100);
           tablaPortada.setWidths(new float[]{1, 3});

           Image logo = Image.getInstance("src/sdgcoilvic/recursos/escudocoilvic.jpg");
           logo.scaleToFit(100, 100);
           PdfPCell celdaLogo = new PdfPCell(logo, true);
           celdaLogo.setBorder(Rectangle.NO_BORDER);
           celdaLogo.setHorizontalAlignment(Element.ALIGN_CENTER);
           tablaPortada.addCell(celdaLogo);

           Paragraph descripcion = new Paragraph("¿Qué es COIL?", FontFactory.getFont("Times-Roman", 13, Font.BOLD, BaseColor.BLACK));
           descripcion.add(new Paragraph("""
                                         
                                         El Aprendizaje Internacional Colaborativo en Línea (COIL) conecta a estudiantes y profesores en diferentes países para proyectos colaborativos y debates como parte de sus cursos.
                                         
                                         """, FontFactory.getFont("Times-Roman",
                           12, Font.NORMAL, BaseColor.BLACK)));
           descripcion.add(new Paragraph("""
                                         COIL Las colaboraciones entre estudiantes y profesores brindan oportunidades significativas para experiencias globales integradas en los programas de estudio.
                                         """, 
                   FontFactory.getFont("Times-Roman", 12, Font.NORMAL, BaseColor.BLACK)));

           PdfPCell celdaDescripcion = new PdfPCell(descripcion);
           celdaDescripcion.setBorder(Rectangle.NO_BORDER);
           celdaDescripcion.setPaddingLeft(10);
           tablaPortada.addCell(celdaDescripcion);

           document.add(tablaPortada);
       }

       public PdfPTable obtenerActividadesDeInforme(List<ActividadColaborativa> actividades) {
           PdfPTable tablaActividades = new PdfPTable(6);
           tablaActividades.setWidthPercentage(100);
           tablaActividades.addCell(crearCeldaSinBorde("No.Actividad"));
           tablaActividades.addCell(crearCeldaSinBorde("Nombre"));
           tablaActividades.addCell(crearCeldaSinBorde("Descripción"));
           tablaActividades.addCell(crearCeldaSinBorde("Inicio"));
           tablaActividades.addCell(crearCeldaSinBorde("Cierre"));
           tablaActividades.addCell(crearCeldaSinBorde("Herramienta"));
           for (ActividadColaborativa actividad : actividades) {
               tablaActividades.addCell(crearCeldaSinBorde(Integer.toString(actividad.getIdActividadColaborativa())));
               tablaActividades.addCell(crearCeldaSinBorde(actividad.getNombreActividad()));
               tablaActividades.addCell(crearCeldaSinBorde(actividad.getInstruccion()));
               tablaActividades.addCell(crearCeldaSinBorde(actividad.getFechaInicio()));
               tablaActividades.addCell(crearCeldaSinBorde(actividad.getFechaCierre()));
               tablaActividades.addCell(crearCeldaSinBorde(actividad.getHerramienta()));
           }
           return tablaActividades;
       }

        public Paragraph obtenerCuerpoDelInforme(PropuestaColaboracion propuestaColaboracion, List<String> profesores) {
            Paragraph cuerpoDeInforme = new Paragraph();
            cuerpoDeInforme.setAlignment(Paragraph.ALIGN_JUSTIFIED_ALL);
            cuerpoDeInforme.setFont(FontFactory.getFont("Times-Roman", 14, Font.NORMAL, BaseColor.BLACK));

            cuerpoDeInforme.add(new Chunk("Informe de Colaboración COIL-VIC", FontFactory.getFont("Times-Roman", 16, Font.BOLD, BaseColor.BLACK)));
            cuerpoDeInforme.add(new Chunk("\n\n"));

            cuerpoDeInforme.add(new Chunk("En el presente informe se detalla la información relevante sobre los acontecimientos llevados a cabo en la colaboración COIL-VIC, organizada por los siguientes profesores ", FontFactory.getFont("Times-Roman", 14, Font.NORMAL, BaseColor.BLACK)));
            cuerpoDeInforme.add(new Chunk("con experiencia en COIL:\n\n", FontFactory.getFont("Times-Roman", 14, Font.NORMAL, BaseColor.BLACK)));

            for (String nombreProfesor : profesores) {
                Paragraph nombreProfesorParrafo = new Paragraph(nombreProfesor, FontFactory.getFont("Times-Roman", 14, Font.NORMAL, BaseColor.BLACK));
                nombreProfesorParrafo.setAlignment(Element.ALIGN_CENTER);
                cuerpoDeInforme.add(nombreProfesorParrafo);
            }
            cuerpoDeInforme.add(new Chunk("\n"));

            cuerpoDeInforme.add(new Chunk("Esta experiencia de colaboración COIL-VIC contó con la participación de un grupo selecto de estudiantes. Durante la colaboración, se utilizó un idioma específico para facilitar la comunicación entre estudiantes y docentes, con el objetivo de ", FontFactory.getFont("Times-Roman", 14, Font.NORMAL, BaseColor.BLACK)));
            cuerpoDeInforme.add(new Chunk(propuestaColaboracion.getObjetivoGeneral() + ". ", FontFactory.getFont("Times-Roman", 14, Font.BOLD, BaseColor.BLACK)));
            cuerpoDeInforme.add(new Chunk("Esta colaboración fue de tipo ", FontFactory.getFont("Times-Roman", 14, Font.NORMAL, BaseColor.BLACK)));
            cuerpoDeInforme.add(new Chunk(propuestaColaboracion.getTipoColaboracion() + ".\n\n", FontFactory.getFont("Times-Roman", 14, Font.BOLD, BaseColor.BLACK)));

            return cuerpoDeInforme;
        }


       private PdfPCell crearCeldaSinBorde(String contenido) {
           PdfPCell celda = new PdfPCell(new Phrase(contenido, FontFactory.getFont("Times-Roman", 12, BaseColor.BLACK)));
           celda.setBorder(Rectangle.NO_BORDER);
           return celda;
       }

       public int guardarArchivoDeInforme(File archivo, Document informeAGuardar, int idColaboracion) {
           int resultadoGuardado = 0;
           Document informeColaboracion = new Document();
           try {
               PdfReader lectorPdf = new PdfReader("Informes/informeDeColaboracion" + idColaboracion + ".pdf");
               int numPaginas = lectorPdf.getNumberOfPages();
               PdfCopy copiaPdf = new PdfCopy(informeColaboracion, new FileOutputStream(archivo));
               informeColaboracion.open();
               for (int i = 1; i <= numPaginas; i++) {
                   copiaPdf.addPage(copiaPdf.getImportedPage(lectorPdf, i));
               }
               informeColaboracion.close();
               lectorPdf.close();
               copiaPdf.close();
               resultadoGuardado = 1;
           } catch (IOException | DocumentException excepcion) {
               LOG.error(excepcion.getCause());
               resultadoGuardado = -1;
           }
           return resultadoGuardado;
       }
}
