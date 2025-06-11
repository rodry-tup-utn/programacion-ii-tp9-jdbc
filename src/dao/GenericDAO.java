package dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface GenericDAO<T> {
    public void crear(T valor, Connection connection) throws SQLException;
    public List<T> listar(Connection connection) throws SQLException;
    public T leer(int id, Connection connection) throws SQLException;
    public boolean eliminar(int id, Connection connection) throws  SQLException;
    public boolean actualizar(T valor, Connection connection) throws SQLException;
}
