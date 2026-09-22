# 🏗️ Proyecto Full Stack — Arquitectura de Microservicios

![Asignatura](https://img.shields.io/badge/Asignatura-Full_Stack-blue)
![Arquitectura](https://img.shields.io/badge/Arquitectura-Microservicios-purple)
![Estado](https://img.shields.io/badge/Estado-Completado-brightgreen)

Proyecto práctico desarrollado para la asignatura de **Full Stack**. Consiste en un sistema distribuido escalable desarrollado bajo una **arquitectura de microservicios**, separación de responsabilidades, integración de API Gateway y comunicación entre componentes del backend y cliente web.

---

## 🏛️ Enfoques de Diseño y Arquitectura

El desarrollo de este sistema Full Stack incorpora las siguientes buenas prácticas y patrones de diseño:

- **Descomposición por Dominio:** División del sistema en servicios independientes y desacoplados (*Database per Service*).
- **API Gateway Pattern:** Punto único de entrada para el cliente que gestiona el enrutamiento de solicitudes, el control de acceso y el tráfico hacia el backend.
- **Comunicación entre Servicios:**
  - **Síncrona (REST / HTTP):** Para peticiones inmediatas y de baja latencia entre el frontend y los servicios.
  - **Asíncrona (Event-Driven Architecture):** Uso de mensajería/eventos para la integración desacoplada entre microservicios.
- **Seguridad & Autenticación:** Módulo centralizado de autenticación mediante Tokens JWT (JSON Web Tokens).

---

## 🧩 Microservicios e Infraestructura del Backend

| Servicio / Componente | Responsabilidad | Base de Datos / Persistencia | Enfoque de Comunicación |
| :--- | :--- | :--- | :--- |
| **API Gateway** | Enrutamiento de peticiones, CORS y proxy del sistema. | N/A | Síncrono (HTTP / REST) |
| **Auth Service** | Autenticación, gestión de usuarios y emisión de JWT. | [ej. PostgreSQL / MongoDB] | Síncrono |
| **Servicio de Dominio 1** | [Nombre del servicio, ej. Service A] | [ej. PostgreSQL] | Síncrono / Asíncrono |
| **Servicio de Dominio 2** | [Nombre del servicio, ej. Service B] | [ej. MongoDB] | Asíncrono (Eventos) |
| **Servicio de Notificaciones / Eventos** | [Procesamiento de eventos en segundo plano] | [ej. Redis] | Suscriptor de Eventos |

---

## 🛠️ Stack Tecnológico

### Frontend
- **Framework / Librería:** [ej. React / Next.js / Vue]
- **Consumo de API:** [ej. Axios / Fetch API / React Query]
- **Estilos:** [ej. Tailwind CSS / CSS Modules / Bootstrap]

### Backend & Microservicios
- **Lenguajes / Runtimes:** [ej. Node.js (Express) / Python (FastAPI) / Java (Spring Boot)]
- **Bases de Datos:** [ej. PostgreSQL / MongoDB / Redis]
- **Event Broker / Mensajería:** [ej. RabbitMQ / Kafka / NATS]
- **Autenticación:** JWT & Encripción con bcrypt

### DevOps & Despliegue
- **Contenedores:** Docker & Docker Compose
- **Documentación API:** Swagger / Postman Collection

---

## 📁 Estructura del Repositorio

```text
├── api-gateway/          # Proxy de entrada y enrutador
├── auth-service/         # Microservicio de Autenticación
├── service-a/            # Microservicio de dominio A
├── service-b/            # Microservicio de dominio B
├── frontend/             # Cliente Web / Interfaz de usuario
├── docker-compose.yml    # Orquestación de contenedores y servicios
└── README.md             # Documentación del proyecto
