package sdgcoilvic.logicaDeNegocio.clases;

public class TablaColaboracion {
    private int idColaboracion;
    private String nombre;
    private String modalidad;
    private String periodo;
    private String numeroActividades;
    private String numeroEvidencias;
    private String estadoColaboracion;

    public TablaColaboracion() {

    }
    
    public TablaColaboracion(int idColaboracion, String nombre, String modalidad, String periodo, String numeroActividades, String numeroEvidencias, String estadoColaboracion) {
        this.idColaboracion = idColaboracion;
        this.nombre = nombre;
        this.modalidad = modalidad;
        this.periodo = periodo;
        this.numeroActividades = numeroActividades;
        this.numeroEvidencias = numeroEvidencias;
        this.estadoColaboracion = estadoColaboracion;
    }

    public int getIdColaboracion() {
        return idColaboracion;
    }

    public void setIdColaboracion(int idColaboracion) {
        this.idColaboracion = idColaboracion;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getModalidad() {
        return modalidad;
    }
    
    public void setModalidad(String modalidad) {
        this.modalidad = modalidad;
    }

    public String getPeriodo() {
        return periodo;
    }
    
    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public String getNumeroActividades() {
        return numeroActividades;
    }
    
    public void setNumeroActividades(String numeroActividades) {
        this.numeroActividades = numeroActividades;
    }
    
    public String getNumeroEvidencias() {
        return numeroEvidencias;
    }

    public void setNumeroEvidencias(String numeroEvidencias) {
        this.numeroEvidencias = numeroEvidencias;
    }
    
    public String getEstadoColaboracion() {
        return estadoColaboracion;
    }

    public void setEstadoColaboracion(String estadoColaboracion) {
        this.estadoColaboracion = estadoColaboracion;
    }
}
