# Alojamiento Gestión (módulo independiente)
- Puerto: 8080
- Base path: /api/v1/alojamiento
- Ejecutar:
  ```bash
  mvn -q -DskipTests clean package
  mvn spring-boot:run
  ```
- Health: GET http://localhost:8080/api/v1/alojamiento/health
