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
           
         }catch(SQLException q){
             
             JOptionPane.showMessageDialog(null,"Error de conexión"+q.getMessage(),"ERROR",JOptionPane.ERROR_MESSAGE);
    
             
             
         }
         
         
         
        return Conectado; 
     }    
    
    
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