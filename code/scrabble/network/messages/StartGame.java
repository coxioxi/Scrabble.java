package scrabble.network.messages;
/*
 * Authors: Ian Boyer, David Carr, Samuel Costa,
 * Maximus Latkovski, Jy'el Mason
 * Course: COMP 3100
 * Instructor: Dr. Barry Wittman
 * Original date: 10/08/2024
 */

import scrabble.controller.Controller;
import scrabble.model.Ruleset;
import scrabble.model.Tile;
import scrabble.network.PartyHost;

import java.util.*;

/**
 * This message class is responsible for sending a start game message to the clients
 */
public class StartGame extends Message {
	private int[] playerIDs;
	private int receivingID;
	private Ruleset ruleset;
	private Tile[] startingTiles;
	//turn ID, player ID, player name
	private HashMap<Integer, HashMap<Integer, String>> playerInfo;

	/**
	 * Constructor for the NewTiles message class, takes the sender ID, the IDs of the players receiving the message,
	 * a hash map with the players' information, the ruleset for this match, and the starting rack for each player
	 *
	 * @param senderID the host ID
	 * @param receivingID the client ID
	 * @param playerInfo A hash map containing the players turn ID, a hashmap containing the player ID and player name
	 * @param ruleset The set of rules that will be used for this game
	 * @param startingTiles	The starting tiles for each player
	 */
	public StartGame(int senderID, int receivingID, HashMap<Integer, HashMap<Integer, String>> playerInfo, Ruleset ruleset, Tile[] startingTiles) {
		super(senderID);
		this.receivingID = receivingID;
		this.ruleset = ruleset;
		this.startingTiles = startingTiles;
		this.playerInfo = playerInfo;
		this.playerIDs = new int[playerInfo.size()];
	}

	/**
	 * Getter for player info hash map
	 *
	 * @return player information
	 */
	public HashMap<Integer, HashMap<Integer, String>> getPlayerInfo() {
		return playerInfo;
	}

	/**
	 * This executes method starts a game with the information initialized in the constructor
	 *
	 * @param controller the controller on which to make changes. Note that this object
	 *                   must use public getter methods for all the components
	 *                   (for example, the GUI components, the model components etc.)
	 */
	@Override
	public void execute(Controller controller) {
		// from the hashmap, create the players for the model.
		// then, assign LocalPlayer based on the id that we already received
		// then, send the rules to the gameScreen via sendRules() in controller
		// then, set up the rack for the player in gameScreen
		String[] playerNames = new String[playerInfo.size()];
		for(Integer turn : getPlayerInfo().keySet()){

			Iterator<Integer> playerIdIterator = playerInfo.get(turn).keySet().iterator();
			playerIDs[turn] = playerIdIterator.next();
			playerNames[turn] = getPlayerInfo().get(turn).get(playerIDs[turn]);
		}
		controller.startGame(ruleset, playerNames, playerIDs, startingTiles);
	}

	@Override
	public void execute(PartyHost partyHost) {
		// do nothing. host will not receive this message. ever.
	}
}
