package scrabble.network.messages;

import scrabble.controller.Controller;
import scrabble.model.NotBlankException;
import scrabble.model.Tile;
import scrabble.network.host.PartyHost;
import scrabble.view.panel.GameScreen;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;
import java.util.ArrayList;

/**
 * Message sent when a player chooses to exchange one or all tiles from their rack.
 * <p>
 *     This class contains the ID of the sending player, the requesting player,
 *     and the tiles to be exchanged. The <code>execute(Controller)</code> sends this
 *     message to the host when
 *     <code>Controller.getModel.getSelf.getPlayerID == this.playerID</code>. On another
 *     client's application, an update to the GUI may appear indicating that the player
 *     has exchanged tiles.
 * </p>
 * <p>
 *     The <code>execute(PartyHost)</code> method generates and returns
 *     a {@link NewTiles} object to the original sender. This generation must
 *     be handled by calling the appropriate methods on <code>TileBag</code>
 *     from the <code>PartyHost</code>.
 * </p>
 */
public class ExchangeTiles extends Message{
	@Serial
	private static final long serialVersionUID = 3L;
	private final int playerID;
	private Tile[] toExchange;
	private Tile[] newTiles;

	public ExchangeTiles(int senderID, int playerID, Tile[] toExchange) {
		super(senderID);
		this.playerID = playerID;
		this.toExchange = toExchange;
	}

	public int getPlayerID() {
		return playerID;
	}

	public Tile[] getToExchange() {
		return toExchange;
	}

	@Override
	public void execute(Controller controller) {

		if (playerID == controller.getModel().getSelf().getID()) {
			selfExecute(controller);
		}
		else {
			themExecute(controller);
		}

		controller.getModel().nextTurn();
	}

	@Override
	public void execute(PartyHost partyHost) {
		newTiles = partyHost.getTiles(toExchange.length);
		ExchangeTiles tiles = new ExchangeTiles(PartyHost.HOST_ID, this.playerID, toExchange);
		try{
			partyHost.sendMessage(this.playerID, tiles);
			partyHost.sendToAllButID(this.playerID, this);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void themExecute(Controller controller) {}

	private void selfExecute(Controller controller) {
		try {
			controller.getMessenger().sendMessage(this);
		} catch (IOException e) {
			// if an error occurs in writing to the host, the host has disconnected.
			// halt the messenger thread and notify the client that the game is over.
			controller.getMessenger().halt();

			//make this sout be a pop-up message for the client
			System.out.println("Host Disconnected");
		}
		ArrayList<Tile> tiles = controller.getModel().getSelf().getRack();
		for (Tile t : toExchange) {
			tiles.remove(t);
		}
		// TODO: check implementation for GUI update!
		controller.exchangeRack((GameScreen) controller.getView().getGame(), newTiles);
	}


}
