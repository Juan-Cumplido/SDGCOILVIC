package sdgcoilvic.logicaDeNegocio.implementacionDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import sdgcoilvic.accesoADatos.ManejadorBaseDeDatos;
import sdgcoilvic.logicaDeNegocio.clases.Profesor;
import sdgcoilvic.logicaDeNegocio.clases.PropuestaColaboracion;
import sdgcoilvic.logicaDeNegocio.interfaces.IPropuestaColaboracion;

public class PropuestaColaboracionDAO implements IPropuestaColaboracion {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(PropuestaColaboracionDAO.class);
    
    private static final String INSERTAR_PROPUESTA = """
                                                     INSERT INTO propuesta_colaboracion (tipoColaboracion, nombre, objetivoGeneral, temas, numeroEstudiante, informacionAdicional, perfilDeLosEstudiantes, idIdiomas, idPeriodo, Profesor_idProfesor, estadoPropuesta)
                                                     VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);""";
    private static final String EVALUAR_PROPUESTA = """
                                                    UPDATE propuesta_colaboracion
                                                    SET estadoPropuesta = ?
                                                    WHERE idPropuestaColaboracion = ?;""";
    private static final String CONSULTAR_PROPUESTA_POR_IDPROPUESTA = "SELECT * FROM propuesta_colaboracion WHERE idPropuestaColaboracion = ?";
    private static final String CONSULTAR_PROPUESTAS_POR_PROFESOR = "SELECT * FROM propuesta_colaboracion WHERE Profesor_idProfesor = ?";
    private static final String CONSULTAR_CORREO_PROFESOR = """
                                                            SELECT profesor.correo
                                                            FROM propuesta_colaboracion
                                                            JOIN profesor ON propuesta_colaboracion.Profesor_idProfesor = profesor.idProfesor
                                                            WHERE propuesta_colaboracion.idPropuestaColaboracion = ?; """;
    private static final String CONSULTAR_PROFESOR_POR_ID = """
                                                            SELECT CONCAT(p.nombre, ' ', p.apellidoPaterno, ' ', IFNULL(p.apellidoMaterno, '')) AS nombre_completo,
                                                                   i.nombreInstitucion AS nombre_institucion
                                                            FROM profesor p
                                                            JOIN institucion i ON p.Institucion_claveInstitucional = i.claveInstitucional
                                                            WHERE p.idProfesor = ?;""";
    private static final String ACTUALIZAR_PROPUESTA = "UPDATE propuesta_colaboracion SET tipoColaboracion = ?, nombre = ?, " +
            "objetivoGeneral = ?, temas = ?, numeroEstudiante = ?, informacionAdicional = ?, perfilDeLosEstudiantes = ?, idIdiomas = ?, " +
            "idPeriodo = ?, estadoPropuesta = ? WHERE idPropuestaColaboracion = ?";
    private static final String CONSULTAR_TODAS_LAS_PROPUESTAS_EN_ESPERA = "SELECT * FROM propuesta_colaboracion where estadoPropuesta='EnEspera';";
    private static final String CONSULTAR_TODAS_LAS_PROPUESTAS_ACEPTADAS = "SELECT * FROM propuesta_colaboracion where estadoPropuesta='Aceptada';";
    private static final String CONSULTAR_TODAS_LAS_PROPUESTAS_OFERTADAS = """
                                                                              SELECT * 
                                                                              FROM propuesta_colaboracion 
                                                                              WHERE estadoPropuesta = 'Ofertada' 
                                                                              AND Profesor_idProfesor != ?;""";
    private static final String CONSULTAR_LISRTA_NOMBRES_PROFESORES = """
                                                                                 SELECT idProfesor, 
                                                                                        CONCAT(nombre, ' ', apellidoPaterno, ' ', COALESCE(apellidoMaterno, '')) AS nombreCompleto 
                                                                                 FROM profesor;""";
 
