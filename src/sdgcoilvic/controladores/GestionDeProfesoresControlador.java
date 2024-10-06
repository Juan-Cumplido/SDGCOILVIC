package sdgcoilvic.controladores;

import java.io.IOException;
import java.sql.SQLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.function.UnaryOperator;
import java.util.logging.Level;
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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import sdgcoilvic.logicaDeNegocio.clases.Acceso;
import sdgcoilvic.logicaDeNegocio.clases.Institucion;
import sdgcoilvic.utilidades.Alertas;
import sdgcoilvic.utilidades.ImagesSetter;
import sdgcoilvic.logicaDeNegocio.implementacionDAO.ProfesorDAO;
import sdgcoilvic.logicaDeNegocio.clases.Profesor;
import sdgcoilvic.logicaDeNegocio.clases.TablaProfesor;
import sdgcoilvic.logicaDeNegocio.implementacionDAO.AccesoDAO;
import sdgcoilvic.logicaDeNegocio.implementacionDAO.InstitucionDAO;
import sdgcoilvic.logicaDeNegocio.implementacionDAO.ProfesorUVDAO;
import sdgcoilvic.utilidades.EnviosDeCorreoElectronico;
import sdgcoilvic.utilidades.GeneradorDeContrasenias;

public class GestionDeProfesoresControlador implements Initializable  {
    private static final Logger LOG = Logger.getLogger(GestionDeProfesoresControlador.class);
   
    ObservableList<TablaProfesor> lista = FXCollections.observableArrayList();
    @FXML private ImageView imageView_SubMenu;
    @FXML private ImageView imageView_noHayProfesores;
    @FXML private Button button_Regresar;   
    @FXML private Button button_Buscar;
    @FXML private Button button_ModificarProfesor;
    @FXML private Button button_AgregarProfesorUV;
    @FXML private Button button_AgregarProfesorExterno;
    @FXML private TextField textField_BuscarProfesor;
    @FXML private ComboBox<String> comboBox_Estado;
    @FXML private ComboBox<String> comboBox_Institucion;
    @FXML private TableView<TablaProfesor> tableView_Profesores;
    @FXML private TableColumn<TablaProfesor, String> column_Nombre;
    @FXML private TableColumn<TablaProfesor, String> column_ApellidoPaterno;
    @FXML private TableColumn<TablaProfesor, String> column_ApellidoMaterno;
    @FXML private TableColumn<TablaProfesor, String> column_Correo; 
    @FXML private TableColumn<TablaProfesor, String> column_Universidad; 
    @FXML private TableColumn<TablaProfesor, String> column_Estado; 
    @FXML private TableColumn<TablaProfesor, Void> column_Credenciales; 
    
    
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

    private List<List<String>> obtenerListaDeInstituciones() throws SQLException {
        return new ProfesorDAO().obtenerListaDeInstituciones();
    }
    
    private List<List<String>> obtenerListaDeIdiomas() throws SQLException {
        return new ProfesorDAO().obtenerListaDeIdiomas();
    }

    private List<List<String>> obtenerListaDeRegion() throws SQLException {
        return new ProfesorUVDAO().obtenerListaDeRegion();
    }
    
    private List<List<String>> obtenerListaDeCategoriaContratacion() throws SQLException {
        return new ProfesorUVDAO().obtenerListaDeCategoriaContratacion();
    }

    private List<List<String>> obtenerListaDeTipoContratacion() throws SQLException {
        return new ProfesorUVDAO().obtenerListaDeTipoContratacion();
    }
    
    private List<List<String>> obtenerListaDeAreaAcademica() throws SQLException {
        return new ProfesorUVDAO().obtenerListaDeAreaAcademica();
    }
    
