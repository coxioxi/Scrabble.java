package scrabble.network.messages;

import scrabble.controller.Controller;
import scrabble.model.Tile;
import scrabble.network.host.PartyHost;

import java.io.IOException;
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

	/**
	 * Constructor for the ExchangeTiles message class
	 *
	 * @param senderID The player who is sending the message
	 * @param playerID The player ID
	 * @param toExchange Tiles that are being sent to be exchanged for new tiles
	 */
	public ExchangeTiles(int senderID, int playerID, Tile[] toExchange) {
		super(senderID);
		this.playerID = playerID;
		this.toExchange = toExchange;
	}

	/**
	 * Getter for playerID
	 *
	 * @return player ID
	 */
	public int getPlayerID() {
		return playerID;
	}

	/**
	 * Getter for tiles that need to be exchanged
	 *
	 * @return Tiles to be exchanged
	 */
	public Tile[] getToExchange() {
		return toExchange;
	}


	/**
	 * Uses helper methods to exchange tiles with the host and pass the turn once tiles were exchanged
	 *
	 * @param controller the controller on which to make changes. Note that this object
	 *                   must use public getter methods for all the components
	 *                   (for example, the GUI components, the model components etc.)
	 */
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

	/**
	 * Send tiles to be exchanged to the host and a message indicating that this player
	 * exchanged tiles to the other players that are part of the party
	 *
	 * @param partyHost the <code>PartyHost</code> on which to make changes. Note that this object
	 *                   must use public getter methods for all the components which should be changed.
	 */
	@Override
	public void execute(PartyHost partyHost) {
		newTiles = partyHost.getTiles(toExchange.length);
		ExchangeTiles tiles = new ExchangeTiles(PartyHost.HOST_ID, this.playerID, toExchange);
		try{
			//send tiles to be exchanged
			partyHost.sendMessage(this.playerID, tiles);

			//send exchange message to all players but itself
			partyHost.sendToAllButID(this.playerID, this);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Remove tiles to be exchanged from this player's rack
	 *
	 * @param controller the controller on which to make changes. Note that this object
	 *                   must use public getter methods for all the components
	 *                   (for example, the GUI components, the model components etc.)
	 */
	private void selfExecute(Controller controller) {
		try {
			controller.getMessenger().sendMessage(this);
		} catch (IOException e) {
			// if an error occurs in writing to the host, the host has disconnected.
			// halt the messenger thread and notify the client that the game is over.
			controller.getMessenger().halt();
		}

		//removing tiles from the rack
		ArrayList<Tile> tiles = controller.getModel().getSelf().getRack();
		for (Tile t : toExchange) {
			tiles.remove(t);
			controller.removeRackTile(t);
		}
	}

	private void themExecute(Controller controller) {}
}
