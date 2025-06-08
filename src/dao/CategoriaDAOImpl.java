package dao;

import config.DatabaseConnection;
import model.Categoria;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAOImpl implements GenericDAO<Categoria> {
    @Override
    public void crear(Categoria categoria) throws SQLException {
        String sql = "INSERT INTO categoria(nombre, descripcion) VALUES(?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, categoria.getNombre());
            statement.setString(2, categoria.getDescripcion());
            statement.execute();
        }
    }

    @Override
    public List<Categoria> listar() throws SQLException {
        List<Categoria> categorias = new ArrayList<>();
        String sqlQuery = "SELECT id, nombre FROM categoria";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String descripcion = rs.getString("descripcion");
                System.out.println("ID: " + id + ", model.Categoria: " + nombre);
                categorias.add(new Categoria(id, nombre, descripcion));
            }
            return categorias;
        }
    }

    @Override
    public Categoria leer(int id) throws SQLException {
        String sqlQuery = "SELECT * FROM categoria WHERE id=?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                String nombre = rs.getString("nombre");
                String descripcion = rs.getString("descripcion");
                Categoria categoria = new Categoria(id, nombre, descripcion);
                System.out.println("*** " + nombre + " ***\n " + descripcion);
                return categoria;
            } else {
                System.out.println("No se encontro ninguna categoria");
                return null;
            }
        }
    }

    @Override
    public boolean eliminar(int id) throws SQLException {
        String sqlDelete = "DELETE FROM categoria WHERE id=?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sqlDelete)) {

            statement.setInt(1, id);
            int filasAfectadas = statement.executeUpdate();
            if (filasAfectadas > 0) {
                return true;
            }
            }
        return false;
    }


    @Override
    public boolean actualizar(Categoria categoria) throws SQLException {
        String sqlUpdate = "UPDATE categoria SET nombre=?, descripcion=? WHERE id=?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sqlUpdate)) {

            statement.setString(1, categoria.getNombre());
            statement.setString(2, categoria.getDescripcion());
            statement.setInt(3, categoria.getId());
            int filasAfectadas = statement.executeUpdate();

            if (filasAfectadas > 0) {
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean existeNombre(String nombre) throws SQLException {
        String sqlQuery = "SELECT COUNT(nombre) FROM categoria WHERE nombre=?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sqlQuery)) {

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        }
    }
}


