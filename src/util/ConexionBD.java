package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {
	private static final String URL = "jdbc:oracle:thin:@localhost:1521:XE";
    private static final String USUARIO = "AdminGestrorPrestamos";
    private static final String CLAVE = "admin";
    
    public static Connection obtenerConexionAdmin() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, CLAVE);
    }
}
