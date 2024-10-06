package sdgcoilvic.logicaDeNegocio.implementacionDAO;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import sdgcoilvic.accesoADatos.ManejadorBaseDeDatos;
import sdgcoilvic.logicaDeNegocio.clases.Acceso;
import sdgcoilvic.logicaDeNegocio.clases.Profesor;
import sdgcoilvic.logicaDeNegocio.clases.ProfesorUV;
import sdgcoilvic.logicaDeNegocio.interfaces.IProfesorUV;

public class ProfesorUVDAO implements IProfesorUV {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(ProfesorUVDAO.class);
    private static final String INSERTAR_PROFESOR = """
                                INSERT INTO profesor (nombre, apellidoPaterno, apellidoMaterno, correo, idIdiomas, idAcceso, estadoProfesor, Institucion_claveInstitucional)
                                VALUES ( ?, ?, ?, ?, ?, ?, ?, ?);
                                """;
    private static final String INSERTAR_PROFESOR_UV = """
                                                       INSERT INTO profesor_uv (noPersonal, disciplina, idRegion, idCategoriaContratacionUV, idTipoContratacionUV, idAreaAcademica, Profesor_idProfesor)
                                                       VALUES (?, ?, ?, ?, ?, ?, ?);"""; 
    private static final String AGREGAR_ACCESO = "{call insertarAcceso(?, ?, ?, ?)}";
    private static final String VERIFICAR_EXISTENCIA_NOPERSONAL = "SELECT COUNT(*) AS number_of_matches FROM profesor_uv WHERE noPersonal = ?";
    private static final String OBTENER_LISTA_DE_CATEGORIA_CONTRATACION = "SELECT idCategoriaContratacionUV, categoriaContratacion FROM categoria_contratacion_uv;";
    private static final String OBTENER_LISTA_DE_TIPO_CONSTRATACION = "SELECT idTipoContratacionUV, tipoContratacion FROM tipo_contratacion_uv;";
    private static final String OBTENER_LISTA_DE_REGION = "SELECT idRegion, nombre FROM region;";
    private static final String OBTENER_LISTA_DE_AREA_ACADEMICA = "SELECT idAreaAcademica, area FROM area_academica;";
    private static final String ELIMINAR_PROFESOR = """
                                                    DELETE profesor_uv, profesor, acceso
                                                    FROM profesor_uv
                                                    JOIN profesor ON profesor_uv.Profesor_idProfesor = profesor.idProfesor
                                                    JOIN acceso ON profesor.idAcceso = acceso.idAcceso
                                                    WHERE profesor_uv.noPersonal = ?;
                                                    """;
    private static final String ACTUALIZAR_PROFESOR = 
        "UPDATE profesor SET nombre = ?, apellidoPaterno = ?, apellidoMaterno = ?, " +
        "correo = ?, idIdiomas = ?, estadoProfesor = ? " +
        "WHERE idProfesor = ?";

