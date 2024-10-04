package main.camera;

import main.GamePanel;
import main.entity.Entity;

import java.awt.*;

public class Camera {

    private final GamePanel GP; // référence au GamePanel
    private CamMode mode; // mode de la caméra
    private int x; // position X sur la map
    private int y; // position Y sur la map
    public final int SCREENX; // position X sur l'écran
    public final int SCREENY; // position Y sur l'écran
    // MODE "ENTITY FOLLOW"
    private Entity followedEntity; // entité suivie par la caméra
    // MODE "AUTO"
    private int vectX; // vecteur de direction X
    private int vectY; // vecteur de direction Y
    // MODE "FREECAM"
    private int speed = 5; // vitesse de déplacement
    public final int SPEED = 100; // vitesse maximale
    private boolean goUp, goDown, goLeft, goRight, goAcc, goDec; // contrôles

    // CONSTRUCTEUR
    public Camera(GamePanel gp, CamMode mode) {
        this.GP = gp; // référence au GamePanel
        this.mode = mode; // mode de la caméra
        SCREENX = GP.SCREEN_WIDTH/2; // position X sur l'écran
        SCREENY = GP.SCREEN_HEIGHT/2; // position Y sur l'écran
    }

    // GETTERS
    public Entity getFollowedEntity() {return followedEntity;}
    public int getX() {return x;}
    public int getY() {return y;}
    public CamMode getMode() {return mode;}
    public int getSpeed() {return speed;}
    public int getVectX() {return vectX;}
    public int getVectY() {return vectY;}
    // SETTER
    public void setX(int x) {this.x = x;}
    public void setY(int y) {this.y = y;}
    public void teleport(int x, int y) {this.x = x;this.y = y;}
    public void setFollowedEntity(Entity followedEntity) {this.followedEntity = followedEntity;}
    public void setMode(CamMode mode) {this.mode = mode;}

    // UPDATE
    // redirige l'update en fonction du mode en vigueur
    public void update() {
        switch (mode) { // redirige l'update en fonction du mode en vigueur
            case AUTO -> updateAUTO(); // mode "Auto"
            case FREECAM -> updateFC(); // mode "Freecam"
            case ENTITY_FOLLOW -> updateEF(); // mode "suivi d'entité"
        }
    }
    // mode "suivi d'entité"
    private void updateEF() {
        // suivra l'entité selectionnée
        // ne dépasse pas des bordures du monde
        if (followedEntity.getX() < GP.SCREEN_WIDTH/2) x = GP.SCREEN_WIDTH/2;
        else x = Math.min(followedEntity.getX(), GP.WORLD_WIDTH - GP.SCREEN_WIDTH / 2);
        if (followedEntity.getY() < GP.SCREEN_HEIGHT/2) y = GP.SCREEN_HEIGHT/2;
        else y = Math.min(followedEntity.getY(), GP.WORLD_HEIGHT - GP.SCREEN_HEIGHT / 2);


    }
    // mode "Auto"
    private void updateAUTO() {
        // se déplacera automatiquement à travers la map
        // todo : implémenter le déplacement automatique

    }
    // mode "freecam"
    private void updateFC() {
        // obéira aux contrôles
        // vitesse
        if (goAcc && speed < SPEED) speed++;
        if (goDec && speed > 1) speed--;
        // directions
        if (goUp) y -= speed;
        if (goDown) y += speed;
        if (goLeft) x -= speed;
        if (goRight) x += speed;
    }

    public void debugDraw(Graphics2D g2) {
        // dessine la croix au centre de la cam
        g2.setColor(Color.GREEN);
        final int E = 2;
        final int L = 20;
        g2.fillRect(SCREENX-E/2, SCREENY-L/2, E, L);
        g2.fillRect(SCREENX-L/2, SCREENY-E/2, L, E);
    }

    // CONTROLE
    public void control() {
        goLeft = GP.KEY.isLeft();
        goRight = GP.KEY.isRight();
        goUp = GP.KEY.isUp();
        goDown = GP.KEY.isDown();

        goAcc = GP.KEY.isAccFC();
        goDec = GP.KEY.isDecFC();
    }


}
