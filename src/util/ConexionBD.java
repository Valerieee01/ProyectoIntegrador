package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {
    private static final String URL = "jdbc:oracle:thin:@192.168.254.215:1521:orcl";
    private static final String USUARIO = "baseProyectoLV";
    private static final String CLAVE = "baseProyectoLV";
    
    public static Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, CLAVE);
    }
}
