package persistencia;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import modelo.Cliente;
import modelo.EstadoTurno;
import modelo.Peluqueria;
import modelo.Peluquero;
import modelo.Servicio;
import modelo.Turno;

public class GestorArchivos {
    private String carpeta;
    private int lineasConError;

    public GestorArchivos(String carpeta) {
        this.carpeta = carpeta;
        this.lineasConError = 0;
    }

    public boolean hayDatosGuardados() {
        File archivo = new File(carpeta + "/clientes.csv");
        return archivo.exists();
    }

    public String guardar(Peluqueria peluqueria) {
        File directorio = new File(carpeta);
        if (!directorio.exists()) {
            directorio.mkdir();
        }
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(carpeta + "/clientes.csv"));
            for (Cliente c : peluqueria.getClientes()) {
                writer.write(c.convertirALineaArchivo());
                writer.newLine();
            }
            writer.close();

            writer = new BufferedWriter(new FileWriter(carpeta + "/peluqueros.csv"));
            for (Peluquero p : peluqueria.getPeluqueros()) {
                writer.write(p.convertirALineaArchivo());
                writer.newLine();
            }
            writer.close();

            writer = new BufferedWriter(new FileWriter(carpeta + "/servicios.csv"));
            for (Servicio s : peluqueria.getServicios()) {
                writer.write(s.convertirALineaArchivo());
                writer.newLine();
            }
            writer.close();

            writer = new BufferedWriter(new FileWriter(carpeta + "/turnos.csv"));
            for (Turno t : peluqueria.getTurnos()) {
                writer.write(t.convertirALineaArchivo());
                writer.newLine();
            }
            writer.close();
            return "Datos guardados.";
        } catch (IOException e) {
            return "No se pudieron guardar los datos: " + e.getMessage();
        }
    }

    public String cargar(Peluqueria peluqueria) {
        lineasConError = 0;
        try {
            cargarClientes(peluqueria);
            cargarPeluqueros(peluqueria);
            cargarServicios(peluqueria);
            cargarTurnos(peluqueria);
        } catch (IOException e) {
            return "No se pudieron leer los datos: " + e.getMessage();
        }
        if (lineasConError > 0) {
            return "Datos cargados. Se ignoraron " + lineasConError + " lineas con errores.";
        }
        return "Datos cargados.";
    }

    private BufferedReader abrir(String nombreArchivo) throws IOException {
        File archivo = new File(carpeta + "/" + nombreArchivo);
        if (!archivo.exists()) {
            return null;
        }
        return new BufferedReader(new FileReader(archivo));
    }

    private void cargarClientes(Peluqueria peluqueria) throws IOException {
        BufferedReader reader = abrir("clientes.csv");
        if (reader == null) {
            return;
        }
        String linea;
        while ((linea = reader.readLine()) != null) {
            try {
                Cliente cliente = Cliente.crearDesdeLinea(linea);
                if (!peluqueria.registrarCliente(cliente).equals("Cliente registrado.")) {
                    lineasConError++;
                }
            } catch (Exception e) {
                lineasConError++;
            }
        }
        reader.close();
    }

    private void cargarPeluqueros(Peluqueria peluqueria) throws IOException {
        BufferedReader reader = abrir("peluqueros.csv");
        if (reader == null) {
            return;
        }
        String linea;
        while ((linea = reader.readLine()) != null) {
            try {
                Peluquero peluquero = Peluquero.crearDesdeLinea(linea);
                if (!peluqueria.registrarPeluquero(peluquero).equals("Peluquero/a registrado/a.")) {
                    lineasConError++;
                }
            } catch (Exception e) {
                lineasConError++;
            }
        }
        reader.close();
    }

    private void cargarServicios(Peluqueria peluqueria) throws IOException {
        BufferedReader reader = abrir("servicios.csv");
        if (reader == null) {
            return;
        }
        String linea;
        while ((linea = reader.readLine()) != null) {
            try {
                Servicio servicio = Servicio.crearDesdeLinea(linea);
                if (!peluqueria.registrarServicio(servicio).equals("Servicio registrado.")) {
                    lineasConError++;
                }
            } catch (Exception e) {
                lineasConError++;
            }
        }
        reader.close();
    }

    private void cargarTurnos(Peluqueria peluqueria) throws IOException {
        BufferedReader reader = abrir("turnos.csv");
        if (reader == null) {
            return;
        }
        String linea;
        while ((linea = reader.readLine()) != null) {
            try {
                Turno turno = crearTurnoDesdeLinea(linea, peluqueria);
                if (turno == null) {
                    lineasConError++;
                } else {
                    peluqueria.agregarTurnoGuardado(turno);
                }
            } catch (Exception e) {
                lineasConError++;
            }
        }
        reader.close();
    }

    private Turno crearTurnoDesdeLinea(String linea, Peluqueria peluqueria) {
        String[] datos = linea.split(";");
        int numero = Integer.parseInt(datos[0]);
        LocalDate fecha = LocalDate.of(Integer.parseInt(datos[3]), Integer.parseInt(datos[2]), Integer.parseInt(datos[1]));
        int hora = Integer.parseInt(datos[4]);
        int minuto = Integer.parseInt(datos[5]);
        Cliente cliente = peluqueria.buscarCliente(datos[6]);
        Peluquero peluquero = peluqueria.buscarPeluquero(datos[7]);
        EstadoTurno estado = EstadoTurno.valueOf(datos[8]);
        if (cliente == null || peluquero == null || peluqueria.buscarTurno(numero) != null) {
            return null;
        }
        Turno turno = new Turno(numero, fecha, hora, minuto, cliente, peluquero, estado);
        String[] nombresServicios = datos[9].split(",");
        for (String nombreServicio : nombresServicios) {
            Servicio servicio = peluqueria.buscarServicio(nombreServicio);
            if (servicio == null) {
                return null;
            }
            turno.agregarServicio(servicio);
        }
        return turno;
    }
}
