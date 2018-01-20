package ium.mario;

import javax.swing.*;

public class Game {

    public static final JFrame gameWindow = new JFrame("Super Mario");

    public static void main(String args[]) {
        gameWindow.setContentPane(new GamePanel());
        gameWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        gameWindow.setResizable(false);
        gameWindow.pack();
        gameWindow.setVisible(true);
    }

}
