
package com.mycompany.estacionamientoproyecto;



public class Usuario {

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public long getCarnet() {
        return Carnet;
    }

    public void setCarnet(int Carnet) {
        this.Carnet = Carnet;
    }

    public int getUsuarioID() {
        return UsuarioID;
    }

    public void setUsuarioID(int UsuarioID) {
        this.UsuarioID = UsuarioID;
    }

   
  private int UsuarioID;
  private String Nombre;
  private long Carnet;
  private Vehiculo vehiculoUs;
 
    
  /*
  Esta clase actúa como un DATA TRANSFER OBJECT (DTO).
  Su única función es transportar los datos obtenidos desde la interfaz gráfica
  hacia la clase UsuarioDB, que se encarga de subir la información a la base de datos.
  No realiza procesos lógicos, solo sirve como contenedor temporal de datos.
 */
   public Usuario(int UsuarioID,String nombre,long carnet, Vehiculo vehiculoUs){
       
       this.Nombre=nombre;
       this.Carnet=carnet;
       this.UsuarioID=UsuarioID;
       this.vehiculoUs=vehiculoUs;
   }
   
   
}
 
 