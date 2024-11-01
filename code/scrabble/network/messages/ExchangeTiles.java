package scrabble.network.messages;

import scrabble.controller.Controller;
import scrabble.model.NotBlankException;
import scrabble.model.Tile;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;
import java.util.ArrayList;

public class ExchangeTiles extends Message{
	@Serial
	private static final long serialVersionUID = 3L;
	private final int playerID;
	private Tile[] toExchange;

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

	private void themExecute(Controller controller) {

	}

	private void selfExecute(Controller controller) {
		try {
			controller.getMessenger().sendMessage(this);
		} catch (IOException e) {
			// if an error occurs in writing to the host, the host has disconnected.
			// halt the messenger thread and notify the client that the game is over.
		}
		ArrayList<Tile> tiles = controller.getModel().getSelf().getRack();
		for (Tile t : toExchange) {
			tiles.remove(t);
		}
		// TODO: update the GUI
	}


}
