package implementacion;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Test;
import sdgcoilvic.logicaDeNegocio.clases.Estudiante;
import sdgcoilvic.logicaDeNegocio.implementacionDAO.EstudianteDAO;


public class EstudianteDAOTest {
    
        @Test    
    public void testRegistrarEstudianteExitoso() throws Exception {
        System.out.println("Registrando Estudiante");
        EstudianteDAO estudianteDAO = new EstudianteDAO();
        Estudiante estudiante = new Estudiante();
        estudiante.setNombre("Juan Eduardo");
        estudiante.setApellidoPaterno("Cumplido");
        estudiante.setApellidoMaterno("Negrete");
        estudiante.setCorreo("cumplido@example.com");
        estudiante.setClaveInstitucional("30MSU0940B");
        int idColaboracion = 1;
        int resultadoObtenido = estudianteDAO.registrarEstudiante(estudiante, idColaboracion);
        assertEquals(2, resultadoObtenido);
    }
    
    @Test 
    public void testRegistrarEstudianteNombreDuplicado() throws Exception {
        System.out.println("Registrando Estudiante");
        EstudianteDAO estudianteDAO = new EstudianteDAO();
        Estudiante estudiante = new Estudiante();
        estudiante.setNombre("Juan Eduardo");
        estudiante.setApellidoPaterno("Cumplido");
        estudiante.setApellidoMaterno("Negrete");
        estudiante.setCorreo("cumplido@example.com");
        estudiante.setClaveInstitucional("30MSU0940B");
        int idColaboracion = 1;
        boolean duplicado = estudianteDAO.verificarExistenciaEstudiante("Juan Eduardo","Cumplido","Negrete");
        if (!duplicado) {
            int resultadoObtenido = estudianteDAO.registrarEstudiante(estudiante, idColaboracion);
            assertEquals(2, resultadoObtenido);
        } else {
            assertEquals(true, duplicado);
        }
    } 
    
        @Test 
    public void testRegistrarEstudianteCorreoDuplicado() throws Exception {
        System.out.println("Registrando Estudiante");
        EstudianteDAO estudianteDAO = new EstudianteDAO();
        Estudiante estudiante = new Estudiante();
        estudiante.setNombre("Juan Eduardo");
        estudiante.setApellidoPaterno("Cumplido");
        estudiante.setApellidoMaterno("Negrete");
        estudiante.setCorreo("cumplido@example.com");
        estudiante.setClaveInstitucional("30MSU0940B");
        int idColaboracion = 1;
        boolean duplicado = estudianteDAO.verificarSiExisteElCorreo("cumplido@example.com");
        if (!duplicado) {
            int resultadoObtenido = estudianteDAO.registrarEstudiante(estudiante, idColaboracion);
            assertEquals(1, resultadoObtenido);
        } else {
            assertEquals(true, duplicado);
        }
    } 
    
    @Test (expected = IllegalArgumentException.class)   
    public void testRegistrarEstudianteNombreIncorrecto() throws Exception {
        System.out.println("Registrando Estudiante");
        EstudianteDAO estudianteDAO = new EstudianteDAO();
        Estudiante estudiante = new Estudiante();
        estudiante.setNombre("Juan +*#Eduardo");
        estudiante.setApellidoPaterno("Cumplido");
        estudiante.setApellidoMaterno("Negrete");
        estudiante.setCorreo("cumplido@example.com");
        estudiante.setClaveInstitucional("30MSU0940B");
        int idColaboracion = 1;
        int resultadoObtenido = estudianteDAO.registrarEstudiante(estudiante, idColaboracion);
        assertEquals(2, resultadoObtenido);
    }
    
    @Test (expected = IllegalArgumentException.class)   
    public void testRegistrarEstudianteNombreNulo() throws Exception {
        System.out.println("Registrando Estudiante");
        EstudianteDAO estudianteDAO = new EstudianteDAO();
        Estudiante estudiante = new Estudiante();
        estudiante.setNombre("");
        estudiante.setApellidoPaterno("Cumplido");
        estudiante.setApellidoMaterno("Negrete");
        estudiante.setCorreo("cumplido@example.com");
        estudiante.setClaveInstitucional("30MSU0940B");
        int idColaboracion = 1;
        int resultadoObtenido = estudianteDAO.registrarEstudiante(estudiante, idColaboracion);
        assertEquals(2, resultadoObtenido);
    }
    
