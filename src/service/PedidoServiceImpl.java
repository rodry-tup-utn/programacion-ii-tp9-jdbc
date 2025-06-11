package service;

import config.DatabaseConnection;
import dao.ItemPedidoDAO;
import dao.PedidoDAO;
import dao.ProductoDAOImpl;
import model.ItemPedido;
import model.Pedido;
import model.Producto;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class PedidoServiceImpl implements GenericService<Pedido>{
    private ProductoDAOImpl productoDAO;
    private PedidoDAO pedidoDAO;
    private ItemPedidoDAO itemPedidoDAO;

    public PedidoServiceImpl(ProductoDAOImpl productoDAO, PedidoDAO pedidoDAO, ItemPedidoDAO itemPedidoDAO) {
        this.productoDAO = productoDAO;
        this.pedidoDAO = pedidoDAO;
        this.itemPedidoDAO = itemPedidoDAO;

    }

    @Override
    public void crear(Pedido pedido) throws SQLException, IllegalArgumentException {
        Connection connection = null; // La conexión se declara aquí para que sea accesible en el catch/finally
        try {
            connection = DatabaseConnection.getConnection();
            connection.setAutoCommit(false);

            // VALIDACIONES

            validarItems(pedido);
            Set<Producto> productosParaActualizarStock = new HashSet<>();
            for (ItemPedido item : pedido.getItems()) {
                validarCantidadItems(item);
                Producto productoActualizadoDeDB = productoDAO.leer(item.getProducto().getId(), connection);
                item.setProducto(productoActualizadoDeDB);

                validarStockYReducirEnMemoria(item);

                productosParaActualizarStock.add(productoActualizadoDeDB);

                item.setSubtotal(productoActualizadoDeDB.getPrecio() * item.getCantidad());
            }

            double total = pedido.calcularTotalPedido();
            pedido.setSubtotal(total);

            // PERSISTENCIA EN LA BASE DE DATOS

            pedidoDAO.crear(pedido, connection);

            for (ItemPedido item : pedido.getItems()) {
                item.setPedido(pedido);
                itemPedidoDAO.crear(item, connection);
            }

            // Actualizar el stock de los productos
            for (Producto productoActualizado : productosParaActualizarStock) {
                productoDAO.actualizar(productoActualizado, connection);
            }

            connection.commit();
            System.out.println("Pedido " + pedido.getId() + " creado exitosamente y stock actualizado.");

        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                    System.err.println("Transacción de pedido revertida por error de base de datos: " + e.getMessage());
                } catch (SQLException rollbackEx) {
                    System.err.println("Error al intentar rollback: " + rollbackEx.getMessage());
                }
            }
            throw e;
        } catch (IllegalArgumentException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                    System.err.println("Transacción de pedido revertida por error de validación: " + e.getMessage());
                } catch (SQLException rollbackEx) {
                    System.err.println("Error al intentar rollback después de validación fallida: " + rollbackEx.getMessage());
                }
            }
            throw e;
        } finally {
            // Asegurarse de cerrar la conexión y restaurar el auto-commit
            if (connection != null) {
                try {
                    connection.setAutoCommit(true); // Restaurar a auto-commit para futuras conexiones
                    connection.close(); // Cerrar la conexión
                } catch (SQLException closeEx) {
                    System.err.println("Error al cerrar conexión: " + closeEx.getMessage());
                }
            }
        }
    }

    public void validarCantidadItems(ItemPedido item) {
            if (item.getCantidad() < 1) {
                throw new IllegalArgumentException("La cantidad del producto " + item.getProducto().getNombre() + " no puede ser menor a 1");
            }
    }

    public void validarItems(Pedido pedido) {
        if (pedido.getItems().isEmpty()) {
            throw new IllegalArgumentException("El pedido no contiene items");
        }
    }

    //validar existencia de producto y stock
    public void validarStockYReducirEnMemoria(ItemPedido item) {
                Producto productoActual = item.getProducto();
                int cantidadFinal = productoActual.getCantidad() - item.getCantidad();
                if (cantidadFinal < 0 ) {
                    throw new IllegalArgumentException("El producto " + productoActual.getNombre() + " no tiene cantidad de suficiente para realizar el pedido");
                }
                productoActual.setCantidad(cantidadFinal);
        }

}