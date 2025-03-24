package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
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
    public Rectangle solidArea = new Rectangle(0, 0, GamePanel.TILE_SIZE, GamePanel.TILE_SIZE);
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
        gamePanel.gameUi.currentDialouge = dialouges[dialougeIndex++];

        switch (gamePanel.player.direction) {
            case "up" -> this.direction = "down";
            case "down" -> this.direction = "up";
            case "left" -> this.direction = "right";
            case "right" -> this.direction = "left";
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
                case "up" -> worldY -= speed;
                case "down" -> worldY += speed;
                case "left" -> worldX -= speed;
                case "right" -> worldX += speed;
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

        BufferedImage entityImage = null;
        try {
            entityImage = ImageIO.read(getClass().getResourceAsStream(imagePath));
            entityImage = UtilityTool.scaleImage(entityImage, GamePanel.TILE_SIZE, GamePanel.TILE_SIZE);
        } catch (IOException e) {
            System.err.println("Error loading image from: " + imagePath + " - " + e.getMessage());
        }
        return entityImage;
    }

    public void draw (Graphics2D g2) {

        BufferedImage entityImage;
        entityImage = null;

        int screenX = worldX - gamePanel.player.worldX + gamePanel.player.screenX;
        int screenY = worldY - gamePanel.player.worldY + gamePanel.player.screenY;

        // CONDITIONAL OBJECT RENDERING
        if (worldX + GamePanel.TILE_SIZE > gamePanel.player.worldX - gamePanel.player.screenX &&
                worldX - GamePanel.TILE_SIZE < gamePanel.player.worldX + gamePanel.player.screenX &&
                worldY + GamePanel.TILE_SIZE > gamePanel.player.worldY - gamePanel.player.screenY &&
                worldY - GamePanel.TILE_SIZE < gamePanel.player.worldY + gamePanel.player.screenY) {

            switch (direction) {
                case "up" -> {
                    if (spriteNum == 1)    entityImage = up1;
                    if (spriteNum == 2)    entityImage = up2;
                }
                case "down" -> {
                    if (spriteNum == 1)    entityImage = down1;
                    if (spriteNum == 2)    entityImage = down2;
                }
                case "left" -> {
                    if (spriteNum == 1)    entityImage = left1;
                    if (spriteNum == 2)    entityImage = left2;
                }
                case "right" -> {
                    if (spriteNum == 1)    entityImage = right1;
                    if (spriteNum == 2)    entityImage = right2;
                }
            }

            g2.drawImage(entityImage, screenX, screenY, GamePanel.TILE_SIZE, GamePanel.TILE_SIZE, null);
        }
    }
}
