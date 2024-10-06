package sdgcoilvic.logicaDeNegocio.interfaces;


import java.sql.SQLException;
import sdgcoilvic.logicaDeNegocio.clases.Acceso;
import sdgcoilvic.logicaDeNegocio.clases.Profesor;

public interface IAcceso {
   public void ejecutarActualizacionBaseDatos() throws SQLException;
   public int agregarAcceso (Acceso acceso)throws SQLException ;
   public int verificarExistenciaAcceso(String usuario, String contrasenia)throws SQLException ; 
   public String obtenerTipoUsuario(String usuario, String contrasenia)throws SQLException ; 
   public int obtenerIdProfesor (String usuario, String contrasenia);
   public Profesor obtenerProfesorPorID(int idProfesor) throws SQLException;
   public int actualizarAcceso(Acceso acceso, int idProfesor) throws SQLException;
}
