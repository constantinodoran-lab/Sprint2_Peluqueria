package modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

public class Peluqueria {
    private String nombre;
    private int horaApertura;
    private int horaCierre;
    private ArrayList<Cliente> clientes;
    private ArrayList<Peluquero> peluqueros;
    private ArrayList<Servicio> servicios;
    private ArrayList<Turno> turnos;
    private int ultimoNumeroTurno;

    public Peluqueria(String nombre, int horaApertura, int horaCierre) {
        this.nombre = nombre;
        this.horaApertura = horaApertura;
        this.horaCierre = horaCierre;
        clientes = new ArrayList<>();
        peluqueros = new ArrayList<>();
        servicios = new ArrayList<>();
        turnos = new ArrayList<>();
        ultimoNumeroTurno = 0;
    }

    public String getNombre() {
        return nombre;
    }

    public ArrayList<Cliente> getClientes() {
        return clientes;
    }

    public ArrayList<Peluquero> getPeluqueros() {
        return peluqueros;
    }

    public ArrayList<Servicio> getServicios() {
        return servicios;
    }

    public ArrayList<Turno> getTurnos() {
        return turnos;
    }

    public String registrarCliente(Cliente cliente) {
        if (!Validaciones.textoValido(cliente.getNombre()) || !Validaciones.textoValido(cliente.getApellido())) {
            return "Nombre y apellido son obligatorios y no pueden tener ; ni ,";
        }
        if (!Validaciones.telefonoValido(cliente.getTelefono())) {
            return "El telefono tiene que tener solo numeros (entre 7 y 13).";
        }
        if (!Validaciones.emailValido(cliente.getEmail())) {
            return "El email no es valido.";
        }
        if (buscarCliente(cliente.getTelefono()) != null) {
            return "Ya existe un cliente con ese telefono.";
        }
        clientes.add(cliente);
        return "Cliente registrado.";
    }

    public String registrarPeluquero(Peluquero peluquero) {
        if (!Validaciones.textoValido(peluquero.getNombre()) || !Validaciones.textoValido(peluquero.getApellido())) {
            return "Nombre y apellido son obligatorios y no pueden tener ; ni ,";
        }
        if (!Validaciones.telefonoValido(peluquero.getTelefono())) {
            return "El telefono tiene que tener solo numeros (entre 7 y 13).";
        }
        if (buscarPeluquero(peluquero.getTelefono()) != null) {
            return "Ya existe un peluquero/a con ese telefono.";
        }
        if (peluquero.getEspecialidad() == null) {
            return "Falta la especialidad.";
        }
        String errorHorario = validarHorarioPeluquero(peluquero.getHoraEntrada(), peluquero.getHoraSalida());
        if (!errorHorario.isEmpty()) {
            return errorHorario;
        }
        peluqueros.add(peluquero);
        return "Peluquero/a registrado/a.";
    }

    public String registrarServicio(Servicio servicio) {
        if (!Validaciones.textoValido(servicio.getNombre())) {
            return "El nombre del servicio es obligatorio y no puede tener ; ni ,";
        }
        if (servicio.getDuracion() <= 0 || servicio.getPrecio() <= 0) {
            return "La duracion y el precio tienen que ser mayores a cero.";
        }
        if (buscarServicio(servicio.getNombre()) != null) {
            return "Ya existe un servicio con ese nombre.";
        }
        servicios.add(servicio);
        return "Servicio registrado.";
    }

    public String validarHorarioPeluquero(int entrada, int salida) {
        if (!Validaciones.horaValida(entrada) || !Validaciones.horaValida(salida)) {
            return "Las horas tienen que estar entre 0 y 23.";
        }
        if (entrada >= salida) {
            return "La hora de entrada tiene que ser menor a la de salida.";
        }
        if (entrada < horaApertura || salida > horaCierre) {
            return "El horario tiene que estar dentro del horario del local (" + horaApertura + " a " + horaCierre + " hs).";
        }
        return "";
    }

    public Cliente buscarCliente(String telefono) {
        for (Cliente c : clientes) {
            if (c.getTelefono().equals(telefono)) {
                return c;
            }
        }
        return null;
    }

