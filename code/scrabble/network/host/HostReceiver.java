package scrabble.network.host;

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
public class HostReceiver implements Runnable {
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
	private boolean listening;	// whether we are listening for new messages from client

	public HostReceiver(Socket socket, PropertyChangeListener listener)
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

		// listen for new messages. stop when this is told to halt, or when socket has been closed
		listening = true;
		Message newMessage = null;
		while (listening) {
			try {
				newMessage = (Message) inputStream.readObject();
			} catch (EOFException | SocketException e) {
				// The client has closed their connection
				// TODO: pass on a message to the host that this player has closed their socket
				// 	i.e. disconnect them on other ppl's models
				//newMessage = new ExitParty();
				this.halt();
			} catch (IOException | ClassNotFoundException e) {
				throw new RuntimeException(e);
			}
			// got a message. tell the listener
			notifier.firePropertyChange("message", null, newMessage);
			newMessage = null;
		}

		// we have stopped listening. close streams
		try {
			closeStreams();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	// closes socket and associated streams
	private void closeStreams() throws IOException {
		inputStream.close();
		outputStream.flush();
		outputStream.close();
		socket.close();
	}

	// send a message to the client
	public void sendMessage(Message message) throws IOException {
		outputStream.writeObject(message);
		outputStream.flush();
	}

	// stop the run method's execution
	public void halt() {
//		System.out.println("Halting");
		listening = false;
	}
}
