package dao;

import model.Producto;

import java.util.List;

public class ProductoDAOImpl implements GenericDAO<Producto> {

    public void crear(Producto p) {


    }

    public Producto leer(int id) {
        return null;
    }


    public boolean actualizar(Producto p) {
        return false;
    }

    public boolean eliminar(int id) {
        return false;
    }

    public List<Producto> listar() {
        return null;
    }

    public List<Producto> listarPorCategoria(int idCategoria) {
        return null;
    }

    public boolean existeCategoria(int idCategoria) {
        return false;
    }

}
