package hotel.modelo;

/**
 * INTERFAZ: Define el contrato para cualquier entidad que pueda generar un costo total.
 * Reserva implementa esta interfaz, demostrando polimorfismo de interfaz.
 */
public interface Facturable {
    double calcularTotal();
    void mostrarDetalle();
}
