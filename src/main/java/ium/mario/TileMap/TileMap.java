package ium.mario.TileMap;

import ium.mario.GameState.SpritesLoader;
import ium.mario.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;

public class TileMap {

    private static final int VOID = 7;

    private double posX;
    private double posY;

    private int minX;
    private int minY;
    private int maxX;
    private int maxY;

    private int[][] map;
    private int tileSize;
    private int numRows;
    private int numCols;

    private Point marioPosition;
    private Point luigiPosition;
    private ArrayList<Point> goombasPositions;

    private BufferedImage[] tiles;

    private int rowOffset;
    private int colOffset;
    private int numRowsToDraw;
    private int numColsToDraw;

    public TileMap(int tileSize, SpritesLoader spritesLoader) {
        this.tileSize = tileSize;
        numRowsToDraw = GamePanel.HEIGHT / tileSize + 2;
        numColsToDraw = GamePanel.WIDTH / tileSize + 2;
        tiles = spritesLoader.getBlocks();
        goombasPositions = new ArrayList<>();
    }

    public void loadMap(String path) {

        try {

            BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(path)));
            numCols = Integer.parseInt(reader.readLine());
            numRows = Integer.parseInt(reader.readLine());
            map = new int[numRows][numCols];
            int width = numCols * tileSize;
            int height = numRows * tileSize;

            minX = GamePanel.WIDTH - width;
            maxX = 0;
            minY = GamePanel.HEIGHT - height;
            maxY = 0;

            String[] tokens;

            for (int row = 0; row < numRows; row++) {
                tokens = reader.readLine().split("\\s+");
                for (int col = 0; col < numCols; col++) map[row][col] = Integer.parseInt(tokens[col]);
            }

            tokens = reader.readLine().split("\\s+");
            marioPosition = new Point(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]));

            tokens = reader.readLine().split("\\s+");
            luigiPosition = new Point(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]));

            int goombasNumber = Integer.parseInt(reader.readLine());
            for (int i = 0; i < goombasNumber; i++) {
                tokens = reader.readLine().split("\\s+");
                goombasPositions.add(new Point(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1])));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public int getTileSize() { return tileSize; }
    public double getPosX() { return posX; }
    public double getPosY() { return posY; }
    public int getNumRows() { return numRows; }
    public int getNumCols() { return numCols; }
    public Point getMarioPosition() { return marioPosition; }
    public Point getLuigiPosition() { return luigiPosition; }
    public ArrayList<Point> getGoombasPositions() { return goombasPositions; }

    public boolean isBlock(int row, int col) { return map[row][col] != VOID; }

    public void setPosition(double posX, double posY) {
        this.posX += posX - this.posX;
        this.posY += posY - this.posY;

        if (posX < minX) this.posX = minX;
        if (posX > maxX) this.posX = maxX;
        if (posY < minY) this.posY = minY;
        if (posY > maxY) this.posY = maxY;

        colOffset = (int)-this.posX / tileSize;
        rowOffset = (int)-this.posY / tileSize;
    }

    public void draw(Graphics2D graphics) {
        for (int row = rowOffset; row < rowOffset + numRowsToDraw; row++) {
            if (row >= numRows) break;

            for (int col = colOffset; col < colOffset + numColsToDraw; col++) {
                if (col >= numCols) break;

                int tile = map[row][col];
                if (tile == VOID) continue;

                graphics.drawImage(tiles[tile], (int)posX + col * tileSize, (int)posY + row * tileSize, null);
            }
        }
    }
}
