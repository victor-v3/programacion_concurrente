package chatBot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;


public class Client {

	// Method to get a integer from console with error control
	public static int getIntFromConsole(String msg) {
		Scanner scanner = new Scanner(System.in);
		int nInteger = 0; 
		boolean isValid = false;

		while (!isValid) {
			System.out.println(msg);
			if (scanner.hasNextInt()) {
				nInteger = scanner.nextInt();
				isValid = true;
			} else {
				System.out.println("Error, not a number! Try again.");
				scanner.next();
			}
		}
		return nInteger;
	}

	public static void main(String[] args) {
		BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

		// Get number of listen port communications
		int port = getIntFromConsole("Enter port number: ");

		// Get IP connection from user
		System.out.println("Enter IP address or localhost: ");
		String ipAddress = null;
		try {
			ipAddress = console.readLine();
		} catch (IOException e) {
			System.out.println("Not valid ip.");
			e.printStackTrace();
		}

		System.out.println("ip: " +  ipAddress);

		try (Socket socket = new Socket(ipAddress, port)) {
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

			Scanner scanner = new Scanner(System.in);

			System.out.println("Enter your name: ");
			String name = scanner.nextLine();
			out.println(name);

			// Define a thread to read inputs and show messages from server
			Thread serverReader = new Thread(() -> {
				try {
					String inputLine;
					while ((inputLine = in.readLine()) != null && !inputLine.isEmpty()) {
						System.out.println(inputLine);
					}
				} catch (IOException e) {
					System.out.println("Error on thread manage.");
				}
			});

			// Start client read thread
			serverReader.start();

			// Loop to manage client complete close
			while (true) {
				String response = scanner.nextLine();
				if (response.equals("q")) {
					System.out.println("Connection finished.");
					socket.close();
					break;
				}
				out.println(response);
			}
		} catch (IOException e) {
			System.out.println("I/O error");
		}
	}
}
