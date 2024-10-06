package sdgcoilvic.controladores;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import org.apache.log4j.Logger;
import sdgcoilvic.utilidades.Alertas;
import sdgcoilvic.logicaDeNegocio.implementacionDAO.ProfesorDAO;
import sdgcoilvic.logicaDeNegocio.clases.Profesor;
import sdgcoilvic.logicaDeNegocio.enums.EnumProfesor;

public class ModificarProfesorExternoControlador implements Initializable{
    private static final Logger LOG = Logger.getLogger(ModificarProfesorExternoControlador.class);
    private static String correoAntiguo;
    private static String estadoProfesorAnterior;
    private Stage escenario;
    public static String idProfesor;
    
    @FXML private Button button_Cancelar;
    @FXML private Button button_Guardar;
    @FXML private ComboBox<String> comboBox_Idioma;
    @FXML private ComboBox<String> comboBox_Institucion;
    @FXML private ComboBox<String> comboBox_EstadoProfesor;
    @FXML private Label label_EstadoProfesor;
    @FXML private TextField textField_Nombre;
    @FXML private TextField textField_ApellidoPaterno;
    @FXML private TextField textField_ApellidoMaterno;
    @FXML private TextField textField_Correo;
    
    private Runnable onCloseCallback;

    public void setOnCloseCallback(Runnable onCloseCallback) {
        this.onCloseCallback = onCloseCallback;
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
        llenarComboBoxIdioma();
        llenarComboBoxInstitucion();
        informacionProfesor();
        llenarComboBoxEstadoProfesor();
        correoAntiguo = textField_Correo.getText(); 
        estadoProfesorAnterior = comboBox_EstadoProfesor.getValue();
        if(estadoProfesorAnterior.equals(EnumProfesor.Activo.toString())|| estadoProfesorAnterior.equals(EnumProfesor.Archivado.toString())){
            comboBox_EstadoProfesor.setVisible(true);
            label_EstadoProfesor.setVisible(true);
        }else{
            comboBox_EstadoProfesor.setVisible(false);
            label_EstadoProfesor.setVisible(false);
        }
        aplicarValidacion(textField_Nombre, "^[\\p{L}áéíóúÁÉÍÓÚüÜ\\s',;\\-_:\\.]{0,60}$");
        aplicarValidacion(textField_ApellidoPaterno, "^[\\p{L}áéíóúÁÉÍÓÚüÜ\\s',;\\-_:\\.]{1,60}$");
        aplicarValidacion(textField_ApellidoMaterno, "^[\\p{L}áéíóúÁÉÍÓÚüÜ\\s',;\\-_:\\.]{1,60}$");
 
    }
    
    
    public void setStage(Stage escenario) {
        this.escenario = escenario;
    }

    private void llenarComboBoxEstadoProfesor() {
        ObservableList<String> items = FXCollections.observableArrayList("Activo", "Archivado");
        comboBox_EstadoProfesor.setItems(items);
    }
    
    private void llenarComboBoxIdioma() {
        List<List<String>> listaDeIdiomas = obtenerListaDeIdiomas();
        ObservableList<String> items = FXCollections.observableArrayList(obtenerNombresDeLasListas(listaDeIdiomas));
        comboBox_Idioma.setItems(items);
    }

    private void llenarComboBoxInstitucion() {
        List<List<String>> listaDeInstituciones = obtenerListaDeInstituciones();
        List<String> nombresFiltrados = new ArrayList<>();
        for (List<String> institucion : listaDeInstituciones) {
            String claveInstitucional = institucion.get(0);
            String nombreInstitucion = institucion.get(1);
            
            if (!claveInstitucional.equals("30MSU0940B") && !nombreInstitucion.equals("Universidad Veracruzana")) {
                nombresFiltrados.add(nombreInstitucion);
            }
        }
        ObservableList<String> items = FXCollections.observableArrayList(nombresFiltrados);
        comboBox_Institucion.setItems(items);
    }

    private List<List<String>> obtenerListaDeIdiomas(){
        return new ProfesorDAO().obtenerListaDeIdiomas();
    }

    private List<List<String>> obtenerListaDeInstituciones(){
        return new ProfesorDAO().obtenerListaDeInstituciones();
    }

    private List<String> obtenerNombresDeLasListas(List<List<String>> lista) {
        List<String> nombres = new ArrayList<>();
        lista.forEach(item -> nombres.add(item.get(1)));
        return nombres;
    }