    private static final String CONSULTAR_ESTADO_PROPUESTA = """
                                                             SELECT COUNT(*) AS propuestas
                                                             FROM propuesta_colaboracion 
                                                             WHERE Profesor_idProfesor = ? 
                                                             AND estadoPropuesta = 'Ofertada';""";
 
    
    /**
    * Agrega una nueva propuesta de colaboración a la base de datos.
    * 
    * @param propuestaColaboracion La propuesta de colaboración que se desea agregar.
    * @return El número de filas afectadas por la operación de inserción. 
    *         Devuelve -1 si ocurre un error.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos, por ejemplo:
     *                      - Cuando no se puede establecer conexión con la base de datos.
     *                      - Si los parámetros proporcionados son inválidos o nulos.
     *                      - Si la consulta SQL no se puede ejecutar correctamente.
     *                      - Si hay un problema al cerrar la conexión con la base de datos.
    */
    @Override
    public int agregarPropuestaColaboracion(PropuestaColaboracion propuestaColaboracion) throws SQLException{
        int filasAfectadas = -1;
        String consulta = INSERTAR_PROPUESTA;
        Connection conexion = ManejadorBaseDeDatos.obtenerConexion();
            PreparedStatement declaracion = conexion.prepareStatement(consulta);
            declaracion.setString(1, propuestaColaboracion.getTipoColaboracion());
            declaracion.setString(2, propuestaColaboracion.getNombre());
            declaracion.setString(3, propuestaColaboracion.getObjetivoGeneral());
            declaracion.setString(4, propuestaColaboracion.getTemas());
            declaracion.setInt(5, propuestaColaboracion.getNumeroEstudiante());
            declaracion.setString(6, propuestaColaboracion.getInformacionAdicional());
            declaracion.setString(7, propuestaColaboracion.getPerfilDeLosEstudiantes());
            declaracion.setInt(8, propuestaColaboracion.getIdIdiomas());
            declaracion.setInt(9, propuestaColaboracion.getIdPeriodo());
            declaracion.setInt(10, propuestaColaboracion.getIdProfesor());
            declaracion.setString(11, propuestaColaboracion.getEstadoPropuesta());
            filasAfectadas = declaracion.executeUpdate();
            ManejadorBaseDeDatos.cerrarConexion();
        return filasAfectadas;
    }
    
    /**
    * Actualiza la información de una propuesta de colaboración en la base de datos.
    *
    * @param propuestaColaboracion La propuesta de colaboración con la información actualizada.
    * @param idPropuesta El ID de la propuesta que se desea actualizar.
    * @return El número de filas afectadas por la operación de actualización.
    * @throws SQLException Si ocurre un error al interactuar con la base de datos, por ejemplo:
    *                      - Cuando no se puede establecer conexión con la base de datos.
    *                      - Si los parámetros proporcionados son inválidos o nulos.
    *                      - Si la consulta SQL no se puede ejecutar correctamente.
    *                      - Si hay un problema al cerrar la conexión con la base de datos.
    */
     @Override
    public int actualizarInformacionDeLaPropuesta (PropuestaColaboracion propuestaColaboracion, int idPropuesta) throws SQLException{
        int filasAfectadas = -1;
        String query = ACTUALIZAR_PROPUESTA; 
        Connection conexion = ManejadorBaseDeDatos.obtenerConexion();
        PreparedStatement declaracion = conexion.prepareStatement(query);
        declaracion.setString(1, propuestaColaboracion.getTipoColaboracion());
        declaracion.setString(2, propuestaColaboracion.getNombre());
        declaracion.setString(3, propuestaColaboracion.getObjetivoGeneral());
        declaracion.setString(4, propuestaColaboracion.getTemas());
        declaracion.setInt(5, propuestaColaboracion.getNumeroEstudiante());
        declaracion.setString(6, propuestaColaboracion.getInformacionAdicional());
        declaracion.setString(7, propuestaColaboracion.getPerfilDeLosEstudiantes());
        declaracion.setInt(8, propuestaColaboracion.getIdIdiomas());
        declaracion.setInt(9, propuestaColaboracion.getIdPeriodo());
        declaracion.setString(10, propuestaColaboracion.getEstadoPropuesta());
        declaracion.setInt(  11, idPropuesta);
        filasAfectadas = declaracion.executeUpdate();
        ManejadorBaseDeDatos.cerrarConexion();
       return filasAfectadas; 
       }
    
