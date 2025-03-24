package object;

import entity.Entity;
import main.GamePanel;

public class ObjDoor extends Entity {

    public ObjDoor(GamePanel gamePanel) {

        super(gamePanel);

        // CONSTANTS
        String name = "Door";
        boolean collision = true;
        String fileName = "door.png"; 
        
        // INITIATIONS
        this.name = name;
        down1 = setup("/objects/" + fileName);
        this.collision = collision;
    }
    public ObjDoor(int x, int y, GamePanel gamePanel) {
        this(gamePanel);
        this.worldX = x * GamePanel.tileSize;
        this.worldY = y * GamePanel.tileSize;
    }
}
