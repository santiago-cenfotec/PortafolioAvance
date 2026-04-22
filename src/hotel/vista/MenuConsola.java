package hotel.vista;

import hotel.controlador.HotelControlador;
import hotel.modelo.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

/**
 * PATRÓN MVC — VISTA:
 * Responsable exclusivamente de la interacción con el usuario.
 * Muestra menús, lee entradas y delega toda la lógica al Controlador.
 *
 * La Vista NO procesa datos ni tiene lógica de negocio.
 * Solo recibe resultados del Controlador y los presenta al usuario.
 *
 * DEPENDENCIA con HotelControlador: la Vista usa el Controlador para operar.
 */
public class MenuConsola {

    private final HotelControlador controlador;
    private final Scanner scanner;

    public MenuConsola(HotelControlador controlador) {
        this.controlador = controlador;
        this.scanner     = new Scanner(System.in);
    }

    /** Punto de entrada principal — muestra el menú en bucle hasta salir. */
    public void iniciar() {
        System.out.println("\n  Bienvenido al sistema del " + controlador.getNombreHotel());

        boolean activo = true;
        while (activo) {
            mostrarMenuPrincipal();
            int opcion = leerEntero("Seleccione una opción: ");

            switch (opcion) {
                case 1  -> menuHuespedes();
                case 2  -> menuHabitaciones();
                case 3  -> menuReservas();
                case 4  -> mostrarReporte();
                case 0  -> activo = false;
                default -> System.out.println("  ⚠ Opción no válida.");
            }
        }

        System.out.println("\n  ¡Hasta luego!");
        scanner.close();
    }

    // ─── Menú principal ───────────────────────────────────────────────────────

    private void mostrarMenuPrincipal() {
        System.out.println("\n╔══════════════════════════════╗");
        System.out.println("║      MENÚ PRINCIPAL          ║");
        System.out.println("╠══════════════════════════════╣");
        System.out.println("║  1. Gestionar huéspedes      ║");
        System.out.println("║  2. Ver habitaciones         ║");
        System.out.println("║  3. Gestionar reservas       ║");
        System.out.println("║  4. Ver reporte general      ║");
        System.out.println("║  0. Salir                    ║");
        System.out.println("╚══════════════════════════════╝");
    }

    // ─── Submenú: Huéspedes ───────────────────────────────────────────────────

    private void menuHuespedes() {
        System.out.println("\n── HUÉSPEDES ──────────────────");
        System.out.println("  1. Registrar nuevo huésped");
        System.out.println("  2. Buscar huésped por cédula");
        System.out.println("  3. Listar todos los huéspedes");
        System.out.println("  0. Volver");

        int opcion = leerEntero("Opción: ");
        switch (opcion) {
            case 1 -> registrarHuesped();
            case 2 -> buscarHuesped();
            case 3 -> listarHuespedes();
            case 0 -> { /* volver */ }
            default -> System.out.println("  ⚠ Opción no válida.");
        }
    }

    private void registrarHuesped() {
        System.out.println("\n── Registrar huésped ──────────");
        String cedula   = leerTexto("Cédula     : ");
        String nombre   = leerTexto("Nombre     : ");
        String telefono = leerTexto("Teléfono   : ");
        String correo   = leerTexto("Correo     : ");

        try {
            controlador.registrarHuesped(cedula, nombre, telefono, correo);
            System.out.println("  ✔ Huésped registrado correctamente.");
        } catch (HotelException e) {
            System.out.println("  ✖ Error: " + e.getMessage());
        }
    }

    private void buscarHuesped() {
        String cedula = leerTexto("Ingrese la cédula: ");
        try {
            controlador.buscarHuesped(cedula).ifPresentOrElse(
                    h -> System.out.println("  Encontrado: " + h + " | Tel: " + h.getTelefono()),
                    () -> System.out.println("  No se encontró ningún huésped con esa cédula.")
            );
        } catch (HotelException e) {
            System.out.println("  ✖ Error: " + e.getMessage());
        }
    }

