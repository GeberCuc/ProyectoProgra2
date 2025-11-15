package com.mycompany.estacionamientoproyecto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import javax.swing.table.DefaultTableModel;

public class Modificar{

    private DefaultTableModel model;
    private int usuarioID = -1;


    public Modificar(DefaultTableModel model){
        this.model=model;
    }

    
    
    public void cargarDatosUsuarioVehiculos(String nombreBuscado){
        model.setRowCount(0);
        usuarioID=-1;

        try(Connection conectado=basededatos.Conectar()){
            String sql = "SELECT UsuarioID, Nombre,Carnet FROM Usuario WHERE Nombre LIKE ?";
            PreparedStatement pstUser=conectado.prepareStatement(sql);
            pstUser.setString(1,"%"+nombreBuscado +"%");
            ResultSet rsUser=pstUser.executeQuery();

            if(rsUser.next()){
                usuarioID=rsUser.getInt("UsuarioID");
                String nombre=rsUser.getString("Nombre");
                int carnet=rsUser.getInt("Carnet");

                String sql2="SELECT Placa, TipoVehiculo, Area FROM vehiculo WHERE Usuarioid = ?";
                PreparedStatement pstVeh=conectado.prepareStatement(sql2);
                pstVeh.setInt(1,usuarioID);
                ResultSet rsVeh=pstVeh.executeQuery();

                boolean tieneVehiculos=false;
                while(rsVeh.next()){
                    tieneVehiculos=true;
                    String placa=rsVeh.getString("Placa");
                    String tipoVehiculo =rsVeh.getString("TipoVehiculo");
                    String area =rsVeh.getString("Area");

                    model.addRow(new Object[]{nombre,carnet,placa,tipoVehiculo, area});
                }

                if(!tieneVehiculos){
                    model.addRow(new Object[]{nombre,carnet,"","",""});
                }

            }else{
                JOptionPane.showMessageDialog(null,"Usuario no encontrado.");
            }

        } catch(SQLException e){
            JOptionPane.showMessageDialog(null,"Error en la búsqueda: "+e.getMessage());
        }
    }

    public void guardarCambios() {
        if(usuarioID==-1) {
            JOptionPane.showMessageDialog(null,"Primero busca un usuario válido.");
            return;
        }

        try(Connection conectado=basededatos.Conectar()) {
            conectado.setAutoCommit(false);

            String nombre=model.getValueAt(0, 0).toString();
            int carnet;
            try{
                carnet=Integer.parseInt(model.getValueAt(0,1).toString());
            } catch(NumberFormatException e){
                JOptionPane.showMessageDialog(null,"Carnet debe ser un número entero.");
                return;
            }

            String sql="UPDATE Usuario SET Nombre =?, Carnet=? WHERE UsuarioID= ?";
            PreparedStatement pstUser=conectado.prepareStatement(sql);
            pstUser.setString(1,nombre);
            pstUser.setInt(2, carnet);
            pstUser.setInt(3,usuarioID);
            pstUser.executeUpdate();

            String sql2 ="UPDATE vehiculo SET Placa = ?, TipoVehiculo = ?, Area = ? WHERE Placa = ? AND Usuarioid = ?";
            PreparedStatement pstVeh=conectado.prepareStatement(sql2);

            for(int i=0;i<model.getRowCount();i++){
                String placaNueva =model.getValueAt(i,2).toString();
                String tipoVehiculo= model.getValueAt(i,3).toString().toLowerCase();
                String area =model.getValueAt(i, 4).toString().toUpperCase();

                if (!(tipoVehiculo.equals("automovil")||tipoVehiculo.equals("moto"))){
                    JOptionPane.showMessageDialog(null,"Fila "+(i+1)+":TipoVehiculo debe ser 'automovil' o 'moto'.");
                    conectado.rollback();
                    return;
                }
                if(!(area.equals("ESTUDIANTE")||area.equals("CATEDRATICO"))){
                    JOptionPane.showMessageDialog(null,"Fila "+(i+1)+": Area debe ser 'ESTUDIANTE' o 'CATEDRATICO'.");
                    conectado.rollback();
                    return;
                }

                pstVeh.setString(1,placaNueva);
                pstVeh.setString(2,tipoVehiculo);
                pstVeh.setString(3,area);
                pstVeh.setString(4,placaNueva);
                pstVeh.setInt(5,usuarioID);

                int updated=pstVeh.executeUpdate();

                if (updated==0){
                    String sql3="INSERT INTO vehiculo (Placa,TipoVehiculo,Area,Usuarioid) VALUES (?,?,?,?)";
                    try(PreparedStatement pstInsert=conectado.prepareStatement(sql3)){
                        pstInsert.setString(1, placaNueva);
                        pstInsert.setString(2, tipoVehiculo);
                        pstInsert.setString(3, area);
                        pstInsert.setInt(4, usuarioID);
                        pstInsert.executeUpdate();
                    }
                }
            }

            conectado.commit();
            JOptionPane.showMessageDialog(null,"Datos actualizados correctamente.");

        } catch(SQLException e){
            JOptionPane.showMessageDialog(null,"Error al actualizar base de datos: "+e.getMessage());
            
        }
    }
}
   

