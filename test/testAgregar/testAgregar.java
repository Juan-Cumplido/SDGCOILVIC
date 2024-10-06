
package testAgregar;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import sdgcoilvic.logicaDeNegocio.clases.Acceso;
import sdgcoilvic.logicaDeNegocio.clases.Profesor;
import sdgcoilvic.logicaDeNegocio.enums.EnumTipoDeAcceso;
import sdgcoilvic.logicaDeNegocio.implementacionDAO.ProfesorDAO;



public class testAgregar {
    
    @Test    
    public void testRegistrarProfesorExitoso() throws Exception {
        System.out.println("Registrando Profesor");
        Profesor profesor = new Profesor();
        profesor.setNombre("Erick");
        profesor.setApellidoPaterno("Atzin");
        profesor.setApellidoMaterno("Olarte");
        profesor.setCorreo("atzin@example.com");
        profesor.setIdIdiomas(1);
        profesor.setIdAcceso(1);
        profesor.setEstadoProfesor("Activo");
        profesor.setClaveInstitucional("30MSU0940B");

        Acceso acceso = new Acceso();
        acceso.setContrasenia("erick*Atzin1@");
        acceso.setUsuario("atzin@example.com");
        acceso.setTipoUsuario(EnumTipoDeAcceso.Profesor.toString());
        ProfesorDAO profesorDAO = new ProfesorDAO();
        int resultadoObtenido = profesorDAO.registrarProfesor(profesor, acceso);
        assertEquals(1, resultadoObtenido);
    }
    
    @Test 
    public void testRegistrarProfesorNombreDuplicado() throws Exception {
        System.out.println("Registrando Profesor");

        Profesor profesor = new Profesor();
        profesor.setNombre("Erick");
        profesor.setApellidoPaterno("Atzin");
        profesor.setApellidoMaterno("Olarte");
        profesor.setCorreo("erick@example.com");
        profesor.setIdIdiomas(2);
        profesor.setIdAcceso(1);
        profesor.setEstadoProfesor("Activo");
        profesor.setClaveInstitucional("30MSU0940B");

        Acceso acceso = new Acceso();
        acceso.setContrasenia("erick*Atzin1@");
        acceso.setUsuario("erick@example.com");
        acceso.setTipoUsuario(EnumTipoDeAcceso.Profesor.toString());
        ProfesorDAO profesorDAO = new ProfesorDAO();
        boolean duplicado = profesorDAO.verificarExistenciaProfesor("Erick","Atzin","Olarte");

        if (!duplicado) {
            int resultadoObtenido = profesorDAO.registrarProfesor(profesor, acceso);
            assertEquals(1, resultadoObtenido);
        } else {
            assertEquals(true, duplicado);
        }
    } 
    
    @Test
    public void testRegistrarProfesorCorreoDuplicado() throws Exception {
        System.out.println("Registrando Profesor");

        Profesor profesor = new Profesor();
        profesor.setNombre("Erick");
        profesor.setApellidoPaterno("Cumplido");
        profesor.setApellidoMaterno("Olarte");
        profesor.setCorreo("atzin@example.com");
        profesor.setIdIdiomas(1);
        profesor.setIdAcceso(1);
        profesor.setEstadoProfesor("Activo");
        profesor.setClaveInstitucional("30MSU0940B");

        Acceso acceso = new Acceso();
        acceso.setContrasenia("erick*Atzin1@");
        acceso.setUsuario("atzin@example.com");
        acceso.setTipoUsuario(EnumTipoDeAcceso.Profesor.toString());

        ProfesorDAO profesorDAO = new ProfesorDAO();
        boolean duplicado = profesorDAO.verificarSiExisteElCorreo("atzin@example.com");
        if (!duplicado) {
            int resultadoObtenido = profesorDAO.registrarProfesor(profesor, acceso);
            assertEquals(1, resultadoObtenido);
        } else {
            assertEquals(true, duplicado);
        }
    }
    
