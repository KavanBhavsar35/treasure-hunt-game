package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;

import javax.imageio.ImageIO;

import entity.Entity;
import object.ObjHeart;

public class UI {

    // INITIATION
    GamePanel gamePanel;
    Graphics2D g2;
    Font maruMonica, maruMonica_40, maruMonica_80b, arial_80B;
    BufferedImage heartFull, heartHalf, heartBlank;
    BufferedImage keyImage;
    public boolean messageOn = false;
    public String message = "";
    int messageCounter = 0;
    int messageDuration = 2; // sec
    public boolean gameFinished = false;
    public int commandNum = 0;

    public String currentDialouge = "";

    double playTime;
    DecimalFormat decimalFormat = new DecimalFormat("#0.00");

    public UI(GamePanel gamePanel) {

        // UI SETTINGS
        this.gamePanel = gamePanel;

        try {
            InputStream inputStream = getClass().getResourceAsStream("/font/maruMonica.ttf");
            maruMonica = Font.createFont(Font.TRUETYPE_FONT, inputStream);
            maruMonica_40 = maruMonica.deriveFont(Font.PLAIN, 45);
            maruMonica_80b = maruMonica.deriveFont(Font.BOLD, 120);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            keyImage = ImageIO.read(getClass().getResourceAsStream("/objects/key.png" ));
        } catch (IOException e) {
            System.out.println("Error: " + e);
        }
        ;

        // CREATING HUD OBJECT
        Entity heart = new ObjHeart(gamePanel);
        heartFull = heart.image;
        heartHalf = heart.image2;
        heartBlank = heart.image3;
    }

    // ACCESS FUNCTIONS
    public void showMessage(String text) {
        message = text;
        messageOn = true;
    }

    // RENDERING MESSAGES
    public void draw(Graphics2D g2) {

        this.g2 = g2;

        g2.setFont(maruMonica);
        g2.setColor(Color.white);

        if (gamePanel.gameState == gamePanel.titleState) {
            this.drawTitleScreen();
        }
        if (gamePanel.gameState == gamePanel.playState) {
            this.drawPlayerLife();
            if (gameFinished) {
                g2.setFont(maruMonica_40);
                g2.setColor(Color.white);

                String text;
                int textLength;
                int x;
                int y;

                // GAME END BANNER
                text = "You Found the treasure !!";
                textLength = UtilityTool.textLength(g2, text);
                x = GamePanel.screenWidth / 2 - textLength / 2;
                y = GamePanel.screenHeight / 2 - (GamePanel.tileSize * 3);
                g2.drawString(text, x, y);

                text = "Your Time is " + decimalFormat.format(playTime) + "!!";
                textLength = UtilityTool.textLength(g2, text);
                x = GamePanel.screenWidth / 2 - textLength / 2;
                y = GamePanel.screenHeight / 2 + (GamePanel.tileSize * 3);
                g2.drawString(text, x, y);

                g2.setFont(maruMonica_80b);
                g2.setColor(Color.YELLOW);

                text = "Congratulations";
                textLength = UtilityTool.textLength(g2, text);
                x = GamePanel.screenWidth / 2 - textLength / 2;
                y = GamePanel.screenHeight / 2 + (GamePanel.tileSize * 2);
                g2.drawString(text, x, y);

                gamePanel.gameThread = null;
            } else {

                // UI COMPONENTS
                g2.setFont(maruMonica_40);
                g2.setColor(Color.white);
                g2.drawImage(keyImage, GamePanel.tileSize / 2, 120 - GamePanel.tileSize + 5, GamePanel.tileSize,
                        GamePanel.tileSize, null);
                g2.drawString(" X " + gamePanel.player.hasKeys, 70, 120);

                // TIME
                playTime += (double) 1 / GamePanel.fps;
                g2.drawString("Time: " + decimalFormat.format(playTime),
                        GamePanel.tileSize * (GamePanel.maxScreenRow - 1),
                        GamePanel.tileSize * 1 + GamePanel.tileSize / 4);

                // MESSAGE
                if (messageOn) {
                    g2.setFont(g2.getFont().deriveFont(30f));
                    g2.drawString(message, GamePanel.tileSize * (GamePanel.maxScreenRow - 1) + GamePanel.tileSize / 2,
                            GamePanel.tileSize * (GamePanel.maxScreenCol - 5) + GamePanel.tileSize / 2);

                    messageCounter++;

                    if (messageCounter > messageDuration * GamePanel.fps) {
                        messageCounter = 0;
                        messageOn = false;
                    }
                }
            }

        }
        if (gamePanel.gameState == gamePanel.pauseState) {
            this.drawPlayerLife();
            this.drawPauseScreen();
        }
        if (gamePanel.gameState == gamePanel.dialougeState) {
            this.drawPlayerLife();
            this.drawDialougeState();
        }
    }

