package com.mycompany.estacionamientoproyecto;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/*
Clase basededatos:
Encargada de administrar la conexión con la base de datos SQLite utilizada por el sistema.
Proporciona métodos estáticos para abrir y cerrar la conexión de forma segura.
*/
public class basededatos {
    
 /*
Atributo Direccion:
Ruta del archivo SQLite donde se almacena la base de datos del sistema.
Contiene el nombre del archivo y su ubicación dentro del proyecto.
*/
      private final static String Direccion="jdbc:sqlite:Data/BaseDeDatosEstacionamientoProyecto.db";
    
      
/*
Método Conectar():
Establece la conexión con la base de datos mediante la dirección configurada.
Devuelve un objeto Connection si la conexión es exitosa.
En caso de error, muestra un mensaje de advertencia y devuelve null.
*/
     public static Connection Conectar(){
         
         Connection Conectado=null;
        
         try{
             Conectado =DriverManager.getConnection(Direccion);
           
         }catch(SQLException q){
             
             JOptionPane.showMessageDialog(null,"Error de conexión"+q.getMessage(),"ERROR",JOptionPane.ERROR_MESSAGE);
    
             
             
         }
         
         
         
        return Conectado; 
     }    
    
    /*
Método cerrar(Connection Conectado):
Cierra de manera segura la conexión abierta con la base de datos.
Verifica que la conexión no sea nula antes de intentar cerrarla.
Muestra un mensaje de error si ocurre algún problema durante el cierre.
*/
    public static void cerrar(Connection Conectado){
               
        
        if(Conectado!=null){
            try{
                Conectado.close();
                
            }catch(SQLException f){
            JOptionPane.showMessageDialog(null, "Error en cerrar la base de datos"+f.getMessage(),"ERROR",JOptionPane.ERROR_MESSAGE);
        }
    }
    
}
}