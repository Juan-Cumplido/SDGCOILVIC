package sdgcoilvic.controladores;
import sdgcoilvic.utilidades.ImagesSetter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import sdgcoilvic.logicaDeNegocio.clases.Profesor;
import sdgcoilvic.logicaDeNegocio.enums.EnumProfesor;
import sdgcoilvic.logicaDeNegocio.implementacionDAO.AccesoDAO;
import sdgcoilvic.logicaDeNegocio.implementacionDAO.ColaboracionDAO;
import sdgcoilvic.utilidades.Alertas;
import sdgcoilvic.utilidades.AccesoSingleton;
import sdgcoilvic.utilidades.ColaboracionEnCursoSinglenton;

public class AccesoControlador implements Initializable {
    private static final Logger LOG = Logger.getLogger(AccesoControlador.class);
    @FXML
    private TextField textField_Usuario;
    @FXML
    private Button button_Acceso;
    @FXML
    private ImageView imageAccesoFondo;
    @FXML
    private PasswordField paswordField_Contrasenia;

    private void mostrarImagen() {
        imageAccesoFondo.setImage(ImagesSetter.getImageAccesoFondo());
    }

    private boolean estaVacio() {
        return textField_Usuario.getText().isEmpty() || paswordField_Contrasenia.getText().isEmpty();
    }

    private int verificarAcceso() {
        if (!estaVacio()) {
            AccesoDAO accesoDao = new AccesoDAO();

            try {
                int existeAcceso = accesoDao.verificarExistenciaAcceso(textField_Usuario.getText(), paswordField_Contrasenia.getText());
                if (existeAcceso > 0) {
                    return existeAcceso;
                } else {
                    Alertas.mostrarMensajeInicioSesionFallido();
                }
            } catch (SQLException sqlException) {
                Alertas.mostrarMensajeErrorBaseDatos();
                LOG.fatal("No hay conexión con la base de datos :" +this.getClass().getName() + ", método " + Thread.currentThread().getStackTrace()[1].getMethodName() + ": " + sqlException.getMessage(), sqlException);
            }
        } else {
            Alertas.mostrarMensajeCamposVacios();
        }
        return 0;
    }
    
    private void abrirVentanaAdministrativoMenu() {
        Stage escenario = (Stage) button_Acceso.getScene().getWindow();
        SDGCOILVIC sdgcoilvic = new SDGCOILVIC();
        try {
            sdgcoilvic.mostrarVentanaAdministrativoMenu(escenario);
        } catch (IOException | NullPointerException ex) {
            LOG.error("Error al abrir la ventana administrativa: " + ex.getMessage());
            Alertas.mostrarMensajeErrorCambioPantalla();
        }
    }

    private void abrirVentanaProfesorMenu() {
        Stage ventana = (Stage) button_Acceso.getScene().getWindow();
        SDGCOILVIC sdgcoilvic = new SDGCOILVIC();
        try {
            sdgcoilvic.mostrarVentanaProfesorMenu(ventana);
        }catch (IOException | NullPointerException ex) {
            LOG.error("Error al abrir la ventana de profesor: " + ex.getMessage());
            Alertas.mostrarMensajeErrorCambioPantalla();
        }
    }
    
    @FXML
    private void button_Acceso(ActionEvent event) {
        Node fuente = (Node) event.getSource();
        Stage escenario = (Stage) fuente.getScene().getWindow();

        if (verificarAcceso() != 0) {
            AccesoDAO accesoDAO = new AccesoDAO();
            int IdAcceso = accesoDAO.obtenerIdProfesor(textField_Usuario.getText(),paswordField_Contrasenia.getText());
            AccesoSingleton.getInstance().setAccesoId(IdAcceso);
            try {
                accesoDAO.ejecutarActualizacionBaseDatos();
                String tipoUsuario=accesoDAO.obtenerTipoUsuario(textField_Usuario.getText(), paswordField_Contrasenia.getText());
                switch (tipoUsuario) {
                    case "Administrativo" -> abrirVentanaAdministrativoMenu();
                    case "Profesor" -> validarEstadoProfesor(IdAcceso);
                }
            } catch (SQLException sqlException) {
                Alertas.mostrarMensajeErrorBaseDatos();
                LOG.fatal("No hay conexión con la base de datos :" +this.getClass().getName() + ", método " + Thread.currentThread().getStackTrace()[1].getMethodName() + ": " + sqlException.getMessage(), sqlException);
            }
        }
    }
    
    private void validarEstadoProfesor(int idAcceso){
        AccesoDAO accesoDAO = new AccesoDAO();
        ColaboracionDAO colaboracionDAO = new ColaboracionDAO();
        int idColaboracionEnCurso = -1;
        try {
            Profesor profesor = accesoDAO.obtenerProfesorPorID(idAcceso);
            if(profesor != null && profesor.getEstadoProfesor().equals(EnumProfesor.Archivado.toString())){
                Alertas.mostrarMensajeAccesoDenegado();
            }else{
                idColaboracionEnCurso = colaboracionDAO.obtenerIdColaboracionEnCurso(idAcceso);
                if(idColaboracionEnCurso >= 1){
                    ColaboracionEnCursoSinglenton.getInstance().setIdColaboracionEnCurso(idColaboracionEnCurso);
                }else{
                    ColaboracionEnCursoSinglenton.getInstance().setIdColaboracionEnCurso(-1);
                }
                abrirVentanaProfesorMenu();
            }
        }catch (SQLException sqlException ) {
            Alertas.mostrarMensajeErrorBaseDatos();
            LOG.fatal("No hay conexión con la base de datos :" +this.getClass().getName() + ", método " + Thread.currentThread().getStackTrace()[1].getMethodName() + ": " + sqlException.getMessage(), sqlException);
            
        } catch (Exception e) {
            LOG.error(e);
    }
    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mostrarImagen();
    }
}