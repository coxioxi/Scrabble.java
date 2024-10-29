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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.*;
import java.net.Socket;
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

	public ClientMessenger(Socket server, PropertyChangeListener listener)
			throws IOException {
		this.server = server;
		outputStream = new ObjectOutputStream(server.getOutputStream());
		inputStream = new ObjectInputStream(server.getInputStream());
		notifier = new PropertyChangeSupport(this);
		notifier.addPropertyChangeListener(listener);
	}


	@Override
	public void run() {
		Object object = null;
		boolean isClosed = false;
		do {
			try {
				try {
					object = inputStream.readObject();
					Message message = (Message) object;
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
				} catch (EOFException e) {
					System.out.println("Eof found");
					inputStream.close();
					isClosed = true;
				}
			} catch (StreamCorruptedException e) {
				System.out.println("Stream corrupted: " + e);
				try {
					inputStream = new ObjectInputStream(server.getInputStream());
				} catch (IOException ex) {
					throw new RuntimeException(ex);
				}
			} catch (ClassNotFoundException | IOException e) {
				throw new RuntimeException(e);
			}
			if (object instanceof Message) {
				notifier.firePropertyChange("Message", null, object);
			}
		} while (!isClosed);
	}

	public void sendMessage(Message message) throws IOException {
		outputStream.writeObject(message);
		outputStream.flush();
	}

	public void closeStreams() throws IOException {
		outputStream.flush();
		outputStream.close();
		inputStream.close();
		server.close();
	}

	public static void main(String[] args) throws IOException {
		Scanner in = new Scanner(System.in);
		System.out.print("Enter host IP: ");
		String host = in.next();
		System.out.print("Enter port: ");
		int port = in.nextInt();

		Socket socket = null;
		try {
			socket = new Socket(host, port);
		}
		catch (IOException e) {
			System.out.print("Error in socket: " + e);
			throw new RuntimeException(e);
		}


		ClientMessenger clientMessenger = new ClientMessenger(socket, evt -> {
			try {
				((ClientMessenger) evt.getSource()).sendMessage((Message)evt.getNewValue());
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		});

		Thread thread = new Thread(clientMessenger);
		thread.start();

		System.out.println("Message type (challenge, exchange, exit, newTiles, pass,\n\tplay, start): ");
		String messageType = in.next();

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
					new Tile[]{new Tile((char)tL, new Point(x, y))}));
		} else if (messageType.equalsIgnoreCase("exit")) {
			System.out.print(" sender, player: ");
			int sID, pID;
			sID = in.nextInt();
			pID = in.nextInt();
			clientMessenger.sendMessage(new ExitParty(sID, pID));
		} else if (messageType.equalsIgnoreCase("newTiles")) {
			System.out.print("Enter senderID: ");
			int sID = in.nextInt();
			System.out.println("Enter tile letter, x and y: ");
			int tL, x, y;
			tL = in.next().charAt(0);
			x = in.nextInt();
			y = in.nextInt();
			clientMessenger.sendMessage(new NewTiles(sID,
					new Tile[]{new Tile((char)tL, new Point(x, y))}));
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
					new Tile[]{new Tile((char)tL, new Point(x, y))}));
		} else if (messageType.equalsIgnoreCase("start")) {
			System.out.print("Not implemented");
		}

	}
}
