
package com.mycompany.estacionamientoproyecto;


import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;


public class exportar {
    
    
    
  
    
    
    public exportar(){
    }
    
    
    
    public void ticket(String direccion){
        
        String sql="SELECT * FROM Ticket";
        
        
        
        
        try(Connection Conectado=basededatos.Conectar(); PreparedStatement ps=Conectado.prepareStatement(sql); ResultSet rs=ps.executeQuery(); PrintWriter escribir= new PrintWriter(new FileWriter(direccion))){
            
            //obtener los datos metadata
            ResultSetMetaData datitos=rs.getMetaData();
            //obtiene el numero de columnas
          int columnas=datitos.getColumnCount();
           for (int i=1;i<=columnas;i++) {
               
               //obtiene el nombre de las columnas y escribe el encabezado
            escribir.print(datitos.getColumnName(i));
            if(i<columnas)escribir.print(",");
        }
        escribir.println();
       escribir.println("----------------------------------------");

        //escribre las filas
        while(rs.next()) {
            for(int i=1;i<=columnas; i++){
                escribir.print(rs.getString(i));
                if(i<columnas)escribir.print(",");
            }
            escribir.println();
        }

        escribir.close();
        JOptionPane.showMessageDialog(null,"Archivo exportado correctamente:\n"+direccion);

    }catch(Exception e){
        JOptionPane.showMessageDialog(null,"Error al exportar tickets: "+e.getMessage());
    }
}
            
            
     
        
        
    
   
    
    
    
    
    
    
    
    
}
