package controlador;

import java.time.LocalDate;
import java.util.ArrayList;
import modelo.Cliente;
import modelo.Especialidad;
import modelo.Peluqueria;
import modelo.Peluquero;
import modelo.Servicio;
import modelo.Turno;
import modelo.Validaciones;
import persistencia.GestorArchivos;
import vista.VistaPeluqueria;

public class ControladorPeluqueria {
    private Peluqueria peluqueria;
    private VistaPeluqueria vista;
    private GestorArchivos archivos;

    public ControladorPeluqueria(Peluqueria peluqueria, VistaPeluqueria vista, GestorArchivos archivos) {
        this.peluqueria = peluqueria;
        this.vista = vista;
        this.archivos = archivos;
    }

    public void iniciar() {
        if (archivos.hayDatosGuardados()) {
            vista.mostrarMensaje(archivos.cargar(peluqueria));
        } else {
            cargarDatosDeEjemplo();
            vista.mostrarMensaje("No habia datos guardados, se cargaron datos de ejemplo.");
            vista.mostrarMensaje(archivos.guardar(peluqueria));
        }
        int opcion;
        do {
            opcion = vista.menuPrincipal();
            if (opcion == 1) {
                gestionarTurnos();
            } else if (opcion == 2) {
                gestionarClientes();
            } else if (opcion == 3) {
                gestionarPeluqueros();
            } else if (opcion == 4) {
                gestionarServicios();
            } else if (opcion == 5) {
                gestionarConsultas();
            } else if (opcion != 0) {
                vista.mostrarMensaje("Opcion incorrecta.");
            }
        } while (opcion != 0);
        vista.mostrarMensaje(archivos.guardar(peluqueria));
        vista.mostrarMensaje("Fin del programa.");
        vista.cerrar();
    }

    private void guardar() {
        String resultado = archivos.guardar(peluqueria);
        if (!resultado.equals("Datos guardados.")) {
            vista.mostrarMensaje(resultado);
        }
    }

    private void gestionarTurnos() {
        int opcion;
        do {
            opcion = vista.menuTurnos();
            if (opcion == 1) {
                sacarTurno();
            } else if (opcion == 2) {
                modificarTurno();
            } else if (opcion == 3) {
                int numero = vista.leerEntero("Numero de turno a cancelar: ");
                vista.mostrarMensaje(peluqueria.cancelarTurno(numero, LocalDate.now()));
            } else if (opcion == 4) {
                int numero = vista.leerEntero("Numero de turno atendido: ");
                vista.mostrarMensaje(peluqueria.marcarAtendido(numero));
            } else if (opcion == 5) {
                int numero = vista.leerEntero("Numero de turno ausente: ");
                vista.mostrarMensaje(peluqueria.marcarAusente(numero));
            } else if (opcion != 0) {
                vista.mostrarMensaje("Opcion incorrecta.");
            }
            if (opcion >= 1 && opcion <= 5) {
                guardar();
            }
        } while (opcion != 0);
    }

    private void gestionarClientes() {
        int opcion;
        do {
            opcion = vista.menuClientes();
            if (opcion == 1) {
                vista.mostrarClientes(peluqueria.getClientes());
            } else if (opcion == 2) {
                String telefono = vista.leerTexto("Telefono actual del cliente: ");
                String nuevoTelefono = vista.leerTexto("Nuevo telefono (Enter para no cambiar): ");
                if (nuevoTelefono.isEmpty()) {
                    nuevoTelefono = telefono;
                }
                String nuevoEmail = vista.leerTexto("Nuevo email (Enter si no tiene): ");
                vista.mostrarMensaje(peluqueria.modificarCliente(telefono, nuevoTelefono, nuevoEmail));
                guardar();
            } else if (opcion == 3) {
                String telefono = vista.leerTexto("Telefono del cliente a eliminar: ");
                vista.mostrarMensaje(peluqueria.eliminarCliente(telefono));
                guardar();
            } else if (opcion != 0) {
                vista.mostrarMensaje("Opcion incorrecta.");
            }
        } while (opcion != 0);
    }

