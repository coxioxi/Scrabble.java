package model;

/**
 * this class represents the player who is on this computer
 * it maintains the tiles which the player has access to
 * it permits plays to be made to update this rack, and plays
 * to be undone as is the case for failed challenges
 */
public class LocalPlayer extends Player {
	private Tile[] rack, 		// The tiles the player has.
			   lastPlay, 		// The tiles which were played on the previous turn
								// this field is used to undo plays for failed challenges.
			   newTiles;		// The tiles which have most recently been added to the rack;
								// these will need to be removed in the case of a failed challenge.

	/**
	 * constructs a localPlayer object from their name and ID, and
	 * sets their rack
	 * @param name the name of the player. Must be at least three characters
	 * @param ID the player's id, which corresponds to their order
	 * @param rack the tiles the player has
	 */
	public LocalPlayer(String name, int ID, Tile[] rack) {
		super(name, ID);
		this.rack = rack;
	}

	/**
	 * Provides access to the tiles contained in rack
	 * without the ability to change rack
	 * @return a cloned copy of rack, the tiles of this player
	 */
	public Tile[] getRack() {
		return rack.clone();
	}

	/**
	 * Updates the player's information after they have
	 * played specified tiles from their rack and added new ones to it
	 * @param toPlay the tiles which the player plays and no longer has.
	 *               Must be non-empty. All tiles in toPlay must have
	 *               letter values which correspond to tiles in the
	 *               player's rack
	 * @param addToRack the tiles which will be added to the rack after
	 *                  the played tiles are removed.
	 *                  this array must be the same size or smaller than toPlay.
	 */
	public void playTiles(Tile[] toPlay, Tile[] addToRack)
			throws NoTileFoundException {
		//TODO: Implement playTiles

		/*
		Implementation notes/details:
			for each tile in toPlay, get letter and is blank.
			see if a matching tile exists in rack. Remove or throw exception.
			remember to add this tile to lastPlay
			This process may be easier if rack is converted to an ArrayList,
			but this option is left to the developer

			add tiles from second array to the rack AFTER above.
			remember to add these tiles to newTiles
		 */

	}

	/**
	 * Resets the state of rack to before the most recent play.
	 * if this method is called next after playTiles,
	 * this.getRack() will contain all the same tiles
	 * before call to playTiles. Ordering is not enforced
	 */
	public void undoLastPlay() {
		//TODO: implement

		/*
		Implementation notes/details:
			Remove newTiles from rack
			add lastPlay to rack
		 */
	}

	public Tile[] getLastPlay() {
		return lastPlay;
	}
}
