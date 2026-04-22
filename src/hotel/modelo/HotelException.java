package hotel.modelo;

/**
 * EXCEPCIÓN PERSONALIZADA BASE: Superclase de todas las excepciones del sistema.
 * Extiende Exception para ser una excepción verificada (checked exception).
 * Herencia: las excepciones específicas del hotel heredan de esta clase.
 */
public class HotelException extends Exception {

    public HotelException(String mensaje) {
        super(mensaje);
    }

    public HotelException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}

