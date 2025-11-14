# Proyecto: Sistema de Estacionamiento

**Autor:** [Geber Abdiel Cuc Ixquiactap]  
**Asignatura:** Programación Orientada a Objetos  
**Fecha:** (11/15/2025)

---

## README

### 1. Resumen
Este proyecto es una aplicación de escritorio en **Java (Swing + JDBC + SQLite)** que simula la gestión de un estacionamiento universitario. Permite registrar usuarios y vehículos, asignar spots, generar y cerrar tickets, visualizar un mapa de ocupación y exportar/importar datos en CSV.

### 2. Objetivos
- Aplicar POO, JDBC y manejo de interfaces Swing.
- Mantener persistencia con SQLite.
- Proporcionar funciones de importación y exportación para datos masivos.
- Guardar y reportar ganancias diarias.

### 3. Tecnologías
- Java SE (versión compatible 8+)
- Swing (Interfaz gráfica)
- SQLite (archivo `.db`)
- JDBC (driver sqlite)
- Herramientas recomendadas: NetBeans o IntelliJ IDEA

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
- JDK 8+ instalado.
- SQLite JDBC (normalmente ya incluido en proyecto si está en librerías).
- NetBeans/IntelliJ para abrir el proyecto (o compilar con `javac`).

### 6. Cómo ejecutar
1. Abrir el proyecto en NetBeans/IntelliJ.
2. Asegurarse de que `Data/BaseDeDatosEstacionamientoProyecto.db` esté en la ruta esperada.
3. Ejecutar la clase principal `visualizarContenido` (o `EstacionamientoProyecto` si configuras `main` ahí).
4. Usuario por defecto en el código: `geber12` / contraseña: `gebr2|` (ver `VerificarContraseña`).

### 7. Notas rápidas
- El sistema usa transacciones en operaciones críticas (asignación de spot, creación de ticket).
- Importar CSV: usa `LeerYGuardarCSV` que transforma filas y llama a `UsuarioDB` o métodos de inserción.
- Exportar tickets: usa `exportar.ticket(direccion)` para escribir CSV.

## Fin