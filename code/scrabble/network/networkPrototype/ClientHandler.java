package scrabble.network.networkPrototype;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Objects;

public class ClientHandler implements Runnable{

	private Socket clientSocket;
	private DataInputStream inputStream;
	private PartyHost partyHost;

	public ClientHandler(Socket clientSocket, PartyHost partyHost)
			throws IOException {
		this.partyHost = partyHost;
		this.clientSocket = clientSocket;
		this.inputStream = new DataInputStream(
				new BufferedInputStream(
						clientSocket.getInputStream()
				)
		);
	}

	public Socket getClientSocket() {
		return clientSocket;
	}

	@Override
	public void run() {
		String line = null;
		while (!Objects.equals(line, "End") &&
				!Objects.equals(line, "Start")){
			try {
				line = inputStream.readUTF();
			} catch (IOException e) {
				try {
					this.clientSocket.close();
				} catch (IOException ex) {
					throw new RuntimeException(ex);
				}
			}
			if (line != null) {
				System.out.println("Client says: " + line);
			}
		}
		if (line != null)
			partyHost.handleMessage(line, this.clientSocket);
		try {
			inputStream.close();
			ObjectOutputStream oos = new ObjectOutputStream(
					clientSocket.getOutputStream()
			);
			oos.flush();
			oos.close();
			clientSocket.close();
		} catch (IOException e) {
			System.out.println(e);
		}
	}
}
