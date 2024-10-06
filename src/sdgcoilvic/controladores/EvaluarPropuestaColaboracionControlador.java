package sdgcoilvic.controladores;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import sdgcoilvic.logicaDeNegocio.clases.Profesor;
import sdgcoilvic.logicaDeNegocio.clases.PropuestaColaboracion;
import sdgcoilvic.logicaDeNegocio.implementacionDAO.PeriodoDAO;
import sdgcoilvic.logicaDeNegocio.implementacionDAO.ProfesorDAO;
import sdgcoilvic.logicaDeNegocio.implementacionDAO.PropuestaColaboracionDAO;
import sdgcoilvic.utilidades.Alertas;
import sdgcoilvic.utilidades.EnviosDeCorreoElectronico;

public class EvaluarPropuestaColaboracionControlador implements Initializable{
    private static final Logger LOG = Logger.getLogger(EvaluarPropuestaColaboracionControlador.class);
    private Stage escenario;
    public static int idPropuestaColaboracion;
    
    @FXML private Button button_Cancelar;
    @FXML private Button button_Enviar;
    @FXML private TextArea txtArea_Descripcion;
    @FXML private TextArea txtArea_Objetivo;
    @FXML private TextArea txtArea_Justificacion;
    @FXML private TextField textField_Periodo;
    @FXML private TextField textField_Idioma;
    @FXML private TextField textField_Modalidad;
    @FXML private TextField textField_TituloPrincipal;
    @FXML private TextField textField_Profesor;
    @FXML private TextField textField_Institutcion;
    @FXML private ComboBox<String> comboBox_Evaluacion;

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
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            llenarComboBoxEvaluacon();
            aplicarValidacion(txtArea_Justificacion, "^[\\p{L}áéíóúÁÉÍÓÚüÜ\\s',;\\-_:\\.]{1,500}$");
            llenarInformacionProfesor();
       } catch (Exception ex) {
          LOG.fatal(ex);
           Alertas.mostrarMensajeErrorBaseDatos();
           Stage escenario = (Stage) button_Cancelar.getScene().getWindow(); 
           escenario.close(); 
       }
    }
    
    private void llenarComboBoxEvaluacon() {
        ObservableList<String> items = FXCollections.observableArrayList("Aceptada", "Rechazada");
        comboBox_Evaluacion.setItems(items);
    }
    
    @FXML
    void button_Cancelar(ActionEvent event) {
        if (Alertas.mostrarMensajeCancelar()) {
            Stage escenario = (Stage) button_Cancelar.getScene().getWindow();
            SDGCOILVIC sdgcoilvic = new SDGCOILVIC();

            try {
                sdgcoilvic.mostrarVentanaGestionDePropuestasColaboracion(escenario);
            } catch (IOException ex) {
                LOG.error( ex);
            }
        }
    }

    @FXML
    void button_Enviar(ActionEvent event) {
        PropuestaColaboracionDAO propuestaColaboracionDAO = new PropuestaColaboracionDAO();
        String correo;
        try {    
            correo = propuestaColaboracionDAO.obtenerCorreoPorIdPropuesta(idPropuestaColaboracion);
            if (!verificarInformacion()) {
                if (propuestaColaboracionDAO.evaluarPropuestaColaboracion(idPropuestaColaboracion, comboBox_Evaluacion.getValue())== 1) {
                        if (enviarCorreo(textField_Profesor.getText(),txtArea_Justificacion.getText(), correo, comboBox_Evaluacion.getValue()) == false) {
                            Alertas.mostrarMensajeElCorreoNoSePudoEnviar();
                            propuestaColaboracionDAO.reevertirEstado(idPropuestaColaboracion);
                            return;
                        }else {
                            Alertas.mostrarMensajeEvaluacionPropuestaExito();
                             Stage escenario = (Stage) button_Cancelar.getScene().getWindow();
                            SDGCOILVIC sdgcoilvic = new SDGCOILVIC();
                            try {
                                sdgcoilvic.mostrarVentanaGestionDePropuestasColaboracion(escenario);
                            } catch (IOException ex) {
                                LOG.error( ex);
                            }
                        }
                }else {
                    Alertas.mostrarMensajeInformacionNoRegistrada();
                }    
            } else {
                Alertas.mostrarMensajeCamposVaciosEvaluacion();
            }
        } catch (SQLException sqlException) {
                Alertas.mostrarMensajeErrorBaseDatos();
                LOG.fatal("Error en la base de datos en la clase " + this.getClass().getName() + ", método " + Thread.currentThread().getStackTrace()[1].getMethodName() + ": " + sqlException.getMessage(), sqlException);
        }
    }
   
    private boolean enviarCorreo(String profesor, String justificacion, String correo,String avaluacion) {
        String mensaje = "Estimado profesor " + profesor + ",\n\n" +
                "Queremos informarte que tu propuesta de colaboración ha sido " + avaluacion + ".\n\n" +
                "Justificación: " + justificacion + "\n\n" +
                "Para más detalles, no dudes en contactarnos.\n\n" +
                "¡Gracias por tu interés en colaborar con nosotros!\n\n" +
                "Atentamente,\n" +
                "Equipo de SDGCOILVIC";

        return EnviosDeCorreoElectronico.verificarEnvioCorreo(correo, "Estado de tu propuesta de colaboración", mensaje);
    }
    
    private boolean verificarInformacion() {
        int indiceEvaluacion = comboBox_Evaluacion.getSelectionModel().getSelectedIndex();

        return  txtArea_Justificacion.getText().isEmpty() ||
                indiceEvaluacion < 0;
    }
    
    private void llenarInformacionProfesor() {
        try {
            PropuestaColaboracion propuestaColaboracion = new PropuestaColaboracion();
            Profesor profesor = new Profesor();
            PropuestaColaboracionDAO propuestaColaboracionDAO = new PropuestaColaboracionDAO();
            
            propuestaColaboracion = propuestaColaboracionDAO.obtenerPropuestasColaboracionPorIdPropuesta(idPropuestaColaboracion);
            textField_TituloPrincipal.setText(propuestaColaboracion.getNombre());
            txtArea_Descripcion.setText(propuestaColaboracion.getInformacionAdicional());
            txtArea_Objetivo.setText(propuestaColaboracion.getObjetivoGeneral());
            asignarNombrePeriodo(propuestaColaboracion);
            asignarNombreIdioma(propuestaColaboracion);
            textField_Modalidad.setText(propuestaColaboracion.getTipoColaboracion());
            profesor = propuestaColaboracionDAO.obtenerProfesorPorid(propuestaColaboracion.getIdProfesor());
            textField_Profesor.setText(profesor.getNombre());
            textField_Institutcion.setText(profesor.getClaveInstitucional());
        } catch (SQLException ex) {
            LOG.error(ex);
        } 
    }
    
    private List<List<String>> obtenerListaDeIdiomas() throws SQLException {
        return new ProfesorDAO().obtenerListaDeIdiomas();
    }

    private List<List<String>> obtenerListaDePeriodo() throws SQLException {
        return new PeriodoDAO().obtenerListaDePeriodos();
    }
    private void asignarNombrePeriodo(PropuestaColaboracion propuestaColaboracion) {
        try {
            int idPeriodo = propuestaColaboracion.getIdPeriodo();
            List<List<String>> listaDePeriodos = obtenerListaDePeriodo();
            for (List<String> periodo : listaDePeriodos) {
                if (Integer.parseInt(periodo.get(0)) == idPeriodo) {
                    String nombre = periodo.get(1);
                    String fechaInicio = periodo.get(2);
                    String fechaFin = periodo.get(3);
                    String nombreYFechas = nombre + " - " + fechaInicio + " a " + fechaFin;
                     textField_Periodo.setText(nombreYFechas);
                    return;
                }
            }
        } catch (SQLException sqlException) {
            LOG.error(sqlException);
        }
    }


    private void asignarNombreIdioma(PropuestaColaboracion propuestaColaboracion) {
        try {
            int idIdioma = propuestaColaboracion.getIdIdiomas();
            List<List<String>> listaDeIdiomas = obtenerListaDeIdiomas();
            for (List<String> idioma : listaDeIdiomas) {
                if (Integer.parseInt(idioma.get(0)) == idIdioma) {
                    textField_Idioma.setText(idioma.get(1));
                    return;
                }
            }
        } catch (SQLException sqlException) {
            LOG.error(sqlException);
        }
    } 
}