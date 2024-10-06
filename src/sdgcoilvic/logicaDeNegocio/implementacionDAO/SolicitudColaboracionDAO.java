package sdgcoilvic.logicaDeNegocio.implementacionDAO;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import sdgcoilvic.accesoADatos.ManejadorBaseDeDatos;
import sdgcoilvic.logicaDeNegocio.interfaces.ISolicitudColaboracion;

public class SolicitudColaboracionDAO implements ISolicitudColaboracion {
    private static final String BUSCAR_SOLICITUDES = "{CALL buscarSolicitudesColaboracionPorProfesor(?)}";
    private static final String CONTAR_SOLICITUDES_ACEPTADAS = "{CALL contarSolicitudesAceptadasPorProfesor(?)}";
    private static final String ENVIAR_SOLICITUD_DE_COLABORACION = "{CALL enviarSolicitudColaboracion(?, ?, ?, ?)}";
    private static final String RECHAZAR_SOLICITUD= """
                                                    UPDATE solicitud_colaboracion
                                                    SET estadoSolicitud = 'Rechazada'
                                                    WHERE idSolicitudColaboracion = ?;""";
    private static final String ACEPTAR_SOLICITUD  = """
                                                     UPDATE solicitud_colaboracion
                                                     SET estadoSolicitud = 'Aceptada'
                                                     WHERE idSolicitudColaboracion = ?;""";
    private static final String CAMBIAR_ESTADO_PROFESOR  = "UPDATE profesor SET estadoProfesor = 'Esperando' WHERE idProfesor = (SELECT Profesor_idProfesor FROM solicitud_colaboracion WHERE idSolicitudColaboracion = ?)";
    private static final String REEVERTIR_EVALUACION  = """
                                                     UPDATE solicitud_colaboracion
                                                     SET estadoSolicitud = 'EnEspera'
                                                     WHERE idSolicitudColaboracion = ?;""";
    private static final String EXISTE_ALMENOS_UNA_SOLICITUD  = """
                                                                SELECT 
                                                                    COALESCE(GROUP_CONCAT(sc.idSolicitudColaboracion), -1) AS solicitud_ids
                                                                FROM 
                                                                    solicitud_propuesta sp
                                                                JOIN 
                                                                    solicitud_colaboracion sc ON sp.idSolicitudColaboracion = sc.idSolicitudColaboracion
                                                                WHERE 
                                                                    sp.idPropuestaColaboracion = ? AND sc.estadoSolicitud = 'Aceptada';""";
    private static final String VERIFICAR_ESTADO_COLABORACION= """
                                                               SELECT COUNT(*) AS colaboraciones_en_curso
                                                               FROM colaboracion_coilvic AS cc
                                                               JOIN Profesor_has_Colaboracion_CoilVic AS phcc ON cc.idColaboracion = phcc.Colaboracion_CoilVic_idColaboracion
                                                               WHERE phcc.Profesor_idProfesor = ?
                                                               AND cc.estadoColaboracion = 'EnCurso';""";
    
    /**
     * Consulta las solicitudes de colaboración para un profesor específico en la base de datos.
     *
     * @param idProfesor El ID del profesor para el cual se consultan las solicitudes de colaboración.
     * @return Una lista de listas de cadenas, donde cada lista interna representa una solicitud de colaboración
     *         con los siguientes elementos en orden:
     *         [idSolicitudColaboracion, nombrePropuesta, nombreProfesorSolicitud, nombreInstitucion,
     *          idiomaPropuesta, mensaje, fechaSolicitud, correoProfesorSolicitud]
     * @throws SQLException Si ocurre un error al interactuar con la base de datos, por ejemplo:
     *                      - Cuando no se puede establecer conexión con la base de datos.
     *                      - Si los parámetros proporcionados son inválidos o nulos.
     *                      - Si la consulta SQL no se puede ejecutar correctamente.
     *                      - Si hay un problema al cerrar la conexión con la base de datos.
     */
    @Override
    public List<List<String>> consultarSolicitudesColaboracion(int idProfesor) throws SQLException {
        List<List<String>> solicitudes = new ArrayList<>();
            Connection conexion = ManejadorBaseDeDatos.obtenerConexion();
            CallableStatement declaracionInvocable = conexion.prepareCall(BUSCAR_SOLICITUDES);
            declaracionInvocable.setInt(1, idProfesor);
            ResultSet resultado = declaracionInvocable.executeQuery();
            while (resultado.next()) {
                            List<String> solicitud = new ArrayList<>();
                            solicitud.add(resultado.getString("idSolicitudColaboracion"));
                            solicitud.add(resultado.getString("nombrePropuesta"));
                            solicitud.add(resultado.getString("nombreProfesorSolicitud"));
                            solicitud.add(resultado.getString("nombreInstitucion"));
                            solicitud.add(resultado.getString("idiomaPropuesta"));
                            solicitud.add(resultado.getString("mensaje"));
                            solicitud.add(resultado.getString("fechaSolicitud"));
                            solicitud.add(resultado.getString("correoProfesorSolicitud"));
                            solicitudes.add(solicitud);
            }
            ManejadorBaseDeDatos.cerrarConexion(); 
        return solicitudes;
    }
    
