# Task Manager API

API REST desarrollada con Java y Spring Boot para la gestión de tareas.

El proyecto está orientado a reforzar conocimientos de desarrollo backend, persistencia de datos y arquitectura por capas mediante una aplicación práctica construida desde cero.

## 🚀 Funcionalidades

- Crear tareas.
- Consultar todas las tareas.
- Consultar una tarea por ID.
- Actualizar tareas existentes.
- Eliminar tareas.
- Persistencia de datos en MariaDB.
- Gestión de errores cuando una tarea no existe.

## 🛠️ Tecnologías

- Java 21
- Spring Boot
- Spring Data JPA
- Hibernate
- MariaDB
- Maven

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
Controller

Gestiona las peticiones HTTP y expone los endpoints de la API.

Service

Contiene la lógica de aplicación y coordina las operaciones sobre las tareas.

Repository

Utiliza Spring Data JPA para realizar las operaciones de persistencia sobre MariaDB.

📡 Endpoints
Método	Endpoint	Descripción
GET	/tasks	Obtener todas las tareas
GET	/tasks/{id}	Obtener una tarea por ID
POST	/tasks	Crear una nueva tarea
PUT	/tasks/{id}	Actualizar una tarea
DELETE	/tasks/{id}	Eliminar una tarea
📦 Modelo Task

Cada tarea contiene:

id
title
description
completed
createdAt

El identificador se genera automáticamente y la fecha de creación se establece al persistir la tarea.

⚠️ Gestión de errores

Cuando se solicita, actualiza o elimina una tarea que no existe, la aplicación utiliza una excepción personalizada TaskNotFoundException.

La API devuelve una respuesta HTTP 404 Not Found con un mensaje descriptivo.

▶️ Ejecución

Clona el repositorio:

git clone https://github.com/diegolijarcio388/Task-Manager-Api.git
cd Task-Manager-Api

Configura la conexión a MariaDB en las propiedades de la aplicación y ejecuta el proyecto con Maven:

./mvnw spring-boot:run
🚧 Estado del proyecto

Proyecto en desarrollo.

Próximas mejoras previstas:

DTOs.
Validación de datos.
Gestión global de excepciones.
Tests.
Autenticación y autorización.
Dockerización.
🎯 Objetivo

Este proyecto forma parte de mi aprendizaje y especialización en desarrollo backend con Java y Spring Boot, aplicando conceptos como APIs REST, persistencia de datos, arquitectura por capas y manejo de errores.
