
package testAgregar;

import java.sql.SQLException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Test;
import sdgcoilvic.logicaDeNegocio.clases.Institucion;
import sdgcoilvic.logicaDeNegocio.implementacionDAO.InstitucionDAO;



public class testInstitucion {
        @Test
    public void testinsertarInstitucionExitoso() throws Exception {
        System.out.println("registrarInstitucion");
        Institucion institucion = new Institucion();
        InstitucionDAO instancia = new InstitucionDAO();
        institucion.setClaveInstitucional("30MSU0940B");
        institucion.setNombreInstitucion("UNIVERSIDAD VERACRUZANA");
        institucion.setNombrePais("México");
        institucion.setCorreo("dgae@uv.mx");
        int resultadoEsperado = 1;
        int resultadoObtenido = instancia.insertarInstitucion(institucion);
        assertEquals(resultadoEsperado , resultadoObtenido);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testinsertarInstitucionClaveNula() throws Exception {
        System.out.println("registrarInstitucion");
        Institucion institucion = new Institucion();
        InstitucionDAO instancia = new InstitucionDAO();
        institucion.setClaveInstitucional("");
        institucion.setNombreInstitucion("UNIVERSIDAD VERACRUZANA");
        institucion.setNombrePais("México");
        institucion.setCorreo("dgae@uv.mx");
        int resultadoEsperado = 1;
        int resultadoObtenido = instancia.insertarInstitucion(institucion);
        assertEquals(resultadoEsperado , resultadoObtenido);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testinsertarInstitucionNombreNulo() throws Exception {
        System.out.println("registrarInstitucion");
        Institucion institucion = new Institucion();
        InstitucionDAO instancia = new InstitucionDAO();
        institucion.setClaveInstitucional("30MSU0940B");
        institucion.setNombreInstitucion("");
        institucion.setNombrePais("México");
        institucion.setCorreo("dgae@uv.mx");
        int resultadoEsperado = 1;
        int resultadoObtenido = instancia.insertarInstitucion(institucion);
        assertEquals(resultadoEsperado , resultadoObtenido);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testinsertarInstitucionPaisNulo() throws Exception {
        System.out.println("registrarInstitucion");
        Institucion institucion = new Institucion();
        InstitucionDAO instancia = new InstitucionDAO();
        institucion.setClaveInstitucional("30MSU0940B");
        institucion.setNombreInstitucion("UNIVERSIDAD VERACRUZANA");
        institucion.setNombrePais("");
        institucion.setCorreo("dgae@uv.mx");
        int resultadoEsperado = 1;
        int resultadoObtenido = instancia.insertarInstitucion(institucion);
        assertEquals(resultadoEsperado , resultadoObtenido);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testinsertarInstitucionCorreoNulo() throws Exception {
        System.out.println("registrarInstitucion");
        Institucion institucion = new Institucion();
        InstitucionDAO instancia = new InstitucionDAO();
        institucion.setClaveInstitucional("30MSU0940B");
        institucion.setNombreInstitucion("UNIVERSIDAD VERACRUZANA");
        institucion.setNombrePais("México");
        institucion.setCorreo("");
        int resultadoEsperado = 1;
        int resultadoObtenido = instancia.insertarInstitucion(institucion);
        assertEquals(resultadoEsperado , resultadoObtenido);
    }
    
    @Test
    public void testinsertarInstitucionNombreDuplicado() throws SQLException {
        System.out.println("registrarInstitucion");
     
        Institucion institucion = new Institucion();
        InstitucionDAO instancia = new InstitucionDAO();
        institucion.setClaveInstitucional("30MSU0940B");
        institucion.setNombreInstitucion("UNIVERSIDAD VERACRUZANA");
        institucion.setNombrePais("México");
        institucion.setCorreo("dgae@uv.mx");
        boolean existeNombreInstitucion = instancia.verificarSiExisteElNombreInstitucion(institucion.getNombreInstitucion());       
    
        if (!existeNombreInstitucion) {
            int resultadoObtenido = instancia.insertarInstitucion(institucion);            
            assertEquals(1, resultadoObtenido);
        }else {
            assertEquals(true, existeNombreInstitucion);
        }
    }
    
    @Test
    public void testinsertarInstitucionCorreoDuplicado() throws SQLException {
        System.out.println("registrarInstitucion");
     
        Institucion institucion = new Institucion();
        InstitucionDAO instancia = new InstitucionDAO();
        institucion.setClaveInstitucional("30PSU2065D");
        institucion.setNombreInstitucion("CENTRO DE ESTUDIOS SUPERIORES DEL NORTE DE VERACRUZ");
        institucion.setNombrePais("México");
        institucion.setCorreo("dgae@uv.mx");
        boolean existeNombreInstitucion = instancia.verificarSiExisteElCorreo(institucion.getCorreo());       
    
        if (!existeNombreInstitucion) {
            int resultadoObtenido = instancia.insertarInstitucion(institucion);            
            assertEquals(1, resultadoObtenido);
        }else {
            assertEquals(true, existeNombreInstitucion);
        }
    }
    
    @Test(expected = AssertionError.class)
    public void testinsertarInstitucionClaveDuplicado() throws SQLException {
        System.out.println("registrarInstitucion");
     
        Institucion institucion = new Institucion();
        InstitucionDAO instancia = new InstitucionDAO();
        institucion.setClaveInstitucional("30MSU0940B");
        institucion.setNombreInstitucion("CENTRO DE ESTUDIOS SUPERIORES DEL NORTE DE VERACRUZ");
        institucion.setNombrePais("México");
        institucion.setCorreo("info@cesunv.edu.mx");
        boolean existeNombreInstitucion = instancia.verificarSiExisteLaClave(institucion.getClaveInstitucional());       
    
        if (!existeNombreInstitucion) {
            int resultadoObtenido = instancia.insertarInstitucion(institucion);            
            assertEquals(1, resultadoObtenido);
        }else {
            fail("Ya existe una insitucion con esta clave insitucional");
        }
    }

    @Test (expected = IllegalArgumentException.class)
    public void testinsertarInstitucionClaveInvalida() throws Exception {
        System.out.println("registrarInstitucion");
        Institucion institucion = new Institucion();
        InstitucionDAO instancia = new InstitucionDAO();
        institucion.setClaveInstitucional("*30MSU0940B");
        institucion.setNombreInstitucion("UNIVERSIDAD VERACRUZANA");
        institucion.setNombrePais("México");
        institucion.setCorreo("dgae@uv.mx");
        int resultadoEsperado = 1;
        int resultadoObtenido = instancia.insertarInstitucion(institucion);
        assertEquals(resultadoEsperado , resultadoObtenido);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testinsertarInstitucionNombreInvalido() throws Exception {
        System.out.println("registrarInstitucion");
        Institucion institucion = new Institucion();
        InstitucionDAO instancia = new InstitucionDAO();
        institucion.setClaveInstitucional("30MSU0940B");
        institucion.setNombreInstitucion("*/UNIVERSIDAD VERACRUZANA");
        institucion.setNombrePais("México");
        institucion.setCorreo("dgae@uv.mx");
        int resultadoEsperado = 1;
        int resultadoObtenido = instancia.insertarInstitucion(institucion);
        assertEquals(resultadoEsperado , resultadoObtenido);
    }
    
    @Test 
    public void testinsertarInstitucionPaisInexistente() throws Exception {
        System.out.println("registrarInstitucion");
        Institucion institucion = new Institucion();
        InstitucionDAO instancia = new InstitucionDAO();
        institucion.setClaveInstitucional("30MSU0940B");
        institucion.setNombreInstitucion("UNIVERSIDAD VERACRUZANA");
        institucion.setNombrePais("Guatemala");
        institucion.setCorreo("dgae@uv.mx");
        int resultadoEsperado = 0;
        int resultadoObtenido = instancia.insertarInstitucion(institucion);
        assertEquals(resultadoEsperado , resultadoObtenido);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testinsertarInstitucionPaisInvalido() throws Exception {
        System.out.println("registrarInstitucion");
        Institucion institucion = new Institucion();
        InstitucionDAO instancia = new InstitucionDAO();
        institucion.setClaveInstitucional("30MSU0940B");
        institucion.setNombreInstitucion("UNIVERSIDAD VERACRUZANA");
        institucion.setNombrePais("*/México");
        institucion.setCorreo("dgae@uv.mx");
        int resultadoEsperado = 1;
        int resultadoObtenido = instancia.insertarInstitucion(institucion);
        assertEquals(resultadoEsperado , resultadoObtenido);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testinsertarInstitucionCorreoInvalido() throws Exception {
        System.out.println("registrarInstitucion");
        Institucion institucion = new Institucion();
        InstitucionDAO instancia = new InstitucionDAO();
        institucion.setClaveInstitucional("30MSU0940B");
        institucion.setNombreInstitucion("UNIVERSIDAD VERACRUZANA");
        institucion.setNombrePais("México");
        institucion.setCorreo("meogmailcom");
        int resultadoEsperado = 1;
        int resultadoObtenido = instancia.insertarInstitucion(institucion);
        assertEquals(resultadoEsperado , resultadoObtenido);
    }
}
