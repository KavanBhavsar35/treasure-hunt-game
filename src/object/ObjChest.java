package object;

import entity.Entity;
import main.GamePanel;

public class ObjChest extends Entity{

    public ObjChest(GamePanel gamePanel) {

        super(gamePanel);
        
        // CONSTANTS
        String name = "Chest";
        String fileName = "chest.png"; 
        
        // INITIATIONS
        this.name = name;
        down1 = setup("/objects/" + fileName);
    }
    public ObjChest(int x, int y, GamePanel gamePanel) {
        this(gamePanel);
        this.worldX = x * GamePanel.tileSize;
        this.worldY = y * GamePanel.tileSize;
    }
}
