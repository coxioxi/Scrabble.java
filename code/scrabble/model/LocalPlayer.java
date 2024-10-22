package scrabble.model;

import java.util.ArrayList;

/**
 * This class represents the player who is on this computer
 */
public class LocalPlayer extends Player{
	private ArrayList<Tile> rack; 	// the tiles the player has

	/**
	 * Constructs a localPlayer object
	 * @param name the name of the player. Must be at least three characters
	 * @param ID the player's id, which corresponds to their order
	 * @param rack the tiles the player has
	 */
	public LocalPlayer(String name, int ID,ArrayList<Tile> rack) {
		super(name, ID); 	// Call the constructor of the superclass Player
		this.rack = rack; 	// Initialize the player's rack with the given tiles
	}

	/**
	 * Constructs a LocalPlayer object with a specified name and ID.
	 * The player's rack will be initialized to null.
	 * @param name the name of the player. Must be at least three characters
	 * @param ID the player's ID, which corresponds to their order of play.
	 */
	public LocalPlayer(String name, int ID) {
		super(name, ID);
	}

	/**
	 * Getter for the player's rack of tiles.
	 * @return an array of tiles currently held by the player.
	 */
	public ArrayList<Tile> getRack() {
		return rack;
	}

	/**
	 * Removes specified tiles from the player's rack.
	 * @param tiles the tiles to remove from the rack.
	 *              This method will need to be implemented to update the rack accordingly.
	 */
	public void removeTiles(Tile[] tiles) {
		for(Tile tile: tiles){
			if(rack.contains(tile)){
				rack.remove((tile));
			}
		}

	}

	/**
	 * Adds specified tiles to the player's rack.
	 * @param tiles the tiles to add to the rack.
	 *              This method will need to be implemented to update the rack accordingly.
	 */
	public void addTiles(Tile[] tiles) {
		for(Tile tile: tiles){
			if(rack.size() <= 7){
				rack.add(tile);
			}
		}

	}
}
