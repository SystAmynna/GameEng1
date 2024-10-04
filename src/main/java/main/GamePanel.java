package main;

import main.camera.CamMode;
import main.camera.Camera;
import main.control.KeyHandler;
import main.entity.Entity;
import main.entity.Player;
import main.menu.Pause;
import main.tile.TileManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

public class GamePanel extends JPanel implements Runnable {

    // OBJETS DE FONCTIONNEMENT
    private final Thread GAME_THREAD = new Thread(this);
    public final KeyHandler KEY = new KeyHandler();
    private final JFrame WINDOW; // fenêtre de jeu
    public final Camera CAM = new Camera(this, CamMode.NONE); // la caméra du client
    public final TileManager TM = new TileManager(this); // gestionnaire de Tiles
    // MENU
    // todo : créer un parent "menu", definit un "currentMenu" et un "menuList"
    private final Pause pause = new Pause(this); // menu pause
    // VARIABLES DE FONCTIONNEMENT
    private boolean running = true; // si la boucle de gameplay doit toujours tourner
    private final int FPS_NOMINAL = 60; // FPS prévus par le jeu
    private int fps = 0; // FPS
    private int currentFrame = 0; // frame arrivé dans la seconde
    private boolean debug; // si le mode debug est activé
    private boolean fullScreen; // si le mode plein écran est activé
    // todo : énumérer l'état "PLAYING" et l'état "IN_MENU" / rediriger le controleur vers l'état actif
    private GameState gameState = GameState.PLAYING; // état du jeu
    // VARIABLES INFORMATIVES
    private long updateTime; // temps d'actualisation de la simulation
    private long drawTime; // temps d'affichage
    private long totalTime; // temps total
    // CONSTANTES -------------------------------------------------------------------
    //original size
    public final static int ORIGINAL_TILE_SIZE = 64; // résolution Tile
    public final static int ORIGINAL_PLAYER_SIZE = ORIGINAL_TILE_SIZE / 2; // résolution joueur
    public final static int SCALE = 2; // multiplicateur
    //size
    public final static int TILE_SIZE = ORIGINAL_TILE_SIZE * SCALE; // résolution Tile écran
    public final static int PLAYER_SIZE = ORIGINAL_PLAYER_SIZE * SCALE; // résolution joueur écran
    //écran
    public final static int MAX_SCREEN_COL = 10; // nombre de colonnes sur l'écran
    public final static int MAX_SCREEN_ROW = 6; // nombre de lignes sur l'écran
    public final static int SCREEN_WIDTH = MAX_SCREEN_COL * TILE_SIZE; // résolution horizontale de l'écran
    public final static int SCREEN_HEIGHT = MAX_SCREEN_ROW * TILE_SIZE; // résolution verticale de l'écran
    //monde
    public final static int MAX_WORLD_COL = 1000; // nombre de Tiles horizontale du monde
    public final static int MAX_WORLD_ROW = 1000; // nombre de Tiles verticale du monde
    public final static int WORLD_WIDTH = TILE_SIZE * MAX_WORLD_COL; // résolution horizontale du monde
    public final static int WORLD_HEIGHT = TILE_SIZE * MAX_WORLD_ROW; // résolution verticale du monde
    // VARIABLES UTILES
    private final ArrayList<Room> ROOMS = Room.ROOMS; // liste des Rooms
    public Room currentRoom; // room actuelle
    // FULLSCREEN
    private BufferedImage screen; // image de l'écran complet
    private Graphics2D g2; // graphique de l'écran complet
    private int originX = 0, originY = 0; // origine de l'écran complet
    private int screenW = SCREEN_WIDTH, screenH = SCREEN_HEIGHT; // résolution de l'écran complet
    private BufferedImage fondFullScreen; // image de fond en plein écran



