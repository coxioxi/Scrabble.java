package scrabble.network.messages;

import scrabble.controller.Controller;
import scrabble.model.Player;
import scrabble.model.Ruleset;
import scrabble.model.Tile;
import scrabble.network.host.HostReceiver;
import scrabble.network.host.PartyHost;

import java.util.HashMap;
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
		controller.getModel().addTiles(startingTiles);
		/*
		IMPLEMENTATION:
		update the GameScreen (set it up) for the controller's view, then
		switch the view over to the game screen.
		make sure we are passing in the number of players, their names, the ruleset,
		etc. then we set up the game screen
		 */

		//set ruleset from game for players: controller.getModel().setRuleset(ruleset)
		// or make it possible to instantiate new game object for players when executing message
		try {
			controller.getMessenger().sendMessage(this);
			controller.getModel().setRuleset(ruleset);
			controller.getView().setupGameScreen(ruleset, controller.getModel().getPlayers(), controller.getModel().getPlayers().length);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public void execute(PartyHost partyHost) {
		// do nothing. host will not receive this message. ever.
	}
}
