package connection;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    
    //Conexión a la base de datos
    Connection connection; 
    
    //CREAR EL PROCESO DE CONEXIÓN
    static String db = "movies rental"; //Nombre de la base de datos
    static String port = "3306"; //Puerto que estamos utilizando en HeidiSQL
    static String login = "root"; //Usuario de HeidiSQL
    static String password = "admin"; //Contraseña de HeidiSQL
    
    //MÉTODO CONSTRUCTOR
    public DBConnection() {
        
        //Cuando se establece conexión
        try {
            
            //Registra el driver de conexión para la base de datos
            Class.forName("com.mysql.jdbc.Driver");
            
            //Asignar la ruta para acceder a la base de datos
            String url = "jdbc:mysql://localhost:" + this.port + "/" + this.db;

            //Realizar el proceso de conexión con DriverManager con los datos del usuario
            connection = DriverManager.getConnection(url, this.login, this.password);

            //Verifica en consola la conexión establecida
            System.out.println("Conexión establecida.");

        }
        //Error en el proceso de conexión
        catch (Exception ex) {
            System.out.println("Error en la conexión.");  
        }
        
    }
    
    //MÉTODO PARA CONECTAR
    public Connection getConnection(){
        return connection;
    }
    
    //MÉTODO PARA DESCONECTAR
    public void desconectar(){
        connection = null;
    }

}
