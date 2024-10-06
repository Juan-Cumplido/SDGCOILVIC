package implementacion;

import static org.junit.Assert.assertTrue;
import org.junit.Test;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import sdgcoilvic.logicaDeNegocio.implementacionDAO.SolicitudColaboracionDAO;


public class SolicitudColaboracionDAOTest {

    @Test
    public void testEnviarSolicitudDeColaboracionExitoso() throws SQLException {
        SolicitudColaboracionDAO solicitudColaboracionDAO = new SolicitudColaboracionDAO();
        int idPropuestaColaboracion = 1;
        String mensaje = "Solicitud de colaboracion";
        int idProfesor = 1;
        int resultado = solicitudColaboracionDAO.enviarSolicitudDeColaboracion(idPropuestaColaboracion, mensaje, idProfesor);
        assertTrue(resultado > -1);
    }
    
    @Test
    public void testConsultarSolicitudesColaboracion() throws SQLException {
        SolicitudColaboracionDAO solicitudColaboracionDAO = new SolicitudColaboracionDAO();
        int idProfesor = 1; 
        List<List<String>> solicitudes = solicitudColaboracionDAO.consultarSolicitudesColaboracion(idProfesor);
        assertNotNull(solicitudes); 
        assertTrue(!solicitudes.isEmpty());
        List<String> datosEsperados = new ArrayList<>();
        datosEsperados.add("1"); 
        datosEsperados.add("AcProyecto de Investigación");
        datosEsperados.add("Erick Atzin Olarte");      
        datosEsperados.add("UNIVERSIDAD VERACRUZANA");
        datosEsperados.add("Inglés");       
        datosEsperados.add("Solicitud de colaboracion"); 
        datosEsperados.add("2024-05-26");
        datosEsperados.add("atzin@example.com");      

        List<String> primeraSolicitud = solicitudes.get(0);
        assertEquals(datosEsperados, primeraSolicitud);
    }
    
    @Test
    public void testRechazarSolicitud() throws SQLException {
        SolicitudColaboracionDAO solicitudColaboracionDAO = new SolicitudColaboracionDAO();
        int idSolicitud = 1;
        int columnasAfectadas = solicitudColaboracionDAO.rechazarSolicitud(idSolicitud);
        assertEquals(1, columnasAfectadas);
    }
    
    @Test
    public void testRevertirEvaluacion() throws SQLException {
        SolicitudColaboracionDAO solicitudColaboracionDAO = new SolicitudColaboracionDAO();
        int idSolicitud = 1; 
        int columnasAfectadas = solicitudColaboracionDAO.reevertirEvaluacion(idSolicitud);
        assertEquals(1, columnasAfectadas);
    }
    
    @Test
    public void testAceptarSolicitud() throws SQLException {
        SolicitudColaboracionDAO solicitudColaboracionDAO = new SolicitudColaboracionDAO();
        int idSolicitud = 1;
        int columnasAfectadas = solicitudColaboracionDAO.aceptarSolicitud(idSolicitud);
        assertEquals(2, columnasAfectadas);
    }    
    
    @Test
    public void testObtenerSolicitudesAprobadas() throws SQLException {
        SolicitudColaboracionDAO solicitudColaboracionDAO = new SolicitudColaboracionDAO();
        int idSolicitud = 1; 
        String resultado = solicitudColaboracionDAO.obtenerSolicitudesAprobadas(idSolicitud);
        assertNotEquals("-1", resultado);
    }

    @Test
    public void testVerificarEstadoColaboracion() throws SQLException {
        int idProfesor = 1;
        SolicitudColaboracionDAO solicitudcolaboracionDAO = new SolicitudColaboracionDAO();
        boolean hayColaboracionesEnCurso = solicitudcolaboracionDAO.verificarEstadoColaboracion(idProfesor);
        assertTrue(hayColaboracionesEnCurso);
    }

    
}
