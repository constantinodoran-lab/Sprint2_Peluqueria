package modelo;

public class Servicio {
    private String nombre;
    private int duracion;
    private double precio;

    public Servicio(String nombre, int duracion, double precio) {
        this.nombre = nombre;
        this.duracion = duracion;
        this.precio = precio;
    }

    public String getNombre() {
        return nombre;
    }

    public int getDuracion() {
        return duracion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String convertirALineaArchivo() {
        return nombre + ";" + duracion + ";" + precio;
    }

    public static Servicio crearDesdeLinea(String linea) {
        String[] datos = linea.split(";");
        int duracion = Integer.parseInt(datos[1]);
        double precio = Double.parseDouble(datos[2]);
        return new Servicio(datos[0], duracion, precio);
    }

    @Override
    public String toString() {
        return nombre + " (" + duracion + " min - $" + (int) precio + ")";
    }
}
