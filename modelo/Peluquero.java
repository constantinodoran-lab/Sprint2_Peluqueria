package modelo;

public class Peluquero extends Persona {
    private Especialidad especialidad;
    private int horaEntrada;
    private int horaSalida;

    public Peluquero(String nombre, String apellido, String telefono, Especialidad especialidad, int horaEntrada, int horaSalida) {
        super(nombre, apellido, telefono);
        this.especialidad = especialidad;
        this.horaEntrada = horaEntrada;
        this.horaSalida = horaSalida;
    }

    public Especialidad getEspecialidad() {
        return especialidad;
    }

    public int getHoraEntrada() {
        return horaEntrada;
    }

    public int getHoraSalida() {
        return horaSalida;
    }

    public void setEspecialidad(Especialidad especialidad) {
        this.especialidad = especialidad;
    }

    public void setHorario(int horaEntrada, int horaSalida) {
        this.horaEntrada = horaEntrada;
        this.horaSalida = horaSalida;
    }

    public boolean tieneEspecialidad(Especialidad buscada) {
        return especialidad == buscada;
    }

    public boolean trabajaEnHorario(int inicio, int fin) {
        return inicio >= horaEntrada * 60 && fin <= horaSalida * 60;
    }

    public String convertirALineaArchivo() {
        return telefono + ";" + nombre + ";" + apellido + ";" + especialidad + ";" + horaEntrada + ";" + horaSalida;
    }

    public static Peluquero crearDesdeLinea(String linea) {
        String[] datos = linea.split(";");
        Especialidad especialidad = Especialidad.valueOf(datos[3]);
        int entrada = Integer.parseInt(datos[4]);
        int salida = Integer.parseInt(datos[5]);
        return new Peluquero(datos[1], datos[2], datos[0], especialidad, entrada, salida);
    }

    @Override
    public String getDescripcion() {
        return "Peluquero/a: " + getNombreCompleto() + " - Tel: " + telefono + " - " + especialidad + " - Horario: " + horaEntrada + " a " + horaSalida + " hs";
    }
}
