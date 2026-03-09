# Currency Converter

[![Java Version](https://img.shields.io/badge/Java-17%2B-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
[![Maven](https://img.shields.io/badge/Maven-3.8%2B-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)](https://maven.apache.org/)

A robust, terminal-based Java application that provides real-time currency conversion using live exchange rates. This project was developed as part of the Alura ONE (Oracle Next Education) challenge, demonstrating practical applications of Object-Oriented Programming, API integration, and JSON parsing in Java.

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Architecture](#architecture)
- [Prerequisites](#prerequisites)
- [Installation and Execution](#installation-and-execution)
- [Technologies](#technologies)
- [Author](#author)

## Overview

The Currency Converter retrieves current exchange rates via the [ExchangeRate-API](https://www.exchangerate-api.com/) and provides accurate, up-to-the-minute conversions. The application is designed with a clean architecture, ensuring strict separation of concerns between external HTTP communication, data mapping, and the user interface.

## Features

- **Interactive CLI Menu:** User-friendly terminal interface for seamless navigation.
- **Real-Time Exchange Rates:** Fetches live data from external services.
- **Robust Error Handling:** Validates user inputs and handles API timeouts or parsing errors gracefully.
- **Supported Conversions:**
  - USD (US Dollar) ↔ ARS (Argentine Peso)
  - USD (US Dollar) ↔ BRL (Brazilian Real)
  - USD (US Dollar) ↔ COP (Colombian Peso)
  - *Extensible design to support additional currency pairs.*

## Architecture

The application is structured to follow strict Object-Oriented Programming (OOP) principles:

- **`Principal`**: The application entry point, responsible solely for bootstrapping the components.
- **`MenuUI`**: Manages terminal input/output and user interactions.
- **`ExchangeClient`**: Handles HTTP communication with the ExchangeRate-API via `java.net.http.HttpClient`.
- **`CurrencyConverter`**: Encapsulates business logic, data transformation, and JSON mapping using Records.

## Prerequisites

Before you begin, ensure you have the following installed on your local machine:

- [Java JDK 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) (or higher)
- [Apache Maven](https://maven.apache.org/download.cgi)
- *Optional:* A free API key from [ExchangeRate-API](https://www.exchangerate-api.com/). (A default testing environment is provided).

## Installation and Execution

### Method 1: Using the Terminal (Maven)

1. Clone the repository and/or navigate to the project directory:
   ```bash
   cd challenge-conversor-moneda
   ```

2. Clean, compile, and run the application directly via Maven:
   ```bash
   mvn clean compile exec:java -Dexec.mainClass="com.alura.conversor.Principal"
   ```

### Method 2: Using an IDE

1. Open your preferred Java IDE (IntelliJ IDEA, Eclipse, or VS Code).
2. Import the root directory as a **Maven Project**.
3. Allow the IDE to resolve dependencies defined in the `pom.xml`.
4. Navigate to `src/main/java/com/alura/conversor/Principal.java`.
5. Run the `Principal` class.

## Technologies

- **Java 17**: Core language implementing HTTP clients and structural Records.
- **Maven**: Build automation and dependency management.
- **Gson (Google)**: High-performance JSON serialization and deserialization.
- **ExchangeRate-API**: External provider for accurate currency market data.

## Author

Developed as a programmatic challenge for **Alura - Oracle Next Education**.
