package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    public static final String url = "jdbc:mysql://localhost:3306/java-test";
    public static final String usuario = "root";
    public static final String clave = "";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("No se pudo cargar el driver");
        }
    }
    public static Connection getConnection() throws  SQLException{
        if (url == null || url.isEmpty() || usuario == null || usuario.isEmpty() || clave == null){
            throw new SQLException("Faltan datos para la conexion");
        }
        return DriverManager.getConnection(url, usuario, clave);
    }
}
