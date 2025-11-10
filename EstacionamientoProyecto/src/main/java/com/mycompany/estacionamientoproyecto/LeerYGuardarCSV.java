
package com.mycompany.estacionamientoproyecto;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
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
    
    
   
    
    public void ObtenerDireccion(int tipo){
        
        
        try{
            
       int rs=ventana.showOpenDialog(ventana);
        
       if(rs==JFileChooser.APPROVE_OPTION){
           
        File csv=ventana.getSelectedFile();
        String Direccion=csv.getAbsolutePath();
        Direccioncsv.setText(Direccion);
        
           LeerDatos(csv,tipo);
       }else{
           JOptionPane.showMessageDialog(null,"No selecciono ningun archivo","INFORMACION",JOptionPane.INFORMATION_MESSAGE);
       }
        }catch(Exception e){
            
            JOptionPane.showMessageDialog(null,"ERROR"+e.getMessage(),"ERROR CATASTROFICO jaa",JOptionPane.ERROR_MESSAGE);
            
        }
        
        
    }
    
    
    
    public void LeerDatos(File CSV,int tipoArchivo){
        
       
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
                switch (tipoArchivo) {
                    case 0: {
                         JOptionPane.showMessageDialog(null,"EN DESARROLLO");
                        break;
                    }
                    case 1:{
                       String []datosSpot= reorganizarSpot(info);
                       subirSpot(datosSpot);
                            registro++;
                        break;
                    }
                    case 2: { 
                        String[] datosVehiculo = ReoraganizarVehiculo(info);
                        subirVehiculo(datosVehiculo);
                        registro++;
                        break;
                        
                    }
                    case 3:{
                        
                        String[] datos = reorganizarDatos(info);
                        if (SubirInformacion(Conectado, datos)) registro++;
                        break;
                    }
                    default:
                        JOptionPane.showMessageDialog(null, "Tipo de archivo no soportado.");
                        break;
                }
                      
                }
                
                JOptionPane.showMessageDialog(null,registro+" Archivos leeidos correctamente","EXITO",JOptionPane.INFORMATION_MESSAGE);
                
                
            }
            
        }catch(Exception e){
            
            JOptionPane.showMessageDialog(null,"Error en la lectura del archivo"+e.getMessage(),"ERROR",JOptionPane.ERROR_MESSAGE);
            
        }
        
       
        
    }
    
    
    
    public String Consulta(){
     int a2=0;
    int a4=0;
    
   
    String sql="SELECT Id, Capacidad FROM Areas WHERE Id IN ('A2', 'A4')";
    
    try (Connection Conectado=basededatos.Conectar(); PreparedStatement ps=Conectado.prepareStatement(sql);  ResultSet rs=ps.executeQuery()) {
        
        while (rs.next()) {
            String id = rs.getString("Id");
            int capacidad = rs.getInt("Capacidad");
            
            if ("A2".equals(id)) {
                a2 = capacidad;
            } else if ("A4".equals(id)) {
                a4 = capacidad;
            }
        }
        
        
       if(a2>a4){
           return "A2";
       }else{
           return "A4";
       }
       
       
        
    } catch (SQLException e) {
       
       
       JOptionPane.showMessageDialog(null,"Error"+e.getMessage(),"ERROR",JOptionPane.ERROR_MESSAGE);
        return "Error";
    }
        
        
    }
    
    
    
    public String messirve(String area){
        
        
        if(area.equalsIgnoreCase("A01")){
                area=Consulta();
                
            }else if (area.equalsIgnoreCase("A02")){
                
                area="A1";
            }else{
                area="A3";
            }
        
        return area;
    }
    
    
    public String [] reorganizarDatos(String [] info){
        
        
        String [] Data=new String[8];
         try{
             
  
            
             Data[0]= info[0].trim();
             
             Data[1]=info[1].trim().toUpperCase();
             
             String area=info[2].trim();
             
            
             Data[2]=messirve(area);
             
            Data[3]=info[3].trim();
            Data[4]=info[4].trim();
            Data[5]=info[5].trim();
            
         String modo = info[6].trim().replace("\r", "").replace("\n", "").toUpperCase();

          if(modo.equalsIgnoreCase("FLAT")){
            modo="plano";
          }else if(modo.equalsIgnoreCase("VARIABLE")){
               modo = "variable";
            }else{
              modo=modo.toLowerCase();
}

        Data[6]=modo;
            
        Data[7]=info[7].trim();
        
        
             
         }catch(Exception e){
             
             JOptionPane.showMessageDialog(null,"Error"+e.getMessage(),"ERROR",JOptionPane.ERROR_MESSAGE);
 
         }

return Data;
    }
    
    
    
    
    
    
    public String caambiooo(String vehiculo){
    
        
    if(vehiculo.equalsIgnoreCase("AUTO")){
        vehiculo="automovil";
        
    }else if(vehiculo.equalsIgnoreCase("MOTO")){
        
        vehiculo="moto";
        
    } 
    
    
    return vehiculo;
}
    
    
    public String areaCambio(String vehiculo){
    
        
    if(vehiculo.equalsIgnoreCase("CATEDRATICOS")){
        vehiculo="CATEDRATICO";
        
    }else if(vehiculo.equalsIgnoreCase("ESTUDIANTES")){
        
        vehiculo="ESTUDIANTE";
        
    }else{
        
        return vehiculo;
    }
    
    
    return vehiculo;
}
    
    
    
    
         
    
public  String [] ReoraganizarVehiculo(String [] info){
    
    String [] VehiculoData= new String [3];
    
    try{
    VehiculoData[0]=info[0].trim().toUpperCase();
    
    
    
   String vehiculo=info[1].trim();
   VehiculoData[1]=caambiooo(vehiculo);  
    
    
   String estudia=info[2].trim(); 
   VehiculoData[2]=areaCambio(estudia);
    
    }catch(Exception e){
        e.printStackTrace();
    }
    
    return VehiculoData;
            
}
    
    



