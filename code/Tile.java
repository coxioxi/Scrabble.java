/**
 * Authors: Ian Boyer, David Carr, Samuel Costa,
 *      Maximus Latkovski, Jy'el Mason
 * Course: COMP 3100
 * Instructor: Dr. Barry Wittman
 * Original date: 10/08/2024
 */

import java.awt.*;

/**
 * This class represents the tiles of the scrabble game
 */
public class Tile {

    private final int score;
    private char letter;
    private boolean isBlank;

    public Tile(char letter, boolean isBlank){
        this.isBlank = isBlank;
        if (!this.isBlank){
            this.letter = letter;
            score = TileScore.getScoreForLetter(letter);
        }
        else {
            score = 0;
        }
    }

    public void setLetter(char letter)
        throws NotBlankException{
        if (!isBlank) {
            throw new NotBlankException("Tile already has value " + this.letter);
        }
        else this.letter = letter;
    }

    public char getLetter() {
        return this.letter;
    }
    public int getScore() {
        return this.score;
    }
}
