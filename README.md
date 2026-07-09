# Perfulandia - Plataforma de Comercio Electrónico (Microservicios)

¡Bienvenido al repositorio oficial de **Perfulandia**! Este proyecto es el resultado de la Evaluación Parcial 3 de la asignatura Desarrollo FullStack 1, donde se ha implementado una arquitectura robusta basada en microservicios utilizando Spring Boot.

## 🏗️ Arquitectura y Tecnologías

El sistema está diseñado bajo el paradigma de microservicios, asegurando alta disponibilidad, escalabilidad y separación de responsabilidades. Todos los microservicios siguen el patrón arquitectónico **CSR (Controller-Service-Repository)**, garantizando un código limpio, mantenible y testeable.

### Stack Tecnológico:
- **Backend Framework:** Java 17 + Spring Boot 3.x
- **Arquitectura:** Microservicios + Spring Cloud API Gateway
- **Base de Datos:** MySQL 8.0 (con persistencia a través de Spring Data JPA)
- **Documentación de API:** OpenAPI 3.0 / Swagger UI
- **Testing:** JUnit 5 + Mockito (Cobertura > 80% validada con JaCoCo)
- **Infraestructura:** Docker y Docker Compose para orquestación de contenedores
- **Comunicación Interna:** OpenFeign Client

---

## 🧩 Ecosistema de Microservicios

El proyecto cuenta con **10 microservicios** de negocio más un **API Gateway** unificador:

| Microservicio | Puerto | Descripción |
| --- | --- | --- |
| `microservicio-api-gateway` | `8080` | Puerta de enlace unificada y enrutador hacia los demás servicios. |
| `microservicio-catalogo` | `8081` | Gestión de perfumes, descripciones y precios. |
| `microservicio-usuario` | `8082` | Gestión de cuentas de usuario, autenticación básica y roles. |
| `microservicio-sucursal` | `8083` | Administración de tiendas físicas y su información de contacto. |
| `microservicio-proveedor` | `8084` | Registro y administración de empresas proveedoras. |
| `microservicio-inventario` | `8085` | Control de stock cruzado entre perfumes y sucursales. |
| `microservicio-carrito` | `8086` | Gestión del carrito de compras y consolidación de pedidos. |
| `microservicio-envios` | `8087` | Seguimiento, logística y estado de los despachos. |
| `microservicio-resenas` | `8088` | Calificaciones y comentarios dejados por los clientes a los perfumes. |
| `microservicio-cliente` | `8089` | Gestión de perfiles e información de contacto de los clientes. |
| `microservicio-promocion` | `8090` | Códigos de descuento, validaciones de fechas y campañas promocionales. |

---

## 🚀 Instrucciones de Ejecución (Paso a Paso)

La plataforma está configurada para ejecutarse localmente conectándose a un servidor MySQL proporcionado por **XAMPP**.

### Prerrequisitos:
- **XAMPP** instalado en tu máquina (con el módulo MySQL habilitado).
- **Java 17** y **Apache Maven** instalados.

### Despliegue Local (con XAMPP):
1. Abre el panel de control de **XAMPP** y arranca el servicio **MySQL**.
   *(Nota: Asegúrate de que MySQL esté corriendo en el puerto por defecto `3306` con el usuario `root` y sin contraseña).*
2. Los microservicios están configurados con `createDatabaseIfNotExist=true`, por lo que **crearán sus respectivas bases de datos automáticamente** al iniciar. No necesitas crearlas manualmente.
3. Para levantar la plataforma, debes ejecutar cada microservicio de manera independiente. Puedes hacerlo desde tu IDE favorito (como IntelliJ o VS Code) ejecutando la clase principal de cada proyecto, o desde la terminal usando Maven:
   ```bash
   # Ejemplo para iniciar el microservicio catalogo:
   cd microservicio-catalogo
   mvn spring-boot:run
   ```
4. Se recomienda levantar el **API Gateway** (`microservicio-api-gateway`) y luego los demás microservicios en el orden deseado.

---

## 📖 Documentación de las APIs (Swagger)

Todos los microservicios cuentan con documentación autogenerada y consumible. Al levantar la aplicación (ya sea por Docker o localmente), puedes acceder a la interfaz de Swagger de cada servicio.

Ejemplos:
- **Catálogo:** [http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html)
- **Usuario:** [http://localhost:8082/swagger-ui.html](http://localhost:8082/swagger-ui.html)
- **Sucursal:** [http://localhost:8083/swagger-ui.html](http://localhost:8083/swagger-ui.html)
- **Proveedor:** [http://localhost:8084/swagger-ui.html](http://localhost:8084/swagger-ui.html)
- **Inventario:** [http://localhost:8085/swagger-ui.html](http://localhost:8085/swagger-ui.html)
- **Carrito/Pedidos:** [http://localhost:8086/swagger-ui.html](http://localhost:8086/swagger-ui.html)
- **Envíos:** [http://localhost:8087/swagger-ui.html](http://localhost:8087/swagger-ui.html)
- **Reseñas:** [http://localhost:8088/swagger-ui.html](http://localhost:8088/swagger-ui.html)
- **Cliente:** [http://localhost:8089/swagger-ui.html](http://localhost:8089/swagger-ui.html)
- **Promoción:** [http://localhost:8090/swagger-ui.html](http://localhost:8090/swagger-ui.html)

---

## 🧪 Pruebas Unitarias y Cobertura (Fase 5)

El código incluye un blindaje de calidad con pruebas unitarias en la capa de negocio (`Service`) de todos los microservicios, simulando dependencias con `@Mock` bajo el patrón **Given-When-Then**.

Para generar el reporte de cobertura de código (JaCoCo):
```bash
# Entra a cualquier microservicio, por ejemplo:
cd microservicio-envios
# Ejecuta las pruebas y genera el reporte
mvn clean test jacoco:report
```
El reporte HTML estará disponible en `target/site/jacoco/index.html`, donde se evidencia el cumplimiento superior al **80% de cobertura** exigido.

---

## 🛒 Ejemplo de Flujo Completo de Negocio

El sistema permite emular el ciclo de vida real de una compra de forma distribuida:

1. **Catálogo (`GET /api/catalog`):** Un cliente navega por la tienda y obtiene la información de los perfumes (Ej. "Chanel N5").
2. **Promociones (`GET /api/promotions/validate/{codigo}`):** El cliente busca aplicar un código "DESC10" para obtener un descuento.
3. **Carrito (`POST /api/pedidos/confirmar`):** El cliente confirma la compra. El `microservicio-carrito` calcula el total (usando la promo), gestiona el pago y genera el "Pedido".
4. **Inventario (`POST /api/inventario/descontar`):** Una vez generado el pedido, este se comunica (idealmente vía eventos o REST) con el inventario para deducir el stock del perfume en la sucursal asignada.
5. **Envíos (`POST /api/envios`):** Paralelamente, se envía la instrucción al microservicio logístico para generar un código de seguimiento en estado "PENDIENTE".
6. **Reseñas (`POST /api/reviews`):** Días después de que el estado de envío cambie a "ENTREGADO", el cliente califica su experiencia con un puntaje de 1 a 5 estrellas.