    /**
    * Evalúa una propuesta de colaboración actualizando su estado en la base de datos.
    *
    * @param idPropuesta El ID de la propuesta que se desea evaluar.
    * @param evaluacion La evaluación asignada a la propuesta.
    * @return El número de columnas afectadas por la operación de evaluación.
    * @throws SQLException Si ocurre un error al interactuar con la base de datos, por ejemplo:
    *                      - Cuando no se puede establecer conexión con la base de datos.
    *                      - Si los parámetros proporcionados son inválidos o nulos.
    *                      - Si la consulta SQL no se puede ejecutar correctamente.
    *                      - Si hay un problema al cerrar la conexión con la base de datos.
    */
    @Override
    public int evaluarPropuestaColaboracion(int idPropuesta, String evaluacion)throws SQLException {
        int columnaAfectada =  -1;
        String consulta = EVALUAR_PROPUESTA;
        Connection conexion = ManejadorBaseDeDatos.obtenerConexion();
        PreparedStatement declaracion = conexion.prepareStatement(consulta);
        declaracion.setString(1, evaluacion);
        declaracion.setInt(2, idPropuesta);
        columnaAfectada = declaracion.executeUpdate();
        ManejadorBaseDeDatos.cerrarConexion();
        return columnaAfectada;

    }
    
    /**
    * Revierte el estado de una propuesta de colaboración a "EnEspera".
    *
    * @param idPropuesta El ID de la propuesta cuyo estado se desea revertir.
    * @return El número de columnas afectadas por la operación.
    * @throws SQLException Si ocurre un error al interactuar con la base de datos, por ejemplo:
    *                      - Cuando no se puede establecer conexión con la base de datos.
    *                      - Si los parámetros proporcionados son inválidos o nulos.
    *                      - Si la consulta SQL no se puede ejecutar correctamente.
    *                      - Si hay un problema al cerrar la conexión con la base de datos.
    */
    @Override
    public int reevertirEstado(int idPropuesta)throws SQLException {
        int columnaAfectada =  -1;
        String consulta = EVALUAR_PROPUESTA;
        Connection conexion = ManejadorBaseDeDatos.obtenerConexion();
        PreparedStatement declaracion = conexion.prepareStatement(consulta);
        declaracion.setString(1, "EnEspera");
        declaracion.setInt(2, idPropuesta);
        columnaAfectada = declaracion.executeUpdate();
        ManejadorBaseDeDatos.cerrarConexion();
        return columnaAfectada;

    }
 
    /**
     * Consulta todas las propuestas de colaboración que están en espera.
     *
     * @return Una lista de propuestas de colaboración en espera.
    * @throws SQLException Si ocurre un error al interactuar con la base de datos, por ejemplo:
    *                      - Cuando no se puede establecer conexión con la base de datos.
    *                      - Si la consulta SQL no se puede ejecutar correctamente.
    *                      - Si hay un problema al cerrar la conexión con la base de datos.
     */
    @Override
    public List<PropuestaColaboracion> consultarTodasLasPropuestasColaboracionEnEspera() throws SQLException {
        List<PropuestaColaboracion> propuestas = new ArrayList<>();
        Connection conexion = ManejadorBaseDeDatos.obtenerConexion();
        PreparedStatement declaracion = conexion.prepareStatement(CONSULTAR_TODAS_LAS_PROPUESTAS_EN_ESPERA);
        ResultSet resultado = declaracion.executeQuery();
        while (resultado.next()) {
            propuestas.add(obtenerPropuesta(resultado));
        }
        ManejadorBaseDeDatos.cerrarConexion();
        return propuestas;
    }

    /**
     * Consulta todas las propuestas de colaboración que han sido aceptadas.
     *
     * @return Una lista de propuestas de colaboración aceptadas.
    * @throws SQLException Si ocurre un error al interactuar con la base de datos, por ejemplo:
    *                      - Cuando no se puede establecer conexión con la base de datos.
    *                      - Si la consulta SQL no se puede ejecutar correctamente.
    *                      - Si hay un problema al cerrar la conexión con la base de datos.
     */
    @Override
    public List<PropuestaColaboracion> consultarTodasLasPropuestasColaboracionAceptadas() throws SQLException {
        List<PropuestaColaboracion> propuestas = new ArrayList<>();
        Connection conexion = ManejadorBaseDeDatos.obtenerConexion();
        PreparedStatement declaracion = conexion.prepareStatement(CONSULTAR_TODAS_LAS_PROPUESTAS_ACEPTADAS);
        ResultSet resultado = declaracion.executeQuery();
        while (resultado.next()) {
            propuestas.add(obtenerPropuesta(resultado));
        }
        ManejadorBaseDeDatos.cerrarConexion();
        return propuestas;
    }
    
