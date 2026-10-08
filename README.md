# Task Manager API

API REST desarrollada con Java y Spring Boot para la gestión de tareas.

Proyecto personal en desarrollo orientado a reforzar y demostrar conocimientos de desarrollo backend, diseño de APIs REST, persistencia de datos y arquitectura por capas.

## 🎥 Demo

▶️ [Ver demostración de la API](https://github.com/user-attachments/assets/a0eef1ee-565f-4e92-ae88-5209e980bba0)

En la demostración se muestran distintas operaciones de la API mediante Postman: creación, consulta, actualización y eliminación de tareas, filtros y búsqueda, paginación y gestión de errores HTTP.

## 🚀 Funcionalidades

- Crear tareas.
- Consultar todas las tareas.
- Consultar una tarea por ID.
- Actualizar tareas existentes.
- Eliminar tareas.
- Filtrar tareas por estado (`completed`).
- Buscar tareas por texto.
- Paginar los resultados.
- Persistencia de datos en MariaDB mediante Spring Data JPA.
- Uso de DTOs para las peticiones y respuestas de la API.
- Validación de datos de entrada.
- Gestión centralizada de excepciones.
- Respuestas HTTP adecuadas para recursos inexistentes y datos no válidos.

## 🛠️ Tecnologías

- Java 21
- Spring Boot
- Spring Data JPA
- Hibernate
- MariaDB
- Maven
- JUnit
- Mockito
- MockMvc
- Postman

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