    private void gestionarPeluqueros() {
        int opcion;
        do {
            opcion = vista.menuPeluqueros();
            if (opcion == 1) {
                vista.mostrarMensaje(peluqueria.registrarPeluquero(vista.pedirDatosPeluquero()));
                guardar();
            } else if (opcion == 2) {
                vista.mostrarPeluqueros(peluqueria.getPeluqueros());
            } else if (opcion == 3) {
                Especialidad especialidad = vista.elegirEspecialidad(false);
                vista.mostrarPeluqueros(peluqueria.peluquerosPorEspecialidad(especialidad));
            } else if (opcion == 4) {
                String telefono = vista.leerTexto("Telefono del peluquero/a: ");
                Especialidad especialidad = vista.elegirEspecialidad(false);
                int entrada = vista.leerEntero("Nueva hora de entrada: ");
                int salida = vista.leerEntero("Nueva hora de salida: ");
                vista.mostrarMensaje(peluqueria.modificarPeluquero(telefono, especialidad, entrada, salida));
                guardar();
            } else if (opcion == 5) {
                String telefono = vista.leerTexto("Telefono del peluquero/a a eliminar: ");
                vista.mostrarMensaje(peluqueria.eliminarPeluquero(telefono));
                guardar();
            } else if (opcion != 0) {
                vista.mostrarMensaje("Opcion incorrecta.");
            }
        } while (opcion != 0);
    }

    private void gestionarServicios() {
        int opcion;
        do {
            opcion = vista.menuServicios();
            if (opcion == 1) {
                vista.mostrarMensaje(peluqueria.registrarServicio(vista.pedirDatosServicio()));
                guardar();
            } else if (opcion == 2) {
                vista.mostrarServicios(peluqueria.getServicios());
            } else if (opcion == 3) {
                String nombre = vista.leerTexto("Nombre del servicio: ");
                int precio = vista.leerEntero("Nuevo precio: ");
                vista.mostrarMensaje(peluqueria.modificarPrecioServicio(nombre, precio));
                guardar();
            } else if (opcion == 4) {
                String nombre = vista.leerTexto("Nombre del servicio a eliminar: ");
                vista.mostrarMensaje(peluqueria.eliminarServicio(nombre));
                guardar();
            } else if (opcion != 0) {
                vista.mostrarMensaje("Opcion incorrecta.");
            }
        } while (opcion != 0);
    }

    private void gestionarConsultas() {
        int opcion;
        do {
            opcion = vista.menuConsultas();
            if (opcion == 1) {
                LocalDate fecha = vista.leerFecha("Fecha de la agenda");
                vista.mostrarAgenda(fecha, peluqueria.agendaDelDia(fecha), peluqueria.turnosPorPeluquero(fecha));
            } else if (opcion == 2) {
                String telefono = vista.leerTexto("Telefono del cliente: ");
                Cliente cliente = peluqueria.buscarCliente(telefono);
                if (cliente == null) {
                    vista.mostrarMensaje("No existe un cliente con ese telefono.");
                } else {
                    vista.mostrarTurnos(peluqueria.turnosDeCliente(cliente));
                }
            } else if (opcion == 3) {
                LocalDate fecha = vista.leerFecha("Fecha del resumen");
                vista.mostrarResumen(fecha, peluqueria.turnosPorEstado(fecha), peluqueria.recaudacionDelDia(fecha));
            } else if (opcion == 4) {
                vista.mostrarTurnos(peluqueria.getTurnos());
            } else if (opcion != 0) {
                vista.mostrarMensaje("Opcion incorrecta.");
            }
        } while (opcion != 0);
    }

    private Cliente obtenerCliente() {
        String telefono = vista.leerTexto("Telefono del cliente: ");
        Cliente cliente = peluqueria.buscarCliente(telefono);
        if (cliente != null) {
            vista.mostrarMensaje("Cliente encontrado: " + cliente.getNombreCompleto());
            return cliente;
        }
        if (!Validaciones.telefonoValido(telefono)) {
            vista.mostrarMensaje("El telefono tiene que tener solo numeros (entre 7 y 13).");
            return null;
        }
        cliente = vista.pedirDatosClienteNuevo(telefono);
        String resultado = peluqueria.registrarCliente(cliente);
        vista.mostrarMensaje(resultado);
        if (!resultado.equals("Cliente registrado.")) {
            return null;
        }
        return cliente;
    }

    private Peluquero elegirPeluquero() {
        vista.mostrarMensaje("Filtrar peluqueros/as por especialidad:");
        Especialidad especialidad = vista.elegirEspecialidad(true);
        if (especialidad == null) {
            return vista.elegirPeluquero(peluqueria.getPeluqueros());
        }
        return vista.elegirPeluquero(peluqueria.peluquerosPorEspecialidad(especialidad));
    }

