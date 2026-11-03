package module;

import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class AppIcon {
    
    public static void setIcon(JFrame frame){
        
        try {
            Image icon = new ImageIcon(AppIcon.class.getResource("/resources/icon/smartCity360Icon.png")).getImage();
            frame.setIconImage(icon);
        } catch (Exception e) {
            System.out.println("Icon can't be found!");
        }
        
    }
    
}
