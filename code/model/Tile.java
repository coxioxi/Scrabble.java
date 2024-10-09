package model; /**
 * Authors: Ian Boyer, David Carr, Samuel Costa,
 *      Maximus Latkovski, Jy'el Mason
 * Course: COMP 3100
 * Instructor: Dr. Barry Wittman
 * Original date: 10/08/2024
 */

/**
 * This class represents the tiles of the scrabble game
 */
public class Tile {

    private final int score;    // how many points this tile scores
    private char letter;        // the letter on the tile
    private final boolean isBlank;    // whether the tile is blank or not

    /**
     * Creates a new model.Tile object
     * @param letter the letter on the face of the tile. Must be an alphabetical
     *               character A-Z, unless it is blank.
     * @param isBlank whether this tile is blank or not.
     */
    public Tile(char letter, boolean isBlank) {
        this.isBlank = isBlank;
        if (!this.isBlank){
            this.letter = letter;
            score = TileScore.getScoreForLetter(letter);
        }
        else {
            score = 0;
        }
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
}
