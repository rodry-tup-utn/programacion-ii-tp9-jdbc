package dao;

import com.mysql.cj.exceptions.WrongArgumentException;
import config.DatabaseConnection;
import model.Categoria;
import model.Producto;

import java.security.InvalidParameterException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAOImpl implements GenericDAO<Producto> {
    private final CategoriaDAOImpl categoriaDAO;

    public ProductoDAOImpl(CategoriaDAOImpl categoriaDAO) {
        this.categoriaDAO = categoriaDAO;

    }

    public void crear(Producto producto, Connection connection) throws SQLException {
        String sqlInsert = "INSERT INTO producto(nombre, descripcion, precio, cantidad, id_categoria) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sqlInsert)) {
            statement.setString(1, producto.getNombre());
            statement.setString(2, producto.getDescripcion());
            statement.setDouble(3, producto.getPrecio());
            statement.setInt(4, producto.getCantidad());
            statement.setInt(5, producto.getIdCategoria());
            statement.execute();
        }

    }

    public Producto leer(int id, Connection connection) throws SQLException{
        String sqlQuery = "SELECT * FROM producto WHERE id_producto=?";
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                String nombre = rs.getString("nombre");
                String descripcion = rs.getString("descripcion");
                double precio = rs.getDouble("precio");
                int cantidad = rs.getInt("cantidad");
                int idCategoria = rs.getInt("id_categoria");

                Categoria categoria = categoriaDAO.leer(idCategoria, connection);
                Producto producto = new Producto(id, nombre, descripcion, precio, cantidad, categoria);
                return producto;

            }
        }
        throw new SQLException("No se encontró el producto con el id: " + id);
    }

    public boolean actualizar(Producto producto, Connection connection) throws SQLException {
        String sqlUpdate = "UPDATE producto SET nombre=?, descripcion=?, precio=?, cantidad=?, id_categoria=? WHERE id_producto=?";
        try (PreparedStatement statement = connection.prepareStatement(sqlUpdate)) {
            statement.setString(1, producto.getNombre());
            statement.setString(2, producto.getDescripcion());
            statement.setDouble(3, producto.getPrecio());
            statement.setInt(4, producto.getCantidad());
            statement.setInt(5, producto.getIdCategoria());
            statement.setInt(6, producto.getId());

            int filasAfectadas = statement.executeUpdate();
            if (filasAfectadas > 0) {
                System.out.println("Producto actualizado correctamente");
                return true;
            }
        }
        System.out.println("No se pudo actualizar el producto");
        return false;
    }

    public boolean eliminar(int id, Connection connection) throws SQLException{
        String sqlDelete = "DELETE FROM proucto WHERE id=?";
        try (PreparedStatement statement = connection.prepareStatement(sqlDelete)) {

            statement.setInt(1, id);
            int filasAfectadas = statement.executeUpdate();
            if (filasAfectadas > 0) {
                return true;
            }
        }
        return false;
    }

    public List<Producto> listar(Connection connection) throws SQLException{
        List<Producto> productos = new ArrayList<>();
        String sqlQuery = "SELECT * FROM producto";
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String descripcion = rs.getString("descripcion");
                double precio = rs.getDouble("precio");
                int cantidad = rs.getInt("cantidad");
                int idCategoria = rs.getInt("id_categoria");

                Categoria categoria = categoriaDAO.leer(idCategoria, connection);
                Producto producto = new Producto(id, nombre, descripcion, precio, cantidad, categoria);
                productos.add(producto);
            }
            return productos;
        }
    }

    public List<Producto> listarPorCategoria(int idCategoriaFiltro, Connection connection) throws SQLException {
        List<Producto> productos = new ArrayList<>();

        if (!categoriaDAO.existeCategoria(idCategoriaFiltro)) {
            return productos; // Retorna una lista vacía si la categoría no existe
        }

        Categoria categoriaAsociada = categoriaDAO.leer(idCategoriaFiltro, connection);

        String sqlQuery = "SELECT id_producto, nombre, descripcion, precio, cantidad, id_categoria FROM producto WHERE id_categoria=?";
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) { // Usa la conexión pasada
            statement.setInt(1, idCategoriaFiltro);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                int id_producto = rs.getInt("id_producto"); // Asegúrate de que el nombre de la columna sea correcto en tu DB
                String nombre = rs.getString("nombre");
                String descripcion = rs.getString("descripcion");
                double precio = rs.getDouble("precio");
                int cantidad = rs.getInt("cantidad");
                Producto producto = new Producto(id_producto, nombre, descripcion, precio, cantidad, categoriaAsociada);
                productos.add(producto);
            }
            return productos;
        }
    }

    public void mostrarProductoConCategoria(int id, Connection connection) throws SQLException{
        String sqlQuery = "SELECT\n" +
                "    p.nombre AS nombre_producto,\n" +
                "    p.precio,\n" +
                "    p.cantidad,\n" +
                "    c.nombre AS nombre_categoria\n" + // <-- Removed the trailing comma here
                " FROM\n" +
                "    producto p\n" +
                "INNER JOIN\n" +
                "    categoria c ON p.id_categoria = c.id \n" +
                "WHERE p.id_producto=?";
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                String nombre = rs.getString("nombre_producto");
                double precio = rs.getDouble("precio");
                int cantidad = rs.getInt("cantidad");
                String nombreCategoria = rs.getString("nombre_categoria");
                System.out.println(nombre + " - $" + precio + " - " + cantidad  +" - " + nombreCategoria);
            }
        }
    }
}


