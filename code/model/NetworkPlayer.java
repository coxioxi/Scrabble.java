package model;

/**
 * This class represents a player over a network.
 */
public class NetworkPlayer extends Player {
	private int numTiles;	// how many tiles the player has
	private boolean isConnected;	// whether they are connected to the host

	/**
	 * Constructs a NetworkPlayer object
	 * @param name the name of the player. Must be at least 3 characters
	 * @param ID the player ID, or their order of play
	 */
	public NetworkPlayer(String name, int ID) {
		super(name, ID);
		numTiles = 7;
		isConnected = true;
	}

	/**
	 * getter for isConnected
	 * @return isConnected
	 */
	public boolean isConnected() {
		return isConnected;
	}

	/**
	 * setter for isConnected
	 * @param connected the value to change isConnected to.
	 *                  false if they have lost connection, true otherwise
	 */
	public void setConnected(boolean connected) {
		isConnected = connected;
	}

	/**
	 * Getter for numTiles
	 * @return the number of tiles a player has
	 */
	public int getNumTiles() {
		return numTiles;
	}

	/**
	 * setter for numTiles
	 * @param numTiles how many tiles the player has. must be between 0-7
	 */
	public void setNumTiles(int numTiles) {
		this.numTiles = numTiles;
	}
}
