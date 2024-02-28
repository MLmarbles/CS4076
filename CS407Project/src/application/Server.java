package application;

import java.io.*;
import java.net.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Server {
	private static ServerSocket servSock;
	private static final int PORT = 1234;
	private static int clientConnections = 0;
	private static HashMap<Integer, ArrayList<Module>> modules;

	public static void main(String[] args) {
		System.out.println("Opening port...\n");
		initializeClassData();
		try {
			servSock = new ServerSocket(PORT);
		} catch (IOException e) {
			System.out.println("Unable to attach to port!");
			System.exit(1);
		}

		do {
			run();
		} while (true);

	}

	private static void initializeClassData() {
		Queries queries = new Queries();
		try {
			modules = queries.getModulesWithData();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void run() {
		Socket link = null;
		try {
			link = servSock.accept();
			clientConnections++;
			BufferedReader in = new BufferedReader(new InputStreamReader(link.getInputStream()));
			PrintWriter out = new PrintWriter(link.getOutputStream(), true);

			String message = in.readLine();
			System.out.println("Message received from client: " + clientConnections + "  " + message);
			out.println("Response from Server (Capitalized Message): " + message.toUpperCase());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				System.out.println("\n* Closing connection... *");
				link.close();
			} catch (IOException e) {
				System.out.println("Unable to disconnect!");
				System.exit(1);
			}
		}
	}
}
