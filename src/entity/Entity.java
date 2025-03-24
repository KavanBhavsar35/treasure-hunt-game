package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.UtilityTool;

public class Entity {

    //  CONSTANTS
    public final int maxDialouges = 20;

    // INITIATION
    GamePanel gamePanel;
    public BufferedImage image, image2, image3;
    public String name;
    public boolean collision = false;
    public boolean isInvincible = false;
    public int invincibleCounter = 0; 
    public int invincibleDuration = 100; 
    public int worldX, worldY;
    public int speed;
    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    public String direction = "down";
    public int spriteCounter = 0;
    public int spriteNum = 1;
    public Rectangle solidArea = new Rectangle(0, 0, GamePanel.tileSize, GamePanel.tileSize);
    public int solidAreaDefaultX, solidAreaDefaultY;
    public boolean collisionOn = false;
    public int actionLockCounter = 0;
    String[] dialouges = new String[maxDialouges];
    int dialougeIndex = 0;
    public int type; //0 = PLAYER, 1 = NPC, 2 = MONSTER

    // CHARACTER STATUS
    public int maxLife;
    public int life;


    public Entity(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    // SETTING ACTIONS 
    public void setAction() {}

    // SPEAK ACTION
    public void speak() {
        if (dialouges[dialougeIndex] == null) dialougeIndex = 0;
        gamePanel.ui.currentDialouge = dialouges[dialougeIndex++];

        switch (gamePanel.player.direction) {
            case "up": this.direction = "down"; break;
            case "down": this.direction = "up"; break;
            case "left": this.direction = "right"; break;
            case "right": this.direction = "left"; break;
        }
    }

    // UPDATE
    public void update() {

        setAction();

        collisionOn = false;

        gamePanel.collisionChecker.checkObject(this, false);
        gamePanel.collisionChecker.checkTile(this);
        gamePanel.collisionChecker.checkEntity(this, gamePanel.npc);
        gamePanel.collisionChecker.checkEntity(this, gamePanel.monster);
        boolean contactPlayer = gamePanel.collisionChecker.checkPlayer(this);


        if (this.type ==2 && contactPlayer) {
            if (!gamePanel.player.isInvincible) {
                gamePanel.player.life -= 1;
                gamePanel.player.isInvincible = true;
            }
        }
        // MOVE IF COLLISION IS OFF
        if (collisionOn == false) {
            switch (direction) {
                case "up": worldY -= speed; break;
                case "down": worldY += speed; break;
                case "left": worldX -= speed; break;
                case "right": worldX += speed; break;
            }
        }

        // SWAP BETWEEN DIFFERENT IMAGES
        spriteCounter++;
        if (spriteCounter > 12) {
            if (spriteNum == 1) spriteNum = 2; 
            else if (spriteNum == 2) spriteNum = 1; 
            spriteCounter = 0;
        }
    }

    // SETUP
    public BufferedImage setup(String imagePath) {

        BufferedImage image = null;
        try {
            image = ImageIO.read(getClass().getResourceAsStream(imagePath));
            image = UtilityTool.scaleImage(image, GamePanel.tileSize, GamePanel.tileSize);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return image;
    }

    public void draw (Graphics2D g2) {

        BufferedImage image = null;

        int screenX = worldX - gamePanel.player.worldX + gamePanel.player.screenX;
        int screenY = worldY - gamePanel.player.worldY + gamePanel.player.screenY;

        // CONDITIONAL OBJECT RENDERING
        if (worldX + GamePanel.tileSize > gamePanel.player.worldX - gamePanel.player.screenX &&
                worldX - GamePanel.tileSize < gamePanel.player.worldX + gamePanel.player.screenX &&
                worldY + GamePanel.tileSize > gamePanel.player.worldY - gamePanel.player.screenY &&
                worldY - GamePanel.tileSize < gamePanel.player.worldY + gamePanel.player.screenY) {

            switch (direction) {
                case "up":
                    if (spriteNum == 1)    image = up1;
                    if (spriteNum == 2)    image = up2;
                    break;
                case "down":
                    if (spriteNum == 1)    image = down1;
                    if (spriteNum == 2)    image = down2;
                    break;
                case "left":
                    if (spriteNum == 1)    image = left1;
                    if (spriteNum == 2)    image = left2;
                    break;
                case "right":
                    if (spriteNum == 1)    image = right1;
                    if (spriteNum == 2)    image = right2;
                    break;
            }

            g2.drawImage(image, screenX, screenY, GamePanel.tileSize, GamePanel.tileSize, null);
        }
    }
}
