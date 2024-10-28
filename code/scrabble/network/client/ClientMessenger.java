package scrabble.network.client;

/*
client side
usage: 	javac scrabble/network/client/ClientMessenger.java
		java scrabble/network/client/ClientMessenger [host_address] [port]
		../host/PartyHost acts as server and prints out local IP for host address
		David: cd "OneDrive - Otterbein University\IdeaProjects\Scrabble\code"
 */
import scrabble.network.messages.Message;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

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
				} catch (EOFException e) {
					System.out.println("Eof found");
					inputStream.close();
					isClosed = true;
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
}
