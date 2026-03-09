# Literalura - Instant Book Catalog

Literalura is a terminal-based application built with **Java 17+** and **Spring Boot**. It is designed to search, catalog, and organize information about books and authors. The application consumes real-time data from the [Gutendex API](https://gutendex.com/books/) and stores it locally using an **in-memory H2 database** with **Spring Data JPA**.

![Java](https://img.shields.io/badge/Java-17+-orange) ![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2.4-green) ![H2 Database](https://img.shields.io/badge/H2_Database-In_Memory-blue) ![License](https://img.shields.io/badge/License-MIT-blueviolet)

---

## Features

*   **Interactive Search**: Find books by title by querying the public Gutendex API.
*   **Automatic Persistence**: Automatically saves books and their authors in the database to prevent redundant network requests.
*   **Catalog Management**:
    *   View all registered books.
    *   List all stored authors in the system.
*   **Targeted Filtering**:
    *   Search for authors who were alive in a specific year.
    *   Retrieve the total number of books registered in a particular language (e.g., English, Spanish, French, Portuguese).
*   **Robust Error Handling**: Fail-safe interface featuring comprehensive JSON parsing (via Jackson) and console exception management.

---

## Architecture and Data Flow

The following details the application's internal and external communication mechanisms.

The architectural diagram below illustrates the complete flow, starting from user interaction with the console to database persistence.

<p align="center">
  <img src="workflow.png" alt="Literalura Architecture Diagram" />
</p>

### Core Components

1.  **Console UI**: Serves as the interactive bridge, collecting options and input from the user.
2.  **LibraryService**: The core application logic. It coordinates existing record validation, orchestrates `GutendexService` queries, and commands storage operations via repository interfaces.
3.  **GutendexService**: Utilizes a configured `HttpClient` to perform `GET` requests to the external Gutendex API. It deserializes the JSON response into Java Records using the Jackson library.
4.  **JPA Repositories (AuthorRepository / BookRepository)**: Spring Data JPA interfaces that directly manage relational data within the local H2 database.

---

## Technologies and Dependencies

*   **Java 17**: Core programming language.
*   **Spring Boot (3.2.4)**: Base framework, utilizing `spring-boot-starter` and `spring-boot-starter-data-jpa`.
*   **H2 Database**: Ultra-lightweight in-memory relational database. (Bootstraps automatically at `jdbc:h2:mem:literalura`).
*   **Jackson**: Direct mapping of external JSON responses to Java Records.
*   **JUnit 5 & Mockito**: Comprehensive testing ecosystem for unit and integration testing.

---

## Installation and Setup

To run this project locally, ensure you have Java 17 (or higher) and Maven installed.

1.  **Clone the repository** and navigate to the project directory:
    ```bash
    git clone <repository-url>
    cd literalura
    ```

2.  **Compile and download dependencies**:
    ```bash
    mvn clean compile
    ```

3.  **Execute automated tests** to verify system health:
    ```bash
    mvn test
    ```

4.  **Run the application**:
    ```bash
    mvn spring-boot:run
    ```

The console menu will launch automatically and prompt for numbered command inputs.

---

## License and Authorship

Alura Challenge Literalura - Created as an educational project focused on backend architecture and API consumption using Java.
