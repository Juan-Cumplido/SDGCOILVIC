package sdgcoilvic.logicaDeNegocio.implementacionDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import sdgcoilvic.accesoADatos.ManejadorBaseDeDatos;
import sdgcoilvic.logicaDeNegocio.clases.ActividadColaborativa;
import sdgcoilvic.logicaDeNegocio.interfaces.IActividadColaborativa;

public class ActividadColaborativaDAO implements IActividadColaborativa {
    private static final String INSERTAR_ACTIVIDAD_COLABORATIVA = """
            INSERT INTO actividad_colaborativa ( nombreActividad, instruccion,  idColaboracion, fechaInicio, 
                                                fechaCierre, herramienta,  estadoActividad, Profesor_idProfesor)
                                                VALUES (?, ?, ?, ?, ?, ?, ?, ?);""";
    private static final String CONSULTAR_ACTIVIDAD_COLABORATIVA = """
                                                                   SELECT * FROM actividad_colaborativa
                                                                   WHERE idActividadColaborativa = ?;""";
    private static final String CONSULTAR_ACTIVIDADES_COLABORATIVAS = "SELECT * FROM Actividad_Colaborativa";
    private static final String CONSULTAR_ACTIVIDADES_POR_IDCOLABORACION = """
                                                                           SELECT * 
                                                                           FROM  Actividad_Colaborativa
                                                                           WHERE idColaboracion = ?;""";
    private static final String VERIFICAR_DUENIO_ACTIVIDAD = """
                                                             SELECT idActividadColaborativa
                                                             FROM actividad_colaborativa
                                                             WHERE Profesor_idProfesor = ?;""";
    private static final String ACTUALIZAR_ACTIVIDAD = "UPDATE actividad_Colaborativa SET nombreActividad = ?, instruccion = ?, fechaInicio = ?, fechaCierre = ?, " +
            "herramienta = ?, estadoActividad = ? WHERE idActividadColaborativa = ?";
    private static final String VERIFICAR_ACTIVIDADES_EN_LA_COLABORACION = """
                                                                               SELECT COUNT(*) AS total_finalizadas
                                                                           FROM actividad_colaborativa
                                                                           WHERE idColaboracion = ? AND estadoActividad = 'Finalizada';""";

    /**
     * Agrega una nueva actividad colaborativa a la base de datos.
     *
     * @param actividadColaborativa La actividad colaborativa a agregar.
     * @return El número de filas afectadas por la inserción, o -1 si ocurre un error.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos, por ejemplo:
     *                      - Cuando no se puede establecer conexión con la base de datos.
     *                      - Si los parámetros proporcionados son inválidos o nulos.
     *                      - Si la consulta SQL no se puede ejecutar correctamente.
     *                      - Si hay un problema al cerrar la conexión con la base de datos.
     */
    @Override
    public int agregarActividadColaborativa(ActividadColaborativa actividadColaborativa) throws SQLException{
        int columnaAfectada = -1;
        String consulta = INSERTAR_ACTIVIDAD_COLABORATIVA;
        try (Connection conexion = ManejadorBaseDeDatos.obtenerConexion();
            PreparedStatement prepararDeclaracion = conexion.prepareStatement(consulta, PreparedStatement.RETURN_GENERATED_KEYS)) {
            prepararDeclaracion.setString(1, actividadColaborativa.getNombreActividad());
            prepararDeclaracion.setString(2, actividadColaborativa.getInstruccion());
            prepararDeclaracion.setInt(3, actividadColaborativa.getIdColaboracion());
            prepararDeclaracion.setDate(4, java.sql.Date.valueOf(actividadColaborativa.getFechaInicio()));
            prepararDeclaracion.setDate(5, java.sql.Date.valueOf(actividadColaborativa.getFechaCierre()));
            prepararDeclaracion.setString(6, actividadColaborativa.getHerramienta());
            prepararDeclaracion.setString(7, actividadColaborativa.getEstadoActividad());
            prepararDeclaracion.setInt(8, actividadColaborativa.getIdProfesor());
            columnaAfectada = prepararDeclaracion.executeUpdate();

        }
        
        return columnaAfectada;
    }
    
