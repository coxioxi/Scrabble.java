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

}