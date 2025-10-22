
package com.mycompany.estacionamientoproyecto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.JOptionPane;

public class UsuarioDB {
    
    
    public void AgregarDb(Usuario usuariodb,Vehiculo v){
        
        
       String sql="INSERT INTO Usuario (Nombre, Carnet) VALUES (?, ?)";
        String sql2="INSERT INTO Vehiculo (Placa, TipoVehiculo, Area, Usuarioid) VALUES (?, ?, ?, ?)";
        
        
  //conectar a la base de datos mediante la clase basededatos, PreparedStatement es para generar una consulta seguro.
        try(Connection Conectado=basededatos.Conectar()){
            Conectado.setAutoCommit(false);
            
            try(PreparedStatement Consulta=Conectado.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS) ){
            
            Consulta.setString(1,usuariodb.getNombre());
            Consulta.setInt(2,usuariodb.getCarnet());
            
  
            //Esto es lo que realmente sube los datos 
            Consulta.executeUpdate();
            
            
            
            //genera el ID para el usuario y vehiculo 
            try (ResultSet rs =Consulta.getGeneratedKeys()) {
                if (rs.next()) {
                    int usuarioId = rs.getInt(1);
                    usuariodb.setUsuarioID(usuarioId); 
                }
            }
            
            }
             try(PreparedStatement quebusca=Conectado.prepareStatement(sql2)){
            
            quebusca.setString(1,v.getPlaca());
            quebusca.setString(2,v.getTipo());
            quebusca.setString(3,v.getPuesto());    
            quebusca.setInt(4,usuariodb.getUsuarioID());
            quebusca.executeUpdate();
            
        }
             Conectado.commit();
            JOptionPane.showMessageDialog(null,"Usuario: "+usuariodb.getNombre()+" Agregado correctamente","Añadido Correctamente",JOptionPane.INFORMATION_MESSAGE);
        }catch(SQLException e){
            
            JOptionPane.showMessageDialog(null,"Error al añadir nuevo usuario"+e.getMessage(),"ERROR",JOptionPane.ERROR_MESSAGE);
            
        }
        
        
        
        
       
        
    }
    
    
    
    
   public boolean AsignarSpot(String Placa,String idarea,String SpotId,String modo,double monto){
       
       String ActualizarSpot="UPDATE Spot Estado='ocupado' WHERE PosicionID =?";
       
       String AgregarTicket= """
                INSERT INTO Ticket(TicketID,Placa,IdArea,Spotid,FechaIngreso,FechaSalida,modo,monto) VALUES(?,?,?,?,?,?,?)  
                """;
            
       Connection Conectado=null;
            try {Conectado=basededatos.Conectar(); Conectado.setAutoCommit(false);
                
                try(PreparedStatement ps=Conectado.prepareStatement(ActualizarSpot)){
                    
                    
                    ps.setString(1, SpotId);
                    ps.executeUpdate();
                }
                
                String ticketid= GenerarTicket(Conectado);
                
                
                String FechaIngreso=LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                
                try(PreparedStatement Pt =Conectado.prepareStatement(AgregarTicket)){
                    
                    
                    //ingresando datos a la tabla ticker en db
                    Pt.setString(1, ticketid);
                    Pt.setString(2, Placa);
                    Pt.setString(3, idarea);
                    Pt.setString(4, SpotId);
                    Pt.setString(5,FechaIngreso);
                    Pt.setString(6, modo);
                    Pt.setDouble(7, monto);
                    
                    Pt.executeUpdate();
                }
                
                Conectado.commit();
                JOptionPane.showMessageDialog(null,"Numero de ticket: "+ticketid,"AGREGADO ALA BASE DE DATOS",JOptionPane.INFORMATION_MESSAGE);
               return true;
               
            } catch(SQLException e){
                
                
                try{ if(Conectado!=null)Conectado.rollback();
                }
                catch(SQLException ignored){
                  
                }
                
                JOptionPane.showMessageDialog(null, "Erro al asignar ticket","ERROR",JOptionPane.ERROR);
                return false;
            } finally{
                
                
                basededatos.cerrar(Conectado);
            }
           
   }
    
    
   
   public boolean RegistrarSalida(String ticketid){
       
       
       String BuscarTicket="SELECT * FROM  Ticket WHERE TicktdID =?";
       String ActualizarTicketSalida="UPDATE Ticket SET Fechasalida=?, modo=?, monto=?, Estado=? WHERE TicketID=?";
       String liberar="UPDATE Spots SET Estado='libre', WHERE Spotid=?";
       
       
       Connection Conectado=null;
       
       
       try{
           Conectado=basededatos.Conectar(); Conectado.setAutoCommit(false);
           String SpotID,modopago;
           
           
           LocalDateTime ingreso;
           
           
           
           
           try(PreparedStatement ps= Conectado.prepareStatement(BuscarTicket)){
               
               ps.setString(1, ticketid);
               try(ResultSet rs=ps.executeQuery()){
                   
                   
                   if(!rs.next()){
                       
                       JOptionPane.showMessageDialog(null,"Error, Ticket no hallado en la Base de datos: "+ticketid,"ERROR",JOptionPane.ERROR_MESSAGE);
                       return false;
                       
                   }
                   
                   
                   SpotID=rs.getString("Spotid");
                  modopago=rs.getString("modo");
                  ingreso=LocalDateTime.parse(rs.getString("FechaIngreso"));
                  
               }
               
               
           }
           
           LocalDateTime salida=LocalDateTime.now();
           Duration duracion=Duration.between(ingreso, salida);
           long minutos=duracion.toMinutes();
           double nuevoMonto=0;
           String EstadoAct="finalizado";
           
           if(modopago.equalsIgnoreCase("plano")){
               if(minutos>120){
                   
               EstadoAct="Expirado";
               
               JOptionPane.showMessageDialog(null,"Su ticket expirto","EXPIRADO SUPERO LAS 2 HORAS",JOptionPane.INFORMATION_MESSAGE);
               
               } else{
               nuevoMonto= obtenerMontoPlano(ticketid,Conectado);
           }
           }else if(modopago.equalsIgnoreCase("variable")){
               
                   double  tarifaHora=10.0;
                   nuevoMonto=(minutos/60.0)*tarifaHora;
                EstadoAct="finalizado";
           }
         
             
        
       
           
           try(PreparedStatement ps=Conectado.prepareStatement(ActualizarTicketSalida)){
               
              
                ps.setString(1, salida.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                ps.setDouble(2, nuevoMonto);
                ps.setString(3, modopago);
                ps.setString(4, EstadoAct);
                ps.setString(5, ticketid);
                ps.executeUpdate();
               
               
           }
           
           
           
           try(PreparedStatement PS=Conectado.prepareStatement(liberar)){
               
               PS.setString(1, SpotID);
              PS.executeUpdate();
               
               
           }
           Conectado.commit();
           JOptionPane.showMessageDialog(null,"Ticket Pagado","PAGO REALIZADO",JOptionPane.INFORMATION_MESSAGE);
           return true;
       
       }catch(SQLException h){
           
           try { if(Conectado!=null) Conectado.rollback(); 
           } catch (SQLException ignored) {
           }
            JOptionPane.showMessageDialog(null, "Error al registrar salida: " + h.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            return false;
        } finally {
            basededatos.cerrar(Conectado);
           
             
       }
   }
   
   
   
   
   
   
       
      private double obtenerMontoPlano(String ticketid,Connection Conectado) throws SQLException{

      String sql= "SELECT monto FROM Ticket WHERE TicketID = ?";
        try (PreparedStatement ps=Conectado.prepareStatement(sql)){
            ps.setString(1,ticketid);
            try (ResultSet rs=ps.executeQuery()) {
                if(rs.next()) {
                   return rs.getDouble("monto");
                }
            }
        }
        return 0;

}  
   
   
   
   
    
  public Vehiculo BuscarPorPlaca(String Buscado){
          
      Vehiculo auto=null;
          
      String ComandoSql="SELECT * FROM vehiculo WHERE Placa= ?";
          
      
      try(Connection Conectado=basededatos.Conectar();   PreparedStatement stm=Conectado.prepareStatement(ComandoSql)){
          
          stm.setString(1, Buscado);
          
          ResultSet Resultado=stm.executeQuery();
          if(Resultado.next()){
              
              
              String placa=Resultado.getString("Placa");
              String vehiculo=Resultado.getString("TipoVehiculo");
              String area=Resultado.getString("Area");
              
              auto= new Vehiculo(placa,vehiculo,area);
              
              
          }
          Resultado.close();
              
          
          
          
      }catch(SQLException s){
          JOptionPane.showMessageDialog(null,"Error en la busqueda "+s.getMessage(),"ERROR", JOptionPane.ERROR_MESSAGE);
          
      }
      
          
          return auto;
      }    
    
  
  
  
  
  
  
  
    private String GenerarTicket(Connection Conectado)throws SQLException {
        
        String consulta = "SELECT TicketID FROM Ticket ORDER BY TicketID DESC LIMIT 1";
        try (PreparedStatement ps = Conectado.prepareStatement(consulta);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                String ultimo = rs.getString("TicketID");
                int numero = Integer.parseInt(ultimo.substring(2)) + 1;
                return String.format("T-%04d", numero);
            } else {
                return "T-0001";
            }
        }
    }
        
        
}