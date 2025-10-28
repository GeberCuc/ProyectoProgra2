
package com.mycompany.estacionamientoproyecto;

import java.awt.Color;
import javax.swing.*;

public class VerContenidoMenu {
    
    
    
    private final JMenu miguel;
  
   private final  JPanel ingresar;
   private final JPanel añadirUsuario;
   boolean siCliqueo=false;
   
   public VerContenidoMenu(JMenu miguel,JPanel ingresar,JPanel añadirUsuario){
        
        this.miguel=miguel;
        this.ingresar=ingresar;
        this.añadirUsuario=añadirUsuario;
    }
    
    
   
  public void mostrarPaneles(){
      
      siCliqueo=!siCliqueo;
      
      if(siCliqueo){
        ingresar.setVisible(true);
        añadirUsuario.setVisible(true);
        miguel.setBackground(new java.awt.Color(100, 150, 255));
        miguel.setForeground(Color.WHITE);
        miguel.setOpaque(true);
        
        
      }else{
          
          
        ingresar.setVisible(false);
        añadirUsuario.setVisible(false);  
          miguel.setBackground(null);
          miguel.setForeground(Color.black);
          miguel.setOpaque(false);
      }
        
        
    }
    

    
}
