package scrabble.network.networkPrototype;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/*
client side
usage: 	javac scrabble/network/networkPrototype/NetworkController.java
		java scrabble/network/networkPrototype/NetworkController [host_address] [port]
		../host/PartyHost acts as server and prints out local IP for host address
		David: cd "OneDrive - Otterbein University\IdeaProjects\Scrabble\code"
 */
public class NetworkController {
	private Socket socket;
	private Scanner input;
	private DataOutputStream out;

	public NetworkController(String address, int port) {
		try {
			socket = new Socket(address, port);
			System.out.println("Connected");
			input = new Scanner(System.in);
			out = new DataOutputStream(
					socket.getOutputStream());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		String line = "";
		while (!line.equals("End")) {

			try {
				line = input.next();
				out.writeUTF(line);
			}
			catch (IOException i) {
				System.out.println(i);
				System.out.println("Error in line IO");
			}
		}
		try {
			input.close();
			out.close();
			socket.close();
		}
		catch (IOException i) {
			System.out.println(i);
			System.out.println("error in stream closing");
		}
	}

	public static void main(String[] args)
	{
		NetworkController client
				= new NetworkController(args[0], Integer.parseInt(args[1]));
	}

}
