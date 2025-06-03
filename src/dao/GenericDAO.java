package dao;

import java.sql.SQLException;
import java.util.List;

public interface GenericDAO<T> {
    public void crear(T valor) throws SQLException;
    public List<T> listar() throws SQLException;
    public T leer(int id) throws SQLException;
    public boolean eliminar(int id) throws  SQLException;
    public boolean actualizar(T valor) throws SQLException;
}
