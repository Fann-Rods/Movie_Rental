package controller;

public interface IPeliculaController {

   /* Método para mostrar la información de las películas */
    public String listar(boolean ordenar, String orden);

    /* Método para devolver películas */
    public String devolver(int id, String username);
    
    /* Método para sumar la cantidad de películas que se devuelven */
    public String sumarCantidad(int id);
    
    /* Método para alquilar */
    public String alquilar(int id, String username);

    /* Método para modificar */
    public String modificar(int id);

}
