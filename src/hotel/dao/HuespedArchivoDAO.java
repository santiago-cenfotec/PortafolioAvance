package hotel.dao;

import hotel.modelo.HotelException;
import hotel.modelo.Huesped;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * PATRÓN DAO — IMPLEMENTACIÓN CON ARCHIVO:
 * Persiste los huéspedes en un archivo de texto plano (huespedes.txt).
 * Formato CSV: cedula,nombre,telefono,correo
 */
public class HuespedArchivoDAO implements IHuespedDAO {

    private static final String ARCHIVO = "huespedes.txt";
    private static final String SEPARADOR = ",";

    /** Guarda un huésped al final del archivo. Si ya existe la cédula, no duplica. */
    @Override
    public void guardar(Huesped huesped) throws HotelException {
        // Verificar si ya existe para no duplicar
        Optional<Huesped> existente = buscarPorCedula(huesped.getCedula());
        if (existente.isPresent()) return;

        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(ARCHIVO, true))) {
            writer.write(huespedALinea(huesped));
            writer.newLine();
        } catch (IOException e) {
            throw new HotelException("Error al guardar el huésped: " + e.getMessage(), e);
        }
    }

    /** Lee todos los huéspedes del archivo. */
    @Override
    public List<Huesped> listarTodos() throws HotelException {
        List<Huesped> lista = new ArrayList<>();
        File archivo = new File(ARCHIVO);

        if (!archivo.exists()) return lista;

        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                if (!linea.isBlank()) {
                    Huesped h = lineaAHuesped(linea);
                    if (h != null) lista.add(h);
                }
            }
        } catch (IOException e) {
            throw new HotelException("Error al leer huéspedes: " + e.getMessage(), e);
        }

        return lista;
    }

    /** Busca un huésped por cédula. */
    @Override
    public Optional<Huesped> buscarPorCedula(String cedula) throws HotelException {
        return listarTodos().stream()
                .filter(h -> h.getCedula().equals(cedula))
                .findFirst();
    }

    /** Elimina un huésped reescribiendo el archivo sin él. */
    @Override
    public boolean eliminar(String cedula) throws HotelException {
        File archivo = new File(ARCHIVO);
        if (!archivo.exists()) return false;

        List<String> restantes = new ArrayList<>();
        boolean encontrado = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                if (linea.isBlank()) continue;
                String[] p = linea.split(SEPARADOR, -1);
                if (p[0].equals(cedula)) {
                    encontrado = true;
                } else {
                    restantes.add(linea);
                }
            }
        } catch (IOException e) {
            throw new HotelException("Error al leer el archivo: " + e.getMessage(), e);
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo, false))) {
            for (String l : restantes) {
                writer.write(l);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new HotelException("Error al reescribir el archivo: " + e.getMessage(), e);
        }

        return encontrado;
    }

    // ─── Conversión privada ───────────────────────────────────────────────────

    private String huespedALinea(Huesped h) {
        return String.join(SEPARADOR, h.getCedula(), h.getNombre(), h.getTelefono(), h.getCorreo());
    }

    private Huesped lineaAHuesped(String linea) {
        try {
            String[] p = linea.split(SEPARADOR, -1);
            return new Huesped(p[0], p[1], p[2], p[3]);
        } catch (Exception e) {
            System.err.println("Advertencia: línea de huésped inválida: " + linea);
            return null;
        }
    }
}
