package sdgcoilvic.logicaDeNegocio.clases;

import java.util.regex.Pattern;

public class PropuestaColaboracion {
    private int idPropuestaColaboracion;
    private String tipoColaboracion;
    private String nombre;
    private String objetivoGeneral;
    private String temas;
    private int numeroEstudiante;
    private String informacionAdicional;
    private String perfilDeLosEstudiantes;
    private int idIdiomas;
    private int idPeriodo;
    private int idProfesor;
    private String estadoPropuesta;

    private final static String EXPRESION_REGULAR_NOMBRE = "^[\\p{L}áéíóúÁÉÍÓÚüÜ\\s',;:\\-_.0-9]{1,200}$";
    private final static String NUMERO_ESTUDIANTE = "^(?:1[0-4][0-9]|150|[1-9][0-9])$";
    private final static String EXPRESION_REGULAR_OBJETIVO_TEMA = "^[\\p{L}áéíóúÁÉÍÓÚüÜ\\s',;:\\-_.0-9]{1,500}$";
        private final static String EXPRESION_REGULAR_INFORMACION_ADICIONAL = "^[\\p{L}áéíóúÁÉÍÓÚüÜ\\s',;:\\-_.0-9]{1,1000}$";
    private final static String EXPRESION_REGULAR_PERFIL_ESTUDIANTE= "^[\\p{L}áéíóúÁÉÍÓÚüÜ\\s',\\-\\.]{1,45}$";      

    public int getIdPropuestaColaboracion() {
        return idPropuestaColaboracion;
    }

    public void setIdPropuestaColaboracion(int idPropuestaColaboracion) {
        this.idPropuestaColaboracion = idPropuestaColaboracion;
    }

    public String getTipoColaboracion() {
        return tipoColaboracion;
    }

    public void setTipoColaboracion(String tipoColaboracion) {
        this.tipoColaboracion = tipoColaboracion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre!=null&&Pattern.matches(EXPRESION_REGULAR_NOMBRE, nombre.trim())) {
            this.nombre = nombre.trim().replaceAll("\\s+", " ");
        }else{
            throw new IllegalArgumentException();
        } 
    }

    public String getObjetivoGeneral() {
        return objetivoGeneral;
    }

    public void setObjetivoGeneral(String objetivoGeneral) {
        if (objetivoGeneral!=null&&Pattern.matches(EXPRESION_REGULAR_OBJETIVO_TEMA, objetivoGeneral.trim())) {
            this.objetivoGeneral = objetivoGeneral.trim().replaceAll("\\s+", " ");
        }else{
            throw new IllegalArgumentException();
        } 
    }

    public String getTemas() {
        return temas;
    }

    public void setTemas(String temas) {
        if (temas!=null&&Pattern.matches(EXPRESION_REGULAR_OBJETIVO_TEMA, temas.trim())) {
            this.temas = temas.trim().replaceAll("\\s+", " ");
        }else{
            throw new IllegalArgumentException();
        } 
    }

    public int getNumeroEstudiante() {
        return numeroEstudiante;
    }

    public void setNumeroEstudiante(int numeroEstudiante) {
            String numeroStr = String.valueOf(numeroEstudiante);
            if (numeroStr.matches(NUMERO_ESTUDIANTE)) {
                this.numeroEstudiante = numeroEstudiante;
            } else {
                throw new IllegalArgumentException();
            }
    }

    public String getInformacionAdicional() {
        return informacionAdicional;
    }

    public void setInformacionAdicional(String informacionAdicional) {
        if (informacionAdicional!=null&&Pattern.matches(EXPRESION_REGULAR_INFORMACION_ADICIONAL, informacionAdicional.trim())) {
            this.informacionAdicional = informacionAdicional.trim().replaceAll("\\s+", " ");
        }else{
            throw new IllegalArgumentException();
        } 
    }

    public String getPerfilDeLosEstudiantes() {
        return perfilDeLosEstudiantes;
    }

    public void setPerfilDeLosEstudiantes(String perfilDeLosEstudiantes) {
        if (perfilDeLosEstudiantes!=null&&Pattern.matches(EXPRESION_REGULAR_PERFIL_ESTUDIANTE, perfilDeLosEstudiantes.trim())) {
            this.perfilDeLosEstudiantes = perfilDeLosEstudiantes.trim().replaceAll("\\s+", " ");
        }else{
            throw new IllegalArgumentException();
        } 
    }

    public int getIdIdiomas() {
        return idIdiomas;
    }

    public void setIdIdiomas(int idIdiomas) {
        this.idIdiomas = idIdiomas;
    }

    public int getIdPeriodo() {
        return idPeriodo;
    }

    public void setIdPeriodo(int idPeriodo) {
        this.idPeriodo = idPeriodo;
    }

    public int getIdProfesor() {
        return idProfesor;
    }

    public void setIdProfesor(int idProfesor) {
        this.idProfesor = idProfesor;
    }

    public String getEstadoPropuesta() {
        return estadoPropuesta;
    }

    public void setEstadoPropuesta(String estadoPropuesta) {
        this.estadoPropuesta = estadoPropuesta;
    }
}