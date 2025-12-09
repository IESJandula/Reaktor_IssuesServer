# 📌 Módulo de gestión de incidencias.
Repositorio del proyecto para el módulo de gestión de incidencias.
Este servicio maneja las solicitudes HTTP relacionadas con la creación, actualización, consulta y eliminación de incidencias en una base de datos de motor MySQL Server. Además, gestiona categorías, ubicaciones y responsables de categorías.

## 📋 Tabla de Endpoints

### Incidencias
<table>
   <tr>
      <th>METODO</th>
      <th>URL</th>
      <th>DESCRIPCION</th>
      <th>ROL REQUERIDO</th>
   </tr>
   <tr>
      <td>🟢 GET</td>
      <td>/issues/incidencias/</td>
      <td>Lista incidencias ordenadas por fecha con paginación.</td>
      <td>PROFESOR</td>
   </tr>
   <tr>
      <td>🟢 GET</td>
      <td>/issues/incidencias/estados/</td>
      <td>Obtiene la lista de estados disponibles para las incidencias.</td>
      <td>ADMINISTRADOR</td>
   </tr>
   <tr>
      <td>🟡 POST</td>
      <td>/issues/incidencias/</td>
      <td>Crea una nueva incidencia en el sistema.</td>
      <td>PROFESOR</td>
   </tr>
   <tr>
      <td>🔵 PUT</td>
      <td>/issues/incidencias/</td>
      <td>Actualiza una incidencia existente (estado, solución, responsable).</td>
      <td>ADMINISTRADOR</td>
   </tr>
   <tr>
      <td>🔴 DELETE</td>
      <td>/issues/incidencias/</td>
      <td>Elimina una incidencia específica de la base de datos.</td>
      <td>PROFESOR, ADMINISTRADOR</td>
   </tr>
</table>

### Categorías
<table>
   <tr>
      <th>METODO</th>
      <th>URL</th>
      <th>DESCRIPCION</th>
      <th>ROL REQUERIDO</th>
   </tr>
   <tr>
      <td>🟢 GET</td>
      <td>/issues/categorias/</td>
      <td>Lista todas las categorías de incidencias.</td>
      <td>ADMINISTRADOR</td>
   </tr>
   <tr>
      <td>🟡 POST</td>
      <td>/issues/categorias/</td>
      <td>Crea una nueva categoría de incidencias.</td>
      <td>ADMINISTRADOR</td>
   </tr>
   <tr>
      <td>🔴 DELETE</td>
      <td>/issues/categorias/</td>
      <td>Elimina una categoría (solo si no tiene incidencias asociadas).</td>
      <td>ADMINISTRADOR</td>
   </tr>
</table>

### Ubicaciones
<table>
   <tr>
      <th>METODO</th>
      <th>URL</th>
      <th>DESCRIPCION</th>
      <th>ROL REQUERIDO</th>
   </tr>
   <tr>
      <td>🟢 GET</td>
      <td>/issues/ubicaciones/</td>
      <td>Lista todas las ubicaciones disponibles.</td>
      <td>PROFESOR</td>
   </tr>
   <tr>
      <td>🟡 POST</td>
      <td>/issues/ubicaciones/</td>
      <td>Crea una nueva ubicación.</td>
      <td>ADMINISTRADOR</td>
   </tr>
   <tr>
      <td>🔴 DELETE</td>
      <td>/issues/ubicaciones/</td>
      <td>Elimina una ubicación específica.</td>
      <td>ADMINISTRADOR</td>
   </tr>
</table>

### Usuarios-Categoría (Responsables)
<table>
   <tr>
      <th>METODO</th>
      <th>URL</th>
      <th>DESCRIPCION</th>
      <th>ROL REQUERIDO</th>
   </tr>
   <tr>
      <td>🟢 GET</td>
      <td>/issues/usuarios_categoria/</td>
      <td>Lista todos los responsables de categorías.</td>
      <td>ADMINISTRADOR</td>
   </tr>
   <tr>
      <td>🟡 POST</td>
      <td>/issues/usuarios_categoria/</td>
      <td>Asigna un responsable a una categoría.</td>
      <td>ADMINISTRADOR</td>
   </tr>
   <tr>
      <td>🔴 DELETE</td>
      <td>/issues/usuarios_categoria/</td>
      <td>Elimina un responsable de una categoría.</td>
      <td>ADMINISTRADOR</td>
   </tr>
