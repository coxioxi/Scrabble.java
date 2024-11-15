package scrabble.model;

/*
 * Authors: Ian Boyer, David Carr, Samuel Costa, Maximus Latkovski, Jy'el Mason
 * Course: COMP 3100
 * Instructor: Dr. Barry Wittman
 * Original date: 10/08/2024
 */

import java.util.ArrayList;

/**
 * this class represents the player who is on this computer
 * it maintains the tiles which the player has access to
 * it permits plays to be made to update this rack, and plays
 * to be undone as is the case for failed challenges
 */
public class LocalPlayer extends Player{

	// The tiles the player has
	private ArrayList<Tile> rack;

	/* The tiles which were played on the previous turn.
	this field is used to undo plays for failed challenges.
	The tiles which have most recently been added to the rack;
	these will need to be removed in the case of a failed challenge.
	 */
	private Tile[] lastPlay;

	/**
	 * Constructs a localPlayer object
	 * @param name the name of the player. Must be at least three characters
	 * @param ID the player's id, which corresponds to their order
	 * @param rack the tiles the player has
	 */
	public LocalPlayer(String name, int ID, int turnID, ArrayList<Tile> rack) {
		// Call the constructor of the superclass Player
		super(name, ID, turnID);

		// Initialize the player's rack with the given tiles
		this.rack = rack;
	}

	/**
	 * Constructs a LocalPlayer object with a specified name and ID.
	 * The player's rack will be initialized to null.
	 * @param name the name of the player. Must be at least three characters
	 * @param ID the player's ID, which corresponds to their order of play.
	 */
	public LocalPlayer(String name, int ID, int turnID) {
		super(name, ID, turnID);
	}

	/**
	 * Getter for the player's rack of tiles.
	 * @return an ArrayList of tiles currently held by the player.
	 */
	public ArrayList<Tile> getRack() {
		return rack;
	}

	/**
	 * Removes specified tiles from the player's rack.
	 * @param tiles the tiles to remove from the rack.
	 * This method will need to be implemented to update the rack accordingly.
	 */
	public void removeTiles(Tile[] tiles) {
		for (Tile tile : tiles) {
			if (rack.contains(tile)){
				rack.remove((tile));
			}
		}

	}

	/**
	 * Adds specified tiles to the player's rack.
	 * @param tiles the tiles to add to the rack.
	 * This method will need to be implemented to update the rack accordingly.
	 */
	public void addTiles(Tile[] tiles) {
		for(Tile tile: tiles){
			if(rack.size() <= 7){
				rack.add(tile);
			}
		}
	}

	/**
	 * getter for tiles last played
	 * @return tiles played from the previous turn
	 */
	public Tile[] getLastPlay() {
		return lastPlay;
	}
}