    private static final String ACTUALIZAR_PROFESOR_UV = 
        "UPDATE profesor_uv SET noPersonal = ?,disciplina = ?, idRegion = ?, idCategoriaContratacionUV = ?, " +
        "idTipoContratacionUV = ?, idAreaAcademica = ? WHERE Profesor_idProfesor = ?";
    private static final String OBTENER_ID_PROFESOR = 
        "SELECT Profesor_idProfesor FROM profesor_uv WHERE noPersonal = ?";
    private static final String OBTENER_PROFESORUV_POR_NUMPERSONAL = """
                                                                     SELECT profesor_uv.*, profesor.nombre, profesor.apellidoPaterno, profesor.apellidoMaterno, profesor.correo, profesor.idIdiomas, profesor.estadoProfesor, profesor.Institucion_claveInstitucional
                                                                     FROM profesor_uv
                                                                     INNER JOIN profesor ON profesor_uv.Profesor_idProfesor = profesor.idProfesor
                                                                     WHERE noPersonal = ?;""";
    /**
     * Registra un nuevo profesor UV en la base de datos.
     * 
     * @param acceso El objeto Acceso que contiene la información del acceso del profesor.
     * @param profesorUV El objeto ProfesorUV que contiene la información del profesor UV.
     * @return El número de filas afectadas por las inserciones.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos, por ejemplo:
     *                      - Cuando no se puede establecer conexión con la base de datos.
     *                      - Si los parámetros proporcionados son inválidos o nulos.
     *                      - Si la consulta SQL no se puede ejecutar correctamente.
     *                      - Si hay un problema al cerrar la conexión con la base de datos.
     */
    @Override
    public int registrarProfesorUV(Acceso acceso,  ProfesorUV profesorUV) throws SQLException{
       int numeroFilasAfectada = -1;
       int idAccesoGenerado = -1;
       int idProfesorGenerado = -1;
       try {
                Connection conexion = ManejadorBaseDeDatos.obtenerConexion();
                conexion.setAutoCommit(false);
                CallableStatement accesoStatement = conexion.prepareCall(AGREGAR_ACCESO);
                accesoStatement.registerOutParameter(1, Types.INTEGER); 
                accesoStatement.setString(2, acceso.getContrasenia());
                accesoStatement.setString(3, acceso.getUsuario());
                accesoStatement.setString(4, acceso.getTipoUsuario());
                accesoStatement.execute();

                idAccesoGenerado = accesoStatement.getInt(1);
                
                PreparedStatement profesorStatement = conexion.prepareStatement(INSERTAR_PROFESOR, Statement.RETURN_GENERATED_KEYS);
                profesorStatement.setString(1, profesorUV.getNombre());
                profesorStatement.setString(2, profesorUV.getApellidoPaterno());
                profesorStatement.setString(3, profesorUV.getApellidoMaterno());
                profesorStatement.setString(4, profesorUV.getCorreo());
                profesorStatement.setInt(5, profesorUV.getIdIdiomas());
                profesorStatement.setInt(6, idAccesoGenerado);
                profesorStatement.setString(7, profesorUV.getEstadoProfesor());
                profesorStatement.setString(8, profesorUV.getClaveInstitucional());
                numeroFilasAfectada = profesorStatement.executeUpdate();
                
                ResultSet clavesGeneradas = profesorStatement.getGeneratedKeys();
                if (clavesGeneradas.next()) {
                    idProfesorGenerado = clavesGeneradas.getInt(1);
                    profesorUV.setIdProfesor(idProfesorGenerado);
                }
                
                PreparedStatement profesorUVstatement = conexion.prepareStatement(INSERTAR_PROFESOR_UV);
                profesorUVstatement.setString(1, profesorUV.getNoPersonal());
                profesorUVstatement.setString(2, profesorUV.getDisciplina());
                profesorUVstatement.setInt(3, profesorUV.getIdRegion());
                profesorUVstatement.setInt(4, profesorUV.getIdCategoriaContratacionUV());
                profesorUVstatement.setInt(5, profesorUV.getIdTipoContratacionUV());
                profesorUVstatement.setInt(6, profesorUV.getIdAreaAcademica());
                profesorUVstatement.setInt(7,idProfesorGenerado );
                numeroFilasAfectada += profesorUVstatement.executeUpdate();
                conexion.commit();
                
       } catch (SQLException ex) {
           ManejadorBaseDeDatos.obtenerConexion().rollback();
       } finally {
           ManejadorBaseDeDatos.obtenerConexion().close();
       }
       return numeroFilasAfectada;
    }

    /**
     * Elimina un profesor de la Universidad Veracruzana (UV) de la base de datos.
     *
     * @param noPersonal El número de personal del profesor a eliminar.
     * @return El número de filas afectadas por la eliminación, o -1 si ocurre un error.
     */
    @Override
    public int eliminarProfesorUV(String noPersonal) {
        int columnaAfectada = -1;
        try (Connection conexion = ManejadorBaseDeDatos.obtenerConexion()){
            PreparedStatement prepararDeclaracion = conexion.prepareStatement(ELIMINAR_PROFESOR);
            prepararDeclaracion.setString(1, noPersonal);
            columnaAfectada = prepararDeclaracion.executeUpdate();
        } catch (SQLException ex) {
            LOG.error(ex);
        }
        
        return columnaAfectada;
    }

