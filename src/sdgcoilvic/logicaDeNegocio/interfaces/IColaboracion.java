package sdgcoilvic.logicaDeNegocio.interfaces;

import java.sql.SQLException;
import java.util.List;
import sdgcoilvic.logicaDeNegocio.clases.Colaboracion;
import sdgcoilvic.logicaDeNegocio.clases.PropuestaColaboracion;
import sdgcoilvic.logicaDeNegocio.clases.TablaColaboracion;

public interface IColaboracion {
    
   public int crearColaboracion(Colaboracion colaboracion, int idProfesor)throws SQLException;
   public int vincularProfesoresALaColaboracion(int idColaboracion);
   public int cambiarEstadoProfesorAColaborando(int idColaboracion);
   public int finalizarColaboracion(int idColaboracion);
   public int cerrarColaboracion(int idColaboracion)throws SQLException;
   public int obtenerIdColaboracionEnCurso(int idProfesor);
   public Colaboracion consultarColaboracion(int idColaboracion);
   public List<Colaboracion> consultarTodasColaboraciones();
   public List<Colaboracion> filtrarColaboraciones(String filtro);
   public List<String> obtenerNombreProfesores(int idColaboracion)throws SQLException;
   public PropuestaColaboracion obtenerPropuestaColaboracion(int idColaboracion) throws SQLException;
   public List<TablaColaboracion> consultarTodasColaboracionesCoilVic()throws SQLException;
   public List<String> consultarDetallesEspecificos(int idColaboracion) throws SQLException; 
   public int rechazarTodasLasSolicitudes(int idPropuesta);
}