    /**
     * Cuenta el número de solicitudes de colaboración aceptadas para un profesor específico.
     *
     * @param idProfesor El ID del profesor para el cual se desea contar las solicitudes aceptadas.
     * @return El número de solicitudes aceptadas.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos, por ejemplo:
     *                      - Cuando no se puede establecer conexión con la base de datos.
     *                      - Si los parámetros proporcionados son inválidos o nulos.
     *                      - Si la consulta SQL no se puede ejecutar correctamente.
     *                      - Si hay un problema al cerrar la conexión con la base de datos.
     */
    @Override
    public int contarSolicitudesAceptadas(int idProfesor) throws SQLException {
        int totalSolicitudes = -1;
        Connection conexion = ManejadorBaseDeDatos.obtenerConexion();
            CallableStatement declaracion = conexion.prepareCall(CONTAR_SOLICITUDES_ACEPTADAS);
            declaracion.setInt(1, idProfesor);
            ResultSet resultado = declaracion.executeQuery();
            if (resultado.next()) {
               totalSolicitudes = resultado.getInt("totalSolicitudesAceptadas");
            } 
            ManejadorBaseDeDatos.cerrarConexion();
        return totalSolicitudes;
    }
    
    /**
     * Envía una solicitud de colaboración.
     *
     * @param idPropuestaColaboracion El ID de la propuesta de colaboración para la cual se envía la solicitud.
     * @param mensaje El mensaje asociado a la solicitud de colaboración.
     * @param idProfesor El ID del profesor que envía la solicitud.
     * @return Un entero que indica el resultado de la operación:
     *         - 1 si la solicitud se envió correctamente.
     *         - Otro valor si ocurrió algún error durante el proceso.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos, por ejemplo:
     *                      - Cuando no se puede establecer conexión con la base de datos.
     *                      - Si los parámetros proporcionados son inválidos o nulos.
     *                      - Si la consulta SQL no se puede ejecutar correctamente.
     *                      - Si hay un problema al cerrar la conexión con la base de datos.
     */
    @Override
    public int enviarSolicitudDeColaboracion(int idPropuestaColaboracion, String mensaje, int idProfesor) throws SQLException {
        int resultado = -1;
            Connection conexion = ManejadorBaseDeDatos.obtenerConexion();
            CallableStatement declaracionInvocable = conexion.prepareCall(ENVIAR_SOLICITUD_DE_COLABORACION);
            declaracionInvocable.setInt(1, idPropuestaColaboracion);
            declaracionInvocable.setString(2, mensaje);
            declaracionInvocable.setInt(3, idProfesor);
            declaracionInvocable.registerOutParameter(4, java.sql.Types.INTEGER);

            declaracionInvocable.execute();

            resultado = declaracionInvocable.getInt(4);

        ManejadorBaseDeDatos.cerrarConexion();     
        return resultado;
    }

