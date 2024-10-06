package sdgcoilvic.logicaDeNegocio.interfaces;

import java.sql.SQLException;
import java.util.List;
import sdgcoilvic.logicaDeNegocio.clases.Profesor;
import sdgcoilvic.logicaDeNegocio.clases.PropuestaColaboracion;

public interface IPropuestaColaboracion {
   public int agregarPropuestaColaboracion(PropuestaColaboracion propuestaColaboracion)throws SQLException;
   public int evaluarPropuestaColaboracion(int idPropuesta, String evaluacion)throws SQLException;
   public List<PropuestaColaboracion> consultarPropuestasColaboracionPorProfesor(int idProfesor)throws SQLException;
   public int actualizarInformacionDeLaPropuesta (PropuestaColaboracion propuestaColaboracion, int idPropuesta) throws SQLException;
   public PropuestaColaboracion obtenerPropuestasColaboracionPorIdPropuesta ( int idPropuesta) throws SQLException;
   public List<List<String>> obtenerListaDeNomnbreProfesorPorIdProfesor () throws SQLException;
   public Profesor obtenerProfesorPorid (int correo) throws SQLException ;
   public String obtenerCorreoPorIdPropuesta (int idPropuesta)  ;
   public int reevertirEstado(int idPropuesta)throws SQLException ;
   public List<PropuestaColaboracion> consultarTodasLasPropuestasColaboracionAceptadas() throws SQLException ;
   public List<PropuestaColaboracion> consultarTodasLasPropuestasColaboracionOfertadas(int idProfesor) throws SQLException ;
   public List<PropuestaColaboracion> consultarTodasLasPropuestasColaboracionEnEspera()throws SQLException;
   public boolean verificarEstadoPropuestaColaboracion(int idProfesor) throws SQLException ;
}

