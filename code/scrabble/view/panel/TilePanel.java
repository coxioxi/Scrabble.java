package scrabble.view.panel;

import scrabble.view.frame.TileButton;

import javax.swing.*;
import java.awt.*;

public class TilePanel extends JPanel {
    private TileButton tileButton;

    public TilePanel(TileButton tileButton) {
        this.tileButton = tileButton;
        this.setLayout(new FlowLayout());
        this.add(this.tileButton);
    }

    public TileButton getTileButton() {
        return tileButton;
    }
}
