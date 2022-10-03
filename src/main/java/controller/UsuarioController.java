package controller;

import java.sql.ResultSet;
import java.sql.Statement;
import com.google.gson.Gson; //Conexión web
import beans.Usuario;
import connection.DBConnection;
import java.util.HashMap;
import java.util.Map;

public class UsuarioController implements IUsuarioController {

    //INICIAR SESIÓN - USUARIO
    @Override
    public String login(String username, String contrasena) {

        //Crear un objeto de la clase Gson
        Gson gson = new Gson();

        //Crear un objeto de la clase DBConnection para onectarnos con la base de datos
        DBConnection conn = new DBConnection();

        //Sentencia SQL
        String sql = "SELECT * FROM "
                + "usuario "
                + "WHERE username = '" + username
                + "' AND + contrasena = '" + contrasena + "'";

        //Cuando se establece la conexión
        try {

            /**
             * El objeto Statement (sentencia) sirve para procesar una sentencia
             * SQL estática y obtener los resultados producidos por ella. Solo
             * puede haber un ResultSet abierto para cada objeto Statement en un
             * momento dado.
             */
            Statement st = conn.getConnection().createStatement();

            /**
             * El objeto ResultSet proporciona varios métodos para obtener los
             * datos de columna correspondientes a un fila. Todos ellos tienen
             * el formato get<Tipo>, siendo <Tipo> un tipo de datos Java™.
             * Algunos ejemplos de estos métodos son getInt, getLong, getString,
             * getTimestamp y getBlob.
             */
            ResultSet rs = st.executeQuery(sql);

            //Proceso de buscar dentro de la conexión
            while (rs.next()) {
                String nombre = rs.getString("nombre");
                String apellido = rs.getString("apellido");
                String email = rs.getString("email");
                double saldo = rs.getDouble("saldo");
                boolean premium = rs.getBoolean("premium");

                //Crear un objeto de la clase Usuarios que almacenara toda la info
                Usuario usuario = new Usuario(username, contrasena, nombre, apellido, email, saldo, premium);

                //Retornar los datos
                return gson.toJson(usuario);

            }

        } //Error en el proceso de conexión
        catch (Exception ex) {
            System.out.println("¡ERROR!: " + ex.getMessage());
        } //Finalizar el proceso de conexión
        finally {
            conn.desconectar();
        }

        return "false";

    }

    //CREAR UN NUEVO USUARIO
    @Override
    public String register(String username, String contrasena, String nombre, String apellido, String email,
            double saldo, boolean premium) {

        Gson gson = new Gson();

        DBConnection conn = new DBConnection();

        String sql = "INSERT INTO usuario VALUES('" + username + "', '" + contrasena + "', '" + nombre
                + "', '" + apellido + "', '" + email + "', " + saldo + ", " + premium + ")";

        try {
            Statement st = conn.getConnection().createStatement();
            st.executeUpdate(sql);

            Usuario usuario = new Usuario(username, contrasena, nombre, apellido, email, saldo, premium);

            st.close();

            return gson.toJson(usuario);

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } finally {
            conn.desconectar();
        }

        return "false";

    }

    //MOSTRAR DATOS DEL USUARIO
    @Override
    public String pedir(String username) {

        Gson gson = new Gson();

        DBConnection con = new DBConnection();
        String sql = "SELECT * FROM usuario WHERE username = '" + username + "'";

        try {

            Statement st = con.getConnection().createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                String contrasena = rs.getString("contrasena");
                String nombre = rs.getString("nombre");
                String apellido = rs.getString("apellido");
                String email = rs.getString("email");
                double saldo = rs.getDouble("saldo");
                boolean premium = rs.getBoolean("premium");

                Usuario usuario = new Usuario(username, contrasena,
                        nombre, apellido, email, saldo, premium);

                return gson.toJson(usuario);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } finally {
            con.desconectar();
        }

        return "false";
    }

    //MODIFICAR USUARIO
    @Override
    public String modificar(String username, String nuevaContrasena,
            String nuevoNombre, String nuevosApellidos,
            String nuevoEmail, double nuevoSaldo, boolean nuevoPremium) {

        DBConnection conn = new DBConnection();

        String sql = "UPDATE usuario SET contrasena = '" + nuevaContrasena
                + "', nombre = '" + nuevoNombre + "', "
                + "apellido = '" + nuevosApellidos + "', email = '"
                + nuevoEmail + "', saldo = " + nuevoSaldo + ", premium = ";

        if (nuevoPremium == true) {
            sql += " 1 ";
        } else {
            sql += " 0 ";
        }

        sql += " WHERE username = '" + username + "'";

        try {

            Statement st = conn.getConnection().createStatement();
            st.executeUpdate(sql);

            return "true";

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } finally {
            conn.desconectar();
        }

        return "false";

    }

    //VER COPIAS
    @Override
    public String verCopias(String username) {

        DBConnection conn = new DBConnection();
        String sql = "SELECT id, COUNT(*) AS num_copias FROM alquiler WHERE username = '"
                + username + "' GROUP BY id;";

        Map<Integer, Integer> copias = new HashMap<Integer, Integer>();

        try {

            Statement st = conn.getConnection().createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                int id = rs.getInt("id");
                int num_copias = rs.getInt("num_copias");
                copias.put(id, num_copias);
            }

            devolverPeliculas(username, copias);

            return "true";

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } finally {
            conn.desconectar();
        }

        return "false";

    }

    //DEVOLVER PELÍCULAS
    @Override
    public String devolverPeliculas(String username, Map<Integer, Integer> copias) {

        DBConnection conn = new DBConnection();

        try {
            for (Map.Entry<Integer, Integer> pelicula : copias.entrySet()) {
                int id = pelicula.getKey();
                int num_copias = pelicula.getValue();

                String sql = "UPDATE pelicula SET copias = (SELECT copias + " + num_copias
                        + " FROM pelicula WHERE id = " + id + ") WHERE id = " + id;

                Statement st = conn.getConnection().createStatement();
                st.executeUpdate(sql);

            }

            this.eliminar(username);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } finally {
            conn.desconectar();
        }
        return "false";
    }

    //ELIMINAR
    public String eliminar(String username) {

        DBConnection conn = new DBConnection();

        String sql1 = "DELETE FROM alquiler WHERE username = '" + username + "'";
        String sql2 = "DELETE FROM usuario WHERE username = '" + username + "'";

        try {
            Statement st = conn.getConnection().createStatement();
            st.executeUpdate(sql1);
            st.executeUpdate(sql2);

            return "true";
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } finally {
            conn.desconectar();
        }

        return "false";
    }

    //MÉTODO RESTAR DINERO
    @Override
    public String restarDinero(String username, double nuevoSaldo) {

        DBConnection con = new DBConnection();
        
        String sql = "UPDATE usuario SET saldo = " + nuevoSaldo + " WHERE username = '" + username + "'";

        try {
            Statement st = con.getConnection().createStatement();
            st.executeUpdate(sql);
            return "true";
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } finally {
            con.desconectar();
        }

        return "false";
    }

}
