# -------- Etapa de construcción --------
# Imagen base con Java 21
FROM eclipse-temurin:21-jdk AS build
# Definir directorio de trabajo
WORKDIR /app
# Copiar código fuente al contenedor
COPY target/generated-sources/annotations .
# Dar permisos de ejecución al Maven Wrapper
RUN chmod +x mvnw
# Construir la aplicación (sin ejecutar tests)
RUN ./mvnw clean package -DskipTests
# -------- Imagen final --------
FROM eclipse-temurin:21-jdk
# Definir directorio de trabajo
WORKDIR /app

# Exponer el puerto de la aplicación
EXPOSE 8080
# Comando de inicio
ENTRYPOINT ["java", "-jar", "app.jar"]