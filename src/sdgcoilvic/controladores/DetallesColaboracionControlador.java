package sdgcoilvic.controladores;
import com.itextpdf.text.Document;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import sdgcoilvic.logicaDeNegocio.clases.ActividadColaborativa;
import sdgcoilvic.logicaDeNegocio.clases.Colaboracion;
import sdgcoilvic.logicaDeNegocio.clases.PropuestaColaboracion;
import sdgcoilvic.logicaDeNegocio.enums.EnumColaboracion;
import sdgcoilvic.logicaDeNegocio.implementacionDAO.ActividadColaborativaDAO;
import sdgcoilvic.logicaDeNegocio.implementacionDAO.ColaboracionDAO;
import sdgcoilvic.utilidades.Alertas;
import sdgcoilvic.utilidades.ImagesSetter;
import sdgcoilvic.utilidades.InformeImplementacion;


public class DetallesColaboracionControlador implements Initializable{
    private static final Logger LOG = Logger.getLogger(DetallesColaboracionControlador.class);
    private Stage escenario;
    public static int idColaboracion;

    @FXML private ImageView imageView_SubMenu;
    @FXML private Button button_Regresar;
    @FXML private Button button_GenerarInforme;
    @FXML private Button button_VerAvances;
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
        llenarCampos();
        verificarEstadoPropuesta();
    }
    
    private void verificarEstadoPropuesta() {
        Colaboracion colaboracion = new Colaboracion();
        ColaboracionDAO colaboracionDAO = new ColaboracionDAO();
        colaboracion = colaboracionDAO.consultarColaboracion(idColaboracion);
        if (colaboracion.getEstadoColaboracion().equals(EnumColaboracion.Finalizada.toString())) {
            button_GenerarInforme.setVisible(true);
        } else {
            button_GenerarInforme.setVisible(false);
        }
    }
    
    private void llenarCampos() {
        PropuestaColaboracion propuestaColaboracion = new PropuestaColaboracion();
        ColaboracionDAO colaboracionDAO = new ColaboracionDAO();
        try {
           List<String> listaNombreProfesores = colaboracionDAO.obtenerNombreProfesores(idColaboracion); 
           propuestaColaboracion = colaboracionDAO.obtenerPropuestaColaboracion(idColaboracion);
           textField_NombreColaboracion.setText(propuestaColaboracion.getNombre());
           textField_Objetivo.setText(propuestaColaboracion.getObjetivoGeneral());
           textField_TipoColaboracion.setText(propuestaColaboracion.getTipoColaboracion());

           List<TextField> camposProfesores = Arrays.asList(textField_Profesor1, textField_Profesor2, textField_Profesor3, textField_Profesor4);
           for (int i = 0; i < listaNombreProfesores.size(); i++) {
               if (i < camposProfesores.size()) {
                   camposProfesores.get(i).setText(listaNombreProfesores.get(i));
               }
           }
        } catch (SQLException sqlExcpetion) {
            LOG.error(sqlExcpetion);
            Alertas.mostrarMensajeErrorBaseDatos();
        }
    }
    
    public List<String> obtenerProfesoresColaboracion(){
        List<String> profesoresObtenidos = new ArrayList<>();
        ColaboracionDAO colaboracionDAO = new ColaboracionDAO();
        try {
            profesoresObtenidos = colaboracionDAO.obtenerNombreProfesores(idColaboracion);
        } catch (SQLException ex) {
            LOG.error(ex);
        }
        return profesoresObtenidos;
    }
    
    public List<ActividadColaborativa> obtenerActividadesColaboracion(){
        List<ActividadColaborativa> actividadesObtenidas = new ArrayList<>();  
        try {
            ActividadColaborativaDAO actividadColaborativaDAO = new ActividadColaborativaDAO();
            actividadesObtenidas = actividadColaborativaDAO.obtenerActividadesColaborativas(idColaboracion);
            return actividadesObtenidas;
        } catch (SQLException ex) {
            LOG.error(ex);
        }
        return actividadesObtenidas;
    }
    
    @FXML
    private void button_GenerarInforme(ActionEvent event) {
        Document informeGenerado = obtenerInformeDeColaboracion();
        if (Objects.nonNull(informeGenerado)) {
            boolean resultadoAlerta = Alertas.mostrarMensajeDescargaDeArchivo();
            if (resultadoAlerta) {
                guardarInforme(informeGenerado);
            }
        } else {
            Alertas.mostrarErrorEnLaCreacionDeInforme();
        }
    }

    
    public Document obtenerInformeDeColaboracion() {
        Document informeGenerado = null;
        ColaboracionDAO colaboracionDAO = new ColaboracionDAO();
        PropuestaColaboracion propuestaColaboracion = new PropuestaColaboracion();
        PropuestaColaboracion propuestaColaboracionActual = new PropuestaColaboracion(); 

        try {
            propuestaColaboracion = colaboracionDAO.obtenerPropuestaColaboracion(idColaboracion);
        } catch (SQLException ex) {
            Alertas.mostrarMensajeErrorBaseDatos();
            LOG.error(ex);
            return null;
        }

        try {
            propuestaColaboracionActual.setNombre(propuestaColaboracion.getNombre());
            propuestaColaboracionActual.setTipoColaboracion(propuestaColaboracion.getTipoColaboracion());
            propuestaColaboracionActual.setObjetivoGeneral(propuestaColaboracion.getObjetivoGeneral());
        } catch (IllegalArgumentException ex) {
            LOG.error("Datos inválidos en la propuesta de colaboración", ex);
            Alertas.mostrarMensajeInformacionInvalida(); 
            return null;
        }

        List<ActividadColaborativa> actividades = obtenerActividadesColaboracion();
        List<String> profesores = obtenerProfesoresColaboracion();
        InformeImplementacion creacionDeInforme = new InformeImplementacion();
        informeGenerado = creacionDeInforme.crearInformeDeColaboracion(idColaboracion, propuestaColaboracionActual, actividades, profesores);

        return informeGenerado;
    }

    
    public void guardarInforme(Document informeAGuardar){
        FileChooser escogerRutaDeGuardado = new FileChooser();
        escogerRutaDeGuardado.setTitle("Seleccione el lugar donde desea guardar el informe");
        escogerRutaDeGuardado.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos PDF", "*.pdf"));
        escogerRutaDeGuardado.setInitialDirectory(new File(System.getProperty("user.home")));
        File archivoAGuardar = escogerRutaDeGuardado.showSaveDialog(escenario);
        InformeImplementacion guardadoImplementacion = new InformeImplementacion();
        int resultadoGuardado = guardadoImplementacion.guardarArchivoDeInforme(archivoAGuardar, informeAGuardar,idColaboracion);
        if(resultadoGuardado==1){
            Alertas.mostrarMensajeInformeGuardadoExitosamente();
        }else{
            Alertas.mostrarMensajeErrorAlGuardarInforme();
        }
    } 

    
    @FXML
    private void button_VerAvances(ActionEvent event) {
        Stage escenario = (Stage) button_VerAvances.getScene().getWindow();
        SDGCOILVIC sdgcoilvic = new SDGCOILVIC();
        try {

            AvanceDeColaboracionControlador.idColaboracion = idColaboracion;
            sdgcoilvic.mostrarVentanaAvanceDeColaboracion(escenario);
            } catch (IOException ioexception) {
                LOG.error(ioexception.getMessage());
                Alertas.mostrarMensajeErrorCambioPantalla();
        }
    }
    
    @FXML
    private void button_Regresar(ActionEvent event) {
        Stage escenario = (Stage) button_Regresar.getScene().getWindow();
        SDGCOILVIC sdgcoilvic = new SDGCOILVIC();

        try {
            sdgcoilvic.mostrarVentanaGestionDeColaboraciones(escenario);
        } catch (IOException ex) {
            LOG.error( ex);
        }
    }
 
}