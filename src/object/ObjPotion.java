package object;

import entity.Entity;
import main.GamePanel;

public class ObjPotion extends Entity{

    public ObjPotion(GamePanel gamePanel) {

        super(gamePanel);

        // CONSTANTS
        String NAME = "Potion";
        String fileName = "potion.png"; 
        
        // INITIATIONS
        this.name = NAME;
        down1 = setup("/objects/" + fileName);
    }
    public ObjPotion(int x, int y, GamePanel gamePanel) {
        this(gamePanel);
        this.worldX = x * GamePanel.tileSize;
        this.worldY = y * GamePanel.tileSize;
    }

}