        @Test (expected = IllegalArgumentException.class)   
    public void testRegistrarEstudianteApellidosIncorrectos() throws Exception {
        System.out.println("Registrando Estudiante");
        EstudianteDAO estudianteDAO = new EstudianteDAO();
        Estudiante estudiante = new Estudiante();
        estudiante.setNombre("Juan Eduardo");
        estudiante.setApellidoPaterno("Cumpl#+1ido");
        estudiante.setApellidoMaterno("Ne232grete");
        estudiante.setCorreo("cumplido@example.com");
        estudiante.setClaveInstitucional("30MSU0940B");
        int idColaboracion = 1;
        int resultadoObtenido = estudianteDAO.registrarEstudiante(estudiante, idColaboracion);
        assertEquals(2, resultadoObtenido);
    }
    
    @Test(expected = IllegalArgumentException.class)   
    public void testRegistrarEstudianteApellidoPaternoNulo() throws Exception {
        System.out.println("Registrando Estudiante");
        EstudianteDAO estudianteDAO = new EstudianteDAO();
        Estudiante estudiante = new Estudiante();
        estudiante.setNombre("Juan Eduardo");
        estudiante.setApellidoPaterno("");
        estudiante.setApellidoMaterno("Negrete");
        estudiante.setCorreo("cumplido@example.com");
        estudiante.setClaveInstitucional("30MSU0940B");
        int idColaboracion = 1;
        int resultadoObtenido = estudianteDAO.registrarEstudiante(estudiante, idColaboracion);
        assertEquals(2, resultadoObtenido);
    }
    
    @Test(expected = IllegalArgumentException.class)   
    public void testRegistrarEstudianteApellidoMaternoNulo() throws Exception {
        System.out.println("Registrando Estudiante");
        EstudianteDAO estudianteDAO = new EstudianteDAO();
        Estudiante estudiante = new Estudiante();
        estudiante.setNombre("Jose");
        estudiante.setApellidoPaterno("Cumplido");
        estudiante.setApellidoMaterno("");
        estudiante.setCorreo("jose@example.com");
        estudiante.setClaveInstitucional("30MSU0940B");
        int idColaboracion = 1;
        int resultadoObtenido = estudianteDAO.registrarEstudiante(estudiante, idColaboracion);
        assertEquals(2, resultadoObtenido);
    }
    
    @Test (expected = IllegalArgumentException.class)   
    public void testRegistrarEstudianteCorreoInvalido() throws Exception {
        System.out.println("Registrando Estudiante");
        EstudianteDAO estudianteDAO = new EstudianteDAO();
        Estudiante estudiante = new Estudiante();
        estudiante.setNombre("Mario");
        estudiante.setApellidoPaterno("Limon");
        estudiante.setApellidoMaterno("Cabrera");
        estudiante.setCorreo("limonexamplecom");
        estudiante.setClaveInstitucional("30MSU0940B");
        int idColaboracion = 1;
        int resultadoObtenido = estudianteDAO.registrarEstudiante(estudiante, idColaboracion);
        assertEquals(2, resultadoObtenido);
    }
    
    @Test 
    public void testRegistrarEstudianteClaveInexistente() throws Exception {
        System.out.println("Registrando Estudiante");
        EstudianteDAO estudianteDAO = new EstudianteDAO();
        Estudiante estudiante = new Estudiante();
        estudiante.setNombre("Mario");
        estudiante.setApellidoPaterno("Limon");
        estudiante.setApellidoMaterno("Cabrera");
        estudiante.setCorreo("limone@example.com");
        estudiante.setClaveInstitucional("clave1");
        int idColaboracion = 1;
        int resultadoObtenido = estudianteDAO.registrarEstudiante(estudiante, idColaboracion);
        assertEquals(-1, resultadoObtenido);
    }
      

    
}