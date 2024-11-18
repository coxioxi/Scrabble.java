package scrabble.network.messages;
/*
 * Authors: Ian Boyer, David Carr, Samuel Costa, Maximus Latkovski, Jy'el Mason
 * Course: COMP 3100
 * Instructor: Dr. Barry Wittman
 * Original date: 10/08/2024
 */

import scrabble.controller.Controller;
import scrabble.network.PartyHost;

import java.io.IOException;
import java.io.Serializable;

/**
 * This message class is responsible for sending new player messages to the host and other players
 * in the party when a new player joins the session
 */
public class NewPlayer extends Message implements Serializable {

    private String playerName;
    private int playerID;


    /**
     * Constructor for the newPlayer message class
     * Initializes senderID, playerID and name of the player who is joining the session
     *
     * @param senderID The host ID
     * @param playerID The player who is joining
     * @param playerName The name of the player who is joining
     */
    public NewPlayer(int senderID, int playerID, String playerName) {
        super(senderID);
        this.playerID = playerID;
        this.playerName = playerName;
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
     * This execute adds the name of the new player being added to the waiting screen and
     * to the host screen so other players know who joined
     *
     * @param controller the controller on which to make changes. Note that this object
     *                   must use public getter methods for all the components
     *                   (for example, the GUI components, the model components etc.)
     */
    @Override
    public void execute(Controller controller) {
        //Create player connection to the host
        //Add getPlayerName to controller in order to add players to waiting screen and game screen
        if (controller.getHost()==null) {
            controller.getView().getWaiting().addPlayerName(this.playerName);
        }
        else {
            controller.getView().getHost().addPlayerName(this.playerName);
        }
    }

    /**
     * Creates a connection to the host by creating a new player message and sending it to the host.
     * It also sends it to all players other than the one who sent the message to the host so all clients can update
     * their views
     *
     * @param partyHost the <code>PartyHost</code> on which to make changes. Note that this object
     *                   must use public getter methods for all the components which should be changed.
     */
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
