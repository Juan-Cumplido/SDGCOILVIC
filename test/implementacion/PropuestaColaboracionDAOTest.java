package implementacion;
import java.sql.SQLException;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import sdgcoilvic.logicaDeNegocio.clases.Profesor;
import sdgcoilvic.logicaDeNegocio.clases.PropuestaColaboracion;
import sdgcoilvic.logicaDeNegocio.implementacionDAO.PropuestaColaboracionDAO;


public class PropuestaColaboracionDAOTest {

    @Test
    public void testAgregarPropuestaColaboracionExitoso() throws SQLException {
        PropuestaColaboracion propuesta = new PropuestaColaboracion();
        propuesta.setTipoColaboracion("Investigación");
        propuesta.setNombre("Proyecto de Investigación");
        propuesta.setObjetivoGeneral("Desarrollar nuevas técnicas de IA");
        propuesta.setTemas("Inteligencia Artificial, Machine Learning");
        propuesta.setNumeroEstudiante(15);
        propuesta.setInformacionAdicional("Colaboracion disponible");
        propuesta.setPerfilDeLosEstudiantes("Estudiantes de último año de Ingeniería");
        propuesta.setIdIdiomas(1);
        propuesta.setIdPeriodo(1);
        propuesta.setIdProfesor(1);
        propuesta.setEstadoPropuesta("Pendiente");
        PropuestaColaboracionDAO propuestaDAO = new PropuestaColaboracionDAO();
        int resultado = propuestaDAO.agregarPropuestaColaboracion(propuesta);
        assertEquals(1, resultado);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAgregarPropuestaColaboracionNombreInvalido() throws SQLException {
        PropuestaColaboracion propuesta = new PropuestaColaboracion();
        propuesta.setTipoColaboracion("Investigación");
        propuesta.setNombre("**Proyecto de Investigación");
        propuesta.setObjetivoGeneral("Desarrollar nuevas técnicas de IA");
        propuesta.setTemas("Inteligencia Artificial, Machine Learning");
        propuesta.setNumeroEstudiante(15);
        propuesta.setInformacionAdicional("Colaboracion disponible");
        propuesta.setPerfilDeLosEstudiantes("Estudiantes de último año de Ingeniería");
        propuesta.setIdIdiomas(1);
        propuesta.setIdPeriodo(1);
        propuesta.setIdProfesor(1);
        propuesta.setEstadoPropuesta("Pendiente");
        PropuestaColaboracionDAO propuestaDAO = new PropuestaColaboracionDAO();
        int resultado = propuestaDAO.agregarPropuestaColaboracion(propuesta);
        assertEquals(1, resultado);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAgregarPropuestaColaboracionNombreNulo() throws SQLException {
        PropuestaColaboracion propuesta = new PropuestaColaboracion();
        propuesta.setTipoColaboracion("Investigación");
        propuesta.setNombre("");
        propuesta.setObjetivoGeneral("Desarrollar nuevas técnicas de IA");
        propuesta.setTemas("Inteligencia Artificial, Machine Learning");
        propuesta.setNumeroEstudiante(15);
        propuesta.setInformacionAdicional("Colaboracion disponible");
        propuesta.setPerfilDeLosEstudiantes("Estudiantes de último año de Ingeniería");
        propuesta.setIdIdiomas(1);
        propuesta.setIdPeriodo(1);
        propuesta.setIdProfesor(1);
        propuesta.setEstadoPropuesta("Pendiente");
        PropuestaColaboracionDAO propuestaDAO = new PropuestaColaboracionDAO();
        int resultado = propuestaDAO.agregarPropuestaColaboracion(propuesta);
        assertEquals(1, resultado);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAgregarPropuestaColaboracionObjetivoInvalido() throws SQLException {

        PropuestaColaboracion propuesta = new PropuestaColaboracion();
        propuesta.setTipoColaboracion("Investigación");
        propuesta.setNombre("Proyecto de Investigación");
        propuesta.setObjetivoGeneral("+*Desarrollar nuevas técnicas de IA");
        propuesta.setTemas("Inteligencia Artificial, Machine Learning");
        propuesta.setNumeroEstudiante(15);
        propuesta.setInformacionAdicional("Colaboracion disponible");
        propuesta.setPerfilDeLosEstudiantes("Estudiantes de último año de Ingeniería");
        propuesta.setIdIdiomas(1);
        propuesta.setIdPeriodo(1);
        propuesta.setIdProfesor(1);
        propuesta.setEstadoPropuesta("Pendiente");

        PropuestaColaboracionDAO propuestaDAO = new PropuestaColaboracionDAO();

        int resultado = propuestaDAO.agregarPropuestaColaboracion(propuesta);
        assertEquals(1, resultado);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAgregarPropuestaColaboracionObjetivoNulo() throws SQLException {

        PropuestaColaboracion propuesta = new PropuestaColaboracion();
        propuesta.setTipoColaboracion("Investigación");
        propuesta.setNombre("Proyecto de Investigación");
        propuesta.setObjetivoGeneral("");
        propuesta.setTemas("Inteligencia Artificial, Machine Learning");
        propuesta.setNumeroEstudiante(15);
        propuesta.setInformacionAdicional("Colaboracion disponible");
        propuesta.setPerfilDeLosEstudiantes("Estudiantes de último año de Ingeniería");
        propuesta.setIdIdiomas(1);
        propuesta.setIdPeriodo(1);
        propuesta.setIdProfesor(1);
        propuesta.setEstadoPropuesta("Pendiente");

        PropuestaColaboracionDAO propuestaDAO = new PropuestaColaboracionDAO();

        int resultado = propuestaDAO.agregarPropuestaColaboracion(propuesta);
        assertEquals(1, resultado);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAgregarPropuestaColaboracionTemaInvalido() throws SQLException {

        PropuestaColaboracion propuesta = new PropuestaColaboracion();
        propuesta.setTipoColaboracion("Investigación");
        propuesta.setNombre("Proyecto de Investigación");
        propuesta.setObjetivoGeneral("Desarrollar nuevas técnicas de IA");
        propuesta.setTemas("@*Inteligencia Artificial, Machine Learning");
        propuesta.setNumeroEstudiante(15);
        propuesta.setInformacionAdicional("Colaboracion disponible");
        propuesta.setPerfilDeLosEstudiantes("Estudiantes de último año de Ingeniería");
        propuesta.setIdIdiomas(1);
        propuesta.setIdPeriodo(1);
        propuesta.setIdProfesor(1);
        propuesta.setEstadoPropuesta("Pendiente");

        PropuestaColaboracionDAO propuestaDAO = new PropuestaColaboracionDAO();

        int resultado = propuestaDAO.agregarPropuestaColaboracion(propuesta);
        assertEquals(1, resultado);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAgregarPropuestaColaboracionTemaNulo() throws SQLException {

        PropuestaColaboracion propuesta = new PropuestaColaboracion();
        propuesta.setTipoColaboracion("Investigación");
        propuesta.setNombre("Proyecto de Investigación");
        propuesta.setObjetivoGeneral("Desarrollar nuevas técnicas de IA");
        propuesta.setTemas("");
        propuesta.setNumeroEstudiante(15);
        propuesta.setInformacionAdicional("Colaboracion disponible");
        propuesta.setPerfilDeLosEstudiantes("Estudiantes de último año de Ingeniería");
        propuesta.setIdIdiomas(1);
        propuesta.setIdPeriodo(1);
        propuesta.setIdProfesor(1);
        propuesta.setEstadoPropuesta("Pendiente");

        PropuestaColaboracionDAO propuestaDAO = new PropuestaColaboracionDAO();

        int resultado = propuestaDAO.agregarPropuestaColaboracion(propuesta);
        assertEquals(1, resultado);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAgregarPropuestaColaboracionEstudiantesInsuficientes() throws SQLException {

        PropuestaColaboracion propuesta = new PropuestaColaboracion();
        propuesta.setTipoColaboracion("Investigación");
        propuesta.setNombre("Proyecto de Investigación");
        propuesta.setObjetivoGeneral("Desarrollar nuevas técnicas de IA");
        propuesta.setTemas("Inteligencia Artificial, Machine Learning");
        propuesta.setNumeroEstudiante(0);
        propuesta.setInformacionAdicional("Colaboracion disponible");
        propuesta.setPerfilDeLosEstudiantes("Estudiantes de último año de Ingeniería");
        propuesta.setIdIdiomas(1);
        propuesta.setIdPeriodo(1);
        propuesta.setIdProfesor(1);
        propuesta.setEstadoPropuesta("Pendiente");

        PropuestaColaboracionDAO propuestaDAO = new PropuestaColaboracionDAO();

        int resultado = propuestaDAO.agregarPropuestaColaboracion(propuesta);
        assertEquals(1, resultado);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAgregarPropuestaColaboracionInformacionAdicionalInvalida() throws SQLException {

        PropuestaColaboracion propuesta = new PropuestaColaboracion();
        propuesta.setTipoColaboracion("Investigación");
        propuesta.setNombre("Proyecto de Investigación");
        propuesta.setObjetivoGeneral("Desarrollar nuevas técnicas de IA");
        propuesta.setTemas("Inteligencia Artificial, Machine Learning");
        propuesta.setNumeroEstudiante(15);
        propuesta.setInformacionAdicional("@Colaboracion disponible");
        propuesta.setPerfilDeLosEstudiantes("Estudiantes de último año de Ingeniería");
        propuesta.setIdIdiomas(1);
        propuesta.setIdPeriodo(1);
        propuesta.setIdProfesor(1);
        propuesta.setEstadoPropuesta("Pendiente");

        PropuestaColaboracionDAO propuestaDAO = new PropuestaColaboracionDAO();

        int resultado = propuestaDAO.agregarPropuestaColaboracion(propuesta);
        assertEquals(1, resultado);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAgregarPropuestaColaboracionInformacionAdicionalNula() throws SQLException {

        PropuestaColaboracion propuesta = new PropuestaColaboracion();
        propuesta.setTipoColaboracion("Investigación");
        propuesta.setNombre("Proyecto de Investigación");
        propuesta.setObjetivoGeneral("Desarrollar nuevas técnicas de IA");
        propuesta.setTemas("Inteligencia Artificial, Machine Learning");
        propuesta.setNumeroEstudiante(15);
        propuesta.setInformacionAdicional("");
        propuesta.setPerfilDeLosEstudiantes("Estudiantes de último año de Ingeniería");
        propuesta.setIdIdiomas(1);
        propuesta.setIdPeriodo(1);
        propuesta.setIdProfesor(1);
        propuesta.setEstadoPropuesta("Pendiente");

        PropuestaColaboracionDAO propuestaDAO = new PropuestaColaboracionDAO();

        int resultado = propuestaDAO.agregarPropuestaColaboracion(propuesta);
        assertEquals(1, resultado);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAgregarPropuestaColaboracionPerfilEstudianteInvalido() throws SQLException {

        PropuestaColaboracion propuesta = new PropuestaColaboracion();
        propuesta.setTipoColaboracion("Investigación");
        propuesta.setNombre("Proyecto de Investigación");
        propuesta.setObjetivoGeneral("Desarrollar nuevas técnicas de IA");
        propuesta.setTemas("Inteligencia Artificial, Machine Learning");
        propuesta.setNumeroEstudiante(15);
        propuesta.setInformacionAdicional("Colaboracion disponible");
        propuesta.setPerfilDeLosEstudiantes("1Estudiantes de último año de Ingeniería");
        propuesta.setIdIdiomas(1);
        propuesta.setIdPeriodo(1);
        propuesta.setIdProfesor(1);
        propuesta.setEstadoPropuesta("Pendiente");

        PropuestaColaboracionDAO propuestaDAO = new PropuestaColaboracionDAO();

        int resultado = propuestaDAO.agregarPropuestaColaboracion(propuesta);
        assertEquals(1, resultado);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAgregarPropuestaColaboracionPerfilEstudianteCaracteres() throws SQLException {

        PropuestaColaboracion propuesta = new PropuestaColaboracion();
        propuesta.setTipoColaboracion("Investigación");
        propuesta.setNombre("Proyecto de Investigación");
        propuesta.setObjetivoGeneral("Desarrollar nuevas técnicas de IA");
        propuesta.setTemas("Inteligencia Artificial, Machine Learning");
        propuesta.setNumeroEstudiante(15);
        propuesta.setInformacionAdicional("Colaboracion disponible");
        propuesta.setPerfilDeLosEstudiantes("@*Estudiantes de último año de Ingeniería");
        propuesta.setIdIdiomas(1);
        propuesta.setIdPeriodo(1);
        propuesta.setIdProfesor(1);
        propuesta.setEstadoPropuesta("Pendiente");

        PropuestaColaboracionDAO propuestaDAO = new PropuestaColaboracionDAO();

        int resultado = propuestaDAO.agregarPropuestaColaboracion(propuesta);
        assertEquals(1, resultado);
    }
    
    @Test
    public void testConsultarTodasLasPropuestasColaboracionEnEspera() throws SQLException {
        PropuestaColaboracionDAO propuestaDAO = new PropuestaColaboracionDAO();
        List<PropuestaColaboracion> propuestasEnEspera = propuestaDAO.consultarTodasLasPropuestasColaboracionEnEspera();
        assertEquals(false, propuestasEnEspera.isEmpty());

        for (PropuestaColaboracion propuesta : propuestasEnEspera) {
            assertEquals("Investigación", propuesta.getTipoColaboracion());
            assertEquals("Proyecto de Investigación", propuesta.getNombre());
            assertEquals("Desarrollar nuevas técnicas de IA", propuesta.getObjetivoGeneral());
            assertEquals("Inteligencia Artificial, Machine Learning", propuesta.getTemas());
            assertEquals(15, propuesta.getNumeroEstudiante());
            assertEquals("Colaboracion disponible", propuesta.getInformacionAdicional());
            assertEquals("Estudiantes de último año de Ingeniería", propuesta.getPerfilDeLosEstudiantes());
            assertEquals(1, propuesta.getIdIdiomas());
            assertEquals(1, propuesta.getIdPeriodo());
            assertEquals(1, propuesta.getIdProfesor());
            assertEquals("EnEspera", propuesta.getEstadoPropuesta());
        }
    }
    
    @Test
    public void testActualizarInformacionPropuestaExitoso() throws SQLException {
        PropuestaColaboracion propuestaActualizada = new PropuestaColaboracion();
        propuestaActualizada.setTipoColaboracion("AcInvestigación");
        propuestaActualizada.setNombre("AcProyecto de Investigación");
        propuestaActualizada.setObjetivoGeneral("AcDesarrollar nuevas técnicas de IA");
        propuestaActualizada.setTemas("AcInteligencia Artificial, Machine Learning");
        propuestaActualizada.setNumeroEstudiante(15);
        propuestaActualizada.setInformacionAdicional("AcColaboracion disponible");
        propuestaActualizada.setPerfilDeLosEstudiantes("AcEstudiantes de último año de Ingeniería");
        propuestaActualizada.setIdIdiomas(1);
        propuestaActualizada.setIdPeriodo(1);
        propuestaActualizada.setEstadoPropuesta("EnEspera");
        PropuestaColaboracionDAO propuestaDAO = new PropuestaColaboracionDAO();

        int idPropuesta = 1;

        int resultado = propuestaDAO.actualizarInformacionDeLaPropuesta(propuestaActualizada, idPropuesta);
        assertEquals(1, resultado);
      }
    
    @Test(expected = IllegalArgumentException.class)
    public void testActualizarInformacionPropuestaNombreInvalido() throws SQLException {
        PropuestaColaboracion propuestaActualizada = new PropuestaColaboracion();
        propuestaActualizada.setTipoColaboracion("AcInvestigación");
        propuestaActualizada.setNombre("@*AcProyecto de Investigación");
        propuestaActualizada.setObjetivoGeneral("AcDesarrollar nuevas técnicas de IA");
        propuestaActualizada.setTemas("AcInteligencia Artificial, Machine Learning");
        propuestaActualizada.setNumeroEstudiante(15);
        propuestaActualizada.setInformacionAdicional("AcColaboracion disponible");
        propuestaActualizada.setPerfilDeLosEstudiantes("AcEstudiantes de último año de Ingeniería");
        propuestaActualizada.setIdIdiomas(1);
        propuestaActualizada.setIdPeriodo(1);
        propuestaActualizada.setEstadoPropuesta("EnEspera");

        PropuestaColaboracionDAO propuestaDAO = new PropuestaColaboracionDAO();

        int idPropuesta = 1;

        int resultado = propuestaDAO.actualizarInformacionDeLaPropuesta(propuestaActualizada, idPropuesta);
        assertEquals(1, resultado);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testActualizarInformacionPropuestaNombreNulo() throws SQLException {
        PropuestaColaboracion propuestaActualizada = new PropuestaColaboracion();
        propuestaActualizada.setTipoColaboracion("AcInvestigación");
        propuestaActualizada.setNombre("");
        propuestaActualizada.setObjetivoGeneral("AcDesarrollar nuevas técnicas de IA");
        propuestaActualizada.setTemas("AcInteligencia Artificial, Machine Learning");
        propuestaActualizada.setNumeroEstudiante(15);
        propuestaActualizada.setInformacionAdicional("AcColaboracion disponible");
        propuestaActualizada.setPerfilDeLosEstudiantes("AcEstudiantes de último año de Ingeniería");
        propuestaActualizada.setIdIdiomas(1);
        propuestaActualizada.setIdPeriodo(1);
        propuestaActualizada.setEstadoPropuesta("EnEspera");

        PropuestaColaboracionDAO propuestaDAO = new PropuestaColaboracionDAO();

        int idPropuesta = 1;

        int resultado = propuestaDAO.actualizarInformacionDeLaPropuesta(propuestaActualizada, idPropuesta);
        assertEquals(1, resultado);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testActualizarInformacionPropuestaObjetivoInvalido() throws SQLException {
        PropuestaColaboracion propuestaActualizada = new PropuestaColaboracion();
        propuestaActualizada.setTipoColaboracion("AcInvestigación");
        propuestaActualizada.setNombre("AcProyecto de Investigación");
        propuestaActualizada.setObjetivoGeneral("*/AcDesarrollar nuevas técnicas de IA");
        propuestaActualizada.setTemas("AcInteligencia Artificial, Machine Learning");
        propuestaActualizada.setNumeroEstudiante(15);
        propuestaActualizada.setInformacionAdicional("AcColaboracion disponible");
        propuestaActualizada.setPerfilDeLosEstudiantes("AcEstudiantes de último año de Ingeniería");
        propuestaActualizada.setIdIdiomas(1);
        propuestaActualizada.setIdPeriodo(1);
        propuestaActualizada.setEstadoPropuesta("EnEspera");

        PropuestaColaboracionDAO propuestaDAO = new PropuestaColaboracionDAO();

        int idPropuesta = 1;
        int resultado = propuestaDAO.actualizarInformacionDeLaPropuesta(propuestaActualizada, idPropuesta);
        assertEquals(1, resultado);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testActualizarInformacionPropuestaObjetivoNulo() throws SQLException {
        PropuestaColaboracion propuestaActualizada = new PropuestaColaboracion();
        propuestaActualizada.setTipoColaboracion("AcInvestigación");
        propuestaActualizada.setNombre("AcProyecto de Investigación");
        propuestaActualizada.setObjetivoGeneral("");
        propuestaActualizada.setTemas("AcInteligencia Artificial, Machine Learning");
        propuestaActualizada.setNumeroEstudiante(15);
        propuestaActualizada.setInformacionAdicional("AcColaboracion disponible");
        propuestaActualizada.setPerfilDeLosEstudiantes("AcEstudiantes de último año de Ingeniería");
        propuestaActualizada.setIdIdiomas(1);
        propuestaActualizada.setIdPeriodo(1);
        propuestaActualizada.setEstadoPropuesta("EnEspera");

        PropuestaColaboracionDAO propuestaDAO = new PropuestaColaboracionDAO();

        int idPropuesta = 1;

        int resultado = propuestaDAO.actualizarInformacionDeLaPropuesta(propuestaActualizada, idPropuesta);
        assertEquals(1, resultado);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testActualizarInformacionPropuestaTemaInvalido() throws SQLException {
        PropuestaColaboracion propuestaActualizada = new PropuestaColaboracion();
        propuestaActualizada.setTipoColaboracion("AcInvestigación");
        propuestaActualizada.setNombre("AcProyecto de Investigación");
        propuestaActualizada.setObjetivoGeneral("AcDesarrollar nuevas técnicas de IA");
        propuestaActualizada.setTemas("*/AcInteligencia Artificial, Machine Learning");
        propuestaActualizada.setNumeroEstudiante(15);
        propuestaActualizada.setInformacionAdicional("AcColaboracion disponible");
        propuestaActualizada.setPerfilDeLosEstudiantes("AcEstudiantes de último año de Ingeniería");
        propuestaActualizada.setIdIdiomas(1);
        propuestaActualizada.setIdPeriodo(1);
        propuestaActualizada.setEstadoPropuesta("EnEspera");

        PropuestaColaboracionDAO propuestaDAO = new PropuestaColaboracionDAO();

        int idPropuesta = 1;

        int resultado = propuestaDAO.actualizarInformacionDeLaPropuesta(propuestaActualizada, idPropuesta);
        assertEquals(1, resultado);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testActualizarInformacionPropuestaTemaNulo() throws SQLException {
        PropuestaColaboracion propuestaActualizada = new PropuestaColaboracion();
        propuestaActualizada.setTipoColaboracion("AcInvestigación");
        propuestaActualizada.setNombre("AcProyecto de Investigación");
        propuestaActualizada.setObjetivoGeneral("AcDesarrollar nuevas técnicas de IA");
        propuestaActualizada.setTemas("");
        propuestaActualizada.setNumeroEstudiante(15);
        propuestaActualizada.setInformacionAdicional("AcColaboracion disponible");
        propuestaActualizada.setPerfilDeLosEstudiantes("AcEstudiantes de último año de Ingeniería");
        propuestaActualizada.setIdIdiomas(1);
        propuestaActualizada.setIdPeriodo(1);
        propuestaActualizada.setEstadoPropuesta("EnEspera");

        PropuestaColaboracionDAO propuestaDAO = new PropuestaColaboracionDAO();

        int idPropuesta = 1;

        int resultado = propuestaDAO.actualizarInformacionDeLaPropuesta(propuestaActualizada, idPropuesta);
        assertEquals(1, resultado);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testActualizarInformacionPropuestaInformacionInvalida() throws SQLException {
        PropuestaColaboracion propuestaActualizada = new PropuestaColaboracion();
        propuestaActualizada.setTipoColaboracion("AcInvestigación");
        propuestaActualizada.setNombre("AcProyecto de Investigación");
        propuestaActualizada.setObjetivoGeneral("AcDesarrollar nuevas técnicas de IA");
        propuestaActualizada.setTemas("AcInteligencia Artificial, Machine Learning");
        propuestaActualizada.setNumeroEstudiante(15);
        propuestaActualizada.setInformacionAdicional("*/AcColaboracion disponible");
        propuestaActualizada.setPerfilDeLosEstudiantes("AcEstudiantes de último año de Ingeniería");
        propuestaActualizada.setIdIdiomas(1);
        propuestaActualizada.setIdPeriodo(1);
        propuestaActualizada.setEstadoPropuesta("EnEspera");

        PropuestaColaboracionDAO propuestaDAO = new PropuestaColaboracionDAO();

        int idPropuesta = 1;

        int resultado = propuestaDAO.actualizarInformacionDeLaPropuesta(propuestaActualizada, idPropuesta);
        assertEquals(1, resultado);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testActualizarInformacionPropuestaInformacionNula() throws SQLException {
        PropuestaColaboracion propuestaActualizada = new PropuestaColaboracion();
        propuestaActualizada.setTipoColaboracion("AcInvestigación");
        propuestaActualizada.setNombre("AcProyecto de Investigación");
        propuestaActualizada.setObjetivoGeneral("AcDesarrollar nuevas técnicas de IA");
        propuestaActualizada.setTemas("AcInteligencia Artificial, Machine Learning");
        propuestaActualizada.setNumeroEstudiante(15);
        propuestaActualizada.setInformacionAdicional("");
        propuestaActualizada.setPerfilDeLosEstudiantes("AcEstudiantes de último año de Ingeniería");
        propuestaActualizada.setIdIdiomas(1);
        propuestaActualizada.setIdPeriodo(1);
        propuestaActualizada.setEstadoPropuesta("EnEspera");

        PropuestaColaboracionDAO propuestaDAO = new PropuestaColaboracionDAO();

        int idPropuesta = 1;

        int resultado = propuestaDAO.actualizarInformacionDeLaPropuesta(propuestaActualizada, idPropuesta);
        assertEquals(1, resultado);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testActualizarInformacionPropuestaPerfilInvalido() throws SQLException {
        PropuestaColaboracion propuestaActualizada = new PropuestaColaboracion();
        propuestaActualizada.setTipoColaboracion("AcInvestigación");
        propuestaActualizada.setNombre("AcProyecto de Investigación");
        propuestaActualizada.setObjetivoGeneral("AcDesarrollar nuevas técnicas de IA");
        propuestaActualizada.setTemas("AcInteligencia Artificial, Machine Learning");
        propuestaActualizada.setNumeroEstudiante(15);
        propuestaActualizada.setInformacionAdicional("AcColaboracion disponible");
        propuestaActualizada.setPerfilDeLosEstudiantes("*/AcEstudiantes de último año de Ingeniería");
        propuestaActualizada.setIdIdiomas(1);
        propuestaActualizada.setIdPeriodo(1);
        propuestaActualizada.setEstadoPropuesta("EnEspera");

        PropuestaColaboracionDAO propuestaDAO = new PropuestaColaboracionDAO();

        int idPropuesta = 1;

        int resultado = propuestaDAO.actualizarInformacionDeLaPropuesta(propuestaActualizada, idPropuesta);
        assertEquals(1, resultado);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testActualizarInformacionPropuestaPerfilNulo() throws SQLException {
        PropuestaColaboracion propuestaActualizada = new PropuestaColaboracion();
        propuestaActualizada.setTipoColaboracion("AcInvestigación");
        propuestaActualizada.setNombre("AcProyecto de Investigación");
        propuestaActualizada.setObjetivoGeneral("AcDesarrollar nuevas técnicas de IA");
        propuestaActualizada.setTemas("AcInteligencia Artificial, Machine Learning");
        propuestaActualizada.setNumeroEstudiante(15);
        propuestaActualizada.setInformacionAdicional("AcColaboracion disponible");
        propuestaActualizada.setPerfilDeLosEstudiantes("");
        propuestaActualizada.setIdIdiomas(1);
        propuestaActualizada.setIdPeriodo(1);
        propuestaActualizada.setEstadoPropuesta("EnEspera");

        PropuestaColaboracionDAO propuestaDAO = new PropuestaColaboracionDAO();

        int idPropuesta = 1;

        int resultado = propuestaDAO.actualizarInformacionDeLaPropuesta(propuestaActualizada, idPropuesta);
        assertEquals(1, resultado);
    }
    
    @Test
    public void testEvaluarPropuestaColaboracionExitoso() throws SQLException {
        PropuestaColaboracionDAO propuestaDAO = new PropuestaColaboracionDAO();
        int idPropuesta = 1;
        String nuevaEvaluacion = "Aceptada";

        int columnasAfectadas = propuestaDAO.evaluarPropuestaColaboracion(idPropuesta, nuevaEvaluacion);
        assertEquals(1, columnasAfectadas);
    }

    @Test
    public void testConsultarTodasLasPropuestasColaboracionAceptadas() throws SQLException {
        PropuestaColaboracionDAO propuestaDAO = new PropuestaColaboracionDAO();
        List<PropuestaColaboracion> propuestasAceptadas = propuestaDAO.consultarTodasLasPropuestasColaboracionAceptadas();
        assertEquals(false, propuestasAceptadas.isEmpty());

        for (PropuestaColaboracion propuesta : propuestasAceptadas) {
            assertEquals("AcInvestigación", propuesta.getTipoColaboracion());
            assertEquals("AcProyecto de Investigación", propuesta.getNombre());
            assertEquals("AcDesarrollar nuevas técnicas de IA", propuesta.getObjetivoGeneral());
            assertEquals("AcInteligencia Artificial, Machine Learning", propuesta.getTemas());
            assertEquals(15, propuesta.getNumeroEstudiante());
            assertEquals("AcColaboracion disponible", propuesta.getInformacionAdicional());
            assertEquals("AcEstudiantes de último año de Ingeniería", propuesta.getPerfilDeLosEstudiantes());
            assertEquals(1, propuesta.getIdIdiomas());
            assertEquals(1, propuesta.getIdPeriodo());
            assertEquals(1, propuesta.getIdProfesor());
            assertEquals("Aceptada", propuesta.getEstadoPropuesta());
        }
    }
    
    @Test
    public void testOfertarPropuestaColaboracionExitoso() throws SQLException {
        PropuestaColaboracionDAO propuestaDAO = new PropuestaColaboracionDAO();
        int idPropuesta = 1;
        String nuevaEvaluacion = "Ofertada";

        int columnasAfectadas = propuestaDAO.evaluarPropuestaColaboracion(idPropuesta, nuevaEvaluacion);
        assertEquals(1, columnasAfectadas);
    }
    
    @Test
    public void testConsultarTodasLasPropuestasColaboracionOfertadas() throws SQLException {
        PropuestaColaboracionDAO propuestaDAO = new PropuestaColaboracionDAO();
        List<PropuestaColaboracion> propuestasOfertadas = propuestaDAO.consultarTodasLasPropuestasColaboracionOfertadas(2);
        assertEquals(false, propuestasOfertadas.isEmpty());

        for (PropuestaColaboracion propuesta : propuestasOfertadas) {
            assertEquals("AcInvestigación", propuesta.getTipoColaboracion());
            assertEquals("AcProyecto de Investigación", propuesta.getNombre());
            assertEquals("AcDesarrollar nuevas técnicas de IA", propuesta.getObjetivoGeneral());
            assertEquals("AcInteligencia Artificial, Machine Learning", propuesta.getTemas());
            assertEquals(15, propuesta.getNumeroEstudiante());
            assertEquals("AcColaboracion disponible", propuesta.getInformacionAdicional());
            assertEquals("AcEstudiantes de último año de Ingeniería", propuesta.getPerfilDeLosEstudiantes());
            assertEquals(1, propuesta.getIdIdiomas());
            assertEquals(1, propuesta.getIdPeriodo());
            assertEquals(1, propuesta.getIdProfesor());
            assertEquals("Ofertada", propuesta.getEstadoPropuesta());
        }
    }
    
    @Test
    public void testConsultarPropuestasColaboracionPorProfesor() throws SQLException {
        int idProfesor = 1;
        PropuestaColaboracionDAO propuestaDAO = new PropuestaColaboracionDAO();
        List<PropuestaColaboracion> propuestasPorProfesor = propuestaDAO.consultarPropuestasColaboracionPorProfesor(idProfesor);
        assertEquals(false, propuestasPorProfesor.isEmpty());
        for (PropuestaColaboracion propuesta : propuestasPorProfesor) {
            assertEquals("AcInvestigación", propuesta.getTipoColaboracion());
            assertEquals("AcProyecto de Investigación", propuesta.getNombre());
            assertEquals("AcDesarrollar nuevas técnicas de IA", propuesta.getObjetivoGeneral());
            assertEquals("AcInteligencia Artificial, Machine Learning", propuesta.getTemas());
            assertEquals(15, propuesta.getNumeroEstudiante());
            assertEquals("AcColaboracion disponible", propuesta.getInformacionAdicional());
            assertEquals("AcEstudiantes de último año de Ingeniería", propuesta.getPerfilDeLosEstudiantes());
            assertEquals(1, propuesta.getIdIdiomas());
            assertEquals(1, propuesta.getIdPeriodo());
            assertEquals(1, propuesta.getIdProfesor());
            assertEquals("Ofertada", propuesta.getEstadoPropuesta());
        }
    }
     
    @Test
    public void testObtenerPropuestasColaboracionPorIdPropuesta() throws SQLException {
        int idPropuesta = 1;
        PropuestaColaboracionDAO propuestaDAO = new PropuestaColaboracionDAO();
        PropuestaColaboracion propuesta = propuestaDAO.obtenerPropuestasColaboracionPorIdPropuesta(idPropuesta);
        assertNotNull(propuesta);
        assertEquals("AcInvestigación", propuesta.getTipoColaboracion());
        assertEquals("AcProyecto de Investigación", propuesta.getNombre());
        assertEquals("AcDesarrollar nuevas técnicas de IA", propuesta.getObjetivoGeneral());
        assertEquals("AcInteligencia Artificial, Machine Learning", propuesta.getTemas());
        assertEquals(15, propuesta.getNumeroEstudiante());
        assertEquals("AcColaboracion disponible", propuesta.getInformacionAdicional());
        assertEquals("AcEstudiantes de último año de Ingeniería", propuesta.getPerfilDeLosEstudiantes());
        assertEquals(1, propuesta.getIdIdiomas());
        assertEquals(1, propuesta.getIdPeriodo());
        assertEquals(1, propuesta.getIdProfesor());
        assertEquals("Ofertada", propuesta.getEstadoPropuesta());
    }

    @Test
    public void testObtenerProfesorPorId() throws SQLException {
        int idProfesor = 1; 
        PropuestaColaboracionDAO propuestaColaboracionDAO = new PropuestaColaboracionDAO();
        Profesor profesor = propuestaColaboracionDAO.obtenerProfesorPorid(idProfesor);
        assertNotNull(profesor);
        assertEquals(idProfesor, profesor.getIdProfesor());
        assertEquals("Erick Atzin Olarte", profesor.getNombre()); 
        assertEquals("UNIVERSIDAD VERACRUZANA", profesor.getClaveInstitucional());
    }

    @Test
    public void testObtenerCorreoPorIdPropuesta() throws SQLException {
        int idPropuesta = 1; 
        PropuestaColaboracionDAO propuestaColaboracionDAO = new PropuestaColaboracionDAO();
        String correo = propuestaColaboracionDAO.obtenerCorreoPorIdPropuesta(idPropuesta);      
        assertEquals("atzin@example.com", correo); 
    }
    
    @Test
    public void testRevertirEstadoExitoso() throws SQLException {
        PropuestaColaboracionDAO propuestaDAO = new PropuestaColaboracionDAO();
        int idPropuesta = 2;
        int columnasAfectadas = propuestaDAO.reevertirEstado(idPropuesta);
        assertEquals(1, columnasAfectadas);
    }
}
