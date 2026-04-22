package hotel.modelo;

/**
 * HERENCIA: HabitacionVIP extiende Habitacion.
 * Hereda todos los atributos y comportamientos de Habitacion,
 * y agrega características propias de una habitación premium.
 *
 * POLIMORFISMO: Sobreescribe toString() para mostrar sus extras.
 */
public class HabitacionVIP extends Habitacion {

    private boolean tieneJacuzzi;
    private boolean tieneBalcon;
    private String vistaDescripcion;  // Ej: "Vista al mar", "Vista a la montaña"

    /**
     * Constructor que llama al constructor de la superclase (Habitacion)
     * usando super() y agrega los atributos propios de VIP.
     */
    public HabitacionVIP(int numero, int piso, TipoHabitacion tipo,
                         boolean tieneJacuzzi, boolean tieneBalcon, String vistaDescripcion) {
        super(numero, piso, tipo);  // Llama al constructor de Habitacion
        this.tieneJacuzzi = tieneJacuzzi;
        this.tieneBalcon = tieneBalcon;
        this.vistaDescripcion = vistaDescripcion;
    }

    // Getters propios de VIP
    public boolean isTieneJacuzzi() { return tieneJacuzzi; }
    public boolean isTieneBalcon() { return tieneBalcon; }
    public String getVistaDescripcion() { return vistaDescripcion; }

    /**
     * POLIMORFISMO (override): Sobreescribe el toString() de Habitacion
     * para incluir los extras VIP. Llama a super.toString() para reutilizar
     * el comportamiento base.
     */
    @Override
    public String toString() {
        StringBuilder extras = new StringBuilder();
        if (tieneJacuzzi) extras.append(" | Jacuzzi");
        if (tieneBalcon)  extras.append(" | Balcón");
        if (vistaDescripcion != null && !vistaDescripcion.isBlank()) {
            extras.append(" | Vista: ").append(vistaDescripcion);
        }
        return "[VIP] " + super.toString() + extras;
    }
}