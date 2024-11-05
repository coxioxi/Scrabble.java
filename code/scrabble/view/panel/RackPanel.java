package scrabble.view.panel;

import javax.swing.*;
import java.awt.*;

public class RackPanel extends JPanel {
    private TilePanel[] tilePanels;

    public RackPanel(TilePanel[] tilePanels){
        this.tilePanels = tilePanels;
        this.setLayout(new GridLayout(1, GameScreen.RACKSIZE, 10, 0));
        for (TilePanel tp : this.tilePanels) {
            this.add(tp);
        }
    }

    public TilePanel[] getTilePanels() {
        return tilePanels;
    }
}
