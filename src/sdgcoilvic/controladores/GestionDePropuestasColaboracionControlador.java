package sdgcoilvic.controladores;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import sdgcoilvic.logicaDeNegocio.clases.PropuestaColaboracion;
import sdgcoilvic.logicaDeNegocio.clases.TablaPropuestasColaboracion;
import sdgcoilvic.logicaDeNegocio.implementacionDAO.PeriodoDAO;
import sdgcoilvic.logicaDeNegocio.implementacionDAO.PropuestaColaboracionDAO;
import sdgcoilvic.utilidades.Alertas;
import sdgcoilvic.utilidades.ImagesSetter;

public class GestionDePropuestasColaboracionControlador implements Initializable{
    private static final Logger LOG = Logger.getLogger(GestionDePropuestasColaboracionControlador.class);
    ObservableList<TablaPropuestasColaboracion> lista = FXCollections.observableArrayList();
    
    @FXML private Button button_Regresar;
    @FXML private TableView<TablaPropuestasColaboracion> tableView_Propuestas;
    @FXML private TableColumn<TablaPropuestasColaboracion, String> column_NombrePropuesta;
    @FXML private TableColumn<TablaPropuestasColaboracion, String> column_Profesor;
    @FXML private TableColumn<TablaPropuestasColaboracion, String> column_Modalidad;
    @FXML private TableColumn<TablaPropuestasColaboracion, String> column_Periodo;
    @FXML private TableColumn<TablaPropuestasColaboracion, Void> column_Evaluar;
    @FXML private ImageView imageView_noHayPropuestas;  
    @FXML private ImageView imageView_SubMenu;  
    private void mostrarImagen() {
        imageView_SubMenu.setImage(ImagesSetter.getImageSubMenu());
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        llenarTabla();
        mostrarImagen();
        column_Evaluar.setCellFactory(param -> new TableCell<>() {
            private final Button button_Evaluar = new Button("EVALUAR");
            {
                button_Evaluar.setOnAction(event -> {
                    TablaPropuestasColaboracion data = getTableView().getItems().get(getIndex());                                                                     
                    try {                     
                        SDGCOILVIC sdgcoilvic = new SDGCOILVIC();
                        Stage escenario = (Stage) button_Evaluar.getScene().getWindow();
                        EvaluarPropuestaColaboracionControlador.idPropuestaColaboracion = data.getIdPropuestaColaboracion();
                        sdgcoilvic.mostrarVentanaEvaluarPropuestaDeColaboracion(escenario );
                    } catch (IOException ioexception) {
                        LOG.error(ioexception.getMessage());
                        Alertas.mostrarMensajeErrorCambioPantalla();
                    }
                });
            }
            @Override
            public void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(button_Evaluar);
                }
            }
        });
    }
    
    private void llenarTabla() {
        PropuestaColaboracionDAO propuestaColaboracionDAO = new PropuestaColaboracionDAO();
        List<PropuestaColaboracion> propuestasLista = null;
        List<List<String>> listaPeriodos = null;
        List<List<String>> listaNombresProfesores = null;
            try {
            lista.clear();
            listaPeriodos = obtenerListaDePeriodo();
            listaNombresProfesores = obtenerListDeNombresProfesores();
            propuestasLista = propuestaColaboracionDAO.consultarTodasLasPropuestasColaboracionEnEspera();
            
            } catch (SQLException ex) {
                Alertas.mostrarMensajeErrorBaseDatos();
                LOG.error(ex);
            }
            if (propuestasLista == null || propuestasLista.isEmpty()) {
                imageView_noHayPropuestas.setVisible(true);
            }else  {
                imageView_noHayPropuestas.setVisible(false);
                if (listaPeriodos != null && listaNombresProfesores != null) {
                for (PropuestaColaboracion propuestaColaboracion : propuestasLista) {
                    String periodoInfo = "";
                    for (List<String> periodo : listaPeriodos) {
                        int id = Integer.parseInt(periodo.get(0));
                        if (id == propuestaColaboracion.getIdPeriodo()) {
                            String nombrePeriodo = periodo.get(1);
                            LocalDate fechaInicio = LocalDate.parse(periodo.get(2)); 
                            LocalDate fechaFin = LocalDate.parse(periodo.get(3)); 
                            periodoInfo = nombrePeriodo + " (" + fechaInicio + " - " + fechaFin + ")";
                            break;
                        }
                    }

                    String nombreCompleto = "";
                    for (List<String> profesor : listaNombresProfesores) {
                        int id = Integer.parseInt(profesor.get(0));
                        if (id == propuestaColaboracion.getIdProfesor()) {
                            String nombreProfesor = profesor.get(1);
                            nombreCompleto = nombreProfesor ;
                            break;
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

                tableView_Propuestas.setItems(lista);
                column_NombrePropuesta.setCellValueFactory(new PropertyValueFactory<>("nombre"));
                column_Periodo.setCellValueFactory(new PropertyValueFactory<>("idPeriodo"));
                column_Profesor.setCellValueFactory(new PropertyValueFactory<>("nombreProfesor"));
                column_Modalidad.setCellValueFactory(new PropertyValueFactory<>("tipoColaboracion"));
    
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