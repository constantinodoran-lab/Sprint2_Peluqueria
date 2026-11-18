package vista;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import modelo.Cliente;
import modelo.Especialidad;
import modelo.EstadoTurno;
import modelo.Peluquero;
import modelo.Servicio;
import modelo.Turno;

public class VistaPeluqueria {
    private Scanner teclado;

    public VistaPeluqueria() {
        teclado = new Scanner(System.in);
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    public int menuPrincipal() {
        System.out.println("\n===== PELUQUERIA - MENU PRINCIPAL =====");
        System.out.println("1. Turnos");
        System.out.println("2. Clientes");
        System.out.println("3. Peluqueros/as");
        System.out.println("4. Servicios");
        System.out.println("5. Consultas");
        System.out.println("0. Salir");
        return leerEntero("Opcion: ");
    }

    public int menuTurnos() {
        System.out.println("\n--- TURNOS ---");
        System.out.println("1. Sacar turno");
        System.out.println("2. Modificar turno");
        System.out.println("3. Cancelar turno");
        System.out.println("4. Marcar turno atendido");
        System.out.println("5. Marcar turno ausente");
        System.out.println("0. Volver");
        return leerEntero("Opcion: ");
    }

    public int menuClientes() {
        System.out.println("\n--- CLIENTES ---");
        System.out.println("1. Listar clientes");
        System.out.println("2. Modificar telefono / email");
        System.out.println("3. Eliminar cliente");
        System.out.println("0. Volver");
        return leerEntero("Opcion: ");
    }

    public int menuPeluqueros() {
        System.out.println("\n--- PELUQUEROS/AS ---");
        System.out.println("1. Registrar peluquero/a");
        System.out.println("2. Listar peluqueros/as");
        System.out.println("3. Buscar por especialidad");
        System.out.println("4. Modificar especialidad y horario");
        System.out.println("5. Eliminar peluquero/a");
        System.out.println("0. Volver");
        return leerEntero("Opcion: ");
    }

    public int menuServicios() {
        System.out.println("\n--- SERVICIOS ---");
        System.out.println("1. Registrar servicio");
        System.out.println("2. Listar servicios");
        System.out.println("3. Modificar precio");
        System.out.println("4. Eliminar servicio");
        System.out.println("0. Volver");
        return leerEntero("Opcion: ");
    }

    public int menuConsultas() {
        System.out.println("\n--- CONSULTAS ---");
        System.out.println("1. Agenda de un dia");
        System.out.println("2. Turnos de un cliente");
        System.out.println("3. Resumen de un dia");
        System.out.println("4. Listar todos los turnos");
        System.out.println("0. Volver");
        return leerEntero("Opcion: ");
    }

    public int leerEntero(String mensaje) {
        System.out.print(mensaje);
        while (!teclado.hasNextInt()) {
            teclado.nextLine();
            System.out.print("Tiene que ser un numero. " + mensaje);
        }
        int numero = teclado.nextInt();
        teclado.nextLine();
        return numero;
    }

    public String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return teclado.nextLine().trim();
    }

    private int diasDelMes(int mes, int anio) {
        if (mes == 2) {
            if (anio % 4 == 0) {
                return 29;
            }
            return 28;
        }
        if (mes == 4 || mes == 6 || mes == 9 || mes == 11) {
            return 30;
        }
        return 31;
    }

    public LocalDate leerFecha(String titulo) {
        System.out.println(titulo);
        int dia = leerEntero("Dia: ");
        int mes = leerEntero("Mes: ");
        int anio = leerEntero("Año: ");
        while (mes < 1 || mes > 12 || dia < 1 || dia > diasDelMes(mes, anio) || anio < 2000) {
            System.out.println("Fecha incorrecta, ingrese de nuevo.");
            dia = leerEntero("Dia: ");
            mes = leerEntero("Mes: ");
            anio = leerEntero("Año: ");
        }
        return LocalDate.of(anio, mes, dia);
    }

    public Especialidad elegirEspecialidad(boolean permitirTodas) {
        Especialidad[] lista = Especialidad.values();
        for (int i = 0; i < lista.length; i++) {
            System.out.println((i + 1) + ". " + lista[i]);
        }
        if (permitirTodas) {
            System.out.println("0. Ver todos");
        }
        int elegido = leerEntero("Especialidad: ");
        while (elegido < 0 || elegido > lista.length || (elegido == 0 && !permitirTodas)) {
            elegido = leerEntero("Opcion incorrecta. Especialidad: ");
        }
        if (elegido == 0) {
            return null;
        }
        return lista[elegido - 1];
    }

