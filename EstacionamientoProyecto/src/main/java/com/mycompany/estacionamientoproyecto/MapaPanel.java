
package com.mycompany.estacionamientoproyecto;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JPanel;



/**
 * Panel que muestra la imagen del estacionamiento
 * y dibuja puntos de colores sobre ella según el estado de cada espacio.
 */
public class MapaPanel extends JPanel {

    private Image mapa;                       // Imagen del plano base
    private final List<PuntoOcupacion> puntos; // Lista de puntos visibles en el mapa

    // Constructor: carga la imagen y prepara el panel
    public MapaPanel(String rutaImagen){
        this.mapa = new ImageIcon(getClass().getResource(rutaImagen)).getImage();
        this.puntos = new ArrayList<>();
        setPreferredSize(new Dimension(mapa.getWidth(null), mapa.getHeight(null)));
    }

    // Agrega un punto al mapa en una posición específica
    public void agregarPunto(int x,int y, String estado) {
        puntos.add(new PuntoOcupacion(x, y, estado.toLowerCase().trim()));
        repaint();
    }

    // Actualiza el estado de un punto existente (según su índice)
    public void actualizarEstadoPorIndice(int index, String nuevoEstado) {
        if (index >=0 && index <puntos.size()){
            puntos.get(index).estado =nuevoEstado.toLowerCase().trim();
        }
        repaint(); // Redibuja el mapa
    }

    // Devuelve el número total de puntos dibujados
    public int getCantidadPuntos(){
        return puntos.size();
    }

    // Dibuja la imagen y los puntos sobre el mapa
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(mapa, 0, 0, this);

        for (PuntoOcupacion p : puntos) {
            switch (p.estado) {
                case "ocupado":
                    g.setColor(Color.RED);
                    break;
                case "espera":
                    g.setColor(Color.YELLOW);
                    break;
                default:
                    g.setColor(Color.GREEN);
                    break;
            }

            g.fillOval(p.x - 5, p.y - 5, 10, 10);
        }
    }

    // ?Clase representa un punto de estacionamiento
    private static class PuntoOcupacion {
        int x, y;
        String estado;

        public PuntoOcupacion(int x, int y, String estado) {
            this.x = x;
            this.y = y;
            this.estado = estado;
        }
    }
}