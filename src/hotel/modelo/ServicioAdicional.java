package hotel.modelo;

/**
 * ABSTRACCIÓN: Representa un servicio extra (spa, desayuno, etc.)
 * que puede añadirse a una reserva.
 *
 * DEPENDENCIA: Reserva depende de esta clase para calcular el total.
 * ServicioAdicional puede existir sin ninguna Reserva activa.
 */
public class ServicioAdicional {

    private String nombre;
    private double precio;
    private String descripcion;

    // Constructor
    public ServicioAdicional(String nombre, double precio, String descripcion) {
        this.nombre      = nombre;
        this.precio      = precio;
        this.descripcion = descripcion;
    }

    // Getters
    public String getNombre()      { return nombre; }
    public double getPrecio()      { return precio; }
    public String getDescripcion() { return descripcion; }

    // Setter con validación
    public void setPrecio(double precio) {
        if (precio >= 0) {
            this.precio = precio;
        }
    }

    @Override
    public String toString() {
        return nombre + " ($" + String.format("%.2f", precio) + ") — " + descripcion;
    }
}