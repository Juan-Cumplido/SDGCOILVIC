package implementacion;

import static org.junit.Assert.assertTrue;
import org.junit.Test;
import java.sql.SQLException;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import sdgcoilvic.logicaDeNegocio.clases.EvidenciaActividad;
import sdgcoilvic.logicaDeNegocio.clases.Profesor;
import sdgcoilvic.logicaDeNegocio.implementacionDAO.EvidenciaActividadDAO;

public class EvidenciaActividadDAOTest {
    
 
       @Test
    public void testAgregarEvidencia() throws SQLException {
        EvidenciaActividad evidencia = new EvidenciaActividad();
        evidencia.setRutaEvidencia("/ruta/evidencia");
        evidencia.setIdActividad(1);
        evidencia.setNombre("Evidencia 1");
        EvidenciaActividadDAO dao = new EvidenciaActividadDAO();
        int resultado = dao.agregarEvidencia(evidencia);
        assertTrue(resultado > 0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAgregarEvidenciaNombreInvalido() throws SQLException {
        EvidenciaActividad evidencia = new EvidenciaActividad();
        evidencia.setRutaEvidencia("/ruta/evidencia");
        evidencia.setIdActividad(1);
        evidencia.setNombre("/*Evidencia 1");
        EvidenciaActividadDAO dao = new EvidenciaActividadDAO();
        int resultado = dao.agregarEvidencia(evidencia);
        assertTrue(resultado > 0);
    }
    
        @Test(expected = IllegalArgumentException.class)
    public void testAgregarEvidenciaNombreNulo() throws SQLException {
        EvidenciaActividad evidencia = new EvidenciaActividad();
        evidencia.setRutaEvidencia("/ruta/evidencia");
        evidencia.setIdActividad(1);
        evidencia.setNombre("");
        EvidenciaActividadDAO dao = new EvidenciaActividadDAO();
        int resultado = dao.agregarEvidencia(evidencia);
        assertTrue(resultado > 0);
    }
    
    @Test
    public void testCrearCarpetaDeActividad() {
        int idActividad = 1;
        int idColaboracion = 1;
        Profesor profesor = new Profesor();
        profesor.setNombre("Erick");
        profesor.setApellidoPaterno("Atzin");
        profesor.setApellidoMaterno("Olarte");
        EvidenciaActividadDAO evidenciaActividadDAO = new EvidenciaActividadDAO();
        boolean resultado = evidenciaActividadDAO.crearCarpetaDeActividad(idActividad, idColaboracion,profesor);
        assertTrue(resultado);
    }
    
    @Test
    public void testActualizarEvidencia() throws SQLException {
        EvidenciaActividad evidencia = new EvidenciaActividad();
        evidencia.setIdEvidencia(1);
        evidencia.setRutaEvidencia("/ruta/evidencia");
        evidencia.setNombre("Evidencia 1.0");
        EvidenciaActividadDAO dao = new EvidenciaActividadDAO();
        int resultado = dao.actualizarEvidencia(evidencia);
        assertEquals(1, resultado);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testActualizarEvidenciaNombreInvalido() throws SQLException {
        EvidenciaActividad evidencia = new EvidenciaActividad();
        evidencia.setIdEvidencia(1);
        evidencia.setRutaEvidencia("/ruta/evidencia");
        evidencia.setNombre("/*Evidencia 1.0");
        EvidenciaActividadDAO dao = new EvidenciaActividadDAO();
        int resultado = dao.actualizarEvidencia(evidencia);
        assertEquals(1, resultado);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testActualizarEvidenciaNombreNulo() throws SQLException {
        EvidenciaActividad evidencia = new EvidenciaActividad();
        evidencia.setIdEvidencia(1);
        evidencia.setRutaEvidencia("/ruta/evidencia");
        evidencia.setNombre("");
        EvidenciaActividadDAO dao = new EvidenciaActividadDAO();
        int resultado = dao.actualizarEvidencia(evidencia);
        assertEquals(1, resultado);
    }
    
    @Test
    public void testObtenerListaDeEvidencias() throws SQLException {
        int idActividadColaborativaEsperado = 1;
        String rutaEvidenciaEsperada = "/ruta/evidencia";
        String nombreEvidenciaEsperado = "Evidencia 1.0";
        int idActividadColaborativa = 1;
        EvidenciaActividadDAO evidenciaActividadDAO = new EvidenciaActividadDAO();
        List<EvidenciaActividad> listaEvidencias = evidenciaActividadDAO.obtenerListaDeEvidencias(idActividadColaborativa);
        assertNotNull(listaEvidencias);
        assertFalse(listaEvidencias.isEmpty());
        for (EvidenciaActividad evidencia : listaEvidencias) {
            assertNotNull(evidencia);
            assertEquals(idActividadColaborativaEsperado, evidencia.getIdActividad());
            assertEquals(rutaEvidenciaEsperada, evidencia.getRutaEvidencia());
            assertEquals(nombreEvidenciaEsperado, evidencia.getNombre());
        }
    }

    @Test
    public void testObtenerEvidenciaPorIdEvidencia() throws SQLException {
        int idEvidenciaEsperado = 1;
        String rutaEvidenciaEsperada = "/ruta/evidencia";
        int idActividadColaborativaEsperado = 1;
        String nombreEvidenciaEsperado = "Evidencia 1.0";
        int idEvidencia = 1;
        EvidenciaActividadDAO evidenciaActividadDAO = new EvidenciaActividadDAO();
        EvidenciaActividad evidencia = evidenciaActividadDAO.obtenerEvidenciaPorIdEvidencia(idEvidencia);
        assertNotNull(evidencia);
        assertEquals(idEvidenciaEsperado, evidencia.getIdEvidencia());
        assertEquals(rutaEvidenciaEsperada, evidencia.getRutaEvidencia());
        assertEquals(idActividadColaborativaEsperado, evidencia.getIdActividad());
        assertEquals(nombreEvidenciaEsperado, evidencia.getNombre());
    }

    
    
}
