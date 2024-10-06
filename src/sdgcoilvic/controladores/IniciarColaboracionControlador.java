package sdgcoilvic.controladores;
import java.sql.SQLException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import sdgcoilvic.logicaDeNegocio.clases.Colaboracion;
import sdgcoilvic.logicaDeNegocio.implementacionDAO.ColaboracionDAO;
import sdgcoilvic.utilidades.AccesoSingleton;
import sdgcoilvic.utilidades.Alertas;
import sdgcoilvic.utilidades.ColaboracionEnCursoSinglenton;
import sdgcoilvic.utilidades.ImagesSetter;

public class IniciarColaboracionControlador  implements Initializable{
    private static final Logger LOG= Logger.getLogger(IniciarColaboracionControlador.class);
    private Stage escenario;
    private AccesoSingleton accesoSingleton;
    public static  int idPropuestaColaboracion;
    @FXML private TextArea txtArea_Descripcion;
    @FXML private TextArea txtArea_Recursos;
    @FXML private TextArea txtArea_Aprendizajes;
    @FXML private TextArea txtArea_Evaluacion;
    @FXML private TextArea txtArea_Asistencia;
    @FXML private TextArea txtArea_Entorno;  
    
    @FXML private Button button_Cancelar;
    @FXML private Button button_Iniciar;
    
    @FXML private ImageView imageView_SubMenu;
    
    private void mostrarImagen() {
        imageView_SubMenu.setImage(ImagesSetter.getImageSubMenu());
    } 
    
    private void aplicarValidacion(TextArea textArea, String expresionRegular) {
        UnaryOperator<TextFormatter.Change> filtro = cambio -> {
            String nuevoTexto = cambio.getControlNewText();
            return (nuevoTexto.matches(expresionRegular) || nuevoTexto.isEmpty()) ? cambio : null;
        };

        textArea.setTextFormatter(new TextFormatter<>(filtro));
    }
    
    public void setStage(Stage escenario) {
        this.escenario = escenario;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mostrarImagen();
        accesoSingleton = AccesoSingleton.getInstance();
        aplicarValidacion(txtArea_Descripcion,  "^[\\p{L}áéíóúÁÉÍÓÚüÜ\\s',;:\\-_.0-9]{1,500}$");
        aplicarValidacion(txtArea_Recursos, "^[\\p{L}áéíóúÁÉÍÓÚüÜ\\s',;:\\-_.0-9]{1,500}$");
        aplicarValidacion(txtArea_Aprendizajes, "^[\\p{L}áéíóúÁÉÍÓÚüÜ\\s',;:\\-_.0-9]{1,500}$");
        aplicarValidacion(txtArea_Evaluacion, "^[\\p{L}áéíóúÁÉÍÓÚüÜ\\s',;:\\-_.0-9]{1,500}$");
        aplicarValidacion(txtArea_Asistencia, "^[\\p{L}áéíóúÁÉÍÓÚüÜ\\s',;:\\-_.0-9]{1,500}$");
        aplicarValidacion(txtArea_Entorno, "^[\\p{L}áéíóúÁÉÍÓÚüÜ\\s',;:\\-_.0-9]{1,500}$");   
    
    }
    
    @FXML
    void button_Cancelar(ActionEvent event) {
        if (Alertas.mostrarMensajeCancelar()) {
            Stage escenario = (Stage) button_Cancelar.getScene().getWindow();
            SDGCOILVIC sdgcoilvic = new SDGCOILVIC();

            try {
                sdgcoilvic.mostrarVentanaAdministrarPropuestasDeColaboracion(escenario);
            } catch (IOException ex) {
                LOG.error( ex);
            }
        }
    }
    
