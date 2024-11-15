package scrabble.network.messages;
/*
 * Authors: Ian Boyer, David Carr, Samuel Costa,
 * Maximus Latkovski, Jy'el Mason
 * Course: COMP 3100
 * Instructor: Dr. Barry Wittman
 * Original date: 10/08/2024
 */

import scrabble.controller.Controller;
import scrabble.model.Tile;
import scrabble.network.host.PartyHost;

import java.io.Serial;

public class NewTiles extends Message {
	@Serial
	private static final long serialVersionUID = 5L;
	private Tile[] tiles;

	public NewTiles(int senderID, Tile[] tiles) {
		super(senderID);
		this.tiles = tiles;
	}

	public Tile[] getTiles() {
		return tiles;
	}

	@Override
	public void execute(Controller controller) {
		controller.addTiles(tiles);
	}

	@Override
	public void execute(PartyHost partyHost) {}
}