    public Peluquero buscarPeluquero(String telefono) {
        for (Peluquero p : peluqueros) {
            if (p.getTelefono().equals(telefono)) {
                return p;
            }
        }
        return null;
    }

    public Servicio buscarServicio(String nombreServicio) {
        for (Servicio s : servicios) {
            if (s.getNombre().equalsIgnoreCase(nombreServicio)) {
                return s;
            }
        }
        return null;
    }

    public Turno buscarTurno(int numero) {
        for (Turno t : turnos) {
            if (t.getNumero() == numero) {
                return t;
            }
        }
        return null;
    }

    public ArrayList<Peluquero> peluquerosPorEspecialidad(Especialidad especialidad) {
        ArrayList<Peluquero> resultado = new ArrayList<>();
        for (Peluquero p : peluqueros) {
            if (p.tieneEspecialidad(especialidad)) {
                resultado.add(p);
            }
        }
        return resultado;
    }

    public ArrayList<Turno> turnosDeCliente(Cliente cliente) {
        ArrayList<Turno> resultado = new ArrayList<>();
        for (Turno t : turnos) {
            if (t.getCliente() == cliente) {
                resultado.add(t);
            }
        }
        return resultado;
    }

    public String modificarCliente(String telefonoActual, String nuevoTelefono, String nuevoEmail) {
        Cliente cliente = buscarCliente(telefonoActual);
        if (cliente == null) {
            return "No existe un cliente con ese telefono.";
        }
        if (!Validaciones.telefonoValido(nuevoTelefono)) {
            return "El telefono tiene que tener solo numeros (entre 7 y 13).";
        }
        if (!nuevoTelefono.equals(telefonoActual) && buscarCliente(nuevoTelefono) != null) {
            return "Ya existe otro cliente con ese telefono.";
        }
        if (!Validaciones.emailValido(nuevoEmail.trim().toLowerCase())) {
            return "El email no es valido.";
        }
        cliente.setTelefono(nuevoTelefono);
        cliente.setEmail(nuevoEmail);
        return "Cliente modificado: " + cliente;
    }

    public String eliminarCliente(String telefono) {
        Cliente cliente = buscarCliente(telefono);
        if (cliente == null) {
            return "No existe un cliente con ese telefono.";
        }
        if (!turnosDeCliente(cliente).isEmpty()) {
            return "No se puede eliminar porque tiene turnos registrados.";
        }
        clientes.remove(cliente);
        return "Cliente eliminado.";
    }

    public String modificarPeluquero(String telefono, Especialidad especialidad, int entrada, int salida) {
        Peluquero peluquero = buscarPeluquero(telefono);
        if (peluquero == null) {
            return "No existe un peluquero/a con ese telefono.";
        }
        String errorHorario = validarHorarioPeluquero(entrada, salida);
        if (!errorHorario.isEmpty()) {
            return errorHorario;
        }
        for (Turno t : turnos) {
            if (t.getPeluquero() == peluquero && t.estaConfirmado()) {
                if (t.getInicio() < entrada * 60 || t.getFin() > salida * 60) {
                    return "No se puede cambiar el horario: el turno " + t.getNumero() + " quedaria afuera.";
                }
            }
        }
        peluquero.setEspecialidad(especialidad);
        peluquero.setHorario(entrada, salida);
        return "Peluquero/a modificado/a: " + peluquero;
    }

    public String eliminarPeluquero(String telefono) {
        Peluquero peluquero = buscarPeluquero(telefono);
        if (peluquero == null) {
            return "No existe un peluquero/a con ese telefono.";
        }
        for (Turno t : turnos) {
            if (t.getPeluquero() == peluquero) {
                return "No se puede eliminar porque tiene turnos registrados.";
            }
        }
        peluqueros.remove(peluquero);
        return "Peluquero/a eliminado/a.";
    }

    public String modificarPrecioServicio(String nombreServicio, double nuevoPrecio) {
        Servicio servicio = buscarServicio(nombreServicio);
        if (servicio == null) {
            return "No existe ese servicio.";
        }
        if (nuevoPrecio <= 0) {
            return "El precio tiene que ser mayor a cero.";
        }
        servicio.setPrecio(nuevoPrecio);
        return "Precio actualizado: " + servicio;
    }

