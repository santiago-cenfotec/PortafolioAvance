package hotel.modelo;

/**
 * EXCEPCIÓN ESPECÍFICA: Se lanza cuando se intenta reservar una habitación que ya está ocupada.
 * HERENCIA: Extiende HotelException (jerarquía de excepciones propias del dominio).
 */
public class HabitacionNoDisponibleException extends HotelException {

    private final int numeroHabitacion;

    public HabitacionNoDisponibleException(int numeroHabitacion) {
        super("La habitación " + numeroHabitacion + " no está disponible.");
        this.numeroHabitacion = numeroHabitacion;
    }

    public int getNumeroHabitacion() {
        return numeroHabitacion;
    }
}