    /**
    * Verifica si un número de personal ya existe en la base de datos.
    *
    * @param noPersonal El número de personal a verificar.
    * @return true si el número de personal existe, false en caso contrario.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos, por ejemplo:
     *                      - Cuando no se puede establecer conexión con la base de datos.
     *                      - Si los parámetros proporcionados son inválidos o nulos.
     *                      - Si la consulta SQL no se puede ejecutar correctamente.
     *                      - Si hay un problema al cerrar la conexión con la base de datos.
    */
        @Override
    public boolean verificarSiExisteElNoPersonal(String noPersonal) throws SQLException {
        boolean existeCorreo = true;
        String consulta = VERIFICAR_EXISTENCIA_NOPERSONAL;
        Connection conexion = ManejadorBaseDeDatos.obtenerConexion();
        PreparedStatement declaracion = conexion.prepareStatement(consulta);
        declaracion.setString(1, noPersonal);
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
     * Obtiene una lista de las regiones disponibles en la base de datos.
     *
     * @return Una lista de listas, donde cada sublista contiene el ID y el nombre de una región.
     */
    @Override
    public List<List<String>> obtenerListaDeRegion() {
        List<List<String>> listaDeRegion = new ArrayList<>();
         String consulta = OBTENER_LISTA_DE_REGION;
        try (Connection conexion = ManejadorBaseDeDatos.obtenerConexion()){
            PreparedStatement declaracion = conexion.prepareStatement(consulta);
            ResultSet resultado = declaracion.executeQuery(); 
               while (resultado.next()) {
               int idRegion = resultado.getInt("idRegion");
               String nombre = resultado.getString("nombre");
               List<String> region = new ArrayList<>();
               region.add(Integer.toString(idRegion));
               region.add(nombre);
               listaDeRegion.add(region);
           }
        } catch (SQLException ex) {
            LOG.error(ex);
        }
            
        return listaDeRegion;
    }

    /**
     * Obtiene una lista de las categorías de contratación disponibles en la base de datos.
     *
     * @return Una lista de listas, donde cada sublista contiene el ID y el nombre de una categoría de contratación.
     */
    @Override
    public List<List<String>> obtenerListaDeCategoriaContratacion() {
        List<List<String>> listaDeCategoriaContratacion = new ArrayList<>();
        String consulta = OBTENER_LISTA_DE_CATEGORIA_CONTRATACION;
        try (Connection conexion = ManejadorBaseDeDatos.obtenerConexion()){
            PreparedStatement declaracion = conexion.prepareStatement(consulta);
            ResultSet resultado = declaracion.executeQuery(); 
            while (resultado.next()) {
                int idCategoriaContratacionUV = resultado.getInt("idCategoriaContratacionUV");
                String categoriaContratacion = resultado.getString("categoriaContratacion");
                List<String> categoria = new ArrayList<>();
                categoria.add(Integer.toString(idCategoriaContratacionUV));
                categoria.add(categoriaContratacion);
                listaDeCategoriaContratacion.add(categoria);
            }
        } catch (SQLException ex) {
            LOG.error(ex);
        }
        return listaDeCategoriaContratacion;
    }

    /**
     * Obtiene una lista de los tipos de contratación disponibles en la base de datos.
     *
     * @return Una lista de listas, donde cada sublista contiene el ID y el nombre de un tipo de contratación.
     */
    @Override
    public List<List<String>> obtenerListaDeTipoContratacion(){
        List<List<String>> listaDeTipoContratacion = new ArrayList<>();
        String consulta = OBTENER_LISTA_DE_TIPO_CONSTRATACION;
        try (Connection conexion = ManejadorBaseDeDatos.obtenerConexion()){
            PreparedStatement declaracion = conexion.prepareStatement(consulta);
            ResultSet resultado = declaracion.executeQuery(); 
            while (resultado.next()) {
                int idTipoContratacionUV = resultado.getInt("idTipoContratacionUV");
                String tipoContratacion = resultado.getString("tipoContratacion");
                List<String> tipo = new ArrayList<>();
                tipo.add(Integer.toString(idTipoContratacionUV));
                tipo.add(tipoContratacion);
                listaDeTipoContratacion.add(tipo);
            }
            
        } catch (SQLException ex) {
            LOG.error(ex);
        }
        return listaDeTipoContratacion;
    }

    /**
    * Obtiene una lista de las áreas académicas disponibles en la base de datos.
    *
    * @return Una lista de listas, donde cada sublista contiene el ID y el nombre de un área académica.
    */
    @Override
    public List<List<String>> obtenerListaDeAreaAcademica()  {
        List<List<String>> listaDeAreaAcademica = new ArrayList<>();
        String consulta = OBTENER_LISTA_DE_AREA_ACADEMICA;
        try (Connection conexion = ManejadorBaseDeDatos.obtenerConexion()){
            PreparedStatement declaracion = conexion.prepareStatement(consulta);
            ResultSet resultado = declaracion.executeQuery(); 
            while (resultado.next()) {
                int idAreaAcademica = resultado.getInt("idAreaAcademica");
                String area = resultado.getString("area");
                List<String> areaAcademica = new ArrayList<>();
                areaAcademica.add(Integer.toString(idAreaAcademica));
                areaAcademica.add(area);
                listaDeAreaAcademica.add(areaAcademica);
            }
        } catch (SQLException ex) {
            LOG.error(ex);
        }
        return listaDeAreaAcademica;
    }
    
    /**
    * Obtiene la información de un profesor de la Universidad Veracruzana (UV) basado en su número de personal.
    *
    * @param numPersonal El número de personal del profesor.
    * @return Un objeto ProfesorUV con la información del profesor.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos, por ejemplo:
     *                      - Cuando no se puede establecer conexión con la base de datos.
     *                      - Si los parámetros proporcionados son inválidos o nulos.
     *                      - Si la consulta SQL no se puede ejecutar correctamente.
     *                      - Si hay un problema al cerrar la conexión con la base de datos.
    */
    @Override
    public ProfesorUV obtenerProfesorUVPorNumPersonal (String numPersonal) throws SQLException {
        ProfesorUV profesorUV = new ProfesorUV();
        String consulta = OBTENER_PROFESORUV_POR_NUMPERSONAL;
        Connection conexion = ManejadorBaseDeDatos.obtenerConexion();
        PreparedStatement declaracion = conexion.prepareStatement(consulta);
        declaracion.setString(1, numPersonal);
        ResultSet resultado = declaracion.executeQuery();
        if (resultado.next()) {
            profesorUV = obtenerProfesorUV(resultado);
        }
        ManejadorBaseDeDatos.cerrarConexion();
        return profesorUV;

    }  
    
    /**
    * Obtiene un objeto ProfesorUV a partir de un ResultSet.
    *
    * @param result El ResultSet con la información del profesor.
    * @return Un objeto ProfesorUV con la información del profesor.
    * @throws SQLException Si ocurre un error al procesar el ResultSet.
    */
    private ProfesorUV obtenerProfesorUV(ResultSet result) throws SQLException {
        ProfesorUV profesorUV = new ProfesorUV();
        profesorUV.setNoPersonal(result.getString("noPersonal"));
        profesorUV.setDisciplina(result.getString("disciplina"));
        profesorUV.setIdRegion(result.getInt("idRegion"));
        profesorUV.setIdCategoriaContratacionUV(result.getInt("idCategoriaContratacionUV"));
        profesorUV.setIdTipoContratacionUV(result.getInt("idTipoContratacionUV"));
        profesorUV.setIdAreaAcademica(result.getInt("idAreaAcademica")); 
        
        return profesorUV;
    }
        
    /**
     * Actualiza la información de un profesor de la Universidad Veracruzana (UV) en la base de datos.
     *
     * @param profesor Un objeto Profesor con la información general del profesor.
     * @param profesorUV Un objeto ProfesorUV con la información específica del profesor de la UV.
     * @param noPersonal El número de personal del profesor a actualizar.
     * @return El número de filas afectadas por la actualización.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos, por ejemplo:
     *                      - Cuando no se puede establecer conexión con la base de datos.
     *                      - Si los parámetros proporcionados son inválidos o nulos.
     *                      - Si la consulta SQL no se puede ejecutar correctamente.
     *                      - Si hay un problema al cerrar la conexión con la base de datos.
     */
    @Override
    public int actualizarInformacionDelProfesorUV(Profesor profesor,ProfesorUV profesorUV, String noPersonal) throws SQLException {
        int resutado = 0;
        int idProfesor = -1;
        try {
            Connection conexion = ManejadorBaseDeDatos.obtenerConexion();
            conexion.setAutoCommit(false);
            PreparedStatement obtenerIdStatement = conexion.prepareStatement(OBTENER_ID_PROFESOR);
                obtenerIdStatement.setString(1, noPersonal);
                ResultSet resultado = obtenerIdStatement.executeQuery();

                if (resultado.next()) {
                    idProfesor = resultado.getInt("Profesor_idProfesor");
                }
            PreparedStatement actualizarProfesorStatement = conexion.prepareStatement(ACTUALIZAR_PROFESOR);
            actualizarProfesorStatement.setString(1, profesor.getNombre());
            actualizarProfesorStatement.setString(2, profesor.getApellidoPaterno());
            actualizarProfesorStatement.setString(3, profesor.getApellidoMaterno());
            actualizarProfesorStatement.setString(4, profesor.getCorreo());
            actualizarProfesorStatement.setInt(5, profesor.getIdIdiomas());
            actualizarProfesorStatement.setString(6, profesor.getEstadoProfesor());
            actualizarProfesorStatement.setInt(7, idProfesor);
            resutado = actualizarProfesorStatement.executeUpdate();
            
            PreparedStatement actualizarProfesorUVStatement = conexion.prepareStatement(ACTUALIZAR_PROFESOR_UV);
            actualizarProfesorUVStatement.setString(1, profesorUV.getNoPersonal());
            actualizarProfesorUVStatement.setString(2, profesorUV.getDisciplina());
            actualizarProfesorUVStatement.setInt(3, profesorUV.getIdRegion());
            actualizarProfesorUVStatement.setInt(4, profesorUV.getIdCategoriaContratacionUV());
            actualizarProfesorUVStatement.setInt(5, profesorUV.getIdTipoContratacionUV());
            actualizarProfesorUVStatement.setInt(6, profesorUV.getIdAreaAcademica());
            actualizarProfesorUVStatement.setInt(7, idProfesor);
            resutado += actualizarProfesorUVStatement.executeUpdate();
            conexion.commit();
        } catch (SQLException ex) {
            ManejadorBaseDeDatos.obtenerConexion().rollback();
        }
        return resutado;
    }
    
    /**
    * Obtiene la información de un profesor de la Universidad Veracruzana (UV) basado en su número de personal.
    *
    * @param numPersonal El número de personal del profesor.
    * @return Un objeto ProfesorUV con la información del profesor.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos, por ejemplo:
     *                      - Cuando no se puede establecer conexión con la base de datos.
     *                      - Si los parámetros proporcionados son inválidos o nulos.
     *                      - Si la consulta SQL no se puede ejecutar correctamente.
     *                      - Si hay un problema al cerrar la conexión con la base de datos.
    */
    @Override
    public ProfesorUV obtenerProfesorPorNumPersonal(String numPersonal) throws SQLException {
        ProfesorUV profesorUV = new ProfesorUV();
        String consulta = OBTENER_PROFESORUV_POR_NUMPERSONAL;
        Connection conexion = ManejadorBaseDeDatos.obtenerConexion();
        PreparedStatement declaracion = conexion.prepareStatement(consulta);
        declaracion.setString(1, numPersonal);
        ResultSet resultado = declaracion.executeQuery();
        if (resultado.next()) {
        profesorUV.setNoPersonal(resultado.getString("noPersonal"));
        profesorUV.setDisciplina(resultado.getString("disciplina"));
        profesorUV.setIdRegion(resultado.getInt("idRegion"));
        profesorUV.setIdCategoriaContratacionUV(resultado.getInt("idCategoriaContratacionUV"));
        profesorUV.setIdTipoContratacionUV(resultado.getInt("idTipoContratacionUV"));
        profesorUV.setIdAreaAcademica(resultado.getInt("idAreaAcademica")); 
        profesorUV.setIdProfesor(resultado.getInt("Profesor_idProfesor"));
        profesorUV.setNombre(resultado.getString("nombre"));
        profesorUV.setApellidoPaterno(resultado.getString("apellidoPaterno"));
        profesorUV.setApellidoMaterno(resultado.getString("apellidoMaterno"));
        profesorUV.setCorreo(resultado.getString("correo"));
        profesorUV.setIdIdiomas(resultado.getInt("idIdiomas"));
        profesorUV.setEstadoProfesor(resultado.getString("estadoProfesor"));
        }
        ManejadorBaseDeDatos.cerrarConexion();
        return profesorUV;
    }
    
}
