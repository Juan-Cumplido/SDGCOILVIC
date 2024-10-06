package sdgcoilvic.logicaDeNegocio.implementacionDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import sdgcoilvic.accesoADatos.ManejadorBaseDeDatos;
import sdgcoilvic.logicaDeNegocio.clases.Estudiante;
import sdgcoilvic.logicaDeNegocio.interfaces.IEstudiante;

public class EstudianteDAO implements IEstudiante {
    private static final String INSERTAR_ESTUDIANTE = "INSERT INTO estudiante (nombre, apellidoPaterno, "
            + "apellidoMaterno, correo, claveInstitucional) "
            + "VALUES (?, ?, ?, ?, ?)";
    private static final String VINCULARLO_A_COLABORACION = """
                                                                INSERT INTO Colaboracion_CoilVic_has_Estudiante (Colaboracion_CoilVic_idColaboracion, Estudiante_idEstudiante)
                                                            VALUES (?, ?);""";   


    private static final String VERIFICAR_EXISTENCIA_NOMBRE_ESTUDIANTE = "SELECT COUNT(*) AS number_of_matches FROM estudiante WHERE (nombre = ?) AND (apellidoPaterno = ? AND apellidoMaterno = ?)";
    private static final String VERIFICAR_EXISTENCIA_CORREO = "SELECT COUNT(*) AS number_of_matches FROM estudiante WHERE correo = ?";
    
    
    /**
     * Registra un estudiante y lo vincula a una colaboración.
     * 
     * @param estudiante El estudiante a registrar.
     * @param idColaboracion El ID de la colaboración a la que se vinculará el estudiante.
     * @return El número de filas afectadas en la base de datos.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos, por ejemplo:
     *                      - Cuando no se puede establecer conexión con la base de datos.
     *                      - Si los parámetros proporcionados son inválidos o nulos.
     *                      - Si la consulta SQL no se puede ejecutar correctamente.
     *                      - Si hay un problema al cerrar la conexión con la base de datos.
     */
    @Override
    public int registrarEstudiante(Estudiante estudiante, int idColaboracion) throws SQLException {
       int numeroFilasAfectada = -1;
       int idEstudianteGenerdo = -1;
        try {
            Connection conexion = ManejadorBaseDeDatos.obtenerConexion();
            conexion.setAutoCommit(false);
            PreparedStatement declaracionPreparada = conexion.prepareStatement(INSERTAR_ESTUDIANTE, PreparedStatement.RETURN_GENERATED_KEYS);
            declaracionPreparada.setString(1, estudiante.getNombre());
            declaracionPreparada.setString(2, estudiante.getApellidoPaterno());
            declaracionPreparada.setString(3, estudiante.getApellidoMaterno());
            declaracionPreparada.setString(4, estudiante.getCorreo());
            declaracionPreparada.setString(5, estudiante.getClaveInstitucional());
            numeroFilasAfectada = declaracionPreparada.executeUpdate();
            
            ResultSet claveGenerada = declaracionPreparada.getGeneratedKeys();
            if (claveGenerada.next()) {
                idEstudianteGenerdo = claveGenerada.getInt(1);
                estudiante.setIdEstudiante(idEstudianteGenerdo);
            }
             
            PreparedStatement declaracion =  conexion.prepareStatement(VINCULARLO_A_COLABORACION);
            declaracion.setInt(1, idColaboracion);
            declaracion.setInt(2, idEstudianteGenerdo);
            numeroFilasAfectada += declaracion.executeUpdate();
            conexion.commit();
       } catch (SQLException ex) {
           
           ManejadorBaseDeDatos.obtenerConexion().rollback();
       } finally {
           ManejadorBaseDeDatos.obtenerConexion().close();
       }
       return numeroFilasAfectada;
    }

    /**
     * Verifica si existe un estudiante en la base de datos según su nombre y apellidos.
     * 
     * @param nombre El nombre del estudiante.
     * @param apellidoPaterno El apellido paterno del estudiante.
     * @param apellidoMaterno El apellido materno del estudiante.
     * @return true si el estudiante existe, false de lo contrario.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos, por ejemplo:
     *                      - Cuando no se puede establecer conexión con la base de datos.
     *                      - Si los parámetros proporcionados son inválidos o nulos.
     *                      - Si la consulta SQL no se puede ejecutar correctamente.
     *                      - Si hay un problema al cerrar la conexión con la base de datos.
     */
    @Override
    public boolean verificarExistenciaEstudiante(String nombre, String apellidoPaterno, String apellidoMaterno) throws SQLException {
        boolean existenciaEstudiante = true;
        String consulta = VERIFICAR_EXISTENCIA_NOMBRE_ESTUDIANTE;
        Connection conexion = ManejadorBaseDeDatos.obtenerConexion();
        PreparedStatement declaracion = conexion.prepareStatement(consulta);
        declaracion.setString(1, nombre);
        declaracion.setString(2, apellidoPaterno);
        declaracion.setString(3, apellidoMaterno);
        ResultSet resultado = declaracion.executeQuery();
        if (resultado.next()) {
            int NO_COINCIDE = 0;
            if (resultado.getInt("number_of_matches") == NO_COINCIDE) {
                existenciaEstudiante = false;
            }
        }
        ManejadorBaseDeDatos.cerrarConexion();
        
        return existenciaEstudiante;
    }

    /**
    * Verifica si existe un correo electrónico en la base de datos.
    * 
    * @param correo El correo electrónico a verificar.
    * @return true si el correo existe, false de lo contrario.
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


}
