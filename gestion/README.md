# gestion 🏗️
**Integrante 1** — Proyecto Spring Boot con 5 microservicios independientes.

## Requisitos
- Java 21
- Maven 3.8+
- MySQL corriendo en localhost:3306

## Cómo ejecutar
```bash
mvn spring-boot:run
```
La base de datos `gestion_db` se crea automáticamente.
Las tablas las crea Flyway al iniciar.

## Endpoints
| Microservicio | Ruta |
|---|---|
| Empleados | GET/POST/PUT/DELETE `/api/empleados` |
| Departamentos | GET/POST/PUT/DELETE `/api/departamentos` |
| Cargos | GET/POST/PUT/DELETE `/api/cargos` |
| Autenticación | POST `/api/auth/registrar` · POST `/api/auth/login` |
| Auditoría | GET/POST `/api/auditoria` |
