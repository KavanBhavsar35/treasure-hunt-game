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
        switch (gamePanel.gameState) {
            case GamePanel.TITLE_STATE -> {
                if (code == KeyEvent.VK_W) {
                    if (gamePanel.gameUi.commandNum == 0) gamePanel.gameUi.commandNum = 2;
                    else gamePanel.gameUi.commandNum--;
                }   if (code == KeyEvent.VK_S) {
                    if (gamePanel.gameUi.commandNum == 2) gamePanel.gameUi.commandNum = 0;
                    else gamePanel.gameUi.commandNum++;
                }   if (code == KeyEvent.VK_ENTER) {
                    switch (gamePanel.gameUi.commandNum) {
                        case 0:
                            gamePanel.gameState = GamePanel.PLAY_STATE;
                            gamePanel.playMusic(0);
                            break;
                            // add later
                        case 1:
                            break;
                        case 2:
                            System.exit(0);
                        default:
                            break;
                    }
                }
            }
            case GamePanel.PLAY_STATE -> {
                if (code == KeyEvent.VK_W) upPressed = true;
                if (code == KeyEvent.VK_S) downPressed = true;
                if (code == KeyEvent.VK_A) leftPressed = true;
                if (code == KeyEvent.VK_D) rightPressed = true;
                if (code == KeyEvent.VK_P) gamePanel.gameState = GamePanel.PAUSE_STATE;
                if (code == KeyEvent.VK_ENTER) enterPressed = true;
                if (code == KeyEvent.VK_T) {
                    checkDrawTime = !checkDrawTime;
                }
            }
            case GamePanel.PAUSE_STATE -> {
                // PAUSE STATE
                
                if (code == KeyEvent.VK_P) gamePanel.gameState = GamePanel.PLAY_STATE;
            }
            case GamePanel.DIALOUGE_STATE -> {
                // DIALOUGE STATE
                
                if (code == KeyEvent.VK_ENTER) gamePanel.gameState = GamePanel.PLAY_STATE;
            }
            default -> {
            }
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