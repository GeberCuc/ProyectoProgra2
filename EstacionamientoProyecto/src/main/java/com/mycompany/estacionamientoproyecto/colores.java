package com.mycompany.estacionamientoproyecto;

import java.awt.*;
import javax.swing.*;

public class colores{

    private static final Color FONDO_PRINCIPAL= new Color(30, 30, 47);
    private static final Color PANEL_SECUNDARIO= new Color(45, 45, 68);
    private static final Color PANEL_TERCIARIO= new Color(78, 78, 106);
    private static final Color BOTONES_COLOR= new Color(106, 106, 142);
    private static final Color TEXTO_SUAVE =new Color(200, 200, 230);
    private static final Color TEXTO_TITULO =Color.WHITE;

    public static void aplicarTema(JFrame frame) {
        frame.getContentPane().setBackground(FONDO_PRINCIPAL);
        aplicarTemaAComponentes(frame.getContentPane());
    }

    private static void aplicarTemaAComponentes(Container container) {

        for (Component comp : container.getComponents()) {

            //  PANEL
            if (comp instanceof JPanel panel) {
                panel.setBackground(PANEL_SECUNDARIO);
                aplicarTemaAComponentes(panel);
            }

            // J TABBED PANE ne
            else if (comp instanceof JTabbedPane tabs) {

                tabs.setBackground(PANEL_SECUNDARIO);
                tabs.setForeground(TEXTO_SUAVE);

                // Fondo de las pestañas
                UIManager.put("TabbedPane.selected", PANEL_SECUNDARIO);
                UIManager.put("TabbedPane.contentAreaColor", PANEL_SECUNDARIO);

                int total = tabs.getTabCount();

                for (int i = 0; i < total; i++) {
                    Component tabContent = tabs.getComponentAt(i);
                    if (tabContent instanceof Container subCont) {
                        subCont.setBackground(PANEL_SECUNDARIO);
                        aplicarTemaAComponentes(subCont);
                    }
                }
            }

            // BOTONES
            else if (comp instanceof JButton btn) {
                btn.setBackground(BOTONES_COLOR);
                btn.setForeground(TEXTO_TITULO);
                btn.setFocusPainted(false);
                btn.setBorderPainted(false);
            }

            //  LABEL 
            else if (comp instanceof JLabel lbl) {
                lbl.setForeground(TEXTO_SUAVE);
            }

            //  TEXTFIELD 
            else if (comp instanceof JTextField txt) {
                txt.setBackground(PANEL_TERCIARIO);
                txt.setForeground(TEXTO_SUAVE);
                txt.setCaretColor(TEXTO_SUAVE);
                txt.setBorder(BorderFactory.createLineBorder(PANEL_TERCIARIO));
            }

            //  PASSWORD
            else if (comp instanceof JPasswordField pwd) {
                pwd.setBackground(PANEL_TERCIARIO);
                pwd.setForeground(TEXTO_SUAVE);
                pwd.setCaretColor(TEXTO_SUAVE);
                pwd.setBorder(BorderFactory.createLineBorder(PANEL_TERCIARIO));
            }

            // TABLAS 
            else if (comp instanceof JTable tabla) {
                tabla.setBackground(PANEL_TERCIARIO);
                tabla.setForeground(TEXTO_SUAVE);
                tabla.setGridColor(PANEL_TERCIARIO);

                tabla.getTableHeader().setBackground(PANEL_SECUNDARIO);
                tabla.getTableHeader().setForeground(TEXTO_TITULO);
            }

            //  JScrollPane 
            else if (comp instanceof JScrollPane scroll) {
                scroll.getViewport().setBackground(PANEL_TERCIARIO);
                aplicarTemaAComponentes(scroll.getViewport());
            }

            //  RADIOBUTTON 
            else if (comp instanceof JRadioButton rb) {
                rb.setBackground(PANEL_SECUNDARIO);
                rb.setForeground(TEXTO_SUAVE);
            }

            //  COMBOBOX 
            else if (comp instanceof JComboBox combo) {
                combo.setBackground(PANEL_TERCIARIO);
                combo.setForeground(TEXTO_SUAVE);
            }
        }
    }
}
