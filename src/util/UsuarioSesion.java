package util;

public class UsuarioSesion {
    private static Long identificacion;
    private static String nombre;
    private static String apellido;
    private static String correo;

    public static void iniciarSesion(Long id, String nom, String ape, String correoUsuario) {
        identificacion = id;
        nombre = nom;
        apellido = ape;
        correo = correoUsuario;
    }


    public static void setIdentificacion(long id) {
        identificacion = id;
    }

    public static long getIdentificacion() {
        return identificacion;
    }

    public static void setNombre(String nom) {
        nombre = nom;
    }

    public static String getNombre() {
        return nombre;
    }

    public static String getApellido() {
        return apellido;
    }

    public static String getCorreo() {
        return correo;
    }

    public static void cerrarSesion() {
        identificacion = null;
        nombre = null;
        apellido = null;
        correo = null;
    }


	
	
}
