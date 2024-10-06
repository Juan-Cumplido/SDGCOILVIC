package sdgcoilvic.logicaDeNegocio.interfaces;

import java.sql.SQLException;
import java.util.List;

public interface ISolicitudColaboracion {
    public List<List<String>> consultarSolicitudesColaboracion(int idProfesor)throws SQLException ;
    public int enviarSolicitudDeColaboracion(int idPropuestaColaboracion, String mensaje, int idProfesor) throws SQLException ;
    public int rechazarSolicitud(int idSolicitudColaboracion) throws SQLException ;
    public int aceptarSolicitud(int idSolicitudColaboracion) throws SQLException ;
    public int reevertirEvaluacion(int idSolicitudColaboracion) throws SQLException ;
    public int contarSolicitudesAceptadas(int idProfesor) throws SQLException ;
    public String obtenerSolicitudesAprobadas(int idSolicitudColaboracion) throws SQLException ;
    public boolean verificarEstadoColaboracion(int idProfesor)throws SQLException;
    public boolean verificarEstadoSolicitud(int idProfesor) throws SQLException;
}
