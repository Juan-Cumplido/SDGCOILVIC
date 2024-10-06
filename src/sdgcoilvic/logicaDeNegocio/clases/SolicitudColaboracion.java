package sdgcoilvic.logicaDeNegocio.clases;

import java.util.regex.Pattern;

public class SolicitudColaboracion {
    private int idSolicitudColaboracion;
    private String fechaCreacion;
    private String mensaje;
    private int idProfesor;
    private int idEstadoSolicitud;

    private final static String EXPRESION_REGULAR_MENSAJE = "^[\\p{L}áéíóúÁÉÍÓÚüÜ\\s',;:\\-_.0-9]{1,500}$";
    
    public SolicitudColaboracion() {
    }

    public int getIdSolicitudColaboracion() {
        return idSolicitudColaboracion;
    }

    public void setIdSolicitudColaboracion(int idSolicitudColaboracion) {
        this.idSolicitudColaboracion = idSolicitudColaboracion;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        if (mensaje!=null&&Pattern.matches(EXPRESION_REGULAR_MENSAJE, mensaje.trim())) {
            this.mensaje = mensaje.trim().replaceAll("\\s+", " ");
        }else{
            throw new IllegalArgumentException();
        } 
    }

    public int getIdProfesor() {
        return idProfesor;
    }

    public void setIdProfesor(int idProfesor) {
        this.idProfesor = idProfesor;
    }

    public int getIdEstadoSolicitud() {
        return idEstadoSolicitud;
    }

    public void setIdEstadoSolicitud(int idEstadoSolicitud) {
        this.idEstadoSolicitud = idEstadoSolicitud;
    }
}