    private void listarHuespedes() {
        try {
            List<Huesped> lista = controlador.listarHuespedes();
            if (lista.isEmpty()) {
                System.out.println("  No hay huéspedes registrados.");
                return;
            }
            System.out.println("\n── Huéspedes registrados ──────");
            lista.forEach(h -> System.out.println("  • " + h +
                    " | Tel: " + h.getTelefono() +
                    " | " + h.getCorreo()));
        } catch (HotelException e) {
            System.out.println("  ✖ Error: " + e.getMessage());
        }
    }

    // ─── Submenú: Habitaciones ────────────────────────────────────────────────

    private void menuHabitaciones() {
        System.out.println("\n── HABITACIONES ───────────────");
        System.out.println("  1. Ver todas las habitaciones");
        System.out.println("  2. Ver solo disponibles");
        System.out.println("  0. Volver");

        int opcion = leerEntero("Opción: ");
        switch (opcion) {
            case 1 -> listarHabitaciones(false);
            case 2 -> listarHabitaciones(true);
            case 0 -> { /* volver */ }
            default -> System.out.println("  ⚠ Opción no válida.");
        }
    }

    private void listarHabitaciones(boolean soloDisponibles) {
        List<Habitacion> lista = soloDisponibles
                ? controlador.getHabitacionesDisponibles()
                : controlador.getTodasHabitaciones();

        if (lista.isEmpty()) {
            System.out.println("  No hay habitaciones" + (soloDisponibles ? " disponibles" : "") + ".");
            return;
        }

        System.out.println();
        lista.forEach(h -> System.out.println("  • " + h));
    }

    // ─── Submenú: Reservas ────────────────────────────────────────────────────

    private void menuReservas() {
        System.out.println("\n── RESERVAS ───────────────────");
        System.out.println("  1. Crear nueva reserva");
        System.out.println("  2. Agregar servicio a reserva");
        System.out.println("  3. Cancelar reserva");
        System.out.println("  4. Ver detalle de reserva");
        System.out.println("  5. Listar todas las reservas");
        System.out.println("  0. Volver");

        int opcion = leerEntero("Opción: ");
        switch (opcion) {
            case 1 -> crearReserva();
            case 2 -> agregarServicio();
            case 3 -> cancelarReserva();
            case 4 -> verDetalleReserva();
            case 5 -> listarReservas();
            case 0 -> { /* volver */ }
            default -> System.out.println("  ⚠ Opción no válida.");
        }
    }

    private void crearReserva() {
        System.out.println("\n── Nueva reserva ──────────────");

        // Mostrar habitaciones disponibles primero
        System.out.println("  Habitaciones disponibles:");
        listarHabitaciones(true);

        String cedula = leerTexto("\nCédula del huésped : ");
        int numHab    = leerEntero("Número de habitación: ");

        LocalDate entrada = leerFecha("Fecha de entrada (AAAA-MM-DD): ");
        LocalDate salida  = leerFecha("Fecha de salida  (AAAA-MM-DD): ");

        if (entrada == null || salida == null) return;

        try {
            Reserva reserva = controlador.crearReserva(cedula, numHab, entrada, salida);
            System.out.println("\n  ✔ Reserva creada exitosamente:");
            reserva.mostrarDetalle();
        } catch (HabitacionNoDisponibleException e) {
            // Manejo específico de excepción personalizada
            System.out.println("  ✖ La habitación " + e.getNumeroHabitacion() +
                    " no está disponible. Por favor elija otra.");
        } catch (HuespedNoEncontradoException e) {
            // Manejo específico de excepción personalizada
            System.out.println("  ✖ No existe un huésped con cédula: " + e.getCedula() +
                    ". Regístrelo primero.");
        } catch (HotelException e) {
            System.out.println("  ✖ Error al crear reserva: " + e.getMessage());
        }
    }

