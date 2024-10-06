package sdgcoilvic.logicaDeNegocio.implementacionDAO;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import sdgcoilvic.accesoADatos.ManejadorBaseDeDatos;
import sdgcoilvic.logicaDeNegocio.clases.Colaboracion;
import sdgcoilvic.logicaDeNegocio.clases.PropuestaColaboracion;
import sdgcoilvic.logicaDeNegocio.clases.TablaColaboracion;
import sdgcoilvic.logicaDeNegocio.interfaces.IColaboracion;

public class ColaboracionDAO implements IColaboracion {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(ColaboracionDAO.class);
    
    private static final String INSERTAR_COLABORACION =  "{call agregarNuevaColaboracion(?, ?, ?, ?, ?, ?, ?, ?, ?)}";
    private static final String VINCULAR_PROFESORES_A_LA_COLABORACION =  "{CALL insertarProfesoresAceptados(?)}";
    private static final String CAMBIAR_ESTADO_PROFESOR_A_COLABORANDO = """
                                                    UPDATE profesor
                                                    SET estadoProfesor = 'Colaborando'
                                                    WHERE idProfesor IN (
                                                        SELECT Profesor_idProfesor
                                                        FROM Profesor_has_Colaboracion_CoilVic
                                                        WHERE Colaboracion_CoilVic_idColaboracion = ?
                                                    );""";
    private static final String RECHAZAR_TODAS_LAS_SOLICITUDES = "UPDATE solicitud_colaboracion " +
                      "SET estadoSolicitud = 'Rechazada' " +
                      "WHERE idSolicitudColaboracion IN (" +
                      "    SELECT idSolicitudColaboracion " +
                      "    FROM solicitud_propuesta " +
                      "    WHERE idPropuestaColaboracion = ? " +
                      ") AND estadoSolicitud = 'EnEspera'";
    private static final String FINALIZAR_COLABORACION = "UPDATE colaboracion_coilvic SET estadoColaboracion = 'Finalizada' WHERE idColaboracion = ?";
    private static final String CERRAR_COLABORACION =  "UPDATE colaboracion_coilvic SET estadoColaboracion = 'Cerrada' WHERE idColaboracion = ?";
    private static final String CAMBIAR_PROFESOR_A_ESTADO_ACTIVO = 
                                                    """
                                                    UPDATE profesor SET estadoProfesor = 'Activo'
                                                    WHERE idProfesor IN ( SELECT Profesor_idProfesor
                                                        FROM Profesor_has_Colaboracion_CoilVic
                                                        WHERE Colaboracion_CoilVic_idColaboracion = ?
                                                    );""";

