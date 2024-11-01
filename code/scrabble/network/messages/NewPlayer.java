package scrabble.network.messages;

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
}
