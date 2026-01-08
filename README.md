# Point Of Sale Backend

Sistema backend de punto de venta desarrollado con Spring Boot y Java 17. Provee APIs REST y un frontend estático integrado para la gestión completa de un sistema POS.

## Características principales

- Backend Spring Boot con Java 17
- Frontend estático incluido
- API REST para gestión de inventario y ventas
- Maven Wrapper incluido para builds reproducibles
- Soporte para Docker
- Tests unitarios

## Requisitos

- **JDK 17** o superior
- **Maven** (opcional, incluye Maven Wrapper)
- **Docker** (opcional, para despliegue en contenedores)

## Instalación y ejecución

### 1. Clonar el repositorio

```bash
git clone https://github.com/113134-Zea-Martin/Point-Of-Sale-Backend.git
cd Point-Of-Sale-Backend
```

### 2. Ejecutar en modo desarrollo

**Windows:**
```bash
.\mvnw.cmd spring-boot:run
```

**Linux/Mac:**
```bash
./mvnw spring-boot:run
```

La aplicación estará disponible en `http://localhost:8080`

### 3. Compilar y ejecutar JAR

**Compilar:**
```bash
.\mvnw.cmd clean package -DskipTests
```

**Ejecutar:**
```bash
java -jar target/gestion-inventario-0.0.1-SNAPSHOT.jar
```

**Especificar puerto personalizado:**
```bash
java -jar target/gestion-inventario-0.0.1-SNAPSHOT.jar --server.port=8080
```

## Despliegue con Docker

**Construir imagen:**
```bash
docker build -t point-of-sale-backend:latest .
```

**Ejecutar contenedor:**
```bash
docker run -p 8080:8080 point-of-sale-backend:latest
```

**Con perfil específico:**
```bash
docker run -p 8080:8080 --env SPRING_PROFILES_ACTIVE=prod point-of-sale-backend:latest
```

## Estructura del proyecto

```
Point-Of-Sale-Backend/
├── src/
│   ├── main/
│   │   ├── java/              # Código fuente Java
│   │   └── resources/
│   │       ├── static/        # Frontend estático
│   │       └── application.properties
│   └── test/                  # Tests unitarios
├── target/                    # Artefactos generados
├── pom.xml                    # Configuración Maven
├── Dockerfile                 # Imagen Docker
└── mvnw, mvnw.cmd            # Maven Wrapper
```

## Configuración

La configuración principal se encuentra en `src/main/resources/application.properties`.

**Variables importantes:**
- `server.port` - Puerto de la aplicación (default: 8080)
- `spring.datasource.*` - Configuración de base de datos
- `spring.profiles.active` - Perfil activo (dev/prod)

**⚠️ Seguridad:** No incluyas credenciales en `application.properties`. Usa variables de entorno o archivos de perfil específicos.

## Abrir navegador automáticamente (opcional)

Para abrir el navegador al iniciar la aplicación, añade este componente:

```java
import java.awt.Desktop;
import java.net.URI;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class BrowserLauncher implements CommandLineRunner {
    @Override
    public void run(String... args) {
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(new URI("http://localhost:8080"));
            }
        } catch (Exception e) {
            System.err.println("No se pudo abrir el navegador: " + e.getMessage());
        }
    }
}
```

**Nota:** Esta funcionalidad no funcionará en entornos sin interfaz gráfica (servidores, contenedores).

## Integración Continua (GitHub Actions)

Ejemplo básico de workflow `.github/workflows/ci.yml`:

```yaml
name: CI
on: [push, pull_request]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      
      - name: Set up JDK 17
        uses: actions/setup-java@v4
        with:
          java-version: '17'
          distribution: 'temurin'
      
      - name: Build with Maven
        run: ./mvnw clean package
      
      - name: Run tests
        run: ./mvnw test
```

## Resolución de problemas

### Puerto en uso
Si el puerto 8080 está ocupado, especifica otro puerto:
```bash
java -jar target/gestion-inventario-0.0.1-SNAPSHOT.jar --server.port=8081
```

### Verificar proceso en ejecución
**Windows:**
```bash
netstat -ano | findstr :8080
tasklist | findstr <PID>
```

**Linux/Mac:**
```bash
lsof -i :8080
```

### El navegador no abre automáticamente
Verifica que `Desktop.isDesktopSupported()` retorne `true` y que tengas interfaz gráfica disponible.

## Comandos útiles

| Acción | Comando |
|--------|---------|
| Ejecutar en desarrollo | `.\mvnw.cmd spring-boot:run` |
| Compilar proyecto | `.\mvnw.cmd clean package` |
| Ejecutar tests | `.\mvnw.cmd test` |
| Ejecutar JAR | `java -jar target/gestion-inventario-0.0.1-SNAPSHOT.jar` |
| Build Docker | `docker build -t point-of-sale-backend .` |
| Run Docker | `docker run -p 8080:8080 point-of-sale-backend` |

## Contribuir

Las contribuciones son bienvenidas. Por favor:

1. Haz fork del proyecto
2. Crea una rama para tu feature (`git checkout -b feature/nueva-funcionalidad`)
3. Commit tus cambios (`git commit -am 'Agrega nueva funcionalidad'`)
4. Push a la rama (`git push origin feature/nueva-funcionalidad`)
5. Abre un Pull Request

## Licencia

Este proyecto necesita definir su licencia. Considera usar [MIT](https://opensource.org/licenses/MIT) o [Apache 2.0](https://www.apache.org/licenses/LICENSE-2.0).

## Contacto

Repositorio: [https://github.com/113134-Zea-Martin/Point-Of-Sale-Backend](https://github.com/113134-Zea-Martin/Point-Of-Sale-Backend)

---

**Nota:** Recuerda actualizar las rutas y nombres según la configuración específica de tu proyecto.