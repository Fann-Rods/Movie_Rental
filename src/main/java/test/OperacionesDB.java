package test;

import beans.Peliculas;
import connection.DBConnection;
import java.sql.ResultSet;
import java.sql.Statement;

public class OperacionesDB {
    
    //CREAR EL MÉTODO PRINCIPAL
    public static void main(String[] args) {
        
        //Mostrar la información de las películas
        listarPeliculas();
        
    }

    //MÉTODO PARA ACTUALIZAR PELÍCULAS
    public static void actualizarPeliculas(int id, String genero){
        
        //Crear un objeto de la clase DBConnection para onectarnos con la base de datos
        DBConnection conn = new DBConnection();
        
        //Sentencia SQL
        String sql = "UPDATE pelicula "
                + "SET genero = '" + genero 
                + "'WHERE id = " + id;
        
        //Cuando se establece la conexión
        try {
            Statement st = conn.getConnection().createStatement();
            st.executeUpdate(sql);
        } 
        //Error en el proceso de conexión
        catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        //Finalizar el proceso de conexión
        finally{
            conn.desconectar();
        }
        
    }
    
    //MÉTODO PARA LISTAR PELÍCULAS
    public static void listarPeliculas(){
        
        //Crear un objeto de la clase DBConnection para onectarnos con la base de datos
        DBConnection conn = new DBConnection();
        
        //Sentencia SQL
        String sql = "SELECT * FROM pelicula";

        //Cuando se establece la conexión
        try {
            
            //
            Statement st = conn.getConnection().createStatement();

            //
            ResultSet rs = st.executeQuery(sql);

            //
            while(rs.next()){
                int id = rs.getInt("id");
                String titulo = rs.getString("titulo");
                String genero = rs.getString("genero");
                String autor = rs.getString("autor");
                int copias = rs.getInt("copias");
                boolean novedad = rs.getBoolean("novedad");
                
                //Crear un objeto de la clase Peliculas para almacenar la info de cada película
                Peliculas pelicula = new Peliculas(id, titulo, genero, autor, copias, novedad);
                
                //Mostrar en pantalla la info de las películas
                System.out.println(pelicula.toString());                            
            }           

            //
            st.executeQuery(sql);

        } 
        //Error en el proceso de conexión
        catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        //Finalizar el proceso de conexión
        finally{
            conn.desconectar();
        }
        
    }

}
