package sdgcoilvic.controladores;
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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import sdgcoilvic.utilidades.Alertas;
import sdgcoilvic.utilidades.EnviosDeCorreoElectronico;
import sdgcoilvic.logicaDeNegocio.clases.Acceso;
import sdgcoilvic.logicaDeNegocio.clases.Profesor;
import sdgcoilvic.logicaDeNegocio.enums.EnumProfesor;
import sdgcoilvic.logicaDeNegocio.enums.EnumTipoDeAcceso;
import sdgcoilvic.utilidades.GeneradorDeContrasenias;
import sdgcoilvic.logicaDeNegocio.implementacionDAO.ProfesorDAO;

public class AgregarProfesorExternoControlador implements Initializable {
    private static final Logger LOG = Logger.getLogger(AgregarProfesorExternoControlador.class);
    private Stage escenario;

    @FXML private Button button_Cancelar;
    @FXML private Button button_Guardar;
    @FXML private ComboBox<String> comboBox_Idioma;
    @FXML private ComboBox<String> comboBox_Institucion;
    @FXML private Label label_ErrorNombre;
    @FXML private Label label_ErrorApellidoPaterno;
    @FXML private Label label_ErrorApellidoMaterno;
    @FXML private Label label_ErrorCorreo;
    @FXML private Label label_ErrorIdioma;
    @FXML private Label label_ErrorInstitucion;
    @FXML private TextField textField_Nombre;
    @FXML private TextField textField_ApellidoPaterno;
    @FXML private TextField textField_ApellidoMaterno;
    @FXML private TextField textField_Correo;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        aplicarValidacion(textField_Nombre, "^[\\p{L}áéíóúÁÉÍÓÚüÜ\\s',;\\-_:\\.]{0,60}$");
        aplicarValidacion(textField_ApellidoPaterno, "^[\\p{L}áéíóúÁÉÍÓÚüÜ\\s',;\\-_:\\.]{1,60}$");
        aplicarValidacion(textField_ApellidoMaterno, "^[\\p{L}áéíóúÁÉÍÓÚüÜ\\s',;\\-_:\\.]{1,60}$");
        
