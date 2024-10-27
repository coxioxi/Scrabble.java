package scrabble.network.messages;

import scrabble.model.NotBlankException;
import scrabble.model.Tile;

import java.awt.*;
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

	@Serial
	private void writeObject(ObjectOutputStream out)
			throws IOException {
		out.defaultWriteObject();
		writeTiles(out, toExchange);
	}

	@Serial
	private void readObject(ObjectInputStream in)
			throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		toExchange = readTile(in);
	}

}
