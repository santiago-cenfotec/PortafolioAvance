package hotel.modelo;

/**
 * ABSTRACCIÓN: Encapsula el tipo y precio de una habitación.
 * Habitacion delega la lógica de precio a esta clase (separación de responsabilidades).
 */
public class TipoHabitacion {

    private String codigo;
    private String descripcion;
    private double precioPorNoche;

    // Constructor completo
    public TipoHabitacion(String codigo, String descripcion, double precioPorNoche) {
        this.codigo          = codigo;
        this.descripcion     = descripcion;
        this.precioPorNoche  = precioPorNoche;
    }

    // Getters
    public String getCodigo()          { return codigo; }
    public String getDescripcion()     { return descripcion; }
    public double getPrecioPorNoche()  { return precioPorNoche; }

    // Setters con validación
    public void setCodigo(String codigo) {
        if (codigo != null && !codigo.isBlank()) {
            this.codigo = codigo;
        }
    }

    public void setPrecioPorNoche(double precio) {
        if (precio > 0) {
            this.precioPorNoche = precio;
        }
    }

    @Override
    public String toString() {
        return descripcion + " ($" + String.format("%.2f", precioPorNoche) + "/noche)";
    }
}
