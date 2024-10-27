package scrabble.view;

import javax.swing.*;
import java.awt.*;

public class FrameTest extends JFrame {
    public FrameTest() {
        JoinScreen join = new JoinScreen();
        this.add(join);
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
