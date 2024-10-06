package sdgcoilvic.controladores;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import sdgcoilvic.logicaDeNegocio.clases.ActividadColaborativa;
import sdgcoilvic.logicaDeNegocio.implementacionDAO.ActividadColaborativaDAO;
import sdgcoilvic.logicaDeNegocio.implementacionDAO.ColaboracionDAO;
import sdgcoilvic.utilidades.AccesoSingleton;
import sdgcoilvic.utilidades.Alertas;
import sdgcoilvic.utilidades.ColaboracionEnCursoSinglenton;
import sdgcoilvic.utilidades.ImagesSetter;

public class AdministrarActividadesControlador implements Initializable{
    private static final Logger LOG = Logger.getLogger(AdministrarActividadesControlador.class);
    private AccesoSingleton accesoSingleton;
    private ColaboracionEnCursoSinglenton colaboracionEnCursoSinglenton;
    private List<Integer> actividadesPermitidas;
    private FilteredList<ActividadColaborativa> filteredList;
    ObservableList<ActividadColaborativa> lista = FXCollections.observableArrayList();
    @FXML private AnchorPane anchorPane_Principal;
    @FXML private TextField textField_BuscarActivida;
    @FXML private TableView<ActividadColaborativa> tableView_Actividades;
    @FXML private TableColumn<ActividadColaborativa, String> column_Nombre;
    @FXML private TableColumn<ActividadColaborativa, String> column_Instrucción;
    @FXML private TableColumn<ActividadColaborativa, String> column_Colaboración;
    @FXML private TableColumn<ActividadColaborativa, String> column_Herramienta;
    @FXML private TableColumn<ActividadColaborativa, String> column_Inicia;
    @FXML private TableColumn<ActividadColaborativa, String> column_Termina;
    @FXML private TableColumn<ActividadColaborativa, String> column_Estado;
    @FXML private TableColumn<ActividadColaborativa, Void> column_Evidencia;
    @FXML private TableColumn<ActividadColaborativa, Void> column_Modificar;
    @FXML private Button button_Buscar;
    @FXML private Button button_Regresar;
    @FXML private Button button_AgregarActividad;
    @FXML private Button button_Activa;
    @FXML private Button button_Inactiva;
    @FXML private Button button_Finalizada;
    @FXML private Button button_NoEntregada;
    @FXML private Button button_Todos;
    @FXML private ImageView imageView_SubMenu;
    @FXML private ImageView imageView_NoHayActividades;
    
    private void mostrarImagen() {
        imageView_SubMenu.setImage(ImagesSetter.getImageSubMenu());
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colaboracionEnCursoSinglenton = ColaboracionEnCursoSinglenton.getInstance();
        accesoSingleton = AccesoSingleton.getInstance();
        llenarTabla();
        mostrarImagen();
        asignarBotonEvidencias();
        ajustarVisibilidadBotonAgregarActividad();
        try {
            actividadesPermitidas = obtenerActividadesPermitidasPorProfesor();
        } catch (SQLException e) {
            LOG.error( e);
        }
        
        asignarBotonDeModificacion();
        
        filteredList = new FilteredList<>(lista, p -> true);
        tableView_Actividades.setItems(filteredList);
    }
    
    private List<Integer> obtenerActividadesPermitidasPorProfesor() throws SQLException {
        ActividadColaborativaDAO actividadColaborativaDAO = new ActividadColaborativaDAO();
        int idProfesor = accesoSingleton.getAccesoId();
        return actividadColaborativaDAO.verificarDuenioActividad(idProfesor);
    }
    
    public void asignarBotonEvidencias() {
        column_Evidencia.setCellFactory(param -> new TableCell<>() {
            private final Button button_Evidencia = new Button("Evidencias");
            {
                button_Evidencia.setOnAction(event -> {
                    ActividadColaborativa idActividad = getTableView().getItems().get(getIndex());
                    try {
                        SDGCOILVIC sdgcoilvic = new SDGCOILVIC();
                        Stage escenario = (Stage) button_Evidencia.getScene().getWindow();
                        EvidenciasControlador.idActividadColaborativa = idActividad.getIdActividadColaborativa();
                        sdgcoilvic.mostrarVentanaEvidencias(escenario);
                    } catch (IOException ioexception) {
                        LOG.error(ioexception.getMessage());
                        Alertas.mostrarMensajeErrorCambioPantalla();
                    }
                });
            }

            @Override
            public void updateItem(Void articulo, boolean vacio) {
                super.updateItem(articulo, vacio);
                setGraphic(vacio ? null : button_Evidencia);
            }
        });
    }
    
