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
import sdgcoilvic.utilidades.Alertas;
import sdgcoilvic.logicaDeNegocio.implementacionDAO.InstitucionDAO;
import sdgcoilvic.logicaDeNegocio.clases.Institucion;
import sdgcoilvic.logicaDeNegocio.enums.EnumPais;

public class RegistrarInstitucionControlador  implements Initializable{
    private static final Logger LOG = Logger.getLogger(RegistrarInstitucionControlador.class);
    private Stage escenario;
   
    @FXML private TextField textField_Clave;
    @FXML private TextField textField_Nombre;
    @FXML private TextField textField_Correo;
    @FXML private ComboBox<String> comboBox_Pais;
    @FXML private Button button_Agregar;
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
        etiquetasDeError();
    }
    
    public void setStage(Stage escenario) {
        this.escenario = escenario;
    }
    
    @FXML
    private void button_Agregar(ActionEvent event) {
        if (verificarInformacion()) {
            Institucion institucion = crearInstitucion();

                InstitucionDAO institucionDAO = new InstitucionDAO();
                try {
                    if (!institucionDAO.verificarSiExisteLaClave(institucion.getClaveInstitucional())) {
                        if (!institucionDAO.verificarSiExisteElNombreInstitucion(institucion.getNombreInstitucion())) {
                            if (!institucionDAO.verificarSiExisteElCorreo(institucion.getCorreo())) {
                                if (institucionDAO.insertarInstitucion(institucion) == 1) {
                                    Alertas.mostrarMensajeExito();
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
        String nombrePais= comboBox_Pais.getValue();
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
    
    private void etiquetasDeError() {
        label_ErrorClave.setVisible(false);
        label_ErrorNombre.setVisible(false);
        label_ErrorCorreo.setVisible(false);
        label_ErrorPais.setVisible(false);
    }
}