import controlador.ControladorPeluqueria;
import modelo.Peluqueria;
import persistencia.GestorArchivos;
import vista.VistaPeluqueria;

public class Main {

    public static void main(String[] args) {
        Peluqueria peluqueria = new Peluqueria("Peluqueria Estilo", 9, 20);
        VistaPeluqueria vista = new VistaPeluqueria();
        GestorArchivos archivos = new GestorArchivos("datos");
        ControladorPeluqueria controlador = new ControladorPeluqueria(peluqueria, vista, archivos);
        controlador.iniciar();
    }
}
