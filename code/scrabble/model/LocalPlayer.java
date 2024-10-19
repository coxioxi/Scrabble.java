package scrabble.model;

import java.util.ArrayList;

/**
 * this class represents the player who is on this computer
 */
public class LocalPlayer extends Player{
	private ArrayList<Tile> rack; 	// the tiles the player has

	/**
	 * constructs a localPlayer object
	 * @param name the name of the player. Must be at least three characters
	 * @param ID the player's id, which corresponds to their order
	 * @param rack the tiles the player has
	 */
	public LocalPlayer(String name, int ID, ArrayList<Tile> rack) {
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

	public ArrayList<Tile> getRack() {
		return rack;
	}

	public void removeTiles(Tile[] tiles) {
		for(Tile tile: tiles){
			if(rack.contains(tile)){
				rack.remove((tile));
			}
		}

	}

	public void addTiles(Tile[] tiles) {
		for(Tile tile: tiles){
			if(rack.size() <= 7){
				rack.add(tile);
			}
		}

	}
}