    /**
    * Actualiza la información de una actividad colaborativa en la base de datos.
    *
    * @param actividadColaborativa La actividad colaborativa con la información actualizada.
    * @param idActividad El ID de la actividad colaborativa a actualizar.
    * @return El número de filas afectadas por la actualización, o -1 si ocurre un error.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos, por ejemplo:
     *                      - Cuando no se puede establecer conexión con la base de datos.
     *                      - Si los parámetros proporcionados son inválidos o nulos.
     *                      - Si la consulta SQL no se puede ejecutar correctamente.
     *                      - Si hay un problema al cerrar la conexión con la base de datos.
    */
    @Override
    public int actualizarInformacionDeLaActividad(ActividadColaborativa actividadColaborativa, int  idActividad) throws SQLException{
        int resutado = -1;
        String consulta = ACTUALIZAR_ACTIVIDAD;
        Connection conexion = ManejadorBaseDeDatos.obtenerConexion();
        PreparedStatement declaracion = conexion.prepareStatement(consulta);
        declaracion.setString(1, actividadColaborativa.getNombreActividad());
        declaracion.setString(2, actividadColaborativa.getInstruccion());
        declaracion.setString(3, actividadColaborativa.getFechaInicio());
        declaracion.setString(4, actividadColaborativa.getFechaCierre());
        declaracion.setString(5, actividadColaborativa.getHerramienta());
        declaracion.setString(6, actividadColaborativa.getEstadoActividad());
        declaracion.setInt(7, idActividad);
        resutado = declaracion.executeUpdate();
        ManejadorBaseDeDatos.cerrarConexion();
        return resutado; 
    }
    
    /**
     * Consulta la información de una actividad colaborativa en la base de datos.
     *
     * @param idActividadColaborativa El ID de la actividad colaborativa a consultar.
     * @return La actividad colaborativa encontrada, o una instancia vacía si no se encuentra.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos, por ejemplo:
     *                      - Cuando no se puede establecer conexión con la base de datos.
     *                      - Si los parámetros proporcionados son inválidos o nulos.
     *                      - Si la consulta SQL no se puede ejecutar correctamente.
     *                      - Si hay un problema al cerrar la conexión con la base de datos.
     */
    @Override
    public ActividadColaborativa consultarActividadColaborativa(int idActividadColaborativa) throws SQLException {
        ActividadColaborativa actividad = new ActividadColaborativa();
        String consulta = CONSULTAR_ACTIVIDAD_COLABORATIVA;
        try (Connection conexion = ManejadorBaseDeDatos.obtenerConexion();
             PreparedStatement declaracion = conexion.prepareStatement(consulta)) {
             declaracion.setInt(1, idActividadColaborativa);
            try (ResultSet resultado = declaracion.executeQuery()) {
                if (resultado.next()) {
                    actividad = new ActividadColaborativa();
                    actividad.setIdActividadColaborativa(resultado.getInt("idActividadColaborativa"));
                    actividad.setNombreActividad(resultado.getString("nombreActividad"));
                    actividad.setInstruccion(resultado.getString("instruccion"));
                    actividad.setIdColaboracion(resultado.getInt("idColaboracion"));
                    actividad.setFechaInicio(resultado.getString("fechaInicio"));
                    actividad.setFechaCierre(resultado.getString("fechaCierre"));
                    actividad.setHerramienta(resultado.getString("herramienta"));
                    actividad.setEstadoActividad(resultado.getString("estadoActividad"));
                    actividad.setIdProfesor(resultado.getInt("Profesor_idProfesor"));
                }
            }
        } 
        return actividad;
    }

    /**
    * Obtiene todas las actividades colaborativas de la base de datos.
    *
    * @return Una lista de todas las actividades colaborativas disponibles.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos, por ejemplo:
     *                      - Cuando no se puede establecer conexión con la base de datos.
     *                      - Si la consulta SQL no se puede ejecutar correctamente.
     *                      - Si hay un problema al cerrar la conexión con la base de datos.
    */
    @Override
    public List<ActividadColaborativa> obtenerActividades() throws SQLException{
        List<ActividadColaborativa> actividades = new ArrayList<>();  
        String consulta = CONSULTAR_ACTIVIDADES_COLABORATIVAS;
        Connection conexion = ManejadorBaseDeDatos.obtenerConexion();
            PreparedStatement declaracion = conexion.prepareStatement(consulta);
            ResultSet actividadesObtenidas = declaracion.executeQuery();
            if(actividadesObtenidas.isBeforeFirst()){
                while(actividadesObtenidas.next()){
                    ActividadColaborativa actividadColaborativa = new ActividadColaborativa();
                    actividadColaborativa.setIdActividadColaborativa(actividadesObtenidas.getInt(1));
                    actividadColaborativa.setNombreActividad(actividadesObtenidas.getString(2));
                    actividadColaborativa.setInstruccion(actividadesObtenidas.getString(3));
                    actividadColaborativa.setIdColaboracion(actividadesObtenidas.getInt(4));
                    actividadColaborativa.setFechaInicio(actividadesObtenidas.getString(5));
                    actividadColaborativa.setFechaCierre(actividadesObtenidas.getString(6));
                    actividadColaborativa.setHerramienta(actividadesObtenidas.getString(7));
                    actividadColaborativa.setEstadoActividad(actividadesObtenidas.getString(8));
                    actividadColaborativa.setIdProfesor(actividadesObtenidas.getInt(9));
                    actividades.add(actividadColaborativa);
                }
            }
        return actividades;
    }
    
