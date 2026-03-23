package hotel.modelo;

/*
 ABSTRACCIÓN: Hace una habitación del hotel.

 ASOCIACIÓN con TipoHabitacion: Habitacion tiene una referencia a TipoHabitacion, pero ambas pueden existir por separado.
 */
public class Habitacion {

    private int numero;
    private int piso;
    private boolean disponible;
    private TipoHabitacion tipo;   // Asociación

    //Constructor
    public Habitacion(int numero, int piso, TipoHabitacion tipo) {
        this.numero = numero;
        this.piso = piso;
        this.tipo = tipo;
        this.disponible = true;   // Disponible por defecto al crearla
    }

    //Getters
    public int getNumero() {
        return numero;
    }
    public int getPiso() {
        return piso;
    }
    public boolean isDisponible() {
        return disponible;
    }
    public TipoHabitacion getTipo() {
        return tipo;
    }

    //Setters
    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    @Override
    public String toString() {
        String estado = disponible ? "Disponible" : "Ocupada";
        return "Hab. " + numero + " | Piso " + piso +
                " | " + tipo.getDescripcion() + " | " + estado;
    }
}
