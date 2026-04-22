# ViviEstu - Backend API (Spring Boot)

ViviEstu es el motor de servicios desarrollado para centralizar la oferta de alojamiento universitario. Esta API REST gestiona la lógica de negocio, la seguridad de los usuarios y la persistencia de datos, permitiendo una conexión fluida entre estudiantes que buscan vivienda y arrendadores que publican sus inmuebles.
## Descripción Técnica

El backend está construido bajo una arquitectura de capas utilizando Java y Spring Boot, lo que garantiza escalabilidad y facilidad de mantenimiento. El sistema aborda la problemática de la inseguridad y la falta de información centralizada, proporcionando un entorno validado para la gestión de arrendamientos.
## Requisitos Funcionales Implementados

### Siguiendo los objetivos definidos en el informe del proyecto:

   Autenticación y Seguridad: Implementación de JWT (JSON Web Tokens) para asegurar que solo usuarios registrados puedan gestionar anuncios o acceder a información sensible.

   Gestión de Inmuebles (CRUD): Endpoints robustos para que los arrendadores puedan registrar, actualizar y eliminar ofertas de habitaciones o departamentos.

   Motor de Búsqueda y Filtrado: Procesamiento de datos para filtrar viviendas por precio, servicios (agua, luz, internet) y cercanía a centros de estudio.

   Persistencia Relacional: Modelado de datos en PostgreSQL para asegurar la integridad de la información de usuarios e inmuebles.

## Stack Tecnológico

   Lenguaje: Java 17+

   Framework: Spring Boot 3.x

   Gestión de Proyectos: Maven

   Base de Datos: PostgreSQL

   Seguridad: Spring Security & JWT

   ORM: Spring Data JPA (Hibernate)
