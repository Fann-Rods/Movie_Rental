package controller;

import java.util.Map;

public interface IUsuarioController {
    
    /* Método para iniciar sesión */
    public String login(String username, String contrasena);
    
    /* Método para regístrarse */
    public String register(String username, String contrasena,
            String nombre, String apellido, String email, double saldo, boolean premium);
    
    /* Método para mostrar los datos del usuario */
    public String pedir(String username);

    /* Método para modificar los datos del usuario */
    public String modificar(String username, String nuevaContrasena,
            String nuevoNombre, String nuevosApellidos, String nuevoEmail,
            double nuevoSaldo, boolean nuevoPremium);

    /** Métodos para eliminar el usuario **/
    /* Para eliminar se requieren tres métodos nuevos para llegar a eliminar.
    Ya que no se puede eliminar a un usuario sino el usuario no ha devuelto las películas */
    public String verCopias(String username);
    
    /* Método para devolver películas */
    public String devolverPeliculas(String username, Map<Integer, Integer> copias);

     /* Método para eliminar usuario */
    public String eliminar(String username);
    
    /* Método para restar dinero */
    public String restarDinero(String username, double nuevoSaldo);


}
