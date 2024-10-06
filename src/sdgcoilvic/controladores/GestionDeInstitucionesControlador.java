package sdgcoilvic.controladores;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import sdgcoilvic.utilidades.Alertas;
import sdgcoilvic.utilidades.ImagesSetter;
import sdgcoilvic.logicaDeNegocio.implementacionDAO.InstitucionDAO;
import sdgcoilvic.logicaDeNegocio.clases.Institucion;
import sdgcoilvic.logicaDeNegocio.clases.TablaInstitucion;

public class GestionDeInstitucionesControlador implements Initializable{
    private static final Logger LOG = Logger.getLogger(GestionDeInstitucionesControlador.class);
   
    private ObservableList<TablaInstitucion> lista = FXCollections.observableArrayList();
    @FXML private TableView<TablaInstitucion> tabla_Instituciones;
    @FXML private TableColumn<TablaInstitucion, String> column_Clave;
    @FXML private TableColumn<TablaInstitucion, String> column_NombreInstitucion;
    @FXML private TableColumn<TablaInstitucion, String> column_Pais;
    @FXML private TableColumn<TablaInstitucion, String> column_Correo;
    @FXML private TableColumn<TablaInstitucion, Void> column_Modificar;
    @FXML private ImageView imageView_SubMenu;   
    @FXML private ImageView imageView_noHayInstituciones; 
    @FXML private Button button_Regresar;
    @FXML private Button button_RegistrarInstitucion;
    @FXML private Button button_Buscar;
    @FXML private TextField textField_BuscarInstitucion;
    @FXML private ComboBox<String> comboBox_Pais;
    
    
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
    public void initialize(URL url, ResourceBundle resourceBundle) {
        aplicarValidacion(textField_BuscarInstitucion, "^[\\p{L}áéíóúÁÉÍÓÚüÜ\\s',;\\-_:\\.]{1,200}$");
        mostrarImagen(); 
        llenarTabla();
        llenarComboBox(); 
        asignarBotonDeModificacion();
    }
    
    public void asignarBotonDeModificacion() {
        column_Modificar.setCellFactory(param -> new TableCell<>() {
            private final Button button_Modificar = new Button("Modificar");

            {
                button_Modificar.setOnAction(event -> {
                    TablaInstitucion institucionSeleccionada = getTableView().getItems().get(getIndex());
                    abrirVentanaModificarInstitucion(institucionSeleccionada, (Stage) ((Button) event.getSource()).getScene().getWindow());
                });
            }

            @Override
            public void updateItem(Void item, boolean vacio) {
                super.updateItem(item, vacio);
                setGraphic(vacio ? null : button_Modificar);
            }
        });
    }


    private void abrirVentanaModificarInstitucion(TablaInstitucion institucionSeleccionada, Stage escenario) {
        try {
            SDGCOILVIC sdgcoilvic = new SDGCOILVIC();
            ModificarInstitucionControlador.clave= institucionSeleccionada.getClave();
            sdgcoilvic.mostrarVentanaModificarInstitucion(escenario, this::llenarTabla);
        } catch (IOException ioexception) {
            LOG.error(ioexception.getMessage());
            Alertas.mostrarMensajeErrorCambioPantalla();
        }
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
    
    @FXML
    private void button_RegistrarInstitucion(ActionEvent event) {
        Stage ventana = (Stage) button_RegistrarInstitucion.getScene().getWindow();
        SDGCOILVIC sdgcoilvic = new SDGCOILVIC();
        try {
            sdgcoilvic.mostrarVentanaRegistrarInstitucion(ventana, this::actualizarDatos);
        } catch (IOException ex) {
            LOG.error( ex);
        }
    }
    
    
    @FXML
    private void button_Buscar(ActionEvent event) {
        String criterioBusqueda = textField_BuscarInstitucion.getText();
        if (criterioBusqueda.isEmpty()) {
            Alertas.mostrarMensaje(Alert.AlertType.INFORMATION, "AVISO", "Por favor ingresa un criterio de búsqueda.");
            return;
        }
        buscarInstitucionesPorNombreYPais(criterioBusqueda, comboBox_Pais.getValue());
    }
    
    private void buscarInstitucionesPorNombreYPais(String criterioBusqueda, String paisSeleccionado) {
        InstitucionDAO institucionDAO = new InstitucionDAO();
        try {
            List<Institucion> institucionesEncontradas = institucionDAO.obtenerListaInstitucionesPorNombre(criterioBusqueda);

            if (paisSeleccionado != null && !paisSeleccionado.equals("Todos")) {
                institucionesEncontradas = institucionesEncontradas.stream()
                    .filter(institucion -> institucion.getNombrePais().equals(paisSeleccionado))
                    .collect(Collectors.toList());
            }

            actualizarTablaConInstitucionesEncontradas(institucionesEncontradas);
        } catch (SQLException ex) {
            LOG.error("Error al buscar instituciones:", ex);
            Alertas.mostrarMensajeErrorBaseDatos();
        }
    }
    
    private void actualizarTablaConInstitucionesEncontradas(List<Institucion> institucionesEncontradas) {
        if (institucionesEncontradas.isEmpty()) {
            Alertas.mostrarMensajeSinResultados();
            return;
        }
        ObservableList<TablaInstitucion> items = FXCollections.observableArrayList();
        for (Institucion institucion : institucionesEncontradas) {
            items.add(new TablaInstitucion(institucion.getClaveInstitucional(), institucion.getNombreInstitucion(), institucion.getNombrePais(), institucion.getCorreo()));
        }
        tabla_Instituciones.setItems(items);
    }
    
    @FXML
    void comboPaisSeleccionado(ActionEvent event) {
        String criterioBusqueda = textField_BuscarInstitucion.getText();
        buscarInstitucionesPorNombreYPais(criterioBusqueda, comboBox_Pais.getValue());
    }
    
    private void actualizarDatos() {
        llenarComboBox();
        llenarTabla();
    }
    
    private void llenarComboBox() {
        InstitucionDAO institucionDAO = new InstitucionDAO();
        try {
            List<String> listaDePaises = institucionDAO.obtenerListaDePaises();
            listaDePaises.add(0, "Todos");
            ObservableList<String> items = FXCollections.observableArrayList(listaDePaises);
            comboBox_Pais.setItems(items); 
        } catch (SQLException ex) {
             LOG.error(ex);
        }
    }
    
    private void llenarTabla() {
        InstitucionDAO institucionDAO = new InstitucionDAO();
        List<Institucion> institucionLista = null;
        try {
            institucionLista = institucionDAO.obtenerTodasLasInstituciones();
        } catch (SQLException ex) {
            Alertas.mostrarMensajeErrorBaseDatos();
            LOG.error(ex);
        }
        
        if (institucionLista == null) {
           imageView_noHayInstituciones.setVisible(true);
        } else {
            imageView_noHayInstituciones.setVisible(false);
            lista.clear();
                for (int i = 0; i < institucionLista.size(); i++) {
                    Institucion institucion = institucionLista.get(i);
                    lista.add(new TablaInstitucion(institucion.getClaveInstitucional(), 
                            institucion.getNombreInstitucion(), 
                            institucion.getNombrePais(),
                            institucion.getCorreo()));
                }
                tabla_Instituciones.setItems(lista);
                column_Clave.setCellValueFactory(new PropertyValueFactory<>("clave"));
                column_NombreInstitucion.setCellValueFactory(new PropertyValueFactory<>("nombre"));
                column_Pais.setCellValueFactory(new PropertyValueFactory<>("pais"));
                column_Correo.setCellValueFactory(new PropertyValueFactory<>("correo"));
        }

    } 
}