</table>

## 🔹 Requisitos de ejecución.
El servicio necesita una base de datos **MySQL** escuchando en el puerto **3306**. El esquema se crea automáticamente con el nombre "**reaktor_issues_server**" si no existe. En el archivo de configuración del proyecto, `application.yaml`, se definen el nombre del esquema y las credenciales de acceso a la base de datos.

**Configuración actual:**
- **Puerto del servidor:** 8085
- **Base de datos:** reaktor_issues_server
- **Puerto MySQL:** 3306
- **Usuario:** root (configurable en `application.yaml`)
- **Contraseña:** toor (configurable en `application.yaml`)

**Para crear un contenedor de forma rápida y sencilla que proporcione este servicio, utiliza el siguiente comando:**
```docker
docker run -d -p 3306:3306 --name mi_mysql -e MYSQL_ROOT_PASSWORD=toor -e MYSQL_DATABASE=reaktor_issues_server mysql
```

**Nota:** El servicio utiliza autenticación basada en roles (PROFESOR y ADMINISTRADOR) mediante JWT. Asegúrate de configurar correctamente las claves públicas en el archivo de configuración.

<br/>
<br/>

# 📌 Endpoints expuestos.
A continuación el listado de endpoints expuestos actualmente y los parámetros necesarios con una descripción de su comportamiento.

**Nota importante:** Todos los endpoints requieren autenticación mediante JWT. El token debe incluirse en el header `Authorization` con el formato `Bearer <token>`.

---

## 🔹 Endpoints de Incidencias

### 🟢 GET - Listar incidencias ordenadas por fecha
```
GET localhost:8085/issues/incidencias/
```
Endpoint que permite recuperar una **lista paginada** de incidencias ordenadas por fecha de creación (más recientes primero).

**Rol requerido:** PROFESOR

**Parámetros de consulta (Query Parameters):**
- `page`: Número de página (por defecto: 0)
- `size`: Tamaño de la página (por defecto: 20)
- `sort`: Campo por el que ordenar (opcional)

**Ejemplo:**
```
GET localhost:8085/issues/incidencias/?page=0&size=10&sort=fecha,desc
```

**Respuesta:** Lista paginada de objetos `IncidenciaDto` con la siguiente estructura:
```json
{
  "content": [
    {
      "id": 1,
      "ubicacion": "Aula 101",
      "email": "profesor@ejemplo.com",
      "fecha": "2024-01-15T10:30:00",
      "problema": "Descripción del problema",
      "estado": "PENDIENTE",
      "solucion": null,
      "emailResponsable": "responsable@ejemplo.com",
      "categoria": "Hardware"
    }
  ],
  "totalElements": 50,
  "totalPages": 5,
  "size": 10,
  "number": 0
}
```

---

### 🟢 GET - Listar estados de incidencias
```
GET localhost:8085/issues/incidencias/estados/
```
Endpoint que devuelve la lista de estados disponibles para las incidencias.

**Rol requerido:** ADMINISTRADOR

**Respuesta:**
```json
[
  "PENDIENTE",
  "EN PROGRESO",
  "RESUELTA",
  "CANCELADA",
  "DUPLICADA"
]
```

---

### 🟡 POST - Crear nueva incidencia
```
POST localhost:8085/issues/incidencias/
```
Endpoint que permite registrar nuevas incidencias en el sistema. El usuario se obtiene automáticamente del token JWT.

**Rol requerido:** PROFESOR

**Headers requeridos:**
- `Authorization`: Bearer token JWT
- `nombreUbicacion`: Nombre de la ubicación donde ocurre la incidencia
- `problema`: Descripción del problema
- `nombreCategoria`: Nombre de la categoría de la incidencia

