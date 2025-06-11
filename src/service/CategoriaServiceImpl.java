package service;

import config.DatabaseConnection;
import dao.CategoriaDAOImpl;
import dao.GenericDAO;
import model.Categoria;

import java.sql.Connection;
import java.sql.SQLException;

public class CategoriaServiceImpl implements GenericService<Categoria> {

    private final CategoriaDAOImpl categoriaDAO;

    public CategoriaServiceImpl(CategoriaDAOImpl categoriaDAO) {
        this.categoriaDAO = categoriaDAO;
    }

    @Override
    public void crear(Categoria categoria) throws IllegalArgumentException, SQLException {
        if (categoria == null) {
            throw new IllegalArgumentException("La categoria no puede ser nula");
        }
        if (categoria.getNombre().trim().isEmpty() || categoria.getNombre() == null) {
            throw new IllegalArgumentException("Falta el nombre de la categoria");
        }

        if (categoriaDAO.existeNombre((categoria.getNombre()))) {
            throw new IllegalArgumentException("El nombre de la categoria ya existe");
        }
        Connection connection = DatabaseConnection.getConnection();
        categoriaDAO.crear(categoria, connection);
    }
}
