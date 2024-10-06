package sdgcoilvic.logicaDeNegocio.clases;

public class TablaSolicitudesColaboracion {
    private int idSolicitudColaboracion;
    private String nombreColaboracion;
    private String nombreProfesor;
    private String InstitucionEducativa;
    private String idioma;
    private String mensaje;
    private String fecha;
    private String correo;

    public TablaSolicitudesColaboracion(int idSolicitudColaboracion, String nombreColaboracion, String nombreProfesor,
            String InstitucionEducativa, String idioma, String mensaje, String fecha, String correo) {
        this.idSolicitudColaboracion = idSolicitudColaboracion;
        this.nombreColaboracion = nombreColaboracion;
        this.nombreProfesor = nombreProfesor;
        this.InstitucionEducativa = InstitucionEducativa;
        this.idioma = idioma;
        this.mensaje = mensaje;
        this.fecha = fecha;
        this.correo = correo;
    }
    
    public int getIdSolicitudColaboracion() {
        return idSolicitudColaboracion;
    }

    public void setIdSolicitudColaboracion(int idSolicitudColaboracion) {
        this.idSolicitudColaboracion = idSolicitudColaboracion;
    }

    public String getNombreColaboracion() {
        return nombreColaboracion;
    }

    public void setNombreColaboracion(String nombreColaboracion) {
        this.nombreColaboracion = nombreColaboracion;
    }

    public String getNombreProfesor() {
        return nombreProfesor;
    }

    public void setNombreProfesor(String nombreProfesor) {
        this.nombreProfesor = nombreProfesor;
    }

    public String getInstitucionEducativa() {
        return InstitucionEducativa;
    }

    public void setInstitucionEducativa(String InstitucionEducativa) {
        this.InstitucionEducativa = InstitucionEducativa;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
}
