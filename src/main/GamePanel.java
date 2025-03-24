//PACKAGE DECLARATION
package main;

//STANDARD LIBRARY CLASSES
import entity.Entity;
import entity.Player;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import javax.swing.JPanel;
import tile.TileManager;

// GAMEPANEL CONSTRUCTOR
public class GamePanel extends JPanel implements Runnable {

    // DEFAULTS
    // SCREEN SETTINGS
    private static final int ORIGINAL_TILE_SIZE = 16; // 16*16
    private static final int SCALE = 3;
    public static final int MAX_SCREEN_COL = 16;
    public static final int MAX_SCREEN_ROW = 12;

    // CALCULATING SIZES
    public static final int TILE_SIZE = ORIGINAL_TILE_SIZE * SCALE;
    public static final int SCREEN_WIDTH = TILE_SIZE * MAX_SCREEN_COL;
    public static final int SCREEN_HEIGHT = TILE_SIZE * MAX_SCREEN_ROW;
    public static final Color GAME_COLOR = Color.black;

    // FPS
    protected static final int FPS = 60;

    // WORLD SETTINGS
    public static final int MAX_WORLD_COL = 50;
    public static final int MAX_WORLD_ROW = 50;
    public static final int WORLD_HEIGHT = TILE_SIZE * MAX_WORLD_COL;
    public static final int WORLD_WIDTH = TILE_SIZE * MAX_WORLD_ROW;

    // OBJECTS
    public static final int MAX_OBJECTS = 10;
    public static final int MAX_NPC = 10;
    public static final int MAX_MONSTER = 20;

    // MANAGER
    final TileManager tileManager = new TileManager(this);
    public KeyHandler keyHandler = new KeyHandler(this);
    Thread gameThread;
    private final Sound music = new Sound();
    private final Sound soundEffect = new Sound();
    public CollisionChecker collisionChecker = new CollisionChecker(this);
    public AssetSetter assetSetter = new AssetSetter(this);
    public UI gameUi = new UI(this);
    public EventHandler eventHandler = new EventHandler(this);

    // ENTITY AND OBJECT
    public Player player = new Player(this, keyHandler);
    public Entity[] obj = new Entity[MAX_OBJECTS];
    public Entity[] npc = new Entity[MAX_NPC];
    public Entity[] monster = new Entity[MAX_MONSTER];
    private final ArrayList<Entity> entityList = new ArrayList<>();

    // GAME STATE
    public int gameState;
    public final static int TITLE_STATE = 0;
    public final static int PLAY_STATE = 1;
    public final static int PAUSE_STATE = 2;
    public final static int DIALOUGE_STATE = 3;

    // GAME PANEL INIT
    public GamePanel() {
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(GAME_COLOR);
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
        gameState = TITLE_STATE;
    }

    // RUN METHOD - Is automatically invoked ok calling a thread
    @Override
    public void run() {

        // GAME LOOP SETTINGS
        double nanoSec = 1 * Math.pow(10, 9);
        double drawInterval = nanoSec / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        @SuppressWarnings("unused")
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
                drawCount = 0;
                timer = 0;
            }
        }
    }

    // UPDATE ENTITYS
    public void update() {

        if (gameState == PLAY_STATE) {

            // PLAYER UPDATE
            player.update();

            // UPDATE NPC
            for (Entity npc1 : npc) {
                if (npc1 != null) {
                    npc1.update();
                }
            }

            // UPDATE MONSTERS
            for (Entity monster1 : monster) {
                if (monster1 != null) {
                    monster1.update();
                }
            }
        }

        if (gameState == PAUSE_STATE) {
            // NOTHING
        }
    }

    // UPDATE SCREEN
    @SuppressWarnings("UnnecessaryContinue")
    @Override
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
        if (gameState == TITLE_STATE) {
            gameUi.draw(g2);
        } else {

            // RENDER
            // TILES
            tileManager.draw(g2);

            
            entityList.add(player);
            // ADD ENTITIES TO THE LIST
            for (Entity npc1 : npc) {
                if (npc1 != null) {
                    entityList.add(npc1);
                }
            }

            for (Entity obj1 : obj) {
                if (obj1 != null) {
                    entityList.add(obj1);
                }
            }

            for (Entity monster1 : monster) {
                if (monster1 != null) {
                    entityList.add(monster1);
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
            gameUi.draw(g2);

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
