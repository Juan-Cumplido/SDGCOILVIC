package sdgcoilvic.controladores;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import sdgcoilvic.logicaDeNegocio.clases.Estudiante;
import sdgcoilvic.logicaDeNegocio.implementacionDAO.EstudianteDAO;
import sdgcoilvic.logicaDeNegocio.implementacionDAO.ProfesorDAO;
import sdgcoilvic.utilidades.Alertas;
import sdgcoilvic.utilidades.ColaboracionEnCursoSinglenton;

public class AgregarEstudianteControlador implements Initializable{
    private static final Logger LOG = Logger.getLogger(AgregarEstudianteControlador.class);
    private Stage escenario;
    private Runnable onCloseCallback;
    @FXML private Button button_Cancelar;
    @FXML private Button button_Guardar;
    @FXML private ComboBox<String> comboBox_Institucion;
    @FXML private TextField textField_Nombre;
    @FXML private TextField textField_ApellidoPaterno;
    @FXML private TextField textField_ApellidoMaterno;
    @FXML private TextField textField_Correo;
    

    
    private void aplicarValidacion(TextField textField, String expresionRegular) {
        UnaryOperator<TextFormatter.Change> filtro = cambio -> {
            String nuevoTexto = cambio.getControlNewText();
            return (nuevoTexto.matches(expresionRegular) || nuevoTexto.isEmpty()) ? cambio : null;
        };

        textField.setTextFormatter(new TextFormatter<>(filtro));
    }

    public void setOnCloseCallback(Runnable onCloseCallback) {
        this.onCloseCallback = onCloseCallback;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        if (button_Cancelar.getScene() != null) {
            Stage escenario = (Stage) button_Cancelar.getScene().getWindow();
            escenario.setOnCloseRequest(event -> {
                event.consume(); 
                cancelarRegistro(new ActionEvent()); 
            });
        }
        aplicarValidacion(textField_Nombre, "^[\\p{L}áéíóúÁÉÍÓÚüÜ\\s',;\\-_:\\.]{0,60}$");
        aplicarValidacion(textField_ApellidoPaterno, "^[\\p{L}áéíóúÁÉÍÓÚüÜ\\s',;\\-_:\\.]{1,60}$");
        aplicarValidacion(textField_ApellidoMaterno, "^[\\p{L}áéíóúÁÉÍÓÚüÜ\\s',;\\-_:\\.]{1,60}$");
        
        llenarComboBoxInstitucion();
    }
    
        @FXML
    private void cancelarRegistro(ActionEvent event) {
        if (Alertas.mostrarMensajeCancelar()) {
            Stage escenario = (Stage) button_Cancelar.getScene().getWindow();
            escenario.close();
            if (onCloseCallback != null) {
                onCloseCallback.run();
            }
        }
    }

    public void setStage(Stage escenario) {
        this.escenario = escenario;
    }
    
    private List<List<String>> obtenerListaDeInstituciones() throws SQLException {
        return new ProfesorDAO().obtenerListaDeInstituciones();
    }
    
    private List<String> obtenerNombresDeLasListas(List<List<String>> lista) {
        List<String> nombres = new ArrayList<>();
        lista.forEach(item -> nombres.add(item.get(1)));
        return nombres;
    }
    
    private void llenarComboBoxInstitucion() {
        try {
            List<List<String>> listaDeInstituciones = obtenerListaDeInstituciones();
            ObservableList<String> articulos = FXCollections.observableArrayList(obtenerNombresDeLasListas(listaDeInstituciones));
            comboBox_Institucion.setItems(articulos);
        } catch (SQLException ex) {
            LOG.error(ex);
        }
    }
    
    @FXML
    private void guardarRegistro(ActionEvent event) {
        if (verificarInformacion()) {
            Estudiante estudiante = crearEstudiante();
            if (registrarEstudiante(estudiante) == true) {
                Alertas.mostrarMensajeEstudianteAgregadoExito();
                this.limpiarTextField();
            }    
        }
    }
    
