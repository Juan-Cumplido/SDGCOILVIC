package sdgcoilvic.controladores;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
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
import sdgcoilvic.utilidades.AccesoSingleton;
import sdgcoilvic.utilidades.Alertas;
import sdgcoilvic.utilidades.ColaboracionEnCursoSinglenton;

public class IniciarActividadControlador implements Initializable{
    private static final Logger LOG = Logger.getLogger(IniciarActividadControlador.class);
    private Stage escenario;
    private ColaboracionEnCursoSinglenton colaboracionEnCursoSinglenton;
    private AccesoSingleton accesoSingleton;
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
        colaboracionEnCursoSinglenton = ColaboracionEnCursoSinglenton.getInstance();
        accesoSingleton = AccesoSingleton.getInstance();
        aplicarValidacion(textField_Nombre, "^[\\p{L}áéíóúÁÉÍÓÚüÜ\\s',;:\\-_.0-9]{1,100}$");
        aplicarValidacion(txtArea_Instruciones, "^[\\p{L}áéíóúÁÉÍÓÚüÜ\\s',;:\\-_.0-9]{1,500}$");
        aplicarValidacion(txtArea_Herramientas, "^[\\p{L}áéíóúÁÉÍÓÚüÜ\\s',;:\\-_.0-9]{1,200}$");
        
    }
    public void setStage(Stage escenario) {
        this.escenario = escenario;
    }
    
    @FXML
    void button_Cancelar(ActionEvent event) {
        if (Alertas.mostrarMensajeCancelar()) {
            Stage escenario = (Stage) button_Cancelar.getScene().getWindow();
            escenario.close();
        }
    }
    
    private ActividadColaborativa crearActividad() {
        ActividadColaborativa actividadColaborativa = new ActividadColaborativa();
        LocalDate fechaActual = LocalDate.now();
        LocalDate fechaInicioActividad = datepicker_FechaInicio.getValue();
        LocalDate fechaCierreActividad = datepicker_FechaCierre.getValue();
        int idColaboracionEnCurso = colaboracionEnCursoSinglenton.getIdColaboracionEnCurso();
        int idProfesor = accesoSingleton.getAccesoId();
        if(fechaInicioActividad.isBefore(fechaCierreActividad)&&fechaCierreActividad.isAfter(fechaActual)){
            String estadoActividad;
            if(fechaActual.isAfter(fechaInicioActividad)||fechaActual.isEqual(fechaInicioActividad)){
                estadoActividad = EnumActividadColaborativa.Activa.toString();
            }else{
                estadoActividad = EnumActividadColaborativa.Inactiva.toString();
            }
                actividadColaborativa.setNombreActividad(textField_Nombre.getText());
                actividadColaborativa.setInstruccion(txtArea_Instruciones.getText());
                actividadColaborativa.setIdColaboracion(idColaboracionEnCurso);
                actividadColaborativa.setFechaInicio(datepicker_FechaInicio.getValue().toString());
                actividadColaborativa.setFechaCierre(datepicker_FechaCierre.getValue().toString());
                actividadColaborativa.setHerramienta(txtArea_Herramientas.getText());
                actividadColaborativa.setEstadoActividad(estadoActividad);
                actividadColaborativa.setIdProfesor(idProfesor);
        }else{
            Alertas.mostrarMensajeFechaInvalida();
            actividadColaborativa=null;
        }
        
        return actividadColaborativa;
    }

    @FXML
    void button_Aceptar(ActionEvent event) {
        if (verificarInformacion()) {
        ActividadColaborativa nuevaActividad = crearActividad();
            ActividadColaborativaDAO actividadColaborativaDAO = new ActividadColaborativaDAO();
            if(Objects.nonNull(nuevaActividad)){
                try {
                    int resultadoRegistro = actividadColaborativaDAO.agregarActividadColaborativa(nuevaActividad);
                    if (resultadoRegistro == 1) {
                        Alertas.mostrarMensajeRegistroActividadExito();
                        Stage miVentana = (Stage) vBox_Principal.getScene().getWindow();
                        miVentana.close();
                            if (onCloseCallback != null) {
                                onCloseCallback.run();
                            }
                    } else if (resultadoRegistro == -1) {
                        Alertas.mostrarMensajeInformacionNoRegistrada();
                    }
                 } catch (SQLException ex) {
                    Alertas.mostrarMensajeErrorBaseDatos();
                    LOG.fatal("Error en la base de datos en la clase " + this.getClass().getName() + ", método " + Thread.currentThread().getStackTrace()[1].getMethodName() + ": " + ex.getMessage(), ex);
                } 
            }
        }
    }
    
    private boolean estaVacio() {
        return textField_Nombre.getText().isEmpty() ||
           txtArea_Instruciones.getText().isEmpty() ||
           txtArea_Herramientas.getText().isEmpty() ||
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
    
}