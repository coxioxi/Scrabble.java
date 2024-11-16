package scrabble.view.screen.component;
/*
 * Authors: Ian Boyer, David Carr, Samuel Costa,
 * Maximus Latkovski, Jy'el Mason
 * Course: COMP 3100
 * Instructor: Dr. Barry Wittman
 * Original date: 10/08/2024
 */

import scrabble.view.screen.GameScreen;

import javax.swing.*;
import java.awt.*;

/**
 * RackPanel is a JPanel that represents a rack of TilePanels,
 * displaying the tiles a player currently holds.
 */
public class RackPanel extends JPanel {
    // Array to hold the TilePanels that make up the player's rack

    private TilePanel[] tilePanels;

    /**
     * Constructor to initialize the RackPanel with an array of TilePanels.
     *
     * @param tilePanels an array of TilePanels to be displayed in the rack
     */
    public RackPanel(TilePanel[] tilePanels){
        // Assigns the given array of TilePanels to the class field
        this.tilePanels = tilePanels;

        // Sets the layout of the panel to a GridLayout with 1 row, RACK_SIZE columns,
        // and a horizontal gap of 10 pixels between components
        this.setLayout(new GridLayout(1, GameScreen.RACK_SIZE, 10, 0));

        // Adds each TilePanel to the RackPanel
        for (TilePanel tp : this.tilePanels) {
            this.add(tp);
        }
    }

    /**
     * Getter method for retrieving the TilePanels in the rack.
     *
     * @return an array of TilePanels currently displayed in the rack
     */
    public TilePanel[] getTilePanels() {
        return tilePanels;
    }

    /**
     * Replaces the button at a specific position in the rack.
     *
     * @param tileButton the new JButton to be set in the specified TilePanel
     * @param i the index of the TilePanel where the button will be set
     */
    public void setButton(JButton tileButton, int i) {
        // Sets a new button in the specified TilePanel at index i
        tilePanels[i].setButton(tileButton);

        // Revalidates the panel to ensure the new button is displayed properly
        this.revalidate();

        // Repaints the panel
        this.repaint();
    }

}
