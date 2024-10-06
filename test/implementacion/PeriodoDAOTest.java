package implementacion;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import java.sql.SQLException;
import java.util.List;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import sdgcoilvic.logicaDeNegocio.clases.Periodo;
import sdgcoilvic.logicaDeNegocio.implementacionDAO.PeriodoDAO;


public class PeriodoDAOTest {

    @Test
    public void testAgregarPeriodoExitoso() throws SQLException {
        PeriodoDAO periodoDAO = new PeriodoDAO();
        
        Periodo periodo = new Periodo();
        periodo.setFechaInicio(java.sql.Date.valueOf("2024-09-01"));
        periodo.setFechaFin(java.sql.Date.valueOf("2025-02-28"));
        periodo.setNombrePeriodo("Periodo Sept 2024 - Feb 2025");
        
        int resultadoEsperado = 1;
        int resultadoObtenido = periodoDAO.agregarPeriodo(periodo);
        
        assertEquals(resultadoEsperado, resultadoObtenido);
    }
    
    @Test
    public void testObtenerListaDePeriodosExitoso() throws SQLException {
        PeriodoDAO periodoDAO = new PeriodoDAO();
        List<List<String>> listaDePeriodos = periodoDAO.obtenerListaDePeriodos();
        assertNotNull(listaDePeriodos);
        assertFalse(listaDePeriodos.isEmpty());       
        List<String> periodoEsperado = List.of( "1","2024-09-01", "2025-02-28", "Periodo Sept 2024 - Feb 2025");                
        assertTrue(listaDePeriodos.contains(periodoEsperado));
    }


}
