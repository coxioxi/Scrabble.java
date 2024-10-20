package scrabble.model;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {
    public GameFrame() {
        setTitle("Scrabble");

        setSize(800, 600);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        initializeComponents();

        setVisible(true);
    }

    public void initializeComponents() {
        JPanel boardPanel = new JPanel();
        boardPanel.setBackground(Color.LIGHT_GRAY);
        boardPanel.setPreferredSize(new Dimension(600, 400));
        add(boardPanel, BorderLayout.CENTER);

        JPanel playerInfo = new JPanel();
        playerInfo.setBackground(Color.DARK_GRAY);
        playerInfo.setPreferredSize(new Dimension(200,400));
        add(playerInfo, BorderLayout.EAST);
    }



    public static void main(String[] args) {
        new GameFrame();
    }


}