    private static final String CONSULTAR_COLABORACION =  "SELECT * FROM colaboracion_coilvic WHERE idColaboracion = ?";
    private static final String CONSULTAR_TODAS_COLABORACIONES =  "SELECT * FROM colaboracion_coilvic";
    private static final String FILTRAR_COLABORACIONES =  "SELECT * FROM colaboracion_coilvic WHERE nombreCurso LIKE ? OR descripcion LIKE ?";
    private static final String OBTENER_IDCOLABORACION = """
                                                    SELECT c.idColaboracion
                                                    FROM colaboracion_coilvic c
                                                    JOIN propuesta_colaboracion p ON c.idPropuestaColaboracion = p.idPropuestaColaboracion
                                                    JOIN Profesor_has_Colaboracion_CoilVic phc ON c.idColaboracion = phc.Colaboracion_CoilVic_idColaboracion
                                                    WHERE phc.Profesor_idProfesor = ? 
                                                      AND c.estadoColaboracion = 'EnCurso';""";
    private static final String OBTENER_LISTA_NOMBRE_PROFESORES = """
                                                    SELECT CONCAT(p.nombre, ' ', p.apellidoPaterno, ' ', COALESCE(p.apellidoMaterno, '')) AS nombreCompleto
                                                    FROM Profesor p
                                                    JOIN Profesor_has_Colaboracion_CoilVic phc ON p.idProfesor = phc.Profesor_idProfesor
                                                    WHERE phc.Colaboracion_CoilVic_idColaboracion = ?;""";
    private static final String BUSCAR_DATOS_PROPUESTA = """
                                                    SELECT p.tipoColaboracion, p.objetivoGeneral, p.nombre
                                                    FROM colaboracion_coilvic c
                                                    JOIN propuesta_colaboracion p ON c.idPropuestaColaboracion = p.idPropuestaColaboracion
                                                    WHERE c.idColaboracion = ?;""";
    private static final String CONSULTAR_COLABORACIONES_COILVIC = "SELECT " +
                                                    "c.idColaboracion, " +
                                                    "pc.nombre AS nombreColaboracion, " +
                                                    "pc.tipoColaboracion, " +
                                                    "CONCAT(p.nombrePeriodo, ' (', p.fechaInicio, ' - ', p.fechaFin, ')') AS periodo, " +
                                                    "(SELECT COUNT(*) FROM actividad_colaborativa ac WHERE ac.idColaboracion = c.idColaboracion) AS numeroActividades, " +
                                                    "(SELECT COUNT(*) FROM evidencia e " +
                                                    "JOIN actividad_colaborativa ac ON e.idActividadColaborativa = ac.idActividadColaborativa " +
                                                    "WHERE ac.idColaboracion = c.idColaboracion) AS numeroEvidencias, " +
                                                    "c.estadoColaboracion " +
                                                    "FROM colaboracion_coilvic c " +
                                                    "JOIN propuesta_colaboracion pc ON c.idPropuestaColaboracion = pc.idPropuestaColaboracion " +
                                                    "JOIN periodo p ON c.idPeriodo = p.idPeriodo;";
    
     private static final String CONSULTAR_DETALLES_ESPECIFICOS = "SELECT idiomas.nombreIdioma, periodo.fechaInicio, periodo.fechaFin "
                                                    + "FROM colaboracion_coilvic "
                                                    + "JOIN propuesta_Colaboracion ON colaboracion_coilvic.idPropuestaColaboracion = propuesta_Colaboracion.idPropuestaColaboracion "
                                                    + "JOIN idiomas ON propuesta_Colaboracion.idIdiomas = idiomas.idIdiomas "
                                                    + "JOIN periodo ON propuesta_Colaboracion.idPeriodo = periodo.idPeriodo "
                                                    + "WHERE colaboracion_coilvic.idColaboracion = ?";

     /**
    * Crea una nueva colaboración en la base de datos.
    *
    * @param colaboracion La colaboración a crear.
    * @param idProfesor El ID del profesor asociado a la colaboración.
    * @return El ID de la colaboración creada, o -1 si ocurre un error.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos, por ejemplo:
     *                      - Cuando no se puede establecer conexión con la base de datos.
     *                      - Si los parámetros proporcionados son inválidos o nulos.
     *                      - Si la consulta SQL no se puede ejecutar correctamente.
     *                      - Si hay un problema al cerrar la conexión con la base de datos.
    */
    @Override
    public int crearColaboracion(Colaboracion colaboracion, int idProfesor) throws SQLException {
        int idColaboracion = -1;
        try (Connection conexion = ManejadorBaseDeDatos.obtenerConexion();
            CallableStatement declaraciónInvocable = conexion.prepareCall(INSERTAR_COLABORACION)){
     
            declaraciónInvocable.setInt(1, idProfesor); 
            declaraciónInvocable.setInt(2, colaboracion.getIdPropuestaColaboracion());
            declaraciónInvocable.setString(3, colaboracion.getDescripcion());
            declaraciónInvocable.setString(4, colaboracion.getRecursos());
            declaraciónInvocable.setString(5, colaboracion.getAprendizajesEsperados());
            declaraciónInvocable.setString(6, colaboracion.getDetallesAsistenciaParticipacion());
            declaraciónInvocable.setString(7, colaboracion.getDetallesEvaluacion());
            declaraciónInvocable.setString(8, colaboracion.getDetallesEntorno());
            declaraciónInvocable.registerOutParameter(9, java.sql.Types.INTEGER); 

            declaraciónInvocable.execute();
            idColaboracion = declaraciónInvocable.getInt(9);
        }
        return idColaboracion;
    }
    
