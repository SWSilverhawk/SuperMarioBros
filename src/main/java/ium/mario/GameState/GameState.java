package ium.mario.GameState;

import java.awt.*;
import java.awt.event.MouseEvent;

abstract class GameState {

    GameStateManager gameStateManager;

    public abstract void update();
    public abstract void draw(Graphics2D graphics);
    public abstract void keyPressed(int key);
    public abstract void keyReleased(int key);
    public abstract void mousePressed(MouseEvent event);
    public abstract void mouseReleased(MouseEvent event);
    public abstract void mouseMoved(MouseEvent event);
    public abstract void mouseDragged(MouseEvent event);

}
