package object;

import java.io.IOException;
import javax.imageio.ImageIO;

public class OBJ_Chest extends SuperObject{

    public OBJ_Chest(){

        name = "Chest";
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/res/objects/obj3.png"));
        }catch(IOException e) {
            e.printStackTrace();
        }
    }

}