    /**
    * Consulta todas las propuestas de colaboración ofertadas por un profesor específico.
    *
    * @param idProfesor El ID del profesor cuyas propuestas ofertadas se desean consultar.
    * @return Una lista de propuestas de colaboración ofertadas por el profesor.
    * @throws SQLException Si ocurre un error al interactuar con la base de datos, por ejemplo:
    *                      - Cuando no se puede establecer conexión con la base de datos.
    *                      - Si los parámetros proporcionados son inválidos o nulos.
    *                      - Si la consulta SQL no se puede ejecutar correctamente.
    *                      - Si hay un problema al cerrar la conexión con la base de datos.
    */
   @Override
    public List<PropuestaColaboracion> consultarTodasLasPropuestasColaboracionOfertadas(int idProfesor) throws SQLException {
        List<PropuestaColaboracion> propuestas = new ArrayList<>();
        Connection conexion = ManejadorBaseDeDatos.obtenerConexion();
        PreparedStatement declaracion = conexion.prepareStatement(CONSULTAR_TODAS_LAS_PROPUESTAS_OFERTADAS);
        declaracion.setInt(1, idProfesor);
        ResultSet resultado = declaracion.executeQuery();
        while (resultado.next()) {
            propuestas.add(obtenerPropuesta(resultado));
        }
        ManejadorBaseDeDatos.cerrarConexion();
        return propuestas;
    }

    /**
    * Consulta todas las propuestas de colaboración realizadas por un profesor específico.
    *
    * @param idProfesor El ID del profesor cuyas propuestas se desean consultar.
    * @return Una lista de propuestas de colaboración realizadas por el profesor.
    * @throws SQLException Si ocurre un error al interactuar con la base de datos, por ejemplo:
    *                      - Cuando no se puede establecer conexión con la base de datos.
    *                      - Si los parámetros proporcionados son inválidos o nulos.
    *                      - Si la consulta SQL no se puede ejecutar correctamente.
    *                      - Si hay un problema al cerrar la conexión con la base de datos.
    */
    @Override
    public List<PropuestaColaboracion> consultarPropuestasColaboracionPorProfesor(int idProfesor) throws SQLException {
        
        String consulta = CONSULTAR_PROPUESTAS_POR_PROFESOR;
        Connection conexion = ManejadorBaseDeDatos.obtenerConexion();
        PreparedStatement declaracion = conexion.prepareStatement(consulta);
        declaracion.setInt(1, idProfesor);
        ResultSet resultado = declaracion.executeQuery();
        List<PropuestaColaboracion> listaPropuestas = new ArrayList<>();
        listaPropuestas = obtenerListPropuestaColaboracion(resultado);
        ManejadorBaseDeDatos.cerrarConexion();
        return listaPropuestas;
    }
    
    /**
     * Obtiene una propuesta de colaboración específica por su ID.
     *
     * @param idPropuesta El ID de la propuesta que se desea obtener.
     * @return La propuesta de colaboración correspondiente al ID proporcionado.
    * @throws SQLException Si ocurre un error al interactuar con la base de datos, por ejemplo:
    *                      - Cuando no se puede establecer conexión con la base de datos.
    *                      - Si los parámetros proporcionados son inválidos o nulos.
    *                      - Si la consulta SQL no se puede ejecutar correctamente.
    *                      - Si hay un problema al cerrar la conexión con la base de datos.
     */
    @Override
    public PropuestaColaboracion obtenerPropuestasColaboracionPorIdPropuesta(int idPropuesta) throws SQLException {
       PropuestaColaboracion propuestaColaboracion = new PropuestaColaboracion();
       String consulta = CONSULTAR_PROPUESTA_POR_IDPROPUESTA;
        Connection conexion = ManejadorBaseDeDatos.obtenerConexion();
        PreparedStatement declaracion = conexion.prepareStatement(consulta);
        declaracion.setInt(1, idPropuesta);
        ResultSet resultado = declaracion.executeQuery();
        if (resultado.next()) {
            propuestaColaboracion = obtenerPropuesta(resultado);
        }
        ManejadorBaseDeDatos.cerrarConexion();
        
        return propuestaColaboracion;
    }
    