    /**
     * Obtiene todas las actividades colaborativas asociadas a una colaboración específica.
     *
     * @param idColaboracion El ID de la colaboración.
     * @return Una lista de actividades colaborativas asociadas a la colaboración especificada.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos, por ejemplo:
     *                      - Cuando no se puede establecer conexión con la base de datos.
     *                      - Si los parámetros proporcionados son inválidos o nulos.
     *                      - Si la consulta SQL no se puede ejecutar correctamente.
     *                      - Si hay un problema al cerrar la conexión con la base de datos.
     */
    @Override
    public List<ActividadColaborativa> obtenerActividadesColaborativas(int idColaboracion) throws SQLException{
        List<ActividadColaborativa> actividades = new ArrayList<>();  
        String consulta = CONSULTAR_ACTIVIDADES_POR_IDCOLABORACION;
        Connection conexion = ManejadorBaseDeDatos.obtenerConexion();
            PreparedStatement declaracion = conexion.prepareStatement(consulta);
            declaracion.setInt(1, idColaboracion);
            ResultSet actividadesObtenidas = declaracion.executeQuery();
            if(actividadesObtenidas.isBeforeFirst()){
                while(actividadesObtenidas.next()){
                    ActividadColaborativa actividadColaborativa = new ActividadColaborativa();
                    actividadColaborativa.setIdActividadColaborativa(actividadesObtenidas.getInt(1));
                    actividadColaborativa.setNombreActividad(actividadesObtenidas.getString(2));
                    actividadColaborativa.setInstruccion(actividadesObtenidas.getString(3));
                    actividadColaborativa.setIdColaboracion(actividadesObtenidas.getInt(4));
                    actividadColaborativa.setFechaInicio(actividadesObtenidas.getString(5));
                    actividadColaborativa.setFechaCierre(actividadesObtenidas.getString(6));
                    actividadColaborativa.setHerramienta(actividadesObtenidas.getString(7));
                    actividadColaborativa.setEstadoActividad(actividadesObtenidas.getString(8));
                    actividadColaborativa.setIdProfesor(actividadesObtenidas.getInt(9));
                    actividades.add(actividadColaborativa);
                }
            }
        return actividades;
    }
    
    /**
    * Verifica si un profesor es dueño de alguna actividad colaborativa.
    *
    * @param idProfesor El ID del profesor.
    * @return Una lista de ID de actividades colaborativas que son propiedad del profesor.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos, por ejemplo:
     *                      - Cuando no se puede establecer conexión con la base de datos.
     *                      - Si los parámetros proporcionados son inválidos o nulos.
     *                      - Si la consulta SQL no se puede ejecutar correctamente.
     *                      - Si hay un problema al cerrar la conexión con la base de datos.
    */
    @Override
    public List<Integer> verificarDuenioActividad(int idProfesor) throws SQLException {
        List<Integer> listaDeActividades = new ArrayList<>();
        String consulta = VERIFICAR_DUENIO_ACTIVIDAD;
    
        Connection conexion = ManejadorBaseDeDatos.obtenerConexion();
        PreparedStatement declaracion = conexion.prepareStatement(consulta);
        declaracion.setInt(1, idProfesor);
        ResultSet reusltado = declaracion.executeQuery();
        
        while (reusltado.next()) {
            int idActividadColaborativa = reusltado.getInt("idActividadColaborativa");
            listaDeActividades.add(idActividadColaborativa);
        }
        ManejadorBaseDeDatos.cerrarConexion();
        
        return listaDeActividades;
    }
    
    /**
     * Verifica si hay al menos 3 actividades finalizadas en una colaboración.
     *
     * @param idColaboracion El ID de la colaboración.
     * @return true si hay al menos 3 actividades finalizadas, false de lo contrario.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos, por ejemplo:
     *                      - Cuando no se puede establecer conexión con la base de datos.
     *                      - Si los parámetros proporcionados son inválidos o nulos.
     *                      - Si la consulta SQL no se puede ejecutar correctamente.
     *                      - Si hay un problema al cerrar la conexión con la base de datos.
     */
    @Override
    public boolean  verificarActividadesFinalizadas(int idColaboracion) throws SQLException {
        String consulta = VERIFICAR_ACTIVIDADES_EN_LA_COLABORACION;
    
        Connection conexion = ManejadorBaseDeDatos.obtenerConexion();
        PreparedStatement declaracion = conexion.prepareStatement(consulta);
        declaracion.setInt(1, idColaboracion);
        ResultSet resultado = declaracion.executeQuery();
        
            if (resultado.next()) {
                int totalFinalizadas = resultado.getInt("total_finalizadas");
                return totalFinalizadas >= 3;
            }
        ManejadorBaseDeDatos.cerrarConexion();
        
        return false;
    }
    
    
    
}