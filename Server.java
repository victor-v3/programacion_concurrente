package chatBot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

public class Server implements Runnable {

	private Socket clientSocket;

	// Constructor
	public Server(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}

	// Method to get an integer number from console with error control
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

	// Method to reverse a string given
	public String reverseStr(String str) {
		String reversedStr = "";
		for(int i=0; i < str.length(); i++) {
			reversedStr = str.charAt(i) +  reversedStr;
		}
		return reversedStr;
	}

	// Method called 'Magic Root' although it is not magic at all
	// Base and exponential numbers are required 
	// to calculate the exponential root
	public static double magicRoot(double base, double exp) {
		return Math.pow(base, 1/exp);
	}


	// Method to count the words of a sentence given
	public static int countWords(String str) {
		if (str == null || str.isEmpty() || str.isBlank()) return 0;
		return str.split("\\s").length;
	}

	// Method to calculate factorial. Just 1 to 20 is allowed
	public static int factorial(int n) {
		int f = 1;
		if (n<=0) return 1;
		for (int i = 2; i<= n; i++) {
			f *= i;
		}
		return f;
	}

	// Method to reverse the order of words of a sentence given
	public static String reverseSentence(String sentence) {
		String[] words = sentence.split(" ");
		for (int i = 0; i < words.length / 2; i++) {
			String temp = words[i];
			words[i] = words[words.length - i - 1];
			words[words.length - i - 1] = temp;
		}
		return String.join(" ", words);
	}

	// Method to send to client the menu options
	public static void menu(PrintWriter out) {
		out.println("Choose one from these five short puzzles "); out.flush();
		out.println("0. EXIT."); out.flush();
		out.println("1. Reversed strings."); out.flush();
		out.println("2. Magic root."); out.flush();
		out.println("3. Factorial"); out.flush();
		out.println("4. Count number of words."); out.flush();
		out.println("5. Reverse a sentence."); out.flush();
		out.println("6. MENU."); out.flush();
	}

	// Method to decode number of option from menu in its meaning of puzzle
	public static String decodeMenuOptions(int option) {
		String str = null;
		switch(option) {
		case 0:
			str = "0. EXIT.";
			return str;
		case 1:
			str = "1. Reversed strings.";
			return str;
		case 2:
			str = "2. Magic root.";
			return str;
		case 3:
			str = "3. Factorial";
			return str;
		case 4:
			str = "4. Count number of words.";
			return str;
		case 5:
			str = "5. Reverse a sentence.";
			return str;
		case 6:
			str = "6. MENU.";
			return str;
		default:
			str = "Invalid option.";
			return str;
		}
	}


	public static void main(String[] args) {
		try {

			// Show local time
			System.out.println(LocalDate.now() + " --- " + LocalTime.now());

			// Get number of port to listen communications
			int port = getIntFromConsole("Enter listen port number: ");

			// Open socket on port, start infinite loop to listen for clients and 
			// starts as many threads as clients are connected
			try (ServerSocket mainServerSocket = new ServerSocket(port)) {
				int i=0;
				System.out.println("Chat server started...");
				while(true) {
					Socket clientSocket = mainServerSocket.accept();
					System.out.println("Client connected from IP: " + 
							clientSocket.getInetAddress() + ":" + clientSocket.getLocalPort());
					i++;
					new Thread(new Server(clientSocket), "Client " + i).start();
					System.out.print("Client " + i + ": ");
				}
			}
		} catch (IOException e) {
			//e.printStackTrace();
			System.out.println("I/O error");
		}
	}

	@Override
	public void run() {
		String inputLine;

		try {
			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

			String clientName = in.readLine();

			out.println("Welcome to this server, " + clientName + "...");
			System.out.println(clientName + " is connected");

			// Show Option list
			menu(out);

			int option = 0;

			while ((inputLine = in.readLine()) != null) {
				boolean flagErr = false;
				try {
					option = Integer.parseInt(inputLine);
					System.out.println("  " + clientName + " --> Option: " + decodeMenuOptions(option));

				} catch (NumberFormatException e) {
					out.println(": Invalid. Not number option! "); out.flush();
					out.println(":: Try again!");
					flagErr = true;
				}
				if (!flagErr) {
					// Depend on the value of option, execute the puzzle related
					switch(option) {
					case 0:
						// Close the socket and inform about disconnection 
						System.out.println(clientName + " disconnected");								
						try {
							out.println("Enter 'q' to exit from client... "); out.flush();
							clientSocket.close(); 
						} catch (IOException e) { 
							e.printStackTrace(); 
						}
						break;
					case 1:
						out.println("1: Enter a string and you will get the reversed form.");
						String wstr = in.readLine();
						out.println("The reversed string is: " + reverseStr(wstr)); out.flush();
						out.println(":: new choice or 0 to exit, 6 for menu.");
						break;
					case 2:
						out.println("2: Enter a base number and the exp number you wish get its root."
								+ " Enter Base: ");
						double base = Double.parseDouble(in.readLine());
						out.println("Root is x^(1/exponent). Enter de exponent: "); out.flush();
						double exp = Double.parseDouble(in.readLine());
						out.println("The value of magic root is: " + magicRoot(base, exp)); out.flush();
						out.println(":: new choice or 0 to exit, 6 for menu."); out.flush();
						break;
					case 3:
						out.println("3: Enter a number to get its factorial. Warning! It grows very fast,"
								+ " so do not choice a very large number.");
						try {
							int number = Integer.parseInt(in.readLine());
							if (number >= 20) {
								out.println("To big for me. Sorry!"); out.flush();
							} else {
								out.println("Factorial of " + number + "! is: " +
									factorial(number));out.flush();
							}
							out.flush();
							out.println(":: new choice or 0 to exit, 6 for menu.");out.flush();
						} catch (NumberFormatException e) {
							out.println(":: not valid number!"); out.flush();
						}
						break;
					case 4:
						out.println("4: Enter a sentence and you will the number of words.");
						String str = in.readLine();
						out.println("The number of words is: " + countWords(str)); out.flush();
						out.println(":: new choice or 0 to exit, 6 for menu."); out.flush();
						break;
					case 5:
						out.println("5: Enter a sentence and you will get the sentence"
							+ " words in order reversed.");
						String sentenceStr = in.readLine();
						out.println("The order reversed sentence is: " + 
								reverseSentence(sentenceStr)); out.flush();
						out.println(":: new choice or 0 to exit, 6 for menu."); out.flush();
						break;
					case 6:
						menu(out);
						break;
					default:
						out.println(": Not permited Option!"); out.flush();
						out.println(":: Try again!");
					}
				}
			}
		} catch (IOException e) {
			System.out.println("Thread ending...");
		} finally {
			try {
				// Close the socket of client when communication is over
				clientSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