    @FXML
    void button_Cancelar(ActionEvent event) {
            Stage escenario = (Stage) button_Cancelar.getScene().getWindow();
            escenario.close();

    }

    @FXML
    void button_Guardar(ActionEvent event) {
         if (verificarInformacion()) {
            Profesor profesor = crearProfesor();
            if (actualizarProfesor(profesor) == true) {
                Alertas.mostrarMensajeExito();
                Stage escenario = (Stage) button_Cancelar.getScene().getWindow();
                escenario.close();
                if (onCloseCallback != null) {
                    onCloseCallback.run();
                }
            }    
        }
    }

    
    private Profesor crearProfesor() {
        Profesor profesor = new Profesor();
        profesor.setNombre(textField_Nombre.getText());
        profesor.setApellidoPaterno(textField_ApellidoPaterno.getText());
        profesor.setApellidoMaterno(textField_ApellidoMaterno.getText());
        profesor.setCorreo(textField_Correo.getText());
        asignarIdIdioma(profesor);
        asignarClaveInstitucional(profesor);
        String estadoProfesor = comboBox_EstadoProfesor.getValue();
        if (estadoProfesor != null) {
            profesor.setEstadoProfesor(estadoProfesor);
        } else {
            profesor.setEstadoProfesor(""); 
        }
        return profesor;
    }
    
    private boolean actualizarProfesor(Profesor profesor) {
        ProfesorDAO profesorDAO = new ProfesorDAO();
        boolean actualizacionExitosa = false;
        String nuevoCorreo = profesor.getCorreo();
            try {
                Profesor profesorActual = profesorDAO.obtenerProfesorPorCorreo(profesor.getCorreo());
                if (!hayCambiosEnDatosProfesor(profesorActual, profesor)) {
                    Alertas.mostrarMensajeDatosIguales();
                    return false;
                }
                if (nuevoCorreo.equals(correoAntiguo)) {
                    if (profesorDAO.actualizarInformacionDelProfesor(profesor, idProfesor) == 1) {
                        actualizacionExitosa = true;
                    } else {
                        Alertas.mostrarMensajeInformacionNoRegistrada();
                    }
                } else {
                    if (!profesorDAO.verificarSiExisteElCorreo(nuevoCorreo)) {
                        if (profesorDAO.actualizarInformacionDelProfesor(profesor, idProfesor) == 1) {
                            actualizacionExitosa = true;
                        } else {
                            Alertas.mostrarMensajeInformacionNoRegistrada();
                        }
                    } else {
                       Alertas.mostrarMensajeEmailYaRegistrado();
                    }
                }
            } catch (SQLException sqlException) {
                Alertas.mostrarMensajeErrorBaseDatos();
                LOG.fatal("Error en la base de datos" + this.getClass().getName() + ", método " + Thread.currentThread().getStackTrace()[1].getMethodName() + ": " + sqlException.getMessage(), sqlException);
            }
        
        return actualizacionExitosa;
    }
    
    private boolean hayCambiosEnDatosProfesor(Profesor profesorActual, Profesor nuevoProfesor) {
        return (profesorActual.getNombre() == null ? nuevoProfesor.getNombre() != null : !profesorActual.getNombre().equals(nuevoProfesor.getNombre())) ||
               (profesorActual.getApellidoPaterno() == null ? nuevoProfesor.getApellidoPaterno() != null : !profesorActual.getApellidoPaterno().equals(nuevoProfesor.getApellidoPaterno())) ||
               (profesorActual.getApellidoMaterno() == null ? nuevoProfesor.getApellidoMaterno() != null : !profesorActual.getApellidoMaterno().equals(nuevoProfesor.getApellidoMaterno())) ||
               (profesorActual.getCorreo() == null ? nuevoProfesor.getCorreo() != null : !profesorActual.getCorreo().equals(nuevoProfesor.getCorreo())) ||
               profesorActual.getIdIdiomas() != nuevoProfesor.getIdIdiomas() ||
               (profesorActual.getEstadoProfesor() == null ? nuevoProfesor.getEstadoProfesor() != null : !profesorActual.getEstadoProfesor().equals(nuevoProfesor.getEstadoProfesor())) ||
               (profesorActual.getClaveInstitucional() == null ? nuevoProfesor.getClaveInstitucional() != null : !profesorActual.getClaveInstitucional().equals(nuevoProfesor.getClaveInstitucional()));
    }


    
    private void asignarClaveInstitucional(Profesor profesor) {
        int indiceInstitucionSeleccionada = comboBox_Institucion.getSelectionModel().getSelectedIndex();
        if (indiceInstitucionSeleccionada >= 0) {
            List<List<String>> listaDeInstituciones = obtenerListaDeInstituciones();
            if (indiceInstitucionSeleccionada < listaDeInstituciones.size()) {
                String claveInstitucion = listaDeInstituciones.get(indiceInstitucionSeleccionada).get(0);
                profesor.setClaveInstitucional(claveInstitucion);
            }
        }
    }
    
