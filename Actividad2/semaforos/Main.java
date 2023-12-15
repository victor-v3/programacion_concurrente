package semaforos;

// La clase Main contiene el método principal del programa
public class Main {
	
	public static void main(String[] args) {
		
		Buffer buffer = new Buffer();
		
		new Consumidor(buffer);
		new Releaser(buffer);
	}
}
