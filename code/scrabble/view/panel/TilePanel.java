package scrabble.view.panel;

import scrabble.view.frame.TileButton;

import javax.swing.*;
import java.awt.*;

public class TilePanel extends JPanel {
    private JButton tileButton;

    public TilePanel(TileButton tileButton) {
        this.tileButton = tileButton;
        this.setLayout(new FlowLayout());
        this.add(this.tileButton);
    }

    public JButton getButton() {
        return tileButton;
    }

    public void setButton(JButton tileButton) {
        this.remove(this.tileButton);
        this.tileButton = tileButton;
        this.add(this.tileButton);
        this.revalidate();
        this.repaint();
    }
}
