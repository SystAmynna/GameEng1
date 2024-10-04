package main.tile;

import main.GamePanel;
import main.Room;
import main.camera.Camera;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class TileManager {

    private final GamePanel GP;
    private Room room;
    private BufferedImage[] sprites;
    private Tile[][] map;
    private ArrayList<Tile> tilesToUpdate = new ArrayList<Tile>();

    // CONSTRUCTEUR
    public TileManager(GamePanel GP) {
        this.GP = GP;
        room = GP.currentRoom;
        generateTiles();

    }

    // GETTER
    public Tile[][] getMap() {
        return map;
    }

    // UPDATE
    public void update() {
        if (this.room != GP.currentRoom) {
            this.room = GP.currentRoom;
            this.map = room.map;
        }

    }

    // DRAW
    public void render(Graphics2D g2, Camera CAM) {
        int startCol = Math.max((int) (CAM.getX() / GP.TILE_SIZE - GP.SCREEN_WIDTH / 2 / GP.TILE_SIZE), 0);
        int startRow = Math.max((int) (CAM.getY() / GP.TILE_SIZE - GP.SCREEN_HEIGHT / 2 / GP.TILE_SIZE), 0);
        int endCol = Math.min((int) ((CAM.getX() + GP.SCREEN_WIDTH / 2) / GP.TILE_SIZE)+1, GP.MAX_WORLD_COL);
        int endRow = Math.min((int) ((CAM.getY() + GP.SCREEN_HEIGHT / 2) / GP.TILE_SIZE)+1, GP.MAX_WORLD_ROW);

        for (int col = startCol; col < endCol; col++) {
            for (int row = startRow; row < endRow; row++) {
                int worldX = col * GP.TILE_SIZE;
                int worldY = row * GP.TILE_SIZE;
                int screenX = worldX - CAM.getX() + CAM.SCREENX;
                int screenY = worldY - CAM.getY() + CAM.SCREENY;

                if (worldX + GP.TILE_SIZE > CAM.getX() - GP.SCREEN_WIDTH &&
                        worldX - GP.TILE_SIZE < CAM.getX() + GP.SCREEN_WIDTH &&
                        worldY + GP.TILE_SIZE > CAM.getY() - GP.SCREEN_HEIGHT &&
                        worldY - GP.TILE_SIZE < CAM.getY() + GP.SCREEN_HEIGHT) {
                    map[col][row].draw(g2, screenX, screenY);
                }
            }
        }
    }


    // GENERATION DES TILES
    private void generateTiles() {
        // initialise la liste des tiles
        sprites = new BufferedImage[3];
        // récupère les sprites
        try {
            sprites[0] = ImageIO.read(Objects.requireNonNull(this.getClass().getResourceAsStream("/sprites/tiles/defaultSpriteB.png")));
            sprites[1] = ImageIO.read(Objects.requireNonNull(this.getClass().getResourceAsStream("/sprites/tiles/defaultSpriteA.png")));
            sprites[2] = ImageIO.read(Objects.requireNonNull(this.getClass().getResourceAsStream("/sprites/tiles/collideTile.png")));
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    // FONCTIONS
    public Tile[][] genDefaultMap() {
        Tile [][] map = new Tile[GamePanel.MAX_WORLD_COL][GamePanel.MAX_WORLD_ROW];
        boolean AB = false;
        for (int col = 0; col < GamePanel.MAX_WORLD_COL; col++) {
            AB = !AB;
            for (int row = 0; row < GamePanel.MAX_WORLD_ROW; row++) {
                map[col][row] = new Tile(GP, sprites[(AB) ? 0 : 1], col, row);
                AB = !AB;
            }
        }
        map[2][1] = new Tile(GP, sprites[2], 2, 1, true);
        map[2][2] = new Tile(GP, sprites[2], 2, 2, true);
        map[3][2] = new Tile(GP, sprites[2], 3, 2, true);

        return map;
    }

}