    @FXML
    void button_Iniciar(ActionEvent event) {
        if (verificarInformacion()) {
            Colaboracion colaboracion = crearColaboracion();
            int idColaboracion = registraColaboracion(colaboracion);
            if (idColaboracion != -1) {
                boolean agregarEstudiantes = Alertas.mostrarMensajeConfirmacion("¿Quiere agregar un estudiante a la colaboración?");
                if (agregarEstudiantes) {
                    Stage escenario = (Stage) button_Iniciar.getScene().getWindow();
                    SDGCOILVIC sdgcoilvic = new SDGCOILVIC();
                    try {          
                        sdgcoilvic.mostrarVentanaAgregarEstudiante(escenario, () -> {
                            Alertas.mostrarMensajeExitoInicioColanboracion();
                            abrirVentanaAdministrarPropuestas(escenario);
                    });
                    } catch (IOException ex) {
                        Alertas.mostrarMensajeErrorCambioPantalla();
                        LOG.error(ex);
                    }
                } else {
                    Alertas.mostrarMensajeExitoInicioColanboracion();
                    Stage escenario = (Stage) button_Iniciar.getScene().getWindow();
                    abrirVentanaAdministrarPropuestas(escenario);
                }
            } else {
                Alertas.mostrarMensajeInformacionNoRegistrada();
            }
        }
    }

    private void abrirVentanaAdministrarPropuestas(Stage escenario) {
        SDGCOILVIC sdgcoilvic = new SDGCOILVIC();
        try {
            sdgcoilvic.mostrarVentanaAdministrarPropuestasDeColaboracion(escenario);
        } catch (IOException ex) {
            LOG.error(ex);
        }
    }
    
    private Colaboracion crearColaboracion() {
        Colaboracion colaboracion = new Colaboracion(); 
        colaboracion.setIdPropuestaColaboracion(idPropuestaColaboracion);
        colaboracion.setDescripcion(txtArea_Descripcion.getText());
        colaboracion.setRecursos(txtArea_Recursos.getText());
        colaboracion.setAprendizajesEsperados(txtArea_Aprendizajes.getText());
        colaboracion.setDetallesAsistenciaParticipacion(txtArea_Evaluacion.getText());
        colaboracion.setDetallesEvaluacion(txtArea_Asistencia.getText());
        colaboracion.setDetallesEntorno(txtArea_Entorno.getText());


        return colaboracion;
    }
   
    private int registraColaboracion(Colaboracion colaboracion) {
        ColaboracionDAO colaboracionDAO = new ColaboracionDAO();
        int idColaboracion = -1;

        int idProfesor = accesoSingleton.getAccesoId();
        if (colaboracion != null) {
            try {
                idColaboracion = colaboracionDAO.crearColaboracion(colaboracion, idProfesor);
                colaboracionDAO.vincularProfesoresALaColaboracion(idColaboracion);
                colaboracionDAO.cambiarEstadoProfesorAColaborando(idColaboracion);
                colaboracionDAO.rechazarTodasLasSolicitudes(idPropuestaColaboracion);
                if(idColaboracion >= 1){
                    ColaboracionEnCursoSinglenton.getInstance().setIdColaboracionEnCurso(idColaboracion);
                }
            } catch (SQLException sqlException) {
                Alertas.mostrarMensajeErrorBaseDatos();
                LOG.fatal("Error en la base de datos en la clase " + this.getClass().getName() + ", método " + Thread.currentThread().getStackTrace()[1].getMethodName() + ": " + sqlException.getMessage(), sqlException);
            }
        }

        return idColaboracion;
    }
        
    private boolean estaVacio() {

        return  txtArea_Descripcion.getText().isEmpty() ||
                txtArea_Recursos.getText().isEmpty() ||
                txtArea_Aprendizajes.getText().isEmpty() ||
                txtArea_Evaluacion.getText().isEmpty() ||
                txtArea_Asistencia.getText().isEmpty() ||
                txtArea_Entorno.getText().isEmpty();
    }

    private boolean verificarInformacion() {
        Colaboracion colaboracion = new Colaboracion();
        boolean validacion = true;

        if (!estaVacio()) {
            try {
                colaboracion.setDescripcion(txtArea_Descripcion.getText());
                colaboracion.setRecursos(txtArea_Recursos.getText());
                colaboracion.setAprendizajesEsperados(txtArea_Aprendizajes.getText());
                colaboracion.setDetallesAsistenciaParticipacion(txtArea_Evaluacion.getText());
                colaboracion.setDetallesEvaluacion(txtArea_Asistencia.getText());
                colaboracion.setDetallesEntorno(txtArea_Entorno.getText());
            } catch (IllegalArgumentException illegalArgument) {
                Alertas.mostrarMensajeInformacionInvalida();
                validacion = false;
            }
        } else {
            Alertas.mostrarMensajeCamposVacios();
            validacion = false;
        }
        return validacion;

    } 
}