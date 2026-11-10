# qhcube-back

Backend de **QHCube**, el juego del Hipercubo Cuántico: una API REST con Spring Boot y MySQL que gestiona los jugadores, la autenticación y las partidas.

El juego lo creó Jesús García López de Lacalle (UPM). Iván García Alcaide (UPM) desarrolló la gestión de jugadores y partidas. El proyecto se divide en tres repositorios:

- [qhcube-front](https://github.com/qhcube/qhcube-front): la interfaz web (Vue 3 + Vite + PrimeVue).
- **qhcube-back** (este): la API REST.
- [qhcube-deploy](https://github.com/qhcube/qhcube-deploy): el despliegue en producción con Docker Compose y Caddy.

## Tecnologías

- Java 17 y Spring Boot 4.1: Spring Web MVC, Spring Security y Spring Data JPA (Hibernate 7).
- MySQL 8.0.
- Autenticación con tokens JWT (jjwt).
- MapStruct y Lombok.
- Maven, a través del wrapper `mvnw` (no hace falta instalarlo).

## Estructura

| Paquete | Contenido |
|---|---|
| `controller` | Controladores REST |
| `service` | Lógica de negocio: jugadores, partidas, login y registro |
| `entity`, `repository` | Entidades JPA y repositorios |
| `dto`, `mapper` | Objetos que intercambia la API y su conversión desde las entidades (MapStruct) |
| `config`, `filter`, `entrypoint` | Seguridad y configuración: CORS, filtro JWT y respuesta 401 |
| `utils` | Utilidades: tokens JWT, envío de emails, validación de movimientos... |

La API tiene dos grupos de rutas:

- **Públicas** (`/free/...`): login, registro, validación del email, reenvío del email de validación y versión del backend.
- **Privadas** (`/api/...`, con token JWT en la cabecera `Authorization`): partidas (crear, obtener, añadir movimientos, reproducir, reiniciar y borrar) y logout.

En desarrollo, la documentación de la API está en Swagger: <http://localhost:9000/free/swagger-ui.html>. En producción está desactivada.

## Arrancar en local

Requisitos: Java 17 y Docker.

1. **Base de datos.** Crear un contenedor MySQL 8.0.33 que escuche en el puerto 3307:

   ```bash
   docker run -d --name mysql-dev -p 127.0.0.1:3307:3306 \
     -e MYSQL_ROOT_PASSWORD=<contraseña-root> \
     -e MYSQL_DATABASE=qhcube_db \
     -e MYSQL_USER=qhcube_usr \
     -e MYSQL_PASSWORD=<contraseña> \
     -v qhcube_mysql_dev:/var/lib/mysql \
     mysql:8.0.33
   ```

   Las siguientes veces basta con `docker start mysql-dev`. Las tablas las crea Hibernate al arrancar la aplicación.

2. **Secretos.** Copiar `.env.example` como `.env` en la raíz del proyecto y rellenarlo. `DB_PASSWORD` es la misma contraseña que `MYSQL_PASSWORD` del paso anterior, y `JWT_SECRET` debe tener al menos 32 caracteres. El fichero `.env` no se sube a git.

   Sin una contraseña de correo válida (`MAIL_PASSWORD`) no se puede enviar el email de verificación, así que el registro de jugadores no se completa.

3. **Arrancar:**

   ```bash
   ./mvnw spring-boot:run
   ```

   La API queda en <http://localhost:9000>, con el perfil `dev`, que es el activo por defecto.

## Tests

```bash
./mvnw test
```

Algunos tests arrancan la aplicación completa, así que necesitan la base de datos de desarrollo (`mysql-dev`) en marcha. La CI de GitHub Actions hace lo mismo con un servicio MySQL en el mismo puerto.

## Versiones y releases

- Cada cambio se hace en una rama propia, con un PR a `dev`. Una versión nueva se publica con un PR de `dev` a `main`.
- La CI (`.github/workflows/ci.yml`) pasa los tests en cada PR y en cada push a `dev` y `main`.
- Al subir un tag `vX.Y.Z`, el workflow `.github/workflows/publicar.yml` comprueba que coincide con la versión del `pom.xml`, compila, pasa los tests y crea un borrador de release con `qhcube-back-X.Y.Z.jar` y `SHA256SUMS`.
- El despliegue en producción se describe en [qhcube-deploy](https://github.com/qhcube/qhcube-deploy).

Para generar el jar a mano:

```bash
./mvnw clean package
```

El jar queda en `target/qhcube-back-X.Y.Z.jar`. En producción se arranca con el perfil `prod`: `java -Dspring.profiles.active=prod -jar qhcube-back-X.Y.Z.jar`.

## Licencia

Copyright (C) 2023-2026 Jesús García López de Lacalle, Universidad Politécnica de Madrid, Iván García Alcaide.

Este programa es software libre: puede redistribuirse y/o modificarse según los términos de la Licencia Pública General de GNU (GPL) publicada por la Free Software Foundation, ya sea la versión 3 de la licencia o, a elección del usuario, cualquier versión posterior.

Se distribuye con la esperanza de que sea útil, pero SIN NINGUNA GARANTÍA, ni siquiera la garantía implícita de COMERCIABILIDAD o IDONEIDAD PARA UN PROPÓSITO PARTICULAR. Consulte el fichero [LICENSE](LICENSE) para más detalles.
