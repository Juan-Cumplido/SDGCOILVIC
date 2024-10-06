package implementacion;
import java.sql.SQLException;
import java.util.List;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import sdgcoilvic.logicaDeNegocio.clases.Colaboracion;
import sdgcoilvic.logicaDeNegocio.clases.PropuestaColaboracion;
import sdgcoilvic.logicaDeNegocio.implementacionDAO.ColaboracionDAO;


public class ColaboracionDAOTest {

    @Test
    public void testCrearColaboracionExitoso() throws SQLException {
        Colaboracion colaboracion = new Colaboracion();
        colaboracion.setIdPropuestaColaboracion(1); 
        colaboracion.setDescripcion("Proyecto de Investigacion");
        colaboracion.setRecursos("Conexión a internet");
        colaboracion.setAprendizajesEsperados("Desarrollar nuevas tecnicas de IA");
        colaboracion.setDetallesAsistenciaParticipacion("Estudiantes de ultimo año de ingenieria");
        colaboracion.setDetallesEvaluacion("Examenes periodicos");
        colaboracion.setDetallesEntorno("Aulas virtuales");        
        int idProfesor = 1;

        ColaboracionDAO colaboracionDAO = new ColaboracionDAO();
        int idColaboracion = colaboracionDAO.crearColaboracion(colaboracion, idProfesor);
        assert(idColaboracion > -1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCrearColaboracionDescripcionInvalida() throws SQLException {
        Colaboracion colaboracion = new Colaboracion();
        colaboracion.setIdPropuestaColaboracion(1); 
        colaboracion.setDescripcion("/*Proyecto de Investigacion");
        colaboracion.setRecursos("Conexión a internet");
        colaboracion.setAprendizajesEsperados("Desarrollar nuevas tecnicas de IA");
        colaboracion.setDetallesAsistenciaParticipacion("Estudiantes de ultimo año de ingenieria");
        colaboracion.setDetallesEvaluacion("Examenes periodicos");
        colaboracion.setDetallesEntorno("Aulas virtuales");        
        int idProfesor = 1;

        ColaboracionDAO colaboracionDAO = new ColaboracionDAO();
        int idColaboracion = colaboracionDAO.crearColaboracion(colaboracion, idProfesor);
        assert(idColaboracion > -1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCrearColaboracionDescripcionNula() throws SQLException {
        Colaboracion colaboracion = new Colaboracion();
        colaboracion.setIdPropuestaColaboracion(1); 
        colaboracion.setDescripcion("");
        colaboracion.setRecursos("Conexión a internet");
        colaboracion.setAprendizajesEsperados("Desarrollar nuevas tecnicas de IA");
        colaboracion.setDetallesAsistenciaParticipacion("Estudiantes de ultimo año de ingenieria");
        colaboracion.setDetallesEvaluacion("Examenes periodicos");
        colaboracion.setDetallesEntorno("Aulas virtuales");        
        int idProfesor = 1;

        ColaboracionDAO colaboracionDAO = new ColaboracionDAO();
        int idColaboracion = colaboracionDAO.crearColaboracion(colaboracion, idProfesor);
        assert(idColaboracion > -1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCrearColaboracionRecursosInvalidos() throws SQLException {
        Colaboracion colaboracion = new Colaboracion();
        colaboracion.setIdPropuestaColaboracion(1); 
        colaboracion.setDescripcion("Proyecto de Investigacion");
        colaboracion.setRecursos("/*Conexión a internet");
        colaboracion.setAprendizajesEsperados("Desarrollar nuevas tecnicas de IA");
        colaboracion.setDetallesAsistenciaParticipacion("Estudiantes de ultimo año de ingenieria");
        colaboracion.setDetallesEvaluacion("Examenes periodicos");
        colaboracion.setDetallesEntorno("Aulas virtuales");        
        int idProfesor = 1;

        ColaboracionDAO colaboracionDAO = new ColaboracionDAO();
        int idColaboracion = colaboracionDAO.crearColaboracion(colaboracion, idProfesor);
        assert(idColaboracion > -1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCrearColaboracionRecursosNulos() throws SQLException {
        Colaboracion colaboracion = new Colaboracion();
        colaboracion.setIdPropuestaColaboracion(1); 
        colaboracion.setDescripcion("Proyecto de Investigacion");
        colaboracion.setRecursos("");
        colaboracion.setAprendizajesEsperados("Desarrollar nuevas tecnicas de IA");
        colaboracion.setDetallesAsistenciaParticipacion("Estudiantes de ultimo año de ingenieria");
        colaboracion.setDetallesEvaluacion("Examenes periodicos");
        colaboracion.setDetallesEntorno("Aulas virtuales");        
        int idProfesor = 1;

        ColaboracionDAO colaboracionDAO = new ColaboracionDAO();
        int idColaboracion = colaboracionDAO.crearColaboracion(colaboracion, idProfesor);
        assert(idColaboracion > -1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCrearColaboracionAprendizajesInvalidos() throws SQLException {
        Colaboracion colaboracion = new Colaboracion();
        colaboracion.setIdPropuestaColaboracion(1); 
        colaboracion.setDescripcion("Proyecto de Investigacion");
        colaboracion.setRecursos("Conexión a internet");
        colaboracion.setAprendizajesEsperados("/*Desarrollar nuevas tecnicas de IA");
        colaboracion.setDetallesAsistenciaParticipacion("Estudiantes de ultimo año de ingenieria");
        colaboracion.setDetallesEvaluacion("Examenes periodicos");
        colaboracion.setDetallesEntorno("Aulas virtuales");        
        int idProfesor = 1;

        ColaboracionDAO colaboracionDAO = new ColaboracionDAO();
        int idColaboracion = colaboracionDAO.crearColaboracion(colaboracion, idProfesor);
        assert(idColaboracion > -1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCrearColaboracionAprendizajesNulos() throws SQLException {
        Colaboracion colaboracion = new Colaboracion();
        colaboracion.setIdPropuestaColaboracion(1); 
        colaboracion.setDescripcion("Proyecto de Investigacion");
        colaboracion.setRecursos("Conexión a internet");
        colaboracion.setAprendizajesEsperados("");
        colaboracion.setDetallesAsistenciaParticipacion("Estudiantes de ultimo año de ingenieria");
        colaboracion.setDetallesEvaluacion("Examenes periodicos");
        colaboracion.setDetallesEntorno("Aulas virtuales");        
        int idProfesor = 1;

        ColaboracionDAO colaboracionDAO = new ColaboracionDAO();
        int idColaboracion = colaboracionDAO.crearColaboracion(colaboracion, idProfesor);
        assert(idColaboracion > -1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCrearColaboracionAsistenciaInvalida() throws SQLException {
        Colaboracion colaboracion = new Colaboracion();
        colaboracion.setIdPropuestaColaboracion(1); 
        colaboracion.setDescripcion("Proyecto de Investigacion");
        colaboracion.setRecursos("Conexión a internet");
        colaboracion.setAprendizajesEsperados("Desarrollar nuevas tecnicas de IA");
        colaboracion.setDetallesAsistenciaParticipacion("/*Estudiantes de ultimo año de ingenieria");
        colaboracion.setDetallesEvaluacion("Examenes periodicos");
        colaboracion.setDetallesEntorno("Aulas virtuales");        
        int idProfesor = 1;

        ColaboracionDAO colaboracionDAO = new ColaboracionDAO();
        int idColaboracion = colaboracionDAO.crearColaboracion(colaboracion, idProfesor);
        assert(idColaboracion > -1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCrearColaboracionAsistenciaNula() throws SQLException {
        Colaboracion colaboracion = new Colaboracion();
        colaboracion.setIdPropuestaColaboracion(1); 
        colaboracion.setDescripcion("Proyecto de Investigacion");
        colaboracion.setRecursos("Conexión a internet");
        colaboracion.setAprendizajesEsperados("Desarrollar nuevas tecnicas de IA");
        colaboracion.setDetallesAsistenciaParticipacion("");
        colaboracion.setDetallesEvaluacion("Examenes periodicos");
        colaboracion.setDetallesEntorno("Aulas virtuales");        
        int idProfesor = 1;

        ColaboracionDAO colaboracionDAO = new ColaboracionDAO();
        int idColaboracion = colaboracionDAO.crearColaboracion(colaboracion, idProfesor);
        assert(idColaboracion > -1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCrearColaboracionEvaluacionInvalida() throws SQLException {
        Colaboracion colaboracion = new Colaboracion();
        colaboracion.setIdPropuestaColaboracion(1); 
        colaboracion.setDescripcion("Proyecto de Investigacion");
        colaboracion.setRecursos("Conexión a internet");
        colaboracion.setAprendizajesEsperados("Desarrollar nuevas tecnicas de IA");
        colaboracion.setDetallesAsistenciaParticipacion("Estudiantes de ultimo año de ingenieria");
        colaboracion.setDetallesEvaluacion("/*Examenes periodicos");
        colaboracion.setDetallesEntorno("Aulas virtuales");        
        int idProfesor = 1;

        ColaboracionDAO colaboracionDAO = new ColaboracionDAO();
        int idColaboracion = colaboracionDAO.crearColaboracion(colaboracion, idProfesor);
        assert(idColaboracion > -1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCrearColaboracionEvaluacionNula() throws SQLException {
        Colaboracion colaboracion = new Colaboracion();
        colaboracion.setIdPropuestaColaboracion(1); 
        colaboracion.setDescripcion("Proyecto de Investigacion");
        colaboracion.setRecursos("Conexión a internet");
        colaboracion.setAprendizajesEsperados("Desarrollar nuevas tecnicas de IA");
        colaboracion.setDetallesAsistenciaParticipacion("Estudiantes de ultimo año de ingenieria");
        colaboracion.setDetallesEvaluacion("");
        colaboracion.setDetallesEntorno("Aulas virtuales");        
        int idProfesor = 1;

        ColaboracionDAO colaboracionDAO = new ColaboracionDAO();
        int idColaboracion = colaboracionDAO.crearColaboracion(colaboracion, idProfesor);
        assert(idColaboracion > -1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCrearColaboracionDetallesInvalidos() throws SQLException {
        Colaboracion colaboracion = new Colaboracion();
        colaboracion.setIdPropuestaColaboracion(1); 
        colaboracion.setDescripcion("Proyecto de Investigacion");
        colaboracion.setRecursos("Conexión a internet");
        colaboracion.setAprendizajesEsperados("Desarrollar nuevas tecnicas de IA");
        colaboracion.setDetallesAsistenciaParticipacion("Estudiantes de ultimo año de ingenieria");
        colaboracion.setDetallesEvaluacion("Examenes periodicos");
        colaboracion.setDetallesEntorno("/*Aulas virtuales");        
        int idProfesor = 1;

        ColaboracionDAO colaboracionDAO = new ColaboracionDAO();
        int idColaboracion = colaboracionDAO.crearColaboracion(colaboracion, idProfesor);
        assert(idColaboracion > -1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCrearColaboracionDetallesNulos() throws SQLException {
        Colaboracion colaboracion = new Colaboracion();
        colaboracion.setIdPropuestaColaboracion(1); 
        colaboracion.setDescripcion("Proyecto de Investigacion");
        colaboracion.setRecursos("Conexión a internet");
        colaboracion.setAprendizajesEsperados("Desarrollar nuevas tecnicas de IA");
        colaboracion.setDetallesAsistenciaParticipacion("Estudiantes de ultimo año de ingenieria");
        colaboracion.setDetallesEvaluacion("Examenes periodicos");
        colaboracion.setDetallesEntorno("");        
        int idProfesor = 1;

        ColaboracionDAO colaboracionDAO = new ColaboracionDAO();
        int idColaboracion = colaboracionDAO.crearColaboracion(colaboracion, idProfesor);
        assert(idColaboracion > -1);
    }
    @Test
    public void testObtenerNombreProfesores() throws SQLException {
        int idColaboracion = 1; 
        ColaboracionDAO colaboracionDAO = new ColaboracionDAO();
        List<String> nombresProfesores = colaboracionDAO.obtenerNombreProfesores(idColaboracion);
        assertEquals(1, nombresProfesores.size());
        assertEquals("Erick Atzin Olarte", nombresProfesores.get(0));
    }
    
    @Test
    public void testObtenerIdColaboracionEnCurso() throws SQLException {
        int idProfesor = 1; 
        ColaboracionDAO colaboracionDAO = new ColaboracionDAO();
        int idColaboracion = colaboracionDAO.obtenerIdColaboracionEnCurso(idProfesor);
        assertEquals(1, idColaboracion); 
    }
    
    @Test
    public void testObtenerPropuestaColaboracion() throws SQLException {
        int idColaboracion = 1; 
        ColaboracionDAO colaboracionDAO = new ColaboracionDAO();
        PropuestaColaboracion propuesta = colaboracionDAO.obtenerPropuestaColaboracion(idColaboracion);
        assertEquals("AcInvestigación", propuesta.getTipoColaboracion());
        assertEquals("AcProyecto de Investigación", propuesta.getNombre());
        assertEquals("AcDesarrollar nuevas técnicas de IA", propuesta.getObjetivoGeneral());
    }
   
    
    @Test
    public void testFinalizarColaboracion() throws SQLException {
        int idColaboracion = 1; 
        ColaboracionDAO colaboracionDAO = new ColaboracionDAO();
        int resultado = colaboracionDAO.finalizarColaboracion(idColaboracion);
        assertEquals(1, resultado);
    }
}