    /**
    * Vincula profesores a una colaboración específica en la base de datos.
    *
    * @param idColaboracion El ID de la colaboración.
    * @return El número de filas afectadas por la vinculación, o -1 si ocurre un error.
    */
    @Override
    public int vincularProfesoresALaColaboracion(int idColaboracion) {
        int resultado = -1;
        try ( Connection conexion = ManejadorBaseDeDatos.obtenerConexion();
            CallableStatement declaraciónInvocable = conexion.prepareCall(VINCULAR_PROFESORES_A_LA_COLABORACION)){
            declaraciónInvocable.setInt(1, idColaboracion);
            resultado = declaraciónInvocable.executeUpdate();
            declaraciónInvocable.close();
            conexion.close();
        } catch (SQLException ex) {
            LOG.error(ex);
        }
        return resultado;
    }

    /**
    * Cambia el estado de un profesor a "colaborando" en una colaboración específica en la base de datos.
    *
    * @param idColaboracion El ID de la colaboración.
    * @return El número de filas afectadas por el cambio de estado, o -1 si ocurre un error.
    */
    @Override
    public int cambiarEstadoProfesorAColaborando(int idColaboracion) {
        int resultado = -1;
        try (Connection conexion = ManejadorBaseDeDatos.obtenerConexion();
            CallableStatement declaraciónInvocable = conexion.prepareCall(CAMBIAR_ESTADO_PROFESOR_A_COLABORANDO)){
            declaraciónInvocable.setInt(1, idColaboracion);
            resultado = declaraciónInvocable.executeUpdate();
            declaraciónInvocable.close();
        } catch (SQLException ex) {
            LOG.error(ex);
        }
        return resultado;
    }
    
    /**
     * Rechaza todas las solicitudes asociadas a una propuesta de colaboración en la base de datos.
     *
     * @param idPropuesta El ID de la propuesta de colaboración.
     * @return El número de filas afectadas por el rechazo, o -1 si ocurre un error.
     */
    @Override
    public int rechazarTodasLasSolicitudes(int idPropuesta) {
        int resultado = -1;
        try (Connection conexion = ManejadorBaseDeDatos.obtenerConexion();
            PreparedStatement consulta = conexion.prepareStatement(RECHAZAR_TODAS_LAS_SOLICITUDES)){
            consulta.setInt(1, idPropuesta);
            resultado = consulta.executeUpdate();
        } catch (SQLException ex) {
            LOG.error(ex);
        }
        return resultado;
    }
    
    /**
     * Obtiene los nombres de los profesores asociados a una colaboración en la base de datos.
     *
     * @param idColaboracion El ID de la colaboración.
     * @return Una lista de nombres de profesores asociados a la colaboración.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos, por ejemplo:
     *                      - Cuando no se puede establecer conexión con la base de datos.
     *                      - Si los parámetros proporcionados son inválidos o nulos.
     *                      - Si la consulta SQL no se puede ejecutar correctamente.
     *                      - Si hay un problema al cerrar la conexión con la base de datos.
     */
    @Override
    public List<String> obtenerNombreProfesores(int idColaboracion) throws SQLException {
         List<String> nombresProfesores = new ArrayList<>();
         try (Connection conexion = ManejadorBaseDeDatos.obtenerConexion();
         PreparedStatement declaracion = conexion.prepareStatement(OBTENER_LISTA_NOMBRE_PROFESORES)){
         declaracion.setInt(1, idColaboracion);
         ResultSet resultado = declaracion.executeQuery();
                while (resultado.next()) {
                    nombresProfesores.add(resultado.getString("nombreCompleto"));
                }
                
         }
        return nombresProfesores;
    }
    
    /**
     * Obtiene el ID de la colaboración en curso asociado a un profesor en la base de datos.
     *
     * @param idProfesor El ID del profesor.
     * @return El ID de la colaboración en curso del profesor, o -1 si no hay ninguna.
     */
    @Override
    public int obtenerIdColaboracionEnCurso(int idProfesor){
      int idColaboracion = -1;
        try (Connection conexion = ManejadorBaseDeDatos.obtenerConexion();
            PreparedStatement declaracion = conexion.prepareStatement(OBTENER_IDCOLABORACION)){
            declaracion.setInt(1, idProfesor);
            ResultSet resultado = declaracion.executeQuery();
            if (resultado.next()) {
               idColaboracion = resultado.getInt("idColaboracion");
            }
        } catch (SQLException ex) {
            LOG.error(ex);
        }
        
        return idColaboracion;
    }
    
