package scrabble.network.host;

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
	potentially helper methods being called.
	 */
	@Override
	public void run() {

	}
}