**Ejemplo de petición:**
```
POST localhost:8085/issues/incidencias/
Headers:
  Authorization: Bearer <token>
  nombreUbicacion: Aula 101
  problema: El proyector no funciona
  nombreCategoria: Hardware
```

**Respuesta:** 200 OK si la incidencia se crea correctamente.

**Nota:** La incidencia se crea automáticamente con:
- Estado: `PENDIENTE`
- Fecha: Fecha y hora actual
- Email: Obtenido del token JWT
- EmailResponsable: Primer responsable asignado a la categoría

---

### 🔵 PUT - Actualizar incidencia
```
PUT localhost:8085/issues/incidencias/
```
Endpoint que permite actualizar el estado, solución y responsable de una incidencia existente.

**Rol requerido:** ADMINISTRADOR

**Headers requeridos:**
- `Authorization`: Bearer token JWT
- `id`: ID de la incidencia a actualizar
- `estado`: Nuevo estado de la incidencia (PENDIENTE, EN PROGRESO, RESUELTA, CANCELADA, DUPLICADA)
- `solucion`: Solución o comentario sobre la resolución
- `emailResponsable`: Email del responsable asignado

**Ejemplo de petición:**
```
PUT localhost:8085/issues/incidencias/
Headers:
  Authorization: Bearer <token>
  id: 1
  estado: RESUELTA
  solucion: Se ha reemplazado la lámpara del proyector
  emailResponsable: tecnico@ejemplo.com
```

**Respuesta:** 200 OK con mensaje "Incidencia modificada con éxito"

---

### 🔴 DELETE - Eliminar incidencia
```
DELETE localhost:8085/issues/incidencias/
```
Endpoint que permite eliminar una incidencia específica. Los profesores solo pueden eliminar sus propias incidencias, mientras que los administradores pueden eliminar cualquier incidencia.

**Rol requerido:** PROFESOR o ADMINISTRADOR

**Headers requeridos:**
- `Authorization`: Bearer token JWT
- `id`: ID de la incidencia a eliminar

**Ejemplo de petición:**
```
DELETE localhost:8085/issues/incidencias/
Headers:
  Authorization: Bearer <token>
  id: 1
```

**Respuesta:** 200 OK si la incidencia se elimina correctamente.

---

## 🔹 Endpoints de Categorías

### 🟢 GET - Listar categorías
```
GET localhost:8085/issues/categorias/
```
Endpoint que devuelve la lista de todas las categorías de incidencias.

**Rol requerido:** ADMINISTRADOR

**Respuesta:** Lista de objetos `CategoriaDto`:
```json
[
  {
    "nombre": "Hardware"
  },
  {
    "nombre": "Software"
  }
]
```

---

### 🟡 POST - Crear categoría
```
POST localhost:8085/issues/categorias/
```
Endpoint que permite crear una nueva categoría de incidencias.

**Rol requerido:** ADMINISTRADOR

**Headers requeridos:**
- `Authorization`: Bearer token JWT
- `nombre`: Nombre de la categoría a crear

**Ejemplo de petición:**
```
POST localhost:8085/issues/categorias/
Headers:
  Authorization: Bearer <token>
  nombre: Redes
```

**Respuesta:** 200 OK si la categoría se crea correctamente.

---

### 🔴 DELETE - Eliminar categoría
```
DELETE localhost:8085/issues/categorias/
```
Endpoint que permite eliminar una categoría. Solo se puede eliminar si no tiene incidencias asociadas.

**Rol requerido:** ADMINISTRADOR

**Headers requeridos:**
- `Authorization`: Bearer token JWT
- `nombre`: Nombre de la categoría a eliminar

**Ejemplo de petición:**
```
DELETE localhost:8085/issues/categorias/
Headers:
  Authorization: Bearer <token>
  nombre: Redes
```

**Respuesta:** 204 No Content si la categoría se elimina correctamente.