    /**
     * Obtiene la propuesta de colaboración asociada a una colaboración en la base de datos.
     *
     * @param idColaboracion El ID de la colaboración.
     * @return La propuesta de colaboración asociada a la colaboración.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos, por ejemplo:
     *                      - Cuando no se puede establecer conexión con la base de datos.
     *                      - Si los parámetros proporcionados son inválidos o nulos.
     *                      - Si la consulta SQL no se puede ejecutar correctamente.
     *                      - Si hay un problema al cerrar la conexión con la base de datos.
     */
    @Override
    public PropuestaColaboracion obtenerPropuestaColaboracion(int idColaboracion) throws SQLException {
        PropuestaColaboracion propuesta = null;
        
        try (Connection conexion = ManejadorBaseDeDatos.obtenerConexion();
            PreparedStatement declaracion = conexion.prepareStatement(BUSCAR_DATOS_PROPUESTA)) {
            declaracion.setInt(1, idColaboracion);
            try (ResultSet resultdo = declaracion.executeQuery()) {
                if (resultdo.next()) {
                    propuesta = new PropuestaColaboracion();
                    propuesta.setTipoColaboracion(resultdo.getString("tipoColaboracion"));
                    propuesta.setObjetivoGeneral(resultdo.getString("objetivoGeneral"));
                    propuesta.setNombre(resultdo.getString("nombre"));
                }
            }
        }
        
        return propuesta;
    }

    /**
    * Finaliza una colaboración en la base de datos.
    *
    * @param idColaboracion El ID de la colaboración.
    * @return El número de filas afectadas por la finalización, o -1 si ocurre un error.
    */
    @Override
    public int finalizarColaboracion(int idColaboracion) {
           int resultado = -1;
        try (Connection conexion = ManejadorBaseDeDatos.obtenerConexion();
            PreparedStatement declaracion = conexion.prepareStatement(FINALIZAR_COLABORACION)){
            declaracion.setInt(1, idColaboracion);
            resultado = declaracion.executeUpdate();
            declaracion.close();
            conexion.close();
        } catch (SQLException ex) {
            LOG.error(ex);
        }
        return resultado;
    }

    /**
    * Cierra una colaboración en la base de datos.
    *
    * @param idColaboracion El ID de la colaboración.
    * @return El número de filas afectadas por el cierre, o -1 si ocurre un error.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos, por ejemplo:
     *                      - Cuando no se puede establecer conexión con la base de datos.
     *                      - Si los parámetros proporcionados son inválidos o nulos.
     *                      - Si la consulta SQL no se puede ejecutar correctamente.
     *                      - Si hay un problema al cerrar la conexión con la base de datos.
    */
    @Override
    public int cerrarColaboracion(int idColaboracion) throws SQLException {
       int resultado = -1;
        try {
            Connection conexion = ManejadorBaseDeDatos.obtenerConexion();
            conexion.setAutoCommit(false);
            PreparedStatement declaracion = conexion.prepareStatement(CERRAR_COLABORACION);
            declaracion.setInt(1, idColaboracion);
            resultado = declaracion.executeUpdate();
            
            PreparedStatement declaracionProfesor = conexion.prepareStatement(CAMBIAR_PROFESOR_A_ESTADO_ACTIVO);
            declaracionProfesor.setInt(1, idColaboracion);
            resultado += declaracionProfesor.executeUpdate();
            conexion.commit();
        } catch (SQLException ex) {
            ManejadorBaseDeDatos.obtenerConexion().rollback();
        } finally {
           ManejadorBaseDeDatos.obtenerConexion().close();
       }
        return resultado;
    }
   
