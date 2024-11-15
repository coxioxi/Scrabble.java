package scrabble.network.host;
/*
 * Authors: Ian Boyer, David Carr, Samuel Costa,
 * Maximus Latkovski, Jy'el Mason
 * Course: COMP 3100
 * Instructor: Dr. Barry Wittman
 * Original date: 10/08/2024
 */

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

	private final PropertyChangeSupport notifier;		// notifies listener of messages received
	private final Socket socket; 	// the socket to the client
	private final ObjectInputStream inputStream;	// the stream from which message objects are read
	private final ObjectOutputStream outputStream;
	private boolean listening;	// whether we are listening for new messages from client

	/**
	 * Constructs a host-client messenger with an observer.
	 * @param socket the socket to the client. Must be non-null.
	 * @param listener the observer to which this sends updates.
	 * @throws IOException if an error occurs in getting the <code>socket</code>'s
	 * 	 			streams.
	 */
	public HostReceiver(Socket socket, PropertyChangeListener listener)
			throws IOException {
		this.socket = socket;
		this.inputStream = new ObjectInputStream(socket.getInputStream());
		this.outputStream = new ObjectOutputStream(socket.getOutputStream());
		notifier = new PropertyChangeSupport(this);
		notifier.addPropertyChangeListener(listener);
		listening = false;
		
	}

	/**
	 * Gets the socket of this class.
	 * @return the socket.
	 */
	public Socket getSocket() {
		return socket;
	}

	/**
	 * Listens for messages from the client and sends them to the observer.
	 * This method will cease execution after <code>halt</code> is called or
	 * if the client closes the socket.
	 */
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
				// Client has closed socket.
				PartyHost ph = (PartyHost) notifier.getPropertyChangeListeners()[0];
				newMessage = new ExitParty(ph.getPlayerID(this), ph.getPlayerID(this));
				this.halt();
			} catch (IOException | ClassNotFoundException e) {
				throw new RuntimeException(e);
			}
			// got a message. tell the listener
			notifier.firePropertyChange("message", null, newMessage);
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

	/**
	 * Sends a message to the client associated with this object.
	 * @param message the message to send. Must be non-null.
	 * @throws IOException when an error occurs in writing to client stream.
	 */
	public void sendMessage(Message message) throws IOException {
		outputStream.writeObject(message);
		outputStream.flush();
	}

	/**
	 * Stops the execution of the <code>run</code> method, closing the connection to the client.
	 */
	public void halt() {
//		System.out.println("Halting");
		listening = false;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;

		return (obj.getClass() == this.getClass()) &&
				socket.equals(((HostReceiver)obj).getSocket());
	}
}
