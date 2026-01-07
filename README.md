# Reto Técnico - Backend Java Senior

**Candidato:** JOSE FRANCISCO CRUZ CORRO  
**Puesto Solicitado:** Senior Desarrollador Backend Java

---

## Descripción General
Este repositorio contiene la solución completa al ejercicio técnico, implementando una **Arquitectura de Microservicios Reactiva y Orientada a Eventos**. El sistema ha sido diseñado desde cero para demostrar capacidades de nivel Senior, enfocándose en escalabilidad, desacoplamiento y resiliencia, cumpliendo estrictamente con los requisitos funcionales (F1-F7) y proponiendo mejoras arquitectónicas robustas.

## Arquitectura del Sistema
El sistema se ha alejado de una arquitectura monolítica tradicional para adoptar un enfoque distribuido:

### 1. Microservicio de Clientes (ms-clientes) - Producer
*   **Responsabilidad**: Gestión del ciclo de vida de la información de los clientes (Entidad Persona y Cliente).
*   **Tecnología**: Spring Boot WebFlux (Non-blocking I/O).
*   **Patrón de Eventos**: Actúa como productor de eventos. Cada vez que se crea o modifica un cliente, publica un evento de dominio (ClienteEvent) en el tópico cliente-events de **Apache Kafka**. Esto desacopla el sistema: ms-clientes no necesita saber quién consume los datos ni esperar respuesta.

### 2. Microservicio de Cuentas (ms-cuentas) - Consumer & Core
*   **Responsabilidad**: Gestión de Cuentas Bancarias, Movimientos y Reportes.
*   **Tecnología**: Spring Boot WebFlux & JPA.
*   **CQRS Simplificado**: Para evitar llamadas síncronas HTTP (que reducen la disponibilidad) al generar reportes, este servicio consume los eventos de Kafka y mantiene una **Réplica de Lectura** de los datos del cliente necesarios. Esto garantiza que los reportes se puedan generar incluso si ms-clientes está caído.
*   **Lógica de Negocio Crítica**: Implementa la validación de saldos ("Saldo no disponible") y asegura la consistencia de las transacciones financieras.

### 3. Infraestructura de Datos
*   **Apache Kafka & Zookeeper**: Backbone de mensajería para garantizar consistencia eventual y desacoplamiento.
*   **PostgreSQL**: Base de datos relacional. Se implementa el patrón **Database per Service** de forma lógica (esquemas db_clientes y db_cuentas independientes) corriendo sobre una misma instancia contenerizada para facilitar el despliegue.

## Tecnologías Utilizadas
*   **Lenguaje**: Java 17
*   **Framework**: Spring Boot 3.2.1 (Stack Reactivo)
*   **Base de Datos**: PostgreSQL 15
*   **Mensajería**: Spring Kafka
*   **Documentación API**: SpringDoc OpenAPI (Swagger UI)
*   **Contenerización**: Docker & Docker Compose
*   **Testing**:
    *   **Unitarios**: JUnit 5, Mockito, Reactor Test.
    *   **Integración (E2E)**: Karate DSL (Behavior Driven Development).

## Estructura del Proyecto
senior-system/
├── ms-clientes/          # Microservicio Producer (Clientes)
├── ms-cuentas/           # Microservicio Consumer (Cuentas/Movimientos)
├── integration-tests/    # Suite de pruebas E2E con Karate
├── docker-compose.yml    # Orquestación de contenedores
├── init-multiple-dbs.sh  # Script de inicialización de BD
├── PostmanCollection.json# Colección de pruebas manuales
└── README.md             # Esta documentación

## Instrucciones de Despliegue

### Requisitos Previos
*   Docker y Docker Compose instalados y en ejecución.
*   Puertos 8081, 8082, 5432 y 9092 libres en el host.
*   *Nota*: En entornos restringidos (como DevContainers sin Docker-in-Docker), los tests de integración pueden requerir configuración adicional.

### Paso 1: Levantar el Entorno
Desde la carpeta raíz senior-system:

```bash
docker compose up --build
```
*Esto compilará los microservicios, descargará las imágenes de Kafka/Postgres y levantará todo el ecosistema. Espere a que los logs indiquen que las aplicaciones han iniciado.*

### Paso 2: Verificar Servicios
*   **API Clientes**: http://localhost:8081
*   **API Cuentas**: http://localhost:8082

## Documentación API (Swagger)
El proyecto incluye documentación interactiva OpenAPI 3.0:
*   📄 **Swagger Clientes**: http://localhost:8081/webjars/swagger-ui/index.html
*   📄 **Swagger Cuentas**: http://localhost:8082/webjars/swagger-ui/index.html

## Guía de Pruebas (Testing)

### 1. Pruebas Unitarias (Lógica de Negocio)
Validan las reglas internas (ej. impedir saldo negativo) sin dependencias externas.

```bash
# Validar MS Clientes
cd ms-clientes
mvn test

# Validar MS Cuentas
cd ../ms-cuentas
mvn test
```

### 2. Pruebas de Integración End-to-End (Karate DSL)
Esta suite prueba el flujo de negocio real a través de los contenedores Docker, verificando la integración HTTP y la mensajería Kafka.
**Flujo Probado**: Crear Cliente -> (Kafka) -> Crear Cuenta -> Movimiento Débito -> Validar Saldo -> Generar Reporte.

**Requisito**: El sistema debe estar corriendo con docker compose.

```bash
cd integration-tests
mvn test
```
*Los reportes detallados en HTML se generan en target/karate-reports/.*

### 3. Pruebas Manuales
Importe el archivo PostmanCollection.json en **Postman** para ejecutar peticiones preconfiguradas contra el entorno local.

## Funcionalidad Integral y Puntos Clave
*   **Manejo de Errores**: Se implementaron GlobalExceptionHandler para capturar excepciones como RuntimeException("Saldo no disponible") y devolver códigos HTTP adecuados (400 Bad Request, 404 Not Found).
*   **Resiliencia**: Configuración de reintentos y timeouts en las comunicaciones.
*   **Principio SOLID**: Código estructurado en capas (Controller, Service, Repository, Entity, DTO) con inyección de dependencias limpia.

---
**Declaración**: Este código es original y representa la calidad técnica que puedo aportar al equipo como Senior Java Backend Developer.
