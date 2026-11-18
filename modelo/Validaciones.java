package modelo;

public class Validaciones {

    public static boolean textoValido(String texto) {
        if (texto == null || texto.isBlank()) {
            return false;
        }
        return !texto.contains(";") && !texto.contains(",");
    }

    public static boolean telefonoValido(String telefono) {
        if (telefono == null || telefono.length() < 7 || telefono.length() > 13) {
            return false;
        }
        for (int i = 0; i < telefono.length(); i++) {
            if (!Character.isDigit(telefono.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean emailValido(String email) {
        if (email.isEmpty()) {
            return true;
        }
        if (email.contains(";") || email.contains(",") || email.contains(" ")) {
            return false;
        }
        int posicion = email.indexOf("@");
        return posicion > 0 && posicion < email.length() - 1;
    }

    public static boolean horaValida(int hora) {
        return hora >= 0 && hora <= 23;
    }
}
