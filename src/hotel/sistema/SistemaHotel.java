package hotel.sistema;

import hotel.modelo.Habitacion;
import hotel.modelo.Huesped;
import hotel.modelo.Reserva;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/*
 COMPOSICIÓN: SistemaHotel crea y gestiona Reservas y Habitaciones.
 Si el sistema deja de existir, sus colecciones también desaparecen.

 AGREGACIÓN: Los huespedes se registran en el sistema, pero pueden existir conceptualmente fuera de él.
 */
public class SistemaHotel {

    private String nombre;
    private List<Habitacion> habitaciones;  // Composición
    private List<Reserva> reservas;      // Composición
    private List<Huesped> huespedes;     // Agregación

    public SistemaHotel(String nombre) {
        this.nombre = nombre;
        this.habitaciones = new ArrayList<>();
        this.reservas = new ArrayList<>();
        this.huespedes = new ArrayList<>();
    }

    //Habitaciones

    public void agregarHabitacion(Habitacion h) {
        habitaciones.add(h);
    }

    public List<Habitacion> getHabitacionesDisponibles() {
        return habitaciones.stream()
                .filter(Habitacion::isDisponible)
                .toList();
    }

    //Huéspedes

    public void registrarHuesped(Huesped h) {
        huespedes.add(h);
    }

    public Optional<Huesped> buscarHuesped(String cedula) {
        return huespedes.stream()
                .filter(h -> h.getCedula().equals(cedula))
                .findFirst();
    }

    //Reservas

    public void registrarReserva(Reserva r) {
        reservas.add(r);
        System.out.println("Reserva " + r.getCodigoReserva() + " registrada.");
    }

    public boolean cancelarReserva(String codigo) {
        Optional<Reserva> encontrada = reservas.stream()
                .filter(r -> r.getCodigoReserva().equals(codigo))
                .findFirst();

        if (encontrada.isPresent()) {
            Reserva r = encontrada.get();
            r.getHabitacion().setDisponible(true);  // Liberar habitación
            reservas.remove(r);
            System.out.println("Reserva " + codigo + " cancelada.");
            return true;
        }

        System.out.println("Reserva no encontrada: " + codigo);
        return false;
    }

    public void listarReservas() {
        if (reservas.isEmpty()) {
            System.out.println("No hay reservas activas.");
            return;
        }
        reservas.forEach(Reserva::mostrarDetalle);
    }

    public void generarReporte() {
        System.out.println("\n=== REPORTE: " + nombre + " ===");
        System.out.println("Habitaciones totales: " + habitaciones.size());
        System.out.println("Habitaciones libres: " + getHabitacionesDisponibles().size());
        System.out.println("Reservas activas: " + reservas.size());
        System.out.println("Huéspedes registrados: " + huespedes.size());
        double ingresos = reservas.stream().mapToDouble(Reserva::calcularTotal).sum();
        // $%.2f%n es para que al imprimir el total, lo imprima con dos decimales y en formato con el signo de dólar :D
        System.out.printf("Ingresos estimados: $%.2f%n", ingresos);
    }
}
