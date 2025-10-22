
package com.mycompany.estacionamientoproyecto;



public class Usuario {

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public int getCarnet() {
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
  private int Carnet;
  private Vehiculo vehiculoUs;
 
    
  

   public Usuario(int UsuarioID,String nombre,int carnet, Vehiculo vehiculoUs){
       
       this.Nombre=nombre;
       this.Carnet=carnet;
       this.UsuarioID=UsuarioID;
       this.vehiculoUs=vehiculoUs;
   }
   
   
   
   
 
   public String toString(){
         
       return  "Usuario { " +
                "ID=" + UsuarioID +
                ", nombre='" + Nombre + '\'' +
                ", carnet=" + Carnet +
                (vehiculoUs != null ? ", vehiculo=" + vehiculoUs.getPlaca() : "") +
                " }";
   }
}
 
 