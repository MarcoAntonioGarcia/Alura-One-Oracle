# Literalura - Catálogo de Libros al Instante 📚

Literalura es una aplicación de terminal desarrollada en **Java 17+** y **Spring Boot**, diseñada para buscar, catalogar y organizar información sobre libros y autores. La aplicación consume datos en tiempo real de la [API de Gutendex](https://gutendex.com/books/) y los almacena localmente utilizando una base de datos **H2 en memoria** con **Spring Data JPA**.

![Java](https://img.shields.io/badge/Java-17+-orange) ![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2.4-green) ![H2 Database](https://img.shields.io/badge/H2_Database-In_Memory-blue) ![License](https://img.shields.io/badge/License-MIT-blueviolet)

---

## 🚀 Características Principales

*   **Búsqueda interactiva**: Encuentra libros por su título consultando la API pública de Gutendex.
*   **Persistencia automática**: Guarda automáticamente los libros y sus autores en la base de datos para no tener que consultarlos a internet repetidamente.
*   **Gestión de Catálogo**: 
    *   Visualiza todos los libros registrados hasta el momento.
    *   Lista todos los autores almacenados en tu sistema.
*   **Filtros Inteligentes**:
    *   Busca autores que estaban vivos en un año específico.
    *   Obtén la cantidad total de libros registrados en un idioma en particular (Español, Inglés, Francés, Portugués, etc.).
*   **Gestión de Errores Robustos**: Interfaz a prueba de fallos mediante el manejo cuidadoso de JSON (Jackson) y excepciones en consola.

---

## 🏗️ Arquitectura y Flujo de Datos

A continuación, se detalla cómo se comunica la aplicación tanto interna como externamente. 

El siguiente diagrama ilustra el flujo completo desde que el usuario elige interactuar con la consola hasta el guardado en base de datos.

<p align="center">
  <img src="workflow.png" alt="Diagrama de Arquitectura de Literalura" />
</p>

### Componentes Principales:
1.  **Principal (Console UI)**: Actúa como el puente interactivo con el usuario recogiendo opciones.
2.  **LibraryService**: Es el cerebro aplicativo. Coordina la validación de registros existentes, organiza búsquedas en GutendexService y comanda el almacenamiento en los repositorios de bases de datos.
3.  **GutendexService**: Contiene el `HttpClient` configurado para hacer peticiones mediante GET a la API externa de Gutendex y serializar la respuesta (Record Structs) usando la librería Jackson.
4.  **Repositorios JPA (AuthorRepository / BookRepository)**: Interfaces autogestionadas por Spring Data Hibernate para manipular directamente los datos relacionales en la base de H2 local.

---

## 🛠️ Tecnologías y Dependencias

*   **Java 17**: Core del lenguaje.
*   **Spring Boot (3.2.4)**: Framework base incluyendo `Spring Boot Starter` y `Spring Boot Starter Data JPA`.
*   **H2 Database**: Base de datos relacional ultraligera en memoria. (Inicia automáticamente en `jdbc:h2:mem:literalura`).
*   **Jackson Databind / Annotations**: Mapeo directo de JSONs externos a Records de Java.
*   **JUnit 5 y Mockito**: Ecosistema completo de Pruebas Unitarias, de Integración y End-To-End.

---

## 📦 Instalación y Ejecución

Para ejecutar este proyecto de manera local, asegúrate de tener instalado Java 17 (o superior) y el gestor de dependencias Maven.

1.  **Clona este repositorio**.
2.  **Abre una terminal** en la carpeta principal del proyecto (donde reside el archivo `pom.xml`).
3.  **Compila y descarga las dependencias**:
    ```bash
    mvn clean compile
    ```
4.  **Ejecuta las pruebas automatizadas** para corroborar la salud del sistema:
    ```bash
    mvn test
    ```
5.  **Inicia la aplicación**:
    ```bash
    mvn spring-boot:run
    ```

El menú de consola se desplegará automáticamente y estará listo para recibir comandos numerados.

---

### Detalles del Equipo / Autor
Alura Challenge Literalura - Creado para el aprendizaje en arquitectura y consumo de APIs de Backend usando Java.
