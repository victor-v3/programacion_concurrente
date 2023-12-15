package semaforos;

import java.util.Random;

//Clase consumidora de memoria
public class Consumidor extends Thread{
	
	private Buffer buffer;
	
	public Consumidor(Buffer buffer) {
		this.buffer = buffer;
		start();
	}
	
	public void reservar() {
		
		Random rdm = new Random();
		
		// Generación aleatoria del tiempo de espera empleado en la reserva
		int timeSleep = rdm.nextInt(250-25+1)+25;
		
		// En espera
		try {
			sleep(timeSleep);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// Si la cola está vacía se evila un null en la operación de poll
		if (!buffer.getResources().isEmpty()) {
			int numResources = buffer.getResources().poll();
			System.out.println("------> Reservando " + numResources + " recursos.");
		}
	}
	
	public void run() {
		
		while (true) {
			//System.out.println("Size of buffer: "+ buffer.getResources().size());
			
			if(buffer.getResources().size()==0) {
				System.out.println(">>>>Intentando reservar... No hay recursos disponibles");
			}
			try {
				buffer.getReserva().acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			reservar();
			
			buffer.getReserva().release();
		}
	}
}
