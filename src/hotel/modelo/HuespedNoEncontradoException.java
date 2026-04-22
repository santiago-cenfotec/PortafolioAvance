package hotel.modelo;

/**
 * EXCEPCIÓN ESPECÍFICA: Se lanza cuando no se encuentra un huésped por su cédula.
 * HERENCIA: Extiende HotelException (jerarquía de excepciones propias del dominio).
 */
public class HuespedNoEncontradoException extends HotelException {

    private final String cedula;

    public HuespedNoEncontradoException(String cedula) {
        super("No se encontró ningún huésped con cédula: " + cedula);
        this.cedula = cedula;
    }

    public String getCedula() {
        return cedula;
    }
}