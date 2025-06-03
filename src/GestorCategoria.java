import config.DatabaseConnection;
import dao.GenericDAO;
import model.Categoria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GestorCategoria {

    public void agregarCategoria(String nombre, String descripcion) {
        try (Connection connection = DatabaseConnection.getConnection()){
            String sqlInsert = "INSERT INTO categoria(nombre, descripcion) VALUES(?, ?)";
           try (PreparedStatement statement = connection.prepareStatement(sqlInsert)) {
               statement.setString(1, nombre);
               statement.setString(2, descripcion);
               int filasActualizadas = statement.executeUpdate();
               System.out.println("Filas actualizadas: " + filasActualizadas);
           } catch (Exception e) {
               System.out.println("Error al insertar los datos");
           }
        }catch (SQLException e) {
            System.out.println("Error al conectarse a la bd");
        }
    }

    public void listarCategorias(){
        try (Connection connection = DatabaseConnection.getConnection()){
            String sqlQuery = "SELECT id, nombre FROM categoria";
            try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
                ResultSet rs = statement.executeQuery();
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String nombre = rs.getString("nombre");
                    System.out.println("ID: " + id + ", model.Categoria: " + nombre);
                }
            } catch (Exception e) {
                System.out.println("Error al ejecutar la query");
            }
        }catch (SQLException e){
            System.out.println("Error al conectarse a la bd");
        }
    }

    public void mostrarCategoria(int id){
        try (Connection connection = DatabaseConnection.getConnection()){
            String sqlQuery = "SELECT * FROM categoria WHERE id=?";
            try (PreparedStatement statement = connection.prepareStatement(sqlQuery)){
                statement.setInt(1, id);
                ResultSet rs = statement.executeQuery();
                if (rs.next()) {
                    String nombre = rs.getString("nombre");
                    String descripcion = rs.getString("descripcion");
                    System.out.println("*** " + nombre + " ***\n " + descripcion);
                } else {
                    System.out.println("No se encontro ninguna categoria");
                }

            } catch (Exception e) {
                System.out.println("Error en la consulta a la bd");
            }
        }catch (SQLException e) {
            System.out.println("Erro al conectarse a la base de datos");
        }
    }

    public void actualizarCategoria (int id, String nombre, String descripcion){
        try (Connection connection = DatabaseConnection.getConnection()){
            String sqlUpdate = "UPDATE categoria SET nombre=?, descripcion=? WHERE id=?";
            try (PreparedStatement statement = connection.prepareStatement(sqlUpdate)){
                statement.setString(1, nombre);
                statement.setString(2, descripcion);
                statement.setInt(3, id);
                int filasAfectadas = statement.executeUpdate();
                if (filasAfectadas > 0) {
                    System.out.println("✅ model.Categoria actualizado con éxito.");
                } else {
                    System.out.println("❌ No se encontró la categoria con ID: " + id);
                }
            } catch (Exception e){
                System.out.println("No se pudo realizar la actualizacion en la bd");
            }
        } catch (SQLException e) {
            System.out.println("Error en la conexion a la bd");
        }
    }

    public void eliminarCategoria(int id){
        String sqlDelete = "DELETE FROM categoria WHERE id=?";
    try (Connection connection = DatabaseConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement(sqlDelete)) {
        statement.setInt(1, id);
        int filasAfectadas = statement.executeUpdate();
        if (filasAfectadas > 0 ) {
            System.out.println("model.Categoria con id: " +  id + " eliminada satisfactorimente");
        } else {
            System.out.println("No se encontro la categoria con el id seleccionado");
        }

    } catch (SQLException e) {
        System.out.println("Error en la conexion a la base de datos");
    }
    }
}
