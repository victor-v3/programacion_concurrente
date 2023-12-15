package monitores;

import java.util.Random;
import java.util.Scanner;

// Clase que contiene el método principal del programa
public class Main {
	public static void main(String[] args) {
		// Creación nuevo objeto de tipo Bridge
        Bridge bridge = new Bridge();
        
        // Variable para definir el límite superior de
        // simulación de paso de coches por el puente
        int rangeSimul = 100;
        
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Introduzca el número límite máximo de simulaciones de pasos de coches: ");
        rangeSimul = scanner.nextInt();
        Random random = new Random();
        
        // Genera número aleatorio de simulaciones de pasos de coches entre 1 y rangeSimul
        int numberOfSimul = random.nextInt(rangeSimul) + 1; 
        System.out.println("Número de simulaciones: " + numberOfSimul);
        
        // Para cada coche, se genera una dirección aleatoria y 
        // se crea un nuevo objeto tipo Car
        for (int i = 0; i < numberOfSimul; i++) {
            String direction = random.nextBoolean() ? "north" : "south"; 
            Car car = new Car(bridge, direction);
            
         // Se inicia el hilo, haciendo que el coche intente cruzar el puente
            car.start();
        }
        scanner.close();
    }
}
