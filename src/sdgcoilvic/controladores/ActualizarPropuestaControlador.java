package sdgcoilvic.controladores;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
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
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import sdgcoilvic.logicaDeNegocio.clases.PropuestaColaboracion;
import sdgcoilvic.logicaDeNegocio.enums.EnumPropuesta;
import sdgcoilvic.logicaDeNegocio.implementacionDAO.PeriodoDAO;
import sdgcoilvic.logicaDeNegocio.implementacionDAO.ProfesorDAO;
import sdgcoilvic.logicaDeNegocio.implementacionDAO.PropuestaColaboracionDAO;
import sdgcoilvic.utilidades.Alertas;
import sdgcoilvic.utilidades.ImagesSetter;

public class ActualizarPropuestaControlador implements Initializable {
    private static final Logger LOG= Logger.getLogger(NuevaPropuestaControlador.class);
    private Stage escenario;
    public static  int idPropuestaColaboracion;
    
    @FXML private Button button_Cancelar;
    @FXML private Button button_Someter;
    @FXML private TextArea txtArea_Informacion;
    @FXML private TextArea txtArea_Temas;
    @FXML private TextArea txtArea_Objetivo;
    @FXML private TextField textField_NoEstudiante;
    @FXML private TextField textField_Nombre;
    @FXML private TextField textField_PerfilEstudiante;
    @FXML private ComboBox<String> comboBox_Periodo;
    @FXML private ComboBox<String> comboBox_Idioma;
    @FXML private ComboBox<String> comboBox_TipoColaboracion; 
    @FXML private ImageView imageView_SubMenu;

