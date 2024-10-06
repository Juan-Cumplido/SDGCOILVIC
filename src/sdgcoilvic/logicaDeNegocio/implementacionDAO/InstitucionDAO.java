package sdgcoilvic.logicaDeNegocio.implementacionDAO;

import com.mysql.cj.jdbc.CallableStatement;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import sdgcoilvic.accesoADatos.ManejadorBaseDeDatos;
import sdgcoilvic.logicaDeNegocio.clases.Institucion;
import sdgcoilvic.logicaDeNegocio.interfaces.IInstitucion;

public class InstitucionDAO implements IInstitucion {
    private static final String AGREGAR_INSTITUCION = "{CALL agregarInstitucion(?, ?, ?, ?)}";
    private static final String VERIFICAR_EXISTENCIA_NOMBRE_INSTITUCION = "SELECT COUNT(*) AS number_of_matches FROM institucion WHERE nombreInstitucion = ?";
    private static final String VERIFICAR_EXISTENCIA_CORREO = "SELECT COUNT(*) AS number_of_matches FROM institucion WHERE correo = ?";
    private static final String VERIFICAR_EXISTENCIA_CLAVE = "SELECT COUNT(*) AS number_of_matches FROM institucion WHERE claveInstitucional = ?";
    private static final String CONSULTA_TODAS_LAS_INSTITUCIONES = """
                                                                   SELECT i.claveInstitucional, i.nombreInstitucion, p.nombre AS nombrePais, i.correo
                                                                   FROM institucion i 
                                                                   INNER JOIN pais p ON i.Pais_idPais = p.idPais;""";
    private static final String OBTENER_LISTA_DE_PAISES = "SELECT nombre FROM pais;";
    private static final String OBTENER_INSTITUCIONES_POR_NOMBRE= "SELECT i.claveInstitucional, i.nombreInstitucion, p.nombre AS nombrePais, i.correo " +
                                                                    "FROM institucion i " +
                                                                    "INNER JOIN pais p ON i.Pais_idPais = p.idPais " +
                                                                    "WHERE i.nombreInstitucion LIKE ?";
    private static final String OBTENER_INSTITUCION_POR_CLAVE = """
                                                                SELECT i.claveInstitucional, i.nombreInstitucion, p.nombre AS nombrePais, i.correo
                                                                FROM institucion i
                                                                JOIN pais p ON i.Pais_idPais = p.idPais
                                                                WHERE i.claveInstitucional = ?;""";
    private static final String ACTUALIZAR_INSTITUCION = "{CALL actualizarInstitucionConClave(?, ?, ?, ?)}";
    
    /**
    * Actualiza la información de una institución en la base de datos.
    * 
    * @param institucion El objeto Institución con la información actualizada.
    * @param clave La Clave de la Institución a actualizar.
    * @return El número de filas afectadas en la base de datos.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos, por ejemplo:
     *                      - Cuando no se puede establecer conexión con la base de datos.
     *                      - Si los parámetros proporcionados son inválidos o nulos.
     *                      - Si la consulta SQL no se puede ejecutar correctamente.
     *                      - Si hay un problema al cerrar la conexión con la base de datos.
    */
    @Override
    public int actualizarInformacionDeLaInstitucion(Institucion institucion, String clave) throws SQLException{
        int resultado = -1;
        String consulta = ACTUALIZAR_INSTITUCION;
        Connection conexion = ManejadorBaseDeDatos.obtenerConexion();
        CallableStatement llamadaProcedimiento = (CallableStatement) conexion.prepareCall(consulta);
        llamadaProcedimiento.setString(1, clave);
        llamadaProcedimiento.setString(2, institucion.getNombreInstitucion());
        llamadaProcedimiento.setString(3, institucion.getNombrePais());
        llamadaProcedimiento.setString(4, institucion.getCorreo());
        resultado = llamadaProcedimiento.executeUpdate();
        ManejadorBaseDeDatos.cerrarConexion();
        return resultado; 
    }
    
        /**
     * Obtiene una Institucion por clave institucional
     * 
     * @param clave La clave de la institución a buscar.
     * @return El objeto Institucion correspondiente a la clave proporcionada, o un objeto Institucion vacío si no se encuentra.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos, por ejemplo:
     *                      - Cuando no se puede establecer conexión con la base de datos.
     *                      - Si los parámetros proporcionados son inválidos o nulos.
     *                      - Si la consulta SQL no se puede ejecutar correctamente.
     *                      - Si hay un problema al cerrar la conexión con la base de datos.
     */
    @Override
    public Institucion obtenerInstitucionPorClave(String clave) throws SQLException {
        Institucion institucion = new Institucion();
        String consulta = OBTENER_INSTITUCION_POR_CLAVE;
        Connection conexion = ManejadorBaseDeDatos.obtenerConexion();
        PreparedStatement declaracion = conexion.prepareStatement(consulta);
        declaracion.setString(1, clave);
        ResultSet resultado = declaracion.executeQuery();
        if (resultado.next()) {
                    institucion.setClaveInstitucional(resultado.getString("claveInstitucional"));
                    institucion.setNombreInstitucion(resultado.getString("nombreInstitucion"));
                    institucion.setNombrePais(resultado.getString("nombrePais"));
                    institucion.setCorreo(resultado.getString("correo"));
        }
        ManejadorBaseDeDatos.cerrarConexion();
        return institucion;

    }
    
