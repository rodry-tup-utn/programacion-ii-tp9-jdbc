import config.DatabaseConnection;
import dao.CategoriaDAOImpl;
import dao.ItemPedidoDAO;
import dao.PedidoDAO;
import dao.ProductoDAOImpl;
import model.Categoria;
import model.ItemPedido;
import model.Pedido;
import model.Producto;
import service.CategoriaServiceImpl;
import service.PedidoServiceImpl;
import service.ProductoServiceImpl;

import javax.swing.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //Kta 1
        GestorCategoria gestor = new GestorCategoria();
        //gestor.agregarCategoria("Almacen", "Productos alimenticios de almacen");
        //gestor.agregarCategoria("Limpieza", "Productos para la limpieza del hogar");
        //gestor.listarCategorias();
        //gestor.mostrarCategoria(5);
        //gestor.actualizarCategoria(1, "Bebidas sin alcohol", "Bebidas sin alcohol te ofrece una amplia gama de opciones refrescantes y deliciosas para cada ocasión.");
        //gestor.eliminarCategoria(5);
        //gestor.listarCategorias();

        //Kata 2
        Categoria lacteos = new Categoria(1123, "Lacteos", "Productos lácteos de todo tipo");
        CategoriaDAOImpl categoriaDAO = new CategoriaDAOImpl();
        CategoriaServiceImpl categoriaService = new CategoriaServiceImpl(categoriaDAO);
        try {
            categoriaService.crear(lacteos);
            System.out.println("Se agrego la categoria " + lacteos.getNombre() + " a la base de datos");
        } catch (Exception e) {
            System.out.println("No se pudo agregar la categoria: " + e.getMessage());
        }

        // Kata 3
        try (Connection connection = DatabaseConnection.getConnection();) {
            ProductoDAOImpl productoDAO = new ProductoDAOImpl(categoriaDAO);
            ProductoServiceImpl productoService = new ProductoServiceImpl(productoDAO, categoriaDAO);
            Categoria bebidas = categoriaDAO.leer(1, connection);
            Producto coca = new Producto(1221, "Coca Cola", "Coca Cola comun", -2200, 10, bebidas);
            try {
                productoService.crear(coca);
                System.out.println("Producto " + coca.getNombre() + " agregado a la base de datos");
            } catch (Exception e) {
                System.out.println("Error al crear el producto: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("No se pudo establecer la conxion a la base de datos");
        }

        try (Connection connection = DatabaseConnection.getConnection()){
            ProductoDAOImpl productoDAO = new ProductoDAOImpl(categoriaDAO);
            ProductoServiceImpl productoService = new ProductoServiceImpl(productoDAO, categoriaDAO);
            List<Producto> productosBebida =  productoDAO.listarPorCategoria(1, connection);
            for (Producto producto : productosBebida) {
                System.out.println(producto.getNombre());
            }
        } catch (SQLException e) {
            System.out.println("Error al conectarse a la base de datos" + e.getMessage());
        }

        try (Connection connection = DatabaseConnection.getConnection()) {
            ProductoDAOImpl productoDAO = new ProductoDAOImpl(categoriaDAO);
            ProductoServiceImpl productoService = new ProductoServiceImpl(productoDAO, categoriaDAO);
            productoDAO.mostrarProductoConCategoria(2, connection);
        } catch (SQLException e) {
            System.out.println("Error al conectarse a la base de datos " + e.getMessage());
        }

        //Kata 4
        try (Connection connection = DatabaseConnection.getConnection()) {
            ProductoDAOImpl productoDAO = new ProductoDAOImpl(categoriaDAO);
            ProductoServiceImpl productoService = new ProductoServiceImpl(productoDAO, categoriaDAO);
            PedidoDAO pedidoDAO = new PedidoDAO();
            ItemPedidoDAO itemPedidoDAO = new ItemPedidoDAO();
            PedidoServiceImpl pedidoService = new PedidoServiceImpl(productoDAO, pedidoDAO,  itemPedidoDAO);

            Producto coca = productoDAO.leer(3, connection);
            Producto fanta = productoDAO.leer(1, connection);

            Pedido nuevoPedido = new Pedido(1, LocalDate.now());
            ItemPedido item1 = new ItemPedido(1, coca, 2);
            ItemPedido item2 = new ItemPedido(2, fanta, 1);

            nuevoPedido.agregarItem(item1);
            nuevoPedido.agregarItem(item2);

            double subtotal = nuevoPedido.calcularTotalPedido();
            nuevoPedido.setSubtotal(subtotal);

            try {
                pedidoService.crear(nuevoPedido);
            } catch (Exception e) {
                System.out.println("Error al guardar el pedido en la base de datos: " + e.getMessage());
            }

        } catch (SQLException e) {
            System.out.println("Error al conectarse a la base de datos: " + e.getMessage());
        }



        /*
        ProductoServiceImpl service = new ProductoServiceImpl(productoDAO);
        Producto producto = new Producto(1, "coca cola 2", "coca comun", 3800.0, 10, new Categoria(0, ""," "));
        try {
            service.crear(producto);
        } catch (SQLException e) {
            System.out.println("no se pudo crear el producto: " + e.getMessage());
        }
        */
        //Mostrar producto con categoria


    }
}