     /**
     * Rechaza una solicitud de colaboración.
     *
     * @param idSolicitudColaboracion El ID de la solicitud de colaboración que se desea rechazar.
     * @return El número de filas afectadas por la operación (generalmente debería ser 1 si la solicitud existía).
     * @throws SQLException Si ocurre un error al interactuar con la base de datos, por ejemplo:
     *                      - Cuando no se puede establecer conexión con la base de datos.
     *                      - Si los parámetros proporcionados son inválidos o nulos.
     *                      - Si la consulta SQL no se puede ejecutar correctamente.
     *                      - Si hay un problema al cerrar la conexión con la base de datos.
     */
    @Override
    public int rechazarSolicitud(int idSolicitudColaboracion) throws SQLException {
        int columnaAfectada =  -1;
        String consulta = RECHAZAR_SOLICITUD;
        Connection conexion = ManejadorBaseDeDatos.obtenerConexion();
        PreparedStatement declaracion = conexion.prepareStatement(consulta);
        declaracion.setInt(1, idSolicitudColaboracion);
        columnaAfectada = declaracion.executeUpdate();
        ManejadorBaseDeDatos.cerrarConexion();
        return columnaAfectada;
    }

    /**
     * Acepta una solicitud de colaboración.
     *
     * @param idSolicitudColaboracion El ID de la solicitud de colaboración que se desea aceptar.
     * @return El número de filas afectadas por la operación (generalmente debería ser 1 si la solicitud existía).
     * @throws SQLException Si ocurre un error al interactuar con la base de datos, por ejemplo:
     *                      - Cuando no se puede establecer conexión con la base de datos.
     *                      - Si los parámetros proporcionados son inválidos o nulos.
     *                      - Si la consulta SQL no se puede ejecutar correctamente.
     *                      - Si hay un problema al cerrar la conexión con la base de datos.
     */
    @Override
    public int aceptarSolicitud(int idSolicitudColaboracion) throws SQLException {
        int columnaAfectada =  -1;
        try {
            Connection conexion = ManejadorBaseDeDatos.obtenerConexion();
            conexion.setAutoCommit(false);
            PreparedStatement solicitudStatement = conexion.prepareStatement(ACEPTAR_SOLICITUD);
            solicitudStatement.setInt(1, idSolicitudColaboracion);
            columnaAfectada = solicitudStatement.executeUpdate();
           
            PreparedStatement profesorStatement = conexion.prepareStatement(CAMBIAR_ESTADO_PROFESOR);
            profesorStatement.setInt(1, idSolicitudColaboracion);
            columnaAfectada += profesorStatement.executeUpdate();
            
        
           conexion.commit();
       } catch (SQLException ex) {
           
           ManejadorBaseDeDatos.obtenerConexion().rollback();
       } finally {
           ManejadorBaseDeDatos.obtenerConexion().close();
       }
       return columnaAfectada;
    }
    
    /**
     * Revierte la evaluación de una solicitud de colaboración.
     *
     * @param idSolicitudColaboracion El ID de la solicitud de colaboración cuya evaluación se desea revertir.
     * @return El número de filas afectadas por la operación (generalmente debería ser 1 si la solicitud existía).
     * @throws SQLException Si ocurre un error al interactuar con la base de datos, por ejemplo:
     *                      - Cuando no se puede establecer conexión con la base de datos.
     *                      - Si los parámetros proporcionados son inválidos o nulos.
     *                      - Si la consulta SQL no se puede ejecutar correctamente.
     *                      - Si hay un problema al cerrar la conexión con la base de datos.
     */
    @Override
    public int reevertirEvaluacion(int idSolicitudColaboracion) throws SQLException {
        int columnaAfectada =  -1;
        String consulta = REEVERTIR_EVALUACION;
        Connection conexion =  ManejadorBaseDeDatos.obtenerConexion();
        PreparedStatement declaracion = conexion.prepareStatement(consulta);
        declaracion.setInt(1, idSolicitudColaboracion);
        columnaAfectada = declaracion.executeUpdate();
        ManejadorBaseDeDatos.cerrarConexion();
        return columnaAfectada;
    }
    
