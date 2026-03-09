# Foro Hub API & Web

ForumHub is a forum platform (Backend + Frontend integrated) where students can register, query, update, and delete their questions (topics).

The system consists of a RESTful API built with Java and Spring Boot 3, backed by a MySQL 8 database managed via Docker. It also features a pure HTML/Bootstrap (Vanilla JS) web client that consumes the API to provide a simple and functional graphical interface served from the same `8080` port.

## Technologies Used

- **Java 17** (or higher)
- **Spring Boot 3.3.4** (Web, Data JPA, Validation, Security)
- **Spring Security 6** (with JSON Web Tokens - JWT)
- **MySQL 8** (Docker containerized)
- **Flyway** (Database migrations and version control)
- **Bootstrap 5 & Vanilla JS** (Lightweight frontend views)
- **Maven** (Dependency management)

---

## Getting Started

### 1. Start the Database (MySQL)
Ensure Docker is installed. Open a terminal in the root directory of this project and execute:

```bash
docker compose up -d
```
*(This will start the `forumhub_db` container on port `3306` using the `forumhub-network` network)*

### 2. Start the Application (Spring Boot)
Once the Docker container is running, execute the application from your terminal using Maven:

```bash
mvn spring-boot:run
```
*(Alternatively, run the main class `ChallengeForoHubApplication.java` directly in your IDE).*

When the server starts, Flyway will automatically create the necessary tables and register the administrator user.

### 3. Stop the Application and Database
To stop the Spring Boot application in the terminal, press `Ctrl + C`. If the port remains in use, you can terminate the process with `kill -9 $(lsof -t -i:8080)`.

To shut down the MySQL database and clean up the terminal, execute:
```bash
docker compose down
```

---

## User Manual: Graphical Interface

Once the application is running, the graphical web interface allows you to manage the forum without needing external tools like Postman.

Open your preferred web browser and navigate to:
**[http://localhost:8080](http://localhost:8080)**

### Access Credentials
The system includes a pre-loaded administrator account:
- **Username:** `admin`
- **Password:** `123456`

### Web Workflow
1. **Login:** Upon entry, the system will securely store the JWT token in the local browser storage (`localStorage`).
2. **Dashboard:** After a successful login, you will see the list of topics. The available buttons allow you to return to the edit form or delete the record.
3. **Form:** The "New Topic" button will open a validated form to create your questions and save them to the database.

---

## API Endpoints
If you prefer to interact directly or create your own frontend in the future, the available endpoints under JWT protection are:

| Method | Endpoint | Description | Body/Params |
|:---:|:---|:---|:---|
| **POST** | `/login` | Authenticates the user and returns the JWT | JSON: `{"login", "clave"}` |
| **GET** | `/topicos` | Lists topics with pagination | Query: `?page=0&size=10` |
| **GET** | `/topicos/{id}` | Displays the details of a specific topic | URL Var: numeric `id` |
| **POST** | `/topicos` | Creates a new topic | JSON: `{"titulo","mensaje","autor","curso"}`|
| **PUT** | `/topicos/{id}` | Updates the title and/or message of a topic | JSON: `{"titulo","mensaje"}` |
| **DELETE** | `/topicos/{id}` | Deletes the topic from the database | URL Var: numeric `id` |

*Note: All `/topicos` routes require the following header: `Authorization: Bearer <your_jwt_token_here>`*

<br>
<br>

<p align="center">
  <img src="https://img.shields.io/badge/Built%20with-Spring%20Boot-green" alt="spring-boot">
  <img src="https://img.shields.io/badge/Status-Completed-blue" alt="status">
</p>
