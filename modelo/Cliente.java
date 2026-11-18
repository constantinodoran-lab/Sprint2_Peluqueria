package modelo;

public class Cliente extends Persona {
    private String email;

    public Cliente(String nombre, String apellido, String telefono, String email) {
        super(nombre, apellido, telefono);
        this.email = email.trim().toLowerCase();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email.trim().toLowerCase();
    }

    public String convertirALineaArchivo() {
        return telefono + ";" + email + ";" + nombre + ";" + apellido;
    }

    public static Cliente crearDesdeLinea(String linea) {
        String[] datos = linea.split(";");
        return new Cliente(datos[2], datos[3], datos[0], datos[1]);
    }

    @Override
    public String getDescripcion() {
        return "Cliente: " + getNombreCompleto() + " - Tel: " + telefono + " - Email: " + email;
    }
}
