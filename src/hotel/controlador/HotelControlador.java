package hotel.controlador;

import hotel.dao.IHuespedDAO;
import hotel.dao.IReservaDAO;
import hotel.modelo.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * PATRÓN MVC — CONTROLADOR:
 * Contiene toda la lógica de negocio del sistema.
 * Recibe solicitudes de la Vista (MenuConsola), las procesa y devuelve resultados.
 *
 * El Controlador conoce el Modelo (clases de hotel.modelo) y los DAOs,
 * pero NO sabe nada de cómo se muestran los datos al usuario.
 *
 * AGREGACIÓN con los DAOs: el controlador usa DAOs que se inyectan en el constructor,
 * lo que permite cambiar la implementación de persistencia sin modificar este controlador.
 */
public class HotelControlador {

    private final String nombreHotel;
    private final List<Habitacion>    habitaciones;
    private final IReservaDAO         reservaDAO;
    private final IHuespedDAO         huespedDAO;
    private int contadorReservas;

    /**
     * Constructor con inyección de dependencias:
     * Recibe las implementaciones de DAO como parámetros (no las crea él mismo),
     * lo que facilita el cambio entre persistencia en archivo, BD, etc.
     */
    public HotelControlador(String nombreHotel,
                            List<Habitacion> habitaciones,
                            IReservaDAO reservaDAO,
                            IHuespedDAO huespedDAO) {
        this.nombreHotel      = nombreHotel;
        this.habitaciones     = habitaciones;
        this.reservaDAO       = reservaDAO;
        this.huespedDAO       = huespedDAO;
        this.contadorReservas = 1;
    }

    // ─── Habitaciones ─────────────────────────────────────────────────────────

    public List<Habitacion> getTodasHabitaciones() {
        return habitaciones;
    }

    public List<Habitacion> getHabitacionesDisponibles() {
        return habitaciones.stream()
                .filter(Habitacion::isDisponible)
                .toList();
    }

    public Optional<Habitacion> buscarHabitacion(int numero) {
        return habitaciones.stream()
                .filter(h -> h.getNumero() == numero)
                .findFirst();
    }

    // ─── Huéspedes ────────────────────────────────────────────────────────────

    /**
     * Registra un nuevo huésped y lo persiste via DAO.
     * @throws HotelException si ocurre un error en la persistencia.
     */
    public void registrarHuesped(String cedula, String nombre,
                                 String telefono, String correo) throws HotelException {
        Huesped huesped = new Huesped(cedula, nombre, telefono, correo);
        huespedDAO.guardar(huesped);
    }

    public Optional<Huesped> buscarHuesped(String cedula) throws HotelException {
        return huespedDAO.buscarPorCedula(cedula);
    }

    public List<Huesped> listarHuespedes() throws HotelException {
        return huespedDAO.listarTodos();
    }

    // ─── Reservas ─────────────────────────────────────────────────────────────

    /**
     * Crea y persiste una nueva reserva.
     *
     * @throws HabitacionNoDisponibleException si la habitación ya está ocupada.
     * @throws HuespedNoEncontradoException    si la cédula no existe.
     * @throws HotelException                  para otros errores del sistema.
     */
    public Reserva crearReserva(String cedulaHuesped, int numeroHabitacion,
                                LocalDate entrada, LocalDate salida)
            throws HotelException {

        // 1. Buscar el huésped — lanza excepción si no existe
        Huesped huesped = huespedDAO.buscarPorCedula(cedulaHuesped)
                .orElseThrow(() -> new HuespedNoEncontradoException(cedulaHuesped));

        // 2. Buscar la habitación
        Habitacion habitacion = buscarHabitacion(numeroHabitacion)
                .orElseThrow(() -> new HotelException(
                        "No existe la habitación número " + numeroHabitacion));

        // 3. Verificar disponibilidad — lanza excepción si está ocupada
        if (!habitacion.isDisponible()) {
            throw new HabitacionNoDisponibleException(numeroHabitacion);
        }

        // 4. Validar fechas
        if (!salida.isAfter(entrada)) {
            throw new HotelException("La fecha de salida debe ser posterior a la de entrada.");
        }

        // 5. Crear la reserva
        String codigo = String.format("RES-%03d", contadorReservas++);
        Reserva reserva = new Reserva(codigo, entrada, salida, huesped, habitacion);

        // 6. Persistir
        reservaDAO.guardar(reserva);

        return reserva;
    }

    /**
     * Agrega un servicio adicional a una reserva existente.
     *
     * @throws ReservaNoEncontradaException si el código no existe.
     */
    public void agregarServicioAReserva(String codigoReserva, ServicioAdicional servicio)
            throws HotelException {

        Reserva reserva = reservaDAO.buscarPorCodigo(codigoReserva)
                .orElseThrow(() -> new ReservaNoEncontradaException(codigoReserva));

        reserva.agregarServicio(servicio);

        // Re-guardar: eliminar la anterior y guardar la actualizada
        reservaDAO.eliminar(codigoReserva);
        reservaDAO.guardar(reserva);
    }

    /**
     * Cancela una reserva liberando la habitación.
     *
     * @throws ReservaNoEncontradaException si el código no existe.
     */
    public void cancelarReserva(String codigoReserva) throws HotelException {
        Reserva reserva = reservaDAO.buscarPorCodigo(codigoReserva)
                .orElseThrow(() -> new ReservaNoEncontradaException(codigoReserva));

        // Liberar la habitación en memoria
        buscarHabitacion(reserva.getHabitacion().getNumero())
                .ifPresent(h -> h.setDisponible(true));

        reservaDAO.eliminar(codigoReserva);
    }

    public List<Reserva> listarReservas() throws HotelException {
        return reservaDAO.listarTodas();
    }

    public Optional<Reserva> buscarReserva(String codigo) throws HotelException {
        return reservaDAO.buscarPorCodigo(codigo);
    }

    // ─── Reporte ──────────────────────────────────────────────────────────────

    /**
     * Genera y devuelve el reporte del hotel como texto.
     * El Controlador prepara los datos; la Vista los imprime.
     */
    public String generarReporte() throws HotelException {
        List<Reserva> reservas = reservaDAO.listarTodas();
        List<Huesped> huespedes = huespedDAO.listarTodos();
        long disponibles = habitaciones.stream().filter(Habitacion::isDisponible).count();
        double ingresos = reservas.stream().mapToDouble(Reserva::calcularTotal).sum();

        return String.format(
                "\n╔══════════════════════════════════════╗\n" +
                        "║       REPORTE: %-22s║\n" +
                        "╠══════════════════════════════════════╣\n" +
                        "║  Habitaciones totales : %-13d║\n" +
                        "║  Habitaciones libres  : %-13d║\n" +
                        "║  Reservas activas     : %-13d║\n" +
                        "║  Huéspedes registrados: %-13d║\n" +
                        "║  Ingresos estimados   : $%-12.2f║\n" +
                        "╚══════════════════════════════════════╝",
                nombreHotel,
                habitaciones.size(),
                disponibles,
                reservas.size(),
                huespedes.size(),
                ingresos
        );
    }

    public String getNombreHotel() { return nombreHotel; }
}
