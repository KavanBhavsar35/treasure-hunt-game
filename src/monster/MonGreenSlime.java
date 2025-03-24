package monster;

import entity.Entity;
import java.util.Random;
import main.GamePanel;

public final class MonGreenSlime extends Entity{

    public MonGreenSlime(GamePanel gamePanel, int x, int y) {
        this(gamePanel);
        this.worldX = x * GamePanel.TILE_SIZE;
        this.worldY = y * GamePanel.TILE_SIZE;
    }

    public MonGreenSlime(GamePanel gamePanel) {
        super(gamePanel);

        type = 2;
        name = "GreenSlime";
        speed = 1;
        maxLife = 4;
        life = maxLife;

        solidArea.x = 3;
        solidArea.y = 18;
        solidArea.width = 42;
        solidArea.height = 30;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        this.getImages();
    }

    public void getImages() {
        up1 = setup("/monster/greenslime_down_1.png");
        up2 = setup("/monster/greenslime_down_2.png");
        down1 = setup("/monster/greenslime_down_1.png");
        down2 = setup("/monster/greenslime_down_2.png");
        left1 = setup("/monster/greenslime_down_1.png");
        left2 = setup("/monster/greenslime_down_2.png");
        right1 = setup("/monster/greenslime_down_1.png");
        right2 = setup("/monster/greenslime_down_2.png");
    }
    
    @Override
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
}
