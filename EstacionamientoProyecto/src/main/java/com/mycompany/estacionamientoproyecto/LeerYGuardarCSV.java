
package com.mycompany.estacionamientoproyecto;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;


public class LeerYGuardarCSV {
    
    
     private  final JFileChooser ventana;
    private final JTextField Direccioncsv;
    
    
    public LeerYGuardarCSV(JFileChooser ventana,JTextField Direccioncsv){
      this.ventana=ventana;  
      this.Direccioncsv=Direccioncsv;
    }
    
    
   
    
    public void ObtenerDireccion(){
        
        
        try{
            
       int rs=ventana.showOpenDialog(ventana);
        
       if(rs==JFileChooser.APPROVE_OPTION){
           
        File csv=ventana.getSelectedFile();
        String Direccion=csv.getAbsolutePath();
        Direccioncsv.setText(Direccion);
        
           LeerDatos(csv);
       }else{
           JOptionPane.showMessageDialog(null,"No selecciono ningun archivo","INFORMACION",JOptionPane.INFORMATION_MESSAGE);
       }
        }catch(Exception e){
            
            JOptionPane.showMessageDialog(null,"ERROR"+e.getMessage(),"ERROR CATASTROFICO jaa",JOptionPane.ERROR_MESSAGE);
            
        }
        
        
    }
    
    
    
    public void LeerDatos(File CSV){
        
        int registro=0;
        
        try( Connection Conectado=basededatos.Conectar();BufferedReader lee=new BufferedReader(new FileReader(CSV))){
            
            
            String Lectura;
            boolean primero=true; 
            
            //verificar primera linea 
            while((Lectura=lee.readLine())!=null){
                if (Lectura.trim().isEmpty()|| primero){
                    
                    primero=false;
                    continue;
                }
                
                
                //lee y para justo en la coma
                String[] info=Lectura.split(",");
                
                if(info.length>=3){
                    if(SubirInformacion(Conectado,info)){
                        
                        registro++;
                        
                        
                    }
                    
                      
                }
                
                JOptionPane.showMessageDialog(null,registro+" Archivos leeidos correctamente","EXITO",JOptionPane.INFORMATION_MESSAGE);
                
                
            }
            
        }catch(Exception e){
            
            JOptionPane.showMessageDialog(null,"Error en la lectura del archivo"+e.getMessage(),"ERROR",JOptionPane.ERROR_MESSAGE);
            
        }
        
       
        
    }
    
    
    
    public boolean SubirInformacion(Connection Conectado,String [] info ){
        
      String historial="""
        INSERT INTO Ticket(TicketID, Placa, IdArea, Spotid, FechaIngreso, Fechasalida, modo, monto)
        VALUES(?,?,?,?,?,?,?,?)
    """; 
    
      try(PreparedStatement ps=Conectado.prepareStatement(historial)){
          
         
              
          for(int i=0;i<info.length;i+=8){
          ps.setString(1, info[i].trim());
          ps.setString(2, info[i+1].trim());
          ps.setString(3,info[i+2].trim());
          ps.setString(4, info[i+3].trim());
          ps.setString(5,info[i+4].trim());
          ps.setString(6, info[i+5].trim());
          ps.setString(7, info[i+6].trim());
          ps.setDouble(8,Double.parseDouble(info[i+7].trim()));
          
          ps.executeUpdate();
          
          }
      JOptionPane.showMessageDialog(null,"Datos guardados con exito","GUARDADO",JOptionPane.INFORMATION_MESSAGE);
        return true;    
      }catch(SQLException e){
          
          JOptionPane.showMessageDialog(null,"Error al momento de subir datos a la base de datos"+e.getMessage(),"ERROR", 0);
          return false;
      }
      
      
      
    }
    
    
    
    
    
}
