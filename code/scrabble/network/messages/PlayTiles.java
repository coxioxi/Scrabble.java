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
import scrabble.network.PartyHost;

import java.io.IOException;
import java.io.Serial;

/**
 * This message class is responsible for sending a play tiles message to the clients and update their views
 */
public class PlayTiles extends Message {

	@Serial
	private static final long serialVersionUID = 7L;
	private final int playerID;
	private final Tile[] tiles;

	/**
	 *
	 * @param senderID The host ID
	 * @param playerID The player who is sending the message
	 * @param tiles The tiles that will be played by the player
	 */
	public PlayTiles(int senderID, int playerID, Tile[] tiles) {
		super(senderID);
		this.playerID = playerID;
		this.tiles = tiles;
	}


	/**
	 * Getter for playerID
	 *
	 * @return Int player ID
	 */
	public int getPlayerID() {
		return playerID;
	}

	/**
	 * Getter for played tiles
	 *
	 * @return Played tiles that will be played by the player
	 */
	public Tile[] getTiles() {
		return tiles;
	}

	/**
	 * Play the tiles on this client's board
	 *
	 * @param controller the controller on which to make changes. Note that this object
	 *                   must use public getter methods for all the components
	 *                   (for example, the GUI components, the model components etc.)
	 */
	@Override
	public void execute(Controller controller) {
		//how to update view to show score
		//if play was valid we'll have a positive number for the score, otherwise we get -1 for the score
		//if we get a -1 reset the most recently placed tiles using playedTiles in gameScreen
		//if valid we update the score in the GUI and then send the message to the host
		controller.playTiles(playerID,tiles);
	}

	/**
	 * This execute sends a new tiles message to the client who played tiles
	 * and sends a played tiles message to the other clients so they can update their views
	 *
	 * @param partyHost the PartyHost object on which to make changes.
	 */
	@Override
	public void execute(PartyHost partyHost) {
		//get new tiles and send it back to the client (this message playerID)
		NewTiles newTiles = new NewTiles(PartyHost.HOST_ID, partyHost.getTiles(tiles.length));
		try{
			partyHost.sendMessage(this.playerID, newTiles);
			partyHost.sendToAllButID(this.playerID, this);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
