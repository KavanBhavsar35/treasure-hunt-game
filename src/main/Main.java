package main;

import javax.swing.ImageIcon;
//EXTENDED LIBRARY
import javax.swing.JFrame;

public class Main {

    private static final String TITLE = "2D Adventure";
    private static final boolean IS_RESIZABLE = false;

    // MAIN METHOD
    public static void main(String[] args) {

        // BASIC WINDOW SETTINGS
        JFrame window = new JFrame();
        // Load the image using ClassLoader
        ClassLoader classLoader = Main.class.getClassLoader();
        java.net.URL imageUrl = classLoader.getResource("player/boy_down_1.png");
        ImageIcon img = new ImageIcon(imageUrl);

        // Set the icon image for the window
        window.setIconImage(img.getImage());

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(IS_RESIZABLE);
        window.setTitle(TITLE);

        // GAMEPANEL INIT
        GamePanel gamePanel = new GamePanel();

        window.add(gamePanel);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        // GAMEPANEL SETUP
        gamePanel.setupGame();
        gamePanel.startGameThread();
    }

}