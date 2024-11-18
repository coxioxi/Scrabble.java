package scrabble.model;
/*
 * Authors: Ian Boyer, David Carr, Samuel Costa, Maximus Latkovski, Jy'el Mason
 * Course: COMP 3100
 * Instructor: Dr. Barry Wittman
 * Original date: 10/08/2024
 */

import java.awt.*;
import java.io.Serializable;

/**
 * This class represents the tiles of the Scrabble game.
 * Each tile can either be a blank tile or contain a letter,
 * and it holds information about its score and location.
 */
public class Tile implements Serializable {

    private final int score;        // How many points this tile scores
    private char letter;            // The letter on the tile
    private final boolean isBlank;  // Whether the tile is blank or not
    private Point location;         // The location of the tile on the game board

    /**
     * Creates a new, blank Tile object
     * This tile has a score of 0 and an unset letter value.
     * this letter value must be set at a future point to be a true representation of scrabble
     */
    public Tile() {
        this.isBlank = true;        // Set to true as this is a blank tile
        score = 0;                  // Default score for a blank tile
    }

    /**
     * Creates a new Tile object from a letter
     * @param letter the letter on the face of the tile. Must be an alphabetical
     *      character A-Z. The letter is automatically scored according to the
     *      rules of Scrabble, and as outlined in class TileScore
     */
    public Tile(char letter) {
        this.letter = letter;                           // Assign the letter to the tile
        score = TileScore.getScoreForLetter(letter);    // Get the score for the letter
        this.isBlank = false;                           // This tile is not blank
    }

    /**
     * Creates a new Tile object from a letter and location.
     * @param letter the letter on the tile.
     * @param location the Point representing the tile's location on the board.
     */
    public Tile(char letter, Point location) {
        this.letter = letter;                           // Assign the letter to the tile
        score = TileScore.getScoreForLetter(letter);    // Get the score for the letter
        this.isBlank = false;                           // This tile is not blank
        this.location = location;                       // Assign the location of the tile
    }

    /**
     * If a tile is blank, its letter can be set once it is played
     * @param letter the letter to put on the tile
     * @throws NotBlankException when a non-blank letter is attempted
     *          to be set.
     */
    public void setLetter(char letter)
            throws NotBlankException{
        if (!isBlank) {
            throw new NotBlankException("model.Tile already has value " + this.letter);
        }
        else this.letter = letter;
    }

    /**
     * Getter for letter
     * @return the letter on the tile
     */
    public char getLetter() {
        return this.letter;
    }

    /**
     * getter for value
     * @return the value of the letter
     */
    public int getScore() {
        return this.score;
    }

    /**
     * getter for isBlank
     * @return the value of isBlank
     */
    public boolean isBlank(){return this.isBlank;}

    /**
     * Sets the location of the tile on the board.
     * @param location the new Point location for the tile.
     */
    public void setLocation(Point location){this.location = location;}

    /**
     * Getter for the tile's location.
     * @return the Point representing the tile's location.
     */
    public Point getLocation(){return location;}

    @Override
    public boolean equals(Object obj) {return this.toString().equals(obj.toString());}

    @Override
    public String toString() {
        return "Tile{" +
                "score=" + score +
                ", letter=" + letter +
                ", isBlank=" + isBlank +
                ", location=" + location +
                '}'; // Return a string representation of the Tile object
    }

    /**
     * An enumeration of the values of the tiles in Scrabble.
     */
    public enum TileScore {
        /** The letter A, worth 1 point. */ A(1), /** The letter B, worth 3 points. */ B(3),
        /** The letter C, worth 3 points. */ C(3), /** The letter D, worth 2 points. */ D(2),
        /** The letter E, worth 1 point. */ E(1), /** The letter F, worth 4 points. */ F(4),
        /** The letter G, worth 2 points. */ G(2), /** The letter H, worth 4 points. */ H(4),
        /** The letter I, worth 1 point. */ I(1), /** The letter J, worth 8 points. */ J(8),
        /** The letter K, worth 5 points. */ K(5), /** The letter L, worth 1 point. */ L(1),
        /** The letter M, worth 3 points. */ M(3), /** The letter N, worth 1 point. */ N(1),
        /** The letter O, worth 1 point. */ O(1), /** The letter P, worth 3 points. */ P(3),
        /** The letter Q, worth 10 points. */ Q(10), /** The letter R, worth 1 point. */ R(1),
        /** The letter S, worth 1 point. */ S(1), /** The letter T, worth 1 point. */ T(1),
        /** The letter U, worth 1 point. */ U(1), /** The letter V, worth 4 points. */ V(4),
        /** The letter W, worth 4 points. */ W(4), /** The letter X, worth 8 points. */ X(8),
        /** The letter Y, worth 4 points. */ Y(4), /** The letter Z, worth 10 point. */ Z(10),
        /** A blank letter, worth 0 points. */ BLANK(0);

        private final int score;	// The value of the letter

        /**
         * Creates a TileScore object with a score
         * @param score value of playing this tile
         */
        TileScore(int score) {
            this.score = score;
        }

        /**
         * Gives the value of the letter
         * @return the value of the letter
         */
        public int getScore() {
            return score;
        }

        /**
         * A way to get the value of a letter for any letter
         * @param letter the letter to get the value of. Must be an alphabetical letter.
         * @return the value of the letter. Between 1-10
         */
        public static int getScoreForLetter(char letter) {
            return TileScore.valueOf( String.valueOf(letter).toUpperCase() ).getScore();
        }

        /**
         * Gets the <code>TileScore</code> which corresponds to the letter passed in.
         * Use '_' for <code>BLANK</code>. Any other values must be a letter.
         *
         * @param letter the character for which to find the corresponding Enum.
         * @return the <code>TileScore</code> corresponding to the letter.
         */
        public static TileScore getTileScoreForLetter(char letter) {
            return (letter == '_' ? BLANK : TileScore.values()['A' - Character.toUpperCase(letter)]);
        }
    }
}