    public Peluquero elegirPeluquero(ArrayList<Peluquero> lista) {
        if (lista.isEmpty()) {
            System.out.println("No hay peluqueros/as para elegir.");
            return null;
        }
        System.out.println("\nPeluqueros/as:");
        for (int i = 0; i < lista.size(); i++) {
            Peluquero p = lista.get(i);
            System.out.println((i + 1) + ". " + p.getNombreCompleto() + " (" + p.getEspecialidad() + ", de " + p.getHoraEntrada() + " a " + p.getHoraSalida() + " hs)");
        }
        int elegido = leerEntero("Elija el peluquero/a: ");
        if (elegido < 1 || elegido > lista.size()) {
            System.out.println("Opcion incorrecta.");
            return null;
        }
        return lista.get(elegido - 1);
    }

    public ArrayList<Servicio> elegirServicios(ArrayList<Servicio> lista) {
        ArrayList<Servicio> elegidos = new ArrayList<>();
        System.out.println("\nServicios:");
        for (int i = 0; i < lista.size(); i++) {
            System.out.println((i + 1) + ". " + lista.get(i));
        }
        int elegido = leerEntero("Agregar servicio (0 para terminar): ");
        while (elegido != 0) {
            if (elegido < 1 || elegido > lista.size()) {
                System.out.println("Opcion incorrecta.");
            } else if (elegidos.contains(lista.get(elegido - 1))) {
                System.out.println("Ese servicio ya esta agregado.");
            } else {
                elegidos.add(lista.get(elegido - 1));
                System.out.println("Agregado: " + lista.get(elegido - 1).getNombre());
            }
            elegido = leerEntero("Agregar otro servicio (0 para terminar): ");
        }
        return elegidos;
    }

    public String nombreDia(LocalDate fecha) {
        String dia = fecha.getDayOfWeek().toString();
        if (dia.equals("MONDAY")) {
            return "Lunes";
        } else if (dia.equals("TUESDAY")) {
            return "Martes";
        } else if (dia.equals("WEDNESDAY")) {
            return "Miercoles";
        } else if (dia.equals("THURSDAY")) {
            return "Jueves";
        } else if (dia.equals("FRIDAY")) {
            return "Viernes";
        } else if (dia.equals("SATURDAY")) {
            return "Sabado";
        }
        return "Domingo";
    }

    public String formatearHora(int minutosTotales) {
        int h = minutosTotales / 60;
        int m = minutosTotales % 60;
        String texto = "";
        if (h < 10) {
            texto = "0";
        }
        texto = texto + h + ":";
        if (m < 10) {
            texto = texto + "0";
        }
        return texto + m;
    }

    public LocalDate elegirDia(ArrayList<LocalDate> dias) {
        System.out.println("\nDias disponibles (la peluqueria atiende de martes a sabado):");
        for (int i = 0; i < dias.size(); i++) {
            System.out.println((i + 1) + ". " + nombreDia(dias.get(i)) + " " + formatearFecha(dias.get(i)));
        }
        System.out.println("0. Cancelar");
        int elegido = leerEntero("Elija el dia: ");
        while (elegido < 0 || elegido > dias.size()) {
            elegido = leerEntero("Opcion incorrecta. Elija el dia: ");
        }
        if (elegido == 0) {
            return null;
        }
        return dias.get(elegido - 1);
    }

    public int elegirHorario(ArrayList<Integer> horarios) {
        System.out.println("\nHorarios libres:");
        String fila = "";
        for (int i = 0; i < horarios.size(); i++) {
            String numero = (i + 1) + ")";
            if (i + 1 < 10) {
                numero = " " + numero;
            }
            fila = fila + numero + " " + formatearHora(horarios.get(i)) + "    ";
            if ((i + 1) % 4 == 0) {
                System.out.println(fila);
                fila = "";
            }
        }
        if (!fila.isEmpty()) {
            System.out.println(fila);
        }
        System.out.println(" 0) Elegir otro dia");
        int elegido = leerEntero("Elija el horario: ");
        while (elegido < 0 || elegido > horarios.size()) {
            elegido = leerEntero("Opcion incorrecta. Elija el horario: ");
        }
        if (elegido == 0) {
            return -1;
        }
        return horarios.get(elegido - 1);
    }

