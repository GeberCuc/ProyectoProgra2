
package com.mycompany.estacionamientoproyecto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class UsuarioDB {
    
    
    public void AgregarDb(Usuario usuariodb){
        
        
       String sql="INSERT INTO Usuario(Nombre,Carnet,Placa,Vehículo) VALUES(?,?,?,?)";
        
  //conectar a la base de datos mediante la clase basededatos, PreparedStatement es para generar una consulta seguro.
        try(Connection Conectado=basededatos.Conectar();   PreparedStatement Consulta=Conectado.prepareStatement(sql) ){
            
            Consulta.setString(1,usuariodb.getNombre());
            Consulta.setInt(2,usuariodb.getCarnet());
            Consulta.setString(3, usuariodb.getPlaca());
            Consulta.setString(4,usuariodb.getVehiculo());
  
            //Esto es lo que realmente sube los datos 
            Consulta.executeUpdate();
            
            JOptionPane.showMessageDialog(null,"Usuario: "+usuariodb.getNombre()+" Agregado correctamente","Añadido Correctamente",JOptionPane.INFORMATION_MESSAGE);
        }catch(SQLException e){
            
            JOptionPane.showMessageDialog(null,"Error al añadir nuevo usuario"+e.getMessage(),"ERROR",JOptionPane.ERROR_MESSAGE);
            
        }   
        
    }
    
    
  public Usuario BuscarPorPlaca(String Buscado){
          
      Usuario usuario=null;
          
      String ComandoSql="SELECT * FROM Usuario WHERE Placa= ?";
          
      
      try(Connection conectado=basededatos.Conectar();   PreparedStatement stm=conectado.prepareStatement(ComandoSql)){
          
          stm.setString(1, Buscado);
          
          ResultSet Resultado=stm.executeQuery();
          if(Resultado.next()){
              
              
              
              String nombre=Resultado.getString("Nombre");
              int carnet=Resultado.getInt("Carnet");
              String placa=Resultado.getString("Placa");
              String vehiculo=Resultado.getString("Vehículo");
              usuario = new Usuario(nombre,carnet,placa,vehiculo);
              
          }
          Resultado.close();
              
          
          
          
      }catch(SQLException s){
          JOptionPane.showMessageDialog(null,"Error en la busqueda "+s.getMessage(),"ERROR", JOptionPane.ERROR_MESSAGE);
          
      }
      
          
          return usuario;
      }    
    
    
    
    
    
    
}