    /**
     * Método auxiliar para obtener una Propuesta de Colaboración a partir de un ResultSet.
     * 
     * @param result El ResultSet que contiene los datos de la Propuesta de Colaboración.
     * @return Un objeto PropuestaColaboracion extraído del ResultSet.
     * @throws SQLException Si ocurre algún error de SQL durante la operación, como problemas de conexión
     *                      o errores al acceder a los datos en el ResultSet.
     */
    private List<PropuestaColaboracion> obtenerListPropuestaColaboracion(ResultSet result) throws SQLException {
        List<PropuestaColaboracion> listaPropuestaColaboracion = new ArrayList<>();
        
        while (result.next()) {
            PropuestaColaboracion propuestaColaboracion = new PropuestaColaboracion();
            propuestaColaboracion.setIdPropuestaColaboracion(result.getInt("idPropuestaColaboracion"));
            propuestaColaboracion.setTipoColaboracion(result.getString("tipoColaboracion"));
            propuestaColaboracion.setNombre(result.getString("nombre"));
            propuestaColaboracion.setObjetivoGeneral(result.getString("objetivoGeneral"));
            propuestaColaboracion.setTemas(result.getString("temas"));
            propuestaColaboracion.setNumeroEstudiante(result.getInt("numeroEstudiante"));
            propuestaColaboracion.setInformacionAdicional(result.getString("informacionAdicional"));
            propuestaColaboracion.setPerfilDeLosEstudiantes(result.getString("perfilDeLosEstudiantes"));
            propuestaColaboracion.setIdIdiomas(result.getInt("idIdiomas"));
            propuestaColaboracion.setIdPeriodo(result.getInt("idPeriodo"));
            propuestaColaboracion.setIdProfesor(result.getInt("Profesor_idProfesor"));
            propuestaColaboracion.setEstadoPropuesta(result.getString("estadoPropuesta"));
            listaPropuestaColaboracion.add(propuestaColaboracion);
        }
        return listaPropuestaColaboracion;
    }

    private PropuestaColaboracion obtenerPropuesta(ResultSet result) throws SQLException {
        PropuestaColaboracion propuestaColaboracion = new PropuestaColaboracion();
            propuestaColaboracion.setIdPropuestaColaboracion(result.getInt("idPropuestaColaboracion"));
            propuestaColaboracion.setTipoColaboracion(result.getString("tipoColaboracion"));
            propuestaColaboracion.setNombre(result.getString("nombre"));
            propuestaColaboracion.setObjetivoGeneral(result.getString("objetivoGeneral"));
            propuestaColaboracion.setTemas(result.getString("temas"));
            propuestaColaboracion.setNumeroEstudiante(result.getInt("numeroEstudiante"));
            propuestaColaboracion.setInformacionAdicional(result.getString("informacionAdicional"));
            propuestaColaboracion.setPerfilDeLosEstudiantes(result.getString("perfilDeLosEstudiantes"));
            propuestaColaboracion.setIdIdiomas(result.getInt("idIdiomas"));
            propuestaColaboracion.setIdPeriodo(result.getInt("idPeriodo"));
            propuestaColaboracion.setIdProfesor(result.getInt("Profesor_idProfesor"));
            propuestaColaboracion.setEstadoPropuesta(result.getString("estadoPropuesta"));
        return propuestaColaboracion;
    }
    
    /**
    * Obtiene una lista de nombres de profesores por su ID.
    *
    * @return Una lista de listas que contienen los IDs y nombres completos de los profesores.
    * @throws SQLException Si ocurre un error al interactuar con la base de datos, por ejemplo:
    *                      - Cuando no se puede establecer conexión con la base de datos.
    *                      - Si la consulta SQL no se puede ejecutar correctamente.
    *                      - Si hay un problema al cerrar la conexión con la base de datos.
    */
    @Override
   public List<List<String>> obtenerListaDeNomnbreProfesorPorIdProfesor () throws SQLException{
    List<List<String>> listaDeProfesores = new ArrayList<>();
         String consulta = CONSULTAR_LISRTA_NOMBRES_PROFESORES;
         try (Connection conexion = ManejadorBaseDeDatos.obtenerConexion()){
         PreparedStatement declaracion = conexion.prepareStatement(consulta);
         ResultSet resultado = declaracion.executeQuery(); 
            while (resultado.next()) {
                int idProfesor = resultado.getInt("idProfesor");
                String nombreCompleto = resultado.getString("nombreCompleto");
                List<String> Profesor = new ArrayList<>();
                Profesor.add(Integer.toString(idProfesor));
                Profesor.add(nombreCompleto);
                listaDeProfesores.add(Profesor);
        }  
        } catch (SQLException ex) {
            LOG.error(ex);
        }  
        return listaDeProfesores;
    }
   