**Error:** 400 Bad Request si la categoría tiene incidencias asociadas.

---

## 🔹 Endpoints de Ubicaciones

### 🟢 GET - Listar ubicaciones
```
GET localhost:8085/issues/ubicaciones/
```
Endpoint que devuelve la lista de todas las ubicaciones disponibles.

**Rol requerido:** PROFESOR

**Respuesta:** Lista de objetos `UbicacionDto`:
```json
[
  {
    "nombre": "Aula 101"
  },
  {
    "nombre": "Aula 102"
  }
]
```

---

### 🟡 POST - Crear ubicación
```
POST localhost:8085/issues/ubicaciones/
```
Endpoint que permite crear una nueva ubicación.

**Rol requerido:** ADMINISTRADOR

**Headers requeridos:**
- `Authorization`: Bearer token JWT
- `nombre`: Nombre de la ubicación a crear

**Ejemplo de petición:**
```
POST localhost:8085/issues/ubicaciones/
Headers:
  Authorization: Bearer <token>
  nombre: Aula 203
```

**Respuesta:** 200 OK si la ubicación se crea correctamente.

**Error:** 400 Bad Request si la ubicación ya existe.

---

### 🔴 DELETE - Eliminar ubicación
```
DELETE localhost:8085/issues/ubicaciones/
```
Endpoint que permite eliminar una ubicación específica.

**Rol requerido:** ADMINISTRADOR

**Headers requeridos:**
- `Authorization`: Bearer token JWT
- `nombre`: Nombre de la ubicación a eliminar

**Ejemplo de petición:**
```
DELETE localhost:8085/issues/ubicaciones/
Headers:
  Authorization: Bearer <token>
  nombre: Aula 203
```

**Respuesta:** 200 OK si la ubicación se elimina correctamente.

---

## 🔹 Endpoints de Usuarios-Categoría (Responsables)

### 🟢 GET - Listar responsables
```
GET localhost:8085/issues/usuarios_categoria/
```
Endpoint que devuelve la lista de todos los responsables asignados a categorías.

**Rol requerido:** ADMINISTRADOR

**Respuesta:** Lista de objetos `UsuarioCategoriaDto`:
```json
[
  {
    "nombreCategoria": "Hardware",
    "nombreResponsable": "Juan Pérez",
    "emailResponsable": "juan.perez@ejemplo.com"
  }
]
```

---

### 🟡 POST - Asignar responsable a categoría
```
POST localhost:8085/issues/usuarios_categoria/
```
Endpoint que permite asignar un responsable a una categoría.

**Rol requerido:** ADMINISTRADOR

**Headers requeridos:**
- `Authorization`: Bearer token JWT
- `nombreCategoria`: Nombre de la categoría
- `nombreResponsable`: Nombre del responsable
- `emailResponsable`: Email del responsable

**Ejemplo de petición:**
```
POST localhost:8085/issues/usuarios_categoria/
Headers:
  Authorization: Bearer <token>
  nombreCategoria: Hardware
  nombreResponsable: Juan Pérez
  emailResponsable: juan.perez@ejemplo.com
```

**Respuesta:** 200 OK si el responsable se asigna correctamente.

---

### 🔴 DELETE - Eliminar responsable de categoría
```
DELETE localhost:8085/issues/usuarios_categoria/
```
Endpoint que permite eliminar un responsable de una categoría.

**Rol requerido:** ADMINISTRADOR

**Headers requeridos:**
- `Authorization`: Bearer token JWT
- `nombreCategoria`: Nombre de la categoría
- `nombreResponsable`: Nombre del responsable
- `emailResponsable`: Email del responsable

**Ejemplo de petición:**
```
DELETE localhost:8085/issues/usuarios_categoria/
Headers:
  Authorization: Bearer <token>
  nombreCategoria: Hardware
  nombreResponsable: Juan Pérez
  emailResponsable: juan.perez@ejemplo.com
```

**Respuesta:** 200 OK si el responsable se elimina correctamente.

---


