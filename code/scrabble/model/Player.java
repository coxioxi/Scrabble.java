package scrabble.model;
/*
 * Authors: Ian Boyer, David Carr, Samuel Costa, Maximus Latkovski, Jy'el Mason
 * Course: COMP 3100
 * Instructor: Dr. Barry Wittman
 * Original date: 10/08/2024
 */

import java.util.ArrayList;

/**
 * Generalized representation of a Player.
 * Players have a name, and ID, a score, and fields for
 * ability to make plays and whether they passed their previous turn.
 * Clients can change the score of the player and set their
 * activity status and their behavior on their previous turn.
 */
public class Player {
	public static int DEFAULT_SCORE = 0;  // Default starting score for players

	private final String name;	// Player's name
	private int score;			// Player's score
	private final int ID;
	private int turnID;			// Player's ID, their turn in play
	/*
		passedLastTurn and isActive are for the requirements on turn passing.
		If a player passes two consecutive turns, they will become
		inactive, not able to make plays on the board.
	 */
	private boolean passedLastTurn;	// Did they pass their last turn?
	private boolean isActive;		// Can they make plays on the board?

	/**
	 * Constructs a Player object
	 * @param name the name of the player. Must be at least three characters
	 * @param ID the id of the player, their order in play
	 */
	public Player(String name, int ID, int turnID) {
		this.score = DEFAULT_SCORE; // Initializes score to default
		this.name = name;			// Set the player's name
		this.ID = ID;				// Assign the player's ID
		this.turnID = turnID;
		passedLastTurn = false;	// Initialize passed last turn status
		isActive = true;			// Set player as active by default
	}

	/**
	 * Getter for player name
	 * @return name of player as String
	 */
	public String getPlayerName() {
		return name;
	}

	/**
	 * Getter for the player's current score.
	 * @return the player's score as an integer.
	 */
	public int getScore() {
		return score;
	}

	/**
	 * Increases the player's score by a specified amount.
	 * note that score may be decreased by passing a negative number
	 * no limits are imposed on the score field. Use this method responsibly
	 * @param amount the amount by which to increase this player's
	 *               score. Must be between the negative and positive
	 *               limits of int.
	 */
	public void increaseScore(int amount) {
		this.score += amount;
	}

	/**
	 * Getter for the player's unique ID.
	 * @return the player's ID as an integer.
	 */
	public int getID() {
		return ID;
	}

	/**
	 * Setter for the player's unique turnID.
	 */
	public void setTurnID(int turnID) {
		this.turnID = turnID;
	}

	/**
	 * Getter for the player's unique turnID.
	 * @return the player's turnID as an integer.
	 */
	public int getTurnID() {
		return turnID;
	}

	/**
	 * getter for hasPassedLastTurn.
	 * @return boolean value of the field
	 */
	public boolean passedLastTurn() {
		return passedLastTurn;
	}

	/**
	 * changes the value of hasPassedLastTurn.
	 * @param passedLastTurn the new value of this object's field
	 */
	public void setPassedLastTurn(boolean passedLastTurn) {
		this.passedLastTurn = passedLastTurn;
	}

	/**
	 * getter for isActive, the ability of the player to make plays
	 * @return the boolean value of isActive. True if they can make plays,
	 * false otherwise
	 */
	public boolean isActive() {
		return isActive;
	}

	/**
	 * setter for isActive, the ability of the player to make plays
	 * @param active the new value of this object's field
	 */
	public void setActive(boolean active) {
		isActive = active;
	}

	public static class LocalPlayer extends Player{
		/**
		 * this class represents the player who is on this computer
		 * it maintains the tiles which the player has access to
		 * it permits plays to be made to update this rack, and plays
		 * to be undone as is the case for failed challenges
		 */
			// The tiles the player has
			private ArrayList<Tile> rack;

			/* The tiles which were played on the previous turn.
            this field is used to undo plays for failed challenges.
            The tiles which have most recently been added to the rack;
            these will need to be removed in the case of a failed challenge.
             */
			private Tile[] lastPlay;

			/**
			 * Constructs a localPlayer object
			 * @param name the name of the player. Must be at least three characters
			 * @param ID the player's id, which corresponds to their order
			 * @param rack the tiles the player has
			 */
			public LocalPlayer(String name, int ID, int turnID, ArrayList<Tile> rack){
				// Call the constructor of the superclass Player
				super(name, ID, turnID);

				// Initialize the player's rack with the given tiles
				this.rack = rack;
			}

			/**
			 * Constructs a LocalPlayer object with a specified name and ID.
			 * The player's rack will be initialized to null.
			 * @param name the name of the player. Must be at least three characters
			 * @param ID the player's ID, which corresponds to their order of play.
			 */
			public LocalPlayer(String name, int ID, int turnID) {
				super(name, ID, turnID);
			}

			/**
			 * Getter for the player's rack of tiles.
			 * @return an ArrayList of tiles currently held by the player.
			 */
			public ArrayList<Tile> getRack() {
				return rack;
			}

			/**
			 * Removes specified tiles from the player's rack.
			 * @param tiles the tiles to remove from the rack.
			 * This method will need to be implemented to update the rack accordingly.
			 */
			public void removeTiles(Tile[] tiles) {
				for (Tile tile : tiles) {
					if (rack.contains(tile)){
						rack.remove((tile));
					}
				}

			}

			/**
			 * Adds specified tiles to the player's rack.
			 * @param tiles the tiles to add to the rack.
			 * This method will need to be implemented to update the rack accordingly.
			 */
			public void addTiles(Tile[] tiles) {
				for(Tile tile: tiles){
					if(rack.size() <= 7){
						rack.add(tile);
					}
				}
			}

			/**
			 * getter for tiles last played
			 * @return tiles played from the previous turn
			 */
			public Tile[] getLastPlay() {
				return lastPlay;
			}
	}
	/**
	 * This class represents a player over a network.
	 */
	public static class NetworkPlayer extends Player{


		// How many tiles the player has
		private int numTiles;

		// Whether they are connected to the host
		private boolean isConnected;

		/**
		 * Constructs a NetworkPlayer object
		 * @param name the name of the player. Must be at least 3 characters
		 * @param ID the player ID, or their order of play
		 */
		public NetworkPlayer(String name, int ID, int turnID) {
			super(name, ID, turnID);
			numTiles = 7;
			isConnected = true;
		}

		/**
		 * Getter for isConnected
		 * @return isConnected
		 */
		public boolean isConnected() {
			return isConnected;
		}

		/**
		 * Setter for isConnected
		 * @param connected the value to change isConnected to.
		 * false if they have lost connection, true otherwise
		 */
		public void setConnected(boolean connected) {
			isConnected = connected;
			if (!isConnected) this.setActive(false);
		}

		/**
		 * Getter for numTiles
		 * @return the number of tiles a player has
		 */
		public int getNumTiles() {
			return numTiles;
		}

		/**
		 * Setter for numTiles
		 * @param numTiles how many tiles the player has. must be between 0-7
		 */
		public void setNumTiles(int numTiles) {
			this.numTiles = numTiles;
		}
	}

}
