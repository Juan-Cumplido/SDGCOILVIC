package sdgcoilvic.controladores;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import sdgcoilvic.logicaDeNegocio.clases.Institucion;
import sdgcoilvic.logicaDeNegocio.enums.EnumPais;
import sdgcoilvic.logicaDeNegocio.implementacionDAO.InstitucionDAO;
import sdgcoilvic.utilidades.Alertas;

public class ModificarInstitucionControlador implements Initializable{
    private static final Logger LOG = Logger.getLogger(ModificarInstitucionControlador.class);
    private Stage  escenario;
    public static String clave;
    private static String claveAntigua;
    @FXML private TextField textField_Clave;
    @FXML private TextField textField_Nombre;
    @FXML private TextField textField_Correo;
    @FXML private ComboBox<String> comboBox_Pais;
    @FXML private Button button_Modificar;
    @FXML private Button button_Cancelar;
    @FXML private Label label_ErrorClave;
    @FXML private Label label_ErrorNombre;
    @FXML private Label label_ErrorCorreo;
    @FXML private Label label_ErrorPais;
    
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
    public void initialize(URL url, ResourceBundle rb) {

        aplicarValidacion(textField_Clave, "^[A-Z0-9]{1,20}$");
        aplicarValidacion(textField_Nombre, "^[\\p{L}áéíóúÁÉÍÓÚüÜ\\s',\\-_\\.]{1,200}$");
        llenarComboBoxPais();
        informacionInstitucion();
        etiquetasDeError();
        claveAntigua = textField_Clave.getText(); 
    }
    
    public void setStage(Stage escenario) {
        this.escenario = escenario;
    }
    
    
    @FXML
    private void button_Modificar(ActionEvent event) {
        if (verificarInformacion()) {
            Institucion institucionNueva = crearInstitucion();
            String claveNueva = institucionNueva.getClaveInstitucional();
            String nombreNuevo = institucionNueva.getNombreInstitucion();
            String correoNuevo = institucionNueva.getCorreo();
                   
                InstitucionDAO institucionDAO = new InstitucionDAO();
                try {
                   Institucion institucionActual = institucionDAO.obtenerInstitucionPorClave(claveAntigua);
                   String claveInstitucionalAntigua = institucionActual.getClaveInstitucional();
                   String nombreAntiguo = institucionActual.getNombreInstitucion();
                   String correoAntiguo = institucionActual.getCorreo();
                   
                   if (!hayCambiosEnDatosInstitucion(institucionNueva, institucionActual)) {
                        Alertas.mostrarMensajeDatosIguales();
                        return;
                        } 
                   if (claveNueva.equals(claveInstitucionalAntigua) || !institucionDAO.verificarSiExisteLaClave(institucionNueva.getClaveInstitucional())) {
                        if (nombreNuevo.equals(nombreAntiguo) || !institucionDAO.verificarSiExisteElNombreInstitucion(institucionNueva.getNombreInstitucion())) {
                            if (correoNuevo.equals(correoAntiguo) || !institucionDAO.verificarSiExisteElCorreo(institucionNueva.getCorreo())) {
                                if (institucionDAO.actualizarInformacionDeLaInstitucion(institucionNueva, claveAntigua) == 1) {
                                    Alertas.mostrarMensajeActualizacionExita();
                                    Stage escenario = (Stage) button_Cancelar.getScene().getWindow();
                                    escenario.close();
                                    if (onCloseCallback != null) {
                                        onCloseCallback.run();
                                    }
                                } else {
                                      Alertas.mostrarMensajeInformacionNoRegistrada();
                                }
                            } else {
                                Alertas.mostrarMensajeEmailYaRegistrado();
                            }
                        } else {
                            Alertas.mostrarMensajeInstitucionYaExistente();
                        }
                    } else {
                         Alertas.mostrarMensajeClaveInstitucionalYaRegistrada();
                    }
                } catch (SQLException ex) {
                    Alertas.mostrarMensajeErrorBaseDatos();
                    LOG.fatal("Error en la base de datos en la clase " + this.getClass().getName() + ", método " + Thread.currentThread().getStackTrace()[1].getMethodName() + ": " + ex.getMessage(), ex);
                }  
        }
     
    }
    
