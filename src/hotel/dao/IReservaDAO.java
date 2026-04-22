package hotel.dao;

import hotel.modelo.HotelException;
import hotel.modelo.Reserva;

import java.util.List;
import java.util.Optional;

/**
 * PATRÓN DAO (Data Access Object) — INTERFAZ:
 * Define el contrato para cualquier implementación de persistencia de Reservas.
 * Permite cambiar la fuente de datos (archivo, base de datos, memoria) sin tocar
 * el resto del sistema — principio de inversión de dependencias.
 */
public interface IReservaDAO {

    /** Guarda una reserva en la fuente de datos. */
    void guardar(Reserva reserva) throws HotelException;

    /** Retorna todas las reservas almacenadas. */
    List<Reserva> listarTodas() throws HotelException;

    /** Busca una reserva por su código. */
    Optional<Reserva> buscarPorCodigo(String codigo) throws HotelException;

    /** Elimina una reserva por su código. Retorna true si fue eliminada. */
    boolean eliminar(String codigo) throws HotelException;
}