    private void asignarIdIdioma(Profesor profesor) {
        int indiceIdiomaSeleccionado = comboBox_Idioma.getSelectionModel().getSelectedIndex();
        if (indiceIdiomaSeleccionado >= 0) {
            try {
                List<List<String>> listaDeIdiomas = obtenerListaDeIdiomas();
                if (indiceIdiomaSeleccionado < listaDeIdiomas.size()) {
                    int idIdioma = Integer.parseInt(listaDeIdiomas.get(indiceIdiomaSeleccionado).get(0));
                    profesor.setIdIdiomas(idIdioma);
                }
            } catch (NumberFormatException ex) {
                LOG.error(ex);
            }
        }
    }
    

    
    private boolean estaVacio() {
        int indiceInstitucionSeleccionada = comboBox_Institucion.getSelectionModel().getSelectedIndex();
        int indiceIdiomaSeleccionado = comboBox_Idioma.getSelectionModel().getSelectedIndex();
        return textField_Nombre.getText().isEmpty() ||
                textField_ApellidoPaterno.getText().isEmpty() ||
                textField_Correo.getText().isEmpty() ||
                indiceInstitucionSeleccionada < 0 ||
                indiceIdiomaSeleccionado < 0;
    }
    
    private boolean verificarInformacion() {
        Profesor profesor = new Profesor();
        boolean validacion = true;

        if (!estaVacio()) {
            try {
                profesor.setNombre(textField_Nombre.getText());
                profesor.setApellidoPaterno(textField_ApellidoPaterno.getText());
                profesor.setApellidoMaterno(textField_ApellidoMaterno.getText());
            } catch (IllegalArgumentException illegalArgument ) {
                Alertas.mostrarMensajeInformacionInvalida();
                validacion = false;
            }

            try {
                profesor.setCorreo(textField_Correo.getText());
            } catch (IllegalArgumentException coreoException) {
                Alertas.mostrarMensajeCorreoConFormatoInvalido();
                validacion = false;
            }

        } else {
            Alertas.mostrarMensajeCamposVacios();
            validacion = false;
        }
        return validacion;

    }  
        
    private void informacionProfesor() {
        Profesor profesor = new Profesor();
        ProfesorDAO profesorDAO = new ProfesorDAO();
        try {
            profesor = profesorDAO.obtenerProfesorPorID(idProfesor);
        } catch (SQLException sQLExcpetion) {
            Alertas.mostrarMensajeErrorBaseDatos();
        }
            textField_Nombre.setText(profesor.getNombre());
            textField_ApellidoPaterno.setText(profesor.getApellidoPaterno());
            textField_ApellidoMaterno.setText(profesor.getApellidoMaterno());
            textField_Correo.setText(profesor.getCorreo());
            asignarNombreInstitucion(profesor);
            asignarNombreIdioma(profesor);
            comboBox_EstadoProfesor.setValue(profesor.getEstadoProfesor());
            estadoProfesorAnterior = profesor.getEstadoProfesor();
    }

    private void asignarNombreInstitucion(Profesor profesor) {
        String claveInstitucional = profesor.getClaveInstitucional();
        List<List<String>> listaDeInstituciones = obtenerListaDeInstituciones();
        for (List<String> institucion : listaDeInstituciones) {
            if (institucion.get(0).equals(claveInstitucional)) {
                comboBox_Institucion.setValue(institucion.get(1));
                return;
            }
        }
    }

    private void asignarNombreIdioma(Profesor profesor) {
        int idIdioma = profesor.getIdIdiomas();
        List<List<String>> listaDeIdiomas = obtenerListaDeIdiomas();
        for (List<String> idioma : listaDeIdiomas) {
            if (Integer.parseInt(idioma.get(0)) == idIdioma) {
                comboBox_Idioma.setValue(idioma.get(1));
                return;
            }
        }
    }
}