
package com.mycompany.estacionamientoproyecto;

import javax.swing.ButtonGroup;


public class IngresarDatos {
    
    
    private String Nombre;
    private String Placa;
    private String Modelo;
    private int tiempo;
    private  final ButtonGroup ButtonGroupIngresar;
    
    
    
     public IngresarDatos(String Nombre,String Placa,String Modelo,int tiempo, ButtonGroup ButtonGroupIngresar){
         
         this.Nombre=Nombre;
         this.Placa=Placa;
         this.Modelo=Modelo;
         this.tiempo=tiempo;
         
         this.ButtonGroupIngresar=ButtonGroupIngresar;
     }
    
    
    
     
     
     
     
     
     
}
