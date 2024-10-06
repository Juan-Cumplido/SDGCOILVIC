package sdgcoilvic.controladores;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import sdgcoilvic.logicaDeNegocio.clases.ActividadColaborativa;
import sdgcoilvic.logicaDeNegocio.enums.EnumActividadColaborativa;
import sdgcoilvic.logicaDeNegocio.implementacionDAO.ActividadColaborativaDAO;
import sdgcoilvic.utilidades.Alertas;

public class ActualizarDetallesDeActividadControlador implements Initializable {
    private static final Logger LOG = Logger.getLogger(ActualizarDetallesDeActividadControlador.class);
    private Stage escenario;
    public static int idActividadColaborativa;
    @FXML private VBox  vBox_Principal;
    @FXML private Button button_Aceptar;
    @FXML private Button button_Cancelar;
    @FXML private TextField textField_Nombre;
    @FXML private TextArea txtArea_Instruciones;
    @FXML private TextArea txtArea_Herramientas;
    @FXML private DatePicker datepicker_FechaInicio;
    @FXML private DatePicker datepicker_FechaCierre;
       
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

    private void aplicarValidacion(TextArea textArea, String expresionRegular) {
        UnaryOperator<TextFormatter.Change> filtro = cambio -> {
            String nuevoTexto = cambio.getControlNewText();
            return (nuevoTexto.matches(expresionRegular) || nuevoTexto.isEmpty()) ? cambio : null;
        };

        textArea.setTextFormatter(new TextFormatter<>(filtro));
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        informacionActividad();
        aplicarValidacion(textField_Nombre, "^[\\p{L}áéíóúÁÉÍÓÚüÜ\\s',;:\\-_.0-9]{1,100}$");
        aplicarValidacion(txtArea_Instruciones, "^[\\p{L}áéíóúÁÉÍÓÚüÜ\\s',;:\\-_.0-9]{1,500}$");
        aplicarValidacion(txtArea_Herramientas, "^[\\p{L}áéíóúÁÉÍÓÚüÜ\\s',;:\\-_.0-9]{1,200}$");
        
    }
    
    public void setStage(Stage escenario) {
        this.escenario = escenario;
    }
    
    @FXML
    void button_Cancelar(ActionEvent event) {
            Stage escenario = (Stage) button_Cancelar.getScene().getWindow();
            escenario.close();
    }
   
    private ActividadColaborativa crearActividad() {
        ActividadColaborativa actividadColaborativa = new ActividadColaborativa();
        LocalDate fechaActual = LocalDate.now();
        LocalDate fechaInicioActividad = datepicker_FechaInicio.getValue();
        LocalDate fechaCierreActividad = datepicker_FechaCierre.getValue();
        if(fechaInicioActividad.isBefore(fechaCierreActividad)&&fechaCierreActividad.isAfter(fechaActual)){
            String estadoActividad;
            if(fechaActual.isAfter(fechaInicioActividad)||fechaActual.isEqual(fechaInicioActividad)){
                estadoActividad = EnumActividadColaborativa.Activa.toString();
            }else{
                estadoActividad = EnumActividadColaborativa.Inactiva.toString();
            }
                actividadColaborativa.setNombreActividad(textField_Nombre.getText());
                actividadColaborativa.setInstruccion(txtArea_Instruciones.getText());
                actividadColaborativa.setFechaInicio(datepicker_FechaInicio.getValue().toString());
                actividadColaborativa.setFechaCierre(datepicker_FechaCierre.getValue().toString());
                actividadColaborativa.setHerramienta(txtArea_Herramientas.getText());
                actividadColaborativa.setEstadoActividad(estadoActividad);

        }else{
            Alertas.mostrarMensajeFechaInvalida();
            actividadColaborativa=null;
        }
        
        return actividadColaborativa;
    }
    
    @FXML
    void button_Aceptar(ActionEvent event) {
         if (verificarInformacion()) {
            ActividadColaborativa actividadColaborativa = crearActividad();
            if (actualizarActividad(actividadColaborativa) == true) {
                Alertas.mostrarMensajeExito();
                Stage escenario = (Stage) button_Cancelar.getScene().getWindow();
                escenario.close();
                if (onCloseCallback != null) {
                    onCloseCallback.run();
                }
            }    
        }
    }
    
