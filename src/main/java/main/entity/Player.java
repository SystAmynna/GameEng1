package main.entity;

import main.GamePanel;
import main.Room;
import org.w3c.dom.css.Rect;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;


public class Player extends Entity{

    private final int SPEED = 3*GamePanel.SCALE; // vitesse du joueur
    private BufferedImage sprite; // sprite du joueur

    // CONSTRUCTEUR
    public Player(GamePanel gp, Room room, int x, int y) {
        super(gp, room, EntityType.NORMAL, x ,y); // appel du constructeur de la classe mère

        try { // chargement du sprite
            sprite = ImageIO.read(Objects.requireNonNull(this.getClass().getResourceAsStream("/sprites/entities/player/player.png")));
        } catch (IOException e) {throw new RuntimeException(e);}

        // hitbox
        this.hitbox = new Rectangle(-GP.PLAYER_SIZE/2,-GP.PLAYER_SIZE/2, GP.PLAYER_SIZE, GP.PLAYER_SIZE);

        room.PLAYERS.add(this); // ajout du joueur à la liste des joueurs de la salle
    }

    @Override
    public void update() {
        super.update(); // appel de la méthode update de la classe mère (au cas où je decide de rajouter des choses plus tard)
        // UPDATE

        updatePos(SPEED); // mise à jour de la position du joueur (méthode de deplacement normale)

    }


    // CONTROLS
    public void control() { // controle du joueur
        // todo : implémenter plus tard la gestion des controls avec l'objet Controls
        goLeft = GP.KEY.isLeft();
        goRight = GP.KEY.isRight();
        goUp = GP.KEY.isUp();
        goDown = GP.KEY.isDown();
    }

    @Override
    protected void draw(Graphics2D g2) { // affichage du joueur

        // affichage du sprite
        g2.drawImage(sprite, getScreenX()-GP.PLAYER_SIZE/2, getScreenY()-GP.PLAYER_SIZE/2, GP.PLAYER_SIZE, GP.PLAYER_SIZE, null);

        if (GP.isDebug()) { // affichage du debug
            final int MARGE = getScreenX() + GP.PLAYER_SIZE/2 +2; // marge pour l'affichage des infos
            final int BORDER = getScreenY() - GP.PLAYER_SIZE/2 +6; // bordure pour l'affichage des infos
            final int DELTA = 12; // delta entre chaque ligne
            int md = 0;
            g2.setColor(this.debugColor);
            g2.drawRect(this.getScreenX()+ hitbox.x, this.getScreenY()+ hitbox.y, hitbox.width, hitbox.height);
            g2.fillRect(this.getScreenX()-2, this.getScreenY()-2, 4, 4);
            g2.setFont(new Font("Couriel", Font.ITALIC, DELTA-2));
            g2.drawString("ID: "+this.ID, MARGE, BORDER+DELTA*md++);
            g2.drawString("Coo: "+this.getX()+" : "+this.getY(), MARGE, BORDER+DELTA*md++);
            g2.drawString("Tile: "+this.getX()/GP.TILE_SIZE+" : "+this.getY()/GP.TILE_SIZE, MARGE, BORDER+DELTA*md++);
        }

    }
}
