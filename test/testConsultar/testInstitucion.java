package testConsultar;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import sdgcoilvic.logicaDeNegocio.clases.Institucion;
import sdgcoilvic.logicaDeNegocio.implementacionDAO.InstitucionDAO;

public class testInstitucion {
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
