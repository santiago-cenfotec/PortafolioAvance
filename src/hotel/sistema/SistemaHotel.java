package hotel.sistema;

import hotel.modelo.*;

import java.util.ArrayList;
import java.util.List;

/**
 * COMPOSICIÓN: SistemaHotel crea y gestiona la lista maestra de Habitaciones.
 * En el avance final, la lógica de negocio fue migrada al Controlador (MVC).
 * Esta clase ahora cumple el rol de fábrica/repositorio en memoria de habitaciones,
 * que son inyectadas al Controlador al iniciar la aplicación.
 */
public class SistemaHotel {

    private final String nombre;
    private final List<Habitacion> habitaciones;  // Composición

    public SistemaHotel(String nombre) {
        this.nombre       = nombre;
        this.habitaciones = new ArrayList<>();
    }

    public void agregarHabitacion(Habitacion h) {
        habitaciones.add(h);
    }

    public List<Habitacion> getHabitaciones() {
        return habitaciones;
    }

    public String getNombre() {
        return nombre;
    }
}
