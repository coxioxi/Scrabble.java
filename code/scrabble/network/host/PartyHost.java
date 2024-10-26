package scrabble.network.host;

/*
this class is the server side
Usage: 	javac scrabble/network/host/PartyHost.java
		java scrabble/network/host/PartyHost
		Use ../controllers/NetworkController as client
David: cd "OneDrive - Otterbein University\IdeaProjects\Scrabble\code"

 */

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * PartyHost receives messages from clients (via ClientHandler) and sends them to clients
 * This class processes messages as needed depending on the type
 */
public class PartyHost implements Runnable, PropertyChangeListener {
	/*
	Some message processing is likely to be needed depending on the messages received.
	For example, when a PlayTiles message is received, the host must send a NewTiles message
	to the client who sent the PlayTiles. Host then must send PlayTiles to the other
	clients, with the new number of tiles which the original client has in their rack.

	It seems reasonable to me (david) to have helper methods for each individual type of method and
	call it using a switch or if-else-if structure based on the type of class

	For example implementation, see the class of the same name in ../networkPrototype
	 */
	@Override
	public void run() {

	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {

	}
}
