# Imagen JDK 17
FROM eclipse-temurin:17-jdk-alpine

# Directorio de trabajo en el contenedor
WORKDIR /app

# Copiar el JAR
COPY target/tu-aplicacion-0.0.1-SNAPSHOT.jar app.jar

# Exponer el puerto en el que escucha Spring Boot
EXPOSE 8080

# Ejecutar la App
ENTRYPOINT ["java", "-jar", "app.jar"]
