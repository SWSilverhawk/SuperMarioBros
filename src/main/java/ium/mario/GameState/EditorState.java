package ium.mario.GameState;

import ium.mario.Game;
import ium.mario.GamePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;

public class EditorState extends GameState {

    private static final int BASE = 0;
    private static final int BRICKS = 1;
    private static final int QUESTIONMARK = 2;
    private static final int SQUARE = 3;
    private static final int MARIO = 4;
    private static final int LUIGI = 5;
    private static final int GOOMBA = 6;
    private static final int VOID = 7;

    private static final int MAP_ROWS = 20;
    private static final int MAP_COLS = 212;

    private int[][] map;
    private BufferedImage[] sprites;
    private int tileSize;

    private int actualSprite = -1;

    private int mouseX;
    private int mouseY;

    private int mapPosition;
    private int minPosition;
    private int maxPosition;
    private Point marioPosition = null;
    private Point luigiPosition = null;
    private ArrayList<Point> goombasPositions;

    private int mouseButton = -1;

    EditorState(GameStateManager gameStateManager, SpritesLoader spritesLoader) {
        this.gameStateManager = gameStateManager;

        map = new int[MAP_ROWS][MAP_COLS];
        goombasPositions = new ArrayList<>();

        for (int i = 0; i < MAP_ROWS; i++) {
            for (int j = 0; j < MAP_COLS; j++) map[i][j] = VOID;
        }

        tileSize = 16;

        maxPosition = MAP_COLS * tileSize - GamePanel.WIDTH;
        minPosition = 0;

        sprites = new BufferedImage[7];
        sprites[BASE] = spritesLoader.getBlocks()[0];
        sprites[BRICKS] = spritesLoader.getBlocks()[1];
        sprites[QUESTIONMARK] = spritesLoader.getBlocks()[2];
        sprites[SQUARE] = spritesLoader.getBlocks()[3];
        sprites[MARIO] = spritesLoader.getMarioIdle()[0];
        sprites[LUIGI] = spritesLoader.getLuigiIdle()[0];
        sprites[GOOMBA] = spritesLoader.getGoombaMove()[0];

    }

    @Override
    public void update() {}

    private void drawMap(Graphics2D graphics) {
        for (int i = 0; i < GamePanel.HEIGHT / tileSize; i++) {
            for (int j = 0; j < GamePanel.WIDTH / tileSize; j++) {
                int tile = map[i][j + mapPosition / tileSize];
                if (tile != VOID)
                    graphics.drawImage(sprites[tile], j * tileSize, i * tileSize, tileSize, tileSize, null);
            }
        }
    }

    private void drawCharacters(Graphics2D graphics) {
        if (marioPosition != null && marioPosition.x >= mapPosition && marioPosition.x < mapPosition + GamePanel.WIDTH)
            graphics.drawImage(sprites[MARIO], marioPosition.x - mapPosition, marioPosition.y, tileSize, tileSize, null);
        if (luigiPosition != null && luigiPosition.x >= mapPosition && luigiPosition.x < mapPosition + GamePanel.WIDTH)
            graphics.drawImage(sprites[LUIGI], luigiPosition.x + tileSize - mapPosition, luigiPosition.y, -tileSize, tileSize, null);
        for (Point point : goombasPositions)
            if (point.x >= mapPosition && point.x < mapPosition + GamePanel.WIDTH)
                graphics.drawImage(sprites[GOOMBA], point.x - mapPosition, point.y, tileSize, tileSize, null);
    }

    @Override
    public void draw(Graphics2D graphics) {
        graphics.setColor(new Color(109, 143, 251));
        graphics.fill(new Rectangle(GamePanel.WIDTH, GamePanel.HEIGHT));
        graphics.setColor(Color.BLACK);
        for (int i = 0; i < GamePanel.WIDTH; i += tileSize) graphics.drawLine(i, 0, i, GamePanel.HEIGHT);
        for (int i = 0; i < GamePanel.HEIGHT; i += tileSize) graphics.drawLine(0, i, GamePanel.WIDTH, i);
        drawMap(graphics);
        drawCharacters(graphics);
        if (actualSprite != -1 && (mouseX != 0 || mouseY != 0) && mouseButton == -1) {
            if (actualSprite == LUIGI)
                graphics.drawImage(sprites[actualSprite], mouseX + tileSize / 2, mouseY - tileSize / 2, -tileSize, tileSize, null);
            else
                graphics.drawImage(sprites[actualSprite], mouseX - tileSize / 2, mouseY - tileSize / 2, tileSize, tileSize, null);
        }
    }

