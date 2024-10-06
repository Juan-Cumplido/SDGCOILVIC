package sdgcoilvic.logicaDeNegocio.interfaces;

import java.sql.SQLException;
import java.util.List;
import sdgcoilvic.logicaDeNegocio.clases.Acceso;
import sdgcoilvic.logicaDeNegocio.clases.Profesor;
import sdgcoilvic.logicaDeNegocio.clases.ProfesorUV;

public interface IProfesorUV {
    int registrarProfesorUV( Acceso acceso,  ProfesorUV profesorUV)throws SQLException;
    public boolean verificarSiExisteElNoPersonal(String noPersonal) throws SQLException;
    int actualizarInformacionDelProfesorUV(Profesor profesor, ProfesorUV profesorUV,String idProfesorUV)throws SQLException;
    public List<List<String>> obtenerListaDeRegion() ;
    public List<List<String>> obtenerListaDeCategoriaContratacion() ;
    public List<List<String>> obtenerListaDeTipoContratacion() ;
    public List<List<String>> obtenerListaDeAreaAcademica() ;    
    public int eliminarProfesorUV(String noPersonal) ;
    public ProfesorUV obtenerProfesorUVPorNumPersonal (String numPersonal) throws SQLException;
        public ProfesorUV obtenerProfesorPorNumPersonal (String numPersonal) throws SQLException;
    
}