    public String eliminarServicio(String nombreServicio) {
        Servicio servicio = buscarServicio(nombreServicio);
        if (servicio == null) {
            return "No existe ese servicio.";
        }
        for (Turno t : turnos) {
            if (t.tieneServicio(servicio)) {
                return "No se puede eliminar porque esta en turnos registrados.";
            }
        }
        servicios.remove(servicio);
        return "Servicio eliminado.";
    }

    public boolean atiendeEseDia(LocalDate fecha) {
        String dia = fecha.getDayOfWeek().toString();
        if (dia.equals("SUNDAY") || dia.equals("MONDAY")) {
            return false;
        }
        return true;
    }

    public boolean peluqueroLibre(Peluquero peluquero, LocalDate fecha, int inicio, int fin, int numeroIgnorado) {
        for (Turno t : turnos) {
            if (t.getPeluquero() == peluquero && t.getNumero() != numeroIgnorado) {
                if (t.seSuperpone(fecha, inicio, fin)) {
                    return false;
                }
            }
        }
        return true;
    }

    public ArrayList<LocalDate> diasDisponibles(LocalDate hoy, int cantidad) {
        ArrayList<LocalDate> dias = new ArrayList<>();
        LocalDate dia = hoy.plusDays(1);
        while (dias.size() < cantidad) {
            if (atiendeEseDia(dia)) {
                dias.add(dia);
            }
            dia = dia.plusDays(1);
        }
        return dias;
    }

    public ArrayList<Integer> horariosDisponibles(Peluquero peluquero, LocalDate fecha, int duracion, int numeroIgnorado) {
        ArrayList<Integer> horarios = new ArrayList<>();
        int inicio = peluquero.getHoraEntrada() * 60;
        while (inicio + duracion <= peluquero.getHoraSalida() * 60) {
            int fin = inicio + duracion;
            if (inicio >= horaApertura * 60 && fin <= horaCierre * 60 && peluqueroLibre(peluquero, fecha, inicio, fin, numeroIgnorado)) {
                horarios.add(inicio);
            }
            inicio = inicio + 30;
        }
        return horarios;
    }

    public String validarTurno(Peluquero peluquero, LocalDate fecha, int hora, int minuto, int duracion, LocalDate hoy, int numeroIgnorado) {
        String errores = "";
        int inicio = hora * 60 + minuto;
        int fin = inicio + duracion;
        if (fecha.isBefore(hoy)) {
            errores = errores + "- La fecha ya paso.\n";
        }
        if (!atiendeEseDia(fecha)) {
            errores = errores + "- La peluqueria atiende de martes a sabado.\n";
        }
        if (!Validaciones.horaValida(hora) || minuto < 0 || minuto > 59) {
            errores = errores + "- La hora no es valida.\n";
        }
        if (inicio < horaApertura * 60 || fin > horaCierre * 60) {
            errores = errores + "- Fuera del horario de atencion (" + horaApertura + " a " + horaCierre + " hs).\n";
        }
        if (!peluquero.trabajaEnHorario(inicio, fin)) {
            errores = errores + "- " + peluquero.getNombreCompleto() + " no trabaja en ese horario.\n";
        }
        if (!peluqueroLibre(peluquero, fecha, inicio, fin, numeroIgnorado)) {
            errores = errores + "- " + peluquero.getNombreCompleto() + " ya tiene un turno en ese horario.\n";
        }
        return errores;
    }

    public String sacarTurno(Cliente cliente, Peluquero peluquero, ArrayList<Servicio> elegidos, LocalDate fecha, int hora, int minuto, LocalDate hoy) {
        if (cliente == null || peluquero == null) {
            return "El turno necesita un cliente y un peluquero/a.";
        }
        if (elegidos.isEmpty()) {
            return "El turno necesita al menos un servicio.";
        }
        int duracion = 0;
        for (Servicio s : elegidos) {
            duracion = duracion + s.getDuracion();
        }
        String errores = validarTurno(peluquero, fecha, hora, minuto, duracion, hoy, 0);
        if (!errores.isEmpty()) {
            return "No se pudo sacar el turno:\n" + errores;
        }
        ultimoNumeroTurno++;
        Turno nuevo = new Turno(ultimoNumeroTurno, fecha, hora, minuto, cliente, peluquero);
        for (Servicio s : elegidos) {
            nuevo.agregarServicio(s);
        }
        turnos.add(nuevo);
        return "Turno confirmado. Numero de turno: " + nuevo.getNumero() + " (guardelo para cancelarlo o modificarlo).";
    }

