package scrabble.network.messages;

import scrabble.controller.Controller;
import scrabble.model.Ruleset;
import scrabble.model.Tile;

public class StartGame extends Message {
	//TODO: add methods like the ones in playTiles and NewTiles
	private int[] playerIDs;
	private int receivingID;
	private Ruleset ruleset;
	private Tile[] startingTiles;
	/*
	others??
	 */

	public StartGame(int senderID, int receivingID, int[] playerIDs, Ruleset ruleset, Tile[] startingTiles) {
		super(senderID);
		this.receivingID = receivingID;
		this.playerIDs = playerIDs;
		this.ruleset = ruleset;
		this.startingTiles = startingTiles;
	}

	public int[] getPlayerIDs() {
		return playerIDs;
	}

	public Ruleset getRuleset() {
		return ruleset;
	}

	public Tile[] getStartingTiles() {
		return startingTiles;
	}

	public int getReceivingID() {
		return receivingID;
	}

	@Override
	public void execute(Controller controller) {

	}
}
