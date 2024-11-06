package scrabble.view.panel;

import scrabble.view.frame.TileButton;

import javax.swing.*;
import java.awt.*;

public class TilePanel extends JPanel {
    private JButton tileButton;

    public TilePanel(TileButton tileButton) {
        this.setLayout(new FlowLayout());
        this.setButton(tileButton);
    }

    public JButton getButton() {
        return tileButton;
    }

    public void setButton(JButton tileButton) {
        if (this.tileButton != null) this.remove(this.tileButton);
        this.tileButton = tileButton;
        this.tileButton.setFont(getFont().deriveFont(Font.BOLD, 12f));
        this.add(this.tileButton);
        this.revalidate();
        this.repaint();
    }
}