    /**
    * Consulta una colaboración en la base de datos.
    *
    * @param idColaboracion El ID de la colaboración.
    * @return La colaboración consultada, o null si no se encuentra.
    */
    @Override
    public Colaboracion consultarColaboracion(int idColaboracion) {
        Colaboracion colaboracion = null;
        try (Connection conexion = ManejadorBaseDeDatos.obtenerConexion();
             PreparedStatement declaracion = conexion.prepareStatement(CONSULTAR_COLABORACION)) {
            declaracion.setInt(1, idColaboracion);
            try (ResultSet resultado = declaracion.executeQuery()) {
                if (resultado.next()) {
                    colaboracion = new Colaboracion();
                    colaboracion.setIdColaboracion(resultado.getInt("idColaboracion"));
                    colaboracion.setDescripcion(resultado.getString("descripcion"));
                    colaboracion.setRecursos(resultado.getString("recursos"));
                    colaboracion.setIdPeriodo(resultado.getInt("idPeriodo"));
                    colaboracion.setAprendizajesEsperados(resultado.getString("aprendizajesEsperados"));
                    colaboracion.setDetallesAsistenciaParticipacion(resultado.getString("detallesAsistenciaParticipacion"));
                    colaboracion.setDetallesEvaluacion(resultado.getString("detallesEvaluacion"));
                    colaboracion.setDetallesEntorno(resultado.getString("detallesEntorno"));
                    colaboracion.setIdPropuestaColaboracion(resultado.getInt("idPropuestaColaboracion"));
                    colaboracion.setEstadoColaboracion(resultado.getString("estadoColaboracion"));
                }
            }
        } catch (SQLException ex) {
            LOG.error(ex);
        }
        return colaboracion;
    }

    /**
    * Consulta todas las colaboraciones en la base de datos.
    *
    * @return Una lista de todas las colaboraciones.
    */
    @Override
    public List<Colaboracion> consultarTodasColaboraciones() {
        List<Colaboracion> colaboraciones = new ArrayList<>();
        try (Connection conexion = ManejadorBaseDeDatos.obtenerConexion();
             PreparedStatement declaracion = conexion.prepareStatement(CONSULTAR_TODAS_COLABORACIONES);
             ResultSet resultado = declaracion.executeQuery()) {
            while (resultado.next()) {
                Colaboracion colaboracion = new Colaboracion();
                colaboracion.setIdColaboracion(resultado.getInt("idColaboracion"));
                colaboracion.setDescripcion(resultado.getString("descripcion"));
                colaboracion.setRecursos(resultado.getString("recursos"));
                colaboracion.setIdPeriodo(resultado.getInt("idPeriodo"));
                colaboracion.setAprendizajesEsperados(resultado.getString("aprendizajesEsperados"));
                colaboracion.setDetallesAsistenciaParticipacion(resultado.getString("detallesAsistenciaParticipacion"));
                colaboracion.setDetallesEvaluacion(resultado.getString("detallesEvaluacion"));
                colaboracion.setDetallesEntorno(resultado.getString("detallesEntorno"));
                colaboracion.setIdPropuestaColaboracion(resultado.getInt("Propuesta_Colaboracion_idPropuestaColaboracion"));
                colaboraciones.add(colaboracion);
            }
        } catch (SQLException ex) {
            LOG.error(ex);
        }
        return colaboraciones;
    }
    
    /**
    * Consulta todas las colaboraciones en formato específico en la base de datos.
    *
    * @return Una lista de todas las colaboraciones en formato específico.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos, por ejemplo:
     *                      - Cuando no se puede establecer conexión con la base de datos.
     *                      - Si los parámetros proporcionados son inválidos o nulos.
     *                      - Si la consulta SQL no se puede ejecutar correctamente.
     *                      - Si hay un problema al cerrar la conexión con la base de datos.
    */
    @Override
    public List<TablaColaboracion> consultarTodasColaboracionesCoilVic() throws SQLException{
        List<TablaColaboracion> colaboraciones = new ArrayList<>();
        try (Connection conexion = ManejadorBaseDeDatos.obtenerConexion();
             PreparedStatement prepararDeclaracion = conexion.prepareStatement(CONSULTAR_COLABORACIONES_COILVIC);
             ResultSet resultado = prepararDeclaracion.executeQuery()) {
            while (resultado.next()) {
                TablaColaboracion tablaColaboracion = new TablaColaboracion();
                tablaColaboracion.setIdColaboracion(resultado.getInt("idColaboracion"));
                tablaColaboracion.setNombre(resultado.getString("nombreColaboracion"));
                tablaColaboracion.setModalidad(resultado.getString("tipoColaboracion"));
                tablaColaboracion.setPeriodo(resultado.getString("periodo"));
                tablaColaboracion.setNumeroActividades(resultado.getString("numeroActividades"));
                tablaColaboracion.setNumeroEvidencias(resultado.getString("numeroEvidencias"));
                tablaColaboracion.setEstadoColaboracion(resultado.getString("estadoColaboracion"));
                colaboraciones.add(tablaColaboracion);
            }
        }
        return colaboraciones;
    }
    
