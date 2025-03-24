//PACKAGE DECLARATION
package main;

//STANDARD LIBRARY CLASSES
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

//EXTENDED LIBRARY CLASSES
import javax.swing.JPanel;

import entity.Entity;
//PACKAGE CLASSES
import entity.Player;
import tile.TileManager;

// GAMEPANEL CONSTRUCTOR
public class GamePanel extends JPanel implements Runnable {

    // DEFAULTS
    // SCREEN SETTINGS
    private static final int originalTileSize = 16; // 16*16
    private static final int scale = 3;
    public static final int maxScreenCol = 16;
    public static final int maxScreenRow = 12;

    // CALCULATING SIZES
    public static final int tileSize = originalTileSize * scale;
    public static final int screenWidth = tileSize * maxScreenCol;
    public static final int screenHeight = tileSize * maxScreenRow;
    public static final Color gameColor = Color.black;

    // FPS
    protected static final int fps = 60;

    // WORLD SETTINGS
    public static final int maxWorldCol = 50;
    public static final int maxWorldRow = 50;
    public static final int worldHeight = tileSize * maxWorldCol;
    public static final int worldWidth = tileSize * maxWorldRow;

    // OBJECTS
    public static final int maxObjects = 10;
    public static final int maxNPC = 10;
    public static final int maxMonster = 20;

    // MANAGER
    TileManager tileManager = new TileManager(this);
    public KeyHandler keyHandler = new KeyHandler(this);
    Thread gameThread;
    Sound music = new Sound();
    Sound soundEffect = new Sound();
    public CollisionChecker collisionChecker = new CollisionChecker(this);
    public AssetSetter assetSetter = new AssetSetter(this);
    public UI ui = new UI(this);
    public EventHandler eventHandler = new EventHandler(this);

    // ENTITY AND OBJECT
    public Player player = new Player(this, keyHandler);
    public Entity[] obj = new Entity[maxObjects];
    public Entity[] npc = new Entity[maxNPC];
    public Entity[] monster = new Entity[maxMonster];
    ArrayList<Entity> entityList = new ArrayList<>();

    // GAME STATE
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int dialougeState = 3;

    // GAME PANEL INIT
    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(gameColor);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);
    }

    // MAIN GAME THREAD
    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    // SETUP GAME
    public void setupGame() {
        assetSetter.setObject();
        assetSetter.setNPC();
        assetSetter.setMonster();
        // playMusic(0);
        gameState = titleState;
    }

    // RUN METHOD - Is automatically invoked ok calling a thread
    @Override
    public void run() {

        // GAME LOOP SETTINGS
        double nanoSec = 1 * Math.pow(10, 9);
        double drawInterval = nanoSec / fps;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        long drawCount = 0;

        while (gameThread != null) {

            // FIND DIFFERENCE SINCE LAST CALL
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            // UPDATE TIME IF DELTA HAS PASS
            if (delta >= 1) {
                update();
                repaint();
                delta--;
                drawCount++;
            }

            // RESET VARIABLES
            if (timer >= nanoSec) {
                System.out.println("FPS: " + drawCount);
                System.out.println("Direction: " + player.direction);

                drawCount = 0;
                timer = 0;
            }
        }
    }

    // UPDATE ENTITYS
    public void update() {

        if (gameState == playState) {

            // PLAYER UPDATE
            player.update();

            // UPDATE NPC
            for (int i = 0; i < npc.length; i++) {

                if (npc[i] != null) {
                    npc[i].update();
                }
            }

            // UPDATE MONSTERS
            for (int i = 0; i < monster.length; i++) {

                if (monster[i] != null) {
                    monster[i].update();
                }
            }
        }

        if (gameState == pauseState) {
            // NOTHING
        }
    }

    // UPDATE SCREEN
    public void paintComponent(Graphics g) {

        super.paintComponent(g);

        // GRAPHIC 2D - USED TO DRAW TWO DIMENSION FIGURES
        Graphics2D g2 = (Graphics2D) g;

        // DEBUG
        long drawStart = 0;
        if (keyHandler.checkDrawTime) {
            drawStart = System.nanoTime();
        }

        // TITLE SCREEN
        if (gameState == titleState) {
            ui.draw(g2);
        } else {

            // RENDER
            // TILES
            tileManager.draw(g2);

            
            entityList.add(player);
            // ADD ENTITIES TO THE LIST
            for (int i = 0; i < npc.length; i++) {
                if (npc[i] != null) {
                    entityList.add(npc[i]);
                }
            }

            for (int i = 0; i < obj.length; i++) {
                if (obj[i] != null) {
                    entityList.add(obj[i]);
                }
            }

            for (int i = 0; i < monster.length; i++) {
                if (monster[i] != null) {
                    entityList.add(monster[i]);
                }
            }

            // SORT
            for (int i = 0; i < entityList.size() - 1; i++) {
                for (int j = 0; j < entityList.size() - i - 1; j++) {
                    Entity e1 = entityList.get(j);
                    Entity e2 = entityList.get(j + 1);

                    if (e1 == null && e2 == null) {
                        continue; // Skip if both entities are null
                    } else if (e1 == null) {
                        // Move null entity to the end
                        entityList.add(entityList.remove(j));
                    } else if (e2 == null) {
                        // Move non-null entity before null entity
                        entityList.add(j, entityList.remove(j + 1));
                    } else if (e1.worldY > e2.worldY) {
                        // Swap entities if e1.worldY is greater than e2.worldY
                        entityList.set(j, e2);
                        entityList.set(j + 1, e1);
                    } else if (e1.worldY == e2.worldY && e1.worldX > e2.worldX) {
                        // If worldY is the same, sort by worldX
                        entityList.set(j, e2);
                        entityList.set(j + 1, e1);
                    }
                }
            }

            // DRAW ENTITIES
            for (int i = 0; i < entityList.size(); i++) {
                entityList.get(i).draw(g2);
            }
            entityList.clear();

            // PLAYER
            player.draw(g2);

            // USER INTERFACE
            ui.draw(g2);

            // DEBUG
            if (keyHandler.checkDrawTime) {
                long drawEnd = System.nanoTime();
                long passed = drawEnd - drawStart;
                g2.setColor(Color.white);
                g2.drawString("Draw time: " + passed, 10, 400);
                System.out.println("Draw time: " + passed);
            }

            // FREE RESOURCES
            g2.dispose();
        }
    }

    // LOOPED BG MUSIC
    public void playMusic(int i) {

        music.setFile(i);
        music.play();
        music.loop();
    }

    public void stopMusic() {

        music.stop();
    }

    // SOUND EFFECT
    public void playSoundEffect(int i) {

        soundEffect.setFile(i);
        soundEffect.play();
    }
}
