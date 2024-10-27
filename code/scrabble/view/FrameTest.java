package scrabble.view;

import scrabble.model.Player;

import javax.swing.*;
import java.awt.*;

public class FrameTest extends JFrame {
    public FrameTest() {
//        JoinScreen join = new JoinScreen();
//        this.add(join);
//        HostScreen host = new HostScreen();
//        this.add(host);
//        MainMenu mainMenu = new MainMenu();
//        this.add(mainMenu);
//        WaitingScreen waitingScreen = new WaitingScreen();
//        this.add(waitingScreen);
        /*
        WinnerScreen winnerScreen = new WinnerScreen(new Player[]{new Player("Ian", 1),
                new Player("David", 2),
                new Player("Max", 3),
                new Player("Sam", 4)});
        this.add(winnerScreen);

         */
        GameScreen gameScreen = new GameScreen();
        this.add(gameScreen);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        this.setMinimumSize(new Dimension(250,150));
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        new FrameTest();
    }
}
