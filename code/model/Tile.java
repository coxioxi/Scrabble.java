package model;
/*
 * Authors: Ian Boyer, David Carr, Samuel Costa,
 * Maximus Latkovski, Jy'el Mason
 * Course: COMP 3100
 * Instructor: Dr. Barry Wittman
 * Original date: 10/08/2024
 */

import java.awt.*;

/**
 * This class represents the tiles of the scrabble game
 */
public class Tile {

    private final int score;    // how many points this tile scores
    private char letter;        // the letter on the tile
    private final boolean isBlank;
    private Point location;// whether the tile is blank or not
    private boolean isNew; //determines whether the tile has already been played

    /**
     * Creates a new, blank Tile object
     * This tile has a score of 0 and an unset letter value.
     * this letter value must be set at a future point to be a true representation of scrabble
     */
    public Tile() {
        this.isBlank = true;
        score = 0;
    }

    /**
     * Creates a new Tile object from a letter
     * @param letter the letter on the face of the tile. Must be an alphabetical
     *      character A-Z. The letter is automatically scored according to the
     *      rules of Scrabble, and as outlined in class TileScore
     */
    public Tile(char letter) {
        this.letter = letter;
        score = TileScore.getScoreForLetter(letter);
        this.isBlank = false;
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

    public void setLocation(Point location){this.location = location;}
    public Point getLocation(){return location;}

}
