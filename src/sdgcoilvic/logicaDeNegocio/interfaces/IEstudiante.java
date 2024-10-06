package sdgcoilvic.logicaDeNegocio.interfaces;

import java.sql.SQLException;
import sdgcoilvic.logicaDeNegocio.clases.Estudiante;

public interface IEstudiante{

    public int registrarEstudiante(Estudiante estudiante, int idColaboracion)  throws SQLException;
    public boolean verificarExistenciaEstudiante(String nombre, String apellidoPaterno, String apellidoMaterno) throws SQLException;
    public boolean verificarSiExisteElCorreo(String correo) throws SQLException ;
    
    
}
