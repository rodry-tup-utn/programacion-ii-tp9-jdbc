package dao;

import config.DatabaseConnection;
import model.ItemPedido;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class ItemPedidoDAO implements GenericDAO<ItemPedido>{
    private final ProductoDAOImpl productoDAO;

    public ItemPedidoDAO(ProductoDAOImpl productoDAO) {
        this.productoDAO = productoDAO;
    }

    @Override
    public void crear(ItemPedido itemPedido, Connection connection) throws SQLException {
        String sqlInsert = "INSERT INTO items_pedido(id_pedido, id_producto, cantidad, subtotal) VALUES(?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sqlInsert)) {
             statement.setInt(1, itemPedido.getPedido().getId());
             statement.setInt(2, itemPedido.getProducto().getId());
             statement.setInt(3, itemPedido.getCantidad());
             statement.setDouble(4, itemPedido.getSubtotal());
            statement.execute();
        }
    }

    @Override
    public List<ItemPedido> listar(Connection connection) throws SQLException {


        return List.of();
    }

    @Override
    public ItemPedido leer(int id, Connection connection) throws SQLException {
        return null;
    }

    @Override
    public boolean eliminar(int id, Connection connection) throws SQLException {
        return false;
    }

    @Override
    public boolean actualizar(ItemPedido valor, Connection connection) throws SQLException {
        return false;
    }
}
