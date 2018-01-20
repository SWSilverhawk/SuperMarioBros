package ium.mario.GameState;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.*;

public class GameStateManager {

    private GameState[] gameStates;
    private int currentState;
    private SpritesLoader spritesLoader;
    private int levelNumber;
    private int currentLevel;

    private static final int NUMGAMESTATES = 3;
    static final int MENUSTATE = 0;
    static final int LEVELSTATE = 1;
    static final int EDITORSTATE = 2;

    public GameStateManager() {

        gameStates = new GameState[NUMGAMESTATES];

        spritesLoader = new SpritesLoader();

        currentState = MENUSTATE;
        loadState(currentState);

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader((getClass().getResourceAsStream("/Maps/LevelsNumber.txt"))));
            levelNumber = Integer.parseInt(reader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void loadState(int state) {
        switch (state) {
            case MENUSTATE:
                gameStates[state] = new MenuState(this);
                break;
            case LEVELSTATE:
                gameStates[state] = new LevelState(this, spritesLoader, currentLevel);
                break;
            case EDITORSTATE:
                gameStates[state] = new EditorState(this, spritesLoader);
                break;
            default: break;
        }
    }

    private void unloadState(int state) { gameStates[state] = null; }

    void setState(int state) {
        unloadState(currentState);
        currentState = state;
        loadState(currentState);
    }

    public void update() { if (gameStates[currentState] != null) gameStates[currentState].update(); }

    public void draw(Graphics2D graphics) { if (gameStates[currentState] != null) gameStates[currentState].draw(graphics); }

    void addLevel() {
        levelNumber++;

        try {
            FileWriter writer = new FileWriter(new File("resources/Maps/LevelsNumber.txt"), false);
            writer.append(Integer.toString(levelNumber)).append('\n').flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    int getLevelNumber() { return levelNumber; }

    void setCurrentLevel(int currentLevel) { this.currentLevel = currentLevel; }

    public void keyPressed(int key) { gameStates[currentState].keyPressed(key); }

    public void keyReleased(int key) { gameStates[currentState].keyReleased(key); }

    public void mousePressed(MouseEvent event) { gameStates[currentState].mousePressed(event); }

    public void mouseReleased(MouseEvent event) { gameStates[currentState].mouseReleased(event); }

    public void mouseMoved(MouseEvent event) { gameStates[currentState].mouseMoved(event); }

    public void mouseDragged(MouseEvent event) { gameStates[currentState].mouseDragged(event); }

}
