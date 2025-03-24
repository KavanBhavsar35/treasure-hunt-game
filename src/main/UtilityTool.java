package main;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class UtilityTool {

    // SCALE IMAGE
    public static final BufferedImage scaleImage(BufferedImage original, int width, int height) {
        BufferedImage scaledImage = new BufferedImage(width, height, original.getType());
        Graphics2D g2 = scaledImage.createGraphics();
        g2.drawImage(original, 0, 0, width, height, null);
        g2.dispose();

        return scaledImage;
    }

    // GET TEXT LENGTH
    public static final int textLength(Graphics2D g2, String text) {

        return (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
    }

    // GET X FOR CENTER
    public static final int getXForCenteredText(Graphics2D g2, String text) {
        int x = GamePanel.screenWidth / 2 - UtilityTool.textLength(g2, text) / 2;
        return x;
    }

    public static final void dispImage(BufferedImage image) {
        ImageIcon icon = new ImageIcon(image);
        JFrame frame = new JFrame();
        frame.add(new JLabel(icon));
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
