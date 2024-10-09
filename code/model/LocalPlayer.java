package model;

/**
 * this class represents the player who is on this computer
 */
public class LocalPlayer extends Player{
	private Tile[] rack; 	// the tiles the player has

	/**
	 * constructs a localPlayer object
	 * @param name the name of the player. Must be at least three characters
	 * @param ID the player's id, which corresponds to their order
	 * @param rack the tiles the player has
	 */
	public LocalPlayer(String name, int ID, Tile[] rack) {
		super(name, ID);
		this.rack = rack;
	}

	/**
	 *
	 * @param name
	 * @param ID
	 */
	public LocalPlayer(String name, int ID) {
		super(name, ID);
	}

	public Tile[] getRack() {
		return rack;
	}

	public void removeTiles(Tile[] tiles) {

	}

	public void addTiles(Tile[] tiles) {

	}
}
