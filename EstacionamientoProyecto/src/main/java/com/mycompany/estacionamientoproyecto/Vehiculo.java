
package com.mycompany.estacionamientoproyecto;

public class Vehiculo {

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getTipo() {
        return Tipo;
    }

    public void setTipo(String Tipo) {
        this.Tipo = Tipo;
    }

public String getPuesto() {
        return puesto;
    }

    public void setPuesto(String puesto) {
        this.puesto = puesto;
    }

private String placa;
private String Tipo;
private String puesto;
    
/*Clase Vehiculo:
Objeto de transferencia de datos que representa un vehículo dentro del sistema.
Contiene únicamente los atributos necesarios (placa, tipo, puesto) para ser
utilizado en operaciones de registro, búsqueda y asignación de espacios en la base de datos.
*/
    public Vehiculo(String placa, String Tipo, String puesto) {
        this.placa = placa;
        this.Tipo = Tipo;
        this.puesto=puesto;
    }


}
    
    
    
    
