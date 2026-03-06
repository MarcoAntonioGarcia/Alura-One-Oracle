# Challenge Conversor de Monedas

Este es un proyecto de una aplicación de consola en Java que funciona como un conversor de monedas utilizando tasas de cambio en tiempo real. Este proyecto es parte del challenge del programa Alura ONE (Oracle Next Education).

## Funcionalidades

El conversor permite realizar las siguientes conversiones de moneda a través de un menú interactivo:

1. Dólar (USD) a Peso argentino (ARS)
2. Peso argentino (ARS) a Dólar (USD)
3. Dólar (USD) a Real brasileño (BRL)
4. Real brasileño (BRL) a Dólar (USD)
5. Dólar (USD) a Peso colombiano (COP)
6. Peso colombiano (COP) a Dólar (USD)

Además, extrae el valor de la moneda ingresada y devuelve la conversión exacta basada en los datos obtenidos de la API de ExchangeRate-API.

## Tecnologías Utilizadas

- **Java 17**
- **Maven** (Gestor de dependencias)
- **Gson (Google)** para el parseo de datos JSON
- **ExchangeRate-API** para obtener las tasas de cambio actualizadas

## Requisitos Previos

- Tener instalado [Java JDK 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) o superior.
- Tener instalado [Maven](https://maven.apache.org/download.cgi).
- (Opcional) Una API Key gratuita en [ExchangeRate-API](https://www.exchangerate-api.com/). Actualmente el proyecto tiene una configurada para pruebas.

## Cómo Ejecutar el Proyecto

### Opción 1: Desde la Terminal (usando Maven)

1. Abre tu terminal o línea de comandos.
2. Navega hasta el directorio raíz del proyecto (`challenge-conversor-moneda`).
3. Ejecuta el siguiente comando para limpiar, compilar y ejecutar la aplicación:

```bash
mvn clean compile exec:java -Dexec.mainClass="com.alura.conversor.Principal"
```

### Opción 2: Usar un IDE (IntelliJ IDEA, Eclipse, VS Code)

1. Importa el proyecto como un proyecto de Maven.
2. Espera a que el IDE descargue las dependencias definidas en el archivo `pom.xml` (en este caso, Gson).
3. Navega hasta `src/main/java/com/alura/conversor/Principal.java`.
4. Ejecuta la clase `Principal` haciendo clic en "Run".

## Estructura del Código

El proyecto sigue una arquitectura con separación de responsabilidades:

- `Principal`: Punto de entrada de la aplicación.
- Componentes de interfaz (Menú interactivo).
- Cliente HTTP (`ExchangeClient`) para llamar a la API externa.
- Lógica de conversión y mapeo de JSON a Records en Java.

## Autor

Desarrollado para el Challenge de Alura - Oracle Next Education.
