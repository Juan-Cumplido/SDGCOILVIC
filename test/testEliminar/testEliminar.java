
package testEliminar;

import java.sql.SQLException;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import sdgcoilvic.logicaDeNegocio.implementacionDAO.ProfesorDAO;

public class testEliminar {
       @Test
    public void testEliminarProfesorExitoso() throws SQLException {
        System.out.println("Eliminando Profesor");
        String correo = "atzin.actualizado@example.com"; 
        ProfesorDAO profesorDAO = new ProfesorDAO();
        int columnaAfectada = profesorDAO.eliminarProfesor(correo);
        assertEquals(2, columnaAfectada);
    }
    
    @Test
    public void testEliminarProfesorInexistente() throws SQLException {
        System.out.println("Eliminando Profesor");
        String correo = "oscar@example.com"; 
        ProfesorDAO profesorDAO = new ProfesorDAO();
        int columnaAfectada = profesorDAO.eliminarProfesor(correo);
        assertEquals(0, columnaAfectada);
    } 
    
        @Test
    public void testEliminarProfesorCorreoInvalido() throws SQLException {
        System.out.println("Eliminando Profesor");
        String correo = "atzinexamplecom"; 
        ProfesorDAO profesorDAO = new ProfesorDAO();
        int columnaAfectada = profesorDAO.eliminarProfesor(correo);
        assertEquals(0, columnaAfectada);
    }
}
