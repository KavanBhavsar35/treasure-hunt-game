package entity;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import main.GamePanel;
import main.KeyHandler;

public final class Player extends Entity {
    
    // INITATION FOR INSTANCES
    KeyHandler keyHandler;
    
    public final int screenX;
    public final int screenY;
    public int standCounter = 0;
    public int hasKeys = 0;

    // CONSTRUCTOR
    public Player(GamePanel gamePanel, KeyHandler keyHandler) {
        
        // INITIATION
        super(gamePanel);
        this.keyHandler = keyHandler;

        // PLAYER CENTER POSITION
        screenX = GamePanel.SCREEN_WIDTH / 2 - (GamePanel.TILE_SIZE / 2);
        screenY = GamePanel.SCREEN_HEIGHT / 2 - (GamePanel.TILE_SIZE / 2);

        // COLLIDABLE AREA FOR PLAYER
        solidArea = new Rectangle();
        solidArea.x = 8;
        solidArea.y = 16;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 32;
        solidArea.height = 32;

        this.setDefaultValue();
        this.getPlayerImage();

    }

    // DEFAULT VALS
    public void setDefaultValue() {
        worldX = GamePanel.TILE_SIZE * 23;
        worldY = GamePanel.TILE_SIZE * 21;
        speed = 4;
        direction = "down";

        // PLAYER STATUS
        maxLife = 6;
        life = maxLife;
    }

    // LOAD IMAGES
    public void getPlayerImage() {

        up1 = setup("/player/boy_up_1.png");
        up2 = setup("/player/boy_up_2.png");
        down2 = setup("/player/boy_down_2.png");
        down1 = setup("/player/boy_down_1.png");
        left1 = setup("/player/boy_left_1.png");
        left2 = setup("/player/boy_left_2.png");
        right1 = setup("/player/boy_right_1.png");
        right2 = setup("/player/boy_right_2.png");
    }

    // UPDATE PLAYER IF CHANGED
    @Override
    public void update() {
    	
        if (gamePanel.player.life == 0) {
            gamePanel.gameState = GamePanel.TITLE_STATE;
        }
    	if (keyHandler.upPressed == true || keyHandler.downPressed == true || 
    			keyHandler.leftPressed == true || keyHandler.rightPressed == true) {

	        if (keyHandler.upPressed == true)  direction = "up"; 
            else if (keyHandler.downPressed == true)  direction = "down"; 
            else if (keyHandler.leftPressed == true)  direction = "left"; 
            else if (keyHandler.rightPressed == true)  direction = "right"; 
            
            // CHECK TILE COLLISION
            collisionOn = false;
            gamePanel.collisionChecker.checkTile(this);

            // CHECK OBJECT COLLISION
            int objIndex = gamePanel.collisionChecker.checkObject(this, true);
            pickUpObject(objIndex);
            
            // CHECK NPC ? MONSTER COLLISION
            int npcIndex =  gamePanel.collisionChecker.checkEntity(this, gamePanel.npc);
            interactNPC(npcIndex);
            
            // CHECK MONSTER COLLISION
            int monsterIndex =  gamePanel.collisionChecker.checkEntity(this, gamePanel.monster);
            contactMonster(monsterIndex);
            
            // CHECKING EVENT
            gamePanel.eventHandler.checkEvent();
            gamePanel.keyHandler.enterPressed = false;


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
    	} else {
            standCounter++;
            if (standCounter == 20) {
                spriteNum = 1;
                standCounter = 0;
            }
        }

        if (isInvincible) {
            invincibleCounter++;
            if (invincibleCounter > invincibleDuration) {
                isInvincible = false;
                invincibleCounter = 0;
            }
        }
    }

    private void contactMonster(int monsterIndex) {
        
        if (monsterIndex == 999) return;
        if (!isInvincible) {
            life -= 1;
            isInvincible = true;
        }

    }

    // PICK A OBJECT
    public void pickUpObject(int objIndex) {
        if (objIndex != 999) {
            String objName = gamePanel.obj[objIndex].name;
            switch (objName) {
                case "Key" -> {
                    gamePanel.gameUi.showMessage("You got a Key !");
                    gamePanel.playSoundEffect(1);
                    gamePanel.obj[objIndex] = null;
                    hasKeys++;
                }
                case "Door" -> {
                    if (hasKeys > 0) {
                        gamePanel.playSoundEffect(3);
                        gamePanel.gameUi.showMessage("You opened a door !");
                        gamePanel.obj[objIndex] = null;
                        hasKeys--;
                    } else {
                        gamePanel.gameUi.showMessage("You dont have a Key !");
                    }
                }
                case "Boots" -> {
                    gamePanel.gameUi.showMessage("Speed up !");
                    gamePanel.playSoundEffect(2);
                    gamePanel.obj[objIndex] = null;
                    speed += 1;
                }
                case "Chest" -> {
                    gamePanel.gameUi.showMessage("You dont have a Key !");
                    gamePanel.stopMusic();
                    gamePanel.playSoundEffect(4);
                    gamePanel.gameUi.gameFinished =  true;
                }
                case "Potion" -> {
                    gamePanel.gameUi.showMessage("You got health!");
                    gamePanel.obj[objIndex] = null;
                    gamePanel.player.life = 6;
                }

            }
        }
    }

    // INTERACTION
    public void interactNPC(int i) {
        if (i != 999) {
            if (keyHandler.enterPressed) {
                    
                gamePanel.gameState = gamePanel.DIALOUGE_STATE;
                gamePanel.npc[i].speak();
            }
        }
    }

    // RENDER PLAYER
    @Override
    public void draw(Graphics2D g2) {
        BufferedImage playerImage = null;
        switch (direction) {
            case "up" -> {
                if (spriteNum == 1) playerImage = up1;
                if (spriteNum == 2) playerImage = up2;
            }
            case "down" -> {
                if (spriteNum == 1) playerImage = down1;
                if (spriteNum == 2) playerImage = down2;
            }
            case "left" -> {
                if (spriteNum == 1) playerImage = left1;
                if (spriteNum == 2) playerImage = left2;
            }
            case "right" -> {
                if (spriteNum == 1) { playerImage = right1; }
                if (spriteNum == 2) { playerImage = right2; }
            }
        }

        float alpha = 1f;
        if (isInvincible) {
            alpha =  0.3f;
        }
        if (invincibleCounter == 40 || invincibleCounter == 90 || invincibleCounter == 100) {
            alpha =  1f;
        } else if (invincibleCounter == 65 || invincibleCounter == 70) {
            alpha =  0.3f;
        }
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha)); 
        g2.drawImage(playerImage, screenX, screenY, GamePanel.TILE_SIZE, GamePanel.TILE_SIZE, null);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }
}
