package scrabble.view.screen.component;

import scrabble.view.frame.TileButton;

import javax.swing.*;
import java.awt.*;

/**
 * TilePanel is a JPanel component that holds a TileButton and provides
 * methods for adding and updating the button in the panel.
 */
public class TilePanel extends JPanel {
    // A JButton to represent the tile displayed in this panel
    private JButton tileButton;

    /**
     * Constructor that initializes the TilePanel with a given TileButton.
     *
     * @param tileButton the initial TileButton to be displayed in this panel
     */
    public TilePanel(TileButton tileButton) {
        // Set the layout to a flow layout to manage the placement of the button
        this.setLayout(new FlowLayout());
        this.setButton(tileButton); // Set the button in the panel
    }

    /**
     * Getter for the current TileButton in the panel.
     *
     * @return the current JButton instance
     */
    public JButton getButton() {
        return tileButton;
    }

    /**
     * Sets a new button in the panel. If a button already exists, it removes the old one
     * before adding the new button.
     *
     * @param tileButton the new JButton to be added to the panel
     */
    public void setButton(JButton tileButton) {
        // Remove the existing button if there is one
        if (this.tileButton != null) this.remove(this.tileButton);

        // Assign the new button and set its font properties
        this.tileButton = tileButton;
        this.tileButton.setFont(getFont().deriveFont(Font.BOLD, 12f));

        // Add the button to the panel and refresh the panel display
        this.add(this.tileButton);
        this.revalidate(); // Revalidates the component hierarchy
        this.repaint(); // Repaints the component to reflect changes
    }
}