    public void agregarTurnoGuardado(Turno turno) {
        turnos.add(turno);
        if (turno.getNumero() > ultimoNumeroTurno) {
            ultimoNumeroTurno = turno.getNumero();
        }
    }

    public String modificarTurno(int numero, Peluquero peluquero, LocalDate fecha, int hora, int minuto, LocalDate hoy) {
        Turno turno = buscarTurno(numero);
        if (turno == null) {
            return "No existe el turno " + numero;
        }
        if (!turno.estaConfirmado()) {
            return "Solo se pueden modificar turnos confirmados.";
        }
        String errores = validarTurno(peluquero, fecha, hora, minuto, turno.calcularDuracionTotal(), hoy, numero);
        if (!errores.isEmpty()) {
            return "No se pudo modificar el turno:\n" + errores;
        }
        turno.cambiarHorario(fecha, hora, minuto, peluquero);
        return "Turno modificado: " + turno;
    }

    public String cancelarTurno(int numero, LocalDate hoy) {
        Turno turno = buscarTurno(numero);
        if (turno == null) {
            return "No existe el turno " + numero;
        }
        return turno.cancelar(hoy);
    }

    public String marcarAtendido(int numero) {
        Turno turno = buscarTurno(numero);
        if (turno == null) {
            return "No existe el turno " + numero;
        }
        if (turno.marcarAtendido()) {
            return "Turno marcado como atendido.";
        }
        return "Solo se pueden marcar turnos confirmados.";
    }

    public String marcarAusente(int numero) {
        Turno turno = buscarTurno(numero);
        if (turno == null) {
            return "No existe el turno " + numero;
        }
        if (turno.marcarAusente()) {
            return "Turno marcado como ausente.";
        }
        return "Solo se pueden marcar turnos confirmados.";
    }

    public TreeMap<Integer, ArrayList<Turno>> agendaDelDia(LocalDate fecha) {
        TreeMap<Integer, ArrayList<Turno>> agenda = new TreeMap<>();
        for (Turno t : turnos) {
            if (t.getFecha().isEqual(fecha)) {
                if (!agenda.containsKey(t.getInicio())) {
                    agenda.put(t.getInicio(), new ArrayList<>());
                }
                agenda.get(t.getInicio()).add(t);
            }
        }
        return agenda;
    }

    public HashMap<String, Integer> turnosPorPeluquero(LocalDate fecha) {
        HashMap<String, Integer> cantidad = new HashMap<>();
        for (Turno t : turnos) {
            if (t.getFecha().isEqual(fecha) && t.estaConfirmado()) {
                String nombrePeluquero = t.getPeluquero().getNombreCompleto();
                if (cantidad.containsKey(nombrePeluquero)) {
                    cantidad.put(nombrePeluquero, cantidad.get(nombrePeluquero) + 1);
                } else {
                    cantidad.put(nombrePeluquero, 1);
                }
            }
        }
        return cantidad;
    }

    public HashMap<EstadoTurno, Integer> turnosPorEstado(LocalDate fecha) {
        HashMap<EstadoTurno, Integer> cantidad = new HashMap<>();
        for (EstadoTurno estado : EstadoTurno.values()) {
            cantidad.put(estado, 0);
        }
        for (Turno t : turnos) {
            if (t.getFecha().isEqual(fecha)) {
                cantidad.put(t.getEstado(), cantidad.get(t.getEstado()) + 1);
            }
        }
        return cantidad;
    }

    public double recaudacionDelDia(LocalDate fecha) {
        double total = 0;
        for (Turno t : turnos) {
            if (t.getFecha().isEqual(fecha) && t.getEstado() == EstadoTurno.ATENDIDO) {
                total = total + t.calcularPrecioTotal();
            }
        }
        return total;
    }
}
