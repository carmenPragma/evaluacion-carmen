# CLIENTES - Evaluación BISA (API)

Este proyecto es una aplicación de gestión de clientes desarrollada en Java con Spring Boot 3. La aplicación proporciona APIs REST para realizar las operaciones
requeridas.
## REQUISITOS
JAVA 8 o superior MAVEN
- SWAGGER (SPRINGDOC), LOMBOK

## ESTRUCTURA DEL PROYECTO

### controller
    ClienteController.java
### model
    Cliente.java
    Persona.java
    Referencia.java

Sin persistencia de datos.

## Uso de las APIs REST

- POST /api/persona: Crea una nueva persona (requerido para crear clientes).
- GET /api/personas: Lista todas las personas registradas.
- POST /api/cliente: Crea un nuevo cliente (se debe enviar la personaId previamente creada).
- POST /api/{clienteId}/referencias: Añade una referencia a un cliente.
- DELETE /api/{clienteId}/referencias: Elimina una referencia de un cliente.
- GET /api/clientes/{accesibilidad}: Lista clientes según su accesibilidad (NULA,MALA,etc.).
- Se adjunta archivo de ejemplo de llamadas a servicios collection postman (Clientes BISA.postman_collection.json)

Documentación general de la API
La documentación de la API está disponible en http://localhost:8080/swagger-ui.html.

Observaciones: Se debió implementar  Request, Responses, Mappers 😳 adecuados además de control de errores.