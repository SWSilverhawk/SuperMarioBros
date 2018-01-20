package ium.mario.GameState;

import ium.mario.Entity.*;
import ium.mario.Entity.Characters.*;
import ium.mario.GamePanel;
import ium.mario.TileMap.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class LevelState extends GameState {

    private TileMap tileMap;
    private Mario mario;
    private Luigi luigi;
    private ArrayList<Enemy> goombas;
    private boolean levelComplete;

    LevelState(GameStateManager gameStateManager, SpritesLoader spritesLoader, int levelNumber) {
        this.gameStateManager = gameStateManager;

        tileMap = new TileMap(16, spritesLoader);
        tileMap.loadMap("/Maps/Level " + levelNumber + ".map");
        tileMap.setPosition(0, 0);

        mario = new Mario(tileMap, spritesLoader);
        Point marioPosition = tileMap.getMarioPosition();
        mario.setPosition(marioPosition.x, marioPosition.y);

        luigi = new Luigi(tileMap, spritesLoader);
        Point luigiPosition = tileMap.getLuigiPosition();
        luigi.setPosition(luigiPosition.x, luigiPosition.y);

        goombas = new ArrayList<>();
        Goomba goomba;
        ArrayList<Point> goombasPositions = tileMap.getGoombasPositions();
        for (Point position : goombasPositions) {
            goomba = new Goomba(tileMap, spritesLoader);
            goomba.setPosition(position.x, position.y);
            goombas.add(goomba);
        }

        levelComplete = false;
    }

    @Override
    public void update() {

        if (levelComplete || mario.isDead()) return;

        // Update Mario
        mario.update();
        tileMap.setPosition(GamePanel.WIDTH / 2 - mario.getx(), GamePanel.HEIGHT / 2 - mario.gety());

        // Update Luigi
        luigi.update();

        // Attack Characters
        mario.checkAttack(goombas);

        // Update All Characters
        for (int i = 0; i < goombas.size(); i++) {
            Enemy enemy = goombas.get(i);
            enemy.update();
            if (enemy.isDead()) {
                goombas.remove(i);
                i--;
            }
        }

    }

    @Override
    public void draw(Graphics2D graphics) {

        if (levelComplete) return;

        // Draw Background
        graphics.setColor(new Color(109, 143, 251));
        graphics.fill(new Rectangle(GamePanel.WIDTH, GamePanel.HEIGHT));

        // Draw Tilemap
        tileMap.draw(graphics);

        // Draw Goombas
        for (Enemy goomba : goombas) goomba.draw(graphics);

        // Draw Mario
        mario.draw(graphics);

        // Draw Luigi
        luigi.draw(graphics);

        // Check Mario Death
        if (mario.isDead()) {
            graphics.setColor(Color.BLACK);
            graphics.fill(new Rectangle(GamePanel.WIDTH, GamePanel.HEIGHT));
            graphics.setFont(new Font("Arial", Font.BOLD, 25));
            graphics.setColor(Color.WHITE);
            int stringWidth = graphics.getFontMetrics().stringWidth("GAME OVER");
            graphics.drawString("GAME OVER", (GamePanel.WIDTH - stringWidth) / 2, GamePanel.HEIGHT / 2);
            graphics.setFont(new Font("Arial", Font.PLAIN, 10));
            stringWidth = graphics.getFontMetrics().stringWidth("Press enter to continue");
            graphics.drawString("Press enter to continue", (GamePanel.WIDTH - stringWidth) / 2, GamePanel.HEIGHT / 2 + 30);
        }

        // Check Level Complete
        if (mario.getRectangle().intersects(luigi.getRectangle())) {
            graphics.setColor(Color.ORANGE);
            graphics.fill(new Rectangle(GamePanel.WIDTH, GamePanel.HEIGHT));
            graphics.setFont(new Font("Arial", Font.BOLD, 25));
            graphics.setColor(Color.BLACK);
            int stringWidth = graphics.getFontMetrics().stringWidth("LEVEL COMPLETE");
            graphics.drawString("LEVEL COMPLETE", (GamePanel.WIDTH - stringWidth) / 2, GamePanel.HEIGHT / 2);
            graphics.setFont(new Font("Arial", Font.PLAIN, 10));
            stringWidth = graphics.getFontMetrics().stringWidth("Press enter to continue");
            graphics.drawString("Press enter to continue", (GamePanel.WIDTH - stringWidth) / 2, GamePanel.HEIGHT / 2 + 30);
            levelComplete = true;
        }

    }

    @Override
    public void keyPressed(int key) {
        if (key == KeyEvent.VK_LEFT) mario.setLeft(true);
        if (key == KeyEvent.VK_RIGHT) mario.setRight(true);
        if (key == KeyEvent.VK_UP) mario.setJumping(true);
        if (key == KeyEvent.VK_ENTER && (mario.isDead() || levelComplete)) gameStateManager.setState(GameStateManager.MENUSTATE);
        if (key == KeyEvent.VK_ESCAPE) gameStateManager.setState(GameStateManager.MENUSTATE);
    }

    @Override
    public void keyReleased(int key) {
        if (key == KeyEvent.VK_LEFT) mario.setLeft(false);
        if (key == KeyEvent.VK_RIGHT) mario.setRight(false);
        if (key == KeyEvent.VK_UP) mario.setJumping(false);
    }

    @Override
    public void mousePressed(MouseEvent event) {}

    @Override
    public void mouseReleased(MouseEvent event) {}

    @Override
    public void mouseMoved(MouseEvent event) {}

    @Override
    public void mouseDragged(MouseEvent event) {}
}
