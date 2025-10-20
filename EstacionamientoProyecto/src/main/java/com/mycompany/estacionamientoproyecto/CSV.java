
package com.mycompany.estacionamientoproyecto;

import com.opencsv.CSVWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.swing.JOptionPane;
public class CSV {

    public String getArchivoCsv() {
        return ArchivoCsv;
    }
    
    
  private String ArchivoCsv;
    
    public CSV(String ArchivoCsv){
        
       this.ArchivoCsv=ArchivoCsv; 
        
        
    }
    
    
    public void AgregarEcabezado(List<String>Encabezado){
        
        try(PrintWriter escribirEN= new PrintWriter(new FileWriter(ArchivoCsv))){
            
            escribirEN.println(String.join(",",Encabezado));
           
            
        }catch(IOException s){
            
            JOptionPane.showMessageDialog(null, "Error no se pudo agregar el encabezado"+s.getMessage(),"ERROR"
                    ,JOptionPane.ERROR_MESSAGE);
        }
         
        
    }
    
    
    
    
    
    
    public void CrearCsv(List<String>Data){
        
        try(PrintWriter escribir= new PrintWriter(new FileWriter(ArchivoCsv,true))){
            
            
           escribir.println(String.join(",",Data));
              
        }catch(IOException e){
            
            JOptionPane.showMessageDialog(null, "Error al momento de crear el archivo Csv"+e.getMessage(),"ERROR"
                    ,JOptionPane.ERROR_MESSAGE);    
        }
        
    }
    
 
    
    
}
