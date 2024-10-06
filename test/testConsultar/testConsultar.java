package testConsultar;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import sdgcoilvic.logicaDeNegocio.clases.Profesor;
import sdgcoilvic.logicaDeNegocio.implementacionDAO.ProfesorDAO;

public class testConsultar {
    
    
    @Test
    public void testObtenerListaDeIdiomasExitoso() throws SQLException {
        System.out.println("Obteniendo Lista de Idiomas");
        List<List<String>> listaEsperada = new ArrayList<>();
        listaEsperada.add(Arrays.asList("1", "Inglés"));
        listaEsperada.add(Arrays.asList("2", "Español"));
        listaEsperada.add(Arrays.asList("3", "Chino mandarín"));
        listaEsperada.add(Arrays.asList("4", "Aleman"));
        ProfesorDAO profesorDAO = new ProfesorDAO();
        List<List<String>> listaObtenida = profesorDAO.obtenerListaDeIdiomas();
        assertNotNull(listaObtenida);

        assertEquals(listaEsperada.size(), listaObtenida.size());
        for (int i = 0; i < listaEsperada.size(); i++) {
            List<String> idiomaEsperado = listaEsperada.get(i);
            List<String> idiomaObtenido = listaObtenida.get(i);

            assertEquals(idiomaEsperado, idiomaObtenido);
        }
    }

    @Test
    public void testObtenerListaDeNombreInstitucionExitoso() throws SQLException {
        System.out.println("Obteniendo Lista de Nombres de Instituciones");

        List<String> listaEsperada = Arrays.asList("UNIVERSIDAD VERACRUZANA");

        ProfesorDAO profesorDAO = new ProfesorDAO();
        List<String> listaObtenida = profesorDAO.obtenerListaDeNombreInstitucion();

        assertNotNull(listaObtenida);
        assertFalse(listaObtenida.isEmpty());
        assertEquals(listaEsperada.size(), listaObtenida.size());
        assertTrue(listaObtenida.containsAll(listaEsperada));
    }
    
        @Test
    public void testObtenerListaDeInstitucionesExitoso() {
        System.out.println("Obteniendo Lista de Instituciones");
        List<List<String>> listaEsperada = new ArrayList<>();
        listaEsperada.add(Arrays.asList("30MSU0940B", "UNIVERSIDAD VERACRUZANA"));

        ProfesorDAO profesorDAO = new ProfesorDAO();
        List<List<String>> listaObtenida = profesorDAO.obtenerListaDeInstituciones();

        assertNotNull(listaObtenida);
        assertFalse(listaObtenida.isEmpty());
        assertEquals(listaEsperada.size(), listaObtenida.size());
        assertTrue(listaObtenida.containsAll(listaEsperada));
    }

    
    @Test
    public void testVerificarProfesorDuplicadoExitoso() throws Exception {
        System.out.println("Verificando Profesor Duplicado");

        Profesor profesor = new Profesor();
        profesor.setNombre("Erick");
        profesor.setApellidoPaterno("Atzin");
        profesor.setApellidoMaterno("Olarte");
        profesor.setCorreo("atzin@example.com");
        profesor.setIdIdiomas(1);
        profesor.setEstadoProfesor("Activo");
        profesor.setClaveInstitucional("30MSU0940B");

        ProfesorDAO profesorDAO = new ProfesorDAO();
        boolean resultadoObtenido = profesorDAO.verificarProfesorDuplicado(profesor);
        assertTrue(resultadoObtenido); 
    }
    
    @Test
    public void testVerificarProfesorDuplicado() throws Exception {
        System.out.println("Verificando Profesor Duplicado");

        Profesor nuevoProfesor = new Profesor();
        nuevoProfesor.setNombre("Juan");
        nuevoProfesor.setApellidoPaterno("Perez");
        nuevoProfesor.setApellidoMaterno("Garcia");
        nuevoProfesor.setCorreo("juan@example.com");
        nuevoProfesor.setIdIdiomas(2); 
        nuevoProfesor.setEstadoProfesor("Activo");
        nuevoProfesor.setClaveInstitucional("claveficticia");
        ProfesorDAO profesorDAO = new ProfesorDAO();
        boolean resultadoObtenido = profesorDAO.verificarProfesorDuplicado(nuevoProfesor);
        assertFalse(resultadoObtenido); 
    }

    @Test
    public void testObtenerListaProfesoresPorNombreExitoso() throws SQLException {
        System.out.println("Obteniendo Lista de Profesores por Nombre");
        String nombre = "Erick";
        ProfesorDAO profesorDAO = new ProfesorDAO();
        List<Profesor> listaProfesores = profesorDAO.obtenerListaProfesoresPorNombre(nombre);
        assertNotNull(listaProfesores);
        assertFalse(listaProfesores.isEmpty());
        for (Profesor profesor : listaProfesores) {
            boolean coincideNombre = profesor.getNombre().contains(nombre);
            boolean coincideApellidoPaterno = profesor.getApellidoPaterno().contains(nombre);
            boolean coincideApellidoMaterno = profesor.getApellidoMaterno().contains(nombre);

            assertTrue(coincideNombre || coincideApellidoPaterno || coincideApellidoMaterno);
        }
    }
    
