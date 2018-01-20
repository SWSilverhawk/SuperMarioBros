package ium.mario.GameState;

import ium.mario.Game;
import ium.mario.GamePanel;
import ium.mario.TileMap.Background;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class MenuState extends GameState {

    private static final String backgroundPath = "/Backgrounds/Menu Background.png";
    private static final String menuTitlePath = "/Backgrounds/Menu Title.png";

    private Background background;
    private BufferedImage menuTitle;
    private int currentChoice = 0;
    private String[] options = { "Start", "Editor", "Quit" };
    private Font textFont;

    MenuState(GameStateManager gameStateManager) {

        this.gameStateManager = gameStateManager;
        try {
            this.menuTitle = ImageIO.read(getClass().getResourceAsStream(menuTitlePath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        background = new Background(backgroundPath);
        background.setVector(-1);

        textFont = new Font("Arial", Font.PLAIN, 18);
    }

    @Override
    public void update() { background.update(); }

    @Override
    public void draw(Graphics2D graphics) {

        // Draw Background
        background.draw(graphics);

        // Draw Title
        graphics.drawImage(menuTitle, (GamePanel.WIDTH - menuTitle.getWidth()) / 2, (GamePanel.HEIGHT - menuTitle.getHeight() * 2) / 2, null);

        // Draw Options
        graphics.setFont(textFont);
        for (int i = 0; i < options.length; i++) {

            if (i == currentChoice) graphics.setColor(Color.YELLOW);
            else graphics.setColor(Color.WHITE);

            Rectangle2D optionBounds = graphics.getFontMetrics().getStringBounds(options[i], graphics);
            int optionWidth = (GamePanel.WIDTH - (int) optionBounds.getWidth()) / 2;
            int optionHeight = ((GamePanel.HEIGHT - (int) optionBounds.getHeight()) / 3) * 2;

            graphics.drawString(options[i], optionWidth, optionHeight + i * 20);
        }

    }

    private void select() {
        if (currentChoice == 0) {
            Object[] option = new Object[gameStateManager.getLevelNumber()];
            for (int i = 0; i < gameStateManager.getLevelNumber(); i++) option[i] = i+1;
            Object level = JOptionPane.showInputDialog(Game.gameWindow, "Select a level", "Level Selector", JOptionPane.PLAIN_MESSAGE, null, option, 1);
            if (level != null) {
                gameStateManager.setCurrentLevel((int)level);
                gameStateManager.setState(GameStateManager.LEVELSTATE);
            }
        }
        if (currentChoice == 1) gameStateManager.setState(GameStateManager.EDITORSTATE);
        if (currentChoice == 2) System.exit(0);

    }

    @Override
    public void keyPressed(int key) {

        if (key == KeyEvent.VK_ENTER) select();

        if (key == KeyEvent.VK_UP) {
            currentChoice--;
            if (currentChoice == -1) currentChoice = options.length - 1;
        }

        if (key == KeyEvent.VK_DOWN) {
            currentChoice++;
            if (currentChoice == options.length) currentChoice = 0;
        }

    }

    @Override
    public void keyReleased(int key) {}

    @Override
    public void mousePressed(MouseEvent event) {}

    @Override
    public void mouseReleased(MouseEvent event) {}

    @Override
    public void mouseMoved(MouseEvent event) {}

    @Override
    public void mouseDragged(MouseEvent event) {}
}
