package main;

import main.entity.Entity;
import main.entity.Player;
import main.tile.Tile;

import java.util.ArrayList;

public class Room {

    // todo : changer la structure de la simulation
    private final GamePanel GP;
    // todo : definir des coordonnées d'entrée de la salle
    private static int idCounter = 0;
    public final int ID;
    protected static final ArrayList<Room> ROOMS = new ArrayList<Room>(); // liste des rooms
    public final ArrayList<Entity> ENTITIES = new ArrayList<Entity>(); // liste des entités
    public final ArrayList<Player> PLAYERS = new ArrayList<Player>();
    public Tile[][] map;

    public Room(GamePanel GP) {
        this.GP = GP;
        this.ID = idCounter++;
        ROOMS.add(this);
    }



}
