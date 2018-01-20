package ium.mario;

import ium.mario.GameState.GameStateManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class GamePanel extends JPanel implements Runnable, KeyListener, MouseListener, MouseMotionListener {

    // Dimensions
    public static final int WIDTH = 480;
    public static final int HEIGHT = 320;
    public static final int SCALE = 2;

    // GameThread
    private Thread gameThread;
    private boolean gameActive;
    private int FPS = 60;
    private long targetTime = 1000 / FPS;

    // Image
    private BufferedImage image;
    private Graphics2D graphics;

    // GameStateManager
    private GameStateManager gameStateManager;

    GamePanel() {
        super();
        setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        setFocusable(true);
        requestFocus();
    }

    @Override
    public void addNotify() {
        super.addNotify();
        if (gameThread == null) {
            gameThread = new Thread(this);
            addKeyListener(this);
            addMouseListener(this);
            addMouseMotionListener(this);
            gameThread.start();
        }
    }

    private void init() {

        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        graphics = (Graphics2D) image.getGraphics();
        gameActive = true;
        gameStateManager = new GameStateManager();

    }

    @Override
    public void run() {

        init();

        long startTime;
        long elapsedTime;
        long waitTime;

        // ium.mario.Game Loop
        while (gameActive) {

            startTime = System.nanoTime();

            update();
            draw();
            drawToScreen();

            elapsedTime = System.nanoTime() - startTime;

            waitTime = targetTime - elapsedTime / 1000000;
            if (waitTime < 0) waitTime = 5;

            try {
                Thread.sleep(waitTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    private void update() { gameStateManager.update(); }

    private void draw() { gameStateManager.draw(graphics); }

    private void drawToScreen() {
        Graphics panelContext = getGraphics();
        panelContext.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
        panelContext.dispose();
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) { if (gameStateManager != null) gameStateManager.keyPressed(e.getKeyCode()); }

    @Override
    public void keyReleased(KeyEvent e) { if (gameStateManager != null) gameStateManager.keyReleased(e.getKeyCode()); }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) { if (gameStateManager != null) gameStateManager.mousePressed(e); }

    @Override
    public void mouseReleased(MouseEvent e) { if (gameStateManager != null) gameStateManager.mouseReleased(e); }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseDragged(MouseEvent e) { if (gameStateManager != null) gameStateManager.mouseDragged(e); }

    @Override
    public void mouseMoved(MouseEvent e) { if (gameStateManager != null) gameStateManager.mouseMoved(e); }
}
