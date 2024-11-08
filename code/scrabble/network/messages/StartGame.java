package scrabble.network.messages;

import scrabble.controller.Controller;
import scrabble.model.Player;
import scrabble.model.Ruleset;
import scrabble.model.Tile;
import scrabble.network.host.HostReceiver;
import scrabble.network.host.PartyHost;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class StartGame extends Message {
	private int[] playerIDs;
	private int receivingID;
	private Ruleset ruleset;
	private Tile[] startingTiles;
	HashMap<Player, Integer> turnID = new HashMap<>();

	private HashMap<Integer, HashMap<Integer, String>> playerInfo;

	public StartGame(int senderID, int receivingID, HashMap<Integer, HashMap<Integer, String>> playerInfo, Ruleset ruleset, Tile[] startingTiles) {
		super(senderID);
		this.receivingID = receivingID;
		this.ruleset = ruleset;
		this.startingTiles = startingTiles;
		this.playerInfo = playerInfo;
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

	public HashMap<Integer, HashMap<Integer, String>> getPlayerInfo() {
		return playerInfo;
	}

	@Override
	public void execute(Controller controller) {

		// from the hashmap, create the players for the model.
		// then, assign LocalPlayer based on the id that we already received
		// then, send the rules to the gameScreen via sendRules() in controller
		// then, set up the rack for the player in gameScreen

		String[] playerNames = new String[playerInfo.size()];
		for(Integer turn: getPlayerInfo().keySet()){
			Iterator<Integer> iterator= playerInfo.get(turn).keySet().iterator();
			playerIDs[turn] = iterator.next();
			playerNames[turn] = getPlayerInfo().get(turn).get(playerIDs[turn]);
		}
		controller.startGame(ruleset, playerNames, playerIDs, startingTiles);

	}

	@Override
	public void execute(PartyHost partyHost) {
		// do nothing. host will not receive this message. ever.
	}
}
