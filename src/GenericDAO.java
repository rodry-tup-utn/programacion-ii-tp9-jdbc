public interface GenericDAO<T> {
    public void crear(T valor);
    public void listar();
    public void leer(int id);
    public void eliminar(int id);
    public void actualizar(T valor);
}
