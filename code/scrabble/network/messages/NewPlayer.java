package scrabble.network.messages;
/*
 * Authors: Ian Boyer, David Carr, Samuel Costa,
 * Maximus Latkovski, Jy'el Mason
 * Course: COMP 3100
 * Instructor: Dr. Barry Wittman
 * Original date: 10/08/2024
 */

import scrabble.controller.Controller;
import scrabble.network.host.PartyHost;

import java.io.IOException;
import java.io.Serializable;

public class NewPlayer extends Message implements Serializable {

    private String playerName;
    private int playerID;

    public NewPlayer(int senderID, int playerID, String playerName) {
        super(senderID);
        this.playerID = playerID;
        this.playerName = playerName;
    }

    public int getPlayerID() {
        return playerID;
    }

    public String getPlayerName(){return playerName;}

    @Override
    public void execute(Controller controller) {
        //Create player connection to the host
        //Add getPlayerName to controller
        //in order to add players to waiting screen and game screen
        if (controller.getHost()==null) {
            controller.getView().getWaiting().addPlayerName(this.playerName);
        }
        else {
            controller.getView().getHost().addPlayerName(this.playerName);
        }
    }

    @Override
    public void execute(PartyHost partyHost) {
        for (String name : partyHost.getPlayerNames()) {
            int selfID = partyHost.getMessagePlayerID();
            NewPlayer newPlayer = new NewPlayer(PartyHost.HOST_ID, selfID, name);
			try {
				partyHost.sendMessage(selfID, newPlayer);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
        partyHost.addPlayerName(this.playerName);
		try {
			partyHost.sendToAllButID(this.playerID, this);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