    /**
     * Obtiene las solicitudes aprobadas para una solicitud de colaboración específica.
     *
     * @param idSolicitudColaboracion El ID de la solicitud de colaboración para la cual se desean obtener las solicitudes aprobadas.
     * @return Una cadena que contiene los IDs de las solicitudes aprobadas, separados por comas.
     *         Retorna "-1" si no hay solicitudes aprobadas o si ocurre algún error.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos, por ejemplo:
     *                      - Cuando no se puede establecer conexión con la base de datos.
     *                      - Si los parámetros proporcionados son inválidos o nulos.
     *                      - Si la consulta SQL no se puede ejecutar correctamente.
     *                      - Si hay un problema al cerrar la conexión con la base de datos.
     */
    @Override
    public String obtenerSolicitudesAprobadas(int idSolicitudColaboracion) throws SQLException {
        
        String resultado = "-1";
        String consulta = EXISTE_ALMENOS_UNA_SOLICITUD;
        Connection conexion = ManejadorBaseDeDatos.obtenerConexion();
        PreparedStatement declaracion = conexion.prepareStatement(consulta);
        declaracion.setInt(1, idSolicitudColaboracion);
            try (ResultSet resultadoDeclaracion = declaracion.executeQuery()) {
                if (resultadoDeclaracion.next()) {
                    resultado = resultadoDeclaracion.getString("solicitud_ids");
                }
            }
        ManejadorBaseDeDatos.cerrarConexion();
        return resultado;
    }
    
    /**
     * Verifica el estado de colaboración para un profesor específico.
     *
     * @param idProfesor El ID del profesor para el cual se desea verificar el estado de colaboración.
     * @return Un booleano que indica si el profesor tiene colaboraciones en curso (true) o no (false).
     * @throws SQLException Si ocurre un error al interactuar con la base de datos, por ejemplo:
     *                      - Cuando no se puede establecer conexión con la base de datos.
     *                      - Si los parámetros proporcionados son inválidos o nulos.
     *                      - Si la consulta SQL no se puede ejecutar correctamente.
     *                      - Si hay un problema al cerrar la conexión con la base de datos.
     */
    @Override
    public boolean verificarEstadoColaboracion(int idProfesor) throws SQLException {
        boolean hayColaboracionesEnCurso  = false;
        String consulta = VERIFICAR_ESTADO_COLABORACION;

        try (Connection conexion = ManejadorBaseDeDatos.obtenerConexion();
             PreparedStatement declaracion = conexion.prepareStatement(consulta)) {
            declaracion.setInt(1, idProfesor);
            try (ResultSet resultado = declaracion.executeQuery()) {
                if (resultado.next()) {
                int colaboracionesEnCurso = resultado.getInt("colaboraciones_en_curso");
               hayColaboracionesEnCurso = (colaboracionesEnCurso > 0);
                }
            }
        }
        return hayColaboracionesEnCurso;
    }
    
    /**
     * Verifica el estado de la solicitud para un profesor específico.
     *
     * @param idProfesor El ID del profesor para el cual se desea verificar el estado de las solicitudes.
     * @return Un booleano que indica si el profesor tiene solicitudes aceptadas o en espera (true) o no (false).
     * @throws SQLException Si ocurre un error al interactuar con la base de datos, por ejemplo:
     *                      - Cuando no se puede establecer conexión con la base de datos.
     *                      - Si los parámetros proporcionados son inválidos o nulos.
     *                      - Si la consulta SQL no se puede ejecutar correctamente.
     *                      - Si hay un problema al cerrar la conexión con la base de datos.
     */
    @Override
    public boolean verificarEstadoSolicitud(int idProfesor)throws SQLException {
        boolean hayColaboracionesEnCurso = false;
        String consulta = "SELECT COUNT(*) AS solicitudes FROM solicitud_colaboracion WHERE Profesor_idProfesor = ? AND estadoSolicitud IN ('Aceptada', 'EnEspera')";

        try (Connection conexion = ManejadorBaseDeDatos.obtenerConexion();
             PreparedStatement declaracion = conexion.prepareStatement(consulta)) {
            declaracion.setInt(1, idProfesor);
            try (ResultSet resultado = declaracion.executeQuery()) {
                if (resultado.next()) {
                    int colaboracionesEnCurso = resultado.getInt("solicitudes");
                    hayColaboracionesEnCurso = (colaboracionesEnCurso > 0);
                }
            }
        }
        return hayColaboracionesEnCurso;
    }
    
}
