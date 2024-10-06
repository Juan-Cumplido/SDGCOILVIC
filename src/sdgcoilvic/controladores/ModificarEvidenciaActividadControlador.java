package sdgcoilvic.controladores;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
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
import sdgcoilvic.logicaDeNegocio.implementacionDAO.EvidenciaActividadDAO;
import sdgcoilvic.utilidades.Alertas;

public class ModificarEvidenciaActividadControlador implements Initializable {
    private static final Logger LOG = Logger.getLogger(ModificarEvidenciaActividadControlador.class);
    private Stage escenario;
    private File archivoASubir;
    public static int idEvidencia;

    @FXML private VBox vBox_Principal;
    @FXML private TextField textField_NombreEvidencia;
    @FXML private Label label_NombreArchivo;
    @FXML private Button button_Guardar;
    @FXML private Button button_SeleccionarArchivo;
    @FXML private Button button_Regresar;

    @FXML private FileChooser filechooser_Evidencia = new FileChooser();

    private Runnable onCloseCallback;

    public void setOnCloseCallback(Runnable onCloseCallback) {
        this.onCloseCallback = onCloseCallback;
    }

    private void aplicarValidacion(TextField textField, String expresionRegular) {
        UnaryOperator<TextFormatter.Change> filtro = cambio -> {
            String nuevoTexto = cambio.getControlNewText();
            return (nuevoTexto.matches(expresionRegular) || nuevoTexto.isEmpty()) ? cambio : null;
        };

        textField.setTextFormatter(new TextFormatter<>(filtro));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        aplicarValidacion(textField_NombreEvidencia, "^(?=.{1,149}$)[^<>:\\\"/\\\\|?*]+$");
        restringirTiposDeArchivo();
        cargarDatosEvidencia();
    }

    public void setStage(Stage escenario) {
        this.escenario = escenario;
    }

    public void restringirTiposDeArchivo() {
        filechooser_Evidencia.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Archivos de Word 2007 (.doc)", "*.doc"),
            new FileChooser.ExtensionFilter("Archivos de Word (.docx)", "*.docx"),
            new FileChooser.ExtensionFilter("Archivos PDF (.pdf)", "*.pdf"),
            new FileChooser.ExtensionFilter("Archivos de Excel (.xlsx)", "*.xlsx")
        );
    }

    public void cargarDatosEvidencia() {
        EvidenciaActividadDAO evidenciaActividadDAO = new EvidenciaActividadDAO();

        try {
            EvidenciaActividad evidenciaActividad = evidenciaActividadDAO.obtenerEvidenciaPorIdEvidencia(idEvidencia);
            textField_NombreEvidencia.setText(evidenciaActividad.getNombre());
        } catch (SQLException sQLException) {
            LOG.error("Error al cargar los datos de evidencia: " + sQLException.getMessage());
            Alertas.mostrarMensajeErrorBaseDatos();
        }
    }

    @FXML
    public void button_Guardar(ActionEvent event) {

        if (archivoASubir == null) {
            Alertas.mostrarMensajeArchivoSinSeleccionar();
            return;
        }
        if (verificarInformacion()) {
            EvidenciaActividadDAO evidenciaActividadDAO = new EvidenciaActividadDAO();
            try {
                EvidenciaActividad evidenciaActividad = evidenciaActividadDAO.obtenerEvidenciaPorIdEvidencia(idEvidencia);
                String rutaActual = evidenciaActividad.getRutaEvidencia();
                String rutaCarpeta = new File(rutaActual).getParent();
                String nombreNuevoArchivo = archivoASubir.getName();
                String nuevaRuta = Paths.get(rutaCarpeta, nombreNuevoArchivo).toString();

                File archivoActual = new File(rutaActual);
                if (archivoActual.exists() && archivoActual.isFile()) {
                    archivoActual.delete();
                }

                Path origen = archivoASubir.toPath();
                Path destino = Paths.get(nuevaRuta);
                Files.copy(origen, destino, StandardCopyOption.REPLACE_EXISTING);

                evidenciaActividad.setNombre(textField_NombreEvidencia.getText());
                evidenciaActividad.setRutaEvidencia(nuevaRuta);

                int resultadoActualizacion = evidenciaActividadDAO.actualizarEvidencia(evidenciaActividad);
                if (resultadoActualizacion == 1) {
                    Alertas.mostrarMensajeArchivoCargado();
                    Stage escenario = (Stage) vBox_Principal.getScene().getWindow();
                    escenario.close();
                    if (onCloseCallback != null) {
                        onCloseCallback.run();
                    }
                } else if (resultadoActualizacion == -1) {
                    Alertas.mostrarMensajeDatosDuplicados();
                }
            } catch (SQLException ex) {
                LOG.error("Error al actualizar la evidencia en la base de datos: " + ex.getMessage());
                Alertas.mostrarMensajeErrorBaseDatos();
            } catch (IOException ex) {
                LOG.error("Error al guardar el archivo de evidencia: " + ex.getMessage());
                Alertas.mostrarMensajeErrorAlGuardarArchivo();
            }
        } 
    }

    @FXML
    public void button_SeleccionarArchivo(ActionEvent event) {
        setArchivoASubir(null);
        label_NombreArchivo.setText("Seleccione un archivo");
        filechooser_Evidencia.setTitle("Seleccionar Evidencia de actividad");

        Stage ventanaSelectorArchivos = new Stage();
        ventanaSelectorArchivos.initModality(Modality.WINDOW_MODAL);
        ventanaSelectorArchivos.initOwner(escenario);

        File archivoSeleccionado = filechooser_Evidencia.showOpenDialog(ventanaSelectorArchivos);
        if (archivoSeleccionado != null) {
            setArchivoASubir(archivoSeleccionado);
            label_NombreArchivo.setText(archivoSeleccionado.getName());
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

    public File getArchivoASubir() {
        return this.archivoASubir;
    }

    public void setArchivoASubir(File archivo) {
        this.archivoASubir = archivo;
    }

    @FXML
    void button_Regresar(ActionEvent event) {
        if (Alertas.mostrarMensajeCancelar()) {
            Stage escenario = (Stage) button_Regresar.getScene().getWindow();
            escenario.close();
        }
    }
}