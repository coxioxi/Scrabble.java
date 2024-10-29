package scrabble.network.host;

import scrabble.network.messages.ExitParty;
import scrabble.network.messages.Message;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

/*
this class is the server side client listening
Usage: 	javac scrabble/network/host/ClientHandler.java
		java scrabble/network/host/ClientHandler
		Use ../controllers/NetworkController as client
David: cd "OneDrive - Otterbein University\IdeaProjects\Scrabble\code"
*/

/**
 * ClientHandler is responsible for listening for new messages coming in from the clients
 * It maintains a reference to the PartyHost (PropertyChangeListener) to notify it when messages are received.
 * For example,
 * <code>
 * 		Message message = inputStream.readObject();
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
	private ObjectInputStream inputStream;	// the stream from which message objects are read
	private ObjectOutputStream outputStream;
	private int clientID;		// the ID of this player
	private boolean listening;

	public ClientHandler(Socket socket, PropertyChangeListener listener)
			throws IOException {
		this.socket = socket;
		this.inputStream = new ObjectInputStream(socket.getInputStream());
		this.outputStream = new ObjectOutputStream(socket.getOutputStream());
		notifier = new PropertyChangeSupport(this);
		notifier.addPropertyChangeListener(listener);
		listening = false;
	}


	public int getClientID() {
		return clientID;
	}

	public void setClientID(int clientID) {
		this.clientID = clientID;
	}

	@Override
	public void run() {
		// listen for objects from the stream
		// use the ObjectInputStream to get the objects.
		// send a notification to the listener via PropertyChangeSupport object
		listening = true;
		Object newMessage = null;
		while (listening) {
			try {
				newMessage = inputStream.readObject();

			} catch (EOFException e) {
				System.out.println("Eof found");
				try {
					inputStream.close();
				} catch (IOException ex) {
					throw new RuntimeException(ex);
				}
			} catch (SocketException e) {
				// The client has closed their connection
				// TODO: pass on a message to the host that this player has closed their socket
				// 	i.e. disconnect them on other ppl's models
				//newMessage = new ExitParty();
				this.halt();
			} catch (IOException | ClassNotFoundException e) {
				throw new RuntimeException(e);
			}
			notifier.firePropertyChange("message", null, newMessage);
			newMessage = null;
		}

		try {
			closeStreams();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void closeStreams() throws IOException {
		inputStream.close();
		outputStream.flush();
		outputStream.close();
		socket.close();
	}

	public void sendMessage(Message message) throws IOException {
		outputStream.writeObject(message);
		outputStream.flush();
	}

	public void halt() {
		System.out.println("Halting");
		listening = false;
	}
}
