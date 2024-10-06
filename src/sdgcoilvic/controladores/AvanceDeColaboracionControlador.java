package sdgcoilvic.controladores;
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
import sdgcoilvic.logicaDeNegocio.implementacionDAO.ActividadColaborativaDAO;
import sdgcoilvic.logicaDeNegocio.implementacionDAO.ColaboracionDAO;
import sdgcoilvic.utilidades.Alertas;
import sdgcoilvic.utilidades.ImagesSetter;


public class AvanceDeColaboracionControlador  implements Initializable{
    private static final Logger LOG = Logger.getLogger(AvanceDeColaboracionControlador.class);
    public static int idColaboracion;
    ObservableList<ActividadColaborativa> lista = FXCollections.observableArrayList();
    
    @FXML private ImageView imageView_SubMenu;
    @FXML private Button button_Regresar;
    @FXML private TextField textField_Idioma;
    @FXML private TextField textField_FechaInicio;
    @FXML private TextField textField_FechaFin;
    
    @FXML private TableView<ActividadColaborativa> tableView_Actividades;
    @FXML private TableColumn<ActividadColaborativa, String> tableColumn_Nombre;
    @FXML private TableColumn<ActividadColaborativa, String> tableColumn_Instruccion;
    @FXML private TableColumn<ActividadColaborativa, String> tableColumn_Inicio;
    @FXML private TableColumn<ActividadColaborativa, String> tableColumn_Fin;
    @FXML private TableColumn<ActividadColaborativa, Void> tableColumn_VerEvidencias;
    
    private void mostrarImagen() {
        imageView_SubMenu.setImage(ImagesSetter.getImageSubMenu());
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mostrarImagen();
        asignarBotonEvidencias();
        llenarCamposDeColaboracion();
        llenarTabla();
    }
    
    private void llenarCamposDeColaboracion() {
        try {
            ColaboracionDAO colaboracionDAO = new ColaboracionDAO();
            List<String> infoColaboracion = colaboracionDAO.consultarDetallesEspecificos(idColaboracion);
            if (infoColaboracion != null && infoColaboracion.size() == 3) {
                textField_Idioma.setText(infoColaboracion.get(0));
                textField_FechaInicio.setText(infoColaboracion.get(1));
                textField_FechaFin.setText(infoColaboracion.get(2));
            } else {
                Alertas.mostrarMensajeErrorBaseDatos();
            }
        } catch (SQLException ex) {
            Alertas.mostrarMensajeErrorBaseDatos();
            LOG.error(ex);
        }
    }
    
    public void asignarBotonEvidencias() {
        tableColumn_VerEvidencias.setCellFactory(param -> new TableCell<>() {
            private final Button button_Evidencia = new Button("Evidencias");
            {
                button_Evidencia.setOnAction(event -> {
                    ActividadColaborativa idActividad = getTableView().getItems().get(getIndex());
                    try {
                        SDGCOILVIC sdgcoilvic = new SDGCOILVIC();
                        Stage escenario = (Stage) button_Evidencia.getScene().getWindow();
                        EvidenciasPorActividadControlador.idActividadColaborativa = idActividad.getIdActividadColaborativa();
                        sdgcoilvic.mostrarVentanaEvidenciasPorActividad(escenario);
                    } catch (IOException ioexception) {
                        LOG.error(ioexception.getMessage());
                        Alertas.mostrarMensajeErrorCambioPantalla();
                    }
                });
            }

            @Override
            public void updateItem(Void item, boolean vacio) {
                super.updateItem(item, vacio);
                setGraphic(vacio ? null : button_Evidencia);
            }
        });
    }
    
        private void llenarTabla() {
        ActividadColaborativaDAO actividadColaborativaDAO = new ActividadColaborativaDAO();
        List<ActividadColaborativa> listaActividadesObtenidas = null;
            
        try {
           listaActividadesObtenidas = actividadColaborativaDAO.obtenerActividadesColaborativas(idColaboracion);
            lista.clear();

            for (ActividadColaborativa actividadColaborativa : listaActividadesObtenidas) {
                lista.add(new ActividadColaborativa(
                        actividadColaborativa.getIdActividadColaborativa(),
                        actividadColaborativa.getNombreActividad(),
                        actividadColaborativa.getInstruccion(),
                        actividadColaborativa.getIdColaboracion(),
                        actividadColaborativa.getFechaInicio(),
                        actividadColaborativa.getFechaCierre(),
                        actividadColaborativa.getHerramienta(),
                        actividadColaborativa.getEstadoActividad(),
                        actividadColaborativa.getIdProfesor()
                ));
            }

            tableView_Actividades.setItems(lista);
            tableColumn_Nombre.setCellValueFactory(new PropertyValueFactory<>("nombreActividad"));
            tableColumn_Instruccion.setCellValueFactory(new PropertyValueFactory<>("instruccion"));
            tableColumn_Inicio.setCellValueFactory(new PropertyValueFactory<>("fechaInicio"));
            tableColumn_Fin.setCellValueFactory(new PropertyValueFactory<>("fechaCierre"));
        } catch (SQLException ex) {
            Alertas.mostrarMensajeErrorBaseDatos();
            LOG.error(ex.getMessage());
        }
    }
    

    @FXML
    void button_Regresar(ActionEvent event) {
        Stage escenario = (Stage) button_Regresar.getScene().getWindow();
        SDGCOILVIC sdgcoilvic = new SDGCOILVIC();

        try {
            sdgcoilvic.mostrarVentanaDetallesColaboracion(escenario);
        } catch (IOException ex) {
            LOG.error( ex);
        }
    }
}