package main.entity;

import main.GamePanel;
import main.Room;
import main.camera.Camera;

import java.awt.*;
import java.util.ArrayList;

public abstract class Entity {

    // liste toute les entitées
    protected ArrayList<Entity> entities; // liste des entités
    private static int idCounter = 0; // compteur d'ID
    public final int ID = idCounter++; // ID de l'entité
    protected Room room; // salle de l'entité
    // todo : changement de la structture de la simulation
    public final GamePanel GP; // GamePanel
    public final Camera CAM; // Camera PANEL
    public final EntityType TYPE; // Type de l'entité
    protected int x; // X sur le monde
    protected int y; // Y sur le monde
    private int screenX; // X sur l'écran
    private int screenY; // Y sur l'écran
    protected Color debugColor = Color.CYAN; // couleur de debug
    protected Rectangle hitbox; // boite de collision
    private boolean loaded; // si l'entité est chargée par la caméra
    protected boolean goUp, goDown, goLeft, goRight; // direction de déplacement (actions)

    // CONSTRUCTEURS
    protected Entity(GamePanel gp, Room room, EntityType type, int x, int y) {
        GP = gp; // GamePanel
        CAM = GP.CAM; // Camera
        this.room = room; // salle
        entities = this.room.ENTITIES; // liste des entités
        this.TYPE = type; // type de l'entité
        entities.add(this); // ajout de l'entité à la liste
        this.x = x; // X sur le monde
        this.y = y; // Y sur le monde
    }

    // GETTERS
    public int getX() {return x;}
    public int getY() {return y;}
    public int getScreenX() {return screenX;}
    public int getScreenY() {return screenY;}
    public boolean isLoaded() {return loaded;}
    // SETTERS
    public void setX(int x) {this.x = x;}
    public void setY(int y) {this.y = y;}
    public void teleport(int x, int y) {this.x = x;this.y = y;}


    // UPDATE
    public void update() {} // UPDATE, pour être Overridé

    // DRAW
    public final void render(Graphics2D g2) { // rendu de l'entité
        loaded = false; // l'entité n'est pas chargée
        // position sur l'écran
        screenX = x - CAM.getX() + CAM.SCREENX; // set X écran
        screenY = y - CAM.getY() + CAM.SCREENY; // set Y écran
        // si l'entité est dans la zone de la caméra (et donc doit être dessinée)
        if (x + GP.TILE_SIZE > CAM.getX() - CAM.SCREENX && x - GP.TILE_SIZE < CAM.getX() + CAM.SCREENX &&
            y + GP.TILE_SIZE > CAM.getY() - CAM.SCREENY && y - GP.TILE_SIZE < CAM.getY() + CAM.SCREENY) {
            draw(g2); // dessin de l'entité
            loaded = true; // l'entité est chargée
        }
    }
    protected void draw(Graphics2D g2) {} // DRAW, pour être Overridé

    // CONTROLE
    public void control() {} // CONTROLE, pour être Overridé

    // UPDATE POSITION
    protected void updatePos(int speed) {
        // si aucune tentative de mouvement
        if(!goLeft && !goRight && !goUp && !goDown) return;
        // set des vitesses de chaque axe
        int xSpeed = 0, ySpeed = 0;
        // gestion axe x
        if (goLeft && !goRight) xSpeed = -speed;
        else if (goRight && !goLeft) xSpeed = speed;
        // gestion axe y
        if (goUp && !goDown) ySpeed = -speed;
        else if (goDown && !goUp) ySpeed = speed;

        // gestion des collisions avec une approche de "glissement"
        for (int i = 0; i < Math.abs(xSpeed); i++) { // pour chaque pixel de déplacement
            if (!isColliding(x + (xSpeed > 0 ? 1 : -1), y) && !isCollidingWithEntity(x + (xSpeed > 0 ? 1 : -1), y)) { // si pas de collision
                x += (xSpeed > 0 ? 1 : -1); // déplacement
            } else { // si collision
                break; // arrêt du déplacement
            }
        }
        for (int i = 0; i < Math.abs(ySpeed); i++) { // pour chaque pixel de déplacement
            if (!isColliding(x, y + (ySpeed > 0 ? 1 : -1)) && !isCollidingWithEntity(x, y + (ySpeed > 0 ? 1 : -1))) { // si pas de collision
                y += (ySpeed > 0 ? 1 : -1); // déplacement
            } else { // si collision
                break; // arrêt du déplacement
            }
        }

        // vérification après le mouvement
        while (isColliding(x, y)) { // tant que collision
            if (xSpeed != 0) { // si déplacement sur x
                x -= (xSpeed > 0 ? 1 : -1); // extraction de l'entité
            }
            if (ySpeed != 0) { // si déplacement sur y
                y -= (ySpeed > 0 ? 1 : -1); // extraction de l'entité
            }
        }
    }

    private boolean isColliding(int futureX, int futureY) { // vérification de collision

        Rectangle futureHitbox = new Rectangle(futureX + hitbox.x, futureY + hitbox.y, hitbox.width, hitbox.height); // hitbox future

        int startTileX = Math.max(0, futureHitbox.x / GamePanel.TILE_SIZE); // début de la hitbox sur x
        int endTileX = Math.min(room.map.length, (futureHitbox.x + futureHitbox.width) / GamePanel.TILE_SIZE + 1); // fin de la hitbox sur x
        int startTileY = Math.max(0, futureHitbox.y / GamePanel.TILE_SIZE); // début de la hitbox sur y
        int endTileY = Math.min(room.map[0].length, (futureHitbox.y + futureHitbox.height) / GamePanel.TILE_SIZE + 1); // fin de la hitbox sur y

        for (int i = startTileX; i < endTileX; i++) { // pour chaque tuile
            for (int j = startTileY; j < endTileY; j++) {
                if (room.map[i][j].isCollision()) { // si la tuile est une collision
                    // todo : importe la hitbox de la tuile: modifié Tile.java
                    Rectangle tileHitbox = new Rectangle(i * GamePanel.TILE_SIZE, j * GamePanel.TILE_SIZE, GamePanel.TILE_SIZE, GamePanel.TILE_SIZE);
                    if (futureHitbox.intersects(tileHitbox)) { // si collision
                        return true; // collision
                    }
                }
            }
        }
        return false; // pas de collision
    }

    private boolean isCollidingWithEntity(int futureX, int futureY) { // vérification de collision avec une entité

        Rectangle futureHitbox = new Rectangle(futureX + hitbox.x, futureY + hitbox.y, hitbox.width, hitbox.height); // hitbox future

        for (Entity entity : entities) { // pour chaque entité
            if (entity == this) continue; // si l'entité est elle-même
            Rectangle entityHitbox = new Rectangle(entity.getX() + entity.hitbox.x, entity.getY() + entity.hitbox.y, entity.hitbox.width, entity.hitbox.height); // importe hitbox de l'entité
            if (futureHitbox.intersects(entityHitbox)) { // si collision
                return true; // collision
            }
        }
        return false; // pas de collision
    }


}
