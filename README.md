# Task Manager API

API REST desarrollada con Java y Spring Boot para la gestión de tareas.

Proyecto personal en desarrollo orientado a reforzar y demostrar conocimientos de desarrollo backend, diseño de APIs REST, persistencia de datos y arquitectura por capas.

## 🎥 Demo

https://github.com/user-attachments/assets/cb23610f-a742-48ff-ab6b-cecbb3c2fe51

Demostración de la API REST desplegada mediante **Swagger UI (OpenAPI)**, incluyendo consulta, creación, actualización y eliminación de tareas, validación de datos y respuestas HTTP.

🔗 **[Probar Swagger UI](https://r9tqe3.bot-keep.xyz/swagger-ui/index.html)**

Las operaciones `GET` son públicas. Las operaciones de creación, actualización y eliminación requieren autenticación **HTTP Basic**.

## 🚀 Funcionalidades

- Crear tareas.
- Consultar todas las tareas.
- Consultar una tarea por ID.
- Actualizar tareas existentes.
- Eliminar tareas.
- Filtrar tareas por estado (`completed`).
- Buscar tareas por texto.
- Paginar los resultados.
- Persistencia de datos en MySQL (Aiven) mediante Spring Data JPA.
- Autenticación HTTP Basic para las operaciones de escritura.
- Uso de DTOs para las peticiones y respuestas de la API.
- Validación de datos de entrada.
- Gestión centralizada de excepciones.
- Respuestas HTTP adecuadas para recursos inexistentes y datos no válidos.

## 🛠️ Tecnologías

- Java 21
- Spring Boot
- Spring Data JPA
- Hibernate
- MySQL (Aiven)
- Maven
- JUnit
- Mockito
- MockMvc
- Spring Security (HTTP Basic)
- Springdoc OpenAPI / Swagger UI

## 🧱 Arquitectura

El proyecto utiliza una estructura por capas para separar responsabilidades:

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
Database
```
