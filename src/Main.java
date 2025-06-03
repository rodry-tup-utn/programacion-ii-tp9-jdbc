//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        GestorCategoria gestor = new GestorCategoria();
        //gestor.agregarCategoria("Almacen", "Productos alimenticios de almacen");
        //gestor.agregarCategoria("Limpieza", "Productos para la limpieza del hogar");
        //gestor.listarCategorias();
        gestor.mostrarCategoria(5);
        //gestor.actualizarCategoria(1, "Bebidas sin alcohol", "Bebidas sin alcohol te ofrece una amplia gama de opciones refrescantes y deliciosas para cada ocasión.");
        gestor.eliminarCategoria(5);
        gestor.listarCategorias();
    }
}