package sdgcoilvic.controladores;
import sdgcoilvic.utilidades.ImagesSetter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import sdgcoilvic.utilidades.Alertas;
import sdgcoilvic.utilidades.AccesoSingleton;

public class AdministrativoMenuControlador implements Initializable {
    private static final Logger LOG = Logger.getLogger(AdministrativoMenuControlador.class);

    @FXML
    private ImageView imageView_Salir;
    @FXML
    private ImageView imageView_Colaboraciones;
    @FXML
    private ImageView imageView_PropuestasColaboracion;
    @FXML
    private ImageView imageView_Profesores;
    @FXML
    private ImageView imageView_Instituciones;
    @FXML
    private ImageView imageView_OfertarColaboracion;
    
    @FXML
    private ImageView imageView_MenuAdministrativo;
    
    private void mostrarImagen() {
 
        imageView_MenuAdministrativo.setImage(ImagesSetter.getImageMenuAdministrativo());
    }
   
    @FXML
    private void cerrarSesion(MouseEvent event) {
        if (Alertas.mostrarConfirmacion("Cerrar Sesión", "¿Seguro que desea cerrar sesión?")) {
            AccesoSingleton.getInstance().borrarInstancia();
            Stage escenario = (Stage) imageView_Salir.getScene().getWindow();
            SDGCOILVIC sdgcoilvic = new SDGCOILVIC();

            try {
                sdgcoilvic.mostrarVentanaAcceso(escenario);
            } catch (IOException ex) {
                LOG.error( ex);
            }
        }
    }
    
    
    @FXML
    private void abrirVentanaPropuestasColaboracion(MouseEvent event) {
        Stage escenario = (Stage) imageView_PropuestasColaboracion.getScene().getWindow();
        SDGCOILVIC sdgcoilvic = new SDGCOILVIC();

        try {
            sdgcoilvic.mostrarVentanaGestionDePropuestasColaboracion(escenario);
        } catch (IOException ex) {
            LOG.error( ex);
        }
    }
    
    @FXML
    private void abrirVentanaColaboraciones(MouseEvent event) {
        Stage escenario = (Stage) imageView_Colaboraciones.getScene().getWindow();
        SDGCOILVIC sdgcoilvic = new SDGCOILVIC();

        try {
            sdgcoilvic.mostrarVentanaGestionDeColaboraciones(escenario);
        } catch (IOException ex) {
            LOG.error( ex);
        }
    }
    
    @FXML
    private void abrirVentanaOfertarColaboracion(MouseEvent event) {
        Stage escenario = (Stage) imageView_OfertarColaboracion.getScene().getWindow();
        SDGCOILVIC sdgcoilvic = new SDGCOILVIC();

        try {
            sdgcoilvic.mostrarVentanaGestionDeOfertasDeColaboracion(escenario);
        } catch (IOException ex) {
            LOG.error( ex);
        }
        
    }
    
    @FXML
    private void abrirVentanaProfesores(MouseEvent event) {
        Stage escenario = (Stage) imageView_Profesores.getScene().getWindow();
        SDGCOILVIC sdgcoilvic = new SDGCOILVIC();

        try {
            sdgcoilvic.mostrarVentanaGestionDeProfesores(escenario);
        } catch (IOException ex) {
            LOG.error( ex);
        }
    }
    
    @FXML
    private void abrirVentanaInstituciones(MouseEvent event) {
        Stage escenario = (Stage) imageView_Instituciones.getScene().getWindow();
        SDGCOILVIC sdgcoilvic = new SDGCOILVIC();
        try {
            sdgcoilvic.mostrarVentanaGestionDeInstituciones(escenario);
        } catch (IOException ex) {
            LOG.error( ex);
        }
    }
    
    public void establecerMensajesImagen() {
        Tooltip tooltipCerrarSesion = new Tooltip("Cerrar Sesión");
        Tooltip tooltipColaboraciones = new Tooltip("Colaboraciones");
        Tooltip tooltipPropuestasColaboracion = new Tooltip("Propuesta de Colaboracion");
        Tooltip tooltipProfesores = new Tooltip("Profesores");
        Tooltip tooltipInstituciones = new Tooltip("Instituciones");
        Tooltip.install(imageView_Salir, tooltipCerrarSesion);
        Tooltip.install(imageView_Colaboraciones, tooltipColaboraciones);
        Tooltip.install(imageView_PropuestasColaboracion, tooltipPropuestasColaboracion);
        Tooltip.install(imageView_Profesores, tooltipProfesores);
        Tooltip.install(imageView_Instituciones, tooltipInstituciones);
        
    }
    
    public void ocultarOpciones() {
        imageView_Salir.setVisible(true);
        imageView_Colaboraciones.setVisible(true);
        imageView_PropuestasColaboracion.setVisible(true);
        imageView_Profesores.setVisible(true);
        imageView_Instituciones.setVisible(true);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        establecerMensajesImagen();
        mostrarImagen();
        ocultarOpciones();
    }
}