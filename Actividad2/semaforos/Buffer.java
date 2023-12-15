package semaforos;

import java.util.concurrent.Semaphore;
import java.util.concurrent.*;

public class Buffer {
	// Se elige Integer.MAX_VALUE para poder simular una cantidad 
	// suficientemente grande de interacciones de gestión de reserva-liberacion
	// de memoria por parte de los semáforos
	public final int bufferSize = Integer.MAX_VALUE ;
	
	private  ConcurrentLinkedQueue<Integer> resources = new ConcurrentLinkedQueue<Integer>();
	private Semaphore reserva = new Semaphore(0, true);
	private Semaphore libera = new Semaphore(bufferSize,true);
	
	// Cola concurrente enlazada para almacenar las peticiones de reserva-liberación de memoria
	public ConcurrentLinkedQueue<Integer> getResources() {
		return resources;
	}
	
	// Semáforo para gestionar la reserva de memoria
	public Semaphore getReserva() {
		return reserva;
	}
	
	// Semáforo para gestionar la liberación de memoria
	public Semaphore getLibera() {
		return libera;
	}
}
