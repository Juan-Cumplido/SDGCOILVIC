package sdgcoilvic.logicaDeNegocio.interfaces;

import java.io.File;
import java.sql.SQLException;
import java.util.List;
import sdgcoilvic.logicaDeNegocio.clases.EvidenciaActividad;
import sdgcoilvic.logicaDeNegocio.clases.Profesor;

public interface IEvidenciaActividad {
    public int agregarEvidencia(EvidenciaActividad evidencia )throws SQLException;
    public boolean crearCarpetaDeActividad(int idActividad, int idColaboracion, Profesor profesor);
    public String guardarEvidenciaDeActividad(int idActividad, int idColaboracion, File archivoNuevo, Profesor profesor);
    public int borrarArchivoDeEvidencia(String rutaEvidencia);
    public List<EvidenciaActividad> obtenerListaDeEvidencias(int idActividadColaborativa)throws SQLException;
    public EvidenciaActividad obtenerEvidenciaPorIdEvidencia(int idEvidencia)throws SQLException;
    public int actualizarEvidencia(EvidenciaActividad evidencia) throws SQLException;
   
}
