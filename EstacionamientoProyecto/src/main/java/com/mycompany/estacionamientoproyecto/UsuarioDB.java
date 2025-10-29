
package com.mycompany.estacionamientoproyecto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimerTask;
import javax.swing.JOptionPane;

import java.util.Timer;

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
    
    
    
    
   public boolean AsignarSpot(String Placa,String idarea,String modo,double monto,String vehiculo,double tiempo){
       
String area="";
    if("automovil".equalsIgnoreCase(vehiculo)&&"ESTUDIANTE".equalsIgnoreCase(idarea)) {
        area="A1";
    } else if("moto".equalsIgnoreCase(vehiculo)&&"ESTUDIANTE".equalsIgnoreCase(idarea)) {
        area="A2";
    } else if("automovil".equalsIgnoreCase(vehiculo)&&"CATEDRATICO".equalsIgnoreCase(idarea)) {
        area="A3";
    } else if("moto".equalsIgnoreCase(vehiculo)&&"CATEDRATICO".equalsIgnoreCase(idarea)) {
        area="A4";
    }

    // --- Definición de sentencias SQL ---
    String VerificarCapacidad = "SELECT Capacidad FROM Areas WHERE Id = ?";
    String BuscarSpotLibre = "SELECT PosicionID FROM Spots WHERE AreaID = ? AND Estado = 'libre' LIMIT 1";
    String ActualizarSpot = "UPDATE Spots SET Estado='ocupado' WHERE PosicionID=?";
    String RestarCapacidad = "UPDATE Areas SET Capacidad = Capacidad - 1 WHERE Id = ?";
    String AgregarTicket = """
        INSERT INTO Ticket(TicketID, Placa, IdArea, Spotid, FechaIngreso, Fechasalida, modo, monto,TiempoPagado)
        VALUES(?,?,?,?,?,?,?,?,?)
    """;

    Connection Conectado = null;

    try {
        Conectado = basededatos.Conectar();
        Conectado.setAutoCommit(false);

        // Verificamos si hay cupo disponible
        int capacidad = 0;
        try (PreparedStatement ps = Conectado.prepareStatement(VerificarCapacidad)) {
            ps.setString(1, area);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    JOptionPane.showMessageDialog(null, "area no encontrada: " + area);
                    Conectado.rollback();
                    return false;
                }
                capacidad = rs.getInt("Capacidad");
            }
        }

        if (capacidad <= 0) {
            JOptionPane.showMessageDialog(null, "No hay espacios disponibles en el area " + area);
            Conectado.rollback();
            return false;
        }

        // buscar un espot disponible en area recien obtenida
        String spotDisponible = null;
        try (PreparedStatement ps = Conectado.prepareStatement(BuscarSpotLibre)) {
            ps.setString(1, area);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    spotDisponible = rs.getString("PosicionID");
                } else {
                    JOptionPane.showMessageDialog(null, "No hay spots libres en el area " + area);
                    Conectado.rollback();
                    return false;
                }
            }
        }

        // actualizamos a ocupado
        try (PreparedStatement ps = Conectado.prepareStatement(ActualizarSpot)) {
            ps.setString(1, spotDisponible);
            ps.executeUpdate();
        }

        // restamos una unidad en DB Area, capacidad
        try (PreparedStatement ps = Conectado.prepareStatement(RestarCapacidad)) {
            ps.setString(1, area);
            ps.executeUpdate();
        }

        //ingresamos info a la base de datos
        String ticketid = GenerarTicket(Conectado);
        String FechaIngreso = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        try (PreparedStatement ps = Conectado.prepareStatement(AgregarTicket)) {
            ps.setString(1, ticketid);
            ps.setString(2, Placa);
            ps.setString(3, area);       
            ps.setString(4, spotDisponible);  
            ps.setString(5, FechaIngreso);
            ps.setString(6, null);      
            ps.setString(7, modo);
            ps.setDouble(8, monto);
            ps.setDouble(9, tiempo);
            ps.executeUpdate();
        }

        //finalizamos 
        Conectado.commit();

        JOptionPane.showMessageDialog(
                null,
                "Ticket generado con éxito:\n" +
                "Ticket: " + ticketid + "\n" +
                "Area: " + area + "\n" +
                "Spot: " + spotDisponible,
                "ASIGNACIoN EXITOSA",
                JOptionPane.INFORMATION_MESSAGE
        );

        return true;

    } catch (SQLException e) {
        try {
            if (Conectado != null) Conectado.rollback();
        } catch (SQLException ignored) { }

        JOptionPane.showMessageDialog(
                null,
                "Error al asignar spot o guardar ticket:\n" + e.getMessage(),
                "ERROR SQL",
                JOptionPane.ERROR_MESSAGE
        );
        return false;

    } finally {
        basededatos.cerrar(Conectado);
    }
}
    
   
    
   public boolean RegistrarSalida(String ticketid){
    // Consultas SQL
    String BuscarTicket="SELECT * FROM Ticket WHERE TicketID = ?";
    String ActualizarTicketSalida="UPDATE Ticket SET Fechasalida = ?, monto = ? WHERE TicketID = ?";
    String liberarSpotSQL="UPDATE Spots SET Estado = 'libre' WHERE PosicionID = ?"; 
    String obtenerIdArea="SELECT IdArea FROM Ticket WHERE TicketID = ?";
    String actualizarArea="UPDATE Areas SET Capacidad = Capacidad + 1 WHERE Id = ?";
    
    Connection Conectado=null;
    
    try{
        Conectado=basededatos.Conectar();
        Conectado.setAutoCommit(false);
        
        String SpotID, modopago, IdArea;
        LocalDateTime ingreso;
        double montoPrevio=0;

        // Buscar ticket
        try(PreparedStatement ps=Conectado.prepareStatement(BuscarTicket)){
            ps.setString(1,ticketid);
            try(ResultSet rs=ps.executeQuery()) {
                if(!rs.next()){
                    JOptionPane.showMessageDialog(null,"Error,Ticket no hallado: "+ticketid,"ERROR", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                // Datos del ticket
                SpotID=rs.getString("Spotid");
                modopago=rs.getString("modo");
                ingreso=LocalDateTime.parse(rs.getString("FechaIngreso"));
                montoPrevio=rs.getDouble("monto");
                IdArea=rs.getString("IdArea"); // Obtenemos IdArea para actualizar despues
            }
        }

        LocalDateTime salida=LocalDateTime.now();
        Duration duracion=Duration.between(ingreso, salida);
        long minutos=duracion.toMinutes();
        double nuevoMonto=0;

        // Logica de modos de pago
        if(modopago.equalsIgnoreCase("plano")){
            if(minutos>120){
                // Ticket expirado -liberar spot y sumamos en area
                nuevoMonto=montoPrevio;
                JOptionPane.showMessageDialog(null,"Ticket expirado - Superó las 2 horas", "EXPIRADO", JOptionPane.WARNING_MESSAGE);
                liberarSpotYActualizarArea(Conectado,SpotID,IdArea);
            }else{
                // Modo plano dentro del tiempo límite
                int opcion=JOptionPane.showConfirmDialog(null, 
                    "¿Sale definitivamente?\nTiempo transcurrido: " + minutos + " minutos", 
                    "Salida Modo Plano", 
                    JOptionPane.YES_NO_OPTION);
                
                if (opcion == JOptionPane.YES_OPTION) {
                    // Sale de forma definitiva
                    nuevoMonto=montoPrevio;
                    liberarSpotYActualizarArea(Conectado,SpotID,IdArea);
                }else{
                    // Salida temporal modo espera
                    iniciarModoEspera(ticketid,SpotID,IdArea,Conectado);
                    Conectado.commit();
                    JOptionPane.showMessageDialog(null,"Spot en espera por 2 horas","MODO ESPERA", JOptionPane.INFORMATION_MESSAGE);
                    return true;
                }
            }
            
        } else if(modopago.equalsIgnoreCase("variable")){
            // Calcular monto variable
            double tarifaHora=10.0;
            double horas=minutos / 60.0;
            nuevoMonto=Math.ceil(horas) * tarifaHora; 
            
            // Procesar pago
            double pago=solicitarPago(nuevoMonto);
            if (pago<nuevoMonto){
                JOptionPane.showMessageDialog(null,"Pago insuficiente","ERROR", JOptionPane.ERROR_MESSAGE);
                Conectado.rollback();
                return false;
            }
            
            // Calcular y mostrar vuelto
            double vuelto=pago-nuevoMonto;
            if(vuelto>0){
                JOptionPane.showMessageDialog(null,String.format("Pago recibido: Q%.2f\nVuelto: Q%.2f", pago, vuelto),"PAGO EXITOSO", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
            
            liberarSpotYActualizarArea(Conectado,SpotID,IdArea);
        }

        // Actualizar ticket
        try(PreparedStatement ps=Conectado.prepareStatement(ActualizarTicketSalida)){
            ps.setString(1,salida.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            ps.setDouble(2,nuevoMonto);
            ps.setString(3,ticketid);
            ps.executeUpdate();
        }

        Conectado.commit();
        JOptionPane.showMessageDialog(null,"Proceso completado exitosamente","ÉXITO", JOptionPane.INFORMATION_MESSAGE);
        return true;

    }catch(SQLException h){
        try{ 
            if (Conectado !=null) Conectado.rollback(); 
        }catch (SQLException ignored){}
        JOptionPane.showMessageDialog(null,"Error al registrar salida: "+h.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        return false;
    }finally{
        basededatos.cerrar(Conectado);
    }
}

//Liberar spot Y sumar +1 en Area
private void liberarSpotYActualizarArea(Connection Conectado,String spotID,String idArea) throws SQLException{
    //  Liberaramos el spot
    String liberarSpotSQL="UPDATE Spots SET Estado = 'libre' WHERE PosicionID = ?";
    try(PreparedStatement ps=Conectado.prepareStatement(liberarSpotSQL)){
        ps.setString(1,spotID);
        ps.executeUpdate();
    }
    
    //  Sumamos +1 a la capacidad del area
    String actualizarAreaSQL="UPDATE Areas SET Capacidad = Capacidad + 1 WHERE Id = ?";
    try (PreparedStatement ps=Conectado.prepareStatement(actualizarAreaSQL)) {
        ps.setString(1,idArea);
        ps.executeUpdate();
    }
}

//Modo espera
private void iniciarModoEspera(String ticketID,String spotID,String idArea, Connection Conectado)throws SQLException{
    // Actualizar spot como espera
    String ActualizarSpot="UPDATE Spots SET Estado = 'espera' WHERE PosicionID = ?";
    try(PreparedStatement ps=Conectado.prepareStatement(ActualizarSpot)){
        ps.setString(1,spotID);
        ps.executeUpdate();
    }
    
    // Programar verificación de expiración
    Timer timer=new Timer();
    timer.schedule(new TimerTask() {
        @Override
        public void run() {
            verificarExpiracionEspera(ticketID, spotID, idArea);
            timer.cancel();
        }
    }, 7200000L); 
}

// Verificar expiración
private void verificarExpiracionEspera(String ticketID,String spotID,String idArea) {
    Connection Conectado=null;
    try{
        Conectado=basededatos.Conectar();
        Conectado.setAutoCommit(false);
        
        // Verificar si el spot sigue en espera
        String checkSQL="SELECT Estado FROM Spots WHERE PosicionID = ?";
        
        try(PreparedStatement ps = Conectado.prepareStatement(checkSQL)){
            ps.setString(1,spotID); 
            ResultSet rs=ps.executeQuery();
            
            if (rs.next() && "espera".equals(rs.getString("Estado"))) {
              
                liberarSpotYActualizarArea(Conectado,spotID,idArea);
            }
        }
        
        Conectado.commit();
    }catch (SQLException e){
        try{
            if(Conectado !=null)Conectado.rollback();
        }catch(SQLException ex){}
        e.printStackTrace();
    }finally{
        basededatos.cerrar(Conectado);
    }
}


private double solicitarPago(double montoRequerido){
    String input=JOptionPane.showInputDialog(null, 
        String.format("Monto a pagar: Q%.2f\nIngrese el monto:", montoRequerido), 
        "PAGO", 
        JOptionPane.QUESTION_MESSAGE);
    
    try{
        return Double.parseDouble(input);
    }catch (NumberFormatException e){
        return 0;
    }
}

private double obtenerMontoPlano(String ticketID, Connection Conectado)throws SQLException{
    String sql="SELECT monto FROM Ticket WHERE TicketID = ?";
    try(PreparedStatement ps = Conectado.prepareStatement(sql)){
        ps.setString(1,ticketID);
        ResultSet rs=ps.executeQuery();
        return rs.next() ? rs.getDouble("monto") : 0;
    }
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