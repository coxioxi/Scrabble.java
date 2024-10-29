package scrabble.network.client;

/*
client side
usage: 	javac scrabble/network/client/ClientMessenger.java
		java scrabble/network/client/ClientMessenger [host_address] [port]
		../host/PartyHost acts as server and prints out local IP for host address
		David: cd "OneDrive - Otterbein University\IdeaProjects\Scrabble\code"
 */
import scrabble.model.Tile;
import scrabble.network.messages.*;

import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

/**
 * This class is responsible for sending and receiving messages from the host
 * after a socket has already been established.
 * ClientMessenger uses PropertyChangeSupport to notify the controller class
 * when new messages are received.
 */
public class ClientMessenger implements Runnable {
	// Use ObjectOutputStream and ObjectInputStream for sending and receiving messages
	// Pass in a socket to the host, get input and output streams from socket,
	// then create OOS and OIS.

	private Socket server;

	private ObjectOutputStream outputStream;
	private ObjectInputStream inputStream;

	private PropertyChangeSupport notifier;
	private boolean isListening;

	public ClientMessenger(Socket server, PropertyChangeListener listener)
			throws IOException {
		this.server = server;
		outputStream = new ObjectOutputStream(server.getOutputStream());
		inputStream = new ObjectInputStream(server.getInputStream());
		notifier = new PropertyChangeSupport(this);
		notifier.addPropertyChangeListener(listener);
		isListening = false;
	}

	@Override
	public void run() {
		isListening = true;
		Object object = null;

		do {
				try {
					object = inputStream.readObject();
					Message message = (Message) object;
					printInstance(message);
				} catch (EOFException e) {
					System.out.println("Eof found");
					this.halt();
				}
				catch (SocketException e) {
					// The host has closed their connection
					// TODO: pass on a message to the controller that host has closed their socket
					// 	i.e. end the game
					//newMessage = new ExitParty();
					this.halt();
				}
				catch (IOException | ClassNotFoundException e) {
					throw new RuntimeException(e);
				}

			if (object != null) {
				notifier.firePropertyChange("Message", null, object);
			}
			object = null;
		} while (isListening);
		try {
			closeStreams();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void sendMessage(Message message) throws IOException {
		outputStream.writeObject(message);
		outputStream.flush();
	}

	public void halt() {
		isListening = false;
	}

	private void closeStreams() throws IOException {
		inputStream.close();
		outputStream.flush();
		outputStream.close();
		if (!server.isClosed()) server.close();
	}

	public static void printInstance(Message message ) {
		if (message instanceof Challenge) {
			System.out.println("Challenge");
		} else if (message instanceof ExchangeTiles) {
			System.out.println("Exchange");
		} else if (message instanceof ExitParty) {
			System.out.println("Exit");
		} else if (message instanceof NewTiles) {
			System.out.println("NewTiles");
		} else if (message instanceof PassTurn) {
			System.out.println("Pass");
		} else if (message instanceof PlayTiles) {
			System.out.println("PlayTiles");
		}
	}

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
			printInstance((Message) evt.getNewValue());
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
