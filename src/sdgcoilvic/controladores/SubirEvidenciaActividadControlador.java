package sdgcoilvic.controladores;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.logging.Level;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import sdgcoilvic.logicaDeNegocio.clases.EvidenciaActividad;
import sdgcoilvic.logicaDeNegocio.clases.Profesor;
import sdgcoilvic.logicaDeNegocio.implementacionDAO.EvidenciaActividadDAO;
import sdgcoilvic.logicaDeNegocio.implementacionDAO.ProfesorDAO;
import sdgcoilvic.utilidades.AccesoSingleton;
import sdgcoilvic.utilidades.Alertas;
import sdgcoilvic.utilidades.ColaboracionEnCursoSinglenton;

public class SubirEvidenciaActividadControlador implements Initializable{
    private static final Logger LOG = Logger.getLogger(SubirEvidenciaActividadControlador.class);
    private Stage stage;
    private File archivoASubir;
    public static int idActividadColaborativa;
    @FXML private VBox vBox_Principal;
    @FXML private TextField textField_NombreEvidencia;
    @FXML private Label label_NombreArchivo;
    @FXML private Button button_SubirEvidencia;
    @FXML private Button button_SeleccionarArchivo;
    @FXML private Button button_Regresar;
    
    @FXML private FileChooser filechooser_Evidencia = new FileChooser();
    
    private Runnable onCloseCallback;

    public void setOnCloseCallback(Runnable onCloseCallback) {
        this.onCloseCallback = onCloseCallback;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        aplicarValidacion(textField_NombreEvidencia, "^(?=.{1,149}$)[^<>:\\\"/\\\\|?*]+$");
        restringirTiposDeArchivo(); 
    }
    
    public void setStage(Stage escenario) {
        this.stage = escenario;
    }
    
    public void restringirTiposDeArchivo(){
        FileChooser.ExtensionFilter archivosWordActual = new FileChooser.ExtensionFilter("Archivos de Word (.docx)", "*.docx");
        FileChooser.ExtensionFilter archivosPDF = new FileChooser.ExtensionFilter("Archivos PDF (.pdf)", "*.pdf");
        FileChooser.ExtensionFilter archivosExcel = new FileChooser.ExtensionFilter("Archivos de Excel (.xlsx)", "*.xlsx");
        FileChooser.ExtensionFilter archivosPowerPoint = new FileChooser.ExtensionFilter("Archivos de PowerPoint (.pptx)", "*.pptx");
        FileChooser.ExtensionFilter archivosPublisher = new FileChooser.ExtensionFilter("Archivos de Publisher (.pub)", "*.pub");
        FileChooser.ExtensionFilter archivosImagenes = new FileChooser.ExtensionFilter("Archivos de Imágenes", "*.jpg", "*.jpeg", "*.png", "*.gif");

        filechooser_Evidencia.getExtensionFilters().addAll(
            archivosWordActual, archivosPDF, archivosExcel, archivosPowerPoint, archivosPublisher, archivosImagenes
        );
    }
    
    private void aplicarValidacion(TextField textField, String expresionRegular) {
        UnaryOperator<TextFormatter.Change> filtro = cambio -> {
            String nuevoTexto = cambio.getControlNewText();
            return (nuevoTexto.matches(expresionRegular) || nuevoTexto.isEmpty()) ? cambio : null;
        };

        textField.setTextFormatter(new TextFormatter<>(filtro));
    }
    
    public void button_SeleccionarArchivo(ActionEvent event){
        setArchivoASubir(null);
        label_NombreArchivo.setText("Seleccione un archivo");
        filechooser_Evidencia.setTitle("Seleccionar Evidencia de actividad");

        
            Stage fileChooserStage = new Stage();
            fileChooserStage.initModality(Modality.WINDOW_MODAL);
            fileChooserStage.initOwner(stage);

            File archivoSeleccionado = filechooser_Evidencia.showOpenDialog(fileChooserStage);
            setArchivoASubir(archivoSeleccionado);
            if (archivoSeleccionado != null) {
                label_NombreArchivo.setText(archivoSeleccionado.getName());
            }
      
    }   
    
    public boolean archivoExiste(String rutaArchivo) {
        Path rutaArchivoDeDestino = Paths.get(rutaArchivo);
        return Files.exists(rutaArchivoDeDestino);
    }
    
