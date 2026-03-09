# 💬 Foro Hub API & Web

FórumHub es una plataforma de foros (Backend + Frontend integrado) donde los estudiantes pueden registrar, consultar, actualizar y eliminar sus dudas (tópicos).

El sistema se compone de una API RESTful construida con **Java y Spring Boot 3**, respaldada por una base de datos MySQL 8 gestionada vía Docker, y cuenta con un **cliente web HTML/Bootstrap puro (Vanilla JS)** que consume la propia API para brindar una interfaz gráfica sencilla y funcional desde el mismo puerto `8080`.

## 🛠️ Tecnologías Utilizadas

- **Java 17** (o superior)
- **Spring Boot 3.3.4** (Web, Data JPA, Validation, Security)
- **Spring Security 6** (con JSON Web Tokens - JWT)
- **MySQL 8** (Containerizado en Docker)
- **Flyway** (Migraciones y control de versiones de Base de Datos)
- **Bootstrap 5 & Vanilla JS** (Vistas ligeras de frontend)
- **Maven** (Gestor de dependencias)

---

## 🚀 Cómo Levantar el Proyecto

### 1. Iniciar la Base de Datos (MySQL)
Debes tener Docker instalado. Abre una terminal en la raíz de este proyecto y ejecuta:

```bash
docker compose up -d
```
*(Esto levantará el contenedor `forumhub_db` en el puerto `3306` usando la red `forumhub-network`)*

### 2. Iniciar la Aplicación (Spring Boot)
Una vez que el Docker esté listo, ejecuta la aplicación desde tu terminal usando Maven:

```bash
mvn spring-boot:run
```
*(O ejecuta la clase principal `ChallengeForoHubApplication.java` directamente en tu IDE).*

Cuando el servidor arranque, Flyway creará automáticamente las tablas necesarias y registrará al usuario administrador.

### 3. Detener la Aplicación y Base de Datos
Para detener la aplicación de Spring Boot en la terminal, presiona `Ctrl + C`. Si el puerto se queda "pegado" en uso, puedes matarlo con `kill -9 $(lsof -t -i:8080)`.

Para apagar la base de datos MySQL y limpiar la terminal, ejecuta:
```bash
docker compose down
```

---

## 🖥️ Manual de Uso: Interfaz Gráfica

Una vez que la aplicación esté corriendo, nuestra interfaz gráfica web te permitirá gestionar el foro sin necesidad de usar herramientas externas como Postman.

Ve a tu navegador web favorito y entra a:
👉 **[http://localhost:8080](http://localhost:8080)**

### Credenciales de Acceso
El sistema viene con una cuenta administradora pre-cargada:
- **Usuario:** `admin`
- **Contraseña:** `123456`

### Flujo Web
1. **Login:** Al ingresar, el sistema guardará de forma segura el token JWT en el navegador local (`localStorage`).
2. **Dashboard:** Tras el inicio de sesión exitoso, verás la lista de tópicos. Los botones te permiten regresar al formulario de edición o eliminar el registro.
3. **Formulario:** El botón "Nuevo Tópico" te abrirá un formulario validado para crear tus dudas y guardarlas en la base de datos.

---

## 🔌 API Endpoints
Si prefieres interactuar directamente o crear tu propio Frontend en el futuro, los endpoints disponibles bajo la protección JWT son:

| Método | Endpoint | Descripción | Body/Params |
|:---:|:---|:---|:---|
| **POST** | `/login` | Autentica al usuario y devuelve el JWT | JSON: `{"login", "clave"}` |
| **GET** | `/topicos` | Lista los tópicos con paginación | Query: `?page=0&size=10` |
| **GET** | `/topicos/{id}` | Muestra el detalle de un tópico específico| URL Var: `id` numérico |
| **POST** | `/topicos` | Crea un nuevo tópico | JSON: `{"titulo","mensaje","autor","curso"}`|
| **PUT** | `/topicos/{id}` | Actualiza el título y/o mensaje de un tópico | JSON: `{"titulo","mensaje"}` |
| **DELETE** | `/topicos/{id}` | Borra el tópico y lo saca de la base de datos | URL Var: `id` numérico |

*Nota: Todas las rutas `/topicos` requieren el header: `Authorization: Bearer <tu_token_jwt_aqui>`*

<br>
<br>

<p align="center">
  <img src="https://img.shields.io/badge/Hecho%20con-🚀%20Spring%20Boot-green" alt="spring-boot">
  <img src="https://img.shields.io/badge/Status-Finalizado-blue" alt="status">
</p>
