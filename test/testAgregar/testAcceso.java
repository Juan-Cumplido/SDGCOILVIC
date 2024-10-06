
package testAgregar;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import sdgcoilvic.logicaDeNegocio.clases.Acceso;
import sdgcoilvic.logicaDeNegocio.enums.EnumTipoDeAcceso;
import sdgcoilvic.logicaDeNegocio.implementacionDAO.AccesoDAO;


public class testAcceso {
    @Test
    public void testAgregarAccesoAlSistemaExitoso() throws Exception {
        System.out.println("registrarAcceso");
        Acceso acceso = new Acceso();
        AccesoDAO accesoDAO = new AccesoDAO();
        acceso.setContrasenia("eduardA*201@");
        acceso.setUsuario("administrativo@gmail.com");
        acceso.setTipoUsuario(EnumTipoDeAcceso.Administrativo.toString());
        int resultadoEsperado = 1;
        int resultadoObtenido = accesoDAO.agregarAcceso(acceso);
        assertEquals(resultadoEsperado , resultadoObtenido);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAgregarAccesoContraseñaCorta() throws Exception {
        System.out.println("registrarAcceso");
        Acceso acceso = new Acceso();
        AccesoDAO accesoDAO = new AccesoDAO();
        acceso.setContrasenia("edu");
        acceso.setUsuario("administrativo@gmail.com");
        acceso.setTipoUsuario(EnumTipoDeAcceso.Administrativo.toString());
        int resultadoEsperado = 1;
        int resultadoObtenido = accesoDAO.agregarAcceso(acceso);
        assertEquals(resultadoEsperado , resultadoObtenido);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testAgregarAccesoContraseñaSinMinuscula() throws Exception {
        System.out.println("registrarAcceso");
        Acceso acceso = new Acceso();
        AccesoDAO accesoDAO = new AccesoDAO();
        acceso.setContrasenia("EDUARDA*201@");
        acceso.setUsuario("administrativo@gmail.com");
        acceso.setTipoUsuario(EnumTipoDeAcceso.Administrativo.toString());
        int resultadoEsperado = 1;
        int resultadoObtenido = accesoDAO.agregarAcceso(acceso);
        assertEquals(resultadoEsperado , resultadoObtenido);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testAgregarAccesoContraseñaSinMayuscula() throws Exception {
        System.out.println("registrarAcceso");
        Acceso acceso = new Acceso();
        AccesoDAO accesoDAO = new AccesoDAO();
        acceso.setContrasenia("eduarda*201@");
        acceso.setUsuario("administrativo@gmail.com");
        acceso.setTipoUsuario(EnumTipoDeAcceso.Administrativo.toString());
        int resultadoEsperado = 1;
        int resultadoObtenido = accesoDAO.agregarAcceso(acceso);
        assertEquals(resultadoEsperado , resultadoObtenido);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testAgregarAccesoContraseñaSinDigito() throws Exception {
        System.out.println("registrarAcceso");
        Acceso acceso = new Acceso();
        AccesoDAO accesoDAO = new AccesoDAO();
        acceso.setContrasenia("eduardA*@");
        acceso.setUsuario("administrativo@gmail.com");
        acceso.setTipoUsuario(EnumTipoDeAcceso.Administrativo.toString());
        int resultadoEsperado = 1;
        int resultadoObtenido = accesoDAO.agregarAcceso(acceso);
        assertEquals(resultadoEsperado , resultadoObtenido);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testAgregarAccesoContraseñaSinCaracterEspecial() throws Exception {
        System.out.println("registrarAcceso");
        Acceso acceso = new Acceso();
        AccesoDAO accesoDAO = new AccesoDAO();
        acceso.setContrasenia("eduardA201");
        acceso.setUsuario("administrativo@gmail.com");
        acceso.setTipoUsuario(EnumTipoDeAcceso.Administrativo.toString());
        int resultadoEsperado = 1;
        int resultadoObtenido = accesoDAO.agregarAcceso(acceso);
        assertEquals(resultadoEsperado , resultadoObtenido);
    }
        
    @Test (expected = IllegalArgumentException.class)
    public void testAgregarAccesoCorreoInvalido() throws Exception {
        System.out.println("registrarAcceso");
        Acceso acceso = new Acceso();
        AccesoDAO accesoDAO = new AccesoDAO();
        acceso.setContrasenia("eduardA*201@");
        acceso.setUsuario("administrativogmailcom");
        acceso.setTipoUsuario(EnumTipoDeAcceso.Administrativo.toString());
        int resultadoEsperado = 1;
        int resultadoObtenido = accesoDAO.agregarAcceso(acceso);
        assertEquals(resultadoEsperado , resultadoObtenido);
    }
}
