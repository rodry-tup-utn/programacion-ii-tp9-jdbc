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

    public void mostrarDetallePedido(int id_pedido, Connection connection) throws SQLException{
        String sqlQuery = "SELECT\n" +
                "    p.id_pedido,\n" +
                "    p.fecha,\n" +
                "    p.total AS total_pedido,\n" + // Asegúrate de que 'subtotal' es la columna correcta para el total del pedido
                "    ip.cantidad AS cantidad_item,\n" +
                "    ip.subtotal AS subtotal_item,\n" +
                "    prod.id_producto,\n" +
                "    prod.nombre AS nombre_producto,\n" +
                "    prod.precio AS precio_producto\n" +
                "FROM\n" +
                "    pedido p\n" +
                "INNER JOIN\n" +
                "    items_pedido ip ON p.id_pedido = ip.id_pedido\n" +
                "INNER JOIN\n" +
                "    producto prod ON ip.id_producto = prod.id_producto\n" +
                "WHERE\n" +
                "    p.id_pedido = ?;";
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            statement.setInt(1, id_pedido);
            ResultSet rs = statement.executeQuery();

            // Variables para almacenar los detalles del pedido principal
            int pedidoId = -1;
            Date fechaPedido = null;
            double totalPedido = 0.0;
            boolean pedidoEncontrado = false;

            System.out.println("\n--- Detalle del Pedido ---");

            while (rs.next()) {
                pedidoEncontrado = true;

                if (pedidoId == -1) {
                    pedidoId = rs.getInt("id_pedido");
                    fechaPedido = rs.getDate("fecha");
                    totalPedido = rs.getDouble("total_pedido");

                    // encabezado del pedido
                    System.out.println("ID de Pedido: " + pedidoId);
                    System.out.println("Fecha del Pedido: " + fechaPedido);
                    System.out.println("Total del Pedido: $" + String.format("%.2f", totalPedido));
                    System.out.println("--------------------------");
                    System.out.println("Ítems del Pedido:");
                }
                int cantidadItem = rs.getInt("cantidad_item");
                double subtotalItem = rs.getDouble("subtotal_item");
                String nombreProducto = rs.getString("nombre_producto");
                double precioUnitario = rs.getDouble("precio_producto");

                System.out.println("  - Producto: " + nombreProducto +
                        " (ID: " + rs.getInt("id_producto") + ")" +
                        ", Cantidad: " + cantidadItem +
                        ", Precio Unitario: $" + String.format("%.2f", precioUnitario) +
                        ", Subtotal Ítem: $" + String.format("%.2f", subtotalItem));
            }

            // Si el bucle nunca se ejecutó, significa que el pedido no fue encontrado
            if (!pedidoEncontrado) {
                System.out.println("No se encontró el pedido con ID: " + id_pedido);
            }
            System.out.println("--------------------------\n");

        }
    }
}
