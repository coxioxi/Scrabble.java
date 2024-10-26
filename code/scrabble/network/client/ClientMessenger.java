package scrabble.network.client;

/*
client side
usage: 	javac scrabble/network/client/ClientMessenger.java
		java scrabble/network/client/ClientMessenger [host_address] [port]
		../host/PartyHost acts as server and prints out local IP for host address
		David: cd "OneDrive - Otterbein University\IdeaProjects\Scrabble\code"
 */

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
