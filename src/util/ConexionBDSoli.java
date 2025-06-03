package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBDSoli {
	private static final String URL = "jdbc:oracle:thin:@localhost:1521:XE";
    private static final String USUARIO = "solicitanteGestorPrestamos";
    private static final String CLAVE = "solicitante";
    
    public static Connection obtenerConexionSolicitante() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, CLAVE);
    }
}
