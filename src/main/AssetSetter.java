package main;

import entity.NpcOldMan;
import monster.MonGreenSlime;
import object.ObjBoots;
import object.ObjChest;
import object.ObjDoor;
import object.ObjKey;
import object.ObjPotion;

public class AssetSetter {
    GamePanel gamePanel;

    // INITIATION
    public AssetSetter(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    // SETUP OBJECTS 
    public void setObject() {
        gamePanel.obj[0] = new ObjKey(23, 7, gamePanel);
        gamePanel.obj[1] = new ObjKey(23, 40, gamePanel);
        gamePanel.obj[2] = new ObjKey(37, 7, gamePanel);
        gamePanel.obj[3] = new ObjDoor(10, 12, gamePanel);
        gamePanel.obj[4] = new ObjDoor(8, 28, gamePanel);
        gamePanel.obj[5] = new ObjDoor(12, 22, gamePanel);
        gamePanel.obj[6] = new ObjChest(10, 7, gamePanel);
        gamePanel.obj[7] = new ObjBoots(37, 42, gamePanel); 
        gamePanel.obj[8] = new ObjPotion(31,42, gamePanel);
    }

    //SETUP NPCs
    public void setNPC() {
        
        gamePanel.npc[0] = new NpcOldMan(gamePanel, 21, 21);
    }

    public void setMonster() {
        gamePanel.monster[0] = new MonGreenSlime(gamePanel, 23, 36);
        gamePanel.monster[1] = new MonGreenSlime(gamePanel, 23, 37);

    }
}
