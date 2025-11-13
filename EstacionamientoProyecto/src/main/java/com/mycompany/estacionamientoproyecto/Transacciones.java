
package com.mycompany.estacionamientoproyecto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;







/*
  Se encarga de manejar todas las transacciones del sistema. 
  Agrega la fecha cuando se realiza la primera transacción del día 
  y sube las ganancias a la tabla "Actividad" en la base de datos.
  
  Además, calcula los cobros, valida pagos, muestra mensajes al usuario,
  y mantiene el registro total de ganancias acumuladas.
 */

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
  
    
    
    
    
    
      /* 
      Método que suma el monto recibido al total de ganancias.
      Se utiliza cada vez que se realiza una transacción exitosa.
     */
  public void sumarGanancias(double monto){
      
     Total+=monto;
      
  }
  
  

  

  
  
  /*
      Método encargado de generar una fecha actual.
      Inserta la fecha del día en la tabla "Actividad" si aún no existe.
      Conectado conexión activa con la base de datos.
     */
  
  public void AgregarFecha(Connection Conectado){
      
      String sql="INSERT OR IGNORE INTO Actividad (Fecha) VALUES (DATE('now','localtime'))";
     
      try( PreparedStatement ps=Conectado.prepareStatement(sql)){
          
          
          ps.executeUpdate();
          
          
      } catch(SQLException e){
          JOptionPane.showMessageDialog(null,"Error al crear fecha."+e.getMessage(),"ERROR",JOptionPane.ERROR_MESSAGE); 
      }
      
      
      
  }
  
  
  
   /*
     Método encargado de subir las ganancias del día.
     Actualiza la tabla "Actividad" sumando la ganancia del momento 
      y aumentando el contador de spots utilizados.
      
     Conectado conexión a la base de datos.
     Total monto total a agregar a las ganancias.
     */
  public void GuardaGanancias(Connection Conectado,double Total){
      
     
      String SubirDatos="UPDATE Actividad SET GananciaTotal=GananciaTotal+?, SpotsUtilizados=SpotsUtilizados+1 WHERE Fecha= DATE('now','localtime')";
       AgregarFecha(Conectado);
      try(PreparedStatement ps=Conectado.prepareStatement(SubirDatos)){
          
          ps.setDouble(1,Total);
          ps.executeUpdate();
          
      }catch(SQLException e){
          
          JOptionPane.showMessageDialog(null,"Error al subir los datos"+e.getMessage(),"ERROR", JOptionPane.ERROR_MESSAGE);
          
      }
      
  }
  
  
  
 
  /*
      Despliega un JOptionPane si el pago no se completó correctamente.
      En caso de éxito, retorna la tarifa (ganancia).
      
     Este método aplica una tarifa fija (Q10) y muestra el vuelto si aplica.
     */
 
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
  
  
  
  
  
  
  /*
      Función encargada de realizar los cálculos necesarios para el cobro.
   Determina la tarifa según el tiempo (minutos) transcurrido.
      Pide el pago al usuario y valida si el monto ingresado es suficiente.
      
     Si el pago es correcto, se guarda la ganancia y se muestra el vuelto.
      
     Conectado conexión a la base de datos.
     minutos tiempo total que el vehículo estuvo estacionado.
     retorna true si el cobro fue exitoso, false si el pago fue insuficiente.
     */
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
  
 
  
  
  
  /*
     Solicita el pago al usuario mediante un cuadro de diálogo
      montoRequerido cantidad a pagar.
     retorna le monto ingresado por el usuario (o 0 si no es válido).
     */
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
