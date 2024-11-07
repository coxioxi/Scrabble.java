package scrabble.network.messages;

import scrabble.controller.Controller;
import scrabble.model.Ruleset;
import scrabble.model.Tile;
import scrabble.network.host.PartyHost;

public class StartGame extends Message {
	private int[] playerIDs;
	private int receivingID;
	private Ruleset ruleset;
	private Tile[] startingTiles;

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

		controller.getModel().setRuleset(ruleset);
		controller.getView().setupGameScreen(controller.getModel().getRuleset(), controller.getModel().getPlayers(), controller.getModel().getPlayers().length);

	}

	@Override
	public void execute(PartyHost partyHost) {
		// do nothing. host will not receive this message. ever.
	}
}
