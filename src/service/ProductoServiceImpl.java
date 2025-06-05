package service;

import dao.ProductoDAOImpl;
import model.Producto;

import java.sql.SQLException;

public class ProductoServiceImpl implements GenericService<Producto>{
    private final ProductoDAOImpl productoDAO;

    public ProductoServiceImpl(ProductoDAOImpl productoDAO) {
        this.productoDAO = productoDAO;
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
        productoDAO.crear(producto);
    }
}
