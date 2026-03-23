package hotel;

import hotel.modelo.*;
import hotel.sistema.SistemaHotel;

import java.time.LocalDate;

public class Main {

    public static void main(String[] args) {

        // Sistema (Composición: el sistema posee todo)
        SistemaHotel hotel = new SistemaHotel("Hotel Paraíso CR");

        // Tipos de Habitacion
        TipoHabitacion sencilla = new TipoHabitacion("SEN", "Sencilla", 70.0);
        TipoHabitacion doble    = new TipoHabitacion("DOB", "Doble",    140.0);
        TipoHabitacion suite    = new TipoHabitacion("SUT", "Suite",   500.0);

        // Habitaciones
        hotel.agregarHabitacion(new Habitacion(101, 1, sencilla));
        hotel.agregarHabitacion(new Habitacion(102, 1, sencilla));
        hotel.agregarHabitacion(new Habitacion(201, 2, doble));
        hotel.agregarHabitacion(new Habitacion(301, 3, suite));

        // Huéspedes
        Huesped ana   = new Huesped("119610475", "Ana Mora",   "8687-7348", "ana@gmail.com");
        Huesped carlos = new Huesped("119610476", "Carlos Díaz","8408-6521", "carlos@gmail.com");
        hotel.registrarHuesped(ana);
        hotel.registrarHuesped(carlos);

        // Servicios Adicionales
        ServicioAdicional desayuno = new ServicioAdicional("Desayuno buffet", 30.0, "Desayuno incluido");
        ServicioAdicional spa      = new ServicioAdicional("Acceso al spa",   80.0, "Acceso por día");

        // Reserva
        Habitacion hab201 = hotel.getHabitacionesDisponibles()
                .stream()
                .filter(h -> h.getNumero() == 201)
                .findFirst()
                .orElseThrow();

        Reserva reserva1 = new Reserva(
                "RES-001",
                LocalDate.now(),
                LocalDate.now().plusDays(3),
                ana,
                hab201
        );
        reserva1.agregarServicio(desayuno);
        reserva1.agregarServicio(spa);
        hotel.registrarReserva(reserva1);

        // Detalles Completos
        reserva1.mostrarDetalle();
        hotel.generarReporte();

        // 8. Cancelar reserva y verificar disponibilidad
        hotel.cancelarReserva("RES-001");
        hotel.generarReporte();
    }
}