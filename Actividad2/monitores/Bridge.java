package monitores;

import java.util.Random;

public class Bridge {
	// directionOnBridge guarda la dirección de los coches que están cruzando el puente
	private String directionOnBridge = "";
	// Número máximo de coches que pueden estar en el puente al mismo tiempo
	private static final int MAX_CARS = 3; 
	// carsOnBridge cuenta cuántos coches están cruzando el puente
	private int carsOnBridge = 0;
    private int numCarsFromNorth = 0;
    private int numCarsFromSouth = 0;

 // Método para que un coche cruce el puente desde el norte
    public synchronized void crossBridgeFromNorth() throws InterruptedException {
        Random rdm = new Random();
        int time = rdm.nextInt(10000);
        // Si el puente no está vacío y los coches en el puente están yendo en la dirección opuesta,
        // o el número de coches en el puente es el máximo permitido, el coche espera
        while ((!directionOnBridge.equals("") && !directionOnBridge.equals("north")) || carsOnBridge == MAX_CARS) {
            wait();
        }
        // Cuando el coche puede cruzar el puente, se actualiza directionOnBridge y se incrementa carsOnBridge
        directionOnBridge = "north";
        carsOnBridge++;
        System.out.println("Coche cruzando el puente desde el norte. Coches en el puente: " + carsOnBridge);
        // Simulamos el tiempo que tarda en cruzar el puente
        Thread.sleep(rdm.nextInt(time) + 100);
        
        // Cuando el coche ha cruzado el puente, se decrementa carsOnBridge
        carsOnBridge--;
        // Se incrementa el contador de coches que han cruzado desde el norte
        numCarsFromNorth++;
        
        // Si no hay más coches en el puente, se actualiza directionOnBridge a "" 
        // y se notifica a todos los coches que esperan
        if (carsOnBridge == 0) {
            directionOnBridge = "";
            notifyAll();
        } else {
        	// Si todavía hay coches en el puente, se notifica a un coche que espera en la misma dirección
        	notify();
        }
        System.out.println("Coche ha cruzado el puente desde el norte. Coches en el puente: " + carsOnBridge +
        		" Total coches desde el norte: "+numCarsFromNorth);
    }
    // Método para que un coche cruce el puente desde el sur (similar al método anterior)
    public synchronized void crossBridgeFromSouth() throws InterruptedException {
        Random rdm = new Random();
        int time = rdm.nextInt(10000);
        while ((!directionOnBridge.equals("") && !directionOnBridge.equals("south")) || carsOnBridge == MAX_CARS) {
            wait();
        }
        directionOnBridge = "south";
        carsOnBridge++;
        System.out.println("Coche cruzando el puente desde el sur. Coches en el puente: " + carsOnBridge);
        // Simulamos el tiempo que tarda en cruzar el puente
        Thread.sleep(rdm.nextInt(time) + 100);
        carsOnBridge--;
        numCarsFromSouth++;
        if (carsOnBridge == 0) {
            directionOnBridge = "";
            notifyAll();
        } else {
            notify();
        }
        System.out.println("Coche ha cruzado el puente desde el sur. Coches en el puente: " + carsOnBridge +
        		" Total coches desde el sur: "+numCarsFromSouth);
    }
}


