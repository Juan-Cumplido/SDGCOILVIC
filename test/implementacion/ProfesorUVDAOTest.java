package implementacion;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import sdgcoilvic.logicaDeNegocio.implementacionDAO.ProfesorUVDAO;
import sdgcoilvic.logicaDeNegocio.clases.Acceso;
import sdgcoilvic.logicaDeNegocio.clases.ProfesorUV;
import sdgcoilvic.logicaDeNegocio.enums.EnumTipoDeAcceso;
import sdgcoilvic.logicaDeNegocio.implementacionDAO.ProfesorDAO;

public class ProfesorUVDAOTest {
  
    @Test
    public void testRegistrarProfesorUVExitoso() throws SQLException {
        ProfesorUVDAO instanciaProfesorUV = new ProfesorUVDAO();
        ProfesorUV profesorUV = new ProfesorUV();

        profesorUV.setNoPersonal("12345");
        profesorUV.setDisciplina("Ingenieria");
        profesorUV.setIdRegion(1);
        profesorUV.setIdCategoriaContratacionUV(1);
        profesorUV.setIdTipoContratacionUV(1);
        profesorUV.setIdAreaAcademica(1);
        profesorUV.setNombre("Erick");
        profesorUV.setApellidoPaterno("Atzin");
        profesorUV.setApellidoMaterno("Olarte");
        profesorUV.setCorreo("atzin@example.com");
        profesorUV.setIdIdiomas(1);
        profesorUV.setEstadoProfesor("Activo");
        profesorUV.setClaveInstitucional("30MSU0940B");
        Acceso acceso = new Acceso();
        acceso.setUsuario("atzin@example.com");
        acceso.setContrasenia("erick*Atzin1@");
        acceso.setTipoUsuario(EnumTipoDeAcceso.Profesor.toString()); 

        int resultadoEsperado = 2; 
        int resultadoObtenido = instanciaProfesorUV.registrarProfesorUV(acceso, profesorUV);
        assertEquals(resultadoEsperado, resultadoObtenido);
    }
    
    @Test
    public void testRegistrarProfesorUVNombreDuplicado() throws SQLException {
        ProfesorUVDAO instanciaProfesorUV = new ProfesorUVDAO();
        ProfesorUV profesorUV = new ProfesorUV();

        profesorUV.setNoPersonal("12345");
        profesorUV.setDisciplina("Ingenieria");
        profesorUV.setIdRegion(1);
        profesorUV.setIdCategoriaContratacionUV(1);
        profesorUV.setIdTipoContratacionUV(1);
        profesorUV.setIdAreaAcademica(1);
        profesorUV.setNombre("Erick");
        profesorUV.setApellidoPaterno("Atzin");
        profesorUV.setApellidoMaterno("Olarte");
        profesorUV.setCorreo("atzin@example.com");
        profesorUV.setIdIdiomas(1);
        profesorUV.setEstadoProfesor("Activo");
        profesorUV.setClaveInstitucional("30MSU0940B");
        
        Acceso acceso = new Acceso();
        acceso.setUsuario("atzin@example.com");
        acceso.setContrasenia("erick*Atzin1@");
        acceso.setTipoUsuario(EnumTipoDeAcceso.Profesor.toString());
        ProfesorDAO profesorDAO = new ProfesorDAO();
        boolean duplicado = profesorDAO.verificarExistenciaProfesor("Erick","Atzin","Olarte");

        if (!duplicado) {
            int resultadoObtenido = profesorDAO.registrarProfesor(profesorUV, acceso);
            assertEquals(1, resultadoObtenido);
        } else {
            assertEquals(true, duplicado);
        }
    }
    
    @Test 
    public void testRegistrarProfesorUVNoPersonalDuplicado() throws SQLException {
        ProfesorUVDAO instanciaProfesorUV = new ProfesorUVDAO();
        ProfesorUV profesorUV = new ProfesorUV();

        profesorUV.setNoPersonal("12345");
        profesorUV.setDisciplina("Ingenieria");
        profesorUV.setIdRegion(1);
        profesorUV.setIdCategoriaContratacionUV(1);
        profesorUV.setIdTipoContratacionUV(1);
        profesorUV.setIdAreaAcademica(1);
        profesorUV.setNombre("Erick");
        profesorUV.setApellidoPaterno("Atzin");
        profesorUV.setApellidoMaterno("Olarte");
        profesorUV.setCorreo("atzin@example.com");
        profesorUV.setIdIdiomas(1);
        profesorUV.setEstadoProfesor("Activo");
        profesorUV.setClaveInstitucional("30MSU0940B");
        
        Acceso acceso = new Acceso();
        acceso.setUsuario("atzin@example.com");
        acceso.setContrasenia("erick*Atzin1@");
        acceso.setTipoUsuario(EnumTipoDeAcceso.Profesor.toString());
        ProfesorUVDAO profesorDAO = new ProfesorUVDAO();
        boolean duplicado = profesorDAO.verificarSiExisteElNoPersonal("12345");

        if (!duplicado) {
            int resultadoObtenido = profesorDAO.registrarProfesorUV(acceso, profesorUV);
            assertEquals(1, resultadoObtenido);
        } else {
            assertEquals(true, duplicado);
        }
    }
    
