package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler  implements KeyListener{
    
    GamePanel gamePanel;
    public KeyHandler(GamePanel gamePanel){
        this.gamePanel = gamePanel;
    }

    // KEY PRESES
    public boolean upPressed, downPressed, leftPressed, rightPressed, enterPressed;

    // DEBUG
    boolean checkDrawTime = false;

    // Empty override to use abstract methods
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        
        int code =  e.getKeyCode();

        // GAME STATE
        if (gamePanel.gameState == gamePanel.titleState) {
               
            if (code == KeyEvent.VK_W) {
                if (gamePanel.ui.commandNum == 0) gamePanel.ui.commandNum = 2; 
                else gamePanel.ui.commandNum--;
            }
            if (code == KeyEvent.VK_S) {
                if (gamePanel.ui.commandNum == 2) gamePanel.ui.commandNum = 0; 
                else gamePanel.ui.commandNum++; 
            }      
            if (code == KeyEvent.VK_ENTER) {
                if (gamePanel.ui.commandNum == 0) {
                    gamePanel.gameState = gamePanel.playState;
                    gamePanel.playMusic(0);
                } else if (gamePanel.ui.commandNum == 1) {
                    // add later
                } else if (gamePanel.ui.commandNum == 2) {
                    System.exit(0);
                }
            }


        } else if (gamePanel.gameState == gamePanel.playState) {
               
            if (code == KeyEvent.VK_W) upPressed = true;
            if (code == KeyEvent.VK_S) downPressed = true;
            if (code == KeyEvent.VK_A) leftPressed = true;
            if (code == KeyEvent.VK_D) rightPressed = true;
            if (code == KeyEvent.VK_P) gamePanel.gameState = gamePanel.pauseState;
            if (code == KeyEvent.VK_ENTER) enterPressed = true;
            
            if (code == KeyEvent.VK_T) {
                if (checkDrawTime) {
                    checkDrawTime = false;
                } else {
                    checkDrawTime = true;
                }
            }
        } else if (gamePanel.gameState ==gamePanel.pauseState) { // PAUSE STATE

            if (code == KeyEvent.VK_P) gamePanel.gameState = gamePanel.playState;      
        } else if (gamePanel.gameState ==gamePanel.dialougeState) { // DIALOUGE STATE
            
            if (code == KeyEvent.VK_ENTER) gamePanel.gameState = gamePanel.playState;      

        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_W) upPressed = false;
        if (code == KeyEvent.VK_S) downPressed = false;
        if (code == KeyEvent.VK_A) leftPressed = false;
        if (code == KeyEvent.VK_D) rightPressed = false;
    }   
}