    private boolean actualizarActividad(ActividadColaborativa actividadColaborativa) {
        ActividadColaborativaDAO actividadColaborativaDAO = new ActividadColaborativaDAO();
        boolean actualizacionExitosa = false;
        if(Objects.nonNull(actividadColaborativa)){
            try {
                ActividadColaborativa actividadColaborativaActual =  actividadColaborativaDAO.consultarActividadColaborativa(idActividadColaborativa);
                if (!hayCambiosEnDatosProfesor(actividadColaborativaActual, actividadColaborativa)) {
                    Alertas.mostrarMensajeDatosIguales();
                    return false;
                }
                if (actividadColaborativaDAO.actualizarInformacionDeLaActividad(actividadColaborativa, idActividadColaborativa) == 1) {
                        actualizacionExitosa = true;
                } else {
                        Alertas.mostrarMensajeInformacionNoRegistrada();
                }
               
            } catch (SQLException sqlException) {
                Alertas.mostrarMensajeErrorBaseDatos();
                LOG.fatal("Error en la base de datos" + this.getClass().getName() + ", método " + Thread.currentThread().getStackTrace()[1].getMethodName() + ": " + sqlException.getMessage(), sqlException);
            }
        }
        return actualizacionExitosa;
    }

    
    private boolean hayCambiosEnDatosProfesor(ActividadColaborativa actividadColaborativaActual, ActividadColaborativa actividadColaborativaNuevo) {
        return (actividadColaborativaActual.getNombreActividad() == null ? actividadColaborativaNuevo.getNombreActividad() != null : !actividadColaborativaActual.getNombreActividad().equals(actividadColaborativaNuevo.getNombreActividad())) ||
               (actividadColaborativaActual.getInstruccion() == null ? actividadColaborativaNuevo.getInstruccion() != null : !actividadColaborativaActual.getInstruccion().equals(actividadColaborativaNuevo.getInstruccion())) ||
               (actividadColaborativaActual.getHerramienta() == null ? actividadColaborativaNuevo.getHerramienta() != null : !actividadColaborativaActual.getHerramienta().equals(actividadColaborativaNuevo.getHerramienta())) ||
               (actividadColaborativaActual.getFechaInicio() == null ? actividadColaborativaNuevo.getFechaInicio() != null : !actividadColaborativaActual.getFechaInicio().equals(actividadColaborativaNuevo.getFechaInicio())) ||
               (actividadColaborativaActual.getFechaCierre() == null ? actividadColaborativaNuevo.getFechaCierre() != null : !actividadColaborativaActual.getFechaCierre().equals(actividadColaborativaNuevo.getFechaCierre()));
    }
    
    private boolean estaVacio() {
        return textField_Nombre.getText().isEmpty()||
                txtArea_Instruciones.getText().isEmpty()||
                txtArea_Herramientas.getText().isEmpty()||
           datepicker_FechaInicio.getValue() == null ||
           datepicker_FechaCierre.getValue() == null;
    }
    
    private boolean verificarInformacion(){
        ActividadColaborativa actividadColaborativa = new   ActividadColaborativa();
        boolean validacion = true;
        
        if (!estaVacio()){
            try{
                actividadColaborativa.setNombreActividad(textField_Nombre.getText());
                actividadColaborativa.setInstruccion(txtArea_Instruciones.getText());
                actividadColaborativa.setHerramienta(txtArea_Herramientas.getText());
               
            } catch (IllegalArgumentException illegalArgument){
                Alertas.mostrarMensajeInformacionInvalida();
                validacion = false;
            }
        
        }else {
          Alertas.mostrarMensajeCamposVacios();
          validacion = false;  
        }
        return validacion;
        
    }
    
    private void informacionActividad() {
        ActividadColaborativaDAO actividadColaborativaDAO = new ActividadColaborativaDAO();
        ActividadColaborativa actividadColaborativa = null;

        try {
            actividadColaborativa = actividadColaborativaDAO.consultarActividadColaborativa(idActividadColaborativa);
        } catch (SQLException sQLException) {
            LOG.error("Error al consultar la actividad colaborativa: " + sQLException.getMessage());
            Alertas.mostrarMensajeErrorBaseDatos();
            return;
        }

        if (actividadColaborativa != null) {
            textField_Nombre.setText(actividadColaborativa.getNombreActividad());
            txtArea_Instruciones.setText(actividadColaborativa.getInstruccion());
            txtArea_Herramientas.setText(actividadColaborativa.getHerramienta());
            try {
                LocalDate fechaInicio = LocalDate.parse(actividadColaborativa.getFechaInicio());
                LocalDate fechaCierre = LocalDate.parse(actividadColaborativa.getFechaCierre());

                datepicker_FechaInicio.setValue(fechaInicio);
                datepicker_FechaCierre.setValue(fechaCierre);
            } catch (DateTimeParseException e) {
                LOG.error("Error al parsear las fechas: " + e.getMessage());
               
            }
        } else {
            LOG.error("La actividad colaborativa con id " + idActividadColaborativa + " no fue encontrada.");
           
        }
    }

}