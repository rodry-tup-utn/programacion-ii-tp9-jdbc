package dao;

import config.DatabaseConnection;
import model.Pedido;

import java.sql.*;
import java.util.List;

public class PedidoDAO implements GenericDAO<Pedido>{
    @Override
    public void crear(Pedido pedido, Connection connection) throws SQLException {
        String sqlInsert = "INSERT INTO pedido(fecha, total) VALUES(?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sqlInsert)) {
            statement.setDate(1, Date.valueOf(pedido.getFecha()));
            statement.setDouble(2, pedido.getSubtotal());
            statement.execute();
        }
    }

    @Override
    public List<Pedido> listar(Connection connection) throws SQLException {
        return List.of();
    }

    @Override
    public Pedido leer(int id, Connection connection) throws SQLException {
        return null;
    }

    @Override
    public boolean eliminar(int id, Connection connection) throws SQLException {
        return false;
    }

    @Override
    public boolean actualizar(Pedido valor, Connection connection) throws SQLException {
        return false;
    }
}
