package scrabble.view.screen.component;

import scrabble.view.screen.GameScreen;

import javax.swing.*;
import java.awt.*;

public class RackPanel extends JPanel {
    private TilePanel[] tilePanels;

    public RackPanel(TilePanel[] tilePanels){
        this.tilePanels = tilePanels;
        this.setLayout(new GridLayout(1, GameScreen.RACK_SIZE, 10, 0));
        for (TilePanel tp : this.tilePanels) {
            this.add(tp);
        }
    }

    public TilePanel[] getTilePanels() {
        return tilePanels;
    }

    public void setButton(JButton tileButton, int i) {
        tilePanels[i].setButton(tileButton);
        this.revalidate();
        this.repaint();
    }

}
