package semaforos;

import java.util.Random;

// Clase liberadora de memoria
public class Releaser extends Thread{
	public Buffer buffer;
	
	public Releaser(Buffer buffer) {
		this.buffer = buffer;
		start();
	}
	
	public void liberar() {
		
		Random rdm = new Random();
		// Simula el número de unidades de la petición que se liberarán
		int numResources = rdm.nextInt(100) + 1;
		
		// Generación aleatoria del tiempo de espera empleado en la liberación
		int timeLiberacion = rdm.nextInt(250-25+1)+25;
		
		// En espera
		try {
			sleep(timeLiberacion);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// Se añade a la cola el número de unidades de la petición
		buffer.getResources().add(numResources);
		System.out.println("<-- Liberando " + numResources + " recursos...");
	}
	
	public void run() {
		while (true) {
						
			//System.out.println("Size of buffer: "+ buffer.getResources().size());
			
			if (buffer.getResources().size()== buffer.bufferSize)
				System.out.println("Pila de recursos disponibles llena.");
			
			try {
				buffer.getLibera().acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			liberar();
			
			buffer.getReserva().release();
		}
	}
}