    @Test 
    public void testRegistrarProfesorUVCorreoDuplicado() throws SQLException {
        ProfesorUVDAO instanciaProfesorUV = new ProfesorUVDAO();
        ProfesorUV profesorUV = new ProfesorUV();

        profesorUV.setNoPersonal("12345");
        profesorUV.setDisciplina("Ingenieria");
        profesorUV.setIdRegion(1);
        profesorUV.setIdCategoriaContratacionUV(1);
        profesorUV.setIdTipoContratacionUV(1);
        profesorUV.setIdAreaAcademica(1);
        profesorUV.setNombre("Erick");
        profesorUV.setApellidoPaterno("Atzin");
        profesorUV.setApellidoMaterno("Olarte");
        profesorUV.setCorreo("atzin@example.com");
        profesorUV.setIdIdiomas(1);
        profesorUV.setEstadoProfesor("Activo");
        profesorUV.setClaveInstitucional("30MSU0940B");

        Acceso acceso = new Acceso();
        acceso.setUsuario("atzin@example.com");
        acceso.setContrasenia("contraseña");
        acceso.setTipoUsuario(EnumTipoDeAcceso.Profesor.toString());

        ProfesorDAO profesorDAO = new ProfesorDAO();
        boolean duplicado = profesorDAO.verificarSiExisteElCorreo("atzin@example.com");

        if (!duplicado) {
            int resultadoObtenido = instanciaProfesorUV.registrarProfesorUV(acceso, profesorUV);
            assertEquals(1, resultadoObtenido);
        } else {
            assertEquals(true, duplicado);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegistrarProfesorUVNombreIncorrecto() throws SQLException {
        ProfesorUVDAO instanciaProfesorUV = new ProfesorUVDAO();
        ProfesorUV profesorUV = new ProfesorUV();

        profesorUV.setNoPersonal("12345");
        profesorUV.setDisciplina("Ingenieria");
        profesorUV.setIdRegion(1);
        profesorUV.setIdCategoriaContratacionUV(1);
        profesorUV.setIdTipoContratacionUV(1);
        profesorUV.setIdAreaAcademica(1);
        profesorUV.setNombre("1Erick");
        profesorUV.setApellidoPaterno("Atzin");
        profesorUV.setApellidoMaterno("Olarte");
        profesorUV.setCorreo("atzin@example.com");
        profesorUV.setIdIdiomas(1);
        profesorUV.setEstadoProfesor("Activo");
        profesorUV.setClaveInstitucional("30MSU0940B");
        Acceso acceso = new Acceso();
        acceso.setUsuario("atzin@example.com");
        acceso.setContrasenia("contraseña");
        acceso.setTipoUsuario(EnumTipoDeAcceso.Profesor.toString()); 

        int resultadoEsperado = 2; 
        int resultadoObtenido = instanciaProfesorUV.registrarProfesorUV(acceso, profesorUV);
        assertEquals(resultadoEsperado, resultadoObtenido);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testRegistrarProfesorUVNombreNulo() throws SQLException {
        ProfesorUVDAO instanciaProfesorUV = new ProfesorUVDAO();
        ProfesorUV profesorUV = new ProfesorUV();

        profesorUV.setNoPersonal("12345");
        profesorUV.setDisciplina("Ingenieria");
        profesorUV.setIdRegion(1);
        profesorUV.setIdCategoriaContratacionUV(1);
        profesorUV.setIdTipoContratacionUV(1);
        profesorUV.setIdAreaAcademica(1);
        profesorUV.setNombre("");
        profesorUV.setApellidoPaterno("Atzin");
        profesorUV.setApellidoMaterno("Olarte");
        profesorUV.setCorreo("atzin@example.com");
        profesorUV.setIdIdiomas(1);
        profesorUV.setEstadoProfesor("Activo");
        profesorUV.setClaveInstitucional("30MSU0940B");
        Acceso acceso = new Acceso();
        acceso.setUsuario("atzin@example.com");
        acceso.setContrasenia("contraseña");
        acceso.setTipoUsuario(EnumTipoDeAcceso.Profesor.toString()); 

        int resultadoEsperado = 2; 
        int resultadoObtenido = instanciaProfesorUV.registrarProfesorUV(acceso, profesorUV);
        assertEquals(resultadoEsperado, resultadoObtenido);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testRegistrarProfesorUVApellidosIncorrectos() throws SQLException {
        ProfesorUVDAO instanciaProfesorUV = new ProfesorUVDAO();
        ProfesorUV profesorUV = new ProfesorUV();

        profesorUV.setNoPersonal("12345");
        profesorUV.setDisciplina("Ingenieria");
        profesorUV.setIdRegion(1);
        profesorUV.setIdCategoriaContratacionUV(1);
        profesorUV.setIdTipoContratacionUV(1);
        profesorUV.setIdAreaAcademica(1);
        profesorUV.setNombre("Erick");
        profesorUV.setApellidoPaterno("1Atzin");
        profesorUV.setApellidoMaterno("1Olarte");
        profesorUV.setCorreo("atzin@example.com");
        profesorUV.setIdIdiomas(1);
        profesorUV.setEstadoProfesor("Activo");
        profesorUV.setClaveInstitucional("30MSU0940B");
        Acceso acceso = new Acceso();
        acceso.setUsuario("atzin@example.com");
        acceso.setContrasenia("contraseña");
        acceso.setTipoUsuario(EnumTipoDeAcceso.Profesor.toString()); 

        int resultadoEsperado = 2; 
        int resultadoObtenido = instanciaProfesorUV.registrarProfesorUV(acceso, profesorUV);
        assertEquals(resultadoEsperado, resultadoObtenido);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testRegistrarProfesorUVApellidosNulos() throws SQLException {
        ProfesorUVDAO instanciaProfesorUV = new ProfesorUVDAO();
        ProfesorUV profesorUV = new ProfesorUV();

        profesorUV.setNoPersonal("12345");
        profesorUV.setDisciplina("Ingenieria");
        profesorUV.setIdRegion(1);
        profesorUV.setIdCategoriaContratacionUV(1);
        profesorUV.setIdTipoContratacionUV(1);
        profesorUV.setIdAreaAcademica(1);
        profesorUV.setNombre("Erick");
        profesorUV.setApellidoPaterno("");
        profesorUV.setApellidoMaterno("");
        profesorUV.setCorreo("atzin@example.com");
        profesorUV.setIdIdiomas(1);
        profesorUV.setEstadoProfesor("Activo");
        profesorUV.setClaveInstitucional("30MSU0940B");
        Acceso acceso = new Acceso();
        acceso.setUsuario("atzin@example.com");
        acceso.setContrasenia("contraseña");
        acceso.setTipoUsuario(EnumTipoDeAcceso.Profesor.toString()); 

        int resultadoEsperado = 2; 
        int resultadoObtenido = instanciaProfesorUV.registrarProfesorUV(acceso, profesorUV);
        assertEquals(resultadoEsperado, resultadoObtenido);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testRegistrarProfesorUVDisciplinaInvalida() throws SQLException {
        ProfesorUVDAO instanciaProfesorUV = new ProfesorUVDAO();
        ProfesorUV profesorUV = new ProfesorUV();

        profesorUV.setNoPersonal("12345");
        profesorUV.setDisciplina("1Ingenieria");
        profesorUV.setIdRegion(1);
        profesorUV.setIdCategoriaContratacionUV(1);
        profesorUV.setIdTipoContratacionUV(1);
        profesorUV.setIdAreaAcademica(1);
        profesorUV.setNombre("Erick");
        profesorUV.setApellidoPaterno("Atzin");
        profesorUV.setApellidoMaterno("Olarte");
        profesorUV.setCorreo("atzin@example.com");
        profesorUV.setIdIdiomas(1);
        profesorUV.setEstadoProfesor("Activo");
        profesorUV.setClaveInstitucional("30MSU0940B");
        Acceso acceso = new Acceso();
        acceso.setUsuario("atzin@example.com");
        acceso.setContrasenia("contraseña");
        acceso.setTipoUsuario(EnumTipoDeAcceso.Profesor.toString()); 

        int resultadoEsperado = 2; 
        int resultadoObtenido = instanciaProfesorUV.registrarProfesorUV(acceso, profesorUV);
        assertEquals(resultadoEsperado, resultadoObtenido);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testRegistrarProfesorUVDisciplinaNula() throws SQLException {
        ProfesorUVDAO instanciaProfesorUV = new ProfesorUVDAO();
        ProfesorUV profesorUV = new ProfesorUV();

        profesorUV.setNoPersonal("12345");
        profesorUV.setDisciplina("");
        profesorUV.setIdRegion(1);
        profesorUV.setIdCategoriaContratacionUV(1);
        profesorUV.setIdTipoContratacionUV(1);
        profesorUV.setIdAreaAcademica(1);
        profesorUV.setNombre("Erick");
        profesorUV.setApellidoPaterno("Atzin");
        profesorUV.setApellidoMaterno("Olarte");
        profesorUV.setCorreo("atzin@example.com");
        profesorUV.setIdIdiomas(1);
        profesorUV.setEstadoProfesor("Activo");
        profesorUV.setClaveInstitucional("30MSU0940B");
        Acceso acceso = new Acceso();
        acceso.setUsuario("atzin@example.com");
        acceso.setContrasenia("contraseña");
        acceso.setTipoUsuario(EnumTipoDeAcceso.Profesor.toString()); 

        int resultadoEsperado = 2; 
        int resultadoObtenido = instanciaProfesorUV.registrarProfesorUV(acceso, profesorUV);
        assertEquals(resultadoEsperado, resultadoObtenido);
    }
    
    @Test
    public void testRegistrarProfesorUVRegionInvalida() throws SQLException {
        ProfesorUVDAO instanciaProfesorUV = new ProfesorUVDAO();
        ProfesorUV profesorUV = new ProfesorUV();

        profesorUV.setNoPersonal("12345");
        profesorUV.setDisciplina("Ingenieria");
        profesorUV.setIdRegion(100000);
        profesorUV.setIdCategoriaContratacionUV(1);
        profesorUV.setIdTipoContratacionUV(1);
        profesorUV.setIdAreaAcademica(1);
        profesorUV.setNombre("Erick");
        profesorUV.setApellidoPaterno("Atzin");
        profesorUV.setApellidoMaterno("Olarte");
        profesorUV.setCorreo("atzin@example.com");
        profesorUV.setIdIdiomas(1);
        profesorUV.setEstadoProfesor("Activo");
        profesorUV.setClaveInstitucional("30MSU09s40B");
        Acceso acceso = new Acceso();
        acceso.setUsuario("atzin@example.com");
        acceso.setContrasenia("erick*Atzin1@");
        acceso.setTipoUsuario(EnumTipoDeAcceso.Profesor.toString()); 

        int resultadoEsperado = -1; 
        int resultadoObtenido = instanciaProfesorUV.registrarProfesorUV(acceso, profesorUV);
        assertEquals(resultadoEsperado, resultadoObtenido);
    }  
    
    @Test
    public void testRegistrarProfesorUVContratacionInvalida() throws SQLException {
        ProfesorUVDAO instanciaProfesorUV = new ProfesorUVDAO();
        ProfesorUV profesorUV = new ProfesorUV();

        profesorUV.setNoPersonal("12345");
        profesorUV.setDisciplina("Ingenieria");
        profesorUV.setIdRegion(1);
        profesorUV.setIdCategoriaContratacionUV(100);
        profesorUV.setIdTipoContratacionUV(1);
        profesorUV.setIdAreaAcademica(1);
        profesorUV.setNombre("Erick");
        profesorUV.setApellidoPaterno("Atzin");
        profesorUV.setApellidoMaterno("Olarte");
        profesorUV.setCorreo("atzin@example.com");
        profesorUV.setIdIdiomas(1);
        profesorUV.setEstadoProfesor("Activo");
        profesorUV.setClaveInstitucional("30MSUU0940B");
        Acceso acceso = new Acceso();
        acceso.setUsuario("atzin@example.com");
        acceso.setContrasenia("erick*Atzin1@");
        acceso.setTipoUsuario(EnumTipoDeAcceso.Profesor.toString()); 

        int resultadoEsperado = -1; 
        int resultadoObtenido = instanciaProfesorUV.registrarProfesorUV(acceso, profesorUV);
        assertEquals(resultadoEsperado, resultadoObtenido);
    }

    @Test
    public void testRegistrarProfesorUVCategoriaInvalida() throws SQLException {
        ProfesorUVDAO instanciaProfesorUV = new ProfesorUVDAO();
        ProfesorUV profesorUV = new ProfesorUV();

        profesorUV.setNoPersonal("12345");
        profesorUV.setDisciplina("Ingenieria");
        profesorUV.setIdRegion(1);
        profesorUV.setIdCategoriaContratacionUV(1);
        profesorUV.setIdTipoContratacionUV(100);
        profesorUV.setIdAreaAcademica(1);
        profesorUV.setNombre("Erick");
        profesorUV.setApellidoPaterno("Atzin");
        profesorUV.setApellidoMaterno("Olarte");
        profesorUV.setCorreo("atzin@example.com");
        profesorUV.setIdIdiomas(1);
        profesorUV.setEstadoProfesor("Activo");
        profesorUV.setClaveInstitucional("30MSUU0940B");
        Acceso acceso = new Acceso();
        acceso.setUsuario("atzin@example.com");
        acceso.setContrasenia("erick*Atzin1@");
        acceso.setTipoUsuario(EnumTipoDeAcceso.Profesor.toString()); 

        int resultadoEsperado = -1; 
        int resultadoObtenido = instanciaProfesorUV.registrarProfesorUV(acceso, profesorUV);
        assertEquals(resultadoEsperado, resultadoObtenido);
    }
    
    @Test 
    public void testRegistrarProfesorUVAreaInvalida() throws SQLException {
        ProfesorUVDAO instanciaProfesorUV = new ProfesorUVDAO();
        ProfesorUV profesorUV = new ProfesorUV();

        profesorUV.setNoPersonal("12345");
        profesorUV.setDisciplina("Ingenieria");
        profesorUV.setIdRegion(1);
        profesorUV.setIdCategoriaContratacionUV(1);
        profesorUV.setIdTipoContratacionUV(1);
        profesorUV.setIdAreaAcademica(100);
        profesorUV.setNombre("Erick");
        profesorUV.setApellidoPaterno("Atzin");
        profesorUV.setApellidoMaterno("Olarte");
        profesorUV.setCorreo("atzin@example.com");
        profesorUV.setIdIdiomas(1);
        profesorUV.setEstadoProfesor("Activo");
        profesorUV.setClaveInstitucional("30MSUU0940B");
        Acceso acceso = new Acceso();
        acceso.setUsuario("atzin@example.com");
        acceso.setContrasenia("erick*Atzin1@");
        acceso.setTipoUsuario(EnumTipoDeAcceso.Profesor.toString()); 

        int resultadoEsperado = -1; 
        int resultadoObtenido = instanciaProfesorUV.registrarProfesorUV(acceso, profesorUV);
        assertEquals(resultadoEsperado, resultadoObtenido);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testRegistrarProfesorUVCorreoInvalido() throws SQLException {
        ProfesorUVDAO instanciaProfesorUV = new ProfesorUVDAO();
        ProfesorUV profesorUV = new ProfesorUV();

        profesorUV.setNoPersonal("12345");
        profesorUV.setDisciplina("Ingenieria");
        profesorUV.setIdRegion(1);
        profesorUV.setIdCategoriaContratacionUV(1);
        profesorUV.setIdTipoContratacionUV(1);
        profesorUV.setIdAreaAcademica(1);
        profesorUV.setNombre("Erick");
        profesorUV.setApellidoPaterno("Atzin");
        profesorUV.setApellidoMaterno("Olarte");
        profesorUV.setCorreo("atzinexamplecom");
        profesorUV.setIdIdiomas(1);
        profesorUV.setEstadoProfesor("Activo");
        profesorUV.setClaveInstitucional("30MSU0940B");
        Acceso acceso = new Acceso();
        acceso.setUsuario("atzinexamplecom");
        acceso.setContrasenia("erick*Atzin1@");
        acceso.setTipoUsuario(EnumTipoDeAcceso.Profesor.toString()); 

        int resultadoEsperado = 2; 
        int resultadoObtenido = instanciaProfesorUV.registrarProfesorUV(acceso, profesorUV);
        assertEquals(resultadoEsperado, resultadoObtenido);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testRegistrarProfesorUVCorreoNulo() throws SQLException {
        ProfesorUVDAO instanciaProfesorUV = new ProfesorUVDAO();
        ProfesorUV profesorUV = new ProfesorUV();

        profesorUV.setNoPersonal("12345");
        profesorUV.setDisciplina("Ingenieria");
        profesorUV.setIdRegion(1);
        profesorUV.setIdCategoriaContratacionUV(1);
        profesorUV.setIdTipoContratacionUV(1);
        profesorUV.setIdAreaAcademica(1);
        profesorUV.setNombre("Erick");
        profesorUV.setApellidoPaterno("Atzin");
        profesorUV.setApellidoMaterno("Olarte");
        profesorUV.setCorreo("");
        profesorUV.setIdIdiomas(1);
        profesorUV.setEstadoProfesor("Activo");
        profesorUV.setClaveInstitucional("30MSU0940B");
        Acceso acceso = new Acceso();
        acceso.setUsuario("atzinexamplecom");
        acceso.setContrasenia("erick*Atzin1@");
        acceso.setTipoUsuario(EnumTipoDeAcceso.Profesor.toString()); 

        int resultadoEsperado = 2; 
        int resultadoObtenido = instanciaProfesorUV.registrarProfesorUV(acceso, profesorUV);
        assertEquals(resultadoEsperado, resultadoObtenido);
    }
    
    @Test
    public void testRegistrarProfesorUVClaveInexistente() throws SQLException {
        ProfesorUVDAO instanciaProfesorUV = new ProfesorUVDAO();
        ProfesorUV profesorUV = new ProfesorUV();

        profesorUV.setNoPersonal("12345");
        profesorUV.setDisciplina("Ingenieria");
        profesorUV.setIdRegion(1);
        profesorUV.setIdCategoriaContratacionUV(1);
        profesorUV.setIdTipoContratacionUV(1);
        profesorUV.setIdAreaAcademica(1);
        profesorUV.setNombre("Erick");
        profesorUV.setApellidoPaterno("Atzin");
        profesorUV.setApellidoMaterno("Olarte");
        profesorUV.setCorreo("atzin@example.com");
        profesorUV.setIdIdiomas(1);
        profesorUV.setEstadoProfesor("Activo");
        profesorUV.setClaveInstitucional("clavefitcitica123");
        Acceso acceso = new Acceso();
        acceso.setUsuario("atzin@example.com");
        acceso.setContrasenia("erick*Atzin1@");
        acceso.setTipoUsuario(EnumTipoDeAcceso.Profesor.toString()); 

        int resultadoEsperado = -1; 
        int resultadoObtenido = instanciaProfesorUV.registrarProfesorUV(acceso, profesorUV);
        assertEquals(resultadoEsperado, resultadoObtenido);
    }
    
    @Test
    public void testRegistrarProfesorUVClaveNula() throws SQLException {
        ProfesorUVDAO instanciaProfesorUV = new ProfesorUVDAO();
        ProfesorUV profesorUV = new ProfesorUV();

        profesorUV.setNoPersonal("12345");
        profesorUV.setDisciplina("Ingenieria");
        profesorUV.setIdRegion(1);
        profesorUV.setIdCategoriaContratacionUV(1);
        profesorUV.setIdTipoContratacionUV(1);
        profesorUV.setIdAreaAcademica(1);
        profesorUV.setNombre("Erick");
        profesorUV.setApellidoPaterno("Atzin");
        profesorUV.setApellidoMaterno("Olarte");
        profesorUV.setCorreo("atzin@example.com");
        profesorUV.setIdIdiomas(1);
        profesorUV.setEstadoProfesor("Activo");
        profesorUV.setClaveInstitucional("");
        Acceso acceso = new Acceso();
        acceso.setUsuario("atzin@example.com");
        acceso.setContrasenia("erick*Atzin1@");
        acceso.setTipoUsuario(EnumTipoDeAcceso.Profesor.toString()); 

        int resultadoEsperado = -1; 
        int resultadoObtenido = instanciaProfesorUV.registrarProfesorUV(acceso, profesorUV);
        assertEquals(resultadoEsperado, resultadoObtenido);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testRegistrarProfesorUVCaracteresEspeciales() throws SQLException {
        ProfesorUVDAO instanciaProfesorUV = new ProfesorUVDAO();
        ProfesorUV profesorUV = new ProfesorUV();

        profesorUV.setNoPersonal("12345");
        profesorUV.setDisciplina("Ingenieria");
        profesorUV.setIdRegion(1);
        profesorUV.setIdCategoriaContratacionUV(1);
        profesorUV.setIdTipoContratacionUV(1);
        profesorUV.setIdAreaAcademica(1);
        profesorUV.setNombre("**Erick");
        profesorUV.setApellidoPaterno("**Atzin");
        profesorUV.setApellidoMaterno("***Olarte");
        profesorUV.setCorreo("atzin@example.com");
        profesorUV.setIdIdiomas(1);
        profesorUV.setEstadoProfesor("Activo");
        profesorUV.setClaveInstitucional("30MSU0940B");
        Acceso acceso = new Acceso();
        acceso.setUsuario("atzin@example.com");
        acceso.setContrasenia("contraseña");
        acceso.setTipoUsuario(EnumTipoDeAcceso.Profesor.toString()); 

        int resultadoEsperado = 2; 
        int resultadoObtenido = instanciaProfesorUV.registrarProfesorUV(acceso, profesorUV);
        assertEquals(resultadoEsperado, resultadoObtenido);
    }

    @Test
    public void testVerificarSiExisteElNoPersonalExistente() throws SQLException {
        ProfesorUVDAO instanciaProfesorUV = new ProfesorUVDAO();
        String noPersonalExistente = "12345"; 
        boolean existeNoPersonal = instanciaProfesorUV.verificarSiExisteElNoPersonal(noPersonalExistente);
        assertTrue(existeNoPersonal); 
    }

    @Test
    public void testVerificarSiExisteElNoPersonalNoExistente() throws SQLException {
        ProfesorUVDAO instanciaProfesorUV = new ProfesorUVDAO();
        String noPersonalNoExistente = "98765"; 
        boolean existeNoPersonal = instanciaProfesorUV.verificarSiExisteElNoPersonal(noPersonalNoExistente);
        assertFalse(existeNoPersonal);
    }
    
    @Test
    public void testObtenerProfesorUVPorNumPersonalExitoso() throws SQLException {
        System.out.println("Obteniendo Profesor UV por Número de Personal");
        String numPersonalInexistente = "12345";
        ProfesorUVDAO instanciaProfesorUV = new ProfesorUVDAO();
        ProfesorUV profesorUV = instanciaProfesorUV.obtenerProfesorUVPorNumPersonal(numPersonalInexistente);
        assertNotNull(profesorUV);
        assertEquals(numPersonalInexistente, profesorUV.getNoPersonal());
    }
    
    @Test
    public void testEliminarProfesorUVExitoso() {
        ProfesorUVDAO instanciaProfesorUV = new ProfesorUVDAO();
        String noPersonal = "12345";
        int filasAfectadas = instanciaProfesorUV.eliminarProfesorUV(noPersonal);
        assertEquals(3, filasAfectadas);
    }
    
    @Test
    public void testEliminarProfesorUVInexistente() {
        ProfesorUVDAO instanciaProfesorUV = new ProfesorUVDAO();
        String noPersonal = "1234500000000000";
        int filasAfectadas = instanciaProfesorUV.eliminarProfesorUV(noPersonal);
        assertEquals(0, filasAfectadas);
    }

    @Test
    public void testObtenerListaDeRegion() {
        ProfesorUVDAO instanciaProfesorUV = new ProfesorUVDAO();
        List<List<String>> listaObtenida = instanciaProfesorUV.obtenerListaDeRegion();
        List<List<String>> listaEsperada = new ArrayList<>();
        listaEsperada.add(Arrays.asList("1", "Xalapa"));
        listaEsperada.add(Arrays.asList("2", "Veracruz"));
        listaEsperada.add(Arrays.asList("3", "Orizaba - Córdoba"));
        listaEsperada.add(Arrays.asList("4", "Coatzacoalcos - Minatitlán"));
        listaEsperada.add(Arrays.asList("5", "Poza Rica - Tuxpan"));
        listaEsperada.add(Arrays.asList("6", "Otra"));
        assertEquals(listaEsperada.size(), listaObtenida.size());
        for (int i = 0; i < listaEsperada.size(); i++) {
            List<String> regionObtenida = listaObtenida.get(i);
            List<String> regionEsperada = listaEsperada.get(i);
            assertEquals(regionEsperada, regionObtenida);
        }
    }
       
    @Test
    public void testObtenerListaDeCategoriaContratacion() {
        ProfesorUVDAO instanciaProfesorUV = new ProfesorUVDAO();
        List<List<String>> listaObtenida = instanciaProfesorUV.obtenerListaDeCategoriaContratacion();
        List<List<String>> listaEsperada = new ArrayList<>();
        listaEsperada.add(Arrays.asList("1", "Docente T.C."));
        listaEsperada.add(Arrays.asList("2", "Investigador T.C."));
        listaEsperada.add(Arrays.asList("3", "Docente Investigador T.C."));
        listaEsperada.add(Arrays.asList("4", "Ejecutante T.C."));
        listaEsperada.add(Arrays.asList("5", "Técnico Académico T.C."));
        listaEsperada.add(Arrays.asList("6", "Docente M.T."));
        listaEsperada.add(Arrays.asList("7", "Investigador M.T."));
        listaEsperada.add(Arrays.asList("8", "Docente - Investigador M.T."));
        listaEsperada.add(Arrays.asList("9", "Ejecutante M.T."));
        listaEsperada.add(Arrays.asList("10", "Técnico Académico M.T."));
        listaEsperada.add(Arrays.asList("11", "Docente P.A."));
        listaEsperada.add(Arrays.asList("12", "Académico Instructor (T.C. 40 HRS)"));
        listaEsperada.add(Arrays.asList("13", "Académico Instructor (T.C. 30 HRS)"));
        listaEsperada.add(Arrays.asList("14", "Eventual"));
        listaEsperada.add(Arrays.asList("15", "Otra"));
        assertEquals(listaEsperada.size(), listaObtenida.size());
        for (int i = 0; i < listaEsperada.size(); i++) {
            List<String> categoriaObtenida = listaObtenida.get(i);
            List<String> categoriaEsperada = listaEsperada.get(i);
            assertEquals(categoriaEsperada, categoriaObtenida);
        }
    }

    @Test
    public void testObtenerListaDeTipoContratacion() {
        ProfesorUVDAO instanciaProfesorUV = new ProfesorUVDAO();
        List<List<String>> listaObtenida = instanciaProfesorUV.obtenerListaDeTipoContratacion();
        List<List<String>> listaEsperada = new ArrayList<>();
        listaEsperada.add(Arrays.asList("1", "Planta"));
        listaEsperada.add(Arrays.asList("2", "Interino por Plaza"));
        listaEsperada.add(Arrays.asList("3", "Interino por Persona"));
        listaEsperada.add(Arrays.asList("4", "Interino por Tiempo Determinado"));
        listaEsperada.add(Arrays.asList("5", "Interino por Obra Determinada"));
        listaEsperada.add(Arrays.asList("6", "Interino por Falta de Grado"));
        listaEsperada.add(Arrays.asList("7", "Suplente o Sustituto"));
        listaEsperada.add(Arrays.asList("8", "Trabajos Específicos"));
        listaEsperada.add(Arrays.asList("9", "Interino por Plaza con Plaza"));
        listaEsperada.add(Arrays.asList("10", "Interino por Persona con Plaza"));
        listaEsperada.add(Arrays.asList("11", "Suplente o Sustituto con Plaza"));
        listaEsperada.add(Arrays.asList("12", "Eventual"));
        listaEsperada.add(Arrays.asList("13", "Beca Trabajo"));
        listaEsperada.add(Arrays.asList("14", "Apoyo"));
        listaEsperada.add(Arrays.asList("15", "Otra"));
        assertEquals(listaEsperada.size(), listaObtenida.size());
        for (int i = 0; i < listaEsperada.size(); i++) {
            List<String> tipoObtenido = listaObtenida.get(i);
            List<String> tipoEsperado = listaEsperada.get(i);
            assertEquals(tipoEsperado, tipoObtenido);
        }
    }
    
    @Test
    public void testObtenerListaDeAreaAcademica() {
        ProfesorUVDAO instanciaProfesorUV = new ProfesorUVDAO();
        List<List<String>> listaObtenida = instanciaProfesorUV.obtenerListaDeAreaAcademica();
        List<List<String>> listaEsperada = new ArrayList<>();
        listaEsperada.add(Arrays.asList("1", "Económico - Administrativo"));
        listaEsperada.add(Arrays.asList("2", "Humanidades"));
        listaEsperada.add(Arrays.asList("3", "Técnica"));
        listaEsperada.add(Arrays.asList("4", "Ciencias de la Salud"));
        listaEsperada.add(Arrays.asList("5", "Biológico - Agropecuarias"));
        listaEsperada.add(Arrays.asList("6", "Otra"));
        assertEquals(listaEsperada.size(), listaObtenida.size());
        for (int i = 0; i < listaEsperada.size(); i++) {
            List<String> areaObtenida = listaObtenida.get(i);
            List<String> areaEsperada = listaEsperada.get(i);
            assertEquals(areaEsperada, areaObtenida);
        }
    }

    

}