    public void asignarBotonDeModificacion() {
        column_Modificar.setCellFactory(param -> new TableCell<>() {
            private final Button button_ModificarActividad = new Button("Modificar");

            {
                button_ModificarActividad.setOnAction(event -> {
                    ActividadColaborativa actividadSelecionada = getTableView().getItems().get(getIndex());
                    abrirVentanaModificarActividad(actividadSelecionada, (Stage) ((Button) event.getSource()).getScene().getWindow());
                });
            }

            @Override
           public void updateItem(Void articulo, boolean vacio) {
               super.updateItem(articulo, vacio);
               if (vacio) {
                   setGraphic(null);
               } else {
                   ActividadColaborativa actividad = getTableView().getItems().get(getIndex());
                   int idColaboracionEnCurso = colaboracionEnCursoSinglenton.getIdColaboracionEnCurso();
                   if (actividad.getEstadoActividad() == null || !actividadesPermitidas.contains(actividad.getIdActividadColaborativa())) {
                       setGraphic(null);
                   }else if(idColaboracionEnCurso == -1){
                       setGraphic(null);      
                   } else {
                       setGraphic(button_ModificarActividad); 
                   }
               }
           }
        });
    }
    
    private void abrirVentanaModificarActividad(ActividadColaborativa actividadSelecionada, Stage escenario) {
        try {
            SDGCOILVIC sdgcoilvic = new SDGCOILVIC();
            ActualizarDetallesDeActividadControlador.idActividadColaborativa = actividadSelecionada.getIdActividadColaborativa();
            sdgcoilvic.mostrarVentanaActualizarDetallesActividad(escenario, this::actualizarBotonesYTabla);
        } catch (IOException ioexception) {
            LOG.error(ioexception.getMessage());
            Alertas.mostrarMensajeErrorCambioPantalla();
        }
    }
    
    private void actualizarBotonesYTabla() {
    llenarTabla();
    asignarBotonDeModificacion();
    asignarBotonEvidencias();
    
    }
    
    
private void llenarTabla() {
    ActividadColaborativaDAO actividadColaborativaDAO = new ActividadColaborativaDAO();
    List<ActividadColaborativa> listaActividadesObtenidas = new ArrayList<>();
    int idColaboracionEnCurso = colaboracionEnCursoSinglenton.getIdColaboracionEnCurso();
    
    try {
        listaActividadesObtenidas = actividadColaborativaDAO.obtenerActividadesColaborativas(idColaboracionEnCurso);
    } catch (SQLException ex) {
        Alertas.mostrarMensajeErrorBaseDatos();
        LOG.error(ex);
    }
    
    if (listaActividadesObtenidas.isEmpty()) {
        imageView_NoHayActividades.setVisible(true);
    } else {
        imageView_NoHayActividades.setVisible(false);
        lista.clear();
        lista.addAll(listaActividadesObtenidas);

        tableView_Actividades.setItems(lista);
        column_Nombre.setCellValueFactory(new PropertyValueFactory<>("nombreActividad"));
        column_Instrucción.setCellValueFactory(new PropertyValueFactory<>("instruccion"));
        column_Colaboración.setCellValueFactory(new PropertyValueFactory<>("idColaboracion"));
        column_Herramienta.setCellValueFactory(new PropertyValueFactory<>("herramienta"));
        column_Inicia.setCellValueFactory(new PropertyValueFactory<>("fechaInicio"));
        column_Termina.setCellValueFactory(new PropertyValueFactory<>("fechaCierre"));
        column_Estado.setCellValueFactory(new PropertyValueFactory<>("estadoActividad"));
    }
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
    
    private void ajustarVisibilidadBotonAgregarActividad() {
        ColaboracionDAO colaboracionDAO = new ColaboracionDAO();
        int idProfesor = accesoSingleton.getAccesoId();
        int idColaboracionEnCurso = colaboracionDAO.obtenerIdColaboracionEnCurso(idProfesor);
            
        if(idColaboracionEnCurso >= 1){
            button_AgregarActividad.setVisible(true);
        }else{
             button_AgregarActividad.setVisible(false);
        }
    }

    @FXML
    void button_AgregarActividad(ActionEvent event) {
        Stage escenario = (Stage) button_AgregarActividad.getScene().getWindow();
        SDGCOILVIC sdgcoilvic = new SDGCOILVIC();
        try {
            sdgcoilvic.mostrarVentanaIniciarActividad(escenario, this::actualizarBotonesYTabla);
        } catch (IOException ex) {
            LOG.error( ex);
        }
    }
    
    private void filtrarTabla(String estado) {
        filteredList.setPredicate(actividad -> {
            if (estado.equals("Todos")) {
                return true; 
            }
            return actividad.getEstadoActividad().equals(estado);
        });
    }

    @FXML
    void button_Activa(ActionEvent event) {
        filtrarTabla("Activa");
    }
    
    @FXML
    void button_Inactiva(ActionEvent event) {
        filtrarTabla("Inactiva");
    }

    @FXML
    void button_Finalizada(ActionEvent event) {
        filtrarTabla("Finalizada");
    }

    @FXML
    void button_NoEntregada(ActionEvent event) {
        filtrarTabla("No Entregada");
    }

    @FXML
    void button_Todos(ActionEvent event) {
        filtrarTabla("Todos");
    }


}