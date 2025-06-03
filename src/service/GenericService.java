package service;

import java.sql.SQLException;

public interface GenericService <T>{
    public void crear(T valor) throws SQLException;
}
