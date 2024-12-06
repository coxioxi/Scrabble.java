package scrabble.view;
/*
 * Authors: Ian Boyer, David Carr, Samuel Costa,
 * Maximus Latkovski, Jy'el Mason
 * Course: COMP 3100
 * Instructor: Dr. Barry Wittman
 * Original date: 10/08/2024
 */

import scrabble.model.Tile;

import javax.swing.*;
import java.awt.*;


/**
 * A JButton representing a tile in the Scrabble game, displaying both the letter and its score.
 */
public class TileButton extends JButton {
    private static final Color BACKGROUND_COLOR = new Color(220, 220, 220);
    private static final Color ENABLED_TEXT_COLOR = Color.BLACK;
    private static final Color DISABLED_TEXT_COLOR = Color.GRAY;

    private Tile tile;
    private Tile.TileScore letterScore;
    private final Font SCORE_FONT = getFont().deriveFont(7f); // The font used for displaying the score.
    private final Font LETTER_FONT = getFont().deriveFont(Font.BOLD, 12f);
    /**
     * Constructor that initializes the button with a given TileScore object.
     *
     * @param letterScore The TileScore object representing the letter and score of the tile.
     * @deprecated use {@link #TileButton(Tile)}
     */
    public TileButton(Tile.TileScore letterScore) {
        // Call the parent JButton constructor with the name of the letter.
        super(letterScore.getLetter()+"");
        System.out.println("TileButton#<init> with " + letterScore.getLetter());
        this.letterScore = letterScore;
    }

    public TileButton(Tile tile) {
        super(tile.getLetter()+"");
        this.tile = tile;
    }

    /**
     * @deprecated use {@link #TileButton(Tile)}
     */
    public TileButton() {
        super(Tile.TileScore.BLANK.getLetter()+"");
        this.letterScore = Tile.TileScore.BLANK;
    }

    /**
     * Overrides the paintComponent method to customize how the tile is painted.
     *
     * @param g The Graphics object used for drawing.
     */
    @Override
    public void paintComponent(Graphics g) {
        // default paint
        Graphics2D g2 = (Graphics2D) g.create();
        super.paintComponent(g2);

        // background
        g2.setColor(BACKGROUND_COLOR);
        g2.fillRect(0,0,getWidth(), getHeight());

        g2.setPaint(this.isEnabled() ? ENABLED_TEXT_COLOR : DISABLED_TEXT_COLOR);
        g2.setFont(LETTER_FONT);
        float x = getWidth()/2f - 2*LETTER_FONT.getSize()/5f - 1;
        float y = getHeight()/2f + 2*LETTER_FONT.getSize()/5f;
        g2.drawString(""+(tile == null ? letterScore.getLetter() : tile.getLetter()), x, y);

        g2.setFont(SCORE_FONT);

        // Draw the score near the bottom-right corner of the tile.
        g2.drawString((tile == null ? letterScore.getScore() : tile.getScore()) + "", getWidth() / 2 + 5, getHeight() / 2 + 8);
        g2.dispose();
    }

    public Tile tile() { return tile; }

    /**
     *
     * @param letter
     */
    public void setTileLetter(Tile.TileScore letter) {
        if (tile.setLetter(letter)) this.repaint();
    }

    public void setTileLocation(Point location) {
        tile.setLocation(location);
    }
}
