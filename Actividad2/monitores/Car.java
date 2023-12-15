package monitores;

// La clase Car representa un coche que intenta
// cruzar el puente
class Car extends Thread {
    private final Bridge bridge;
    private final String direction;

    // El constructor de la clase, toma un objeto
    // Bridge y una dirección como argumentos
    public Car(Bridge bridge, String direction) {
        this.bridge = bridge;
        this.direction = direction;
    }

    // Cuando se inicia el hilo, el coche intenta
    // cruzar el puente desde la dirección especificada
    @Override
    public void run() {
        try {
            if ("north".equals(direction)) {
                bridge.crossBridgeFromNorth();
            } else {
                bridge.crossBridgeFromSouth();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}