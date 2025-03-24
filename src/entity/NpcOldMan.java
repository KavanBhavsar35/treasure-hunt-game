package entity;

import java.util.Random;

import main.GamePanel;

public class NpcOldMan extends Entity{
    
    public NpcOldMan(GamePanel gamePanel, int x, int y) {
        this(gamePanel);
        this.worldX = x * GamePanel.tileSize;
        this.worldY = y * GamePanel.tileSize;
    }

    public NpcOldMan(GamePanel gamePanel) {
        super(gamePanel);
        
        direction = "down";
        speed = 1;

        solidArea.x = 3;
        solidArea.y = 18;
        solidArea.width = 42;
        solidArea.height = 30;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        this.getPlayerImage();
        this.setDialouge();
    }

    // LOAD IMAGES
    public void getPlayerImage() {
    
        up1 = setup("/npc/oldman_up_1.png");
        up2 = setup("/npc/oldman_up_2.png");
        down2 = setup("/npc/oldman_down_2.png");
        down1 = setup("/npc/oldman_down_1.png");
        left1 = setup("/npc/oldman_left_1.png");
        left2 = setup("/npc/oldman_left_2.png");
        right1 = setup("/npc/oldman_right_1.png");
        right2 = setup("/npc/oldman_right_2.png");
    }

    // DIALOUGES
    public void setDialouge() {
        dialouges[0] = "Hello, lad.";
        dialouges[1] = "So you've come to this island to \nfind the treasure ?";
        dialouges[2] = "I used to be a great wizardd but now... \nI'm a bit too old for taking an adventure";
        dialouges[3] = "Well, Good Luck to you !";
    }

    //
    public void setAction() {

        actionLockCounter++;

        if (actionLockCounter == 120) {
            Random random = new Random();
            int i = random.nextInt(100) + 1; // PICK A NUM FROM 1 - 100
            
            if (i <= 25) direction = "up";
            if (i >= 25 && i <=50) direction = "down";
            if (i >= 51 && i <=75) direction = "left";
            if (i >= 75 && i <=100) direction = "right";
            
            actionLockCounter = 0;
        }
    }

    public void speak() {
        super.speak();
    }
}