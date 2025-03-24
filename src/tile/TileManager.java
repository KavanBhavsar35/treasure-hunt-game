//PACKAGE DECLARATION
package tile;

//STANDARD LIBRARY CLASSES
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

//EXTENDED LIBRARY CLASSES
import javax.imageio.ImageIO;

import main.GamePanel;

public class TileManager {

    // CONSTANTS
    int maxTiles = 50;
    String mapFileName = "worldV2.txt";
    GamePanel gamePanel;
    public Tile[] tile;
    public int mapTileNumber[][];

    public TileManager(GamePanel gamePanel) {

        // INITIATION
        this.gamePanel = gamePanel;
        tile = new Tile[maxTiles];
        mapTileNumber = new int[GamePanel.maxWorldCol][GamePanel.maxWorldRow];

        // LOADING RESPECTIVE TILES ACCORDING TO MAP
        this.getTileImage();
        this.loadMap("/maps/" + mapFileName);
    }

    // LOAD MAP
    public void loadMap(String filePath) {
        try {
            InputStream inputStream = getClass().getResourceAsStream(filePath);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            int col = 0;
            int row = 0;

            while (col < GamePanel.maxWorldCol && row < GamePanel.maxWorldRow) {

                String line = bufferedReader.readLine();

                while (col < GamePanel.maxWorldCol) {
                    String[] numbers = line.split(" ");

                    int num = Integer.parseInt(numbers[col]);
                    mapTileNumber[col][row] = num;

                    col++;
                }
                if (col == GamePanel.maxWorldCol) {
                    col = 0;
                    row++;
                }
            }

        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    // LOAD IMAGES
    public void getTileImage() {

        // PLACEHOLDER
        setup(0, "grass00.png", false);
        setup(1, "grass00.png", false);
        setup(2, "grass00.png", false);
        setup(3, "grass00.png", false);
        setup(4, "grass00.png", false);
        setup(5, "grass00.png", false);
        setup(6, "grass00.png", false);
        setup(7, "grass00.png", false);
        setup(8, "grass00.png", false);
        setup(9, "grass00.png", false);

        // ACTUAL IMAGES
        setup(10, "grass00.png", false);
        setup(11, "grass01.png", false);
        setup(12, "water00.png", true);
        setup(13, "water01.png", true);
        setup(14, "water02.png", true);
        setup(15, "water03.png", true);
        setup(16, "water04.png", true);
        setup(17, "water05.png", true);
        setup(18, "water06.png", true);
        setup(19, "water07.png", true);
        setup(20, "water08.png", true);
        setup(21, "water09.png", true);
        setup(22, "water10.png", true);
        setup(23, "water11.png", true);
        setup(24, "water12.png", true);
        setup(25, "water13.png", true);
        setup(26, "road00.png", false);
        setup(27, "road01.png", false);
        setup(28, "road02.png", false);
        setup(29, "road03.png", false);
        setup(30, "road04.png", false);
        setup(31, "road05.png", false);
        setup(32, "road06.png", false);
        setup(33, "road07.png", false);
        setup(34, "road08.png", false);
        setup(35, "road09.png", false);
        setup(36, "road10.png", false);
        setup(37, "road11.png", false);
        setup(38, "road12.png", false);
        setup(39, "earth.png", false);
        setup(40, "wall.png", true);
        setup(41, "tree.png", true); 


    }

    // SETUP
    public void setup(int index, String tileImageName, boolean collision) {
        try {
            tile[index] = new Tile(ImageIO.read(getClass().getResourceAsStream("/tiles/" + tileImageName)), collision);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // CONDITIONAL RENDERING FOR PLAYER VIEW GRID
    public void draw(Graphics2D g2) {

        int worldCol = 0;
        int worldRow = 0;

        while (worldCol < GamePanel.maxWorldCol && worldRow < GamePanel.maxWorldRow) {

            int tileNum = mapTileNumber[worldCol][worldRow];

            int worldX = worldCol * GamePanel.tileSize;
            int worldY = worldRow * GamePanel.tileSize;
            int screenX = worldX - gamePanel.player.worldX + gamePanel.player.screenX;
            int screenY = worldY - gamePanel.player.worldY + gamePanel.player.screenY;

            if (worldX + GamePanel.tileSize > gamePanel.player.worldX - gamePanel.player.screenX &&
                    worldX - GamePanel.tileSize < gamePanel.player.worldX + gamePanel.player.screenX &&
                    worldY + GamePanel.tileSize > gamePanel.player.worldY - gamePanel.player.screenY &&
                    worldY - GamePanel.tileSize < gamePanel.player.worldY + gamePanel.player.screenY) {

                g2.drawImage(tile[tileNum].image, screenX, screenY, GamePanel.tileSize, GamePanel.tileSize, null);
            }

            worldCol++;

            if (worldCol == GamePanel.maxWorldCol) {
                worldCol = 0;
                worldRow++;
            }
        }
    }
}
