Sistema de Gestión de Tareas Domésticas - Backend



* **Lenguaje:** Java 21 (LTS)
* **Framework:** Spring Boot 
* **Seguridad:** Spring Security + BCrypt Hashing
* **Base de Datos:** PostgreSQL
* **Gestor de Dependencias:** Maven

---

Para que el proyecto les corra, instalen jdk 21 LTS y PostgreSQL, creo que usé la versión 17.9


# Creen una base de datos local llamada:
`db_tareas_domesticas`

### 3. Archivo de Configuración Local
creen un archivo llamado application.proerties en:
`src/main/resources/application.properties`

**metan en el archivo creado anteriormente, en la ruta especificada, el siguiente contenido base:**
```properties
spring.application.name=tareas-domesticas
spring.datasource.url=jdbc:postgresql://localhost:5432/db_tareas_domesticas
spring.datasource.username=TU_USUARIO_POSTGRES
spring.datasource.password=TU_CONTRASEÑA_POSTGRES
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```
**Reemplacen TU_USUARIO_POSTGRES y TU_CONTRASENA_POSTGRES por las credenciales que seteen ustedes mismos. Por eso no inclui el archivo dentro del repositorio**