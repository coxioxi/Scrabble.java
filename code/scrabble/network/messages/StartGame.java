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
		try {
			controller.getMessenger().sendMessage(this);

			int[] randomNumbers = new int[controller.getModel().getPlayers().length];
			for (int i = 0; i < randomNumbers.length; i++) {
				randomNumbers[i] = i;
			}

			//shuffle randomNumbers array so the player order is randomised
			Random random = new Random();
			for (int i = 0; i < randomNumbers.length;) {
				int index = random.nextInt(randomNumbers.length);
				int temp;
				if (index != i) {
					temp = randomNumbers[index];
					randomNumbers[index] = randomNumbers[i];
					randomNumbers[i] = temp;
					++i;
				}
			}

			int i = 0;
			for(Player player: turnID.keySet()){
				turnID.replace(player, randomNumbers[i]);
				++i;
			}

			for (int j = 0; j < controller.getModel().getPlayers().length; ++i)
				controller.getModel().getPlayers()[i].setTurnID(turnID.get(controller.getModel().getPlayers()[i]));

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
