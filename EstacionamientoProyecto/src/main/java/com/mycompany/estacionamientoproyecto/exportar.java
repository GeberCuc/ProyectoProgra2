
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
    
    
    /*
Clase exportar:
Se encarga de extraer la información almacenada en la base de datos y guardarla en un archivo externo.
Facilita la generación de reportes o copias de respaldo de los registros de la tabla Ticket.
*/
  
    
    
    public exportar(){
    }
    
    
 /*
Método ticket(String direccion):
Exporta el contenido completo de la tabla Ticket a un archivo de texto o CSV.
Recibe como parámetro la ruta o dirección del archivo donde se guardarán los datos.
Abre la conexión con la base de datos, consulta todos los registros y escribe cada fila en el archivo.
Incluye los encabezados de columna y separa los valores con comas.
Muestra un mensaje de confirmación si la exportación fue exitosa o un mensaje de error en caso contrario.
*/
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
