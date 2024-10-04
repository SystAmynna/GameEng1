package main.tile;

import main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Tile {

    private final GamePanel GP; // GamePanel
    private BufferedImage sprite;
    private boolean collision;
    private int tileX, tileY;
    public Rectangle collideBox;

    protected Tile(GamePanel GP, int x, int y) {
        this.GP = GP;
        collision = false;
        sprite = null;
        tileX = x;
        tileY = y;
        collideBox = new Rectangle(x* GamePanel.TILE_SIZE, y* GamePanel.TILE_SIZE, GamePanel.TILE_SIZE, GamePanel.TILE_SIZE);
    }
    protected Tile(GamePanel GP, BufferedImage sprite, int x, int y) {
        this(GP, x, y);
        this.sprite = sprite;
    }
    protected Tile(GamePanel GP, BufferedImage sprite, int x, int y, boolean collision) {
        this(GP,sprite,x,y);
        this.collision = collision;
    }

    // GETTERS
    public boolean isCollision() {return collision;}
    public Rectangle getCollideBox() {return collideBox;}
    protected BufferedImage getSprite() {return sprite;}

    // DRAW
    protected void draw(Graphics2D g2, int sx, int sy) {
        g2.drawImage(sprite, sx, sy, GP.TILE_SIZE, GP.TILE_SIZE, null);
        if (GP.isDebug() && collision) {
            Font colF = new Font("Couriel", Font.ITALIC, 12);
            g2.setColor(Color.CYAN);
            g2.drawRect(sx, sy, GP.TILE_SIZE-1, GP.TILE_SIZE-1);
            g2.setFont(colF);
            g2.drawString("COLLISION", sx, sy+12);
        }
    }

}