private int UsuarioX(Connection Conectado) throws SQLException {
    String select="SELECT UsuarioID FROM Usuario WHERE Nombre = 'GENERICO'";
    try(PreparedStatement ps=Conectado.prepareStatement(select);
         ResultSet rs=ps.executeQuery()){
        if(rs.next()){
            return rs.getInt("UsuarioID");
        }
    }

    String insert ="INSERT INTO Usuario (Nombre, Carnet) VALUES('GENERICO',0)";
    try(PreparedStatement ps=Conectado.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)){
        ps.executeUpdate();
        try (ResultSet rs=ps.getGeneratedKeys()) {
            if (rs.next()){
                return rs.getInt(1);
            }
        }
    }
    throw new SQLException("No se pudo crear ni obtener el usuario Xmen");
}



private void subirVehiculo(String []VehiculoData){
    
    String sql = "INSERT INTO vehiculo (Placa, TipoVehiculo, Area, Usuarioid) VALUES (?, ?, ?, ?)";

    try (Connection Conectado = basededatos.Conectar();  PreparedStatement ps=Conectado.prepareStatement(sql)) {
 
        int usuarioGenericoID = UsuarioX(Conectado);

        
            ps.setString(1,VehiculoData[0].trim()); 
            ps.setString(2,VehiculoData[1].trim().toLowerCase()); 
            ps.setString(3,VehiculoData[2].trim().toUpperCase()); 
            ps.setInt(4, usuarioGenericoID); 

            ps.executeUpdate();
         

    } catch (SQLException e) {
        e.printStackTrace();
    }
    
    
    
    
    
}
    
    
    
public String [] reorganizarSpot(String [] info){


String []datons=new String[4];


try{
    
   datons[0]=info[0].trim();
    
   String spot=info[1].trim();
   
   
   datons[1]=messirve(spot);
   
  String vehiculo=info[2].trim();
  datons[2]=caambiooo(vehiculo);
    
  
  
  String libresiono=info[3].trim();
  
  if(libresiono.equalsIgnoreCase("FREE")||libresiono.equalsIgnoreCase("AVAILABLE")){
      
      datons[3]="libre";
      
  }else if(libresiono.equalsIgnoreCase("OCCUPIED")||libresiono.equalsIgnoreCase("TAKEN")){
      
       datons[3]="ocupado";
  }else{
      
       datons[3]="espera";
      
  }
  
  
  
  
}catch(Exception e){
    
    JOptionPane.showMessageDialog(null,"Error"+e.getMessage(),"ERROR", 0);
}

return datons;

}

public void subirSpot(String [] data){
    
    
    String sql="INSERT INTO Spots (PosicionID,AreaID,Tipo_vehiculo,Estado) VALUES(?,?,?,?)";
    String sql2="UPDATE Areas SET Capacidad=Capacidad+1 WHERE id=?";
   
    
    try(Connection Conectado=basededatos.Conectar()){
         String Spotid=data[1];
         Conectado.setAutoCommit(false);
         
        try(PreparedStatement ps=Conectado.prepareStatement(sql)){
            
            ps.setString(1,data[0]);
            ps.setString(2,data[1]);
            ps.setString(3,data[2]);
            ps.setString(4,data[3]);
          ps.executeUpdate();
        }
        
        try(PreparedStatement p=Conectado.prepareStatement(sql2)){
            
            p.setString(1, Spotid);
            p.executeUpdate();
        }
        
        Conectado.commit();
    }catch(SQLException e){
        
        JOptionPane.showMessageDialog(null,"ERROR"+e.getMessage(),"ERROR", 0);
        
    }
    
    
    
    
}

















    
    
   public boolean SubirInformacion(Connection Conectado, String[] Data) {
    String verificar ="SELECT COUNT(*) FROM Ticket WHERE TicketID = ?";
    String insertar="""
        INSERT INTO Ticket(TicketID, Placa, IdArea, Spotid, FechaIngreso, Fechasalida, modo, monto)
        VALUES(?,?,?,?,?,?,?,?)
    """;
UsuarioDB us=new UsuarioDB();
    try (
        PreparedStatement psVerificar = Conectado.prepareStatement(verificar);
        PreparedStatement psInsertar = Conectado.prepareStatement(insertar)
    ) {
        // Verificamos
        psVerificar.setString(1,Data[0]);
        ResultSet rs=psVerificar.executeQuery();
        rs.next();
        boolean existe=rs.getInt(1) > 0;
        rs.close();

      //generamos tickt nuevo si el que veni ya existe 
        String ticketID = Data[0];
        if(existe){
            ticketID=us.GenerarTicket(Conectado);
           JOptionPane.showMessageDialog(null, "Ticket repetido, se generó uno nuevo: " +ticketID);
        }

        
        psInsertar.setString(1,ticketID);
        psInsertar.setString(2,Data[1]);
        psInsertar.setString(3,Data[2]);
        psInsertar.setString(4,Data[3]);
        psInsertar.setString(5,Data[4]);
        psInsertar.setString(6,Data[5]);
        psInsertar.setString(7,Data[6]);
        psInsertar.setDouble(8,Double.parseDouble(Data[7]));
        psInsertar.executeUpdate();

        return true;

    }catch(SQLException e){
        JOptionPane.showMessageDialog(null,"Error al subir datos: " + e.getMessage(),"ERROR", JOptionPane.ERROR_MESSAGE);
        return false;
    }
}
    
    
    
    
    
}

