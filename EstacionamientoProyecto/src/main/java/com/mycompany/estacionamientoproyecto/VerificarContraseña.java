
package com.mycompany.estacionamientoproyecto;
import  java.util.Arrays;
import javax.swing.JOptionPane;
import javax.swing.*;

import java.awt.event.ActionEvent;



public class VerificarContraseña {

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public char[] getContraseña() {
        return contraseña;
    }

    public void setContraseña(char[] contraseña) {
        this.contraseña = contraseña;
    }
    
    
    private String usuario;
    private char[] contraseña;
    protected static int intentos=3; 
    
    
   protected final char[] contra={'g','e','b','r','2','|'};
   protected final String us="geber12";
   boolean verificarPaso=true;
   
   
   
   
   
   private final  JPanel PanelIngresarDatos;
   private final JPanel PanelLogin;
   private final JTextField UsuarioIngresado;
   private final JPasswordField ContraIngresada;
   private final JButton IniciarSesion;
   private final JLabel TxtEspera; 
   private final JProgressBar BarraTiempo;
   private final ButtonGroup ButtGrupoLogin;
   private final JPanel JpanelAñadirUsuario;
   
   public VerificarContraseña(String usuario, char[]contraseña, JPanel PanelIngresarDatos,JPanel PanelLogin,JTextField UsuarioIngresado,JPasswordField ContraIngresada,JButton IniciarSesion,JLabel TxtEspera,JProgressBar BarraTiempo,ButtonGroup ButtGrupoLogin,JPanel JpanelAñadirUsuario){
       
       
       this.usuario=usuario;
       this.contraseña=contraseña;
       this.PanelIngresarDatos=PanelIngresarDatos;
       this.PanelLogin=PanelLogin;
        this.UsuarioIngresado=UsuarioIngresado;
        this.ContraIngresada= ContraIngresada;
        this.IniciarSesion=IniciarSesion;
        this.TxtEspera=TxtEspera;
        this.BarraTiempo=BarraTiempo;
         this.ButtGrupoLogin=ButtGrupoLogin;
         this.JpanelAñadirUsuario=JpanelAñadirUsuario;
   }
   
    
    
    public  void verificar(){
        
        boolean pasas=Arrays.equals(contraseña, contra)&&usuario.equals(us);
        
     
        
        if(ButtGrupoLogin.isSelected(null)){
            
            JOptionPane.showMessageDialog(null,"Seleccione una opcion entre Empleado o Administrador","ERROR",JOptionPane.INFORMATION_MESSAGE);
           
            
        }else{
      if(pasas){
          
          JOptionPane.showMessageDialog(null,"Bienvenido");
          Arrays.fill(contraseña, '°');
          PanelLogin.setVisible(false);
          PanelIngresarDatos.setVisible(true);
          JpanelAñadirUsuario.setVisible(false);
      }
        
      if(!pasas){
          intentos--;
          Arrays.fill(contraseña, '°');
          JOptionPane.showMessageDialog(null,"Contraseña o Usuario incorrecto, intentos restastes: "+intentos);
            ButtGrupoLogin.clearSelection();
          UsuarioIngresado.setText("");
          ContraIngresada.setText("");
          
          if(intentos==0){
              
              bloqueoIntentos(10000);
              
              
          }
          
          
          
          
          
          
          
      }
        
        }
        
      
    }
    
    
    public void bloqueoIntentos(int tiempo){
        
       JOptionPane.showMessageDialog(null, "intentos agotados, intentelo nuevamente un rato");
        SwingUtilities.invokeLater(()->{
        
        UsuarioIngresado.setEnabled(false);
        ContraIngresada.setEnabled(false); 
         IniciarSesion.setEnabled(false);
           TxtEspera.setVisible(true);
           BarraTiempo.setVisible(true);
           BarraTiempo.setMaximum(tiempo/1000);
           
        });
        final int tempF=tiempo/1000;
        Timer tm= new Timer(1000,null);
        final  int[] TiempoRestante={0}; 
            
            
        
        tm.addActionListener((ActionEvent t)->{
            
          TiempoRestante[0]++;  
            
          BarraTiempo.setValue(TiempoRestante[0]);
        
            
          if(TiempoRestante[0]>=tempF){
              tm.stop();
              
              UsuarioIngresado.setEnabled(true);
              ContraIngresada.setEnabled(true);
              IniciarSesion.setEnabled(true);
              TxtEspera.setVisible(false);
           BarraTiempo.setVisible(false);
              intentos=3;
              verificarPaso=false;
           }
          
        });
            
            tm.setInitialDelay(0);
            tm.start();
            
      
        
            
            
            
        }
        
       
        
    }
    
    

