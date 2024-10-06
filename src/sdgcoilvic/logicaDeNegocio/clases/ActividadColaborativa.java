package sdgcoilvic.logicaDeNegocio.clases;

import java.util.regex.Pattern;

public final class ActividadColaborativa {
    private int idActividadColaborativa;
    private String nombreActividad;
    private String instruccion;
    private int idColaboracion;
    private String fechaInicio;
    private String fechaCierre;
    private String herramienta;
    private String estadoActividad;
    private int idProfesor;
    
    private final static String EXPRESION_REGULAR_NOMBRE = "^[\\p{L}áéíóúÁÉÍÓÚüÜ\\s',;:\\-_.0-9]{1,100}$";
    private final static String EXPRESION_REGULAR_DESCRIPCION = "^[\\p{L}áéíóúÁÉÍÓÚüÜ\\s',;:\\-_.0-9]{1,500}$";
    private final static String EXPRESION_REGULAR_HERRAMIENTA = "^[\\p{L}áéíóúÁÉÍÓÚüÜ\\s',;:\\-_.0-9]{1,200}$";

    public ActividadColaborativa(int idActividadColaborativa, String nombreActividad, String instruccion, 
            int idColaboracion, String fechaInicio, String fechaCierre, String herramienta, 
            String estadoActividad, int idProfesor) {
        this.idActividadColaborativa = idActividadColaborativa;
        setNombreActividad(nombreActividad);
        setInstruccion(instruccion); 
        this.idColaboracion = idColaboracion;
        this.fechaInicio = fechaInicio;
        this.fechaCierre = fechaCierre;
        setHerramienta(herramienta); 
        this.estadoActividad = estadoActividad;
        this.idProfesor = idProfesor;
        
    }

    public ActividadColaborativa() {
    }
    
    public int getIdActividadColaborativa() {
        return idActividadColaborativa;
    }

    public void setIdActividadColaborativa(int idActividadColaborativa) {
        this.idActividadColaborativa = idActividadColaborativa;
    }

    public String getNombreActividad() {
        return nombreActividad;
    }

    public void setNombreActividad(String nombreActividad) {
        if (nombreActividad!=null&&Pattern.matches(EXPRESION_REGULAR_NOMBRE, nombreActividad.trim())) {
            this.nombreActividad = nombreActividad.trim().replaceAll("\\s+", " ");
        }else{
            throw new IllegalArgumentException();
        } 
    }

    public String getInstruccion() {
        return instruccion;
    }

    public void setInstruccion(String instruccion) {
        if (instruccion!=null&&Pattern.matches(EXPRESION_REGULAR_DESCRIPCION, instruccion.trim())) {
            this.instruccion = instruccion.trim().replaceAll("\\s+", " ");
        }else{
            throw new IllegalArgumentException();
        } 
    }

    public int getIdColaboracion() {
        return idColaboracion;
    }

    public void setIdColaboracion(int idColaboracion) {
        this.idColaboracion = idColaboracion;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaCierre() {
        return fechaCierre;
    }

    public void setFechaCierre(String fechaCierre) {
        this.fechaCierre = fechaCierre;
    }

    public String getHerramienta() {
        return herramienta;
    }

    public void setHerramienta(String herramienta) {
        if (herramienta!=null&&Pattern.matches(EXPRESION_REGULAR_HERRAMIENTA, herramienta.trim())) {
            this.herramienta = herramienta.trim().replaceAll("\\s+", " ");
        }else{
            throw new IllegalArgumentException();
        } 
    }

    public String getEstadoActividad() {
        return estadoActividad;
    }

    public void setEstadoActividad(String estadoActividad) {
        this.estadoActividad = estadoActividad;
    }
    
    public int getIdProfesor() {
        return idProfesor;
    }

    public void setIdProfesor(int idProfesor) {
        this.idProfesor = idProfesor;
    }
}
