package sdgcoilvic.logicaDeNegocio.implementacionDAO;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import sdgcoilvic.accesoADatos.ManejadorBaseDeDatos;
import sdgcoilvic.logicaDeNegocio.clases.EvidenciaActividad;
import sdgcoilvic.logicaDeNegocio.clases.Profesor;
import sdgcoilvic.logicaDeNegocio.interfaces.IEvidenciaActividad;


public class EvidenciaActividadDAO  implements IEvidenciaActividad{
     private static final Logger LOG=Logger.getLogger(EvidenciaActividadDAO.class);
   
    private static final String AGREGAR_EVIDENCIA = """
                                                           INSERT INTO evidencia (ruta, idActividadColaborativa, nombreEvidencia)
                                                           VALUES (?, ?, ?);""";
    private static final String OBTENER_EVIDENCIAS_POR_IDACTIVIDAD = """
                                                                   SELECT * 
                                                                   FROM evidencia 
                                                                   WHERE idActividadColaborativa = ?;""";
    private static final String OBTENER_EVIDENCIAS_POR_IDEVIDENCIA = """
                                                                   SELECT * 
                                                                   FROM evidencia 
                                                                   WHERE idEvidencia = ?;""";
    private static final String ACTUALIZAR_EVIDENCIA = "UPDATE evidencia SET ruta = ?, nombreEvidencia = ? WHERE idEvidencia = ?";

   
    /**
    * Agrega una nueva evidencia de actividad a la base de datos.
    * 
    * @param evidencia La evidencia de actividad a agregar.
    * @return El número de filas afectadas en la base de datos.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos, por ejemplo:
     *                      - Cuando no se puede establecer conexión con la base de datos.
     *                      - Si los parámetros proporcionados son inválidos o nulos.
     *                      - Si la consulta SQL no se puede ejecutar correctamente.
     *                      - Si hay un problema al cerrar la conexión con la base de datos.
    */
    @Override
    public int agregarEvidencia(EvidenciaActividad evidencia ) throws SQLException {
        int resultadoInsercion = 0;
        String consulta = AGREGAR_EVIDENCIA;
        try (Connection conexion = ManejadorBaseDeDatos.obtenerConexion();
             PreparedStatement declaracion = conexion.prepareStatement(consulta)) {
            declaracion.setString(1, evidencia.getRutaEvidencia());
            declaracion.setInt(2, evidencia.getIdActividad());
            declaracion.setString(3, evidencia.getNombre());
            resultadoInsercion = declaracion.executeUpdate();
        } 
        return resultadoInsercion;
    }
    
    /**
    * Crea la ruta de la evidencia de actividad.
    * 
    * @param idActividad El ID de la actividad.
    * @param idColaboracion El ID de la colaboración.
    * @param archivoNuevo El archivo de la evidencia.
    * @param profesor El profesor asociado a la evidencia.
    * @return La ruta de la evidencia de actividad.
    */
    public String obtenerRutaEvidenciaDeActividad(int idActividad, int idColaboracion, File archivoNuevo, Profesor profesor) {
        return "Colaboraciones/Colaboracion" + idColaboracion + "/Actividad" + idActividad + "/Profesor-" + profesor.getNombre() + profesor.getApellidoPaterno() + profesor.getApellidoMaterno() + "/" + archivoNuevo.getName();
    }

    /**
    * Crea una carpeta para la actividad de colaboración.
    * 
    * @param idActividad El ID de la actividad.
    * @param idColaboracion El ID de la colaboración.
    * @param profesor El profesor asociado a la actividad.
    * @return true si la carpeta se crea correctamente, false de lo contrario.
    */
    @Override
    public boolean crearCarpetaDeActividad(int idActividad, int idColaboracion, Profesor profesor) {
        boolean resultadoCreacionDeCarpeta = true;
        String rutaCarpeta = "Colaboraciones\\Colaboracion"+idColaboracion+"\\Actividad"+idActividad+"\\Profesor-"+profesor.getNombre()+profesor.getApellidoPaterno()+profesor.getApellidoMaterno();
        File carpetaActividad = new File(rutaCarpeta);
        try{
            if(!carpetaActividad.exists()){
                resultadoCreacionDeCarpeta = carpetaActividad.mkdirs();
            }
        }catch(SecurityException excepcion){
            LOG.error(excepcion.getCause());
            resultadoCreacionDeCarpeta = false;
        }
        return resultadoCreacionDeCarpeta;
    }
    
    
    /**
     * Guarda la evidencia de actividad en el sistema de archivos.
     * 
     * @param idActividad El ID de la actividad.
     * @param idColaboracion El ID de la colaboración.
     * @param archivoNuevo El archivo de la evidencia.
     * @param profesor El profesor asociado a la evidencia.
     * @return La ruta de la evidencia guardada.
     */
    @Override
    public String guardarEvidenciaDeActividad(int idActividad, int idColaboracion, File archivoNuevo, Profesor profesor) {
        String rutaDeRegistro="";
        String rutaOriginal = archivoNuevo.getAbsolutePath();
        String rutaDeDestino = "Colaboraciones/Colaboracion"+idColaboracion+"/Actividad"+idActividad+"/Profesor-"+profesor.getNombre()+profesor.getApellidoPaterno()+profesor.getApellidoMaterno()+"/"+archivoNuevo.getName();
        Path rutaDeArchivoOriginal = Paths.get(rutaOriginal);
        Path rutaArchivoDeDestino = Paths.get(rutaDeDestino);
        try{
            Files.copy(rutaDeArchivoOriginal, rutaArchivoDeDestino, StandardCopyOption.REPLACE_EXISTING);
            rutaDeRegistro = rutaDeDestino;
        }catch(IOException excepcion){
            LOG.error(excepcion.getCause());
        }
        return rutaDeRegistro;
    }
    
