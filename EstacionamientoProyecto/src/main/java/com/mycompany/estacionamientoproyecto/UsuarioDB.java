
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
import javax.swing.*;

import java.util.Timer;
import javax.swing.table.DefaultTableModel;

public class UsuarioDB {
    
    
    public void AgregarDb(Usuario usuariodb,Vehiculo v){
        
        
       String sql="INSERT INTO Usuario (Nombre, Carnet) VALUES (?, ?)";
        String sql2="INSERT INTO Vehiculo (Placa, TipoVehiculo, Area, Usuarioid) VALUES (?, ?, ?, ?)";
        
        
  //conectar a la base de datos mediante la clase basededatos, PreparedStatement es para generar una consulta seguro.
        try(Connection Conectado=basededatos.Conectar()){
            Conectado.setAutoCommit(false);
            
            try(PreparedStatement Consulta=Conectado.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS) ){
            
            Consulta.setString(1,usuariodb.getNombre());
            Consulta.setLong(2,usuariodb.getCarnet());
            
  
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
    
    
    
    
    
    public String area (String vehiculo,String idarea){
        
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
        
        
        return area;
    }
    
    
    
   public boolean AsignarSpot(String Placa,String idarea,String modo,double monto,String vehiculo,double tiempo){
       
   String area=area(vehiculo, idarea);

  String VerificarExistencia = "SELECT COUNT(*) AS Activos FROM Ticket WHERE Placa = ? AND Fechasalida IS NULL";
   
    String VerificarCapacidad= "SELECT Capacidad FROM Areas WHERE Id = ?";
    String BuscarSpotLibre= "SELECT PosicionID FROM Spots WHERE AreaID = ? AND Estado = 'libre' LIMIT 1";
    String ActualizarSpot="UPDATE Spots SET Estado='ocupado' WHERE PosicionID=?";
    String RestarCapacidad ="UPDATE Areas SET Capacidad = Capacidad - 1 WHERE Id = ?";
    String AgregarTicket ="""
        INSERT INTO Ticket(TicketID, Placa, IdArea, Spotid, FechaIngreso, Fechasalida, modo, monto,TiempoPagado)
        VALUES(?,?,?,?,?,?,?,?,?)
    """;

    Connection Conectado = null;

    try {
        Conectado = basededatos.Conectar();
        Conectado.setAutoCommit(false);
        





try (PreparedStatement ps = Conectado.prepareStatement(VerificarExistencia)) {
    ps.setString(1, Placa);
    try (ResultSet rs = ps.executeQuery()) {
        if (rs.next() && rs.getInt("Activos") > 0) {
            JOptionPane.showMessageDialog(null,"Esta placa ya tiene un ticket activo.","Ticket Activo",JOptionPane.INFORMATION_MESSAGE);
            Conectado.rollback();
            return false; 
        }
    }
}     
        
        
        
  
        

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
        
        
      if (!"variable".equalsIgnoreCase(modo)){
    Transacciones tra=new Transacciones();
    double cobrado=tra.planoCobro(Conectado,monto);
    
    if (cobrado==0){
        Conectado.rollback();
        return false;
    }
    monto=cobrado;
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

        JOptionPane.showMessageDialog(null, "Ticket generado con éxito:\n" +"Ticket: " + ticketid + "\n" +"Area: " + area + "\n" +"Spot: " + spotDisponible,"ASIGNACIoN EXITOSA",JOptionPane.INFORMATION_MESSAGE  );

        return true;
        
    } catch (SQLException e) {
        try {
            if (Conectado != null) Conectado.rollback();
        } catch (SQLException ignored) { }

        JOptionPane.showMessageDialog( null,"Error al asignar spot o guardar ticket:\n" + e.getMessage(),  "ERROR SQL",JOptionPane.ERROR_MESSAGE );
        return false;

    } finally {
        basededatos.cerrar(Conectado);
    }
}
    
   
   
   
   
   
   
   
   
   
    
   public boolean RegistrarSalida(String ticketid){
    // Consultas SQL
    
    String BuscarTicket="SELECT * FROM Ticket WHERE TicketID = ?";
    String ActualizarTicketSalida="UPDATE Ticket SET Fechasalida = ?, monto = ? WHERE TicketID = ?";
   
    
    Connection Conectado=null;
    
    try{
    Conectado=basededatos.Conectar();
    Conectado.setAutoCommit(false);

    String SpotID,modopago,IdArea;
    LocalDateTime ingreso;
    double montoPrevio=0;

    
   String fechaSalida="";
    
    // Buscar ticket existente
    try (PreparedStatement ps = Conectado.prepareStatement(BuscarTicket)){
        ps.setString(1, ticketid);
        try (ResultSet rs = ps.executeQuery()){
            if(!rs.next()){
                JOptionPane.showMessageDialog(null,"Error, Ticket no hallado: "+ticketid,"ERROR",JOptionPane.ERROR_MESSAGE);
                return false;
            }
            SpotID=rs.getString("Spotid");
            modopago=rs.getString("modo");
            ingreso=LocalDateTime.parse(rs.getString("FechaIngreso"));
            fechaSalida=rs.getString("FechaSalida");
            montoPrevio=rs.getDouble("monto");
            IdArea=rs.getString("IdArea");
        }
    }

    LocalDateTime salida= LocalDateTime.now();
    Duration duracion=Duration.between(ingreso, salida);
    long minutos=duracion.toMinutes();
    double nuevoMonto=0;

  
    if(modopago.equalsIgnoreCase("plano")){
    if(fechaSalida!= null&&!fechaSalida.isEmpty()){
        JOptionPane.showMessageDialog(null,"Ticket Expirado","INFORMACION",JOptionPane.INFORMATION_MESSAGE);
        Conectado.rollback();
        return false;
    }

    if(minutos>120){
        if (spotEnEspera(Conectado, SpotID)) {
            nuevoMonto = montoPrevio;
            JOptionPane.showMessageDialog(null,"Ticket expirado (spot estaba en espera).","EXPIRADO",JOptionPane.WARNING_MESSAGE);
            liberarSpotYActualizarArea(Conectado,SpotID,IdArea);
        } else {
            nuevoMonto=montoPrevio; 
        }
    }else{
        int opcion = JOptionPane.showConfirmDialog(null,"¿Sale definitivamente?\nTiempo transcurrido: "+minutos+" minutos","Salida Modo Plano", JOptionPane.YES_NO_OPTION);

        if(opcion==JOptionPane.YES_OPTION){
            nuevoMonto=montoPrevio;
            liberarSpotYActualizarArea(Conectado,SpotID,IdArea);
        }else{
            iniciarModoEspera(ticketid,SpotID,IdArea,Conectado);
            Conectado.commit();
            JOptionPane.showMessageDialog(null,"Spot en espera por 2 horas","MODO ESPERA",JOptionPane.INFORMATION_MESSAGE);
            return true;
        }
    }

}else if(modopago.equalsIgnoreCase("variable")) {
    Transacciones trans=new Transacciones();

    boolean exito=trans.cobrar(Conectado, minutos);
    nuevoMonto=trans.getGanancia();

    if(!exito){
        try{
            Conectado.rollback();
        }catch(SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    liberarSpotYActualizarArea(Conectado, SpotID, IdArea);
}

       
    
    // Actualizar salida
    try (PreparedStatement ps=Conectado.prepareStatement(ActualizarTicketSalida)) {
        ps.setString(1,salida.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        ps.setDouble(2,nuevoMonto);
        ps.setString(3,ticketid);
        ps.executeUpdate();
    }

    Conectado.commit();
    JOptionPane.showMessageDialog(null,"Proceso completado exitosamente","EXITO",JOptionPane.INFORMATION_MESSAGE);
    return true;

}catch(SQLException h) {
    try{
        if(Conectado!=null)Conectado.rollback();
    }catch (SQLException ignored) {}
    JOptionPane.showMessageDialog(null,"Error al registrar salida: "+h.getMessage(),"ERROR",JOptionPane.ERROR_MESSAGE);
    return false;
}finally{
    basededatos.cerrar(Conectado);
}
}

   
   
   
   public boolean ReingresoTicket(String ticketid) {
    String BuscarTicket="SELECT * FROM Ticket WHERE TicketID =?";
    String actualizarSpot="UPDATE Spots SET Estado='ocupado' WHERE PosicionID=?";
    Connection Conectado=null;

    try{
        Conectado=basededatos.Conectar();
        Conectado.setAutoCommit(false);

        String SpotID;
        LocalDateTime ingreso;

        try(PreparedStatement ps=Conectado.prepareStatement(BuscarTicket)){
            ps.setString(1,ticketid);
            try(ResultSet rs=ps.executeQuery()){
                if(!rs.next()) {
                    JOptionPane.showMessageDialog(null,"Ticket no encontrado: "+ticketid,"ERROR",JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                SpotID= rs.getString("Spotid"); //obtenemos datos
                ingreso=LocalDateTime.parse(rs.getString("FechaIngreso"));
            }
        }

        LocalDateTime ahora=LocalDateTime.now();
        long minutos=Duration.between(ingreso, ahora).toMinutes();

        if(minutos<=120){
            int opcion= JOptionPane.showConfirmDialog(null," De 2 horas han pasodo (" + minutos + " min).\n¿Desea reingresar con la misma placa?","REINGRESO",JOptionPane.YES_NO_OPTION);

            if(opcion==JOptionPane.YES_OPTION){
                try(PreparedStatement ps= Conectado.prepareStatement(actualizarSpot)){
                    ps.setString(1, SpotID);
                    ps.executeUpdate();
                }
                
                Conectado.commit();
                JOptionPane.showMessageDialog(null,"Reingreso exitoso. Spot marcado como ocupado.","REINGRESO",JOptionPane.INFORMATION_MESSAGE);
                return true;
            }
        }else{
            
            JOptionPane.showMessageDialog(null,"Su tiempo de reigresar se agoto, registrelo en la salida para finalizar el ticket","TIEMPO AGOTADO",JOptionPane.INFORMATION_MESSAGE);
        }

        Conectado.rollback();
        return false;

    }catch(SQLException e){
        try{if (Conectado != null)Conectado.rollback();
        }catch(SQLException ignored){
        }
        JOptionPane.showMessageDialog(null,"Error en reingreso: "+ e.getMessage(),"ERROR",JOptionPane.ERROR_MESSAGE);
        return false;
    }finally{
        basededatos.cerrar(Conectado);
    }
}
   
   private boolean spotEnEspera(Connection con,String spotId)throws SQLException {
    String sql="SELECT Estado FROM Spots WHERE PosicionID=?";
    try (PreparedStatement ps=con.prepareStatement(sql)){
        ps.setString(1, spotId);
        try(ResultSet rs=ps.executeQuery()){
            if(rs.next()){
                String estado=rs.getString("Estado");
                return estado!=null&& estado.equalsIgnoreCase("ESPERA");
            }
        }
    }
    return false;
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
        
        
        String checkSQL="SELECT Estado FROM Spots WHERE PosicionID = ?";
        
        try(PreparedStatement ps=Conectado.prepareStatement(checkSQL)){
            ps.setString(1,spotID); 
            ResultSet rs=ps.executeQuery();
            
            if(rs.next()&&"espera".equals(rs.getString("Estado"))) {
              
                liberarSpotYActualizarArea(Conectado,spotID,idArea);
            }
        }
        
        Conectado.commit();
    }catch(SQLException e){
        try{
            if(Conectado!=null)Conectado.rollback();
        }catch(SQLException ex){
        }
        e.printStackTrace();
    }finally{
        basededatos.cerrar(Conectado);
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
    
  
  

  
    public String GenerarTicket(Connection Conectado)throws SQLException {
        
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
        
   
    
    
    
    public void datosEnTabla(JTable TablaParaVisualizar,String Consulta){
        
        
        
        try(Connection Conectado=basededatos.Conectar(); PreparedStatement ps=Conectado.prepareStatement(Consulta); ResultSet rs=ps.executeQuery();){
            
           
            DefaultTableModel ver=(DefaultTableModel)TablaParaVisualizar.getModel();
            

            ver.setRowCount(0);
            
            
            
            while(rs.next()){
                
                  Object [] datos={
                      
                      rs.getString("TicketID"),
                      rs.getString("Placa"),
                      rs.getString("IdArea"),
                      rs.getString("Spotid"),
                      rs.getString("FechaIngreso"),
                      rs.getString("FechaSalida"),
                      rs.getString("modo"),
                      rs.getString("monto"),
                      rs.getString("TiempoPagado")
                  
                  }; 
            
                  ver.addRow(datos);
                  
            }
            
         
            
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERROR"+e.getMessage(),"ERROR", JOptionPane.ERROR_MESSAGE);
        }
        
        
    }
    
    
    public String DatosDeldia() {
    double ganancias=0;
    int hoy=0;
    int spotTotal=0;
    int spotOcupados=0;
    
    String consulta="SELECT GananciaTotal, SpotsUtilizados FROM Actividad WHERE Fecha = DATE('now','localtime')";
    String consultaTotal="SELECT COUNT(*) FROM Spots"; 
       String consultaOcupados="SELECT COUNT(*) FROM Spots WHERE Estado IN ('ocupado','espera')";
    try(Connection Conectado=basededatos.Conectar()){

      
        try (PreparedStatement ps=Conectado.prepareStatement(consulta);
             ResultSet rs=ps.executeQuery()) {

            if(rs.next()){
                ganancias=rs.getDouble("GananciaTotal");
                hoy=rs.getInt("SpotsUtilizados");
            }
        }

     
        try(PreparedStatement ps2=Conectado.prepareStatement(consultaTotal);
             ResultSet rs2=ps2.executeQuery()) {
            if (rs2.next()){
                spotTotal=rs2.getInt(1);
            }
        }
        
        
        
         try(PreparedStatement ps3=Conectado.prepareStatement(consultaOcupados);
             ResultSet rs3=ps3.executeQuery()) {
            if (rs3.next()){
                spotOcupados=rs3.getInt(1);
            }
        

        }
      return String.format(
            "Ganancias: Q%.2f - Spots utilizados hoy: %d - Ocupados actualmente: %d / %d",ganancias,hoy, spotOcupados, spotTotal );
    }catch(SQLException e){
        JOptionPane.showMessageDialog(null, "Error en la búsqueda: " + e.getMessage(),"ERROR", JOptionPane.ERROR_MESSAGE);
        return "ERROR AL OBTENER LOS DATOS";
    }
}
        
        
        
    public void buscarPorTicket(String Buscado, JTable T){
          
      
          
      String ComandoSql="SELECT * FROM Ticket WHERE TicketID= ?";
          
      
      try(Connection Conectado=basededatos.Conectar();PreparedStatement stm=Conectado.prepareStatement(ComandoSql)){
          
          stm.setString(1, Buscado);
          DefaultTableModel ver=(DefaultTableModel)T.getModel();
            

            ver.setRowCount(0);
            
            ResultSet rs=stm.executeQuery();
            
            while(rs.next()){
                
                  Object [] datos={
                      
                      rs.getString("TicketID"),
                      rs.getString("Placa"),
                      rs.getString("IdArea"),
                      rs.getString("Spotid"),
                      rs.getString("FechaIngreso"),
                      rs.getString("FechaSalida"),
                      rs.getString("modo"),
                      rs.getString("monto"),
                      rs.getString("TiempoPagado")
                  
                  }; 
            
                  ver.addRow(datos);
                  
            }
          
      }catch(SQLException s){
          JOptionPane.showMessageDialog(null,"Error en la busqueda "+s.getMessage(),"ERROR", JOptionPane.ERROR_MESSAGE);
          
      }
      
          
          
      }   
  
    
    
    
    
    
    
}