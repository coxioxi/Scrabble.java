package scrabble.network.messages;

import scrabble.model.Player;
import scrabble.model.Ruleset;
import scrabble.model.Tile;

public class StartGame extends Message {
	//TODO: add methods like the ones in playTiles and NewTiles
	private Player[] players;
	private Ruleset ruleset;
	private Tile[] startingTiles;
	/*
	others??
	 */

	public StartGame(int senderID, Player[] players, Ruleset ruleset, Tile[] startingTiles) {
		super(senderID);
		this.players = players;
		this.ruleset = ruleset;
		this.startingTiles = startingTiles;
	}

	public Player[] getPlayers() {
		return players;
	}

	public Ruleset getRuleset() {
		return ruleset;
	}

	public Tile[] getStartingTiles() {
		return startingTiles;
	}
}
