package main;

public class EventHandler {
    
    GamePanel gamePanel;
    EventRect eventRect[][];
    
    int previousEventX, previousEventY;
    boolean canEventOccur = false;

    public EventHandler(GamePanel gamePanel) {
        this.gamePanel = gamePanel;

        eventRect = new EventRect[GamePanel.maxWorldCol][GamePanel.maxWorldRow];
        int row = 0;
        int col = 0;
        while (col < GamePanel.maxWorldCol && row < GamePanel.maxWorldRow) {
            
            
            eventRect[col][row] = new EventRect();
            eventRect[col][row].x = 23;
            eventRect[col][row].y = 23;
            eventRect[col][row].width = 2;
            eventRect[col][row].height = 2;
            eventRect[col][row].eventRectDefaultX = eventRect[col][row].x;
            eventRect[col][row].eventRectDefaultY = eventRect[col][row].y;

            col++;
            if (col == GamePanel.maxWorldCol) {
                col = 0;
                row++;
            }
        }
    }

    public void checkEvent() {

        // CHECK FROM HAPPENING EVENT REPEAT(1 TILE DISTANCE)
        int xDistance = Math.abs(gamePanel.player.worldX - previousEventX);
        int yDistance = Math.abs(gamePanel.player.worldY - previousEventY);
        int distance = Math.max(xDistance, yDistance);
        if (distance > GamePanel.tileSize) {
            canEventOccur = true;
        }

        if (canEventOccur) {
            
            if (hit(27,16,"right")) damagePit(27, 16, gamePanel.dialougeState);
            if (hit(23,19,"any")) damagePit(23, 19, gamePanel.dialougeState);
            if (hit(23,12,"up")) healingPool(gamePanel.dialougeState);
        }
    }

    private void damagePit(int col, int row, int gameState) {
        
        gamePanel.gameState = gameState;
        gamePanel.ui.currentDialouge = "You fall into a pit !";
        gamePanel.player.life -= 1;
        // eventRect[col][row].eventDone = true;
        canEventOccur = false;
    }

    public boolean hit(int col, int row, String reqDirection) {

        boolean hit = false;

        gamePanel.player.solidArea.x += gamePanel.player.worldX; 
        gamePanel.player.solidArea.y += gamePanel.player.worldY; 
        eventRect[col][row].x += (col * GamePanel.tileSize);
        eventRect[col][row].y += (row * GamePanel.tileSize);

        if (gamePanel.player.solidArea.intersects(eventRect[col][row]) && !eventRect[col][row].eventDone) {
            if (gamePanel.player.direction.contentEquals(reqDirection) || reqDirection.contentEquals("any")) {
                hit = true;

                previousEventX = gamePanel.player.worldX;
                previousEventY = gamePanel.player.worldY;
            }
        }

        // RESET SOLID AREAS
        gamePanel.player.solidArea.x = gamePanel.player.solidAreaDefaultX;
        gamePanel.player.solidArea.y = gamePanel.player.solidAreaDefaultY;

        eventRect[col][row].x = eventRect[col][row].eventRectDefaultX;
        eventRect[col][row].y = eventRect[col][row].eventRectDefaultY;

        return hit;
    }

    public void healingPool(int gameState) {
        
        if (gamePanel.keyHandler.enterPressed) {
            gamePanel.gameState = gameState;
            gamePanel.ui.currentDialouge = "You drink the water \nYour life has been recovered";
            gamePanel.player.life = gamePanel.player.maxLife;
        }
    }
}

