package sdgcoilvic.controladores;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class SDGCOILVIC extends Application {
    @Override
    public void start(Stage escenario) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/sdgcoilvic/interfazDeUsuario/Acceso.fxml"));
        Scene escena = new Scene(root);
        escenario.setScene(escena);  
        escenario.show();    
    } 
    
     public void mostrarVentanaAcceso (Stage escenario) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/sdgcoilvic/interfazDeUsuario/Acceso.fxml"));
        Scene escena = new Scene (root);
        escenario.setScene(escena);
        escenario.show();
    }
    
    public void mostrarVentanaAdministrativoMenu(Stage escenario) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/sdgcoilvic/interfazDeUsuario/AdministrativoMenu.fxml"));
        Scene escena = new Scene (root);
        escenario.setScene(escena);
        escenario.show();
    }
    
    public void mostrarVentanaGestionDeColaboraciones (Stage escenario) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/sdgcoilvic/interfazDeUsuario/GestionDeColaboraciones.fxml"));
        Scene escena = new Scene (root);
        escenario.setScene(escena);
        escenario.show();
    }
    
   
    public void mostrarVentanaGestionDeOfertasDeColaboracion (Stage escenario) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/sdgcoilvic/interfazDeUsuario/GestionDeOfertasDeColaboracion.fxml"));
        Scene escena = new Scene (root);
        escenario.setScene(escena);
        escenario.show();
    }
    
    public void mostrarVentanaGestionDeProfesores (Stage escenario) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/sdgcoilvic/interfazDeUsuario/GestionDeProfesores.fxml"));
        Scene escena = new Scene (root);
        escenario.setScene(escena);
        escenario.show();
    }
    
    public void mostrarVentanaAgregarProfesorExterno(Stage escenario, Runnable onCloseCallback) throws IOException {
        Stage escenarioAgregar = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/sdgcoilvic/interfazDeUsuario/AgregarProfesorExterno.fxml"));
        Parent root = loader.load();
        Scene escena = new Scene(root);
        escenarioAgregar.setScene(escena);

        escenarioAgregar.initModality(Modality.APPLICATION_MODAL);
        AgregarProfesorExternoControlador controller = loader.getController();
        controller.setOnCloseCallback(onCloseCallback);
        controller.setStage(escenario);

        escenarioAgregar.show();
    }
    
    public void mostrarVentanaAgregarProfesorUV(Stage escenario, Runnable onCloseCallback) throws IOException {
        Stage escenarioAgregar = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/sdgcoilvic/interfazDeUsuario/AgregarProfesorUV.fxml"));
        Parent root = loader.load();
        Scene escena = new Scene(root);
        escenarioAgregar.setScene(escena);

        escenarioAgregar.initModality(Modality.APPLICATION_MODAL);
        AgregarProfesorUVControlador controller = loader.getController();
        controller.setOnCloseCallback(onCloseCallback);
        controller.setStage(escenario);

        escenarioAgregar.show();
    }
    
    public void mostrarVentanaModificarProfesorUV(Stage escenario, Runnable onCloseCallback) throws IOException {
        Stage escenarioAgregar = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/sdgcoilvic/interfazDeUsuario/ModificarProfesorUV.fxml"));
        Parent root = loader.load();
        Scene escena = new Scene(root);
        escenarioAgregar.setScene(escena);

        escenarioAgregar.initModality(Modality.APPLICATION_MODAL);
        ModificarProfesorUVControlador controller = loader.getController();
        controller.setOnCloseCallback(onCloseCallback);
        controller.setStage(escenario);

        escenarioAgregar.show();
    }
    
        public void mostrarVentanaModificarProfesorExterno(Stage escenario, Runnable onCloseCallback) throws IOException {
        Stage escenarioAgregar = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/sdgcoilvic/interfazDeUsuario/ModificarProfesorExterno.fxml"));
        Parent root = loader.load();
        Scene escena = new Scene(root);
        escenarioAgregar.setScene(escena);

        escenarioAgregar.initModality(Modality.APPLICATION_MODAL);
        ModificarProfesorExternoControlador controller = loader.getController();
        controller.setOnCloseCallback(onCloseCallback);
        controller.setStage(escenario);

        escenarioAgregar.show();
    }
    
    public void mostrarVentanaGestionDePropuestasColaboracion (Stage escenario) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/sdgcoilvic/interfazDeUsuario/GestionDePropuestasColaboracion.fxml"));
        Scene escena = new Scene (root);
        escenario.setScene(escena);
        escenario.show();
    }
    
    public void mostrarVentanaEvaluarPropuestaDeColaboracion(Stage escenario) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/sdgcoilvic/interfazDeUsuario/EvaluarPropuestaDeColaboracion.fxml"));
        Scene escena = new Scene (root);
        escenario.setScene(escena);
        escenario.show();
        
    }
    
    public void mostrarVentanaGestionDeInstituciones(Stage escenario) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/sdgcoilvic/interfazDeUsuario/GestionDeInstituciones.fxml"));
        Scene escena = new Scene (root);
        escenario.setScene(escena);
        escenario.show();
    }
    
    public void mostrarVentanaRegistrarInstitucion(Stage escenario, Runnable onCloseCallback) throws IOException {
        Stage stageRegistrar = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/sdgcoilvic/interfazDeUsuario/RegistrarInstitucion.fxml"));
        Parent root = loader.load();
        Scene escena = new Scene(root);
        stageRegistrar.setScene(escena);

        stageRegistrar.initModality(Modality.APPLICATION_MODAL);
        RegistrarInstitucionControlador controller = loader.getController();
        controller.setStage(escenario);
        controller.setOnCloseCallback(onCloseCallback);
        stageRegistrar.show();
    }
    
    public void mostrarVentanaModificarInstitucion(Stage escenario, Runnable onCloseCallback) throws IOException {
        Stage stageRegistrar = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/sdgcoilvic/interfazDeUsuario/ModificarInstitucion.fxml"));
        Parent root = loader.load();
        Scene escena = new Scene(root);
        stageRegistrar.setScene(escena);

        stageRegistrar.initModality(Modality.APPLICATION_MODAL);
        ModificarInstitucionControlador controller = loader.getController();
        controller.setStage(escenario);
        controller.setOnCloseCallback(onCloseCallback);
        stageRegistrar.show();
    }
    
    public void mostrarVentanaProfesorMenu (Stage escenario) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/sdgcoilvic/interfazDeUsuario/ProfesorMenu.fxml"));
        Scene escena = new Scene (root);
        escenario.setScene(escena);
        escenario.show();
    }
    
    public void mostrarVentanaAdministrarColaboracionActiva(Stage escenario) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/sdgcoilvic/interfazDeUsuario/AdministrarColaboracionActiva.fxml"));
        Scene escena = new Scene (root);
        escenario.setScene(escena);
        escenario.show();
    }
    
    public void mostrarVentanaDetallesColaboracion(Stage escenario) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/sdgcoilvic/interfazDeUsuario/DetallesColaboracion.fxml"));
        Scene escena = new Scene (root);
        escenario.setScene(escena);
        escenario.show();
    }
    
    public void mostrarVentanaEvidenciasPorActividad(Stage escenario) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/sdgcoilvic/interfazDeUsuario/EvidenciasPorActividad.fxml"));
        Scene escena = new Scene (root);
        escenario.setScene(escena);
        escenario.show();
    }
    
    public void mostrarVentanaAvanceDeColaboracion(Stage escenario) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/sdgcoilvic/interfazDeUsuario/AvanceDeColaboracion.fxml"));
        Scene escena = new Scene (root);
        escenario.setScene(escena);
        escenario.show();
    }
    
    public void mostrarVentanaAdministrarSolicitudes(Stage escenario) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/sdgcoilvic/interfazDeUsuario/AdministrarSolicitudes.fxml"));
        Scene escena = new Scene (root);
        escenario.setScene(escena);
        escenario.show();
    }
    
    public void mostrarVentanaAdministrarColaboracionesDisponibles(Stage escenario) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/sdgcoilvic/interfazDeUsuario/AdministrarColaboracionesDisponibles.fxml"));
        Scene escena = new Scene (root);
        escenario.setScene(escena);
        escenario.show();
    }
        
    public void mostrarVentanaDeclaracionDeProposito(Stage escenario) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/sdgcoilvic/interfazDeUsuario/DeclaracionDeProposito.fxml"));
        Scene escena = new Scene (root);
        escenario.setScene(escena);
        escenario.show();
    }
    
    public void mostrarVentanaAdministrarActividades(Stage escenario) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/sdgcoilvic/interfazDeUsuario/AdministrarActividades.fxml"));
        Scene escena = new Scene (root);
        escenario.setScene(escena);
        escenario.show();
    }
    
    public void mostrarVentanaAdministrarPropuestasDeColaboracion(Stage escenario) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/sdgcoilvic/interfazDeUsuario/AdministrarPropuestasDeColaboracion.fxml"));
        Scene escena = new Scene (root);
        escenario.setScene(escena);
        escenario.show();
    }
    
    public void mostrarVentanaNuevaPropuesta(Stage escenario) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/sdgcoilvic/interfazDeUsuario/NuevaPropuesta.fxml"));
        Scene escena = new Scene (root);
        escenario.setScene(escena);
        escenario.show();
    }
    
    public void mostrarVentanaActualizarPropuestaPropuesta(Stage escenario) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/sdgcoilvic/interfazDeUsuario/ActualizarPropuesta.fxml"));
        Scene escena = new Scene (root);
        escenario.setScene(escena);
        escenario.show();
    }
    
    public void mostrarVentanaIniciarColaboracion(Stage escenario) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/sdgcoilvic/interfazDeUsuario/IniciarColaboracion.fxml"));
        Scene escena = new Scene (root);
        escenario.setScene(escena);
        escenario.show();
    }
    
    public void mostrarVentanaAgregarEstudiante(Stage escenario,  Runnable onCloseCallback) throws IOException {
        Stage stageRegistrar = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/sdgcoilvic/interfazDeUsuario/AgregarEstudiante.fxml"));
        Parent root = loader.load();
        Scene escena = new Scene(root);
        stageRegistrar.setScene(escena);
 
       stageRegistrar.initModality(Modality.APPLICATION_MODAL);
        AgregarEstudianteControlador controller = loader.getController();
        controller.setStage(escenario);
        controller.setOnCloseCallback(onCloseCallback);
        stageRegistrar.show();
    }
    
    public void mostrarVentanaIniciarActividad(Stage escenario, Runnable onCloseCallback) throws IOException {
        Stage stageRegistrar = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/sdgcoilvic/interfazDeUsuario/IniciarActividad.fxml"));
        Parent root = loader.load();
        Scene escena = new Scene(root);
        stageRegistrar.setScene(escena);

        stageRegistrar.initModality(Modality.APPLICATION_MODAL);
        IniciarActividadControlador controller = loader.getController();
        controller.setStage(escenario);
        controller.setOnCloseCallback(onCloseCallback);
        stageRegistrar.show();
    }
    
    public void mostrarVentanaEvidencias(Stage escenario) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/sdgcoilvic/interfazDeUsuario/Evidencias.fxml"));
        Scene escena = new Scene (root);
        escenario.setScene(escena);
        escenario.show();
    }
    
    public void mostrarVentanaSubirEvidencia(Stage escenario, Runnable onCloseCallback) throws IOException {
        Stage stageRegistrar = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/sdgcoilvic/interfazDeUsuario/SubirEvidenciaActividad.fxml"));
        Parent root = loader.load();
        Scene escena = new Scene(root);
        stageRegistrar.setScene(escena);

        stageRegistrar.initModality(Modality.APPLICATION_MODAL);
        SubirEvidenciaActividadControlador controlador = loader.getController();
        controlador.setStage(escenario);
        controlador.setOnCloseCallback(onCloseCallback);
        stageRegistrar.show();
        
    }
    
    public void mostrarVentanaModificarEvidencia(Stage escenario, Runnable onCloseCallback) throws IOException {
        Stage stageRegistrar = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/sdgcoilvic/interfazDeUsuario/ModificarEvidenciaActividad.fxml"));
        Parent root = loader.load();
        Scene escena = new Scene(root);
        stageRegistrar.setScene(escena);

        stageRegistrar.initModality(Modality.APPLICATION_MODAL);
        ModificarEvidenciaActividadControlador controlador = loader.getController();
        controlador.setStage(escenario);
        controlador.setOnCloseCallback(onCloseCallback);
        stageRegistrar.show();
        
    }
    
    public void mostrarVentanaActualizarDetallesActividad(Stage escenario, Runnable onCloseCallback) throws IOException {
        Stage stageRegistrar = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/sdgcoilvic/interfazDeUsuario/ActualizarDetallesDeActividad.fxml"));
        Parent root = loader.load();
        Scene escena = new Scene(root);
        stageRegistrar.setScene(escena);

        stageRegistrar.initModality(Modality.APPLICATION_MODAL);
        ActualizarDetallesDeActividadControlador controlador = loader.getController();
        controlador.setStage(escenario);
        controlador.setOnCloseCallback(onCloseCallback);
        stageRegistrar.show();
        
    }
    
   
    
    public static void main(String[] args) {
           launch();
       }

}