    private void sacarTurno() {
        Cliente cliente = obtenerCliente();
        if (cliente == null) {
            return;
        }
        ArrayList<Servicio> elegidos = vista.elegirServicios(peluqueria.getServicios());
        if (elegidos.isEmpty()) {
            vista.mostrarMensaje("Hay que elegir al menos un servicio.");
            return;
        }
        int duracion = 0;
        for (Servicio s : elegidos) {
            duracion = duracion + s.getDuracion();
        }
        Peluquero peluquero = vista.elegirPeluquero(peluqueria.getPeluqueros());
        if (peluquero == null) {
            return;
        }
        LocalDate fecha = null;
        int inicio = -1;
        while (inicio == -1) {
            fecha = vista.elegirDia(peluqueria.diasDisponibles(LocalDate.now(), 10));
            if (fecha == null) {
                vista.mostrarMensaje("Turno cancelado.");
                return;
            }
            ArrayList<Integer> horarios = peluqueria.horariosDisponibles(peluquero, fecha, duracion, 0);
            if (horarios.isEmpty()) {
                vista.mostrarMensaje(peluquero.getNombreCompleto() + " no tiene horarios libres ese dia. Elija otro.");
            } else {
                inicio = vista.elegirHorario(horarios);
            }
        }
        if (!vista.confirmarTurno(cliente, peluquero, elegidos, fecha, inicio)) {
            vista.mostrarMensaje("Turno no registrado.");
            return;
        }
        vista.mostrarMensaje(peluqueria.sacarTurno(cliente, peluquero, elegidos, fecha, inicio / 60, inicio % 60, LocalDate.now()));
    }

    private void modificarTurno() {
        int numero = vista.leerEntero("Numero de turno a modificar: ");
        Turno turno = peluqueria.buscarTurno(numero);
        if (turno == null) {
            vista.mostrarMensaje("No existe ese turno.");
            return;
        }
        if (!turno.estaConfirmado()) {
            vista.mostrarMensaje("Solo se pueden modificar turnos confirmados.");
            return;
        }
        vista.mostrarMensaje("Turno actual: " + turno);
        Peluquero peluquero = elegirPeluquero();
        if (peluquero == null) {
            return;
        }
        LocalDate fecha = null;
        int inicio = -1;
        while (inicio == -1) {
            fecha = vista.elegirDia(peluqueria.diasDisponibles(LocalDate.now(), 10));
            if (fecha == null) {
                vista.mostrarMensaje("No se modifico el turno.");
                return;
            }
            ArrayList<Integer> horarios = peluqueria.horariosDisponibles(peluquero, fecha, turno.calcularDuracionTotal(), numero);
            if (horarios.isEmpty()) {
                vista.mostrarMensaje(peluquero.getNombreCompleto() + " no tiene horarios libres ese dia. Elija otro.");
            } else {
                inicio = vista.elegirHorario(horarios);
            }
        }
        vista.mostrarMensaje(peluqueria.modificarTurno(numero, peluquero, fecha, inicio / 60, inicio % 60, LocalDate.now()));
    }

    private void cargarDatosDeEjemplo() {
        Cliente c1 = new Cliente("Ana", "Gomez", "3534111111", "ana.gomez@gmail.com");
        Cliente c2 = new Cliente("Juan", "Perez", "3534222222", "juanperez@hotmail.com");
        Cliente c3 = new Cliente("Lucia", "Fernandez", "3534333333", "");
        peluqueria.registrarCliente(c1);
        peluqueria.registrarCliente(c2);
        peluqueria.registrarCliente(c3);

        Peluquero p1 = new Peluquero("Carla", "Ruiz", "3534999001", Especialidad.COLORACION, 9, 17);
        Peluquero p2 = new Peluquero("Martin", "Sosa", "3534999002", Especialidad.BARBERIA, 12, 20);
        Peluquero p3 = new Peluquero("Sofia", "Lopez", "3534999003", Especialidad.CORTE, 9, 18);
        peluqueria.registrarPeluquero(p1);
        peluqueria.registrarPeluquero(p2);
        peluqueria.registrarPeluquero(p3);

        Servicio corte = new Servicio("Corte", 30, 8000);
        Servicio color = new Servicio("Coloracion", 90, 25000);
        Servicio brushing = new Servicio("Brushing", 30, 7000);
        Servicio barba = new Servicio("Barba", 30, 5000);
        peluqueria.registrarServicio(corte);
        peluqueria.registrarServicio(color);
        peluqueria.registrarServicio(brushing);
        peluqueria.registrarServicio(barba);

        LocalDate hoy = LocalDate.now();
        LocalDate dia = hoy.plusDays(1);
        while (!peluqueria.atiendeEseDia(dia)) {
            dia = dia.plusDays(1);
        }

        ArrayList<Servicio> s1 = new ArrayList<>();
        s1.add(corte);
        s1.add(color);
        peluqueria.sacarTurno(c1, p1, s1, dia, 10, 0, hoy);

        ArrayList<Servicio> s2 = new ArrayList<>();
        s2.add(corte);
        s2.add(barba);
        peluqueria.sacarTurno(c2, p2, s2, dia, 14, 30, hoy);

        ArrayList<Servicio> s3 = new ArrayList<>();
        s3.add(brushing);
        peluqueria.sacarTurno(c3, p3, s3, dia, 9, 0, hoy);
    }
}
