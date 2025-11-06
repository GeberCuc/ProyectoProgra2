
package com.mycompany.estacionamientoproyecto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import javax.swing.JOptionPane;


public class Transacciones {

    public double getTotal() {
        return Total;
    }

    public void setTotal(double Total) {
        this.Total = Total;
    }

   
    
  private double Total=0.00;

    public double getGanancia() {
        return ganancia;
    }
 private double ganancia;
    
 
    public Transacciones() {
    }
  
  public void sumarGanancias(double monto){
      
     Total+=monto;
      
  }
  
  

  

  
  
  
  
  public void AgregarFecha(Connection Conectado){
      
      String sql="INSERT OR IGNORE INTO Actividad (Fecha) VALUES (DATE('now'))";
     
      try( PreparedStatement ps=Conectado.prepareStatement(sql)){
          
          
          ps.executeUpdate();
          
          
      } catch(SQLException e){
          JOptionPane.showMessageDialog(null,"Error al crear fecha."+e.getMessage(),"ERROR",JOptionPane.ERROR_MESSAGE); 
      }
      
      
      
  }
  
  
  public void GuardaGanancias(Connection Conectado,double Total){
      
     
      String SubirDatos="UPDATE Actividad SET GananciaTotal=GananciaTotal+?, SpotsUtilizados=SpotsUtilizados+1 WHERE Fecha= DATE('now')";
       AgregarFecha(Conectado);
      try(PreparedStatement ps=Conectado.prepareStatement(SubirDatos)){
          
          ps.setDouble(1,Total);
          ps.executeUpdate();
          
      }catch(SQLException e){
          
          JOptionPane.showMessageDialog(null,"Error al subir los datos"+e.getMessage(),"ERROR", JOptionPane.ERROR_MESSAGE);
          
      }
      
  }
  
  
  
 
  
  
  
  public double planoCobro(Connection Conectado,double pago){
      
     double tarifa=10;
      
      if(pago<tarifa){
          
          JOptionPane.showMessageDialog(null,"Pago insuficiente","ERROR",JOptionPane.ERROR_MESSAGE);
          return 0;
      }
      double vueltoPlano=pago-tarifa;
      sumarGanancias(tarifa);
      GuardaGanancias(Conectado,tarifa);
      if(vueltoPlano>0){
          JOptionPane.showMessageDialog(null,String.format("Pago recibido: Q%.2f\nVuelto: Q%.2f", pago, vueltoPlano),"PAGO EXITOSO",JOptionPane.INFORMATION_MESSAGE);
          
      }else{
            JOptionPane.showMessageDialog(null,String.format("Pago recibido: Q%.2f\nSin vuelto",pago),"PAGO EXITOSO", JOptionPane.INFORMATION_MESSAGE);
        }
      return tarifa;
  }
  
  
  
  
  
  
  
  public boolean cobrar(Connection Conectado,double minutos) {
        double tarifaHora = 10.0;
        double horas = minutos / 60.0;
        double nuevoMonto = Math.ceil(horas) * tarifaHora;

        double pago=solicitarPago(nuevoMonto);

        if(pago<nuevoMonto){
            JOptionPane.showMessageDialog(null,"Pago insuficiente","ERROR",JOptionPane.ERROR_MESSAGE);
            return false; 
        }

        double vuelto=pago-nuevoMonto;
        
        ganancia=nuevoMonto;
         GuardaGanancias(Conectado,nuevoMonto);
         
         
        if(vuelto>0){
            JOptionPane.showMessageDialog(null,
                    String.format("Pago recibido: Q%.2f\nVuelto: Q%.2f", pago, vuelto),"PAGO EXITOSO",JOptionPane.INFORMATION_MESSAGE);
        }else{
            JOptionPane.showMessageDialog(null,String.format("Pago recibido: Q%.2f\nSin vuelto",pago),"PAGO EXITOSO", JOptionPane.INFORMATION_MESSAGE);
        }

        return true;
    }
  
  
 public double solicitarPago(double montoRequerido){
    String input=JOptionPane.showInputDialog(null, 
        String.format("Monto a pagar: Q%.2f\nIngrese el monto:", montoRequerido),"PAGO",JOptionPane.QUESTION_MESSAGE);
    
    try{
        return Double.parseDouble(input);
    }catch (NumberFormatException e){
        return 0;
    }
}

 
 
 
 
 
 
}