    /**
    * Borra un archivo de evidencia de actividad del sistema de archivos.
    * 
    * @param rutaEvidencia La ruta del archivo de evidencia.
    * @return El resultado de la operación de eliminación: 1 si se elimina correctamente, -1 si hay un error.
    */
    @Override
    public int borrarArchivoDeEvidencia(String rutaEvidencia) {
       int resultadoEliminacion = 0;
       File archivoAEliminar = new File(rutaEvidencia);
       try{
           archivoAEliminar.delete();
           resultadoEliminacion = 1;
       }catch(SecurityException excepcion){
           LOG.error(excepcion.getCause());
           resultadoEliminacion = -1;
       }
       return resultadoEliminacion;
    }
    
    /**
     * Obtiene una lista de evidencias de actividad por ID de actividad colaborativa.
     * 
     * @param idActividadColaborativa El ID de la actividad colaborativa.
     * @return Una lista de evidencias de actividad.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos, por ejemplo:
     *                      - Cuando no se puede establecer conexión con la base de datos.
     *                      - Si los parámetros proporcionados son inválidos o nulos.
     *                      - Si la consulta SQL no se puede ejecutar correctamente.
     *                      - Si hay un problema al cerrar la conexión con la base de datos.
     */
    @Override
   public List<EvidenciaActividad> obtenerListaDeEvidencias(int idActividadColaborativa)throws SQLException {
   List<EvidenciaActividad> listaEvidenciaActividad = new ArrayList<>();
         try (Connection conexion = ManejadorBaseDeDatos.obtenerConexion()){
            PreparedStatement delcaracion = conexion.prepareStatement(OBTENER_EVIDENCIAS_POR_IDACTIVIDAD);
            delcaracion.setInt(1, idActividadColaborativa);
            ResultSet resultado = delcaracion.executeQuery(); 
                while (resultado.next()) {
                EvidenciaActividad evidenciaActividad = new EvidenciaActividad();
                evidenciaActividad.setIdEvidencia(resultado.getInt("idEvidencia"));
                evidenciaActividad.setRutaEvidencia(resultado.getString("ruta"));
                evidenciaActividad.setIdActividad(resultado.getInt("idActividadColaborativa"));
                evidenciaActividad.setNombre(resultado.getString("nombreEvidencia"));

                listaEvidenciaActividad.add(evidenciaActividad);
         }
        }
        return listaEvidenciaActividad;
     }
   
    /**
     * Obtiene una evidencia de actividad por su ID.
     * 
     * @param idEvidencia El ID de la evidencia.
     * @return La evidencia de actividad.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos, por ejemplo:
     *                      - Cuando no se puede establecer conexión con la base de datos.
     *                      - Si los parámetros proporcionados son inválidos o nulos.
     *                      - Si la consulta SQL no se puede ejecutar correctamente.
     *                      - Si hay un problema al cerrar la conexión con la base de datos.
     */
    @Override
    public EvidenciaActividad obtenerEvidenciaPorIdEvidencia(int idEvidencia) throws SQLException {
         EvidenciaActividad evidenciaActividad = new EvidenciaActividad(); 
         try (Connection conexion = ManejadorBaseDeDatos.obtenerConexion()){
            PreparedStatement declaracion = conexion.prepareStatement(OBTENER_EVIDENCIAS_POR_IDEVIDENCIA);
            declaracion.setInt(1, idEvidencia);
            ResultSet resultado = declaracion.executeQuery(); 
                while (resultado.next()) {
                evidenciaActividad.setIdEvidencia(resultado.getInt("idEvidencia"));
                evidenciaActividad.setRutaEvidencia(resultado.getString("ruta"));
                evidenciaActividad.setIdActividad(resultado.getInt("idActividadColaborativa"));
                evidenciaActividad.setNombre(resultado.getString("nombreEvidencia"));

         }
        }
        return evidenciaActividad;
    }
    
    /**
    * Actualiza la información de una evidencia de actividad en la base de datos.
    * 
    * @param evidencia La evidencia de actividad con la información actualizada.
    * @return El número de filas afectadas en la base de datos.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos, por ejemplo:
     *                      - Cuando no se puede establecer conexión con la base de datos.
     *                      - Si los parámetros proporcionados son inválidos o nulos.
     *                      - Si la consulta SQL no se puede ejecutar correctamente.
     *                      - Si hay un problema al cerrar la conexión con la base de datos.
    */
    @Override
    public int actualizarEvidencia(EvidenciaActividad evidencia) throws SQLException {
    int resultadoActualizacion = 0;
    String consulta = ACTUALIZAR_EVIDENCIA;
    try (Connection conexion = ManejadorBaseDeDatos.obtenerConexion();
         PreparedStatement declaracion = conexion.prepareStatement(consulta)) {
        declaracion.setString(1, evidencia.getRutaEvidencia());
        declaracion.setString(2, evidencia.getNombre());
        declaracion.setInt(3, evidencia.getIdEvidencia());
        resultadoActualizacion = declaracion.executeUpdate();
    }
    return resultadoActualizacion;
}

}
