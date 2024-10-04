package main;

import javax.swing.*;
import java.awt.*;

/**
 * Programme Game1
 * tentative de créer un Game Engine
 * Par SystAmynna
 * Execution ici
 */

public class App {
    public static void main(String[] args) {
        // FENÊTRE
        JFrame window = new JFrame(); // créer la nouvelle fenêtre
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // quitter le programme à la fermeture de la fenêtre
        window.setTitle("GAME 1 | SystAmynna"); // definit le titre
        window.setResizable(false); // ne peut être redimensionné par l'utilisateur
        // PANEL
        GamePanel gp = new GamePanel(window); // créer le panel
        window.add(gp); // ajoute le panel à la fenêtre
        gp.requestFocusInWindow(); // requiert la priorité de la fenêtre pour le panel
        window.pack(); // complile le panel
        // AFFICHE
        



        window.setLocationRelativeTo(null); // centre la fenêtre
        window.setVisible(true); // affiche la fenêtre


    }
}