    private void drawPlayerLife() {

        int x = GamePanel.tileSize / 2;
        int y = GamePanel.tileSize / 2;
        int i = 0;

        // DRAW MAX LIFE
        while (i < gamePanel.player.maxLife / 2) {
            g2.drawImage(heartBlank, x, y, null);
            i++;
            x += GamePanel.tileSize;
        }

        // RESET
        x = GamePanel.tileSize / 2;
        y = GamePanel.tileSize / 2;
        i = 0;

        // DRAW CURRENT LIFE
        while (i < gamePanel.player.life) {
            g2.drawImage(heartHalf, x, y, null);
            i++;
            if (i < gamePanel.player.life) {
                g2.drawImage(heartFull, x, y, null);
            }
            i++;
            x += GamePanel.tileSize;
        }
    }

    private void drawTitleScreen() {

        int offset = 2;

        // TITLE NAME
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 96f));
        String text = "Blue Boy Adventure";
        int x = UtilityTool.getXForCenteredText(g2, text);
        int y = GamePanel.tileSize * 3;

        // SHADOW
        g2.setColor(Color.gray);
        g2.drawString(text, x + offset, y + offset);

        // TITLE
        g2.setColor(Color.white);
        g2.drawString(text, x, y);

        // BLUE BOY IMAGE
        x = GamePanel.screenWidth / 2 - (GamePanel.tileSize * 2 / 2);
        y += GamePanel.tileSize * 2;
        g2.drawImage(gamePanel.player.down1, x, y, GamePanel.tileSize * 2, GamePanel.tileSize * 2, null);

        // MENU
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 48f));

        text = "NEW GAME";
        x = UtilityTool.getXForCenteredText(g2, text);
        y += GamePanel.tileSize * 3.5;
        g2.drawString(text, x, y);
        if (commandNum == 0) {
            g2.drawString(">", x - GamePanel.tileSize, y);
        }

        text = "LOAD GAME";
        x = UtilityTool.getXForCenteredText(g2, text);
        y += GamePanel.tileSize;
        g2.drawString(text, x, y);
        if (commandNum == 1) {
            g2.drawString(">", x - GamePanel.tileSize, y);
        }

        text = "QUIT";
        x = UtilityTool.getXForCenteredText(g2, text);
        y += GamePanel.tileSize;
        g2.drawString(text, x, y);
        if (commandNum == 2) {
            g2.drawString(">", x - GamePanel.tileSize, y);
        }

    }

    private void drawPauseScreen() {

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 80f));
        String text = "Paused";
        int x;
        x = UtilityTool.getXForCenteredText(g2, text);
        int y = GamePanel.screenHeight / 2;

        g2.drawString(text, x, y);

    }

    private void drawDialougeState() {

        // WINDOW
        int x = GamePanel.tileSize * 2;
        int y = GamePanel.tileSize / 2;
        int width = GamePanel.screenWidth - (GamePanel.tileSize * 4);
        int height = GamePanel.tileSize * 5;

        drawSubWindow(x, y, width, height);

        x += GamePanel.tileSize;
        y += GamePanel.tileSize;
        g2.setFont(g2.getFont().deriveFont(30f));

        for (String line : currentDialouge.split("\n")) {

            g2.drawString(line, x, y);
            y += 40;
        }

    }

    // SUBWINDOW
    public void drawSubWindow(int x, int y, int width, int height) {

        Color color = new Color(0, 0, 0, 200);
        g2.setColor(color);
        g2.fillRoundRect(x, y, width, height, 35, 35);

        color = new Color(255, 255, 255, 230);
        g2.setColor(color);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25);
    }
}