    @Test
    public void testRegistrarProfesorDuplicado() throws Exception {
        System.out.println("Registrando Profesor");

        Profesor profesor = new Profesor();
        profesor.setNombre("Erick");
        profesor.setApellidoPaterno("Atzin");
        profesor.setApellidoMaterno("Olarte");
        profesor.setCorreo("atzin@example.com");
        profesor.setIdIdiomas(1);
        profesor.setIdAcceso(1);
        profesor.setEstadoProfesor("Activo");
        profesor.setClaveInstitucional("30MSU0940B");

        Acceso acceso = new Acceso();
        acceso.setContrasenia("erick*Atzin1@");
        acceso.setUsuario("atzin@example.com");
        acceso.setTipoUsuario(EnumTipoDeAcceso.Profesor.toString());
        ProfesorDAO profesorDAO = new ProfesorDAO();
        boolean duplicado = profesorDAO.verificarProfesorDuplicado(profesor);

        if (!duplicado) {
            int resultadoObtenido = profesorDAO.registrarProfesor(profesor, acceso);
            assertEquals(1, resultadoObtenido);
        } else {
            assertEquals(true, duplicado);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegistrarProfesorNombreIncorrecto() throws Exception {
        System.out.println("Registrando Profesor");

        Profesor profesor = new Profesor();
        profesor.setNombre("1Erick");
        profesor.setApellidoPaterno("Atzin");
        profesor.setApellidoMaterno("Olarte");
        profesor.setCorreo("atzin@example.com");
        profesor.setIdIdiomas(1);
        profesor.setIdAcceso(1);
        profesor.setEstadoProfesor("Activo");
        profesor.setClaveInstitucional("30MSU0940B");

        Acceso acceso = new Acceso();
        acceso.setContrasenia("erick*Atzin1@");
        acceso.setUsuario("atzin@example.com");
        acceso.setTipoUsuario(EnumTipoDeAcceso.Profesor.toString());
        ProfesorDAO profesorDAO = new ProfesorDAO();
        int resultadoObtenido = profesorDAO.registrarProfesor(profesor, acceso);
        assertEquals(1, resultadoObtenido);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testRegistrarProfesorNombreNulo() throws Exception {
        System.out.println("Registrando Profesor");

        Profesor profesor = new Profesor();
        profesor.setNombre("");
        profesor.setApellidoPaterno("Atzin");
        profesor.setApellidoMaterno("Olarte");
        profesor.setCorreo("atzin@example.com");
        profesor.setIdIdiomas(1);
        profesor.setIdAcceso(1);
        profesor.setEstadoProfesor("Activo");
        profesor.setClaveInstitucional("30MSU0940B");

        Acceso acceso = new Acceso();
        acceso.setContrasenia("erick*Atzin1@");
        acceso.setUsuario("atzin@example.com");
        acceso.setTipoUsuario(EnumTipoDeAcceso.Profesor.toString());
        ProfesorDAO profesorDAO = new ProfesorDAO();
        int resultadoObtenido = profesorDAO.registrarProfesor(profesor, acceso);
        assertEquals(1, resultadoObtenido);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testRegistrarProfesorApellidosIncorrectos() throws Exception {
        System.out.println("Registrando Profesor");

        Profesor profesor = new Profesor();
        profesor.setNombre("Erick");
        profesor.setApellidoPaterno("1Atzin");
        profesor.setApellidoMaterno("1Olarte");
        profesor.setCorreo("atzin@example.com");
        profesor.setIdIdiomas(1);
        profesor.setIdAcceso(1);
        profesor.setEstadoProfesor("Activo");
        profesor.setClaveInstitucional("30MSU0940B");

        Acceso acceso = new Acceso();
        acceso.setContrasenia("erick*Atzin1@");
        acceso.setUsuario("atzin@example.com");
        acceso.setTipoUsuario(EnumTipoDeAcceso.Profesor.toString());
        ProfesorDAO profesorDAO = new ProfesorDAO();
        int resultadoObtenido = profesorDAO.registrarProfesor(profesor, acceso);
        assertEquals(1, resultadoObtenido);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testRegistrarProfesorApellidosNulos() throws Exception {
        System.out.println("Registrando Profesor");

        Profesor profesor = new Profesor();
        profesor.setNombre("Erick");
        profesor.setApellidoPaterno("");
        profesor.setApellidoMaterno("");
        profesor.setCorreo("atzin@example.com");
        profesor.setIdIdiomas(1);
        profesor.setIdAcceso(1);
        profesor.setEstadoProfesor("Activo");
        profesor.setClaveInstitucional("30MSU0940B");

        Acceso acceso = new Acceso();
        acceso.setContrasenia("erick*Atzin1@");
        acceso.setUsuario("atzin@example.com");
        acceso.setTipoUsuario(EnumTipoDeAcceso.Profesor.toString());
        ProfesorDAO profesorDAO = new ProfesorDAO();
        int resultadoObtenido = profesorDAO.registrarProfesor(profesor, acceso);
        assertEquals(1, resultadoObtenido);
    }
    
    
    @Test(expected = IllegalArgumentException.class)
    public void testRegistrarProfesorCorreoInvalido() throws Exception {
        System.out.println("Registrando Profesor");

        Profesor profesor = new Profesor();
        profesor.setNombre("Erick");
        profesor.setApellidoPaterno("Atzin");
        profesor.setApellidoMaterno("Olarte");
        profesor.setCorreo("atzinexamplecom");
        profesor.setIdIdiomas(1);
        profesor.setIdAcceso(1);
        profesor.setEstadoProfesor("Activo");
        profesor.setClaveInstitucional("30MSU0940B");

        Acceso acceso = new Acceso();
        acceso.setContrasenia("erick*Atzin1@");
        acceso.setUsuario("atzin@example.com");
        acceso.setTipoUsuario(EnumTipoDeAcceso.Profesor.toString());
        ProfesorDAO profesorDAO = new ProfesorDAO();
        int resultadoObtenido = profesorDAO.registrarProfesor(profesor, acceso);
        assertEquals(1, resultadoObtenido);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegistrarProfesorCorreoNulo() throws Exception {
        System.out.println("Registrando Profesor");

        Profesor profesor = new Profesor();
        profesor.setNombre("Erick");
        profesor.setApellidoPaterno("Atzin");
        profesor.setApellidoMaterno("Olarte");
        profesor.setCorreo("");
        profesor.setIdIdiomas(1);
        profesor.setIdAcceso(1);
        profesor.setEstadoProfesor("Activo");
        profesor.setClaveInstitucional("30MSU0940B");

        Acceso acceso = new Acceso();
        acceso.setContrasenia("erick*Atzin1@");
        acceso.setUsuario("atzin@example.com");
        acceso.setTipoUsuario(EnumTipoDeAcceso.Profesor.toString());
        ProfesorDAO profesorDAO = new ProfesorDAO();
        int resultadoObtenido = profesorDAO.registrarProfesor(profesor, acceso);
        assertEquals(1, resultadoObtenido);
    }
    
    @Test
    public void testRegistrarProfesorIdiomaInexistente() throws Exception {
        System.out.println("Registrando Profesor");

        Profesor profesor = new Profesor();
        profesor.setNombre("Erick");
        profesor.setApellidoPaterno("Atzin");
        profesor.setApellidoMaterno("Olarte");
        profesor.setCorreo("atzin@example.com");
        profesor.setIdIdiomas(10);
        profesor.setIdAcceso(1);
        profesor.setEstadoProfesor("Activo");
        profesor.setClaveInstitucional("30MSU0940B");

        Acceso acceso = new Acceso();
        acceso.setContrasenia("erick*Atzin1@");
        acceso.setUsuario("atzin@example.com");
        acceso.setTipoUsuario(EnumTipoDeAcceso.Profesor.toString());
        ProfesorDAO profesorDAO = new ProfesorDAO();
        int resultadoObtenido = profesorDAO.registrarProfesor(profesor, acceso);
        assertEquals(-1, resultadoObtenido);
    }

    @Test
    public void testRegistrarProfesorClaveInexistente() throws Exception {
        System.out.println("Registrando Profesor");

        Profesor profesor = new Profesor();
        profesor.setNombre("Erick");
        profesor.setApellidoPaterno("Atzin");
        profesor.setApellidoMaterno("Olarte");
        profesor.setCorreo("atzin@example.com");
        profesor.setIdIdiomas(1);
        profesor.setIdAcceso(1);
        profesor.setEstadoProfesor("Activo");
        profesor.setClaveInstitucional("Clave Inexistente 123");

        Acceso acceso = new Acceso();
        acceso.setContrasenia("erick*Atzin1@");
        acceso.setUsuario("atzin@example.com");
        acceso.setTipoUsuario(EnumTipoDeAcceso.Profesor.toString());
        ProfesorDAO profesorDAO = new ProfesorDAO();
        int resultadoObtenido = profesorDAO.registrarProfesor(profesor, acceso);
        assertEquals(-1, resultadoObtenido);
    }
    
    @Test
    public void testRegistrarProfesorClaveNula() throws Exception {
        System.out.println("Registrando Profesor");

        Profesor profesor = new Profesor();
        profesor.setNombre("Erick");
        profesor.setApellidoPaterno("Atzin");
        profesor.setApellidoMaterno("Olarte");
        profesor.setCorreo("atzin@example.com");
        profesor.setIdIdiomas(1);
        profesor.setIdAcceso(1);
        profesor.setEstadoProfesor("Activo");
        profesor.setClaveInstitucional("");

        Acceso acceso = new Acceso();
        acceso.setContrasenia("erick*Atzin1@");
        acceso.setUsuario("atzin@example.com");
        acceso.setTipoUsuario(EnumTipoDeAcceso.Profesor.toString());
        ProfesorDAO profesorDAO = new ProfesorDAO();
        int resultadoObtenido = profesorDAO.registrarProfesor(profesor, acceso);
        assertEquals(-1, resultadoObtenido);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testRegistrarProfesorCaracteresEspeciales() throws Exception {
        System.out.println("Registrando Profesor");

        Profesor profesor = new Profesor();
        profesor.setNombre("***Erick");
        profesor.setApellidoPaterno("#Atzin");
        profesor.setApellidoMaterno("#Olarte");
        profesor.setCorreo("atzin@example.com");
        profesor.setIdIdiomas(1);
        profesor.setIdAcceso(1);
        profesor.setEstadoProfesor("Activo");
        profesor.setClaveInstitucional("30MSU0940B");

        Acceso acceso = new Acceso();
        acceso.setContrasenia("erick*Atzin1@");
        acceso.setUsuario("atzin@example.com");
        acceso.setTipoUsuario(EnumTipoDeAcceso.Profesor.toString());
        ProfesorDAO profesorDAO = new ProfesorDAO();
        int resultadoObtenido = profesorDAO.registrarProfesor(profesor, acceso);
        assertEquals(1, resultadoObtenido);
    }
    
}
