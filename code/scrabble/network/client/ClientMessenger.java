package scrabble.network.client;

/**
 * This class is responsible for sending and receiving messages from the host
 * after a socket has already been established.
 * ClientMessenger uses PropertyChangeSupport to notify the controller class
 * when new messages are received.
 */
public class ClientMessenger {
	// Use ObjectOutputStream and ObjectInputStream for sending and receiving messages
	// Pass in a socket to the host, get input and output streams from socket,
	// then create OOS and OIS.
}
