package modelo;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class Turno {
    private int numero;
    private LocalDate fecha;
    private int hora;
    private int minuto;
    private Cliente cliente;
    private Peluquero peluquero;
    private ArrayList<Servicio> servicios;
    private EstadoTurno estado;

    public Turno(int numero, LocalDate fecha, int hora, int minuto, Cliente cliente, Peluquero peluquero) {
        this.numero = numero;
        this.fecha = fecha;
        this.hora = hora;
        this.minuto = minuto;
        this.cliente = cliente;
        this.peluquero = peluquero;
        this.servicios = new ArrayList<>();
        this.estado = EstadoTurno.CONFIRMADO;
    }

    public Turno(int numero, LocalDate fecha, int hora, int minuto, Cliente cliente, Peluquero peluquero, EstadoTurno estado) {
        this.numero = numero;
        this.fecha = fecha;
        this.hora = hora;
        this.minuto = minuto;
        this.cliente = cliente;
        this.peluquero = peluquero;
        this.servicios = new ArrayList<>();
        this.estado = estado;
    }

    public int getNumero() {
        return numero;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public int getHora() {
        return hora;
    }

    public int getMinuto() {
        return minuto;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Peluquero getPeluquero() {
        return peluquero;
    }

    public ArrayList<Servicio> getServicios() {
        return servicios;
    }

    public EstadoTurno getEstado() {
        return estado;
    }

    public void agregarServicio(Servicio servicio) {
        servicios.add(servicio);
    }

    public boolean tieneServicio(Servicio servicio) {
        return servicios.contains(servicio);
    }

    public int calcularDuracionTotal() {
        int total = 0;
        for (Servicio s : servicios) {
            total = total + s.getDuracion();
        }
        return total;
    }

    public double calcularPrecioTotal() {
        double total = 0;
        for (Servicio s : servicios) {
            total = total + s.getPrecio();
        }
        return total;
    }

    public int getInicio() {
        return hora * 60 + minuto;
    }

    public int getFin() {
        return getInicio() + calcularDuracionTotal();
    }

    public boolean estaConfirmado() {
        return estado == EstadoTurno.CONFIRMADO;
    }

    public boolean seSuperpone(LocalDate otraFecha, int otroInicio, int otroFin) {
        if (!estaConfirmado() || !fecha.isEqual(otraFecha)) {
            return false;
        }
        return getInicio() < otroFin && otroInicio < getFin();
    }

    public String cancelar(LocalDate hoy) {
        if (!estaConfirmado()) {
            return "No se puede cancelar, el turno esta " + estado;
        }
        long dias = ChronoUnit.DAYS.between(hoy, fecha);
        if (dias < 1) {
            estado = EstadoTurno.CANCELACION_TARDIA;
            return "Turno cancelado. Queda registrado como cancelacion tardia.";
        }
        estado = EstadoTurno.CANCELADO;
        return "Turno cancelado.";
    }

    public boolean marcarAtendido() {
        if (estaConfirmado()) {
            estado = EstadoTurno.ATENDIDO;
            return true;
        }
        return false;
    }

    public boolean marcarAusente() {
        if (estaConfirmado()) {
            estado = EstadoTurno.AUSENTE;
            return true;
        }
        return false;
    }

    public void cambiarHorario(LocalDate nuevaFecha, int nuevaHora, int nuevoMinuto, Peluquero nuevoPeluquero) {
        fecha = nuevaFecha;
        hora = nuevaHora;
        minuto = nuevoMinuto;
        peluquero = nuevoPeluquero;
    }

    public String mostrarHora(int minutosTotales) {
        int h = minutosTotales / 60;
        int m = minutosTotales % 60;
        if (m < 10) {
            return h + ":0" + m;
        }
        return h + ":" + m;
    }

    public String getNombresServicios() {
        String nombres = "";
        for (Servicio s : servicios) {
            if (!nombres.isEmpty()) {
                nombres = nombres + ",";
            }
            nombres = nombres + s.getNombre();
        }
        return nombres;
    }

    public String convertirALineaArchivo() {
        return numero + ";" + fecha.getDayOfMonth() + ";" + fecha.getMonthValue() + ";" + fecha.getYear()
            + ";" + hora + ";" + minuto + ";" + cliente.getTelefono() + ";" + peluquero.getTelefono()
            + ";" + estado + ";" + getNombresServicios();
    }

    @Override
    public String toString() {
        return "Turno " + numero + " | " + fecha.getDayOfMonth() + "/" + fecha.getMonthValue() + "/" + fecha.getYear()
            + " de " + mostrarHora(getInicio()) + " a " + mostrarHora(getFin())
            + " | " + cliente.getNombreCompleto()
            + " | " + peluquero.getNombreCompleto()
            + " | " + getNombresServicios()
            + " | $" + (int) calcularPrecioTotal()
            + " | " + estado;
    }
}
