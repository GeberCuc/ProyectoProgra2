## Manual de Usuario


### 1. Inicio y Login
1. Ejecuta la aplicación.
2. En la ventana de login introduce:
   - Usuario: `geber12`
   - Contraseña: `gebr2|`
3. Si fallan los intentos, el sistema bloquea temporalmente y muestra barra de progreso.
![Ventana de Login](imagenes/login.png)
*Figura 1: Pantalla de inicio de sesión del sistema*




### 2. Panel principal (visualizarContenido)
- Paneles principales:
  - `PanelLogin` — pantalla de ingreso.
  - `PanelFunciones` — menú principal con pestañas.
  - `Mapa` — abre `VistaMapa` con el `MapaPanel`.
![Panel Principal](imagenes/Principal.png)
*Figura 2: Vista general del panel principal con pestañas*



### 3. Registrar usuario y vehículo
1. Pulsa “Agregar nuevo usuario”.
2. Rellena Nombre, Carnet, Placa.
3. Selecciona tipo (automóvil / moto) y puesto (ESTUDIANTE / CATEDRATICO).
4. Guardar → se llamará a `UsuarioDB.AgregarDb(Usuario, Vehiculo)` y la UI mostrará mensaje de confirmación.
![Panel Registrar](imagenes/Registro.png)
*Figura 3: Formulario para registrar nuevo usuario y vehículo*

![Panel Registrar](imagenes/RegistroExitoso.png)
*Figura 4: Mensaje de confirmación exitosa*

### 4. Buscar por placa
- Ingresa la placa en el campo y pulsa "Buscar".
- Si existe, se cargan los datos (tipo, puesto).
- Si no existe, el sistema te ofrece crear un nuevo usuario.
![Panel buscar](imagenes/Buscar.png)
*Figura 5:  Campo de búsqueda y resultados*




### 5. Generar ticket (ingreso)
1. Escribe la placa.
2. Selecciona tipo/puesto y modo (plano = tarifa fija; variable = pago por tiempo).
3. Si `plano`, ingresa billete y confirma — el sistema cobra la tarifa fija.
4. Si `variable`, ingresa el tiempo estimado.
5. Pulsa “Generar” → `UsuarioDB.AsignarSpot(...)` realizará:
   - búsqueda de spot libre,
   - marcado del spot como `ocupado`,
   - decremento de capacidad en `Areas`,
   - inserción del `Ticket` (toda la operación en transacción).

![Ticket](imagenes/Ticket.png)
*Figura 6: Formulario para generar nuevo ticket*

   

### 6. Registrar salida
1. En la funcionalidad "Registrar salida" introduce el `TicketID`.
2. El sistema calcula minutos, determina monto (si `variable`) y libera el spot.
3. Si modo es `plano`, verifica el tiempo y decide modo espera/expiración según reglas del código.

![Salida](imagenes/Salida.png)
*Figura 7: Pantalla para registrar salida de vehículo*



### 7. Exportar / Importar CSV
- **Exportar**: utiliza el diálogo para elegir carpeta; crea un archivo `tickets_TIMESTAMP.csv`.
- **Importar**: usa el selector de archivo; `LeerYGuardarCSV` lee y llama a los métodos de inserción según tipo de archivo (áreas, spots, vehículos, tickets).

![Exportar](imagenes/Exportar.png)
*Figura 7: Pantalla de inicio de sesión del sistema*



### 8. Mapa
- Abre `VistaMapa` (botón `Mapa`).
- `MapaPanel` dibuja los `PuntoOcupacion` con estado (`libre` / `ocupado` / `espera`).
- Visual: colores o iconos (según tus imágenes).

![Exportar](imagenes/Estacionamiento.png)
*Figura 8: Vista general del mapa de estacionamiento*


## Fin