        llenarComboBoxIdioma();
        llenarComboBoxInstitucion();
        etiquetasDeError();
    }

    public void setStage(Stage escenario) {
        this.escenario = escenario;
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

    private void llenarComboBoxInstitucion() {
        try {
            List<List<String>> listaDeInstituciones = obtenerListaDeInstituciones();
            ObservableList<String> articulos = FXCollections.observableArrayList(obtenerNombresDeLasListas(listaDeInstituciones));
            comboBox_Institucion.setItems(articulos);
        } catch (SQLException ex) {
            LOG.error(ex);
        }
    }


    private List<List<String>> obtenerListaDeIdiomas() throws SQLException {
        return new ProfesorDAO().obtenerListaDeIdiomas();
    }

    private List<List<String>> obtenerListaDeInstituciones() throws SQLException {
        return new ProfesorDAO().obtenerListaDeInstitucionesSinInstitucion();
    }

    private List<String> obtenerNombresDeLasListas(List<List<String>> lista) {
        List<String> nombres = new ArrayList<>();
        lista.forEach(item -> nombres.add(item.get(1)));
        return nombres;
    }

    @FXML
    private void cancelarRegistro(ActionEvent event) {
        if (Alertas.mostrarMensajeCancelar()) {
            Stage escenario = (Stage) button_Cancelar.getScene().getWindow();
            escenario.close();
        }
    }
    
    @FXML
    private void guardarRegistro(ActionEvent event) {
        etiquetasDeError();
        if (verificarInformacion()) {
            Profesor profesor = crearProfesor();
            Acceso acceso = crearAcceso();
            if (registrarProfesor(profesor, acceso) == true) {
                Alertas.mostrarMensajeExito();
                Stage escenario = (Stage) button_Cancelar.getScene().getWindow();
                escenario.close();
                if (onCloseCallback != null) {
                    onCloseCallback.run();
                }
            }    
        }
    }
    
    private Profesor crearProfesor() {
        Profesor profesor = new Profesor();
        profesor.setNombre(textField_Nombre.getText());
        profesor.setApellidoPaterno(textField_ApellidoPaterno.getText());
        profesor.setApellidoMaterno(textField_ApellidoMaterno.getText());
        profesor.setCorreo(textField_Correo.getText());
        asignarIdIdioma(profesor);
        profesor.setEstadoProfesor(EnumProfesor.Activo.toString());
        asignarClaveInstitucional(profesor);
        return profesor;
    }

    private Acceso crearAcceso() {
        Acceso acceso = new Acceso();
        acceso.setUsuario(textField_Correo.getText());
        acceso.setContrasenia(GeneradorDeContrasenias.generarContraseña());
        acceso.setTipoUsuario(EnumTipoDeAcceso.Profesor.toString());
        return acceso;
    }
    
    private void asignarClaveInstitucional(Profesor profesor) {
        int indiceInstitucionSeleccionada = comboBox_Institucion.getSelectionModel().getSelectedIndex();
        if (indiceInstitucionSeleccionada >= 0) {
            try {
                List<List<String>> listaDeInstituciones = obtenerListaDeInstituciones();
                if (indiceInstitucionSeleccionada < listaDeInstituciones.size()) {
                    String claveInstitucion = listaDeInstituciones.get(indiceInstitucionSeleccionada).get(0);
                    profesor.setClaveInstitucional(claveInstitucion);
                }
            } catch (SQLException ex) {
                LOG.error(ex);
            }
        }
    }

    private void asignarIdIdioma(Profesor profesor) {
        int indiceIdiomaSeleccionado = comboBox_Idioma.getSelectionModel().getSelectedIndex();
        if (indiceIdiomaSeleccionado >= 0) {
            try {
                List<List<String>> listaDeIdiomas = obtenerListaDeIdiomas();
                if (indiceIdiomaSeleccionado < listaDeIdiomas.size()) {
                    int idIdioma = Integer.parseInt(listaDeIdiomas.get(indiceIdiomaSeleccionado).get(0));
                    profesor.setIdIdiomas(idIdioma);
                }
            } catch (SQLException | NumberFormatException ex) {
                LOG.error(ex);
            }
        }
    }
    
    private boolean registrarProfesor(Profesor profesor, Acceso acceso) {
        ProfesorDAO profesorDAO = new ProfesorDAO();
        boolean registroExitoso = false;
        if (profesor != null) {
            try {
                if (!profesorDAO.verificarSiExisteElCorreo(profesor.getCorreo())) {
                    if (!profesorDAO.verificarExistenciaProfesor(profesor.getNombre(), profesor.getApellidoPaterno(), profesor.getApellidoMaterno())) {
                        if (profesorDAO.registrarProfesor(profesor, acceso) == 1) {
                            registroExitoso = true;
                            if (enviarCorreo(profesor, acceso) == false) {
                                Stage miVentana = (Stage) button_Cancelar.getScene().getWindow();
                                miVentana.close();
                                if (onCloseCallback != null) {
                                    onCloseCallback.run();
                                }
                                Alertas.mostrarMensajeElCorreoNoSePudoEnviar();
                                registroExitoso = false;
                            }
                        } else {
                            Alertas.mostrarMensajeInformacionNoRegistrada();
                        }
                    } else {
                        Alertas.mostrarMensajeProfesorYaExistente();
                    }
                } else {
                    Alertas.mostrarMensajeEmailYaRegistrado();
                }
            } catch (SQLException sqlException) {
                Alertas.mostrarMensajeErrorBaseDatos();
                LOG.fatal("Error en la base de datos en la clase " + this.getClass().getName() + ", método " + Thread.currentThread().getStackTrace()[1].getMethodName() + ": " + sqlException.getMessage(), sqlException);
            } catch (IllegalArgumentException ilegaLArgument) {
                LOG.error(ilegaLArgument);
            }
        }
        return registroExitoso;
    }
    
    private boolean enviarCorreo(Profesor profesor, Acceso acceso) {
        String mensaje = "Estimado profesor " + profesor.getNombre() + ",\n\n" +
                "Lo hemos registrado exitosamente como profesor. A continuación se muestran tus credenciales de acceso:\n\n" +
                "Usuario: " + acceso.getUsuario() + "\n" +
                "Contraseña: " + acceso.getContrasenia() + "\n\n" +
                "¡Gracias por su solicitud!\n" +
                "SDGCOILVIC";
        
        return EnviosDeCorreoElectronico.verificarEnvioCorreo(profesor.getCorreo(), "Credenciales de acceso", mensaje);
    }
    
    private boolean estaVacioTextField() {
          return textField_Nombre.getText().isEmpty() ||
                 textField_ApellidoPaterno.getText().isEmpty() ||
                 textField_Correo.getText().isEmpty();
    }

    private boolean estaVacioComboBox() {
          int indiceInstitucionSeleccionada = comboBox_Institucion.getSelectionModel().getSelectedIndex();
          int indiceIdiomaSeleccionado = comboBox_Idioma.getSelectionModel().getSelectedIndex();
          return indiceInstitucionSeleccionada < 0 || indiceIdiomaSeleccionado < 0;
    }

    private boolean verificarInformacion() {
          Profesor profesor = new Profesor();
          boolean validacion = true;

          boolean textFieldVacios = estaVacioTextField();
          boolean comboBoxVacios = estaVacioComboBox();

          if (textFieldVacios && comboBoxVacios) {
              Alertas.mostrarMensajeCamposVacios();
              validacion = false;
          } else if (textFieldVacios) {
              Alertas.mostrarMensajeCamposVacios();
              validacion = false;
          } else if (comboBoxVacios) {
              Alertas.mostrarMensajeComboBoxSinSeleccionar("Estado o Institución");
              validacion = false;
          } else {
              try {
                  profesor.setNombre(textField_Nombre.getText());
                  profesor.setApellidoPaterno(textField_ApellidoPaterno.getText());
                  profesor.setApellidoMaterno(textField_ApellidoMaterno.getText());
              } catch (IllegalArgumentException ilegaLArgument) {
                  Alertas.mostrarMensajeInformacionInvalida();
                  validacion = false;
              }

              try {
                  profesor.setCorreo(textField_Correo.getText());
              } catch (IllegalArgumentException correoException) {
                  label_ErrorCorreo.setVisible(true);
                  Alertas.mostrarMensajeCorreoConFormatoInvalido();
                  validacion = false;
              }
          }

          return validacion;
    }
    
    private void etiquetasDeError() {
        label_ErrorNombre.setVisible(false);
        label_ErrorApellidoPaterno.setVisible(false);
        label_ErrorApellidoMaterno.setVisible(false);
        label_ErrorCorreo.setVisible(false);
        label_ErrorIdioma.setVisible(false);
        label_ErrorInstitucion.setVisible(false);
    }
}