    private boolean hayCambiosEnDatosInstitucion(Institucion institucionActual, Institucion institucionNueva) {
        return (institucionActual.getClaveInstitucional() == null ? institucionNueva.getClaveInstitucional() != null : !institucionActual.getClaveInstitucional().equals(institucionNueva.getClaveInstitucional())) ||
               (institucionActual.getNombreInstitucion() == null ? institucionNueva.getNombreInstitucion() != null : !institucionActual.getNombreInstitucion().equals(institucionNueva.getNombreInstitucion())) ||
               (institucionActual.getNombrePais() == null ? institucionNueva.getNombrePais() != null : !institucionActual.getNombrePais().equals(institucionNueva.getNombrePais())) ||
               (institucionActual.getCorreo() == null ? institucionNueva.getCorreo() != null : !institucionActual.getCorreo().equals(institucionNueva.getCorreo()));
    }
    
    private void llenarComboBoxPais() {
        ObservableList<String> items = FXCollections.observableArrayList();
        for (EnumPais pais : EnumPais.values()) {
            items.add(pais.name());
        }
        comboBox_Pais.setItems(items);
    }
    
    private Institucion crearInstitucion() {
        Institucion institucion = new Institucion();
        institucion.setClaveInstitucional(textField_Clave.getText());
        institucion.setNombreInstitucion(textField_Nombre.getText());
        String nombrePais = comboBox_Pais.getValue();
        if (nombrePais != null) {
            institucion.setNombrePais(nombrePais);
        } else {
            institucion.setNombrePais(""); 
        }
        institucion.setCorreo(textField_Correo.getText());
        return institucion;
    }

    private boolean estaVacio() {
        return textField_Clave.getText().isEmpty()||
                textField_Nombre.getText().isEmpty()||
                textField_Correo.getText().isEmpty()||
                comboBox_Pais.getSelectionModel().getSelectedIndex() < 0 ;
    }
    
    private boolean verificarInformacion(){
        Institucion institucion = new   Institucion();
        boolean validacion = true;
        
        if (!estaVacio()){
            try{
                institucion.setClaveInstitucional(textField_Clave.getText());
            } catch (IllegalArgumentException claveInstitucionalException){
                label_ErrorClave.setVisible(true);
                validacion = false;
            }

            try{
                institucion.setNombreInstitucion(textField_Nombre.getText());
            } catch (IllegalArgumentException nombreException){
                label_ErrorNombre.setVisible(true);
                validacion = false;
            } 

            try{
                institucion.setCorreo(textField_Correo.getText());
            } catch (IllegalArgumentException coreoException){
                label_ErrorCorreo.setVisible(true);
                Alertas.mostrarMensajeCorreoConFormatoInvalido();
                validacion = false;
                } 
        }else {
          Alertas.mostrarMensajeCamposVacios();
          validacion = false;  
        }
        return validacion;
        
    }
    
    @FXML
    void button_Cancelar(ActionEvent event) {
        if (Alertas.mostrarMensajeCancelar()) {
            Stage escenario = (Stage) button_Cancelar.getScene().getWindow();
            escenario.close();
        }
    }
    
    private void informacionInstitucion() {
        Institucion institucion = new Institucion();
        InstitucionDAO institucionDAO = new InstitucionDAO();
        try {
            institucion = institucionDAO.obtenerInstitucionPorClave(clave);
        } catch (SQLException sQLExcpetion) {
            Alertas.mostrarMensajeErrorBaseDatos();
        }
            textField_Nombre.setText(institucion.getNombreInstitucion());
            textField_Clave.setText(institucion.getClaveInstitucional());
            textField_Correo.setText(institucion.getCorreo());
            comboBox_Pais.setValue(institucion.getNombrePais());

    }
    
    private void etiquetasDeError() {
        label_ErrorClave.setVisible(false);
        label_ErrorNombre.setVisible(false);
        label_ErrorCorreo.setVisible(false);
        label_ErrorPais.setVisible(false);
    }
}
