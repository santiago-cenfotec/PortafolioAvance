package hotel.modelo;

/*
 ABSTRACCIÓN: Representa solo los datos esenciales de un huésped que son relevantes para el sistema de reservas.
 ENCAPSULAMIENTO: Todos los atributos son privados; se acceden únicamente mediante getters y setters controlados.
 */
public class Huesped {

    // Atributos Privados / Encapsulamiento
    private String cedula;
    private String nombre;
    private String telefono;
    private String correo;

    // Constructor Completo
    public Huesped(String cedula, String nombre, String telefono, String correo) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.telefono = telefono;
        this.correo = correo;
    }

    // Getters
    public String getCedula() {
        return cedula;
    }
    public String getNombre() {
        return nombre;
    }
    public String getTelefono() {
        return telefono;
    }
    public String getCorreo() {
        return correo;
    }

    // Setters
    public void setTelefono(String telefono) {
        if (telefono != null && !telefono.isBlank()) {
            this.telefono = telefono;
        }
    }

    public void setCorreo(String correo) {
        if (correo != null && correo.contains("@")) {
            this.correo = correo;
        }
    }

    @Override
    public String toString() {
        return nombre + " (Cédula: " + cedula + ")";
    }
}