    /**
    * Obtiene un profesor específico por su ID.
    *
    * @param idProfesor El ID del profesor que se desea obtener.
    * @return El profesor correspondiente al ID proporcionado.
    * @throws SQLException Si ocurre un error al interactuar con la base de datos, por ejemplo:
    *                      - Cuando no se puede establecer conexión con la base de datos.
    *                      - Si los parámetros proporcionados son inválidos o nulos.
    *                      - Si la consulta SQL no se puede ejecutar correctamente.
    *                      - Si hay un problema al cerrar la conexión con la base de datos.
    */
    @Override
    public Profesor obtenerProfesorPorid (int idProfesor) throws SQLException {
        Profesor profesor = new Profesor();
        String consulta = CONSULTAR_PROFESOR_POR_ID;
        Connection conexion = ManejadorBaseDeDatos.obtenerConexion();
        PreparedStatement declaracion = conexion.prepareStatement(consulta);
        declaracion.setInt(1, idProfesor);
        ResultSet resultado = declaracion.executeQuery();

        if (resultado.next()) {
            String nombreCompleto = resultado.getString("nombre_completo");
            String nombreInstitucion = resultado.getString("nombre_institucion");
            profesor.setIdProfesor(idProfesor);
            profesor.setNombre(nombreCompleto);
            profesor.setClaveInstitucional(nombreInstitucion);
        }
        ManejadorBaseDeDatos.cerrarConexion();
        return profesor;
    }
    
    /**
     * Obtiene el correo electrónico de un profesor asociado a una propuesta específica.
     *
     * @param idPropuesta El ID de la propuesta cuyo correo del profesor se desea obtener.
     * @return El correo electrónico del profesor.
     */
    @Override
    public String obtenerCorreoPorIdPropuesta(int idPropuesta) {
        String correo = "";
        try (Connection conexion = ManejadorBaseDeDatos.obtenerConexion()) {
            PreparedStatement declaracion=  conexion.prepareStatement(CONSULTAR_CORREO_PROFESOR);
            declaracion.setInt(1, idPropuesta);
            ResultSet resuldato = declaracion.executeQuery();
            if (resuldato.next()) {
                correo = resuldato.getString("correo");
            }
        } catch (SQLException ex) {
            LOG.error(ex);
        }
        return correo;
    }

    /**
    * Verifica si un profesor tiene propuestas de colaboración ofertadas.
    *
    * @param idProfesor El ID del profesor cuya oferta de colaboraciones se desea verificar.
    * @return True si el profesor tiene colaboraciones ofertadas, false en caso contrario.
    * @throws SQLException Si ocurre un error al interactuar con la base de datos, por ejemplo:
    *                      - Cuando no se puede establecer conexión con la base de datos.
    *                      - Si los parámetros proporcionados son inválidos o nulos.
    *                      - Si la consulta SQL no se puede ejecutar correctamente.
    *                      - Si hay un problema al cerrar la conexión con la base de datos.
    */

    @Override
    public boolean verificarEstadoPropuestaColaboracion(int idProfesor) throws SQLException {
        boolean hayColaboracionesOfertadas  = false;
        String consulta = CONSULTAR_ESTADO_PROPUESTA;

        try (Connection conexion = ManejadorBaseDeDatos.obtenerConexion();
             PreparedStatement declaracion = conexion.prepareStatement(consulta)) {
            declaracion.setInt(1, idProfesor);
            try (ResultSet resultado = declaracion.executeQuery()) {
                if (resultado.next()) {
                int colaboracionesOfertada = resultado.getInt("propuestas");
                hayColaboracionesOfertadas = (colaboracionesOfertada > 0);
                }
            }
        }
        return hayColaboracionesOfertadas;
    }

}
