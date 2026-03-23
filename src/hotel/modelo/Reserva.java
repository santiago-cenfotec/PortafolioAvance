package hotel.modelo;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**

 CLASE CENTRAL del sistema.

 COMPOSICIÓN con Habitacion: la reserva "posee" su habitación en el contexto de la reserva — sin reserva no hay asignación.

 AGREGACIÓN con Huesped: el huésped existe independientemente de la reserva; solo se referencia aquí.

 DEPENDENCIA con ServicioAdicional: se usa en calcularTotal() para sumar los cargos adicionales, pero no se posee de forma exclusiva.

 */
public class Reserva {

    private String        codigoReserva;
    private LocalDate     fechaEntrada;
    private LocalDate     fechaSalida;
    private Huesped       huesped;     // AGREGACIÓN
    private Habitacion    habitacion;  // COMPOSICIÓN

    // DEPENDENCIA: se usan dentro de métodos de esta clase
    private List<ServicioAdicional> servicios;

    //Constructor
    public Reserva(String codigoReserva, LocalDate fechaEntrada, LocalDate fechaSalida, Huesped huesped, Habitacion habitacion) {
        this.codigoReserva = codigoReserva;
        this.fechaEntrada  = fechaEntrada;
        this.fechaSalida   = fechaSalida;
        this.huesped       = huesped; //Agregación > Huesped ya existía
        this.habitacion    = habitacion; //Composición > La habitacion queda asignada
        this.servicios     = new ArrayList<>();

        // Marca la habitación como no disponible al reservarla
        habitacion.setDisponible(false);
    }

    //Getters
    public String getCodigoReserva() {
        return codigoReserva;
    }
    public LocalDate getFechaEntrada() {
        return fechaEntrada;
    }
    public LocalDate getFechaSalida() {
        return fechaSalida;
    }
    public Huesped getHuesped() {
        return huesped;
    }
    public Habitacion getHabitacion() {
        return habitacion;
    }

    // DEPENDENCIA ServicioAdicional
    public void agregarServicio(ServicioAdicional servicio) {
        servicios.add(servicio);
    }

    // Calcula el número de noches de la reserva
    public long getNochesReservadas() {
        return ChronoUnit.DAYS.between(fechaEntrada, fechaSalida);
    }

    /*
     Calcula el costo total: (precio/noche × noches) + suma de servicios adicionales
     Aquí se ve claramente la DEPENDENCIA: usamos ServicioAdicional solo para leer su precio
     */
    //Dependencia, usando getPrecio de Servicio Adicional
    public double calcularTotal() {
        double totalHabitacion = habitacion.getTipo().getPrecioPorNoche()
                * getNochesReservadas();
        //Convierte los servicios en un stream o canal de procesamiento
        double totalServicios  = servicios.stream()
                //Convierte los datos en double
                .mapToDouble(ServicioAdicional::getPrecio)
                //Suma los servicios asociados
                .sum();
        return totalHabitacion + totalServicios;
    }

    // Detalles Completos de la Reserva, como un to String
    public void mostrarDetalle() {
        System.out.println("  RESERVA: " + codigoReserva);
        System.out.println("  Huésped: " + huesped);
        System.out.println("  Habitación: " + habitacion);
        System.out.println("  Entrada: " + fechaEntrada);
        System.out.println("  Salida: " + fechaSalida);
        System.out.println("  Noches: " + getNochesReservadas());

        if (!servicios.isEmpty()) {
            System.out.println("  Servicios adicionales:");
            servicios.forEach(s -> System.out.println("    - " + s));
        }

        // $%.2f%n es para que al imprimir el total, lo imprima con dos decimales y en formato con el signo de dólar :D
        System.out.printf("  TOTAL     : $%.2f%n", calcularTotal());
    }
}