    private void agregarServicio() {
        System.out.println("\n── Agregar servicio ───────────");
        System.out.println("  Servicios disponibles:");
        System.out.println("    1. Desayuno buffet    ($30.00)");
        System.out.println("    2. Acceso al spa      ($80.00)");
        System.out.println("    3. Traslado aeropuerto($50.00)");
        System.out.println("    4. Cena romántica     ($120.00)");

        String codigo = leerTexto("Código de reserva: ");
        int opServicio = leerEntero("Número de servicio: ");

        ServicioAdicional servicio = switch (opServicio) {
            case 1 -> new ServicioAdicional("Desayuno buffet",    30.0,  "Desayuno incluido diario");
            case 2 -> new ServicioAdicional("Acceso al spa",      80.0,  "Acceso por día");
            case 3 -> new ServicioAdicional("Traslado aeropuerto",50.0,  "Ida o vuelta");
            case 4 -> new ServicioAdicional("Cena romántica",     120.0, "Cena para dos con decoración");
            default -> null;
        };

        if (servicio == null) {
            System.out.println("  ⚠ Servicio no válido.");
            return;
        }

        try {
            controlador.agregarServicioAReserva(codigo, servicio);
            System.out.println("  ✔ Servicio agregado correctamente.");
        } catch (ReservaNoEncontradaException e) {
            System.out.println("  ✖ No existe la reserva: " + e.getCodigoReserva());
        } catch (HotelException e) {
            System.out.println("  ✖ Error: " + e.getMessage());
        }
    }

    private void cancelarReserva() {
        String codigo = leerTexto("Código de reserva a cancelar: ");
        try {
            controlador.cancelarReserva(codigo);
            System.out.println("  ✔ Reserva " + codigo + " cancelada. Habitación liberada.");
        } catch (ReservaNoEncontradaException e) {
            System.out.println("  ✖ No existe la reserva: " + e.getCodigoReserva());
        } catch (HotelException e) {
            System.out.println("  ✖ Error: " + e.getMessage());
        }
    }

    private void verDetalleReserva() {
        String codigo = leerTexto("Código de reserva: ");
        try {
            controlador.buscarReserva(codigo).ifPresentOrElse(
                    Reserva::mostrarDetalle,
                    () -> System.out.println("  No se encontró la reserva.")
            );
        } catch (HotelException e) {
            System.out.println("  ✖ Error: " + e.getMessage());
        }
    }

    private void listarReservas() {
        try {
            List<Reserva> lista = controlador.listarReservas();
            if (lista.isEmpty()) {
                System.out.println("  No hay reservas activas.");
                return;
            }
            System.out.println("\n── Reservas activas ───────────");
            lista.forEach(r -> System.out.println("  • " + r));
        } catch (HotelException e) {
            System.out.println("  ✖ Error: " + e.getMessage());
        }
    }

    // ─── Reporte ──────────────────────────────────────────────────────────────

    private void mostrarReporte() {
        try {
            System.out.println(controlador.generarReporte());
        } catch (HotelException e) {
            System.out.println("  ✖ Error al generar reporte: " + e.getMessage());
        }
    }

    // ─── Utilidades de entrada ────────────────────────────────────────────────

    /** Lee una línea de texto no vacía. */
    private String leerTexto(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    /** Lee un entero con manejo de excepción de formato. */
    private int leerEntero(String prompt) {
        System.out.print(prompt);
        try {
            int valor = Integer.parseInt(scanner.nextLine().trim());
            return valor;
        } catch (NumberFormatException e) {
            System.out.println("  ⚠ Ingrese un número válido.");
            return -1;
        }
    }

    /** Lee una fecha con manejo de excepción de formato. */
    private LocalDate leerFecha(String prompt) {
        System.out.print(prompt);
        try {
            return LocalDate.parse(scanner.nextLine().trim());
        } catch (DateTimeParseException e) {
            System.out.println("  ⚠ Formato de fecha inválido. Use AAAA-MM-DD (ej: 2026-04-25).");
            return null;
        }
    }
}