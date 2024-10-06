package sdgcoilvic.logicaDeNegocio.interfaces;

import java.sql.SQLException;
import java.util.List;
import sdgcoilvic.logicaDeNegocio.clases.Institucion;

public interface IInstitucion {
    int insertarInstitucion(Institucion institucion)throws SQLException;
    public boolean verificarSiExisteElNombreInstitucion(String nombreInstitucion) throws SQLException;
    public boolean verificarSiExisteElCorreo(String correo) throws SQLException;
    public boolean verificarSiExisteLaClave(String claveInstitucion) throws SQLException;
    public List<Institucion>obtenerTodasLasInstituciones() throws SQLException;
    public List<Institucion>obtenerListaInstitucionesPorNombre(String criterioBusqueda) throws SQLException;
    public List<String>obtenerListaDePaises() throws SQLException;
    public Institucion obtenerInstitucionPorClave(String clave) throws SQLException;
    public int actualizarInformacionDeLaInstitucion(Institucion institucion, String clave) throws SQLException;
}