    private boolean registrarEstudiante(Estudiante estudiante) {
        EstudianteDAO estudianteDAO = new EstudianteDAO();
        boolean registroExitoso = false;
        int idColaboracion = ColaboracionEnCursoSinglenton.getInstance().getIdColaboracionEnCurso();
        if (estudiante != null) {
            try {
                if (!estudianteDAO.verificarSiExisteElCorreo(estudiante.getCorreo())) {
                    if (!estudianteDAO.verificarExistenciaEstudiante(estudiante.getNombre(), estudiante.getApellidoPaterno(), estudiante.getApellidoMaterno())) {
                        if (estudianteDAO.registrarEstudiante(estudiante, idColaboracion) == 2) {
                            registroExitoso = true;
                        } else {
                            Alertas.mostrarMensajeInformacionNoRegistrada();
                        }
                    } else {
                        Alertas.mostrarMensajeEstudianteYaExistente();
                    }
                } else {
                    Alertas.mostrarMensajeEmailYaRegistrado();
                }
            } catch (SQLException sqlException) {
                Alertas.mostrarMensajeErrorBaseDatos();
                LOG.fatal("Error en la base de datos en la clase " + this.getClass().getName() + ", método " + Thread.currentThread().getStackTrace()[1].getMethodName() + ": " + sqlException.getMessage(), sqlException);
            }
        }
        return registroExitoso;
    }
    
    private Estudiante crearEstudiante() {
        Estudiante estudiante = new Estudiante();
        estudiante.setNombre(textField_Nombre.getText());
        estudiante.setApellidoPaterno(textField_ApellidoPaterno.getText());
        estudiante.setApellidoMaterno(textField_ApellidoMaterno.getText());
        estudiante.setCorreo(textField_Correo.getText());
        asignarClaveInstitucional(estudiante);
        return estudiante;
    }
    
        private void asignarClaveInstitucional(Estudiante estudiante) {
        int indiceInstitucionSeleccionada = comboBox_Institucion.getSelectionModel().getSelectedIndex();
        if (indiceInstitucionSeleccionada >= 0) {
            try {
                List<List<String>> listaDeInstituciones = obtenerListaDeInstituciones();
                if (indiceInstitucionSeleccionada < listaDeInstituciones.size()) {
                    String claveInstitucion = listaDeInstituciones.get(indiceInstitucionSeleccionada).get(0);
                    estudiante.setClaveInstitucional(claveInstitucion);
                }
            } catch (SQLException ex) {
                LOG.error(ex);
            }
        }
    }
        
    private boolean estaVacio() {
        int indiceInstitucionSeleccionada = comboBox_Institucion.getSelectionModel().getSelectedIndex();
        return textField_Nombre.getText().isEmpty() ||
                textField_ApellidoPaterno.getText().isEmpty() ||
                textField_Correo.getText().isEmpty() ||
                indiceInstitucionSeleccionada < 0 ;
    }
    
        private boolean verificarInformacion() {
        Estudiante estudiante = new Estudiante();
        boolean validacion = true;

        if (!estaVacio()) {
            try {
                estudiante.setNombre(textField_Nombre.getText());
                estudiante.setApellidoPaterno(textField_ApellidoPaterno.getText());
                estudiante.setApellidoMaterno(textField_ApellidoMaterno.getText());
            } catch (IllegalArgumentException illegalArgument) {
                Alertas.mostrarMensajeInformacionInvalida();
                validacion = false;
            }

            try {
                estudiante.setCorreo(textField_Correo.getText());
            } catch (IllegalArgumentException illegalArgument) {
                Alertas.mostrarMensajeCorreoConFormatoInvalido();
                validacion = false;
            }

        } else {
            Alertas.mostrarMensajeCamposVacios();
            validacion = false;
        }
        return validacion;

    }  
        
    private void limpiarTextField() {
        textField_Nombre.setText("");
        textField_ApellidoPaterno.setText("");
        textField_ApellidoMaterno.setText("");
        textField_Correo.setText("");
    }
}