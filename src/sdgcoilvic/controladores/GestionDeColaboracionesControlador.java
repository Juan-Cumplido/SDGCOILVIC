package sdgcoilvic.controladores;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.control.TableCell;
import org.apache.log4j.Logger;
import sdgcoilvic.logicaDeNegocio.clases.TablaColaboracion;
import sdgcoilvic.logicaDeNegocio.enums.EnumColaboracion;
import sdgcoilvic.logicaDeNegocio.implementacionDAO.ColaboracionDAO;
import sdgcoilvic.utilidades.Alertas;
import sdgcoilvic.utilidades.ImagesSetter;

public class GestionDeColaboracionesControlador implements Initializable{
    private static final Logger LOG = Logger.getLogger(GestionDeColaboracionesControlador.class);
    ObservableList<TablaColaboracion> lista = FXCollections.observableArrayList();
    
    @FXML private AnchorPane anchorPane_Principal;
    @FXML private ImageView imageView_SubMenu;
    @FXML private ImageView imageView_noHayColaboraciones;
    @FXML private Pane pane_Contenedor;
    @FXML private TextField textField_BuscarColaboracion;
    @FXML private TableView<TablaColaboracion> tableView_Colaboraciones;
    @FXML private TableColumn<TablaColaboracion, String> tableColumn_Nombre;
    @FXML private TableColumn<TablaColaboracion, String> tableColumn_Tipo;
    @FXML private TableColumn<TablaColaboracion, String> tableColumn_Periodo;
    @FXML private TableColumn<TablaColaboracion, String> tableColumn_NumeroActividades;
    @FXML private TableColumn<TablaColaboracion, String> tableColumn_NumeroEvidencias;
    @FXML private TableColumn<TablaColaboracion, String> tableColumn_Estado;
    @FXML private TableColumn<TablaColaboracion, Void> tableColumn_Detalles;   
    @FXML private ComboBox<String> comboBox_Modalidad;
    @FXML private ComboBox<String> comboBox_Estado;
    @FXML private Button button_Buscar;
    @FXML private Button button_Regresar;
    @FXML private Button button_DarBajaColaboracion;
        
    private void mostrarImagen() {
        imageView_SubMenu.setImage(ImagesSetter.getImageSubMenu());
    }

    private void aplicarValidacion(TextField textField, String expresionRegular) {
        UnaryOperator<TextFormatter.Change> filtro = cambio -> {
            String nuevoTexto = cambio.getControlNewText();
            return (nuevoTexto.matches(expresionRegular) || nuevoTexto.isEmpty()) ? cambio : null;
        };

        textField.setTextFormatter(new TextFormatter<>(filtro));
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       aplicarValidacion(textField_BuscarColaboracion, "^[\\p{L}áéíóúÁÉÍÓÚüÜ\\s',;\\-_:\\.]{1,200}$");
       llenarComboBoxEstado();
       llenarComboBoxModalidad();
       llenarTabla();
       mostrarImagen();
       asignarBotonDetalles();
    }
    
    @FXML
    private void comboEstadoSeleccionada(ActionEvent event) {
        filtrarTabla();
    }   

    @FXML
    private void comboModalidadSeleccionada(ActionEvent event) {
        filtrarTabla();
    }
    
    private void llenarComboBoxEstado() {
        ObservableList<String> items = FXCollections.observableArrayList(
                "EnCurso", "Cerrada", "Finalizada", "Todos");
        comboBox_Estado.setItems(items);
        comboBox_Estado.setValue("Todos"); 
    }

    private void llenarComboBoxModalidad() {
        ObservableList<String> items = FXCollections.observableArrayList("COIL-VIC", "ESPEJO", "Todos");
        comboBox_Modalidad.setItems(items);
        comboBox_Modalidad.setValue("Todos");
    }
    
    private void filtrarTabla() {
        FilteredList<TablaColaboracion> filteredData = new FilteredList<>(lista, p -> true);

        filteredData.setPredicate(colaboracion -> {
            String filterText = textField_BuscarColaboracion.getText();
            String selectedEstado = comboBox_Estado.getValue();
            String selectedModalidad = comboBox_Modalidad.getValue();

            if (filterText != null && !filterText.isEmpty()) {
                String lowerCaseFilter = filterText.toLowerCase();
                if (!colaboracion.getNombre().toLowerCase().contains(lowerCaseFilter) &&
                    !colaboracion.getModalidad().toLowerCase().contains(lowerCaseFilter) &&
                    !colaboracion.getPeriodo().toLowerCase().contains(lowerCaseFilter) &&
                    !colaboracion.getEstadoColaboracion().toLowerCase().contains(lowerCaseFilter)) {
                    return false;
                }
            }

            if (selectedEstado != null && !selectedEstado.equals("Todos")) {
                if (!colaboracion.getEstadoColaboracion().equals(selectedEstado)) {
                    return false;
                }
            }
            
            if (selectedModalidad != null && !selectedModalidad.equals("Todos")) {
                if (!colaboracion.getModalidad().equals(selectedModalidad)) {
                    return false;
                }
            }

            return true;
        });

        SortedList<TablaColaboracion> datosOrdenados = new SortedList<>(filteredData);
        datosOrdenados.comparatorProperty().bind(tableView_Colaboraciones.comparatorProperty());
        tableView_Colaboraciones.setItems(datosOrdenados);
        
        if (datosOrdenados.isEmpty()) {
        Alertas.mostrarMensaje(Alert.AlertType.INFORMATION, "AVISO", "No se encontraron resultados.");
        }
    }
    
