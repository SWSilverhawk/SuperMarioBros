package ium.mario.TileMap;

import ium.mario.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Background {

    private BufferedImage image;

    private int x;
    private int y;
    private int dx;

    public Background(String path) {

        try {
            this.image = ImageIO.read(getClass().getResourceAsStream(path));
            this.y = 0;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void setPosition(int x) { this.x = x % image.getWidth(); }

    public void setVector(int dx) { this.dx = dx; }

    public void update() { setPosition(x + dx); }

    public void draw(Graphics2D graphics) {
        int i = 0;
        while (x + i * image.getWidth() < GamePanel.WIDTH) {
            graphics.drawImage(image, x + i * image.getWidth(), y, null);
            i++;
        }
    }

}
