package scrabble.network.messages;

import scrabble.controller.Controller;
import scrabble.model.Tile;
import scrabble.network.host.PartyHost;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;

public class PlayTiles extends Message {

	@Serial
	private static final long serialVersionUID = 7L;
	private int playerID;
	private Tile[] tiles;

	public PlayTiles(int senderID, int playerID, Tile[] tiles) {
		super(senderID);
		this.playerID = playerID;
		this.tiles = tiles;
	}

	public int getPlayerID() {
		return playerID;
	}

	public Tile[] getTiles() {
		return tiles;
	}

	@Override
	public void execute(Controller controller) {
		//how to update view to show score
		//if play was valid we'll have a positive number for the score, otherwise we get -1 for the score
		//if we get a -1 reset the most recently placed tiles using playedTiles in gameScreen
		//if valid we update the score in the GUI and then send the message to the host
		int score = controller.getModel().playTiles(playerID,tiles);

		if(score != -1){
			//valid play
			try {
				controller.getMessenger().sendMessage(this);
			} catch (IOException e) {
				controller.getMessenger().halt();

				//make this sout be a pop-up message for the client
				System.out.println("Host Disconnected");
			}
		}
		else{
			//not valid play
			//ask david about gameScreen getter
			//controller.resetRack();
		}
	}

	@Override
	public void execute(PartyHost partyHost) {

	}
}
