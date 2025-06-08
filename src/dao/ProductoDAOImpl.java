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
    private CategoriaDAOImpl categoriaDAO;

    public ProductoDAOImpl(CategoriaDAOImpl categoriaDAO) {
        this.categoriaDAO = categoriaDAO;
    }

    private Categoria crearCategoria(int id) throws SQLException  {
        if (existeCategoria(id)) {
        return categoriaDAO.leer(id);
        } else {
            throw new SQLException("Categoria id" + id + " no encontrada" );
        }
    }

    public void crear(Producto producto) throws SQLException {
        String sqlInsert = "INSERT INTO producto(nombre, descripcion, precio, cantidad, categoria) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sqlInsert)) {
            statement.setString(1, producto.getNombre());
            statement.setString(2, producto.getDescripcion());
            statement.setDouble(3, producto.getPrecio());
            statement.setInt(4, producto.getCantidad());
            statement.setInt(5, producto.getIdCategoria());
            statement.execute();
        }

    }

    public Producto leer(int id) throws SQLException{
        String sqlQuery = "SELECT * FROM producto WHERE id=?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                String nombre = rs.getString("nombre");
                String descripcion = rs.getString("descripcion");
                double precio = rs.getDouble("precio");
                int cantidad = rs.getInt("cantidad");
                int idCategoria = rs.getInt("id_categoria");

                Categoria categoria = crearCategoria(idCategoria);
                Producto producto = new Producto(id, nombre, descripcion, precio, cantidad, categoria);
                return producto;

            }
        }
        throw new SQLException("No se encontró el producto con el id: " + id);
    }

    public boolean actualizar(Producto producto) throws SQLException {

        if (producto == null) {
            System.out.println("El producto no puede ser nulo");
            return false;
        }
        if (!existeCategoria(producto.getIdCategoria()) || producto.getIdCategoria() == 0 || producto.getCategoria() == null){
            System.out.println("Error relacionado con la categoria enviada");
            return false;
        }

        String sqlUpdate = "UPDATE producto SET nombre=?, descripcion=?, precio=?, cantidad=?, categoria=?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sqlUpdate)) {
            statement.setString(1, producto.getNombre());
            statement.setString(2, producto.getDescripcion());
            statement.setDouble(3, producto.getPrecio());
            statement.setInt(4, producto.getCantidad());
            statement.setInt(5, producto.getIdCategoria());

            int filasAfectadas = statement.executeUpdate();
            if (filasAfectadas > 0) {
                System.out.println("Producto actualizado correctamente");
                return true;
            }
        }
        System.out.println("No se pudo actualizar el producto");
        return false;
    }

    public boolean eliminar(int id) throws SQLException{
        String sqlDelete = "DELETE FROM proucto WHERE id=?";
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

    public List<Producto> listar() throws SQLException{
        List<Producto> productos = new ArrayList<>();
        String sqlQuery = "SELECT * FROM producto";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String descripcion = rs.getString("descripcion");
                double precio = rs.getDouble("precio");
                int cantidad = rs.getInt("cantidad");
                int idCategoria = rs.getInt("id_categoria");

                Categoria categoria = crearCategoria(idCategoria);
                Producto producto = new Producto(id, nombre, descripcion, precio, cantidad, categoria);
                productos.add(producto);
            }
            return productos;
        }
    }

    public List<Producto> listarPorCategoria(int id) throws SQLException {
        List<Producto> productos = new ArrayList<>();
        boolean categoriaCreada = false;
        Categoria categoria = null;
        String sqlQuery = "SELECT * FROM producto WHERE id_categoria=?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                int id_producto = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String descripcion = rs.getString("descripcion");
                double precio = rs.getDouble("precio");
                int cantidad = rs.getInt("cantidad");
                int idCategoria = rs.getInt("id_categoria");

                if (!categoriaCreada) {
                    categoria = crearCategoria(idCategoria);
                    categoriaCreada = true;
                }

                if (categoria == null) {
                    System.out.println("No se pudo cargar la categoria");
                }
                Producto producto = new Producto(id_producto, nombre, descripcion, precio, cantidad, categoria);
                productos.add(producto);
            }
            return productos;
        }
    }

    public boolean existeCategoria(int idCategoria) throws SQLException {
        String sqlQuery = "SELECT id FROM categoria WHERE id=?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            statement.setInt(1, idCategoria);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return true;
            }
        }
        return false;
    }
}