    /**
     * Inserta una nueva institución en la base de datos.
     * 
     * @param institucion La institución a insertar.
     * @return El número de filas afectadas en la base de datos.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos, por ejemplo:
     *                      - Cuando no se puede establecer conexión con la base de datos.
     *                      - Si los parámetros proporcionados son inválidos o nulos.
     *                      - Si la consulta SQL no se puede ejecutar correctamente.
     *                      - Si hay un problema al cerrar la conexión con la base de datos.
     */
    @Override
    public int insertarInstitucion(Institucion institucion) throws SQLException{
        int resultado = 0;
        try {
            ManejadorBaseDeDatos.obtenerConexion().setAutoCommit(false);
            CallableStatement declaracion = (CallableStatement) ManejadorBaseDeDatos.obtenerConexion().prepareCall(AGREGAR_INSTITUCION);
            declaracion.setString(1, institucion.getClaveInstitucional());
            declaracion.setString(2, institucion.getNombreInstitucion());
            declaracion.setString(3, institucion.getNombrePais());
            declaracion.setString(4, institucion.getCorreo());
            resultado = declaracion.executeUpdate();
            ManejadorBaseDeDatos.obtenerConexion().commit();
        } catch (SQLException ex) {
            ManejadorBaseDeDatos.obtenerConexion().rollback();
        } finally {
            ManejadorBaseDeDatos.obtenerConexion().close();
        }
        return resultado;
    }
    
    /**
    * Verifica si existe una institución con el nombre especificado.
    * 
    * @param nombreInstitucion El nombre de la institución a verificar.
    * @return true si la institución existe, false de lo contrario.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos, por ejemplo:
     *                      - Cuando no se puede establecer conexión con la base de datos.
     *                      - Si los parámetros proporcionados son inválidos o nulos.
     *                      - Si la consulta SQL no se puede ejecutar correctamente.
     *                      - Si hay un problema al cerrar la conexión con la base de datos.
    */
    @Override
    public boolean verificarSiExisteElNombreInstitucion(String nombreInstitucion) throws SQLException {
        boolean existeNombreInstitucion = true;
        String consulta = VERIFICAR_EXISTENCIA_NOMBRE_INSTITUCION;
        Connection conexion = ManejadorBaseDeDatos.obtenerConexion();
        PreparedStatement declaracion = conexion.prepareStatement(consulta);
        declaracion.setString(1, nombreInstitucion);
        ResultSet resultado = declaracion.executeQuery();
        
        if (resultado.next()) {
            int NO_COINCIDE = 0;
            
            if (resultado.getInt("number_of_matches") == NO_COINCIDE) {
                existeNombreInstitucion = false;
            }
        }
        ManejadorBaseDeDatos.obtenerConexion().close();
        return existeNombreInstitucion;
    }
    
    /**
    * Verifica si existe un correo electrónico en la base de datos.
    * 
    * @param correo El correo electrónico a verificar.
    * @return true si el correo electrónico existe, false de lo contrario.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos, por ejemplo:
     *                      - Cuando no se puede establecer conexión con la base de datos.
     *                      - Si los parámetros proporcionados son inválidos o nulos.
     *                      - Si la consulta SQL no se puede ejecutar correctamente.
     *                      - Si hay un problema al cerrar la conexión con la base de datos.
    */
    @Override
    public boolean verificarSiExisteElCorreo(String correo) throws SQLException {
        boolean existeCorreo = true;
        String consulta = VERIFICAR_EXISTENCIA_CORREO;
        Connection conexion = ManejadorBaseDeDatos.obtenerConexion();
        PreparedStatement declaracion = conexion.prepareStatement(consulta);
        declaracion.setString(1, correo);
        ResultSet resultado = declaracion.executeQuery();
        
        if (resultado.next()) {
            int NO_COINCIDE = 0;
            
            if (resultado.getInt("number_of_matches") == NO_COINCIDE) {
                existeCorreo = false;
            }
        }
        ManejadorBaseDeDatos.obtenerConexion().close();
        return existeCorreo;
    }
    
    /**
    * Verifica si existe una clave de institución en la base de datos.
    * 
    * @param claveInstitucion La clave de institución a verificar.
    * @return true si la clave de institución existe, false de lo contrario.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos, por ejemplo:
     *                      - Cuando no se puede establecer conexión con la base de datos.
     *                      - Si los parámetros proporcionados son inválidos o nulos.
     *                      - Si la consulta SQL no se puede ejecutar correctamente.
     *                      - Si hay un problema al cerrar la conexión con la base de datos.
    */
    @Override
    public boolean verificarSiExisteLaClave(String claveInstitucion) throws SQLException {
        boolean existeClaveInstitucion = true;
        String consulta = VERIFICAR_EXISTENCIA_CLAVE;
        Connection conexion = ManejadorBaseDeDatos.obtenerConexion();
        PreparedStatement declaracion = conexion.prepareStatement(consulta);
        declaracion.setString(1, claveInstitucion);
        ResultSet resultado = declaracion.executeQuery();
        if (resultado.next()) {
            int NO_COINCIDE = 0;
            
            if (resultado.getInt("number_of_matches") == NO_COINCIDE) {
                existeClaveInstitucion = false;
            }
        }
        ManejadorBaseDeDatos.obtenerConexion().close();
        return existeClaveInstitucion;
    }
    