    @FXML
    private void button_Buscar(ActionEvent event) {
        String criterioBusqueda = textField_BuscarColaboracion.getText();
        if (criterioBusqueda.isEmpty()) {
            Alertas.mostrarMensaje(Alert.AlertType.INFORMATION, "AVISO", "Por favor ingresa un criterio de búsqueda.");
            return;
        }
        filtrarTabla();
    }
    

    
    @FXML
    private void button_Regresar(ActionEvent event) {
        Stage escenario = (Stage) button_Regresar.getScene().getWindow();
        SDGCOILVIC sdgcoilvic = new SDGCOILVIC();

        try {
            sdgcoilvic.mostrarVentanaAdministrativoMenu(escenario);
        } catch (IOException ex) {
            LOG.error( ex);
        }
    }
    
    public void asignarBotonDetalles() {
        tableColumn_Detalles.setCellFactory(param -> new TableCell<>() {
            private final Button button_Detalles = new Button("Ver detalles");

            {
                button_Detalles.setOnAction(event -> {
                    TablaColaboracion colaboracionSeleccionada = getTableView().getItems().get(getIndex());
                    try {
                        SDGCOILVIC sdgcoilvic = new SDGCOILVIC();
                        Stage escenario = (Stage) button_Detalles.getScene().getWindow();
                        DetallesColaboracionControlador.idColaboracion = colaboracionSeleccionada.getIdColaboracion();
                        sdgcoilvic.mostrarVentanaDetallesColaboracion(escenario);
                    } catch (IOException ioexception) {
                        LOG.error(ioexception.getMessage());
                        Alertas.mostrarMensajeErrorCambioPantalla();
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean vacio) {
                super.updateItem(item, vacio);
                if (vacio) {
                    setGraphic(null);
                } else {
                    setGraphic(button_Detalles);
                     
                }
            }
        });
    }
    
    private void llenarTabla() {
        ColaboracionDAO colaboracionDAO = new ColaboracionDAO();
        List<TablaColaboracion> colaboracionesLista = new ArrayList<>();
        try {
            colaboracionesLista = colaboracionDAO.consultarTodasColaboracionesCoilVic();

        } catch (SQLException ex) {
            
            Alertas.mostrarMensajeErrorBaseDatos();
            LOG.error(ex);
        }
        
        if (colaboracionesLista.isEmpty()) {
            imageView_noHayColaboraciones.setVisible(true);
        } else {
            imageView_noHayColaboraciones.setVisible(false);
                lista.clear();
                for (int i = 0; i < colaboracionesLista.size(); i++) {
                    TablaColaboracion tablaColaboracion = colaboracionesLista.get(i);
                    lista.add(new TablaColaboracion(
                            tablaColaboracion.getIdColaboracion(),
                            tablaColaboracion.getNombre(), 
                            tablaColaboracion.getModalidad(), 
                            tablaColaboracion.getPeriodo(),
                            tablaColaboracion.getNumeroActividades(),
                            tablaColaboracion.getNumeroEvidencias(),
                            tablaColaboracion.getEstadoColaboracion()));

                }
                tableView_Colaboraciones.setItems(lista);
                tableColumn_Nombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
                tableColumn_Tipo.setCellValueFactory(new PropertyValueFactory<>("modalidad"));
                tableColumn_Periodo.setCellValueFactory(new PropertyValueFactory<>("periodo"));
                tableColumn_NumeroActividades.setCellValueFactory(new PropertyValueFactory<>("numeroActividades"));
                tableColumn_NumeroEvidencias.setCellValueFactory(new PropertyValueFactory<>("numeroEvidencias"));
                tableColumn_Estado.setCellValueFactory(new PropertyValueFactory<>("estadoColaboracion"));
            }
 
    }

    @FXML
    private void button_DarBajaColaboracion(ActionEvent event) {
        if (!tableView_Colaboraciones.getSelectionModel().isEmpty()) {
            if(tableView_Colaboraciones.getSelectionModel().getSelectedItem().getEstadoColaboracion() == null ? EnumColaboracion.Cerrada.toString() == null : tableView_Colaboraciones.getSelectionModel().getSelectedItem().getEstadoColaboracion().equals(EnumColaboracion.Cerrada.toString())){
                int idColaboracionSeleccionada  = tableView_Colaboraciones.getSelectionModel().getSelectedItem().getIdColaboracion();  
                boolean finalizarColaboracion = Alertas.mostrarMensajeConfirmacionFinalizarColaboracion();
                if (finalizarColaboracion) {
                    ColaboracionDAO colaboracionDAO = new ColaboracionDAO();
                    colaboracionDAO.finalizarColaboracion(idColaboracionSeleccionada);
                    llenarTabla();
                }
            }else{
                Alertas.mostrarMensajeColaboracionEnCursoOFinalizada();
            }
        }else {
            Alertas.mostrarMensajeColaboracionNoSeleccionado();
        }
        
    }
}