package sdgcoilvic.logicaDeNegocio.interfaces;
import java.sql.SQLException;
import java.util.List;
import sdgcoilvic.logicaDeNegocio.clases.ActividadColaborativa;

public interface IActividadColaborativa {
    public int agregarActividadColaborativa(ActividadColaborativa actividadColaborativa)throws SQLException;
    public ActividadColaborativa consultarActividadColaborativa(int idActividadColaborativa)throws SQLException;
    public List<ActividadColaborativa> obtenerActividades()throws SQLException;
    public List<Integer> verificarDuenioActividad(int idProfesor) throws SQLException ;
    public int actualizarInformacionDeLaActividad(ActividadColaborativa actividadColaborativa, int  idActividad) throws SQLException;
    public boolean  verificarActividadesFinalizadas(int idColaboracion) throws SQLException;
    public List<ActividadColaborativa> obtenerActividadesColaborativas(int idColaboracion) throws SQLException;
}
