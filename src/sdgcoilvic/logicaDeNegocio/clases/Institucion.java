package sdgcoilvic.logicaDeNegocio.clases;

import java.util.regex.Pattern;

public class Institucion {
    private String claveInstitucional;
    private String nombreInstitucion;
    private String nombrePais;
    private String correo;

    private final static String EXPRESION_REGULAR_CLAVE_INSTITUCIONAL = "^[A-Z0-9']{1,20}$";
    private final static String EXPRESION_REGULAR_NOMBRE_INSTITUCION = "^[\\p{L}áéíóúÁÉÍÓÚüÜ\\s',\\-_\\.]{1,200}$";
    private final static String EXPRESION_REGULAR_NOMBRE_PAIS = "^[\\p{L}áéíóúÁÉÍÓÚüÜ\\s',\\-]{1,60}$";
    private final static String EXPRESION_REGULAR_CORREO_ELECTRONICO = "^[a-zA-Z0-9_+&-]+(?:\\.[a-zA-Z0-9_+&-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";


    public String getClaveInstitucional() {
        return claveInstitucional;
    }

    public void setClaveInstitucional(String claveInstitucional) {
        if (claveInstitucional!=null&&Pattern.matches(EXPRESION_REGULAR_CLAVE_INSTITUCIONAL, claveInstitucional.trim())) {
            this.claveInstitucional = claveInstitucional.trim().replaceAll("\\s+", " ");
        }else{
            throw new IllegalArgumentException();
        }
    }

    public String getNombreInstitucion() {
        return nombreInstitucion;
    }

    public void setNombreInstitucion(String nombreInstitucion) {
        if (nombreInstitucion!=null&&Pattern.matches(EXPRESION_REGULAR_NOMBRE_INSTITUCION, nombreInstitucion.trim())) {
            this.nombreInstitucion = nombreInstitucion.trim().replaceAll("\\s+", " ");
        }else{
            throw new IllegalArgumentException();
        } 
    }

    public String getNombrePais() {
        return nombrePais;
    }

    public void setNombrePais(String nombrePais) {
        if (nombrePais!=null&&Pattern.matches(EXPRESION_REGULAR_NOMBRE_PAIS,nombrePais.trim())) {
            this.nombrePais = nombrePais.trim().replaceAll("\\s+", " ");
        }else{
            throw new IllegalArgumentException();
        } 
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        if (correo!=null&&Pattern.matches(EXPRESION_REGULAR_CORREO_ELECTRONICO, correo.trim())) {
            this.correo = correo.trim().replaceAll("\\s+", " ");
        }else{
            throw new IllegalArgumentException();
        } 
    }
    
     @Override
    public String toString() {
        return claveInstitucional + " " + nombreInstitucion + " " + nombrePais + " " + correo;
    }
    
    @Override 
    public boolean equals(Object object) {
        if((object == null) || (object.getClass() != this.getClass())) {
            return false;
        } 
       final Institucion otraInstitucion = (Institucion) object;
       return (this.claveInstitucional == null ? otraInstitucion.claveInstitucional == null : this.claveInstitucional.equals(otraInstitucion.claveInstitucional))
            && (this.nombreInstitucion == null ? otraInstitucion.nombreInstitucion == null : this.nombreInstitucion.equals(otraInstitucion.nombreInstitucion))
            && (this.nombrePais == null ? otraInstitucion.nombrePais == null : this.nombrePais.equals(otraInstitucion.nombrePais))   
            && (this.correo == null ? otraInstitucion.correo == null : this.correo.equals(otraInstitucion.correo));
 
    }
}