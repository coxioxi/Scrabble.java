package scrabble.network.client;

import scrabble.model.Tile;
import scrabble.network.host.PartyHost;
import scrabble.network.messages.*;

import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

/**
 * This class implements communication with a host, using the objects in
 * the {@link scrabble.network.messages messages package}.
 * <p>
 *     A thread of this class continuously listens for message objects
 *     from the host which are then sent to the PropertyChangeListener
 *     passed in at construction. A client of this class can send messages to
 *     the host by calling {@link #sendMessage}. When this thread is no longer needed,
 *     calling the method {@link #halt} signals run to cease execution and close the socket.
 * </p>
 */
public class ClientMessenger implements Runnable {

	private final Socket server;	// socket to server/host

	private final ObjectOutputStream outputStream;		// stream for sending objects to host
	private final ObjectInputStream inputStream;		// stream for receiving objects from host

	private final PropertyChangeSupport notifier;		// notifies listener of property changes
														// that is, when messages are received
	private boolean isListening;		// whether this object is listening for new messages

	/**
	 * Constructs a client-host messenger with an observer.
	 * @param server the socket to the server. This value may not be null.
	 * @param listener the observer which receives updates on new messages.
	 * @throws IOException if an error occurs in getting the <code>server</code>'s
	 * streams.
	 */
	public ClientMessenger(Socket server, PropertyChangeListener listener)
			throws IOException {
		// setting up streams
		this.server = server;
		outputStream = new ObjectOutputStream(server.getOutputStream());
		inputStream = new ObjectInputStream(server.getInputStream());
		notifier = new PropertyChangeSupport(this);
		notifier.addPropertyChangeListener(listener);
		isListening = false;
	}

	/**
	 * Listens for messages from the host and sends them to the observer.
	 * This method will cease execution after <code>halt</code> is called or
	 * if the host closes the socket.
	 */
	public void run() {
		// here we start listening for new messages to come in
		isListening = true;
		// container for new message.
		Message message = null;
		do {
			try {
				message = (Message) inputStream.readObject();
				System.out.print("clientMessenger received:  ");
				Message.printInstance(message);
			}
			catch (SocketException | EOFException e) {
				// Thrown when the host has closed their connection
				message = new ExitParty(PartyHost.HOST_ID, PartyHost.HOST_ID);
				this.halt();
			}
			catch (IOException | ClassNotFoundException e) {
				throw new RuntimeException(e);
			}

			// we have received a message; tell listener so they can do stuff with it
			if (message != null) {
				notifier.firePropertyChange("Message", null, message);
			}
		} while (isListening);

		/*
		 this has stopped listening for new messages, perhaps because we have disconnected,
		 perhaps the host has disconnected. either will have been handled by the controller
		 In the former case, the controller would have sent a disconnect signal; in the latter, this
		 will have notified the controller (see socketException catch clause)

		 now, simply close the streams and end execution of this run().
		 */
		try {
			closeStreams();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Sends a message to the host.
	 * @param message the message to be sent. Must be non-null.
	 * @throws IOException if an error occurs in output stream.
	 */
	public void sendMessage(Message message) throws IOException {
		outputStream.writeObject(message);
		outputStream.flush();
	}

	public Message getMessage(){
		return (new PassTurn(1,1)); // thew in to run NetworkTest.java
	}

	/**
	 * Ceases execution of <code>run()</code>.
	 */
	public void halt() {
		isListening = false;
	}

	// close socket and associated streams
	private void closeStreams() throws IOException {
		inputStream.close();
		outputStream.flush();
		outputStream.close();
		if (!server.isClosed()) server.close();
	}

	// driver for sending and receiving messages.
	public static void main(String[] args) throws IOException {
		Scanner in = new Scanner(System.in);
		System.out.print("Enter host IP: ");
		String host = in.next();
		System.out.print("Enter port: ");
		int port = in.nextInt();

		Socket socket;
		try {
			socket = new Socket(host, port);
		}
		catch (IOException e) {
			System.out.print("Error in socket: " + e);
			throw new RuntimeException(e);
		}

		ClientMessenger clientMessenger = new ClientMessenger(socket, evt -> {
			Message.printInstance((Message) evt.getNewValue());
		});

		Thread thread = new Thread(clientMessenger);
		thread.start();

		String messageType;
		do {
			System.out.println("Message type (challenge, exchange, exit, newTiles, pass,\n\tplay, start): ");
			messageType = in.next();

			if (messageType.equalsIgnoreCase("challenge")) {
				System.out.print("Enter senderID, challengingPlayerID, challengedPlayerID: ");
				int sID, cpID, cdpID;
				sID = in.nextInt();
				cpID = in.nextInt();
				cdpID = in.nextInt();
				clientMessenger.sendMessage(new Challenge(sID, cpID, cdpID));
			} else if (messageType.equalsIgnoreCase("exchange")) {
				System.out.print("Enter senderID, playerID: ");
				int sID, pID;
				sID = in.nextInt();
				pID = in.nextInt();
				System.out.println("Enter tile letter, x and y: ");
				int tL, x, y;
				tL = in.next().charAt(0);
				x = in.nextInt();
				y = in.nextInt();
				clientMessenger.sendMessage(new ExchangeTiles(sID, pID,
						new Tile[]{new Tile((char) tL, new Point(x, y))}));
			} else if (messageType.equalsIgnoreCase("exit") || messageType.equalsIgnoreCase("quit")) {
				System.out.print(" sender, player: ");
				int sID, pID;
				sID = in.nextInt();
				pID = in.nextInt();
				clientMessenger.sendMessage(new ExitParty(sID, pID));
				clientMessenger.halt();
			} else if (messageType.equalsIgnoreCase("newTiles")) {
				System.out.print("Enter senderID: ");
				int sID = in.nextInt();
				System.out.println("Enter tile letter, x and y: ");
				int tL, x, y;
				tL = in.next().charAt(0);
				x = in.nextInt();
				y = in.nextInt();
				clientMessenger.sendMessage(new NewTiles(sID,
						new Tile[]{new Tile((char) tL, new Point(x, y))}));
			} else if (messageType.equalsIgnoreCase("pass")) {
				System.out.print(" sender, player: ");
				int sID, pID;
				sID = in.nextInt();
				pID = in.nextInt();
				clientMessenger.sendMessage(new PassTurn(sID, pID));
			} else if (messageType.equalsIgnoreCase("play")) {
				System.out.print("Enter senderID, playerID: ");
				int sID, pID;
				sID = in.nextInt();
				pID = in.nextInt();
				System.out.println("Enter tile letter, x and y: ");
				int tL, x, y;
				tL = in.next().charAt(0);
				x = in.nextInt();
				y = in.nextInt();
				clientMessenger.sendMessage(new PlayTiles(sID, pID,
						new Tile[]{new Tile((char) tL, new Point(x, y))}));
			} else if (messageType.equalsIgnoreCase("start")) {
				System.out.print("Not implemented");
			}
		} while (!messageType.equalsIgnoreCase("quit") && !messageType.equalsIgnoreCase("exit"));
	}
}
