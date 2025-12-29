# Etapa 1: Construcción (Build)
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
# Copiamos el pom y descargamos dependencias (esto optimiza el cache de Docker)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copiamos el código fuente y generamos el archivo JAR
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Ejecución (Run)
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
# Copiamos el JAR generado desde la etapa de build
# El nombre del jar suele ser artifactId-version.jar
COPY --from=build /app/target/gestion-inventario-0.0.1-SNAPSHOT.jar app.jar

# Exponemos el puerto (aunque Render lo maneja internamente, es buena práctica)
EXPOSE 8080

# Comando para arrancar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]