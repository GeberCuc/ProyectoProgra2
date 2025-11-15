
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
    
    /*
Clase VerificarContraseña:
Encargada del manejo del inicio de sesión en el sistema. 
Verifica el usuario y contraseña ingresados, controla los intentos permitidos 
y gestiona la interfaz gráfica según el resultado de la verificación.

Atributos principales:
 usuario y contraseña: datos ingresados por el usuario.
 intentos: contador de intentos fallidos.
 contra y us: credenciales válidas predefinidas.
 Componentes gráficos (paneles, botones, etiquetas, etc.) utilizados para
  mostrar mensajes, habilitar o deshabilitar campos y controlar el flujo visual.
*/
    
    private String usuario;
    private char[] contraseña;
    protected static int intentos=3; 
    
    
   protected final char[] contra={'g','e','b','r','2','|'};
   protected final String us="geber12";
   
   
   
   protected final char[] contraAdmin={'g','e','b','e','r','2','|'};
   protected final String usAdmin="ADMIN12";
   
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
   private final JTabbedPane pestañas;
   private final JRadioButton empleado;
   private final JRadioButton admin;
   
   public VerificarContraseña(String usuario, char[]contraseña, JPanel PanelIngresarDatos,JPanel PanelLogin,JTextField UsuarioIngresado,JPasswordField ContraIngresada,
           JButton IniciarSesion,JLabel TxtEspera,JProgressBar BarraTiempo,ButtonGroup ButtGrupoLogin,JPanel JpanelAñadirUsuario,JTabbedPane pestañas
             ,JRadioButton empleado,JRadioButton admin){
       
       
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
         this.pestañas=pestañas;
         this.empleado=empleado;
         this.admin=admin;
         
   }
   
    
    /*
Método verificar():
Compara las credenciales ingresadas con las almacenadas.
Si el usuario y la contraseña coinciden, se muestra un mensaje de éxito 
 y se activan los paneles del sistema principal.
 Si son incorrectas, se resta un intento y se limpian los campos.
 Si se agotan los intentos, se bloquea temporalmente el inicio de sesión.
*/
    public  void verificar(){

  boolean pasasAdmin=false;
   boolean pasas=false;

            
           
           
   if(empleado.isSelected()){
                 
         pasas=Arrays.equals(contraseña, contra)&&usuario.equals(us);
   }else if(admin.isSelected()){
       
             pasasAdmin=Arrays.equals(contraseña, contraAdmin)&&usuario.equals(usAdmin);
           
   }
        
    
        
        if (ButtGrupoLogin.isSelected(null)){
        JOptionPane.showMessageDialog(null,"Seleccione una opcion entre Empleado o Administrador","ERROR",JOptionPane.INFORMATION_MESSAGE);

    }else{
        if (pasasAdmin){
        
            
            
            JOptionPane.showMessageDialog(null, "Bienvenido Administrador", "INICIO DE SESION EXITOSA", JOptionPane.INFORMATION_MESSAGE);
            Arrays.fill(contraseña,'°');
            PanelLogin.setVisible(false);
            
            PanelIngresarDatos.setVisible(true);
            
            JpanelAñadirUsuario.setVisible(false); 
            pestañas.setVisible(true);
            pestañas.setEnabledAt(4, true);

        }else if(pasas){
           
            
            
            JOptionPane.showMessageDialog(null,"Bienvenido Empleado","INICIO DE SESION EXITOSA",JOptionPane.INFORMATION_MESSAGE);
            Arrays.fill(contraseña,'°');
            PanelLogin.setVisible(false);
            
            PanelIngresarDatos.setVisible(true);
            JpanelAñadirUsuario.setVisible(false);
            
            pestañas.setVisible(true);
            pestañas.setEnabledAt(4, false);
        }else{
            intentos--;
            Arrays.fill(contraseña,'°');
            JOptionPane.showMessageDialog(null,"Contraseña o Usuario incorrecto,intentos restantes: " + intentos);
            ButtGrupoLogin.clearSelection();
            
            UsuarioIngresado.setText("");
            
            ContraIngresada.setText("");

            if (intentos == 0) {
                bloqueoIntentos(10000);
            }
        }
    }
}
   
    
   
    /*
Método bloqueoIntentos(int tiempo):
Bloquea temporalmente el acceso al sistema cuando se superan los intentos fallidos.
Deshabilita los campos de entrada, muestra una barra de progreso con el tiempo
de espera restante y reestablece los intentos una vez finalizado el bloqueo.
*/
    public void bloqueoIntentos(int tiempo){
        
       JOptionPane.showMessageDialog(null,"intentos agotados, intentelo nuevamente un rato");
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
    
    

