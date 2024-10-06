package sdgcoilvic.controladores;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import sdgcoilvic.logicaDeNegocio.clases.SolicitudColaboracion;
import sdgcoilvic.logicaDeNegocio.implementacionDAO.SolicitudColaboracionDAO;
import sdgcoilvic.utilidades.AccesoSingleton;
import sdgcoilvic.utilidades.Alertas;

public class DeclaracionDePropositoControlador implements Initializable{
    private static final Logger LOG = Logger.getLogger(DeclaracionDePropositoControlador.class);
    private Stage escenario;
    private AccesoSingleton accesoSingleton;
    public static int idPropuestaColaboracion;
    @FXML private TextArea txtArea_Comentario;
    @FXML private Button button_Regresar;
    @FXML private Button button_Enviar;

    private Runnable onCloseCallback;

    public void setOnCloseCallback(Runnable onCloseCallback) {
        this.onCloseCallback = onCloseCallback;
    }
    
    private void aplicarValidacion(TextArea textArea, String expresionRegular) {
        UnaryOperator<TextFormatter.Change> filtro = cambio -> {
            String nuevoTexto = cambio.getControlNewText();
            return (nuevoTexto.matches(expresionRegular) || nuevoTexto.isEmpty()) ? cambio : null;
        };

        textArea.setTextFormatter(new TextFormatter<>(filtro));
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       aplicarValidacion(txtArea_Comentario, "^[\\p{L}áéíóúÁÉÍÓÚüÜ\\s',;:\\-_.0-9]{1,500}$");
       accesoSingleton = AccesoSingleton.getInstance();
           
    }
    
    public void setStage(Stage escenario) {
        this.escenario = escenario;
    }
    
    @FXML
    void button_Regresar(ActionEvent event) {
        if (Alertas.mostrarMensajeCancelar()) {
            Stage escenario = (Stage) button_Regresar.getScene().getWindow();
            SDGCOILVIC sdgcoilvic = new SDGCOILVIC();
            try {
                sdgcoilvic.mostrarVentanaAdministrarColaboracionesDisponibles(escenario);
            } catch (IOException exception) {
                LOG.error(exception);
            }
        }

    }
    
    @FXML
    private void button_Enviar(ActionEvent event) {
        SolicitudColaboracionDAO solicitudColaboracionDAO = new SolicitudColaboracionDAO();
        int idAcceso = accesoSingleton.getAccesoId();    
            if (verificarInformacion()) {
                try {
                    if (solicitudColaboracionDAO.enviarSolicitudDeColaboracion(idPropuestaColaboracion, txtArea_Comentario.getText(),idAcceso)== 1) {
                            Alertas.mostrarMensajeExito();
                                Stage escenario = (Stage) button_Regresar.getScene().getWindow();
                                SDGCOILVIC sdgcoilvic = new SDGCOILVIC();
                                try {
                                    sdgcoilvic.mostrarVentanaAdministrarColaboracionesDisponibles(escenario);
                                } catch (IOException ex) {
                                    LOG.error(ex);
                                }

                    }else {
                        Alertas.mostrarMensajeInformacionNoRegistrada();
                    }    
                } catch (SQLException ex) {
                   Alertas.mostrarMensajeErrorBaseDatos();
                   LOG.error(ex);
                }
            } 
    }
    
    private boolean estaVacio() {
        return  txtArea_Comentario.getText().isEmpty();
    }
    
    private boolean verificarInformacion(){
        SolicitudColaboracion solicitudColaboracion = new   SolicitudColaboracion();
        boolean validacion = true;
        
        if (!estaVacio()){
            try{
                solicitudColaboracion.setMensaje(txtArea_Comentario.getText());
            } catch (IllegalArgumentException illegalArgument){
                LOG.warn(illegalArgument);
                validacion = false;
            }
        
        }else {
          Alertas.mostrarMensajeCamposVacios();
          validacion = false;  
        }
        return validacion;
        
    }
}