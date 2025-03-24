package object;

import entity.Entity;
import main.GamePanel;

public class ObjHeart extends Entity {

    public ObjHeart(GamePanel gamePanel) {

        super(gamePanel);
        // CONSTANTS
        String name = "Heart";
        String fileName1 = "heart_full.png"; 
        String fileName2 = "heart_half.png"; 
        String fileName3 = "heart_blank.png"; 

        // INITIATION
        this.name = name;
        this.image = setup("/objects/" + fileName1);
        this.image2 = setup("/objects/" + fileName2);
        this.image3 = setup("/objects/" + fileName3);
    }
}
