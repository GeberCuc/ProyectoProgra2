# Proyecto: Sistema de Estacionamiento

**Autor:** [Geber Abdiel Cuc Ixquiactap]  
**Asignatura:** Programación Orientada a Objetos  
**Fecha:** (11/15/2025)

---

# README

### 1. Resumen
Aplicación de escritorio desarrollada en **Java (Swing + JDBC + SQLite)** para gestionar un estacionamiento universitario. Permite registrar usuarios y vehículos, asignar espacios, generar y cerrar tickets, visualizar un mapa de ocupación y manejar importación/exportación de datos en formato CSV.

### 2. Objetivos
- Aplicar principios de POO, JDBC y diseño de interfaces Swing.
- Mantener persistencia de datos mediante SQLite.
- Soportar importación y exportación masiva de datos vía CSV.
- Registrar y reportar las ganancias diarias del estacionamiento.

### 3. Tecnologías
- Java SE 8 o superior
- Swing para interfaz gráfica
- SQLite para base de datos (archivo `.db`)
- JDBC para conexión con SQLite
- Recomendado usar IDEs como NetBeans o IntelliJ IDEA

### 4. Estructura de carpetas
EstacionamientoProyecto/
├─ src/
│ └─ com/mycompany/estacionamientoproyecto/
│ ├─ basededatos.java
│ ├─ EstacionamientoProyecto.java
│ ├─ exportar.java
│ ├─ LeerYGuardarCSV.java
│ ├─ MapaPanel.java
│ ├─ Transacciones.java
│ ├─ Usuario.java
│ ├─ UsuarioDB.java
│ ├─ Vehiculo.java
│ ├─ VerificarContraseña.java
│ ├─ VistaMapa.java
│ └─ visualizarContenido.java
├─ Data/
│ └─ BaseDeDatosEstacionamientoProyecto.db
├─ Imagenes/
└─ README.md

### 5. Requisitos
- JDK 8 o superior instalado en el sistema.
- Driver JDBC para SQLite (normalmente incluido en el proyecto).
- IDE recomendado: NetBeans o IntelliJ IDEA (aunque se puede compilar por línea de comandos).

### 6. Cómo ejecutar
1. Abrir el proyecto en el IDE (NetBeans/IntelliJ).
2. Confirmar que el archivo `Data/BaseDeDatosEstacionamientoProyecto.db` se encuentra en la ruta correcta.
3. Ejecutar la clase principal `visualizarContenido` o `EstacionamientoProyecto` (según configuración).
4. Usuario y contraseña por defecto para login:  
   - Empleado:
   - Usuario: `geber12`  
   - Contraseña: `gebr2|`
   - Admin:
   - Usuario: `ADMIN12`  
   - Contraseña: `geber2|`  
   *(Ver clase `VerificarContraseña` para detalles)*

### 7. Notas adicionales
- Se usan transacciones para asegurar la integridad en operaciones como asignar spots y generar tickets.
- La importación de CSV se realiza con la clase `LeerYGuardarCSV`, que transforma y valida datos antes de insertarlos.
- La exportación de tickets a CSV usa el método `exportar.ticket(direccion)`.

---

**Fin del documento**