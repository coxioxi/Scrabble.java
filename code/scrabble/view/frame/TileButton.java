package scrabble.view.frame;
/*
 * Authors: Ian Boyer, David Carr, Samuel Costa,
 * Maximus Latkovski, Jy'el Mason
 * Course: COMP 3100
 * Instructor: Dr. Barry Wittman
 * Original date: 10/08/2024
 */

import scrabble.model.TileScore;

import javax.swing.*;
import java.awt.*;

/**
 * A JButton representing a tile in the Scrabble game, displaying both the letter and its score.
 */
public class TileButton extends JButton {
    private TileScore letterScore; // The score and letter associated with this tile.
    private final Font scoreFont; // The font used for displaying the score.

    /**
     * Constructor that initializes the button with a given TileScore object.
     *
     * @param letterScore The TileScore object representing the letter and score of the tile.
     */
    public TileButton(TileScore letterScore) {
        // Call the parent JButton constructor with the name of the letter.
        super(letterScore.name());
        this.letterScore = letterScore;

        // Set the font size for the score and main button text.
        scoreFont = getFont().deriveFont(8f);
        setFont(getFont().deriveFont(Font.BOLD, 12f));
    }

    public TileButton() {
        super();
        scoreFont = getFont().deriveFont(8f);
        setFont(getFont().deriveFont(Font.BOLD, 12f));
    }

    /**
     * Overrides the paintComponent method to customize how the tile is painted.
     *
     * @param g The Graphics object used for drawing.
     */
    @Override
    public void paintComponent(Graphics g) {
        // Create a copy of the Graphics object for drawing.
        Graphics2D g2 = (Graphics2D) g.create();

        // Call the superclass paintComponent method to handle standard painting.
        super.paintComponent(g);

        // Set the color and font for drawing the score on the tile.
        g2.setPaint(Color.BLACK);
        g2.setFont(scoreFont);

        // Draw the score near the bottom-right corner of the tile.
        if (this.letterScore != null) {
            g2.drawString(letterScore.getScore() + "", getWidth() / 2 + 8, getHeight() / 2 + 8);
        }
        else {
            g2.drawString("_", getWidth() / 2 + 8, getHeight() / 2 + 8);
        }
        // Dispose of the Graphics object to free resources.
        g2.dispose();
    }
}
