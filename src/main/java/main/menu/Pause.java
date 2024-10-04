package main.menu;

import main.GamePanel;

import java.awt.*;

public class Pause {

    private final GamePanel GP;
    private int selected = 0;
    private boolean open = false;
    private boolean goUp, goDown, goLeft, goRight;
    private boolean canUp, canDown, canLeft, canRight;
    private PauseSub ps = PauseSub.NONE;


    public Pause(GamePanel gp) {
        GP = gp;

        canLeft = true;
        canRight = true;
        canUp = true;
        canDown = true;
    }

    // SETTER
    public void resetOpen() {open = false;}

    // UPDATE
    public void update() {

        if (!open) {
            selected = 0;
            ps = PauseSub.NONE;
            open = true;
        }


        if (goUp) selected--;
        else if (goDown) selected++;

        if (selected < 0) selected = 3;
        else if (selected > 3) selected = 0;

        if (goRight) {
            switch (selected) {
                case 0 -> GP.KEY.closePause();
                case 1 -> ps = PauseSub.OPTIONS;
                case 2 -> ps = PauseSub.CONTROLS;
                case 3 -> System.exit(0);
            }
        }

        if (goLeft) GP.KEY.closePause();




    }

    // DRAW
    public void draw(Graphics2D g2) {

        // FONDU SOMBRE
        AlphaComposite alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f); // defini la tansparence
        g2.setComposite(alphaComposite); // applique la transparence
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, GP.SCREEN_WIDTH, GP.SCREEN_HEIGHT);
        g2.setComposite(AlphaComposite.SrcOver); // ferme la transparence

        // TITRE
        g2.setFont(new Font("Couriel", Font.BOLD, 50));
        g2.setColor(Color.WHITE);
        g2.drawString("PAUSE", 210, 150);

        // ENCADRE
        final Color bgC = new Color(30, 30, 80);
        final int encW = 300;
        final int encH = 400;
        g2.setColor(bgC);
        g2.fillRect(200, 220, encW, encH);
        g2.setColor(Color.LIGHT_GRAY);
        g2.drawRect(200, 220, encW, encH);
        g2.setColor(Color.WHITE);
        g2.drawRect(205, 215, encW-10, encH+10);
        g2.drawRect(195, 225, encW+10, encH-10);
        final int lsc = 20;
        g2.drawRect(190, 210, lsc, lsc);
        g2.drawRect(190+encW, 210, lsc, lsc);
        g2.drawRect(190, 210+encH, lsc, lsc);
        g2.drawRect(190+encW, 210+encH, lsc, lsc);


        // CHOIX
        g2.setFont(new Font("Couriel", Font.ITALIC, 30));
        final Color nC = Color.WHITE;
        final Color sC = (ps == PauseSub.NONE) ? Color.ORANGE : Color.BLUE;
        final int nX = 240;
        final int sX = 280;
        final int B = 310;
        final int D = 80;
        int d = 0;
        g2.setColor((selected == 0) ? sC : nC);
        g2.drawString("Retour au Jeu", (selected == 0) ? sX : nX, B+D*d++);
        g2.setColor((selected == 1) ? sC : nC);
        g2.drawString("Options", (selected == 1) ? sX : nX, B+D*d++);
        g2.setColor((selected == 2) ? sC : nC);
        g2.drawString("Controls", (selected == 2) ? sX : nX, B+D*d++);
        g2.setColor((selected == 3) ? sC : nC);
        g2.drawString("Quitter", (selected == 3) ? sX : nX, B+D*d);

        // SUB MENU
        if (ps != PauseSub.NONE) {
            final int subW = 400;
            final int subH = 400;

            g2.setColor(bgC);
            g2.fillRect(600, 220, subW, subH);
            g2.setColor(Color.LIGHT_GRAY);
            g2.drawRect(600, 220, subW, subH);
            g2.setColor(Color.WHITE);
            g2.drawRect(605, 215, subW-10, subH+10);
            g2.drawRect(595, 225, subW+10, subH-10);

            g2.drawRect(590, 210, lsc, lsc);
            g2.drawRect(590+subW, 210, lsc, lsc);
            g2.drawRect(590, 210+subH, lsc, lsc);
            g2.drawRect(590+subW, 210+subH, lsc, lsc);


            if (ps == PauseSub.OPTIONS) drawSubOpt(g2);
            else if (ps == PauseSub.CONTROLS) drawSubCtrl(g2);
        }


    }

    private void drawSubOpt(Graphics2D g2) {

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Couriel", Font.ITALIC, 40));
        g2.drawString("COMING SOON !", 630, 350);


    }

    private void drawSubCtrl(Graphics2D g2) {

    }

    // CONTROL
    public void control() {

        if (canLeft && GP.KEY.isLeft()) {
            goLeft = true;
            canLeft = false;
        } else goLeft = false;
        if (!GP.KEY.isLeft()) canLeft = true;

        if (canRight && GP.KEY.isRight()) {
            goRight = true;
            canRight = false;
        } else goRight = false;
        if (!GP.KEY.isRight()) canRight = true;

        if (canUp && GP.KEY.isUp()) {
            goUp = true;
            canUp = false;
        } else goUp = false;
        if (!GP.KEY.isUp()) canUp = true;

        if (canDown && GP.KEY.isDown()) {
            goDown = true;
            canDown = false;
        } else goDown = false;
        if (!GP.KEY.isDown()) canDown = true;



    }
}
