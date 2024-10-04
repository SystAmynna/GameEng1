package main.control;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    private boolean up, down, left, right; // directions
    private int codeUp, codeDown, codeLeft, codeRight; // touches primaires
    private int codeUp_, codeDown_, codeLeft_, codeRight_; // touches secondaires
    private boolean debug, canDebug; // debug
    private boolean pause, canPause;
    private boolean fullScreen, canFullScreen;
    private boolean accFC, decFC; // vitesse de la freecam
    private int codeAccFC, codeDecFC; // touche de vitesse de la freecam

    public KeyHandler() {
        // DEPLACEMENTS
        codeUp = KeyEvent.VK_Z; // Z
        codeDown = KeyEvent.VK_S; // S
        codeLeft = KeyEvent.VK_Q; // Q
        codeRight = KeyEvent.VK_D; // D
        // DEPLACEMENTS SECONDAIRES
        codeUp_ = KeyEvent.VK_UP; // flèche haut
        codeDown_ = KeyEvent.VK_DOWN; // flèche bas
        codeLeft_ = KeyEvent.VK_LEFT; // flèche gauche
        codeRight_ = KeyEvent.VK_RIGHT; // flèche droite
        // FREECAM
        codeAccFC = KeyEvent.VK_E;
        codeDecFC = KeyEvent.VK_A;

        // VALEURS PAR DEFAUT
        canDebug = true;
        debug = false;
        canPause = true;
        pause = false;
        canFullScreen = true;
        fullScreen = false;
    }

    // GETTERS
    public boolean isUp() {return up;}
    public boolean isDown() {return down;}
    public boolean isLeft() {return left;}
    public boolean isRight() {return right;}
    public boolean isDebug() {return debug;}
    public boolean isPause() {return pause;}
    public boolean isFullScreen() {return fullScreen;}
    public boolean isAccFC() {return accFC;}
    public boolean isDecFC() {return decFC;}
    // SETTER
    public void closePause() {pause = false;}

    // set une valeur, quand la touche est pressée ou relâchée
    private void setKeys(int code, boolean val) {
        // si la touche entrée est une touche de control, alors set la valeur à cette touche
        if (code == codeUp || code == codeUp_) up = val; // haut
        if (code == codeDown || code == codeDown_) down = val; // bas
        if (code == codeLeft || code == codeLeft_) left = val; // gauche
        if (code == codeRight || code == codeRight_) right = val; // droite
        if (code == codeAccFC) accFC = val;
        if (code == codeDecFC) decFC = val;
    }


    // DETECTION CLAVIER ----------------------------------

    @Override
    public void keyTyped(KeyEvent e) {} // quand une touche est tapée (ne peut pas reconnaitre la touche)

    @Override
    public void keyPressed(KeyEvent e) { // quand une touche est pressée
        int code = e.getKeyCode(); // convertis la touche en integer
        setKeys(code,true); // appel la méthode d'application des touches
        if (canDebug && code == KeyEvent.VK_F3) {debug = !debug; canDebug = false;} // DEBUG switch avec la touche
        if (canPause && code == KeyEvent.VK_ESCAPE) {pause = !pause; canPause = false;}
        if (canFullScreen && code == KeyEvent.VK_F11) fullScreen = !fullScreen;
    }

    @Override
    public void keyReleased(KeyEvent e) { // quand une touche est relâchée
        int code = e.getKeyCode(); // convertis la touche en integer
        setKeys(code,false); // appel la méthode d'application des touches
        if (!canDebug && code == KeyEvent.VK_F3) canDebug = true; // debug switch avec la touche
        if (code == KeyEvent.VK_ESCAPE) canPause = true;
        if (!canFullScreen && code == KeyEvent.VK_F11) canFullScreen = true;
    }


}
