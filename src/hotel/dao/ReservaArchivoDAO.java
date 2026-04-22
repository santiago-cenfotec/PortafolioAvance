package hotel.dao;

import hotel.modelo.*;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * PATRÓN DAO — IMPLEMENTACIÓN CON ARCHIVO:
 * Persiste las reservas en un archivo de texto plano (reservas.txt).
 * Cada línea del archivo representa una reserva en formato CSV:
 *
 *   codigoReserva,cedulaHuesped,nombreHuesped,telefonoHuesped,correoHuesped,
 *   numeroHabitacion,pisoHabitacion,codigoTipo,descTipo,precioTipo,fechaEntrada,fechaSalida
 *
 * NOTA: Los servicios adicionales se guardan en líneas extra con prefijo "SVC:"
 * inmediatamente después de su reserva.
 *
 * Esta clase implementa IReservaDAO, lo que significa que el resto del sistema
 * solo conoce la interfaz — si mañana se quiere usar una base de datos,
 * solo se cambia esta clase por una nueva implementación.
 */
public class ReservaArchivoDAO implements IReservaDAO {

    private static final String ARCHIVO = "reservas.txt";
    private static final String SEPARADOR = ",";
    private static final String PREFIJO_SVC = "SVC:";

    /**
     * Guarda una reserva al final del archivo.
     * Si el archivo no existe, lo crea automáticamente.
     */
    @Override
    public void guardar(Reserva reserva) throws HotelException {
        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(ARCHIVO, true))) {  // true = modo append

            writer.write(reservaALinea(reserva));
            writer.newLine();

            // Guarda los servicios adicionales de esta reserva
            for (ServicioAdicional s : reserva.getServicios()) {
                writer.write(PREFIJO_SVC + reserva.getCodigoReserva()
                        + SEPARADOR + s.getNombre()
                        + SEPARADOR + s.getPrecio()
                        + SEPARADOR + s.getDescripcion());
                writer.newLine();
            }

        } catch (IOException e) {
            throw new HotelException("Error al guardar la reserva en archivo: " + e.getMessage(), e);
        }
    }

    /**
     * Lee todas las reservas del archivo y las reconstruye como objetos.
     * Maneja el caso en que el archivo aún no existe.
     */
    @Override
    public List<Reserva> listarTodas() throws HotelException {
        List<Reserva> reservas = new ArrayList<>();
        File archivo = new File(ARCHIVO);

        if (!archivo.exists()) return reservas;  // Sin archivo = lista vacía

        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            Reserva ultimaReserva = null;

            while ((linea = reader.readLine()) != null) {
                if (linea.isBlank()) continue;

                if (linea.startsWith(PREFIJO_SVC)) {
                    // Es un servicio adicional de la última reserva leída
                    if (ultimaReserva != null) {
                        ServicioAdicional s = lineaAServicio(linea);
                        ultimaReserva.agregarServicio(s);
                    }
                } else {
                    // Es una reserva
                    ultimaReserva = lineaAReserva(linea);
                    if (ultimaReserva != null) {
                        reservas.add(ultimaReserva);
                    }
                }
            }
        } catch (IOException e) {
            throw new HotelException("Error al leer reservas del archivo: " + e.getMessage(), e);
        }

        return reservas;
    }

    /** Busca una reserva por su código leyendo todas y filtrando. */
    @Override
    public Optional<Reserva> buscarPorCodigo(String codigo) throws HotelException {
        return listarTodas().stream()
                .filter(r -> r.getCodigoReserva().equals(codigo))
                .findFirst();
    }

    /**
     * Elimina una reserva reescribiendo el archivo sin la reserva indicada.
     * También elimina las líneas SVC: asociadas a esa reserva.
     */
    @Override
    public boolean eliminar(String codigo) throws HotelException {
        File archivo = new File(ARCHIVO);
        if (!archivo.exists()) return false;

        List<String> lineasRestantes = new ArrayList<>();
        boolean encontrada = false;
        boolean saltarSVC = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                if (linea.isBlank()) continue;

                if (!linea.startsWith(PREFIJO_SVC)) {
                    // Es una reserva: chequear si es la que se quiere eliminar
                    String[] partes = linea.split(SEPARADOR, -1);
                    if (partes[0].equals(codigo)) {
                        encontrada = true;
                        saltarSVC = true;   // Las siguientes líneas SVC también se eliminan
                        continue;
                    } else {
                        saltarSVC = false;  // Nueva reserva distinta, dejar pasar sus SVC
                    }
                } else {
                    // Es SVC: saltar si pertenece a la reserva eliminada
                    if (saltarSVC) continue;
                }

                lineasRestantes.add(linea);
            }
        } catch (IOException e) {
            throw new HotelException("Error al leer el archivo para eliminar: " + e.getMessage(), e);
        }

        // Reescribir el archivo sin la reserva eliminada
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo, false))) {
            for (String l : lineasRestantes) {
                writer.write(l);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new HotelException("Error al reescribir el archivo: " + e.getMessage(), e);
        }

        return encontrada;
    }

    // ─── Métodos privados de conversión ──────────────────────────────────────

    /** Convierte una Reserva a una línea CSV. */
    private String reservaALinea(Reserva r) {
        return String.join(SEPARADOR,
                r.getCodigoReserva(),
                r.getHuesped().getCedula(),
                r.getHuesped().getNombre(),
                r.getHuesped().getTelefono(),
                r.getHuesped().getCorreo(),
                String.valueOf(r.getHabitacion().getNumero()),
                String.valueOf(r.getHabitacion().getPiso()),
                r.getHabitacion().getTipo().getCodigo(),
                r.getHabitacion().getTipo().getDescripcion(),
                String.valueOf(r.getHabitacion().getTipo().getPrecioPorNoche()),
                r.getFechaEntrada().toString(),
                r.getFechaSalida().toString()
        );
    }

    /** Reconstruye una Reserva desde una línea CSV. */
    private Reserva lineaAReserva(String linea) {
        try {
            String[] p = linea.split(SEPARADOR, -1);
            // Índices: 0=codigo, 1=cedula, 2=nombre, 3=tel, 4=correo,
            //          5=numHab, 6=piso, 7=codTipo, 8=descTipo, 9=precio,
            //          10=entrada, 11=salida
            Huesped huesped = new Huesped(p[1], p[2], p[3], p[4]);
            TipoHabitacion tipo = new TipoHabitacion(p[7], p[8], Double.parseDouble(p[9]));
            Habitacion hab = new Habitacion(Integer.parseInt(p[5]), Integer.parseInt(p[6]), tipo);
            hab.setDisponible(false);  // Si está en archivo, está ocupada

            return new Reserva(p[0],
                    LocalDate.parse(p[10]),
                    LocalDate.parse(p[11]),
                    huesped, hab);
        } catch (Exception e) {
            System.err.println("Advertencia: no se pudo leer la línea de reserva: " + linea);
            return null;
        }
    }

    /** Reconstruye un ServicioAdicional desde una línea SVC. */
    private ServicioAdicional lineaAServicio(String linea) {
        // Formato: SVC:codigoReserva,nombre,precio,descripcion
        String sinPrefijo = linea.substring(PREFIJO_SVC.length());
        String[] p = sinPrefijo.split(SEPARADOR, -1);
        // p[0]=codigoReserva (no se necesita aquí), p[1]=nombre, p[2]=precio, p[3]=desc
        return new ServicioAdicional(p[1], Double.parseDouble(p[2]), p[3]);
    }
}