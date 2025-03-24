package object;

import entity.Entity;
import main.GamePanel;

public class ObjBoots extends Entity{
    public ObjBoots(GamePanel gamePanel) {

        super(gamePanel);// CONSTANTS
        String NAME = "Boots";
        String fileName = "boots.png"; 
        
        // INITIATIONS
        this.name = NAME;
        down1 = setup("/objects/" + fileName);

    }
    public ObjBoots(int x, int y, GamePanel gamePanel) {
        this(gamePanel);
        this.worldX = x * GamePanel.TILE_SIZE;
        this.worldY = y * GamePanel.TILE_SIZE;
    }
}