    private void mostrarImagen() {
        imageView_SubMenu.setImage(ImagesSetter.getImageSubMenu());
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
    
    public void setStage(Stage escenario) {
        this.escenario = escenario;
    }    
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            llenarComboBoxIdioma();
            llenarComboBoxPerdiodo();
            llenarComboBoxTipoColaboracion();
            aplicarValidacion(textField_Nombre,  "^[\\p{L}áéíóúÁÉÍÓÚüÜ\\s',;:\\-_.0-9]{1,500}$");
            aplicarValidacion(textField_NoEstudiante, "^(?!0$)\\d{1,4}$");
            aplicarValidacion(textField_PerfilEstudiante, "^[\\p{L}áéíóúÁÉÍÓÚüÜ\\s',;\\-_:\\.]{1,45}$");
            aplicarValidacion(txtArea_Temas, "^[\\p{L}áéíóúÁÉÍÓÚüÜ\\s',;:\\-_.0-9]{1,500}$");
            aplicarValidacion(txtArea_Informacion, "^[\\p{L}áéíóúÁÉÍÓÚüÜ\\s',;:\\-_.0-9]{1,500}$");
            aplicarValidacion(txtArea_Objetivo, "^[\\p{L}áéíóúÁÉÍÓÚüÜ\\s',;:\\-_.0-9]{1,500}$");
            mostrarImagen();
            llenarInformacionProfesor();
       } catch (Exception ex) {
          LOG.fatal(ex);
           Alertas.mostrarMensajeErrorBaseDatos();
           Stage escenario = (Stage) button_Cancelar.getScene().getWindow(); 
           escenario.close(); 
       }
    }
    
    private void llenarComboBoxTipoColaboracion() {
        ObservableList<String> items = FXCollections.observableArrayList("COIL-VIC", "ESPEJO");
        comboBox_TipoColaboracion.setItems(items);
    }

    private void asignarTipoColaboracionAlSeleccionar(PropuestaColaboracion propuesta) {
        comboBox_TipoColaboracion.getSelectionModel().selectedItemProperty().addListener((observable, valorAntiguo, valorNuevo) -> {
            if (valorNuevo != null) {
                propuesta.setTipoColaboracion(valorNuevo);
            }
        });
        String primerValor = comboBox_TipoColaboracion.getItems().get(0);
        propuesta.setTipoColaboracion(primerValor);
    }
    
    private void llenarComboBoxIdioma() {
        try {
            List<List<String>> listaDeIdiomas = obtenerListaDeIdiomas();
            ObservableList<String> articulos = FXCollections.observableArrayList(obtenerNombresDeLasListas(listaDeIdiomas));
            comboBox_Idioma.setItems(articulos);
        } catch (SQLException ex) {
            LOG.error(ex);
        }
    }

    private void llenarComboBoxPerdiodo() {
        try {
            List<List<String>> listaDePeriodos = obtenerListaDePeriodo();
            ObservableList<String> articulos = FXCollections.observableArrayList(obtenerNombresYFechasDeLasListas(listaDePeriodos));
            comboBox_Periodo.setItems(articulos);
        } catch (SQLException ex) {
            LOG.error(ex);
        }
    }

    private List<String> obtenerNombresYFechasDeLasListas(List<List<String>> lista) {
        List<String> nombresYFechas = new ArrayList<>();
        lista.forEach(item -> {
            String nombre = item.get(1);
            String fechaInicio = item.get(2);
            String fechaFin = item.get(3);
            nombresYFechas.add(nombre + " - " + fechaInicio + " a " + fechaFin);
        });
        return nombresYFechas;
    }

    private List<List<String>> obtenerListaDeIdiomas() throws SQLException {
        return new ProfesorDAO().obtenerListaDeIdiomas();
    }

    private List<List<String>> obtenerListaDePeriodo() throws SQLException {
        return new PeriodoDAO().obtenerListaDePeriodos();
    }

    private List<String> obtenerNombresDeLasListas(List<List<String>> lista) {
        List<String> nombres = new ArrayList<>();
        lista.forEach(item -> nombres.add(item.get(1)));
        return nombres;
    }

     @FXML
    void cancelarPropuesta(ActionEvent event) {
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
    
    private PropuestaColaboracion crearPropuesta() {
        PropuestaColaboracion propuestaColaboracion = new PropuestaColaboracion(); 
        asignarTipoColaboracionAlSeleccionar(propuestaColaboracion);
        propuestaColaboracion.setNombre(textField_Nombre.getText());
        propuestaColaboracion.setObjetivoGeneral(txtArea_Objetivo.getText());
        propuestaColaboracion.setTemas(txtArea_Temas.getText());
        try {
            int numeroEstudiante = Integer.parseInt(textField_NoEstudiante.getText());
            propuestaColaboracion.setNumeroEstudiante(numeroEstudiante);
        } catch (NumberFormatException nfe) {
           LOG.warn(nfe);
        }
        propuestaColaboracion.setInformacionAdicional(txtArea_Informacion.getText());
        propuestaColaboracion.setPerfilDeLosEstudiantes(textField_PerfilEstudiante.getText());
        propuestaColaboracion.setEstadoPropuesta(EnumPropuesta.EnEspera.toString());
        asignarIdIdioma(propuestaColaboracion);
        asignarIdPeriodo(propuestaColaboracion);
        return propuestaColaboracion;
    }
        
    @FXML
    void someterColaboracion(ActionEvent event) {
        if (verificarInformacion()) {
            PropuestaColaboracion propuestaColaboracion = crearPropuesta();
            if (actualizarPropuesta(propuestaColaboracion) == true) {
                Alertas.mostrarMensajeExito();
                 Stage escenario = (Stage) button_Cancelar.getScene().getWindow();
                SDGCOILVIC sdgcoilvic = new SDGCOILVIC();
                try {
                    sdgcoilvic.mostrarVentanaAdministrarPropuestasDeColaboracion(escenario);
                } catch (IOException ex) {
                    LOG.error( ex);
                }
            }    
        }
    }
    
    private boolean actualizarPropuesta(PropuestaColaboracion propuestaColaboracion) {
        PropuestaColaboracionDAO propuestaColaboracionDAO = new PropuestaColaboracionDAO();
        boolean registroExitoso = false;
        if (propuestaColaboracion != null) {
            try {
                if (propuestaColaboracionDAO.actualizarInformacionDeLaPropuesta(propuestaColaboracion, idPropuestaColaboracion) == 1) {
                    registroExitoso = true;
                } else {
                    Alertas.mostrarMensajeInformacionNoRegistrada();
                }

            } catch (SQLException sqlException) {
                Alertas.mostrarMensajeErrorBaseDatos();
                LOG.fatal("Error en la base de datos en la clase " + this.getClass().getName() + ", método " + Thread.currentThread().getStackTrace()[1].getMethodName() + ": " + sqlException.getMessage(), sqlException);
            }
        }
        return registroExitoso;
    }
    
    private void asignarIdPeriodo(PropuestaColaboracion propuestaColaboracion) {
        int indicePeriodoSeleccionada = comboBox_Periodo.getSelectionModel().getSelectedIndex();
        if (indicePeriodoSeleccionada >= 0) {
            try {
                List<List<String>> listaDePeriodos = obtenerListaDePeriodo();
                if (indicePeriodoSeleccionada < listaDePeriodos.size()) {
                    int periodosEscolares = Integer.parseInt(listaDePeriodos.get(indicePeriodoSeleccionada).get(0));
                    propuestaColaboracion.setIdPeriodo(periodosEscolares);
                }
            } catch (SQLException ex) {
                LOG.error(ex);
            }
        }
    }

    private void asignarIdIdioma(PropuestaColaboracion propuestaColaboracion) {
        int indiceIdiomaSeleccionado = comboBox_Idioma.getSelectionModel().getSelectedIndex();
        if (indiceIdiomaSeleccionado >= 0) {
            try {
                List<List<String>> listaDeIdiomas = obtenerListaDeIdiomas();
                if (indiceIdiomaSeleccionado < listaDeIdiomas.size()) {
                    int idIdioma = Integer.parseInt(listaDeIdiomas.get(indiceIdiomaSeleccionado).get(0));
                    propuestaColaboracion.setIdIdiomas(idIdioma);
                }
            } catch (SQLException | NumberFormatException ex) {
                LOG.error(ex);
            }
        }
    }
      
    private boolean estaVacio() {
        int indiceTipoColaboracionSeleccionada = comboBox_TipoColaboracion.getSelectionModel().getSelectedIndex();
        int indicePeriodoSeleccionado = comboBox_Periodo.getSelectionModel().getSelectedIndex();
        int indiceIdiomaSeleccionado = comboBox_Idioma.getSelectionModel().getSelectedIndex();

        return  (textField_Nombre.getText() == null || textField_Nombre.getText().isEmpty()) ||
            (textField_NoEstudiante.getText() == null || textField_NoEstudiante.getText().isEmpty()) ||
            (textField_PerfilEstudiante.getText() == null || textField_PerfilEstudiante.getText().isEmpty()) ||
            (txtArea_Temas.getText() == null || txtArea_Temas.getText().isEmpty()) ||
            (txtArea_Informacion.getText() == null || txtArea_Informacion.getText().isEmpty()) ||
            (txtArea_Objetivo.getText() == null || txtArea_Objetivo.getText().isEmpty()) ||
            indiceTipoColaboracionSeleccionada < 0 ||
            indicePeriodoSeleccionado < 0 ||
            indiceIdiomaSeleccionado < 0;

    }

    private boolean verificarInformacion() {
        PropuestaColaboracion propuestaColaboracion = new PropuestaColaboracion();
        boolean validacion = true;

        if (!estaVacio()) {
            try {
                propuestaColaboracion.setNombre(textField_Nombre.getText());
                propuestaColaboracion.setPerfilDeLosEstudiantes(textField_PerfilEstudiante.getText());
                propuestaColaboracion.setTemas(txtArea_Temas.getText());
                propuestaColaboracion.setInformacionAdicional(txtArea_Informacion.getText());
                propuestaColaboracion.setObjetivoGeneral(txtArea_Objetivo.getText());
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
    
    private void llenarInformacionProfesor() {
       PropuestaColaboracion propuestaColaboracion = new PropuestaColaboracion();
       PropuestaColaboracionDAO propuestaColaboracionDAO = new PropuestaColaboracionDAO();
        try {
            propuestaColaboracion = propuestaColaboracionDAO.obtenerPropuestasColaboracionPorIdPropuesta(idPropuestaColaboracion);
            textField_Nombre.setText(propuestaColaboracion.getNombre());
            textField_NoEstudiante.setText(String.valueOf(propuestaColaboracion.getNumeroEstudiante()));
            textField_PerfilEstudiante.setText(propuestaColaboracion.getPerfilDeLosEstudiantes());
            txtArea_Temas.setText(propuestaColaboracion.getTemas());
            txtArea_Informacion.setText(propuestaColaboracion.getInformacionAdicional());
            txtArea_Objetivo.setText(propuestaColaboracion.getObjetivoGeneral());
            asignarNombrePeriodo(propuestaColaboracion);
            asignarNombreIdioma(propuestaColaboracion);
            comboBox_TipoColaboracion.setValue(propuestaColaboracion.getTipoColaboracion());
        } catch (SQLException sQLExcpetion) {
            Alertas.mostrarMensajeErrorBaseDatos();
        }

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
                    comboBox_Periodo.setValue(nombreYFechas);
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
                    comboBox_Idioma.setValue(idioma.get(1));
                    return;
                }
            }
        } catch (SQLException sqlException) {
            LOG.error(sqlException);
        }
    } 
    
}