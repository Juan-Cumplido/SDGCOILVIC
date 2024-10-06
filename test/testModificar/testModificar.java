package testModificar;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import sdgcoilvic.logicaDeNegocio.clases.Profesor;
import sdgcoilvic.logicaDeNegocio.implementacionDAO.ProfesorDAO;

public class testModificar {
    
       @Test
        public void testActualizarInformacionDelProfesorExitoso() throws Exception {
        System.out.println("Actualizando Información del Profesor");
 
        Profesor profesor = new Profesor();
        profesor.setNombre("ErickActualizado");
        profesor.setApellidoPaterno("AtzinActualizado");
        profesor.setApellidoMaterno("OlarteActualizado");
        profesor.setCorreo("atzin.actualizado@example.com");
        profesor.setIdIdiomas(1);
        profesor.setEstadoProfesor("Activo");
        profesor.setClaveInstitucional("30MSU0940B");
        profesor.setIdProfesor(2);
        
        ProfesorDAO profesorDAO = new ProfesorDAO();
        int resultadoObtenido = profesorDAO.actualizarInformacionDelProfesor(profesor, "2");
        assertEquals(1, resultadoObtenido);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testActualizarInformacionDelProfesorNombreIncorrecto() throws Exception {
        System.out.println("Actualizando Información del Profesor");
 
        Profesor profesor = new Profesor();
        profesor.setNombre("1ErickActualizado");
        profesor.setApellidoPaterno("AtzinActualizado");
        profesor.setApellidoMaterno("OlarteActualizado");
        profesor.setCorreo("atzin.actualizado@example.com");
        profesor.setIdIdiomas(1);
        profesor.setEstadoProfesor("Activo");
        profesor.setClaveInstitucional("30MSU0940B");
        profesor.setIdProfesor(1);
        
        ProfesorDAO profesorDAO = new ProfesorDAO();
        int resultadoObtenido = profesorDAO.actualizarInformacionDelProfesor(profesor, "1");
        assertEquals(1, resultadoObtenido);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testActualizarInformacionDelProfesorNombreNulo() throws Exception {
        System.out.println("Actualizando Información del Profesor");
 
        Profesor profesor = new Profesor();
        profesor.setNombre("");
        profesor.setApellidoPaterno("AtzinActualizado");
        profesor.setApellidoMaterno("OlarteActualizado");
        profesor.setCorreo("atzin.actualizado@example.com");
        profesor.setIdIdiomas(1);
        profesor.setEstadoProfesor("Activo");
        profesor.setClaveInstitucional("30MSU0940B");
        profesor.setIdProfesor(1);
        
        ProfesorDAO profesorDAO = new ProfesorDAO();
        int resultadoObtenido = profesorDAO.actualizarInformacionDelProfesor(profesor, "1");
        assertEquals(1, resultadoObtenido);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testActualizarInformacionDelProfesorApellidosIncorrecto() throws Exception {
        System.out.println("Actualizando Información del Profesor");
 
        Profesor profesor = new Profesor();
        profesor.setNombre("ErickActualizado");
        profesor.setApellidoPaterno("1AtzinActualizado");
        profesor.setApellidoMaterno("1OlarteActualizado");
        profesor.setCorreo("atzin.actualizado@example.com");
        profesor.setIdIdiomas(1);
        profesor.setEstadoProfesor("Activo");
        profesor.setClaveInstitucional("30MSU0940B");
        profesor.setIdProfesor(1);
        
        ProfesorDAO profesorDAO = new ProfesorDAO();
        int resultadoObtenido = profesorDAO.actualizarInformacionDelProfesor(profesor, "1");
        assertEquals(1, resultadoObtenido);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testActualizarInformacionDelProfesorApellidosNulos() throws Exception {
        System.out.println("Actualizando Información del Profesor");
 
        Profesor profesor = new Profesor();
        profesor.setNombre("ErickActualizado");
        profesor.setApellidoPaterno("");
        profesor.setApellidoMaterno("");
        profesor.setCorreo("atzin.actualizado@example.com");
        profesor.setIdIdiomas(1);
        profesor.setEstadoProfesor("Activo");
        profesor.setClaveInstitucional("30MSU0940B");
        profesor.setIdProfesor(1);
        
        ProfesorDAO profesorDAO = new ProfesorDAO();
        int resultadoObtenido = profesorDAO.actualizarInformacionDelProfesor(profesor, "1");
        assertEquals(1, resultadoObtenido);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testActualizarInformacionDelProfesorCorreoInvalido() throws Exception {
        System.out.println("Actualizando Información del Profesor");
 
        Profesor profesor = new Profesor();
        profesor.setNombre("ErickActualizado");
        profesor.setApellidoPaterno("AtzinActualizado");
        profesor.setApellidoMaterno("OlarteActualizado");
        profesor.setCorreo("atzin.actualizadoexample.com");
        profesor.setIdIdiomas(1);
        profesor.setEstadoProfesor("Activo");
        profesor.setClaveInstitucional("30MSU0940B");
        profesor.setIdProfesor(1);
        
        ProfesorDAO profesorDAO = new ProfesorDAO();
        int resultadoObtenido = profesorDAO.actualizarInformacionDelProfesor(profesor, "1");
        assertEquals(1, resultadoObtenido);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testActualizarInformacionDelProfesorCorreoNulo() throws Exception {
        System.out.println("Actualizando Información del Profesor");
 
        Profesor profesor = new Profesor();
        profesor.setNombre("ErickActualizado");
        profesor.setApellidoPaterno("AtzinActualizado");
        profesor.setApellidoMaterno("OlarteActualizado");
        profesor.setCorreo("");
        profesor.setIdIdiomas(1);
        profesor.setEstadoProfesor("Activo");
        profesor.setClaveInstitucional("30MSU0940B");
        profesor.setIdProfesor(1);
        
        ProfesorDAO profesorDAO = new ProfesorDAO();
        int resultadoObtenido = profesorDAO.actualizarInformacionDelProfesor(profesor, "1");
        assertEquals(1, resultadoObtenido);
    }
    
    @Test
    public void testActualizarInformacionDelProfesorIdiomaInexistente() throws Exception {
        System.out.println("Actualizando Información del Profesor");
 
        Profesor profesor = new Profesor();
        profesor.setNombre("ErickActualizado");
        profesor.setApellidoPaterno("AtzinActualizado");
        profesor.setApellidoMaterno("OlarteActualizado");
        profesor.setCorreo("atzin.actualizado@example.com");
        profesor.setIdIdiomas(10);
        profesor.setEstadoProfesor("Activo");
        profesor.setClaveInstitucional("30MSU0940B");
        profesor.setIdProfesor(1);
        
        ProfesorDAO profesorDAO = new ProfesorDAO();
        int resultadoObtenido = profesorDAO.actualizarInformacionDelProfesor(profesor, "1");
        assertEquals(0, resultadoObtenido);
    }
    
    @Test
    public void testActualizarInformacionDelProfesorClaveInvalida() throws Exception {
        System.out.println("Actualizando Información del Profesor");
 
        Profesor profesor = new Profesor();
        profesor.setNombre("ErickActualizado");
        profesor.setApellidoPaterno("AtzinActualizado");
        profesor.setApellidoMaterno("OlarteActualizado");
        profesor.setCorreo("atzin.actualizado@example.com");
        profesor.setIdIdiomas(1);
        profesor.setEstadoProfesor("Activo");
        profesor.setClaveInstitucional("claveficticia321");
        profesor.setIdProfesor(1);
        
        ProfesorDAO profesorDAO = new ProfesorDAO();
        int resultadoObtenido = profesorDAO.actualizarInformacionDelProfesor(profesor, "1");
        assertEquals(0, resultadoObtenido);
    }
    
    @Test
    public void testActualizarInformacionDelProfesorClaveNula() throws Exception {
        System.out.println("Actualizando Información del Profesor");
 
        Profesor profesor = new Profesor();
        profesor.setNombre("ErickActualizado");
        profesor.setApellidoPaterno("AtzinActualizado");
        profesor.setApellidoMaterno("OlarteActualizado");
        profesor.setCorreo("atzin.actualizado@example.com");
        profesor.setIdIdiomas(1);
        profesor.setEstadoProfesor("Activo");
        profesor.setClaveInstitucional("");
        profesor.setIdProfesor(1);
        
        ProfesorDAO profesorDAO = new ProfesorDAO();
        int resultadoObtenido = profesorDAO.actualizarInformacionDelProfesor(profesor, "1");
        assertEquals(0, resultadoObtenido);
    }


    
}
