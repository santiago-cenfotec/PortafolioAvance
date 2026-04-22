package hotel.modelo;

/**
 * EXCEPCIÓN ESPECÍFICA: Se lanza cuando no se encuentra una reserva por su código.
 * HERENCIA: Extiende HotelException (jerarquía de excepciones propias del dominio).
 */
public class ReservaNoEncontradaException extends HotelException {

    private final String codigoReserva;

    public ReservaNoEncontradaException(String codigoReserva) {
        super("No se encontró la reserva con código: " + codigoReserva);
        this.codigoReserva = codigoReserva;
    }

    public String getCodigoReserva() {
        return codigoReserva;
    }
}