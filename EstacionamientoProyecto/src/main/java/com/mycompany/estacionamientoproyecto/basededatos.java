package com.mycompany.estacionamientoproyecto;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;


public class basededatos {
    
    
        private final static String Direccion="jdbc:sqlite:Data/BaseDeDatosEstacionamientoProyecto.db";
    
     public static Connection Conectar(){
         
         Connection Conectado=null;
        
         try{
             Conectado =DriverManager.getConnection(Direccion);
             JOptionPane.showMessageDialog(null,"Conexion exitosa","Conexion DB",JOptionPane.INFORMATION_MESSAGE);
             
             
             
         }catch(SQLException q){
             
             JOptionPane.showMessageDialog(null,"Error de conexión"+q.getMessage(),"ERROR",JOptionPane.ERROR_MESSAGE);
    
             
             
         }
         
         
         
        return Conectado; 
     }    
    
    
    
    
}
