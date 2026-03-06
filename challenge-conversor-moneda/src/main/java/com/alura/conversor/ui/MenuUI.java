package com.alura.conversor.ui;

import com.alura.conversor.client.ExchangeClient;
import com.alura.conversor.model.ConversionRate;
import com.alura.conversor.model.ConversionRecord;
import com.alura.conversor.service.CurrencyConverter;
import com.alura.conversor.service.HistoryService;

import java.time.LocalDateTime;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Encargada de interactuar con el usuario mediante la consola.
 * Maneja el flujo del menú principal, conversiones predefinidas y dinámicas,
 * y muestra el historial de transacciones.
 */
public class MenuUI {

    private final Scanner scanner;
    private final ExchangeClient exchangeClient;
    private final CurrencyConverter currencyConverter;
    private final HistoryService historyService;

    public MenuUI() {
        this.scanner = new Scanner(System.in);
        this.exchangeClient = new ExchangeClient();
        this.currencyConverter = new CurrencyConverter();
        this.historyService = new HistoryService();
    }

    public void displayMenu() {
        int option = -1;

        while (option != 4) {
            System.out.println("*********************************************************");
            System.out.println("Sea bienvenido/a al Conversor de Monedas Avanzado =]");
            System.out.println();
            System.out.println("1) Realizar conversión usando lista predefinida");
            System.out.println("2) Realizar conversión (Escribir Código de Moneda, ej: EUR, CAD)");
            System.out.println("3) Ver historial de conversiones");
            System.out.println("4) Salir");
            System.out.println("Elija una opción válida:");
            System.out.println("*********************************************************");

            try {
                option = scanner.nextInt();
                scanner.nextLine(); // Consumir nueva línea

                switch (option) {
                    case 1:
                        handlePredefinedMenu();
                        break;
                    case 2:
                        handleDynamicConversion();
                        break;
                    case 3:
                        historyService.displayHistory();
                        break;
                    case 4:
                        System.out.println("Gracias por usar el Conversor de Monedas.");
                        break;
                    default:
                        System.out.println("Opción no válida. Por favor, intente de nuevo.\n");
                        break;
                }

            } catch (InputMismatchException e) {
                System.out.println("Error: Entrada inválida. Por favor, ingrese un número.\n");
                scanner.nextLine(); // Limpiamos el buffer del scanner
            } catch (Exception e) {
                System.out.println("Ocurrió un error inesperado al navegar el menú: " + e.getMessage() + "\n");
            }
        }

        scanner.close();
    }

    private void handlePredefinedMenu() {
        int subOption = -1;
        System.out.println("=== Opciones Predefinidas ===");
        System.out.println("1) Dólar (USD)           => Peso argentino (ARS)");
        System.out.println("2) Peso argentino (ARS)  => Dólar (USD)");
        System.out.println("3) Dólar (USD)           => Real brasileño (BRL)");
        System.out.println("4) Real brasileño (BRL)  => Dólar (USD)");
        System.out.println("5) Dólar (USD)           => Peso colombiano (COP)");
        System.out.println("6) Peso colombiano (COP) => Dólar (USD)");
        System.out.println("7) Dólar (USD)           => Euro (EUR)");
        System.out.println("8) Euro (EUR)            => Dólar (USD)");
        System.out.println("9) Volver al menú principal");
        System.out.println("=============================");
        System.out.println("Elija una opción válida:");

        try {
            subOption = scanner.nextInt();
            scanner.nextLine();

            switch (subOption) {
                case 1:
                    processConversion("USD", "ARS");
                    break;
                case 2:
                    processConversion("ARS", "USD");
                    break;
                case 3:
                    processConversion("USD", "BRL");
                    break;
                case 4:
                    processConversion("BRL", "USD");
                    break;
                case 5:
                    processConversion("USD", "COP");
                    break;
                case 6:
                    processConversion("COP", "USD");
                    break;
                case 7:
                    processConversion("USD", "EUR");
                    break;
                case 8:
                    processConversion("EUR", "USD");
                    break;
                case 9:
                    break;
                default:
                    System.out.println("Opción no válida.\n");
                    break;
            }
        } catch (InputMismatchException e) {
            System.out.println("Error: Se esperaba un número.\n");
            scanner.nextLine(); // Limpiar el buffer
        }
    }

    private void handleDynamicConversion() {
        System.out.println("=== Conversión por Códigos ISO ===");
        System.out.println("Ingrese el código de la moneda base (Ej. USD, GBP, JPY):");
        String baseCurrency = scanner.nextLine().trim().toUpperCase();

        System.out.println("Ingrese el código de la moneda objetivo (Ej. EUR, MXN, CLP):");
        String targetCurrency = scanner.nextLine().trim().toUpperCase();

        if (baseCurrency.length() != 3 || targetCurrency.length() != 3) {
            System.out.println("Precaución: Los códigos de moneda habitualmente tienen 3 letras (Ej. USD).\n");
        }

        processConversion(baseCurrency, targetCurrency);
    }

    private void processConversion(String baseCurrency, String targetCurrency) {
        System.out.println("Ingrese el valor que deseas convertir:");

        try {
            double amount = scanner.nextDouble();
            scanner.nextLine(); // Limpiar buffer

            System.out.println("Obteniendo tasas de cambio de " + baseCurrency + " a " + targetCurrency + "...");
            String jsonResponse = exchangeClient.fetchConversionData(baseCurrency, targetCurrency);

            ConversionRate rateData = currencyConverter.parseConversionData(jsonResponse);
            double convertedAmount = amount * rateData.conversion_rate();

            System.out.printf("El valor %.2f [%s] corresponde al valor final de <=> %.2f [%s]\n\n",
                    amount, baseCurrency, convertedAmount, targetCurrency);

            // Se registra exitosamente en el historial usando java.time.LocalDateTime
            ConversionRecord record = new ConversionRecord(
                    baseCurrency, targetCurrency, amount, convertedAmount, LocalDateTime.now());
            historyService.addRecord(record);

        } catch (InputMismatchException e) {
            System.out.println(
                    "Error: Tipo de valor inválido. Se esperaba un número Decimal/Entero (Ej: 100.50 o 100,50 según tu región).\n");
            scanner.nextLine(); // Limpiar el buffer
        } catch (RuntimeException e) {
            System.out.println("Error en la conversión: " + e.getMessage() + "\n");
        }
    }
}
