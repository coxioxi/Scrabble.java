package scrabble.network.messages;

import scrabble.controller.Controller;
import scrabble.network.host.PartyHost;

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

        //controller.addNewPlayer(playerID,playerName)
    }

    @Override
    public void execute(PartyHost partyHost) {
        //no need to send it to the host just send the player to the other players from the host
    }
}
