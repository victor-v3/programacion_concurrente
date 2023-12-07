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
				scanner.next();  // Descarta la entrada no válida
			}
		}
		return nInteger;
	}
    
    public static void main(String[] args) {
    	BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

    	// Get number of port to listen communications
        int port = getIntFromConsole("Enter port number: ");
        
        // Petición de la IP de conexión
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

            // Crear un hilo para leer y mostrar mensajes del servidor
            Thread serverReader = new Thread(() -> {
                try {
                    String inputLine;
                    while ((inputLine = in.readLine()) != null && !inputLine.isEmpty()) {
                        System.out.println(inputLine);
                    }
                } catch (IOException e) {
                    //e.printStackTrace();
                    System.out.println("Error on thread manage.");
                }
            });

            // Iniciar el hilo del lector del servidor
            serverReader.start();

            // Bucle principal para enviar mensajes al servidor
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
            //e.printStackTrace();
            System.out.println("I/O error");
        }
    }
}
