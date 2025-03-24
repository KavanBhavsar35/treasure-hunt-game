package object;

import entity.Entity;
import main.GamePanel;

public class ObjKey extends Entity{

    public ObjKey(GamePanel gamePanel) {

        super(gamePanel);

        // CONSTANTS
        String NAME = "Key";
        String fileName = "key.png"; 
        
        // INITIATIONS
        this.name = NAME;
        down1 = setup("/objects/" + fileName);
    }
    public ObjKey(int x, int y, GamePanel gamePanel) {
        this(gamePanel);
        this.worldX = x * GamePanel.tileSize;
        this.worldY = y * GamePanel.tileSize;
    }

}
