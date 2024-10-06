package testConsultar;

import java.sql.SQLException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import sdgcoilvic.logicaDeNegocio.clases.Profesor;
import sdgcoilvic.logicaDeNegocio.implementacionDAO.AccesoDAO;

public class testAcceso {
        @Test
    public void testExisteAccesosExitoso() throws Exception {
        AccesoDAO accesoDAO = new AccesoDAO();
        String usuarioExistente= "administrativo@gmail.com";
        String contraseniaExistente= "eduardA*201@";
        int resultadoEsperado = 1;
        int resultadoObtenido = accesoDAO.verificarExistenciaAcceso(usuarioExistente,contraseniaExistente); 
        assertEquals(resultadoEsperado , resultadoObtenido);
    }
    
    @Test
    public void testAccesoUsuarioInexistente() throws Exception {
        AccesoDAO accesoDAO = new AccesoDAO();
        String usuarioExistente= "administrativo123@gmail.com";
        String contraseniaExistente= "eduardA*201@";
        int resultadoEsperado = 0;
        int resultadoObtenido = accesoDAO.verificarExistenciaAcceso(usuarioExistente,contraseniaExistente); 
        assertEquals(resultadoEsperado , resultadoObtenido);
    }
    
    @Test
    public void testObtenerTipoUsuarioExistente() throws SQLException {
        AccesoDAO accesoDAO = new AccesoDAO();
        String usuarioExistente = "administrativo@gmail.com";
        String contrasenia = "atzin@example.com";
        String tipoUsuarioEsperado = "eduardA*201@"; 
        String tipoUsuarioObtenido = accesoDAO.obtenerTipoUsuario(usuarioExistente, contrasenia);
        assertEquals(tipoUsuarioEsperado, tipoUsuarioObtenido);
    }
    
    @Test
    public void testObtenerIdProfesorExistente() {
        AccesoDAO accesoDAO = new AccesoDAO();
        String usuarioExistente = "atzin@example.com";
        String contrasenia = "eduardA*201@";
        int idProfesorEsperado = 2;
        int idProfesorObtenido = accesoDAO.obtenerIdProfesor(usuarioExistente, contrasenia);
        assertEquals(idProfesorEsperado, idProfesorObtenido);
    }
    
    @Test
    public void testObtenerProfesorPorIDExitoso() throws SQLException {
        AccesoDAO accesoDAO = new AccesoDAO();
        int idProfesorExistente = 1;
        
        Profesor profesorEsperado = new Profesor();
        profesorEsperado.setIdProfesor(1);
        profesorEsperado.setNombre("Erick");
        profesorEsperado.setApellidoPaterno("Atzin");
        profesorEsperado.setApellidoMaterno("Olarte");
        profesorEsperado.setCorreo("atzin@example.com");
        profesorEsperado.setIdIdiomas(1);
        profesorEsperado.setIdAcceso(2);
        profesorEsperado.setEstadoProfesor("Activo");
        profesorEsperado.setClaveInstitucional("30MSU0940B");
        
        Profesor profesorObtenido = accesoDAO.obtenerProfesorPorID(idProfesorExistente);
        
        assertNotNull(profesorObtenido);
        
        assertEquals(profesorEsperado.getIdProfesor(), profesorObtenido.getIdProfesor());
        assertEquals(profesorEsperado.getNombre(), profesorObtenido.getNombre());
        assertEquals(profesorEsperado.getApellidoPaterno(), profesorObtenido.getApellidoPaterno());
        assertEquals(profesorEsperado.getApellidoMaterno(), profesorObtenido.getApellidoMaterno());
        assertEquals(profesorEsperado.getCorreo(), profesorObtenido.getCorreo());
        assertEquals(profesorEsperado.getIdIdiomas(), profesorObtenido.getIdIdiomas());
        assertEquals(profesorEsperado.getIdAcceso(), profesorObtenido.getIdAcceso());
        assertEquals(profesorEsperado.getEstadoProfesor(), profesorObtenido.getEstadoProfesor());
        assertEquals(profesorEsperado.getClaveInstitucional(), profesorObtenido.getClaveInstitucional());
    }
}
