package sdgcoilvic.logicaDeNegocio.clases;

import java.util.regex.Pattern;

public class Colaboracion {
    private int idColaboracion;
    private String descripcion;
    private String recursos;
    private int idPeriodo;
    private String aprendizajesEsperados;
    private String detallesAsistenciaParticipacion;
    private String detallesEvaluacion;
    private String detallesEntorno;
    private int idPropuestaColaboracion;
    private String estadoColaboracion;

    private final static String EXPRESION_REGULAR = "^[\\p{L}áéíóúÁÉÍÓÚüÜ\\s',;:\\-_.0-9]{1,500}$";

    public Colaboracion() {
    }

    public int getIdColaboracion() {
        return idColaboracion;
    }

    public void setIdColaboracion(int idColaboracion) {
        this.idColaboracion = idColaboracion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = validarCadena(descripcion);
    }

    public String getRecursos() {
        return recursos;
    }

    public void setRecursos(String recursos) {
        this.recursos = validarCadena(recursos);
    }

    public int getIdPeriodo() {
        return idPeriodo;
    }

    public void setIdPeriodo(int idPeriodo) {
        this.idPeriodo = idPeriodo;
    }

    public String getAprendizajesEsperados() {
        return aprendizajesEsperados;
    }

    public void setAprendizajesEsperados(String aprendizajesEsperados) {
        this.aprendizajesEsperados = validarCadena(aprendizajesEsperados);
    }

    public String getDetallesAsistenciaParticipacion() {
        return detallesAsistenciaParticipacion;
    }

    public void setDetallesAsistenciaParticipacion(String detallesAsistenciaParticipacion) {
        this.detallesAsistenciaParticipacion = validarCadena(detallesAsistenciaParticipacion);
    }

    public String getDetallesEvaluacion() {
        return detallesEvaluacion;
    }

    public void setDetallesEvaluacion(String detallesEvaluacion) {
        this.detallesEvaluacion = validarCadena(detallesEvaluacion);
    }

    public String getDetallesEntorno() {
        return detallesEntorno;
    }

    public void setDetallesEntorno(String detallesEntorno) {
        this.detallesEntorno = validarCadena(detallesEntorno);
    }

    public String getEstadoColaboracion() {
        return estadoColaboracion;
    }

    public void setEstadoColaboracion(String estadoColaboracion) {
        this.estadoColaboracion = estadoColaboracion;
    }

    public int getIdPropuestaColaboracion() {
        return idPropuestaColaboracion;
    }

    public void setIdPropuestaColaboracion(int idPropuestaColaboracion) {
        this.idPropuestaColaboracion = idPropuestaColaboracion;
    }

    private String validarCadena(String cadena) {
        if (cadena != null && Pattern.matches(EXPRESION_REGULAR, cadena.trim())) {
            return cadena.trim().replaceAll("\\s+", " ");
        } else {
            throw new IllegalArgumentException("La cadena no cumple con las reglas de validación.");
        }
    }
}
