package sdgcoilvic.logicaDeNegocio.clases;

public class TablaPropuestasColaboracion {
    private int idPropuestaColaboracion;
    private String tipoColaboracion;
    private String nombre;
    private String objetivoGeneral;
    private String temas;
    private String idPeriodo;
    private String estadoPropuesta;
    private String nombreProfesor; 
    private String idioma; 

    public TablaPropuestasColaboracion(int idPropuestaColaboracion, String tipoColaboracion, String nombre,
            String objetivoGeneral, String temas, String idPeriodo, String estadoPropuesta, String nombreProfesor,String idioma) {
        this.idPropuestaColaboracion = idPropuestaColaboracion;
        this.tipoColaboracion = tipoColaboracion;
        this.nombre = nombre;
        this.objetivoGeneral = objetivoGeneral;
        this.temas = temas;
        this.idPeriodo = idPeriodo;
        this.estadoPropuesta = estadoPropuesta;
        this.nombreProfesor = nombreProfesor;
        this.idioma = idioma;
    }
    
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
        this.nombre = nombre;
        
    }

    public String getObjetivoGeneral() {
        return objetivoGeneral;
    }

    public void setObjetivoGeneral(String objetivoGeneral) {
        this.objetivoGeneral = objetivoGeneral;
    }

    public String getTemas() {
        return temas;
    }

    public void setTemas(String temas) {
        this.temas = temas;
    }

    public String getIdPeriodo() {
        return idPeriodo;
    }

    public void setIdPeriodo(String idPeriodo) {
        this.idPeriodo = idPeriodo;
    }

    public String getEstadoPropuesta() {
        return estadoPropuesta;
    }

    public void setEstadoPropuesta(String estadoPropuesta) {
        this.estadoPropuesta = estadoPropuesta;
    }    
    
    public String getNombreProfesor() {
        return nombreProfesor;
    }

    public void setNombreProfesor(String nombreProfesor) {
        this.nombreProfesor = nombreProfesor;
    }
    
    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }
    
}
