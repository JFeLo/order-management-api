# Usa una imagen base de OpenJDK 21
FROM openjdk:21-jdk-slim

# Establece el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia el archivo JAR desde la compilación de tu proyecto (target/) al contenedor
COPY target/orderapi-0.0.1-SNAPSHOT.jar orderapi.jar

# Expone el puerto donde se ejecutará la aplicación
EXPOSE 8080

# Ejecuta el JAR de la aplicación
ENTRYPOINT ["java", "-jar", "orderapi.jar"]