    private void saveMap() {
        int levelNumber = gameStateManager.getLevelNumber() + 1;
        File mapFile = new File("resources/Maps/Level " + levelNumber + ".map");

        try {
            if (!mapFile.createNewFile()) return;
            FileWriter mapFileWriter = new FileWriter(mapFile, false);

            mapFileWriter.append(Integer.toString(MAP_COLS)).append('\n')
                    .append(Integer.toString(MAP_ROWS)).append('\n');

            for (int i = 0; i < MAP_ROWS; i++) {
                for (int j = 0; j < MAP_COLS; j++) {
                    mapFileWriter.append(Integer.toString(map[i][j]));
                    if (j != MAP_COLS - 1) mapFileWriter.append(' ');
                    else mapFileWriter.append('\n');
                }
                mapFileWriter.flush();
            }

            mapFileWriter.append(Integer.toString(marioPosition.x + tileSize / 2)).append(' ')
                    .append(Integer.toString(marioPosition.y + tileSize / 2)).append('\n')
                    .append(Integer.toString(luigiPosition.x + tileSize / 2)).append(' ')
                    .append(Integer.toString(luigiPosition.y + tileSize / 2)).append('\n');

            mapFileWriter.append(Integer.toString(goombasPositions.size())).append('\n');

            for (Point point : goombasPositions) {
                mapFileWriter.append(Integer.toString(point.x + tileSize / 2)).append(' ')
                        .append(Integer.toString(point.y + tileSize / 2)).append('\n');
            }

            mapFileWriter.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

        gameStateManager.addLevel();

    }

    @Override
    public void keyPressed(int key) {
        switch (key) {
            case KeyEvent.VK_1:
                actualSprite = BASE;
                break;
            case KeyEvent.VK_2:
                actualSprite = BRICKS;
                break;
            case KeyEvent.VK_3:
                actualSprite = QUESTIONMARK;
                break;
            case KeyEvent.VK_4:
                actualSprite = SQUARE;
                break;
            case KeyEvent.VK_5:
                actualSprite = MARIO;
                break;
            case KeyEvent.VK_6:
                actualSprite = LUIGI;
                break;
            case KeyEvent.VK_7:
                actualSprite = GOOMBA;
                break;
            case KeyEvent.VK_LEFT:
                mapPosition -= tileSize;
                if (mapPosition < minPosition) mapPosition = minPosition;
                break;
            case KeyEvent.VK_RIGHT:
                mapPosition += tileSize;
                if (mapPosition > maxPosition) mapPosition = maxPosition;
                break;
            case KeyEvent.VK_ESCAPE:
                gameStateManager.setState(GameStateManager.MENUSTATE);
                break;
            case KeyEvent.VK_ENTER:
                if (marioPosition != null && luigiPosition != null) {
                    saveMap();
                    gameStateManager.setState(GameStateManager.MENUSTATE);
                    JOptionPane.showMessageDialog(Game.gameWindow, "Level create", "Success", JOptionPane.PLAIN_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(Game.gameWindow, "Mario and Luigi must be present", "Error", JOptionPane.PLAIN_MESSAGE);
                }
                break;
        }
    }

    @Override
    public void keyReleased(int key) {}

    @Override
    public void mousePressed(MouseEvent event) {
        int currRow = event.getY() / GamePanel.SCALE / tileSize;
        int currCol = (mapPosition + event.getX() / GamePanel.SCALE) / tileSize;
        mouseButton = event.getButton();

        if (event.getButton() == MouseEvent.BUTTON1) {

            if (map[currRow][currCol] == VOID) {
                switch (actualSprite) {
                    case BASE:
                        map[currRow][currCol] = BASE;
                        break;
                    case BRICKS:
                        map[currRow][currCol] = BRICKS;
                        break;
                    case QUESTIONMARK:
                        map[currRow][currCol] = QUESTIONMARK;
                        break;
                    case SQUARE:
                        map[currRow][currCol] = SQUARE;
                        break;
                    case MARIO:
                        if (marioPosition == null)
                            marioPosition = new Point(currCol * tileSize, currRow * tileSize);
                        break;
                    case LUIGI:
                        if (luigiPosition == null)
                            luigiPosition = new Point(currCol * tileSize, currRow * tileSize);
                        break;
                    case GOOMBA:
                        Point point = new Point(currCol * tileSize, currRow * tileSize);
                        if (!point.equals(marioPosition) && !point.equals(luigiPosition))
                            goombasPositions.add(point);
                    default:
                        map[currRow][currCol] = VOID;
                        break;
                }
            }
        } else if (event.getButton() == MouseEvent.BUTTON3) {
            Point point = new Point(currCol * tileSize, currRow * tileSize);
            if (point.equals(marioPosition)) marioPosition = null;
            if (point.equals(luigiPosition)) luigiPosition = null;
            goombasPositions.remove(point);
            map[currRow][currCol] = VOID;
        }
    }

    @Override
    public void mouseReleased(MouseEvent event) {
        mouseButton = -1;
        mouseX = event.getX() / GamePanel.SCALE;
        mouseY = event.getY() / GamePanel.SCALE;
    }

    @Override
    public void mouseMoved(MouseEvent event) {
        mouseX = event.getX() / GamePanel.SCALE;
        mouseY = event.getY() / GamePanel.SCALE;
    }

    @Override
    public void mouseDragged(MouseEvent event) {
        int currRow = event.getY() / GamePanel.SCALE / tileSize;
        int currCol = (mapPosition + event.getX() / GamePanel.SCALE) / tileSize;

        if (actualSprite == -1 || currRow < 0 || currRow >= MAP_ROWS || currCol < 0 || currCol >= MAP_COLS) return;

        if (mouseButton == MouseEvent.BUTTON1 && map[currRow][currCol] == VOID && actualSprite != MARIO && actualSprite != LUIGI && actualSprite != GOOMBA)
            map[currRow][currCol] = actualSprite;
        else if (mouseButton == MouseEvent.BUTTON3) map[currRow][currCol] = VOID;

    }


}
