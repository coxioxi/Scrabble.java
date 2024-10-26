package scrabble.network.host;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * ClientHandler is responsible for listening for new messages coming in from the clients
 * It maintains a reference to the PartyHost to notify it when messages are received.
 * For example,
 * <code>
 * 		Message message = inputStream.readUTF();
 * 		partyHost.sendMessage(message)
 * 	</code>
 * The party host is then responsible for handling the message that was received.
 * Party host creates one ClientHandler thread for each client who joins the game
 *
 */
public class ClientHandler  implements Runnable {
	/*
	The majority of the work for this class will be handled inside the run() method, with
	potentially helper methods being called. See ../networkPrototype/ClientHandler for
	an example of implementation, and ../networkPrototype/PartyHost for example of usage
	 */

	private PropertyChangeSupport notifier;		// notifies listener of messages received
	private Socket socket; 	// the socket of the client
	private ObjectInputStream input;	// the stream from which message objects are read

	public ClientHandler(Socket socket, PropertyChangeListener listener)
			throws IOException {
		this.socket = socket;
		this.input = new ObjectInputStream(socket.getInputStream());
		notifier = new PropertyChangeSupport(this);
		notifier.addPropertyChangeListener(listener);
	}


	@Override
	public void run() {

	}
}
