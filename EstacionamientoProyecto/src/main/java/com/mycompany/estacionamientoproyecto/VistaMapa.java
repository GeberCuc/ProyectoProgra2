
package com.mycompany.estacionamientoproyecto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;





  public class VistaMapa extends JFrame {

    private final MapaPanel mapa;

    public VistaMapa() {
        
        setTitle("Mapa del Estacionamiento");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // cierra esta ventana
        setResizable(false);

        // ️ Cargar imagen del mapa
        mapa=new MapaPanel("/Imagenes/Mapa.png");

        // ️ Configuración de coordenadas
        int inicioX=110;
        int y1=71;
        int y2=100;
        int espacios=53;
        int separacion=20;

        // Fila superior
        for (int i=0;i<espacios;i++) {
            int x=inicioX+(i*separacion);
            mapa.agregarPunto(x,y1,"libre");
        }

        // Fila inferior
        for (int i=0;i<espacios;i++) {
            int x=inicioX+(i*separacion);
            mapa.agregarPunto(x,y2,"libre");
        }

        add(mapa);
        pack();
        setSize(1200, 400);
        setLocationRelativeTo(null);
        setVisible(true);

        // 🔄 Actualiza los colores con los datos reales
        actualizarMapaDesdeBD();
    }

    /**
     * Consulta cuántos lugares hay ocupados en espera y libres.
     * Los aplica visualmente sin importar si hay más o menos registros en la BD.
     */
    private void actualizarMapaDesdeBD() {
        String sql = "SELECT Estado, COUNT(*) AS Cantidad FROM Spots GROUP BY Estado";

        int ocupados = 0;
        int espera = 0;
        int libres = 0;

        try (Connection con = basededatos.Conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String estado = rs.getString("Estado").toLowerCase().trim();
                int cantidad = rs.getInt("Cantidad");

                switch (estado) {
                    case "ocupado":
                        ocupados = cantidad;
                        break;
                    case "espera":
                        espera = cantidad;
                        break;
                    case "libre":
                        libres = cantidad;
                        break;
                }
            }

            int totalPuntos = mapa.getCantidadPuntos();

            //  Pinta 
            int index = 0;

            for(;index< ocupados && index < totalPuntos; index++)
                mapa.actualizarEstadoPorIndice(index,"ocupado");

            for(; index<ocupados+espera&&index<totalPuntos;index++)
                mapa.actualizarEstadoPorIndice(index,"espera");

            for (; index<totalPuntos;index++)
                mapa.actualizarEstadoPorIndice(index, "libre");

            mapa.repaint();

            System.out.println("Mapa → Ocupados: " + ocupados + ", Espera: " + espera + ", Libres: " + libres);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,"Error al cargar estados: " + e.getMessage());
        }
    }
}