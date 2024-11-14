package scrabble.model;

/*
 * Authors: Ian Boyer, David Carr, Samuel Costa, Maximus Latkovski, Jy'el Mason
 * Course: COMP 3100
 * Instructor: Dr. Barry Wittman
 * Original date: 10/08/2024
 */

/**
 * This class represents a player over a network.
 */
public class NetworkPlayer extends Player {

	// How many tiles the player has
	private int numTiles;

	// Whether they are connected to the host
	private boolean isConnected;

	/**
	 * Constructs a NetworkPlayer object
	 * @param name the name of the player. Must be at least 3 characters
	 * @param ID the player ID, or their order of play
	 */
	public NetworkPlayer(String name, int ID, int turnID) {
		super(name, ID, turnID);
		numTiles = 7;
		isConnected = true;
	}

	/**
	 * Getter for isConnected
	 * @return isConnected
	 */
	public boolean isConnected() {
		return isConnected;
	}

	/**
	 * Setter for isConnected
	 * @param connected the value to change isConnected to.
	 * false if they have lost connection, true otherwise
	 */
	public void setConnected(boolean connected) {
		isConnected = connected;
		if (!isConnected) this.setActive(false);
	}

	/**
	 * Getter for numTiles
	 * @return the number of tiles a player has
	 */
	public int getNumTiles() {
		return numTiles;
	}

	/**
	 * Setter for numTiles
	 * @param numTiles how many tiles the player has. must be between 0-7
	 */
	public void setNumTiles(int numTiles) {
		this.numTiles = numTiles;
	}
}
