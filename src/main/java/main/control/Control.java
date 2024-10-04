package main.control;

public class Control {

    private ControlState state; // état du controle
    // GENERAL
    private boolean tchat; // action
    // PLAYING
    private boolean goUp, goDown, goLeft, goRight; // direction de déplacement (actions)
    private boolean sprint; // action
    private boolean pause; // actions
    private boolean interract;
    private boolean inventory, map, book;
    private boolean attack, use;
    private boolean hotbar1, hotbar2, hotbar3, hotbar4, hotbar5, hotbar6, hotbar7, hotbar8, hotbar9, hotbar0;
    // MENU
    private boolean up, down, left, right; // direction de déplacement (actions)
    private boolean valid, back; // action


    public Control(ControlState state) {
        this.state = state;
    }

    // GETTERS

    // SETTER
    public void setState(ControlState state) {this.state = state;}

    public void update() {
        switch (state) {
            case PLAYING:
                break;
            case MENU:
                break;
            case WRITE:
                break;
        }
    }

    private void playing() {
        // todo : actions
    }
    private void menu() {
        // todo : actions
    }
    private void write() {
        // todo : actions
    }



}
