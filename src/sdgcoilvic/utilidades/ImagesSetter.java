package sdgcoilvic.utilidades;

import javafx.scene.image.Image;

public class ImagesSetter {

    private static final Image imageAccesoFondo = new Image(ImagesSetter.class.getResourceAsStream("/sdgcoilvic/recursos/AccesoFondo.png"));
    private static final Image imageEscudoCoilvic = new Image(ImagesSetter.class.getResourceAsStream("/sdgcoilvic/recursos/escudocoilvic.jpg"));
    private static final Image imageMenuAdministrativo = new Image(ImagesSetter.class.getResourceAsStream("/sdgcoilvic/recursos/MenuAdministrativo.png"));
    private static final Image imageMenuProfesor = new Image(ImagesSetter.class.getResourceAsStream("/sdgcoilvic/recursos/MenuProfesor.png"));
    private static final Image imageSubMenu = new Image(ImagesSetter.class.getResourceAsStream("/sdgcoilvic/recursos/SubMenu.png"));
    
    public static Image getImageAccesoFondo(){
        return imageAccesoFondo;
    }
    
    public static Image getimageEscudoCoilvic() {
        return imageEscudoCoilvic;
    }
    
    public static Image getImageMenuAdministrativo() {
        return imageMenuAdministrativo;
    }
    
    public static Image getImageMenuProfesor() {
        return imageMenuProfesor;
    }
    
    public static Image getImageSubMenu() {
        return imageSubMenu;
    }
}

