package hotel.dao;

import hotel.modelo.HotelException;
import hotel.modelo.Huesped;

import java.util.List;
import java.util.Optional;

/**
 * PATRÓN DAO — INTERFAZ:
 * Define el contrato para la persistencia de Huéspedes.
 */
public interface IHuespedDAO {

    /** Guarda un huésped en la fuente de datos. */
    void guardar(Huesped huesped) throws HotelException;

    /** Retorna todos los huéspedes almacenados. */
    List<Huesped> listarTodos() throws HotelException;

    /** Busca un huésped por su cédula. */
    Optional<Huesped> buscarPorCedula(String cedula) throws HotelException;

    /** Elimina un huésped por su cédula. Retorna true si fue eliminado. */
    boolean eliminar(String cedula) throws HotelException;
}