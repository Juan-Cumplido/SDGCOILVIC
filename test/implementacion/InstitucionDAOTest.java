package implementacion;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Test;
import sdgcoilvic.logicaDeNegocio.implementacionDAO.InstitucionDAO;
import sdgcoilvic.logicaDeNegocio.clases.Institucion;


public class InstitucionDAOTest {
    
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
    
    @Test
    public void testObtenerTodasLasInstituciones() throws SQLException {
        System.out.println("Obteniendo Lista de Instituciones");

        List<Institucion> listaEsperada = new ArrayList<>();
        Institucion institucion1 = new Institucion();
        institucion1.setClaveInstitucional("30MSU0940B");
        institucion1.setNombreInstitucion("UNIVERSIDAD VERACRUZANA");
        institucion1.setNombrePais("México");
        institucion1.setCorreo("dgae@uv.mx");
        listaEsperada.add(institucion1);

        InstitucionDAO institucionDAO = new InstitucionDAO();
        List<Institucion> listaObtenida = institucionDAO.obtenerTodasLasInstituciones();

        assertNotNull(listaObtenida);
        assertEquals(listaEsperada.size(), listaObtenida.size());
        for (int i = 0; i < listaEsperada.size(); i++) {
            Institucion institucionEsperada = listaEsperada.get(i);
            Institucion institucionObtenida = listaObtenida.get(i);            
            assertEquals(institucionEsperada, institucionObtenida);
        }
    }
    
    @Test
    public void testObtenerListaInstitucionesPorNombre() throws SQLException {
        String criterioBusqueda = "UNIVERSIDAD VERACRUZANA";
        InstitucionDAO institucionDAO = new InstitucionDAO();
        List<Institucion> listaInstituciones = institucionDAO.obtenerListaInstitucionesPorNombre(criterioBusqueda);
        assertNotNull(listaInstituciones);

        List<Institucion> listaPreliminarTecnologico = new ArrayList<>();
        Institucion institucionTecnologico = new Institucion();
        institucionTecnologico.setClaveInstitucional("30MSU0940B");
        institucionTecnologico.setNombreInstitucion("UNIVERSIDAD VERACRUZANA");
        institucionTecnologico.setNombrePais("México");
        institucionTecnologico.setCorreo("dgae@uv.mx");
        listaPreliminarTecnologico.add(institucionTecnologico);

        assertEquals(listaPreliminarTecnologico.size(), listaInstituciones.size());
        for (int i = 0; i < listaPreliminarTecnologico.size(); i++) {
            Institucion institucionEsperada = listaPreliminarTecnologico.get(i);
            Institucion institucionObtenida = listaInstituciones.get(i);
            assertEquals(institucionEsperada, institucionObtenida);
        }
    }

    @Test
    public void testObtenerListaDePaises() throws SQLException {
        InstitucionDAO institucionDAO = new InstitucionDAO();
        List<String> listaPaises = institucionDAO.obtenerListaDePaises();
        assertNotNull(listaPaises);
        List<String> listaPreliminarPaises = Arrays.asList("México");
        assertEquals(listaPreliminarPaises.size(), listaPaises.size());
        assertTrue(listaPaises.containsAll(listaPreliminarPaises));
    }
}
