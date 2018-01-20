package ium.mario.GameState;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class SpritesLoader {

    private BufferedImage[] marioIdle;
    private BufferedImage[] marioRun;
    private BufferedImage[] marioJump;
    private BufferedImage[] marioDeath;
    private BufferedImage[] luigiIdle;
    private BufferedImage[] goombaMove;
    private BufferedImage[] goombaDeath;
    private BufferedImage[] blocks;

    SpritesLoader() {

        BufferedImage spritesheet;

        try {

            // Mario Sprites
            spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Players.png"));

            // Idle
            marioIdle = new BufferedImage[1];
            marioIdle[0] = spritesheet.getSubimage(80, 32, 16, 16);

            // Run
            marioRun = new BufferedImage[4];
            for (int i = 0; i < 4; i++) marioRun[i] = spritesheet.getSubimage(80 + i * 16, 32, 16, 16);

            // Jump
            marioJump = new BufferedImage[1];
            marioJump[0] = spritesheet.getSubimage(160, 32, 16, 16);

            // Death
            marioDeath = new BufferedImage[1];
            marioDeath[0] = spritesheet.getSubimage(176, 32, 16, 16);

            // Luigi Sprites
            luigiIdle = new BufferedImage[1];
            luigiIdle[0] = spritesheet.getSubimage(80, 80, 16, 16);

            // Goomba Sprites
            spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Enemies.png"));

            // Move
            goombaMove = new BufferedImage[2];
            goombaMove[0] = spritesheet.getSubimage(0, 16, 16, 16);
            goombaMove[1] = spritesheet.getSubimage(16, 16, 16, 16);

            // Death
            goombaDeath = new BufferedImage[1];
            goombaDeath[0] = spritesheet.getSubimage(32, 16, 16, 16);

            // Blocks Sprites
            spritesheet = ImageIO.read(getClass().getResourceAsStream("/Tilesets/Tileset.png"));
            blocks = new BufferedImage[4];
            blocks[0] = spritesheet.getSubimage(0, 0, 16, 16);
            blocks[1] = spritesheet.getSubimage(16, 0, 16, 16);
            blocks[2] = spritesheet.getSubimage(432, 0, 16, 16);
            blocks[3] = spritesheet.getSubimage(0, 16, 16, 16);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public BufferedImage[] getMarioIdle() { return marioIdle; }
    public BufferedImage[] getMarioRun() { return marioRun; }
    public BufferedImage[] getMarioJump() { return marioJump; }
    public BufferedImage[] getMarioDeath() { return marioDeath; }
    public BufferedImage[] getLuigiIdle() { return luigiIdle; }
    public BufferedImage[] getGoombaMove() { return goombaMove; }
    public BufferedImage[] getGoombaDeath() { return goombaDeath; }
    public BufferedImage[] getBlocks() { return blocks; }

}
