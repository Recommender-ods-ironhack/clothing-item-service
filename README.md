# clothing-item-service

## Descripción

El microservicio `clothing-item-service` gestiona un catálogo de artículos de vestimenta, ofreciendo operaciones CRUD y funcionalidades avanzadas de filtrado. Permite obtener items basados en parámetros como nombre, talla, estilos, color y precio máximo, además de identificar aquellos con exceso de stock para aplicar descuentos. Se integra en una arquitectura de microservicios mediante Eureka y es accesible a través de un API Gateway.

## Características

- **Respuestas estandarizadas:** Todos los endpoints devuelven respuestas encapsuladas en `ResponseEntity` para un manejo preciso de los códigos de estado HTTP y mensajes.
- **Operaciones CRUD:** Permite crear, obtener, actualizar parcialmente y eliminar items.
- **Filtrado Dinámico:** Filtrado flexible de items usando diversos parámetros:
    - `name` (String)
    - `size` (ESize)
    - `styles` (List<EStyle>)
    - `color` (String)
    - `maxPrice` (Integer)
- **Exceso de Stock:** Identificación de items con alto stock para la aplicación de descuentos.
- **Integración con Microservicios:** Registro en Eureka para el descubrimiento por otros servicios y comunicación a través de un Gateway.

## Tecnologías Utilizadas

- [Spring Boot](https://spring.io/projects/spring-boot)
- [Maven](https://maven.apache.org/)
- [MySQL](https://www.mysql.com/)
- [Lombok](https://projectlombok.org/)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [Eureka](https://cloud.spring.io/spring-cloud-netflix/multi/multi_spring-cloud-eureka-server.html)

## Endpoints

> **Nota:** Todas las respuestas se devuelven encapsuladas en `ResponseEntity`.

### GET `/api/clothing-item/{id}`
- **Función:** Devuelve un item específico a partir de su ID.
- **Response:** `ResponseEntity<ClothingItem>`

### GET `/api/clothing-item/filtered`
- **Función:** Retorna una lista de items que cumplen con los criterios de filtrado, según los parámetros opcionales:
    - `name` (String)
    - `size` (ESize)
    - `styles` (List<EStyle>)
    - `color` (String)
    - `maxPrice` (Integer)
- **Response:** `ResponseEntity<List<ClothingItem>>`

### GET `/api/clothing-item/excess-stock`
- **Función:** Obtiene una lista de items con exceso de stock, lista para la aplicación de descuentos.
- **Response:** `ResponseEntity<List<ClothingItem>>`

### POST `/api/clothing-item`
- **Función:** Crea un nuevo item de vestimenta.
- **Request Body:** Objeto `ClothingItem` (con validación `@Valid`).
- **Response:** `ResponseEntity<ClothingItem>`

### PATCH `/api/clothing-item/{id}`
- **Función:** Realiza una actualización parcial del item especificado.
- **Request Body:** Objeto `ClothingItemDTO` que contiene los cambios.
- **Response:** `ResponseEntity<?>`

### DELETE `/api/clothing-item/{id}`
- **Función:** Elimina el item identificado por su ID.
- **Response:** `ResponseEntity<?>` con un mensaje de confirmación.

## Configuración y Ejecución

### Requisitos Previos

- **Base de Datos:** MySQL debe estar instalado y configurado.
- **Eureka:** La instancia de Eureka debe estar funcionando antes de iniciar el microservicio.

### Configuración de la Base de Datos

Crea la base de datos en MySQL utilizando la siguiente URL:

jdbc:mysql://localhost:3306/clothing_item


Asegúrate de insertar los datos iniciales necesarios para el correcto funcionamiento del servicio.

### Archivo de Configuración

Dentro de la carpeta `src/main/resources`, crea el archivo `application.properties` con la siguiente configuración, adaptándola a tu entorno:

```properties
# Nombre de la aplicación para el registro en Eureka
spring.application.name=clothing-item-service

# Configuración del servidor
server.port=8082

# Configuración de la base de datos
spring.datasource.url=jdbc:mysql://localhost:3306/<nombre de tu base de datos>
spring.datasource.username=<nombre de tu usuario>
spring.datasource.password=<contraseña>
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# Configuración de Eureka para el descubrimiento de servicios
eureka.client.serviceUrl.defaultZone=http://localhost:8761/eureka/

# Configuración de JPA para la gestión del esquema de la base de datos
spring.jpa.hibernate.ddl-auto=update

```
## Descripción de las propiedades

- **spring.application.name:** Define el nombre del microservicio para su registro en Eureka.
- **server.port:** Especifica el puerto en el que se ejecutará el servicio.
- **spring.datasource.\*:** Parámetros necesarios para la conexión a la base de datos MySQL, incluyendo URL, credenciales y el driver JDBC.
- **spring.jpa.properties.hibernate.dialect:** Informa a Hibernate del dialecto SQL a utilizar, optimizando la interacción con MySQL.
- **eureka.client.serviceUrl.defaultZone:** Proporciona la URL del servidor Eureka para habilitar el registro y descubrimiento del servicio.
- **spring.jpa.hibernate.ddl-auto:** Controla la estrategia de actualización del esquema de la base de datos; en este caso, se actualiza automáticamente al iniciar el servicio.

## Ejecución del Microservicio

1. **Inicia la instancia Eureka:**  
   Verifica que Eureka esté en funcionamiento para asegurar el registro correcto del servicio.

2. **Ejecuta el microservicio:**  
   Ejecuta la clase principal `ClothingItemServiceApplication.java` desde tu IDE o mediante la línea de comandos.
