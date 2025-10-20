
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

    public String getPlaca() {
        return Placa;
    }

    public void setPlaca(String Placa) {
        this.Placa = Placa;
    }

    public String getVehiculo() {
        return Vehiculo;
    }

    public void setVehiculo(String Vehiculo) {
        this.Vehiculo = Vehiculo;
    }
    
  private String Nombre;
  private int Carnet;
  private String Placa;
  private String Vehiculo;
 
    
  

   public Usuario(String nombre,int carnet, String placa,String Vehiculo){
       
       this.Nombre=nombre;
       this.Carnet=carnet;
       this.Placa=placa;  
       this.Vehiculo=Vehiculo;
   }
   
   
   
   
 
   public String toString(){
         
       return "Usuario:" +"nombre='" + Nombre + '\'' +", carnet=" + Carnet +", placa='" + Placa + '\'' +", vehiculo='" + Vehiculo;
   }
}
 
 