    private boolean verificarListasNoVacias() {
        boolean algunaListaVacia = false;
        try {
            if (obtenerListaDeInstituciones().isEmpty() ) {
                algunaListaVacia = true;
            }

            if (obtenerListaDeIdiomas().isEmpty()) {
                algunaListaVacia = true;
            }

            if (obtenerListaDeRegion().isEmpty()) {
                algunaListaVacia = true;
            }

            if (obtenerListaDeCategoriaContratacion().isEmpty()) {
                algunaListaVacia = true;
            }

            if (obtenerListaDeTipoContratacion().isEmpty()) {
                algunaListaVacia = true;
            }

            if (obtenerListaDeAreaAcademica().isEmpty()) {
                algunaListaVacia = true;
            }
        } catch (SQLException ex) {
            LOG.error(ex);
        }
        return algunaListaVacia;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        aplicarValidacion(textField_BuscarProfesor, "^[\\p{L}áéíóúÁÉÍÓÚüÜ\\s',;\\-_:\\.]{1,200}$");
        llenarTabla();
        llenarComboBoxInstitucion();
        llenarComboBoxEstadoProfesor();
        mostrarImagen();
        asignarBotonReenviarCredenciales();
    }
    
    public void asignarBotonReenviarCredenciales() {
        column_Credenciales.setCellFactory(param -> new TableCell<>() {
            private final Button button_Credenciales = new Button("Reenviar Credenciales");
            {
                button_Credenciales.setOnAction(event -> {
                    TablaProfesor profesor = getTableView().getItems().get(getIndex());
                    int idProfesor = profesor.getIdProfesor(); 
                    String correo = profesor.getCorreo();   
                    String nombreProfesor = profesor.getNombre(); 
                    Acceso acceso = new Acceso();
                    AccesoDAO accesoDAO = new AccesoDAO();
                        acceso.setUsuario(correo);
                        acceso.setContrasenia(GeneradorDeContrasenias.generarContraseña());
                    try {
                        if (accesoDAO.actualizarAcceso(acceso, idProfesor) == 1) {
                            if (enviarCorreo(nombreProfesor, acceso, correo) == false) {
                                Alertas.mostrarMensajeElCorreoNoSePudoEnviar();
                            }else{
                                Alertas.mostrarMensajeCredencialesEnviadoExitoso();
                            }
                        } else {
                            Alertas.mostrarMensajeInformacionNoRegistrada();
                        }
                    } catch (SQLException ex) {
                        LOG.error(ex);
                    }
                });
            }

            @Override
            public void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : button_Credenciales);
            }
        });
    }
    
    private boolean enviarCorreo(String nombre, Acceso acceso, String correo) {
        String mensaje = "Estimado profesor " + nombre + ",\n\n" +
                "Sus credenciales se han actualizado correctamente. A continuación se muestran tus credenciales de acceso:\n\n" +
                "Usuario: " + acceso.getUsuario() + "\n" +
                "Contraseña: " + acceso.getContrasenia() + "\n\n" +
                "¡Gracias por su solicitud!\n" +
                "SDGCOILVIC";
        
        return EnviosDeCorreoElectronico.verificarEnvioCorreo(correo, "Credenciales de acceso", mensaje);
    }
    
    
    @FXML
    private void button_Buscar(ActionEvent event) {
        String criterioBusqueda = textField_BuscarProfesor.getText();
        if (criterioBusqueda.isEmpty()) {
            Alertas.mostrarMensaje(Alert.AlertType.INFORMATION, "AVISO", "Por favor ingresa un criterio de búsqueda.");
            return;
        }
        filtrarTabla();
    }
    
    @FXML
    private void comboEstadoSeleccionado(ActionEvent event) {
        filtrarTabla();
    }

    @FXML
    private void comboInstitucionSeleccionada(ActionEvent event) {
        filtrarTabla();
    }
    
    private void filtrarTabla() {
        FilteredList<TablaProfesor> filteredData = new FilteredList<>(lista, p -> true);

        filteredData.setPredicate(profesor -> {
            String filterText = textField_BuscarProfesor.getText().toLowerCase();
            String selectedEstado = comboBox_Estado.getValue();
            String selectedInstitucion = comboBox_Institucion.getValue();

            if (!filterText.isEmpty()) {
                if (!profesor.getNombre().toLowerCase().contains(filterText) &&
                    !profesor.getApellidoPaterno().toLowerCase().contains(filterText) &&
                    !profesor.getApellidoMaterno().toLowerCase().contains(filterText)) {
                    return false;
                }
            }

            if (selectedEstado != null && !selectedEstado.equals("Todos")) {
                if (!profesor.getEstadoProfesor().equals(selectedEstado)) {
                    return false;
                }
            }

            if (selectedInstitucion != null && !selectedInstitucion.equals("Todos")) {
                if (!profesor.getClaveInstitucional().equals(selectedInstitucion)) {
                    return false;
                }
            }

            return true;
        });

        SortedList<TablaProfesor> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableView_Profesores.comparatorProperty());
        tableView_Profesores.setItems(sortedData);
            
        if (sortedData.isEmpty()) {
        Alertas.mostrarMensaje(Alert.AlertType.INFORMATION, "AVISO", "No se encontraron resultados.");
        }
    }

    
    private void llenarComboBoxInstitucion() {
        ProfesorDAO profesorDAO = new ProfesorDAO();
        try {
            List<String> listaDeInstituciones = profesorDAO.obtenerListaDeNombreInstitucion();
            listaDeInstituciones.add(0, "Todos");
            ObservableList<String> items = FXCollections.observableArrayList(listaDeInstituciones);
            comboBox_Institucion.setItems(items); 
        } catch (SQLException ex) {
             LOG.error(ex);
        }
    }
    
    private void llenarComboBoxEstadoProfesor() {
        ProfesorDAO profesorDAO = new ProfesorDAO();
           try {
               List<String> listaDeEstadoProfesor = profesorDAO.obtenerListaDeTodosLosEstadoProfesor();
               Set<String> conjuntoDeEstados = new HashSet<>(listaDeEstadoProfesor);
               listaDeEstadoProfesor = new ArrayList<>(conjuntoDeEstados);
               listaDeEstadoProfesor.add(0, "Todos");
               ObservableList<String> items = FXCollections.observableArrayList(listaDeEstadoProfesor);
               comboBox_Estado.setItems(items); 
           } catch (SQLException ex) {
               LOG.error(ex);
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
    private void button_ModificarProfesor(ActionEvent event) {
            ProfesorDAO profesorDAO = new ProfesorDAO();
            if (!tableView_Profesores.getSelectionModel().isEmpty()) {
                try {
                    int idProfesorSeleccionado = tableView_Profesores.getSelectionModel().getSelectedItem().getIdProfesor();
                    String correo = tableView_Profesores.getSelectionModel().getSelectedItem().getCorreo();
                    String identificador = profesorDAO.identitificarProfesorUVOProfesorExterno(correo);
                    Stage escenario = (Stage) button_ModificarProfesor.getScene().getWindow();
                    SDGCOILVIC sdgcoilvic = new SDGCOILVIC();

                    String idProfesorSeleccionadoStr = String.valueOf(idProfesorSeleccionado); 
                    if (verificarListasNoVacias() == true) {
                            Alertas.mostrarMensajeNoCatalogosDisponibles();
                    }else{
                        if (esNumero(identificador)) {
                            if (identificador.equals(idProfesorSeleccionadoStr)) { 
                                ModificarProfesorExternoControlador.idProfesor = identificador;
                                sdgcoilvic.mostrarVentanaModificarProfesorExterno(escenario, this::actualizarDatos);
                            } else {
                                ModificarProfesorUVControlador.noPersonal = identificador;
                                sdgcoilvic.mostrarVentanaModificarProfesorUV(escenario, this::actualizarDatos);
                            }
                        } else {
                            ModificarProfesorUVControlador.noPersonal = identificador;
                            sdgcoilvic.mostrarVentanaModificarProfesorUV(escenario, this::actualizarDatos);
                        }
                    }
                } catch (IOException ioexception) {
                    LOG.error("Error al abrir la ventana de profesor: " + ioexception.getMessage());
                    Alertas.mostrarMensajeErrorCambioPantalla();
                }
            } else {
                Alertas.mostrarMensajeProfesorNoSeleccionado();
            }
        }


        private boolean esNumero(String cadena) {
        for (char c : cadena.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }
        
    private Institucion crearInstitucion() {
        Institucion institucion = new Institucion();
        institucion.setClaveInstitucional("30MSU0940B");
        institucion.setNombreInstitucion("Universidad Veracruzana");
        institucion.setNombrePais("México"); 
        institucion.setCorreo("dgae@uv.mx");
        return institucion;
    }
    
    @FXML
    private void button_AgregarProfesorUV(ActionEvent event) {
        Institucion institucion = crearInstitucion();
        InstitucionDAO institucionDAO = new InstitucionDAO();
        Stage escenario = (Stage) button_AgregarProfesorUV.getScene().getWindow();
        SDGCOILVIC sdgcoilvic = new SDGCOILVIC();
        try {
            if (!institucionDAO.verificarSiExisteLaClave(institucion.getClaveInstitucional())) {
                boolean agregarInstitucionUV = Alertas.mostrarMensajeConfirmacionAgregarInstitucionUV();
                if (agregarInstitucionUV) {
                    if (institucionDAO.insertarInstitucion(institucion) == 1) {
                        Alertas.mostrarMensajeAgregarInstitucionExito();         
                    } else {
                        Alertas.mostrarMensajeInformacionNoRegistrada();
                    }
                }
            }else{
                if (verificarListasNoVacias() == true) {
                Alertas.mostrarMensajeNoCatalogosDisponibles();
                }else{
                try {
                    sdgcoilvic.mostrarVentanaAgregarProfesorUV(escenario, this::actualizarDatos);
                    } catch (IOException ex) {
                        LOG.error( ex);
                    }
                }
            }
        } catch (SQLException ex) {        
            Alertas.mostrarMensajeErrorBaseDatos();
            LOG.error(ex);
        }
    }
    
    @FXML
    private void button_AgregarProfesorExterno(ActionEvent event) {
        Stage escenario = (Stage) button_AgregarProfesorExterno.getScene().getWindow();
        SDGCOILVIC sdgcoilvic = new SDGCOILVIC();
        String clave = "30MSU0940B";
        try {
            List<List<String>> listaDeIdiomas = obtenerListaDeIdiomas();
            List<List<String>> listaDeInstituciones = obtenerListaDeInstituciones();

            if (listaDeIdiomas.isEmpty() || listaDeInstituciones.isEmpty()) {
                Alertas.mostrarMensajeNoCatalogosDisponibles();
            } else {
                if (listaDeInstituciones.size() == 1) {
                    List<String> institucion = listaDeInstituciones.get(0);
                    String claveInstitucional = institucion.get(0);
                    if (clave.equals(claveInstitucional)) {
                        Alertas.mostrarMensajeNoCatalogosDisponibles();
                        return;
                    }
                }
                try {
                    sdgcoilvic.mostrarVentanaAgregarProfesorExterno(escenario, this::actualizarDatos);
                } catch (IOException ex) {
                    LOG.error(ex);
                }
            }
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(GestionDeProfesoresControlador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    private void actualizarDatos() {
        llenarComboBoxEstadoProfesor();
        llenarTabla();
    }
    
    private void llenarTabla() {
        ProfesorDAO profesorDAO = new ProfesorDAO();
        List<Profesor> profesorLista = null;
        try {
            profesorLista = profesorDAO.obtenerListaTodosLosProfesores();
            if (profesorLista == null) {
                imageView_noHayProfesores.setVisible(true);
            } else {
                imageView_noHayProfesores.setVisible(false);
                lista.clear();
                for (int i = 0; i < profesorLista.size(); i++) {
                    Profesor profesor = profesorLista.get(i);
                    lista.add(new TablaProfesor(
                            profesor.getIdProfesor(),
                            profesor.getNombre(), 
                            profesor.getApellidoPaterno(), 
                            profesor.getApellidoMaterno(),
                            profesor.getCorreo(),
                            profesor.getClaveInstitucional(),
                            profesor.getEstadoProfesor()));

                }
                tableView_Profesores.setItems(lista);
                column_Nombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
                column_ApellidoPaterno.setCellValueFactory(new PropertyValueFactory<>("apellidoPaterno"));
                column_ApellidoMaterno.setCellValueFactory(new PropertyValueFactory<>("apellidoMaterno"));
                column_Correo.setCellValueFactory(new PropertyValueFactory<>("correo"));
                column_Universidad.setCellValueFactory(new PropertyValueFactory<>("claveInstitucional"));
                column_Estado.setCellValueFactory(new PropertyValueFactory<>("estadoProfesor"));
            }
        } catch (SQLException ex) {
            Alertas.mostrarMensajeErrorBaseDatos();
            LOG.error(ex);
        }
 
    }
}