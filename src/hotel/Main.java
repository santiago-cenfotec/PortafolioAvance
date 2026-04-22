package hotel;

import hotel.controlador.HotelControlador;
import hotel.dao.HuespedArchivoDAO;
import hotel.dao.IHuespedDAO;
import hotel.dao.IReservaDAO;
import hotel.dao.ReservaArchivoDAO;
import hotel.modelo.*;
import hotel.sistema.SistemaHotel;
import hotel.vista.MenuConsola;

/**
 * PATRÓN MVC — PUNTO DE ENTRADA:
 * El Main inicializa todos los componentes y los conecta:
 *
 *   1. Crea el SistemaHotel con las habitaciones (Modelo)
 *   2. Crea las implementaciones DAO (Persistencia)
 *   3. Crea el Controlador inyectando Modelo y DAOs
 *   4. Crea la Vista pasándole el Controlador
 *   5. Inicia la Vista
 *
 * Esta separación aplica el principio de inversión de dependencias:
 * el Controlador no sabe qué implementación de DAO se usa,
 * y la Vista no sabe nada del Modelo ni de la persistencia.
 */
public class Main {

    public static void main(String[] args) {

        // ── 1. MODELO: Habitaciones ──────────────────────────────────────────

        TipoHabitacion sencilla = new TipoHabitacion("SEN", "Sencilla",  70.0);
        TipoHabitacion doble    = new TipoHabitacion("DOB", "Doble",    140.0);
        TipoHabitacion suite    = new TipoHabitacion("SUT", "Suite",    350.0);
        TipoHabitacion vip      = new TipoHabitacion("VIP", "VIP",      500.0);

        SistemaHotel sistema = new SistemaHotel("Hotel Paraíso CR");

        // Habitaciones estándar
        sistema.agregarHabitacion(new Habitacion(101, 1, sencilla));
        sistema.agregarHabitacion(new Habitacion(102, 1, sencilla));
        sistema.agregarHabitacion(new Habitacion(201, 2, doble));
        sistema.agregarHabitacion(new Habitacion(202, 2, doble));
        sistema.agregarHabitacion(new Habitacion(301, 3, suite));

        // Habitaciones VIP — HERENCIA: HabitacionVIP extiende Habitacion
        // POLIMORFISMO: se almacenan como Habitacion pero su toString() es el de VIP
        sistema.agregarHabitacion(new HabitacionVIP(401, 4, vip, true,  true, "Vista al mar"));
        sistema.agregarHabitacion(new HabitacionVIP(402, 4, vip, false, true, "Vista a la montaña"));

        // ── 2. PERSISTENCIA: DAOs ────────────────────────────────────────────

        IReservaDAO  reservaDAO  = new ReservaArchivoDAO();
        IHuespedDAO  huespedDAO  = new HuespedArchivoDAO();

        // ── 3. CONTROLADOR ───────────────────────────────────────────────────

        HotelControlador controlador = new HotelControlador(
                sistema.getNombre(),
                sistema.getHabitaciones(),
                reservaDAO,
                huespedDAO
        );

        // ── 4. VISTA ─────────────────────────────────────────────────────────

        MenuConsola menu = new MenuConsola(controlador);

        // ── 5. INICIAR ───────────────────────────────────────────────────────

        menu.iniciar();
    }
}