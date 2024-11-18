package scrabble.network.messages;
/*
 * Authors: Ian Boyer, David Carr, Samuel Costa, Maximus Latkovski, Jy'el Mason
 * Course: COMP 3100
 * Instructor: Dr. Barry Wittman
 * Original date: 10/08/2024
 */

import scrabble.controller.Controller;
import scrabble.model.Tile;
import scrabble.network.PartyHost;
import java.io.Serial;

/**
 * This message class is responsible for sending new tiles to players and updating
 * their views
 */
public class NewTiles extends Message {
	@Serial
	private static final long serialVersionUID = 5L;
	private Tile[] tiles;

	/**
	 * Constructor for the NewTiles message class, takes the sender ID and the new tiles
	 *
	 * @param senderID The hostID
	 * @param tiles New tiles that will be sent to the player
	 */
	public NewTiles(int senderID, Tile[] tiles) {
		super(senderID);
		this.tiles = tiles;
	}

	/**
	 * Getter for the new tiles
	 *
	 * @return the new tiles array
	 */
	public Tile[] getTiles() {
		return tiles;
	}


	/**
	 * Add the new tiles to the player view
	 *
	 * @param controller the controller on which to make changes. Note that this object
	 *                   must use public getter methods for all the components
	 *                   (for example, the GUI components, the model components etc.)
	 */
	@Override
	public void execute(Controller controller) {
		controller.addTiles(tiles);
	}

	/**
	 * New tiles will never be sent to the host so this method is empty
	 *
	 * @param partyHost the <code>PartyHost</code> on which to make changes. Note that this object
	 *                   must use public getter methods for all the components which should be changed.
	 */
	@Override
	public void execute(PartyHost partyHost) {}
}
