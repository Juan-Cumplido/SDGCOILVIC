package sdgcoilvic.logicaDeNegocio.clases;

public class TablaInstitucion {
    String clave;
    String nombre;
    String pais;
    String correo;
    
    public TablaInstitucion(String clave, String nombre, String pais, String correo) {
        this.clave = clave;
        this.nombre = nombre;
        this.pais = pais;
        this.correo = correo;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }
    
    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
}
