package sdgcoilvic.controladores;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import sdgcoilvic.logicaDeNegocio.clases.ActividadColaborativa;
import sdgcoilvic.logicaDeNegocio.clases.EvidenciaActividad;
import sdgcoilvic.logicaDeNegocio.implementacionDAO.ActividadColaborativaDAO;
import sdgcoilvic.logicaDeNegocio.implementacionDAO.EvidenciaActividadDAO;
import sdgcoilvic.utilidades.Alertas;
import sdgcoilvic.utilidades.ImagesSetter;

public class EvidenciasPorActividadControlador  implements Initializable {
    private static final Logger LOG = Logger.getLogger(EvidenciasPorActividadControlador.class);
    public static int idActividadColaborativa;
    ObservableList<EvidenciaActividad> lista = FXCollections.observableArrayList();
    @FXML private TextField textField_NombreActividad;
    @FXML private TextField textField_FechaInicio;
    @FXML private TextField textField_FechaTermina;
    @FXML private TableView<EvidenciaActividad> tableView_Evidencias;
    @FXML private TableColumn<EvidenciaActividad, String> column_NombreEvidencia;
    @FXML private TableColumn<EvidenciaActividad, Void> column_Visualizar;
    @FXML private Button button_Regresar;
    @FXML private Button button_SubirEvidencia;
    @FXML private ImageView imageView_SubMenu;
    
    private void mostrarImagen() {
        imageView_SubMenu.setImage(ImagesSetter.getImageSubMenu());
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mostrarImagen();
        llenarInformacionActividad();
        llenarTabla();
        asignarBotonVisualizarEvidencia();
    }
    
        private void llenarInformacionActividad() {
        try {
            ActividadColaborativa actividadColaborativa = obtenerActividadColaborativa();
            mostrarInformacionActividad(actividadColaborativa);
        } catch (SQLException sQLExcpetion) {
            LOG.error(sQLExcpetion.getMessage());
            Alertas.mostrarMensajeErrorBaseDatos();
        }
    }

    private ActividadColaborativa obtenerActividadColaborativa() throws SQLException {
        ActividadColaborativaDAO actividadColaborativaDAO = new ActividadColaborativaDAO();
        return actividadColaborativaDAO.consultarActividadColaborativa(idActividadColaborativa);
    }

    private void mostrarInformacionActividad(ActividadColaborativa actividadColaborativa) {
        textField_NombreActividad.setText(actividadColaborativa.getNombreActividad());
        textField_FechaInicio.setText(actividadColaborativa.getFechaInicio());
        textField_FechaTermina.setText(actividadColaborativa.getFechaCierre());
    }

    public void asignarBotonVisualizarEvidencia() {
        column_Visualizar.setCellFactory(param -> new TableCell<>() {
            private final Button button_VisualizarEvidencia = new Button("Visualizar Evidencia");

            {
                button_VisualizarEvidencia.setOnAction(event -> {
                    EvidenciaActividad evidenciaSeleccionada = getTableView().getItems().get(getIndex());
                    abrirArchivoEvidencia(evidenciaSeleccionada.getRutaEvidencia());
                });
            }

            @Override
            public void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : button_VisualizarEvidencia);
            }
        });
    }

    private void abrirArchivoEvidencia(String rutaEvidencia) {
        File archivoEvidencia = new File(rutaEvidencia);
        if (archivoEvidencia.exists()) {
            try {
                Desktop.getDesktop().open(archivoEvidencia);
            } catch (IOException excepcion) {
                LOG.error(excepcion);
                Alertas.mostrarMensajeErrorAlAbrirElArchivo();
            }
        } else {
            LOG.error("El archivo no existe: " + rutaEvidencia);
            Alertas.mostrarMensajeNoExisteElArchivo();
        }
    }
    
        private void llenarTabla() {
        EvidenciaActividadDAO evidenciaActividadDAO = new EvidenciaActividadDAO();
        List<EvidenciaActividad> listaEvidenciaActividadObtenida = null;
        try {
            listaEvidenciaActividadObtenida = evidenciaActividadDAO.obtenerListaDeEvidencias(idActividadColaborativa);
            lista.clear();

            for (EvidenciaActividad evidenciaActividad : listaEvidenciaActividadObtenida) {
                lista.add(new EvidenciaActividad(
                        evidenciaActividad.getIdEvidencia(),
                        evidenciaActividad.getRutaEvidencia(),
                        evidenciaActividad.getIdActividad(),
                        evidenciaActividad.getNombre()
                ));
            }
            tableView_Evidencias.setItems(lista);
            column_NombreEvidencia.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        } catch (SQLException ex) {
            Alertas.mostrarMensajeErrorBaseDatos();
            LOG.error(ex);
        }
    }

    @FXML
    void button_Regresar(ActionEvent event) {
        Stage escenario = (Stage) button_Regresar.getScene().getWindow();
        SDGCOILVIC sdgcoilvic = new SDGCOILVIC();
        try {
            sdgcoilvic.mostrarVentanaAvanceDeColaboracion(escenario);
        } catch (IOException ex) {
            LOG.error(ex);
        }
    }
}