    // CONSTRUCTEUR
    protected GamePanel(JFrame frame) {
        // PANEL PARAMETRAGE
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT)); // definition des dimensions
        this.setBackground(Color.BLACK); // couleur du background
        this.setDoubleBuffered(true); // garde la dernière frame pendant l'affichage de la nouvelle
        this.addKeyListener(KEY); // ajoute un écouteur de clavier
        this.setFocusable(true); // peut recevoir l'attention du KeyManager
        // fullScreen
        screen = new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT, BufferedImage.TYPE_INT_RGB); // écran complet
        g2 = (Graphics2D) screen.getGraphics(); // graphique de l'écran complet
        try {
            fondFullScreen = ImageIO.read(Objects.requireNonNull(this.getClass().getResourceAsStream("/sprites/fondFullScreen.jpg"))); // image de fond en plein écran
        } catch (IOException e) {e.printStackTrace();}
        // OBJETS DE FONCTIONNEMENTS
        WINDOW = frame; // definition de la fenêtre
        // ROOM
        new Room(this); // création de la room 0
        currentRoom = ROOMS.get(0); // sélection de la room 0

        // TEST - - - - - -
        currentRoom.map = TM.genDefaultMap();
        CAM.setMode(CamMode.ENTITY_FOLLOW);
        CAM.teleport(9*TILE_SIZE, 9*TILE_SIZE);
        CAM.setFollowedEntity(new Player(this, currentRoom, 4*TILE_SIZE, 4*TILE_SIZE));
        new Player(this, currentRoom, 6*TILE_SIZE, 6*TILE_SIZE);
        //TM.genDefaultMap();

        // START LE THREAD
        GAME_THREAD.start(); // démarrage du Thread
    }

    // GETTER
    public boolean isDebug() {return debug;}

    // RUN
    @Override
    public void run() {
        double drawInterval = (double) (1000000000 / FPS_NOMINAL); // interval entre chaque dessin du panel
        double delta = 0.0; // delta de temps
        long lastTime = System.nanoTime(); // temps de départ
        long timer = 0L; // compte le temps
        long currentTime; // temps actuel
        while (running) { // tant que le Thread Simulation fonctionne
            currentTime = System.nanoTime(); // récupère le temps de maintenant
            delta += (double) (currentTime - lastTime) / drawInterval; // calcule le delta
            timer += currentTime - lastTime; // compte le temps
            lastTime = currentTime; // met à jour le temps de départ
            if (delta >= 1.0) { // si le delta est supérieur à 1
                updateTime = System.nanoTime(); // chrono de l'update
                update(); // UPDATE
                updateTime = System.nanoTime() - updateTime; // fin du chrono de l'update
                drawTime = System.nanoTime(); // chrono de l'affichage
                this.repaint(); // AFFICHAGE
                drawTime = System.nanoTime() - drawTime; // fin du chrono de l'affichage
                totalTime = updateTime + drawTime; // temps total
                if (totalTime > drawInterval) System.out.println("[ALERTE] " + totalTime*1000000 + " ms"); // alerte si le temps de traitement est supérieur à l'intervalle
                --delta; // décrémente le delta
                ++currentFrame; // incrémente le nombre de frame
            }
            if (timer >= 1000000000L) { // toute les secondes
                System.out.println("FPS: " + currentFrame + "/" + FPS_NOMINAL); // affiche
                fps = currentFrame; // set les FPS
                currentFrame = 0; // remets à 0 les FPS pour la seconde prochaine
                timer = 0L; // remets le chronomètre à 0 pour la seconde prochaine
            }
        }
    }

    // UPDATE
    protected void update() {

        // todo : modifier les contrôles de chaque touche spéciale (F3 F11...)
        debug = KEY.isDebug(); // verifie l'activité du mode debug

        // GAME STATE
        // todo : Corriger avec un état "IN_MENU" et rediriger le controleur vers l'état actif
        if (KEY.isPause()) {
            gameState = GameState.PAUSE;
        } else {
            gameState = GameState.PLAYING;
            pause.resetOpen();
        }

        // CONTROLS
        // todo : corriger l'état "IN_MENU" et rediriger le controleur vers l'état actif
        if (gameState == GameState.PLAYING) {
            if (CAM.getMode() == CamMode.FREECAM) CAM.control();
            else if (CAM.getMode() == CamMode.ENTITY_FOLLOW) CAM.getFollowedEntity().control();
        } else if (gameState == gameState.PAUSE) {
            pause.control();
            pause.update();
        }


        // TILE UPDATE
        TM.update(); // mise à jour des Tiles

        // ENTITY UPDATE
        // update les entités de la room
        // todo : modifier ici lors du changement de room
        if (!currentRoom.ENTITIES.isEmpty()) {
            for (Entity entity : currentRoom.ENTITIES) if (!(entity instanceof Player)) entity.update();
            // UPDATE PLAYER APRES LES AUTRES ENTITES
            for (Player player : currentRoom.PLAYERS) player.update();
        }


        // CAMERA UPDATE
        CAM.update(); // mise à jour de la caméra ABSOLUMENT EN DERNIER !

    }

    // DRAW
    private void maindraw() { // dessine le jeu sur l'écran complet
        // TILES
        TM.render(g2, CAM); // rendu des Tiles

        // ENTITIES
        if (!currentRoom.ENTITIES.isEmpty()) { // si la liste des entités n'est pas vide
            currentRoom.ENTITIES.sort(new Comparator<Entity>() { // trie les entités par ordre croissant de Y
                @Override
                public int compare(Entity e1, Entity e2) {
                    return Integer.compare(e1.getY(), e2.getY());
                }
            });
            // trie pour afficher les entités dans l'ordre (fausse perspective)
            for (Entity entity : currentRoom.ENTITIES) entity.render(g2);
        }

        // HUD
        // todo : faire les HUD

        // MENU
        // todo : afficher le menu actif
        if (gameState == GameState.PAUSE) pause.draw(g2);

        // DEBUG GAME
        if (debug) { // DEBUG INFOS
            CAM.debugDraw(g2); // affiche les infos de la caméra
            final int MARGE = 3; // marge de décalage
            final int DELTA = 12; // delta de décalage
            int nbDelta = 0; // compte de delta
            final int BORDER = 20; // bordure
            final int CADRE = 160; // cadre
            Font title = new Font("Couriel", Font.BOLD, DELTA); // police titre
            Font info = new Font("Couriel", Font.ITALIC, DELTA); // police info
            // INFOS GLOBALS
            g2.setColor(Color.DARK_GRAY);
            g2.fillRect(0, BORDER+DELTA*nbDelta++, CADRE, 4*DELTA+3);
            g2.setColor(Color.WHITE);
            g2.setFont(title);
            g2.drawString("[F3] DEBUG DISPLAY: "+KEY.isDebug(), MARGE, BORDER+DELTA*nbDelta++);
            g2.setFont(info);
            g2.drawString("FPS: "+fps+"/"+FPS_NOMINAL+" "+totalTime/1000000+"ms ("+totalTime+")", MARGE, BORDER+DELTA*nbDelta++);
            g2.drawString("Update: "+updateTime/1000000+"ms ("+updateTime+")", MARGE+DELTA, BORDER+DELTA*nbDelta++);
            g2.drawString("Draw: "+drawTime/1000000+"ms ("+drawTime+")", MARGE+DELTA, BORDER+DELTA*nbDelta++);
            // POSITION
            g2.setColor(new Color(0, 35, 100));
            g2.fillRect(0, BORDER+DELTA*nbDelta++, CADRE, 5*DELTA+3);
            g2.setColor(Color.WHITE);
            g2.setFont(title);
            g2.drawString("POSITION: ", MARGE, BORDER+DELTA*nbDelta++);
            g2.setFont(info);
            g2.drawString("Room: "+currentRoom.ID+"/"+(ROOMS.size()-1), MARGE, BORDER+DELTA*nbDelta++);
            g2.drawString("Coordinates: "+CAM.getX()+" : "+CAM.getY(), MARGE, BORDER+DELTA*nbDelta++);
            g2.drawString("in Tile ("+TILE_SIZE+"): "+CAM.getX()%TILE_SIZE+" : "+CAM.getY()%TILE_SIZE, MARGE+DELTA, BORDER+DELTA*nbDelta++);
            g2.drawString("Tile: "+CAM.getX()/TILE_SIZE+" : "+CAM.getY()/TILE_SIZE+" ("+MAX_WORLD_COL+"/"+MAX_WORLD_ROW+")", MARGE, BORDER+DELTA*nbDelta++);
            // ENTITY
            g2.setColor(new Color(10, 60, 0));
            g2.fillRect(0, BORDER+DELTA*nbDelta++, CADRE, 5*DELTA+3);
            g2.setColor(Color.WHITE);
            g2.setFont(title);
            g2.drawString("ENTITIES:", MARGE, BORDER+DELTA*nbDelta++);
            g2.setFont(info);
            // todo : modifier le compte des entités loaded
            int el = 0;
            for (Entity entity : currentRoom.ENTITIES) if (entity.isLoaded()) el++;
            g2.drawString("entities: "+currentRoom.ENTITIES.size()+" ("+el+" loaded)", MARGE, BORDER+DELTA*nbDelta++);
            int nbN = 0, nbT = 0, nbUK = 0;
            for (Entity entity : currentRoom.ENTITIES) switch (entity.TYPE) {
                case NORMAL -> nbN++;
                case TRIGGER_AREA -> nbT++;
                default -> nbUK++;
            }
            g2.drawString("Normals: "+nbN, MARGE+DELTA, BORDER+DELTA*nbDelta++);
            g2.drawString("Trigger areas: "+nbT, MARGE+DELTA, BORDER+DELTA*nbDelta++);
            if (nbUK>0) g2.setColor(Color.RED);
            g2.drawString("Unknown: "+nbUK+((nbUK>0) ? " [!]" : " [OK]"), MARGE+DELTA, BORDER+DELTA*nbDelta++);
            g2.setColor(Color.WHITE);
            // CAMERA
            g2.setColor(new Color(80, 0, 0));
            g2.fillRect(0, BORDER+DELTA*nbDelta++, CADRE, 3*DELTA+3);
            g2.setColor(Color.WHITE);
            g2.setFont(title);
            g2.drawString("CAMERA:", MARGE, BORDER+DELTA*nbDelta++);
            g2.setFont(info);
            g2.drawString("Mode: "+CAM.getMode(), MARGE, BORDER+DELTA*nbDelta++);
            if (CAM.getMode() == CamMode.ENTITY_FOLLOW) g2.drawString("Entity to follow: "+((CAM.getFollowedEntity() != null) ? CAM.getFollowedEntity().ID : "NONE"), MARGE+DELTA, BORDER+DELTA*nbDelta++);
            else if (CAM.getMode() == CamMode.FREECAM) g2.drawString("Speed: "+CAM.getSpeed()+" (1 to "+CAM.SPEED+")", MARGE+DELTA, BORDER+DELTA*nbDelta++);
            else if (CAM.getMode() == CamMode.AUTO) g2.drawString("Vector: "+CAM.getVectX()+" : "+CAM.getVectY(), MARGE+DELTA, BORDER+DELTA*nbDelta++);
            else g2.drawString("Waiting mode...", MARGE+DELTA, BORDER+DELTA*nbDelta++);
            // NET
            g2.setColor(new Color(190, 170, 0));
            g2.fillRect(0, BORDER+DELTA*nbDelta++, CADRE, 1*DELTA+3);
            g2.setColor(Color.WHITE);
            g2.setFont(title);
            g2.drawString("NET: [OFF]", MARGE, BORDER+DELTA*nbDelta++);

        }

    }
    private void setFullScreen() { // met en plein écran

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment(); // récupère l'environnement graphique
        GraphicsDevice gd = ge.getDefaultScreenDevice(); // récupère le périphérique graphique par défaut
        if (!gd.isFullScreenSupported()) { // si le mode plein écran n'est pas supporté
            System.out.println("Le mode plein écran n'est pas supporté par votre ordinateur."); // message d'erreur
            return; // arrête la méthode
        }
        gd.setFullScreenWindow(WINDOW); // met la fenêtre en plein écran
        WINDOW.validate(); // valide la fenêtre

        screenH = WINDOW.getHeight(); // definit la hauteur de la fenêtre
        screenW = WINDOW.getWidth(); // definit la largeur de la fenêtre
        if (screenW > (screenH * SCREEN_WIDTH / SCREEN_HEIGHT)) { // si la largeur est plus grande que la largeur de l'écran
            screenW = screenH * SCREEN_WIDTH / SCREEN_HEIGHT; // definit la largeur de l'écran
            originX = (WINDOW.getWidth() - screenW) / 2; // definit l'origine X
        } else { // sinon
            screenH = screenW * SCREEN_HEIGHT / SCREEN_WIDTH; // definit la hauteur de l'écran
            originY = (WINDOW.getHeight() - screenH) / 2; // definit l'origine Y
        }


    }
    private void unFullScreen() { // enlève le plein écran

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment(); // récupère l'environnement graphique
        GraphicsDevice gd = ge.getDefaultScreenDevice(); // récupère le périphérique graphique par défaut
        gd.setFullScreenWindow(null); // enlève le plein écran
        WINDOW.validate(); // valide la fenêtre
        WINDOW.setLocationRelativeTo(null); // recentre la fenêtre

        screenW = SCREEN_WIDTH; // definit la largeur de l'écran
        screenH = SCREEN_HEIGHT; // definit la hauteur de l'écran
        originX = 0; // definit l'origine X
        originY = 0; // definit l'origine Y

    }

    protected void paintComponent(Graphics g) { // affiche le jeu
        super.paintComponent(g); // appel la méthode parent

        // FULLSCREEN ON / OFF
        // todo : refaire les controls
        if (fullScreen != KEY.isFullScreen()) { // si le mode plein écran a changé
            fullScreen = KEY.isFullScreen(); // set le mode plein écran
            if (fullScreen) setFullScreen(); // si le mode plein écran est activé met en plein écran
            else unFullScreen(); // sinon enlève le plein écran
        }

        // FULLSCREEN BACKGROUND
        //if (fullScreen) { // si le mode plein écran est activé
        //    // affiche l'image de fond en plein écran
        //    g.drawImage(fondFullScreen, 0, 0, fondFullScreen.getWidth()/2, fondFullScreen.getHeight()/2, null);
        //}

        // MAIN DRAW
        maindraw(); // dessine le jeu
        g.drawImage(screen, originX, originY, screenW, screenH, null); // affiche l'image de l'écran complet

        g.dispose(); // libère les ressources graphiques
    }

}