    /**
    * Obtiene todas las instituciones almacenadas en la base de datos.
    * 
    * @return Una lista de instituciones.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos, por ejemplo:
     *                      - Cuando no se puede establecer conexión con la base de datos.
     *                      - Si los parámetros proporcionados son inválidos o nulos.
     *                      - Si la consulta SQL no se puede ejecutar correctamente.
     *                      - Si hay un problema al cerrar la conexión con la base de datos.
    */
    @Override
     public List<Institucion> obtenerTodasLasInstituciones() throws SQLException {
        String consulta = CONSULTA_TODAS_LAS_INSTITUCIONES;
        Connection conexion = ManejadorBaseDeDatos.obtenerConexion();
        Statement declaracion = (Statement) conexion.createStatement();
        ResultSet resultado = declaracion.executeQuery(consulta);
        List<Institucion> listaInstituciones = new ArrayList<>();  
        listaInstituciones = obtenerListInstitucion(resultado);
        ManejadorBaseDeDatos.cerrarConexion();
     
        return listaInstituciones; 
     }

    /**
     * Obtiene una lista de instituciones que coinciden con el criterio de búsqueda por nombre.
     * 
     * @param criterioBusqueda El criterio de búsqueda por nombre.
     * @return Una lista de instituciones que coinciden con el criterio de búsqueda.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos, por ejemplo:
     *                      - Cuando no se puede establecer conexión con la base de datos.
     *                      - Si los parámetros proporcionados son inválidos o nulos.
     *                      - Si la consulta SQL no se puede ejecutar correctamente.
     *                      - Si hay un problema al cerrar la conexión con la base de datos.
     */
     @Override
     public List<Institucion> obtenerListaInstitucionesPorNombre(String criterioBusqueda) throws SQLException {
        String consulta = OBTENER_INSTITUCIONES_POR_NOMBRE;
            List<Institucion> listaInstitucionesPorPais = new ArrayList<>();
            Connection conexion = ManejadorBaseDeDatos.obtenerConexion();
            PreparedStatement declaracion = conexion.prepareStatement(consulta);
            declaracion.setString(1, "%" + criterioBusqueda + "%");
            ResultSet resultado = declaracion.executeQuery(); 
                while (resultado.next()) {
                    Institucion institucion = new Institucion();
                    institucion.setClaveInstitucional(resultado.getString("claveInstitucional"));
                    institucion.setNombreInstitucion(resultado.getString("nombreInstitucion"));
                    institucion.setNombrePais(resultado.getString("nombrePais"));
                    institucion.setCorreo(resultado.getString("correo"));
                    listaInstitucionesPorPais.add(institucion);
                }
            ManejadorBaseDeDatos.cerrarConexion();
        return listaInstitucionesPorPais;
     }
     
    /**
     * Obtiene una lista de todos los países almacenados en la base de datos.
     * 
     * @return Una lista de nombres de países.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos, por ejemplo:
     *                      - Cuando no se puede establecer conexión con la base de datos.
     *                      - Si la consulta SQL no se puede ejecutar correctamente.
     *                      - Si hay un problema al cerrar la conexión con la base de datos.
     */
     @Override
     public List<String>obtenerListaDePaises() throws SQLException{
         List<String> paises = new ArrayList<>();
         String consulta = OBTENER_LISTA_DE_PAISES;
         Connection conexion = ManejadorBaseDeDatos.obtenerConexion();
         PreparedStatement declaracion = conexion.prepareStatement(consulta);
         ResultSet resultado = declaracion.executeQuery(); 
            while (resultado.next()) {
                String nombre = resultado.getString("nombre");
                paises.add(nombre);
            }
        ManejadorBaseDeDatos.cerrarConexion();
        return paises;
     }
     
    /**
     * Método privado para obtener una lista de instituciones a partir de un ResultSet.
     * 
     * @param result El ResultSet que contiene los datos de las instituciones.
     * @return Una lista de instituciones.
     * @throws SQLException Si ocurre un error al interactuar con el ResultSet.
     */
    private List<Institucion> obtenerListInstitucion(ResultSet result) throws SQLException {
        List<Institucion> listaInstitucion = new ArrayList<>();
        
        while (result.next()) {
            Institucion institucion = new Institucion();
            institucion.setClaveInstitucional(result.getString("claveInstitucional"));
            institucion.setNombreInstitucion(result.getString("nombreInstitucion"));
            institucion.setNombrePais(result.getString("nombrePais"));
            institucion.setCorreo(result.getString("correo"));
            listaInstitucion.add(institucion);
        }
        return listaInstitucion;
    }

}
