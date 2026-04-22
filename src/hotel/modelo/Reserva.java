package hotel.modelo;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * CLASE CENTRAL del sistema.
 *
 * COMPOSICIÓN con Habitacion: la reserva "posee" su habitación en el contexto
 * de la asignación — sin reserva no tiene sentido la asignación.
 *
 * AGREGACIÓN con Huesped: el huésped existe independientemente de la reserva.
 *
 * DEPENDENCIA con ServicioAdicional: se usa en calcularTotal() para sumar cargos,
 * pero no se posee de forma exclusiva.
 *
 * INTERFAZ Facturable: implementa el contrato de facturación (polimorfismo de interfaz).
 */
public class Reserva implements Facturable {

    private String     codigoReserva;
    private LocalDate  fechaEntrada;
    private LocalDate  fechaSalida;
    private Huesped    huesped;      // AGREGACIÓN
    private Habitacion habitacion;   // COMPOSICIÓN (en contexto de la reserva)
    private List<ServicioAdicional> servicios;  // DEPENDENCIA

    // Constructor
    public Reserva(String codigoReserva, LocalDate fechaEntrada,
                   LocalDate fechaSalida, Huesped huesped, Habitacion habitacion) {
        this.codigoReserva = codigoReserva;
        this.fechaEntrada  = fechaEntrada;
        this.fechaSalida   = fechaSalida;
        this.huesped       = huesped;
        this.habitacion    = habitacion;
        this.servicios     = new ArrayList<>();

        // Marca la habitación como no disponible al reservarla
        habitacion.setDisponible(false);
    }

    // Getters
    public String     getCodigoReserva() { return codigoReserva; }
    public LocalDate  getFechaEntrada()  { return fechaEntrada; }
    public LocalDate  getFechaSalida()   { return fechaSalida; }
    public Huesped    getHuesped()       { return huesped; }
    public Habitacion getHabitacion()    { return habitacion; }
    public List<ServicioAdicional> getServicios() { return new ArrayList<>(servicios); }

    // Agrega un servicio adicional a la reserva (DEPENDENCIA)
    public void agregarServicio(ServicioAdicional servicio) {
        servicios.add(servicio);
    }

    // Calcula el número de noches de la reserva
    public long getNochesReservadas() {
        return ChronoUnit.DAYS.between(fechaEntrada, fechaSalida);
    }

    /**
     * IMPLEMENTACIÓN DE INTERFAZ Facturable:
     * Calcula el total: (precio/noche × noches) + suma de servicios adicionales.
     * Aquí se ve la DEPENDENCIA: usamos ServicioAdicional solo para leer su precio.
     */
    @Override
    public double calcularTotal() {
        double totalHabitacion = habitacion.getTipo().getPrecioPorNoche()
                * getNochesReservadas();
        double totalServicios = servicios.stream()
                .mapToDouble(ServicioAdicional::getPrecio)
                .sum();
        return totalHabitacion + totalServicios;
    }

    /**
     * IMPLEMENTACIÓN DE INTERFAZ Facturable:
     * Muestra todos los detalles de la reserva incluyendo servicios y total.
     */
    @Override
    public void mostrarDetalle() {
        System.out.println("─────────────────────────────────────────");
        System.out.println("  RESERVA : " + codigoReserva);
        System.out.println("  Huésped : " + huesped);
        System.out.println("  Habitac.: " + habitacion);
        System.out.println("  Entrada : " + fechaEntrada);
        System.out.println("  Salida  : " + fechaSalida);
        System.out.println("  Noches  : " + getNochesReservadas());

        if (!servicios.isEmpty()) {
            System.out.println("  Servicios adicionales:");
            servicios.forEach(s -> System.out.println("    • " + s));
        }

        System.out.printf("  TOTAL   : $%.2f%n", calcularTotal());
        System.out.println("─────────────────────────────────────────");
    }

    @Override
    public String toString() {
        return codigoReserva + " | " + huesped.getNombre() +
                " | Hab." + habitacion.getNumero() +
                " | " + fechaEntrada + " → " + fechaSalida;
    }
}