    @Test
    public void testObtenerListaProfesoresPorNombreInexistente() throws SQLException {
        System.out.println("Obteniendo Lista de Profesores por Nombre");
        String criterioBusqueda = "Oscar";
        ProfesorDAO profesorDAO = new ProfesorDAO();
        List<Profesor> listaProfesores = profesorDAO.obtenerListaProfesoresPorNombre(criterioBusqueda);
        assertNotNull(listaProfesores);
        assertTrue(listaProfesores.isEmpty());
    }
    
    @Test
    public void testObtenerListaProfesoresPorNombreInvalido() throws SQLException {
        System.out.println("Obteniendo Lista de Profesores por Nombre");
        String nombre = "1Erick";
        ProfesorDAO profesorDAO = new ProfesorDAO();
        List<Profesor> listaProfesores = profesorDAO.obtenerListaProfesoresPorNombre(nombre);
        assertNotNull(listaProfesores);
        assertTrue(listaProfesores.isEmpty());
    }

    @Test
    public void testVerificarExistenciaProfesorExitoso() throws SQLException {
        System.out.println("Verificando Existencia de Profesor");

        String nombre = "Erick";
        String apellidoPaterno = "Atzin";
        String apellidoMaterno = "Olarte";

        ProfesorDAO profesorDAO = new ProfesorDAO();
        boolean resultadoObtenido = profesorDAO.verificarExistenciaProfesor(nombre, apellidoPaterno, apellidoMaterno);
        assertTrue(resultadoObtenido); 
    }

    @Test
    public void testVerificarExistenciaProfesorNoExitoso() throws SQLException {
        System.out.println("Verificando No Existencia de Profesor");

        String nombre = "Juan";
        String apellidoPaterno = "Perez";
        String apellidoMaterno = "Garcia";

        ProfesorDAO profesorDAO = new ProfesorDAO();
        boolean resultadoObtenido = profesorDAO.verificarExistenciaProfesor(nombre, apellidoPaterno, apellidoMaterno);
        assertFalse(resultadoObtenido); 
    }
    
    @Test
    public void testVerificarExistenciaProfesorNombreInvalido() throws SQLException {
        System.out.println("Verificando Existencia de Profesor");

        String nombre = "1.Erick";
        String apellidoPaterno = "Atzin";
        String apellidoMaterno = "Olarte";

        ProfesorDAO profesorDAO = new ProfesorDAO();
        boolean resultadoObtenido = profesorDAO.verificarExistenciaProfesor(nombre, apellidoPaterno, apellidoMaterno);
        assertFalse(resultadoObtenido); 
    }
    
    @Test
    public void testVerificarExistenciaProfesorApellidoInvalido() throws SQLException {
        System.out.println("Verificando Existencia de Profesor");

        String nombre = "Erick";
        String apellidoPaterno = "1.Atzin";
        String apellidoMaterno = "1.Olarte";

        ProfesorDAO profesorDAO = new ProfesorDAO();
        boolean resultadoObtenido = profesorDAO.verificarExistenciaProfesor(nombre, apellidoPaterno, apellidoMaterno);
        assertFalse(resultadoObtenido); 
    }
    
    @Test
    public void testObtenerProfesorPorCorreoExitoso() throws SQLException {
        System.out.println("Obteniendo Profesor por Correo");
        String correoExistente = "atzin@example.com";

        ProfesorDAO profesorDAO = new ProfesorDAO();
        Profesor profesorObtenido = profesorDAO.obtenerProfesorPorCorreo(correoExistente);

        assertNotNull(profesorObtenido);
        assertEquals(correoExistente, profesorObtenido.getCorreo());
    }

    @Test(expected = AssertionError.class)
    public void testObtenerProfesorPorCorreoInexistente() throws SQLException {
        System.out.println("Obteniendo Profesor por Correo");
        String correoInexistente = "atzin1@example.com";

        ProfesorDAO profesorDAO = new ProfesorDAO();
        Profesor profesorObtenido = profesorDAO.obtenerProfesorPorCorreo(correoInexistente);

        assertNotNull(profesorObtenido);
        assertEquals(correoInexistente, profesorObtenido.getCorreo());
    }
    
    @Test
    public void testObtenerProfesorPorIDExitoso() throws SQLException {
        System.out.println("Obteniendo Profesor por ID");
        String idProfesorExistente = "2";
        ProfesorDAO profesorDAO = new ProfesorDAO();
        Profesor profesorObtenido = profesorDAO.obtenerProfesorPorID(idProfesorExistente);

        assertNotNull(profesorObtenido);
        assertEquals(idProfesorExistente, String.valueOf(profesorObtenido.getIdProfesor()));
    }
    
}
