package sdgcoilvic.controladores;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import sdgcoilvic.logicaDeNegocio.clases.PropuestaColaboracion;
import sdgcoilvic.logicaDeNegocio.implementacionDAO.ActividadColaborativaDAO;
import sdgcoilvic.logicaDeNegocio.implementacionDAO.ColaboracionDAO;
import sdgcoilvic.utilidades.Alertas;
import sdgcoilvic.utilidades.ColaboracionEnCursoSinglenton;
import sdgcoilvic.utilidades.ImagesSetter;

public class AdministrarColaboracionActivaControlador implements Initializable{
    private static final Logger LOG = Logger.getLogger(AdministrarColaboracionActivaControlador.class);
    private ColaboracionEnCursoSinglenton colaboracionEnCursoSinglenton;
    @FXML private ImageView imageView_SubMenu;

    @FXML private Button button_Regresar;
    @FXML private Button button_IniciarActividad;
    @FXML private Button button_CerrarColaboracion;
    @FXML private Button button_AgregarEstudiante;
    @FXML private TextField textField_NombreColaboracion;
    @FXML private TextField textField_Profesor1;
    @FXML private TextField textField_Profesor2;
    @FXML private TextField textField_Profesor3;
    @FXML private TextField textField_Profesor4;
    @FXML private TextField textField_Objetivo;
    @FXML private TextField textField_TipoColaboracion;
    
    private void mostrarImagen() {
        imageView_SubMenu.setImage(ImagesSetter.getImageSubMenu());
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mostrarImagen();
        colaboracionEnCursoSinglenton = ColaboracionEnCursoSinglenton.getInstance();
        llenarCampos();
    }
    
    private void llenarCampos() {
        int idColaboracionEnCurso = colaboracionEnCursoSinglenton.getIdColaboracionEnCurso();
        PropuestaColaboracion propuestaColaboracion = new PropuestaColaboracion();
        ColaboracionDAO colaboracionDAO = new ColaboracionDAO();
        try {
           List<String> listaNombreProfesores = colaboracionDAO.obtenerNombreProfesores(idColaboracionEnCurso); 
           propuestaColaboracion = colaboracionDAO.obtenerPropuestaColaboracion(idColaboracionEnCurso);
           textField_NombreColaboracion.setText(propuestaColaboracion.getNombre());
           textField_Objetivo.setText(propuestaColaboracion.getObjetivoGeneral());
           textField_TipoColaboracion.setText(propuestaColaboracion.getTipoColaboracion());

           List<TextField> camposProfesores = Arrays.asList(textField_Profesor1, textField_Profesor2, textField_Profesor3, textField_Profesor4);
           for (int i = 0; i < listaNombreProfesores.size(); i++) {
               if (i < camposProfesores.size()) {
                   camposProfesores.get(i).setText(listaNombreProfesores.get(i));
               }
           }
        } catch (SQLException sQLExcpetion) {
            Alertas.mostrarMensajeErrorBaseDatos();
        }
    }
    
    @FXML
    private void button_AgregarEstudiante(ActionEvent event) {
        Stage escenario = (Stage) button_AgregarEstudiante.getScene().getWindow();
        SDGCOILVIC sdgcoilvic = new SDGCOILVIC();
        Runnable onCloseCallback = () -> {
  
        };
        try {
            sdgcoilvic.mostrarVentanaAgregarEstudiante(escenario, onCloseCallback);
        } catch (IOException ex) {
            LOG.error(ex);
        }
    }

    
    @FXML
    private void button_IniciarActividad(ActionEvent event) {
        Stage escenario = (Stage) button_IniciarActividad.getScene().getWindow();
        SDGCOILVIC sdgcoilvic = new SDGCOILVIC();
        Runnable onCloseCallback = () -> {
            Alertas.mostrarMensajeExito();
            
        };
        try {
            sdgcoilvic.mostrarVentanaIniciarActividad(escenario, onCloseCallback);
        } catch (IOException ex) {
            LOG.error( ex);
        }
    }
    
    @FXML
    private void button_CerrarColaboracion(ActionEvent event) {
        ActividadColaborativaDAO actividadColaborativaDAO = new ActividadColaborativaDAO();
        ColaboracionDAO colaboracionDAO = new ColaboracionDAO();
        int idColaboracionEnCurso = colaboracionEnCursoSinglenton.getIdColaboracionEnCurso();
            
        try {
            boolean verificarActividades = actividadColaborativaDAO.verificarActividadesFinalizadas(idColaboracionEnCurso);
            if (verificarActividades == true) {
                boolean cerrarColaboracion = Alertas.mostrarMensajeConfirmacionCerrarColaboracion();
                if (cerrarColaboracion) {
                    colaboracionDAO.cerrarColaboracion(idColaboracionEnCurso);
                    
                    ColaboracionEnCursoSinglenton.getInstance().destruirColaboracionEnCursoSinglenton();
                    
                    Stage escenario = (Stage) button_CerrarColaboracion.getScene().getWindow();
                    SDGCOILVIC sdgcoilvic = new SDGCOILVIC();
                    try {
                        sdgcoilvic.mostrarVentanaProfesorMenu(escenario);
                        Alertas.mostrarMensajeExitoIColaboracionCerrada();
                    } catch (IOException ex) {
                        Alertas.mostrarMensajeErrorCambioPantalla();
                        LOG.error(ex);
                    }
                } 
            }else{
               Alertas.mostrarMensajeColaboracionSinActividades(); 
            }
        } catch (SQLException ex) {
            Alertas.mostrarMensajeErrorBaseDatos();
            LOG.error(ex);
        }

    }
    
    @FXML
    private void button_Regresar(ActionEvent event) {
        Stage escenario = (Stage) button_Regresar.getScene().getWindow();
        SDGCOILVIC sdgcoilvic = new SDGCOILVIC();

        try {
            sdgcoilvic.mostrarVentanaProfesorMenu(escenario);
        } catch (IOException ex) {
            LOG.error( ex);
        }
    }
}