    public boolean confirmarTurno(Cliente cliente, Peluquero peluquero, ArrayList<Servicio> servicios, LocalDate fecha, int inicio) {
        int duracion = 0;
        int total = 0;
        String nombres = "";
        for (Servicio s : servicios) {
            duracion = duracion + s.getDuracion();
            total = total + (int) s.getPrecio();
            if (!nombres.isEmpty()) {
                nombres = nombres + " + ";
            }
            nombres = nombres + s.getNombre();
        }
        System.out.println("\n========== RESUMEN DEL TURNO ==========");
        System.out.println("Cliente:     " + cliente.getNombreCompleto());
        System.out.println("Peluquero/a: " + peluquero.getNombreCompleto());
        System.out.println("Servicios:   " + nombres);
        System.out.println("Dia:         " + nombreDia(fecha) + " " + formatearFecha(fecha));
        System.out.println("Horario:     " + formatearHora(inicio) + " a " + formatearHora(inicio + duracion));
        System.out.println("Total:       $" + total);
        System.out.println("=======================================");
        String respuesta = leerTexto("Confirmar turno? (S/N): ");
        return respuesta.equalsIgnoreCase("s");
    }

    public Cliente pedirDatosClienteNuevo(String telefono) {
        System.out.println("Cliente nuevo, ingrese sus datos.");
        String nombre = leerTexto("Nombre: ");
        String apellido = leerTexto("Apellido: ");
        String email = leerTexto("Email (Enter si no tiene): ");
        return new Cliente(nombre, apellido, telefono, email);
    }

    public Peluquero pedirDatosPeluquero() {
        String nombre = leerTexto("Nombre: ");
        String apellido = leerTexto("Apellido: ");
        String telefono = leerTexto("Telefono: ");
        Especialidad especialidad = elegirEspecialidad(false);
        int entrada = leerEntero("Hora de entrada: ");
        int salida = leerEntero("Hora de salida: ");
        return new Peluquero(nombre, apellido, telefono, especialidad, entrada, salida);
    }

    public Servicio pedirDatosServicio() {
        String nombre = leerTexto("Nombre del servicio: ");
        System.out.println("Duracion aproximada:");
        System.out.println("1. 30 minutos");
        System.out.println("2. 1 hora");
        System.out.println("3. 1 hora y media");
        System.out.println("4. 2 horas");
        int opcion = leerEntero("Elija la duracion: ");
        while (opcion < 1 || opcion > 4) {
            opcion = leerEntero("Opcion incorrecta. Elija la duracion: ");
        }
        int precio = leerEntero("Precio: ");
        return new Servicio(nombre, opcion * 30, precio);
    }

    public void mostrarClientes(ArrayList<Cliente> lista) {
        if (lista.isEmpty()) {
            System.out.println("No hay clientes registrados.");
        }
        for (Cliente c : lista) {
            System.out.println(c);
        }
    }

    public void mostrarPeluqueros(ArrayList<Peluquero> lista) {
        if (lista.isEmpty()) {
            System.out.println("No hay peluqueros/as para mostrar.");
        }
        for (Peluquero p : lista) {
            System.out.println(p);
        }
    }

    public void mostrarServicios(ArrayList<Servicio> lista) {
        if (lista.isEmpty()) {
            System.out.println("No hay servicios registrados.");
        }
        for (Servicio s : lista) {
            System.out.println(s);
        }
    }

    public void mostrarTurnos(ArrayList<Turno> lista) {
        if (lista.isEmpty()) {
            System.out.println("No hay turnos para mostrar.");
        }
        for (Turno t : lista) {
            System.out.println(t);
        }
    }

    public String formatearFecha(LocalDate fecha) {
        return fecha.getDayOfMonth() + "/" + fecha.getMonthValue() + "/" + fecha.getYear();
    }

    public void mostrarAgenda(LocalDate fecha, TreeMap<Integer, ArrayList<Turno>> agenda, HashMap<String, Integer> porPeluquero) {
        System.out.println("\n--- AGENDA DEL " + formatearFecha(fecha) + " ---");
        if (agenda.isEmpty()) {
            System.out.println("No hay turnos ese dia.");
            return;
        }
        for (Map.Entry<Integer, ArrayList<Turno>> entrada : agenda.entrySet()) {
            for (Turno t : entrada.getValue()) {
                System.out.println(t);
            }
        }
        System.out.println("\nTurnos confirmados por peluquero/a:");
        for (Map.Entry<String, Integer> entrada : porPeluquero.entrySet()) {
            System.out.println(entrada.getKey() + ": " + entrada.getValue());
        }
    }

    public void mostrarResumen(LocalDate fecha, HashMap<EstadoTurno, Integer> porEstado, double recaudacion) {
        System.out.println("\n--- RESUMEN DEL " + formatearFecha(fecha) + " ---");
        for (EstadoTurno estado : EstadoTurno.values()) {
            System.out.println(estado + ": " + porEstado.get(estado));
        }
        System.out.println("Recaudacion (turnos atendidos): $" + recaudacion);
    }

    public void cerrar() {
        teclado.close();
    }
}
