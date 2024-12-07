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
    private final TileScore primary;
    private TileScore secondary = null;
    private Point location;         // The location of the tile on the game board

    /**
     * Creates an array of <code>Tile</code>s from the given <code>TileScore</code>.
     * The array contains the number of <code>Tile</code>s specified by {@link TileScore#getFrequency},
     * all constructed from the parameter.
     * @param letter the <code>TileScore</code> from which to construct <code>Tile</code>s.
     * @return An array consisting of <code>letter.getFrequency()</code> <code>Tile</code>s.
     * @see #Tile(TileScore)
     * @see TileScore
     */
    public static Tile[] getTiles(TileScore letter) {
        Tile[] tiles = new Tile[letter.frequency];
        for (int i = 0; i < tiles.length; i++) {
            tiles[i] = new Tile(letter);
        }
        return tiles;
    }

    /**
     * Creates a new, blank Tile object
     * This tile has a score of 0 and an unset letter value.
     * this letter value must be set at a future point to be a true representation of scrabble.
     * @deprecated Use {@link #Tile(TileScore)} with {@link TileScore#BLANK}.
     */
    public Tile() {
        this.primary = TileScore.BLANK;        // Set to true as this is a blank tile
    }

    /**
     * Creates a new Tile object from a letter
     * @param letter the letter on the face of the tile. Must be an alphabetical
     *      character A-Z. The letter is automatically scored according to the
     *      rules of Scrabble, and as outlined in class TileScore
     * @deprecated Use {@link #Tile(TileScore)}.
     */
    public Tile(char letter) {
        this.primary = this.secondary = TileScore.scoreValueOf(Character.toUpperCase(letter) + "");
    }

    /**
     * Creates a new Tile object from a letter and location.
     * @param letter the letter on the tile.
     * @param location the Point representing the tile's location on the board.
     * @deprecated Use {@link #Tile(TileScore, Point)}.
     */
    public Tile(char letter, Point location) {
        this(letter);
        this.location = location;                       // Assign the location of the tile
    }

    /**
     * Constructs a Tile from a <code>TileScore</code> enum.
     * @param ts the enum from which this <code>Tile</code> is created.
     */
    public Tile(TileScore ts) {
        if (ts != TileScore.BLANK) this.primary = this.secondary = ts;
        else this.primary = ts;
    }

    /**
     * Constructs a Tile from a <code>TileScore</code> enum at a location.
     * @param ts the enum from which this <code>Tile</code> is created.
     * @param point the point at which this tile is placed.
     */
    public Tile(TileScore ts, Point point) {
        this(ts);
        this.location = point;
    }

    /**
     * Sets the letter of a blank tile to be the letter passed in. If the value
     * of this tile has already been set, call {@link #resetLetter()} before this.
     *
     * @param ts the letter which this blank tile should be.
     * @return True if the tile's letter was successfully set, false otherwise.
     */
    public boolean setLetter(TileScore ts) {
        if (secondary != null || ts == TileScore.BLANK) return false;
        this.secondary = ts;
        return true;
    }

    public void resetLetter() {
        if (primary == TileScore.BLANK) secondary = null;
    }

    /**
     * Getter for letter
     * @return the letter on the tile
     */
    public char getLetter() {
        if (secondary == null)
            return primary.getLetter();
        else
            return secondary.getLetter();
    }

    /**
     * getter for value
     * @return the value of the letter
     */
    public int getScore() {
        return primary.getScore();
    }

    /**
     * getter for isBlank
     * @return the value of isBlank
     */
    public boolean isBlank(){return primary == TileScore.BLANK;}

    /**
     * Sets the location of the tile on the board.
     * When a tile is removed, pass in null.
     * @param location the new Point location for the tile.
     */
    public void setLocation(Point location){this.location = location;}

    /**
     * Getter for the tile's location.
     * @return the Point representing the tile's location.
     */
    public Point getLocation(){return location;}

    @Override
    public boolean equals(Object obj) {return obj.getClass() == this.getClass() && this.toString().equals(obj.toString());}

    @Override
    public String toString() {
        return "Tile{" +
                "score=" + primary.getScore() +
                ", letter=" + (secondary != null ? secondary.getLetter() : primary.getLetter()) +
                ", isBlank=" + this.isBlank() +
                ", location=" + location +
                '}'; // Return a string representation of the Tile object
    }

    /**
     * An enumeration of the values of the tiles in Scrabble. Each enum consists of a score, an associated letter,
     * and a frequency.
     */
    public enum TileScore {
        /** The letter A, worth 1 point. */ A(1, 'A', 9), /** The letter B, worth 3 points. */ B(3, 'B', 2),
        /** The letter C, worth 3 points. */ C(3, 'C', 2), /** The letter D, worth 2 points. */ D(2, 'D', 4),
        /** The letter E, worth 1 point. */ E(1, 'E',12), /** The letter F, worth 4 points. */ F(4, 'F',2),
        /** The letter G, worth 2 points. */ G(2, 'G',3), /** The letter H, worth 4 points. */ H(4, 'H',2),
        /** The letter I, worth 1 point. */ I(1, 'I',9), /** The letter J, worth 8 points. */ J(8, 'J',1),
        /** The letter K, worth 5 points. */ K(5, 'K', 1), /** The letter L, worth 1 point. */ L(1, 'L', 4),
        /** The letter M, worth 3 points. */ M(3, 'M', 2), /** The letter N, worth 1 point. */ N(1, 'N', 6),
        /** The letter O, worth 1 point. */ O(1, 'O', 8), /** The letter P, worth 3 points. */ P(3, 'P', 2),
        /** The letter Q, worth 10 points. */ Q(10, 'Q', 1), /** The letter R, worth 1 point. */ R(1, 'R', 6),
        /** The letter S, worth 1 point. */ S(1, 'S', 4), /** The letter T, worth 1 point. */ T(1, 'T', 6),
        /** The letter U, worth 1 point. */ U(1, 'U', 4), /** The letter V, worth 4 points. */ V(4, 'V', 2),
        /** The letter W, worth 4 points. */ W(4, 'W', 2), /** The letter X, worth 8 points. */ X(8, 'X', 1),
        /** The letter Y, worth 4 points. */ Y(4, 'Y', 2), /** The letter Z, worth 10 points. */ Z(10, 'Z', 1),
        /** A blank letter, worth 0 points. */ BLANK(0, '_', 2);

        private final int score;	// The value of the letter
        private final char letter;
        private final int frequency;

        /**
         * Creates a TileScore object with a score
         * @param score value of playing this tile
         */
        TileScore(int score, char letter, int frequency) {
            this.score = score;
            this.letter = letter;
            this.frequency = frequency;
        }

        /**
         * Gives the value of the letter
         * @return the value of the letter
         */
        public int getScore() {
            return score;
        }

        /**
         * Gives the character representing this enum.
         * @return The corresponding character.
         */
        public char getLetter() {
            return letter;
        }

        /**
         * Gives the number of occurrences this enum has at the start of the game.
         * @return The number of occurrences this tile has at the start of the game.
         */
        public int getFrequency() {
            return frequency;
        }

        /**
         * A way to get the value of a letter for any letter
         * @param letter the letter to get the value of. Must be an alphabetical letter.
         * @return the value of the letter. Between 1-10
         */
        public static int getScoreForLetter(char letter) {
            return TileScore.scoreValueOf( String.valueOf(letter).toUpperCase() ).getScore();
        }

        /**
         * Supersedes the default <code>valueOf</code> method, which does not work with
         * the letter value of <code>BLANK</code>.
         * <br>
         * Use this method over <code>valueOf</code>.
         * @param name the letter for which to get the enum constant. For alphabetical Strings,
         *             "A", "B", "C", etc. For a blank string, the letter value of <code>TileScore.BLANK</code>,
         *             or the String "BLANK".
         * @return The TileScore associated with the specified String.
         */
        public static TileScore scoreValueOf(String name) {
            return (name.equals(TileScore.BLANK.getLetter()+"") ? TileScore.BLANK : valueOf(name));
        }
    }
}