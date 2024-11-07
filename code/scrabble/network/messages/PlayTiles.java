package scrabble.network.messages;

import scrabble.controller.Controller;
import scrabble.model.Tile;
import scrabble.network.host.PartyHost;
import scrabble.view.screen.GameScreen;

import java.io.IOException;
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

		if(score >= 0){
			//valid play
			try {
				controller.getMessenger().sendMessage(this);
				((GameScreen)controller.getView().getGame()).disableLastPlayedTiles();
			} catch (IOException e) {
				controller.getMessenger().halt();

				//make this sout be a pop-up message for the client
				System.out.println("Host Disconnected");
			}
		}
		else{
			//not valid play
			controller.resetRack((GameScreen) controller.getView().getGame());
		}
	}

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