    /**
    * Filtra las colaboraciones en la base de datos.
    *
    * @param filtro El filtro a aplicar.
    * @return Una lista de colaboraciones filtradas.
    */
    @Override
    public List<Colaboracion> filtrarColaboraciones(String filtro) {
        List<Colaboracion> colaboraciones = new ArrayList<>();
        try (Connection conexion = ManejadorBaseDeDatos.obtenerConexion();
             PreparedStatement prepararDeclaracion = conexion.prepareStatement(FILTRAR_COLABORACIONES)) {
            prepararDeclaracion.setString(1, "%" + filtro + "%");
            prepararDeclaracion.setString(2, "%" + filtro + "%");
            try (ResultSet resultado = prepararDeclaracion.executeQuery()) {
                while (resultado.next()) {
                    Colaboracion colaboracion = new Colaboracion();
                    colaboracion.setIdColaboracion(resultado.getInt("idColaboracion"));
                    colaboracion.setDescripcion(resultado.getString("descripcion"));
                    colaboracion.setRecursos(resultado.getString("recursos"));
                    colaboracion.setIdPeriodo(resultado.getInt("Periodo_idPeriodo"));
                    colaboracion.setAprendizajesEsperados(resultado.getString("aprendizajesEsperados"));
                    colaboracion.setDetallesAsistenciaParticipacion(resultado.getString("detallesAsistenciaParticipacion"));
                    colaboracion.setDetallesEvaluacion(resultado.getString("detallesEvaluacion"));
                    colaboracion.setDetallesEntorno(resultado.getString("detallesEntorno"));
                    colaboracion.setIdPropuestaColaboracion(resultado.getInt("Propuesta_Colaboracion_idPropuestaColaboracion"));
                    colaboraciones.add(colaboracion);
                }
            }
        } catch (SQLException ex) {
            LOG.error(ex);
        }
        return colaboraciones;
    }
    
    /**
     * Consulta detalles específicos de una colaboración en la base de datos.
     *
     * @param idColaboracion El ID de la colaboración.
     * @return Una lista con detalles específicos de la colaboración.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos, por ejemplo:
     *                      - Cuando no se puede establecer conexión con la base de datos.
     *                      - Si los parámetros proporcionados son inválidos o nulos.
     *                      - Si la consulta SQL no se puede ejecutar correctamente.
     *                      - Si hay un problema al cerrar la conexión con la base de datos.
     */
    @Override
    public List<String> consultarDetallesEspecificos(int idColaboracion) throws SQLException {
        List<String> informacion = new ArrayList<>();
        Connection conexion = ManejadorBaseDeDatos.obtenerConexion();
            PreparedStatement declaracion = conexion.prepareStatement(CONSULTAR_DETALLES_ESPECIFICOS);
            declaracion.setInt(1, idColaboracion);
            try (ResultSet resultado = declaracion.executeQuery()) {
                if (resultado.next()) {
                    String nombreIdioma = resultado.getString("nombreIdioma");
                    String fechaInicio = resultado.getString("fechaInicio");
                    String fechaFin = resultado.getString("fechaFin");

                    informacion.add(nombreIdioma);
                    informacion.add(fechaInicio);
                    informacion.add(fechaFin);
                }
            }

        return informacion;
    }

}