package service;

import config.DatabaseConnection;
import dao.CategoriaDAOImpl;
import dao.ProductoDAOImpl;
import model.Producto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductoServiceImpl implements GenericService<Producto>{
    private final ProductoDAOImpl productoDAO;
    private final CategoriaDAOImpl categoriaDAO;

    public ProductoServiceImpl(ProductoDAOImpl productoDAO, CategoriaDAOImpl categoriaDAO) {
        this.productoDAO = productoDAO;
        this.categoriaDAO = categoriaDAO;
    }

    @Override
    public void crear(Producto producto) throws SQLException, IllegalArgumentException {
        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser null");
        }
        if (producto.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("No se ingresó el nombre del producto");
        }
        if (producto.getCantidad() <= 0) {
            throw new IllegalArgumentException("La cantidad no puede ser menor a 1");
        }
        if (producto.getPrecio() <= 0) {
            throw new IllegalArgumentException("El precio no puede ser menor a 1");
        }

        if (!categoriaDAO.existeCategoria(producto.getIdCategoria())) {
            throw new SQLException("No existe el id de categoria referenciado");
        }
        Connection connection = DatabaseConnection.getConnection();
        productoDAO.crear(producto, connection);
    }

}
