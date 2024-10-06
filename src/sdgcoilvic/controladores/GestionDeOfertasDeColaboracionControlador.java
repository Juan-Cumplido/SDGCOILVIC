package sdgcoilvic.controladores;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import sdgcoilvic.logicaDeNegocio.clases.PropuestaColaboracion;
import sdgcoilvic.logicaDeNegocio.clases.TablaPropuestasColaboracion;
import sdgcoilvic.logicaDeNegocio.enums.EnumPropuesta;
import sdgcoilvic.logicaDeNegocio.implementacionDAO.PeriodoDAO;
import sdgcoilvic.logicaDeNegocio.implementacionDAO.PropuestaColaboracionDAO;
import sdgcoilvic.utilidades.Alertas;
import sdgcoilvic.utilidades.ImagesSetter;

public class GestionDeOfertasDeColaboracionControlador implements Initializable{
    private static final Logger LOG = Logger.getLogger(GestionDeOfertasDeColaboracionControlador.class);
    ObservableList<TablaPropuestasColaboracion> lista = FXCollections.observableArrayList();
    
    @FXML private ImageView imageView_SubMenu;
    @FXML private ImageView imageView_noHayColaboracionesAceptadas;
    @FXML private TableView<TablaPropuestasColaboracion> tableView_Colaboraciones;
    @FXML private TableColumn<TablaPropuestasColaboracion, String> column_Nombre;
    @FXML private TableColumn<TablaPropuestasColaboracion, String> column_Profesor;
    @FXML private TableColumn<TablaPropuestasColaboracion, String> column_Tipo;
    @FXML private TableColumn<TablaPropuestasColaboracion, String> column_Objetivo;
    @FXML private TableColumn<TablaPropuestasColaboracion, String> column_Temas;
    @FXML private TableColumn<TablaPropuestasColaboracion, String> column_Periodo;
    @FXML private Button button_Regresar;
    @FXML private Button button_Ofertar;

    private void mostrarImagen() {
        imageView_SubMenu.setImage(ImagesSetter.getImageSubMenu());
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        llenarTabla();
        mostrarImagen();
        
    }
    
    @FXML
    void button_Regresar(ActionEvent event) {
        Stage escenario = (Stage) button_Regresar.getScene().getWindow();
        SDGCOILVIC sdgcoilvic = new SDGCOILVIC();

        try {
            sdgcoilvic.mostrarVentanaAdministrativoMenu(escenario);
        } catch (IOException ex) {
            LOG.error( ex);
        }
    }
    
    @FXML
    void button_Ofertar(ActionEvent event) {
        PropuestaColaboracionDAO propuestaColaboracionDAO = new PropuestaColaboracionDAO();
        TablaPropuestasColaboracion propuestaSeleccionada = tableView_Colaboraciones.getSelectionModel().getSelectedItem();
        String estadoOfertada = EnumPropuesta.Ofertada.name(); // Usar name() para obtener la representación exacta del enum

        if (propuestaSeleccionada != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmación de Oferta");
            alert.setHeaderText("Confirmación de Oferta");
            alert.setContentText("¿Está seguro de que desea ofertar esta propuesta?");

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    int idPropuestaSeleccionada = propuestaSeleccionada.getIdPropuestaColaboracion();
                    try {
                        if (propuestaColaboracionDAO.evaluarPropuestaColaboracion(idPropuestaSeleccionada, estadoOfertada) == 1) {
                            Alertas.mostrarMensajeExito();
                            llenarTabla();
                        } else {
                            Alertas.mostrarMensajeInformacionNoRegistrada();
                        }
                    } catch (SQLException ex) {
                        Alertas.mostrarMensajeErrorBaseDatos();
                        LOG.fatal("Error en la base de datos en la clase " + this.getClass().getName() + ", método " + Thread.currentThread().getStackTrace()[1].getMethodName() + ": " + ex.getMessage(), ex);
                    }
                }
            });
        } else {
            Alertas.mostrarMensajePropuestaNoSeleccionado();
        }
    }
    
    private void llenarTabla() {
        PropuestaColaboracionDAO propuestaColaboracionDAO = new PropuestaColaboracionDAO();
        List<PropuestaColaboracion> propuestasLista = new ArrayList<>();
        List<List<String>> listaPeriodos = null;
        List<List<String>> listaNombresProfesores = null;
        try {
            lista.clear();
            listaPeriodos = obtenerListaDePeriodo();
            listaNombresProfesores = obtenerListDeNombresProfesores();
            propuestasLista = propuestaColaboracionDAO.consultarTodasLasPropuestasColaboracionAceptadas();
        } catch (SQLException ex) {
                Alertas.mostrarMensajeErrorBaseDatos();
                LOG.error(ex);
        }
        if (propuestasLista == null || propuestasLista.isEmpty()) {
           imageView_noHayColaboracionesAceptadas.setVisible(true);
        } else {
            imageView_noHayColaboracionesAceptadas.setVisible(false);
            if (listaPeriodos != null && listaNombresProfesores != null) {
                for (PropuestaColaboracion propuestaColaboracion : propuestasLista) {
                    String periodoInfo = "";
                    for (List<String> periodo : listaPeriodos) {
                        if (periodo.size() >= 4) {
                            int id = Integer.parseInt(periodo.get(0));
                            if (id == propuestaColaboracion.getIdPeriodo()) {
                                String nombrePeriodo = periodo.get(1);
                                LocalDate fechaInicio = LocalDate.parse(periodo.get(2)); 
                                LocalDate fechaFin = LocalDate.parse(periodo.get(3)); 
                                periodoInfo = nombrePeriodo + " (" + fechaInicio + " - " + fechaFin + ")";
                                break;
                            }
                        }
                    }

                    String nombreCompleto = "";
                    for (List<String> profesor : listaNombresProfesores) {
                        if (profesor.size() >= 2) {
                            int id = Integer.parseInt(profesor.get(0));
                            if (id == propuestaColaboracion.getIdProfesor()) {
                                String nombreProfesor = profesor.get(1);
                                nombreCompleto = nombreProfesor;
                                break;
                            }
                        }
                    }
                    lista.add(new TablaPropuestasColaboracion(
                            propuestaColaboracion.getIdPropuestaColaboracion(),
                            propuestaColaboracion.getTipoColaboracion(),
                            propuestaColaboracion.getNombre(),
                            propuestaColaboracion.getObjetivoGeneral(),
                            propuestaColaboracion.getTemas(),
                            periodoInfo,
                            propuestaColaboracion.getEstadoPropuesta(),
                            nombreCompleto,
                            String.valueOf(propuestaColaboracion.getIdIdiomas())
                    ));
                }

                tableView_Colaboraciones.setItems(lista);
                column_Nombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
                column_Tipo.setCellValueFactory(new PropertyValueFactory<>("tipoColaboracion"));
                column_Profesor.setCellValueFactory(new PropertyValueFactory<>("nombreProfesor"));
                column_Objetivo.setCellValueFactory(new PropertyValueFactory<>("objetivoGeneral"));
                column_Temas.setCellValueFactory(new PropertyValueFactory<>("temas"));
                column_Periodo.setCellValueFactory(new PropertyValueFactory<>("idPeriodo"));
            }
        }
    }
    

    private List<List<String>> obtenerListaDePeriodo() throws SQLException {
        return new PeriodoDAO().obtenerListaDePeriodos();
    }
    
    private List<List<String>> obtenerListDeNombresProfesores() throws SQLException{
        return new PropuestaColaboracionDAO().obtenerListaDeNomnbreProfesorPorIdProfesor();
    }
}