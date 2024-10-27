package scrabble.view.frame;

import scrabble.model.Player;
import scrabble.view.panel.*;

import javax.swing.*;
import java.awt.*;
import java.util.Scanner;

public class FrameTest extends JFrame {

    private JPanel mainMenu = new MainMenuScreen();
    private JPanel host = new HostScreen();
    private JPanel join = new JoinScreen();
    private JPanel waiting = new WaitingScreen();
    private JPanel game = new GameScreen();
    private JPanel winner = new WinnerScreen(new Player[]{new Player("Ian", 1),
            new Player("David", 2),
            new Player("Max", 3),
            new Player("Sam", 4)});

    public FrameTest() {
    }

    private void setupFrame() {
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        this.setMinimumSize(new Dimension(250,150));
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public void showGame() {
        this.add(game);
        this.setupFrame();
    }

    public void showHost() {
        this.add(host);
        this.setupFrame();
    }

    public void showJoin() {
        this.add(join);
        this.setupFrame();
    }

    public void showMain() {
        this.add(mainMenu);
        this.setupFrame();
    }

    public void showWaiting() {
        this.add(waiting);
        this.setupFrame();
    }

    public void showWinner() {
        this.add(winner);
        this.setupFrame();
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.print("Enter screen to view: ");
        String screen = in.next();
        if (screen.equalsIgnoreCase("game")) {
            new FrameTest().showGame();
        } else if (screen.equalsIgnoreCase("host")) {
            new FrameTest().showHost();
        } else if (screen.equalsIgnoreCase("join")) {
            new FrameTest().showJoin();
        } else if (screen.equalsIgnoreCase("main")) {
            new FrameTest().showMain();
        } else if (screen.equalsIgnoreCase("waiting")) {
            new FrameTest().showWaiting();
        } else if (screen.equalsIgnoreCase("winner")) {
            new FrameTest().showWinner();
        }
        in.close();
    }
}