    public void button_SubirEvidencia(ActionEvent event) {
        EvidenciaActividad nuevaEvidencia = new EvidenciaActividad();
        ProfesorDAO profesorDAO = new ProfesorDAO();
        int idColaboracionEnCurso = ColaboracionEnCursoSinglenton.getInstance().getIdColaboracionEnCurso();
        int idProfesor = AccesoSingleton.getInstance().getAccesoId();
        Profesor profesor = new Profesor();
        try {
            profesor = profesorDAO.obtenerProfesorPorID(String.valueOf(idProfesor));
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(SubirEvidenciaActividadControlador.class.getName()).log(Level.SEVERE, null, ex);
        }
        EvidenciaActividadDAO evidenciaActividadDAO = new EvidenciaActividadDAO();
        File archivoASubir = getArchivoASubir();

        if (archivoASubir == null) {
            Alertas.mostrarMensajeArchivoSinSeleccionar();
            return;
        }

        if (verificarInformacion()) {
            boolean resultadoAccesoACarpeta = evidenciaActividadDAO.crearCarpetaDeActividad(idActividadColaborativa, idColaboracionEnCurso, profesor);
            if (resultadoAccesoACarpeta) {
                String rutaArchivo = evidenciaActividadDAO.obtenerRutaEvidenciaDeActividad(idActividadColaborativa, idColaboracionEnCurso, archivoASubir, profesor);
                if (archivoExiste(rutaArchivo)) {
                    boolean reemplazar = Alertas.mostrarMensajeArchivoExistente();
                    if (!reemplazar) {
                        return;
                    }
                }
                String rutaFinal = evidenciaActividadDAO.guardarEvidenciaDeActividad(idActividadColaborativa, idColaboracionEnCurso, archivoASubir, profesor);
                try {
                    nuevaEvidencia.setNombre(textField_NombreEvidencia.getText()+"_"+profesor.getNombre()+"_"+profesor.getApellidoPaterno());
                    nuevaEvidencia.setRutaEvidencia(rutaFinal);
                    nuevaEvidencia.setIdActividad(idActividadColaborativa);
                    int resultadoInsercion = evidenciaActividadDAO.agregarEvidencia(nuevaEvidencia);
                    switch (resultadoInsercion) {
                        case 1 -> {
                            Alertas.mostrarMensajeArchivoCargado();
                            Stage myStage = (Stage) vBox_Principal.getScene().getWindow();
                            myStage.close();
                            if (onCloseCallback != null) {
                                onCloseCallback.run();
                            }
                        }
                        case -1 -> Alertas.mostrarMensajeDatosDuplicados();
                    }
                } catch (IllegalArgumentException excepcion) {
                    LOG.error(excepcion.getCause());
                    evidenciaActividadDAO.borrarArchivoDeEvidencia(rutaFinal);
                    Alertas.mostrarMensajeInformacionInvalida();
                } catch (SQLException ex) {
                    Alertas.mostrarMensajeErrorBaseDatos();
                    evidenciaActividadDAO.borrarArchivoDeEvidencia(rutaFinal);
                    LOG.error(ex.getCause());
                }
            } else {
                Alertas.mostrarMensajeErrorAlAccederAlaCarpeta();
            }
        }
    }

    
    private boolean estaVacio() {
        return textField_NombreEvidencia.getText().isEmpty();
    }
    
    private boolean verificarInformacion(){
        EvidenciaActividad evidenciaActividad = new EvidenciaActividad();
        boolean validacion = true;

        if (!estaVacio()) {
            try {
                evidenciaActividad.setNombre(textField_NombreEvidencia.getText());
            } catch (IllegalArgumentException claveInstitucionalException) {
                Alertas.mostrarMensajeNombreInvalido();
                validacion = false;
            }
        } else {
            Alertas.mostrarMensajeNombreInvalido();
            validacion = false;
        }
        return validacion;
    }
    
    public File getArchivoASubir(){
        return this.archivoASubir;
    }
    
    public void setArchivoASubir(File archivo){
        archivoASubir = archivo;
    }
    
    @FXML
    void button_Regresar(ActionEvent event) {
        if (Alertas.mostrarMensajeCancelar()) {
            Stage escenario = (Stage) button_Regresar.getScene().getWindow();
            escenario.close();
        }
    }
}
