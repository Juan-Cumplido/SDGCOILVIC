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
import sdgcoilvic.logicaDeNegocio.implementacionDAO.ProfesorDAO;
import sdgcoilvic.logicaDeNegocio.implementacionDAO.PropuestaColaboracionDAO;
import sdgcoilvic.logicaDeNegocio.implementacionDAO.SolicitudColaboracionDAO;
import sdgcoilvic.utilidades.AccesoSingleton;
import sdgcoilvic.utilidades.Alertas;
import sdgcoilvic.utilidades.ColaboracionEnCursoSinglenton;
import sdgcoilvic.utilidades.ImagesSetter;

public class AdministrarColaboracionesDisponiblesControlador implements Initializable{
    private static final Logger LOG = Logger.getLogger(GestionDePropuestasColaboracionControlador.class);
    private AccesoSingleton accesoSingleton;
    ObservableList<TablaPropuestasColaboracion> lista = FXCollections.observableArrayList();
    @FXML private Button button_Regresar;
    @FXML private TableView<TablaPropuestasColaboracion> tableView_Colaboraciones;
    @FXML private TableColumn<TablaPropuestasColaboracion, String> tableColumn_Nombre;
    @FXML private TableColumn<TablaPropuestasColaboracion, String> tableColumn_Profesor;
    @FXML private TableColumn<TablaPropuestasColaboracion, String> tableColumn_Periodo;
    @FXML private TableColumn<TablaPropuestasColaboracion, String> tableColumn_Modalidad;
    @FXML private TableColumn<TablaPropuestasColaboracion, String> tableColumn_Objetivo;
    @FXML private TableColumn<TablaPropuestasColaboracion, String> tableColumn_Idioma;
    @FXML private TableColumn<TablaPropuestasColaboracion, Void > tableColumn_EnviarSolicitud;
    @FXML private ImageView imageView_SubMenu;  
    
    private void mostrarImagen() {
        imageView_SubMenu.setImage(ImagesSetter.getImageSubMenu());
    }
    
        
    @FXML
    void button_Regresar(ActionEvent event) {
       Stage escenario = (Stage) button_Regresar.getScene().getWindow();
        SDGCOILVIC sdgcoilvic = new SDGCOILVIC();

        try {
            sdgcoilvic.mostrarVentanaProfesorMenu(escenario);
        } catch (IOException ex) {
            LOG.error( ex);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        accesoSingleton = AccesoSingleton.getInstance();
        llenarTabla();
        mostrarImagen();
        configurarColumnaOpcion();
    }
    

    private boolean verificarSiTieneColaboracionEnCurso() {
        boolean tieneColaboracionEnCurso = false;
        int idColaboracionEnCurso = ColaboracionEnCursoSinglenton.getInstance().getIdColaboracionEnCurso();
        tieneColaboracionEnCurso = idColaboracionEnCurso>=1;
        return tieneColaboracionEnCurso;
    }
    
    private void configurarColumnaOpcion() {
        tableColumn_EnviarSolicitud.setCellFactory(param -> new TableCell<>() {
            private final Button button_EnviarSolicitud = new Button("ENVIAR SOLICITUD");
            {
                button_EnviarSolicitud.setOnAction(event -> {
                    TablaPropuestasColaboracion colaboracionSeleccionada = getTableView().getItems().get(getIndex());
                    try {
                        SDGCOILVIC sdgcoilvic = new SDGCOILVIC();
                        Stage ventana = (Stage) button_EnviarSolicitud.getScene().getWindow();
                        DeclaracionDePropositoControlador.idPropuestaColaboracion = colaboracionSeleccionada.getIdPropuestaColaboracion();
                        sdgcoilvic.mostrarVentanaDeclaracionDeProposito(ventana);
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
                    int idAcceso = accesoSingleton.getAccesoId();
                    boolean tieneSolicitudesAceptadasOEnEspera = verificarEstadoSolicitud(idAcceso);
                    if (tieneSolicitudesAceptadasOEnEspera || verificarSiTieneColaboracionEnCurso() == true) {
                        setGraphic(null);
                    } else {
                        setGraphic(button_EnviarSolicitud);
                    }
                }
            }

            private boolean verificarEstadoSolicitud(int idProfesor) {
                boolean resultado = false;
                SolicitudColaboracionDAO solicitudColaboracionDAO = new SolicitudColaboracionDAO();
                try {
                    resultado = solicitudColaboracionDAO.verificarEstadoSolicitud(idProfesor);
                } catch (SQLException e) {
                    LOG.error("Error verificando el estado de las solicitudes", e);
                }
                return resultado;
            }
        });
    }
    
    private void llenarTabla() {
        PropuestaColaboracionDAO propuestaColaboracionDAO = new PropuestaColaboracionDAO();
        List<PropuestaColaboracion> propuestasLista = null;
        List<List<String>> listaPeriodos = null;
         List<List<String>> listaIdiomas = null;
        List<List<String>> listaNombresProfesores = null;
        int idAcceso = accesoSingleton.getAccesoId();
        try {
            lista.clear();
            listaPeriodos = obtenerListaDePeriodo();
            listaIdiomas = obtenerListaDeIdiomas();
            listaNombresProfesores = obtenerListDeNombresProfesores();
            propuestasLista = propuestaColaboracionDAO.consultarTodasLasPropuestasColaboracionOfertadas(idAcceso);
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
                
                String idiomaNombre = "";
                for (List<String> idioma : listaIdiomas) {
                    int id = Integer.parseInt(idioma.get(0));
                    if (id == propuestaColaboracion.getIdIdiomas()) {
                        String idIdioma = idioma.get(1);
                        idiomaNombre = idIdioma ;
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
                        idiomaNombre
                ));

            }

                tableView_Colaboraciones.setItems(lista);
                tableColumn_Nombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
                tableColumn_Profesor.setCellValueFactory(new PropertyValueFactory<>("nombreProfesor"));
                tableColumn_Periodo.setCellValueFactory(new PropertyValueFactory<>("idPeriodo"));
                tableColumn_Modalidad.setCellValueFactory(new PropertyValueFactory<>("tipoColaboracion"));
                tableColumn_Objetivo.setCellValueFactory(new PropertyValueFactory<>("objetivoGeneral"));
                tableColumn_Idioma.setCellValueFactory(new PropertyValueFactory<>("idioma"));
            } catch (SQLException ex) {
                Alertas.mostrarMensajeErrorBaseDatos();
                LOG.error(ex);
            }
        }

    private List<List<String>> obtenerListaDePeriodo() throws SQLException {
        return new PeriodoDAO().obtenerListaDePeriodos();
    }
    
    private List<List<String>> obtenerListDeNombresProfesores() throws SQLException{
        return new PropuestaColaboracionDAO().obtenerListaDeNomnbreProfesorPorIdProfesor();
    }
    
    private List<List<String>> obtenerListaDeIdiomas() throws SQLException {
        return new ProfesorDAO().obtenerListaDeIdiomas();
    }
}