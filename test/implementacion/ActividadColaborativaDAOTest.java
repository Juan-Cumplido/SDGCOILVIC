package implementacion;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import java.sql.SQLException;
import java.util.List;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import sdgcoilvic.logicaDeNegocio.clases.ActividadColaborativa;
import sdgcoilvic.logicaDeNegocio.implementacionDAO.ActividadColaborativaDAO;


public class ActividadColaborativaDAOTest {

    
    @Test
    public void testAgregarActividadColaborativa() throws SQLException {
        ActividadColaborativa actividad = new ActividadColaborativa();
        actividad.setNombreActividad("Primera actividad");
        actividad.setInstruccion("Analise correctamente las instrucciones");
        actividad.setIdColaboracion(1);
        actividad.setFechaInicio("2024-06-01");
        actividad.setFechaCierre("2024-11-01");
        actividad.setHerramienta("Internet");
        actividad.setEstadoActividad("Activa");
        actividad.setIdProfesor(1);
        ActividadColaborativaDAO dao = new ActividadColaborativaDAO();
        int resultado = dao.agregarActividadColaborativa(actividad);
        assertEquals(1, resultado);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAgregarActividadColaborativaNombreInvalido() throws SQLException {
        ActividadColaborativa actividad = new ActividadColaborativa();
        actividad.setNombreActividad("*/Primera actividad");
        actividad.setInstruccion("Analise correctamente las instrucciones");
        actividad.setIdColaboracion(1);
        actividad.setFechaInicio("2024-06-01");
        actividad.setFechaCierre("2024-11-01");
        actividad.setHerramienta("Internet");
        actividad.setEstadoActividad("Activa");
        actividad.setIdProfesor(1);
        ActividadColaborativaDAO dao = new ActividadColaborativaDAO();
        int resultado = dao.agregarActividadColaborativa(actividad);
        assertEquals(1, resultado);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAgregarActividadColaborativaNombreNulo() throws SQLException {
        ActividadColaborativa actividad = new ActividadColaborativa();
        actividad.setNombreActividad("");
        actividad.setInstruccion("Analise correctamente las instrucciones");
        actividad.setIdColaboracion(1);
        actividad.setFechaInicio("2024-06-01");
        actividad.setFechaCierre("2024-11-01");
        actividad.setHerramienta("Internet");
        actividad.setEstadoActividad("Activa");
        actividad.setIdProfesor(1);
        ActividadColaborativaDAO dao = new ActividadColaborativaDAO();
        int resultado = dao.agregarActividadColaborativa(actividad);
        assertEquals(1, resultado);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAgregarActividadColaborativaInstruccionInvalida() throws SQLException {
        ActividadColaborativa actividad = new ActividadColaborativa();
        actividad.setNombreActividad("Primera actividad");
        actividad.setInstruccion("*/Analise correctamente las instrucciones");
        actividad.setIdColaboracion(1);
        actividad.setFechaInicio("2024-06-01");
        actividad.setFechaCierre("2024-11-01");
        actividad.setHerramienta("Internet");
        actividad.setEstadoActividad("Activa");
        actividad.setIdProfesor(1);
        ActividadColaborativaDAO dao = new ActividadColaborativaDAO();
        int resultado = dao.agregarActividadColaborativa(actividad);
        assertEquals(1, resultado);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAgregarActividadColaborativaInstruccionNula() throws SQLException {
        ActividadColaborativa actividad = new ActividadColaborativa();
        actividad.setNombreActividad("Primera actividad");
        actividad.setInstruccion("");
        actividad.setIdColaboracion(1);
        actividad.setFechaInicio("2024-06-01");
        actividad.setFechaCierre("2024-11-01");
        actividad.setHerramienta("Internet");
        actividad.setEstadoActividad("Activa");
        actividad.setIdProfesor(1);
        ActividadColaborativaDAO dao = new ActividadColaborativaDAO();
        int resultado = dao.agregarActividadColaborativa(actividad);
        assertEquals(1, resultado);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAgregarActividadColaborativaHerramientaInvalida() throws SQLException {
        ActividadColaborativa actividad = new ActividadColaborativa();
        actividad.setNombreActividad("Primera actividad");
        actividad.setInstruccion("Analise correctamente las instrucciones");
        actividad.setIdColaboracion(1);
        actividad.setFechaInicio("2024-06-01");
        actividad.setFechaCierre("2024-11-01");
        actividad.setHerramienta("*/Internet");
        actividad.setEstadoActividad("Activa");
        actividad.setIdProfesor(1);
        ActividadColaborativaDAO dao = new ActividadColaborativaDAO();
        int resultado = dao.agregarActividadColaborativa(actividad);
        assertEquals(1, resultado);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAgregarActividadColaborativaHerramientaNula() throws SQLException {
        ActividadColaborativa actividad = new ActividadColaborativa();
        actividad.setNombreActividad("Primera actividad");
        actividad.setInstruccion("Analise correctamente las instrucciones");
        actividad.setIdColaboracion(1);
        actividad.setFechaInicio("2024-06-01");
        actividad.setFechaCierre("2024-11-01");
        actividad.setHerramienta("");
        actividad.setEstadoActividad("Activa");
        actividad.setIdProfesor(1);
        ActividadColaborativaDAO dao = new ActividadColaborativaDAO();
        int resultado = dao.agregarActividadColaborativa(actividad);
        assertEquals(1, resultado);
    }
    
    
    @Test
    public void testActualizarInformacionDeLaActividad() throws SQLException {
        ActividadColaborativaDAO actividadDAO = new ActividadColaborativaDAO();
        ActividadColaborativa actividad = new ActividadColaborativa();
        actividad.setNombreActividad("Primera actividad");
        actividad.setInstruccion("Instrucción");
        actividad.setFechaInicio("2024-05-01");
        actividad.setFechaCierre("2024-12-01");
        actividad.setHerramienta("Herramienta");
        actividad.setEstadoActividad("Activa");
        actividad.setIdProfesor(1);
        int idActividad = 1;
        int columnasAfectadas = actividadDAO.actualizarInformacionDeLaActividad(actividad, idActividad);
        assertTrue(columnasAfectadas > 0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testActualizarInformacionDeLaActividadNombreInvalido() throws SQLException {
        ActividadColaborativaDAO actividadDAO = new ActividadColaborativaDAO();
        ActividadColaborativa actividad = new ActividadColaborativa();
        actividad.setNombreActividad("*/Primera actividad");
        actividad.setInstruccion("Instrucción");
        actividad.setFechaInicio("2024-05-01");
        actividad.setFechaCierre("2024-12-01");
        actividad.setHerramienta("Herramienta");
        actividad.setEstadoActividad("Activa");
        actividad.setIdProfesor(1);
        int idActividad = 1;
        int columnasAfectadas = actividadDAO.actualizarInformacionDeLaActividad(actividad, idActividad);
        assertTrue(columnasAfectadas > 0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testActualizarInformacionDeLaActividadNombreNulo() throws SQLException {
        ActividadColaborativaDAO actividadDAO = new ActividadColaborativaDAO();
        ActividadColaborativa actividad = new ActividadColaborativa();
        actividad.setNombreActividad("");
        actividad.setInstruccion("Instrucción");
        actividad.setFechaInicio("2024-05-01");
        actividad.setFechaCierre("2024-12-01");
        actividad.setHerramienta("Herramienta");
        actividad.setEstadoActividad("Activa");
        actividad.setIdProfesor(1);
        int idActividad = 1;
        int columnasAfectadas = actividadDAO.actualizarInformacionDeLaActividad(actividad, idActividad);
        assertTrue(columnasAfectadas > 0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testActualizarInformacionDeLaActividadInstruccionInvalida() throws SQLException {
        ActividadColaborativaDAO actividadDAO = new ActividadColaborativaDAO();
        ActividadColaborativa actividad = new ActividadColaborativa();
        actividad.setNombreActividad("Primera actividad");
        actividad.setInstruccion("*/Instrucción");
        actividad.setFechaInicio("2024-05-01");
        actividad.setFechaCierre("2024-12-01");
        actividad.setHerramienta("Herramienta");
        actividad.setEstadoActividad("Activa");
        actividad.setIdProfesor(1);
        int idActividad = 1;
        int columnasAfectadas = actividadDAO.actualizarInformacionDeLaActividad(actividad, idActividad);
        assertTrue(columnasAfectadas > 0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testActualizarInformacionDeLaActividadInstruccionNula() throws SQLException {
        ActividadColaborativaDAO actividadDAO = new ActividadColaborativaDAO();
        ActividadColaborativa actividad = new ActividadColaborativa();
        actividad.setNombreActividad("Primera actividad");
        actividad.setInstruccion("");
        actividad.setFechaInicio("2024-05-01");
        actividad.setFechaCierre("2024-12-01");
        actividad.setHerramienta("Herramienta");
        actividad.setEstadoActividad("Activa");
        actividad.setIdProfesor(1);
        int idActividad = 1;
        int columnasAfectadas = actividadDAO.actualizarInformacionDeLaActividad(actividad, idActividad);
        assertTrue(columnasAfectadas > 0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testActualizarInformacionDeLaActividadHerramientaInvalida() throws SQLException {
        ActividadColaborativaDAO actividadDAO = new ActividadColaborativaDAO();
        ActividadColaborativa actividad = new ActividadColaborativa();
        actividad.setNombreActividad("Primera actividad");
        actividad.setInstruccion("Instrucción");
        actividad.setFechaInicio("2024-05-01");
        actividad.setFechaCierre("2024-12-01");
        actividad.setHerramienta("*/Herramienta");
        actividad.setEstadoActividad("Activa");
        actividad.setIdProfesor(1);
        int idActividad = 1;
        int columnasAfectadas = actividadDAO.actualizarInformacionDeLaActividad(actividad, idActividad);
        assertTrue(columnasAfectadas > 0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testActualizarInformacionDeLaActividadHerramientaNula() throws SQLException {
        ActividadColaborativaDAO actividadDAO = new ActividadColaborativaDAO();
        ActividadColaborativa actividad = new ActividadColaborativa();
        actividad.setNombreActividad("Primera actividad");
        actividad.setInstruccion("Instrucción");
        actividad.setFechaInicio("2024-05-01");
        actividad.setFechaCierre("2024-12-01");
        actividad.setHerramienta("");
        actividad.setEstadoActividad("Activa");
        actividad.setIdProfesor(1);
        int idActividad = 1;
        int columnasAfectadas = actividadDAO.actualizarInformacionDeLaActividad(actividad, idActividad);
        assertTrue(columnasAfectadas > 0);
    }
    
    @Test
    public void testConsultarActividadColaborativa() throws SQLException {
        ActividadColaborativaDAO actividadDAO = new ActividadColaborativaDAO();
        int idActividad = 1;
        ActividadColaborativa actividad = actividadDAO.consultarActividadColaborativa(idActividad);
        assertNotNull(actividad);
        assertEquals(1, actividad.getIdActividadColaborativa());
        assertEquals("Primera actividad", actividad.getNombreActividad());
        assertEquals("Instrucción", actividad.getInstruccion());
        assertEquals(1, actividad.getIdColaboracion());
        assertEquals("2024-05-01", actividad.getFechaInicio());
        assertEquals("2024-12-01", actividad.getFechaCierre());
        assertEquals("Herramienta", actividad.getHerramienta());
        assertEquals("Activa", actividad.getEstadoActividad());
        assertEquals(1, actividad.getIdProfesor());
    }

    
    @Test
    public void testVerificarDuenioActividad() throws SQLException {
        ActividadColaborativaDAO actividadDAO = new ActividadColaborativaDAO();
        int idProfesor = 1;
        List<Integer> listaDeActividades = actividadDAO.verificarDuenioActividad(idProfesor);
        assertNotNull(listaDeActividades);
        assertEquals(true, listaDeActividades.size() > 0);
    }
    
}
