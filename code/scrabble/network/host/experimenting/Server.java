package scrabble.network.host.experimenting;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Locale;
import java.util.Objects;

/*
cd "OneDrive - Otterbein University/IdeaProjects/Scrabble/code"
javac scrabble/network/host/experimenting/Server.java
java scrabble/network/host/experimenting/Server
 */
public class Server {
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		int port = 5000;

		System.out.println("Your IP: " + Inet4Address.getLocalHost().getHostAddress());
		System.out.println("Listening at port " + port);
		System.out.println("Looking for clients...");
		ServerSocket server = new ServerSocket(port);
		Socket client = server.accept();
		ObjectInputStream inputStream = new ObjectInputStream(client.getInputStream());
		Object object = null;
		String message = null;
		boolean isClosed = false;
		do {
			try {
				object = inputStream.readObject();
			}
			catch (EOFException e) {
				System.out.println("Eof found");
				inputStream.close();
				isClosed = true;
			}

			if (object instanceof String) {
				System.out.println((String) object);
			}
			else
				message = "";
		} while (!Objects.equals(message, "quit") && !isClosed);

		inputStream.close();
	}
}
