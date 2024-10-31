package scrabble.network.experimenting;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Objects;
import java.util.Scanner;
/*
cd "OneDrive - Otterbein University/IdeaProjects/Scrabble/code"
javac scrabble/network/host/experimenting/Client.java
java scrabble/network/host/experimenting/Client 10.80.2.1 5000
 */
public class Client {
	public static void main(String[] args) throws IOException, InterruptedException {
		String address = args[0];
		int port = Integer.parseInt(args[1]);
		Socket socket = new Socket(address, port);
		System.out.println("Connected");
		Scanner input = new Scanner(System.in);
		System.out.println("Enter message: ");
		String message = input.next();
		ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
		while (!Objects.equals(message.toLowerCase(), "quit")) {
			outputStream.writeObject(message);
			outputStream.flush();
			System.out.println("Enter message: ");
			message = input.nextLine();
		}

		outputStream.writeObject(message);
		outputStream.flush();
		Thread.sleep(1000);
		outputStream.close();
		input.close();
	}
}
