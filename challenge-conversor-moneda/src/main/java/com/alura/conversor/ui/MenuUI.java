package com.alura.conversor.ui;

import com.alura.conversor.client.ExchangeClient;
import com.alura.conversor.model.ConversionRate;
import com.alura.conversor.service.CurrencyConverter;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Encargada exclusivamente del Scanner y las impresiones en consola.
 */
public class MenuUI {

    private final Scanner scanner;
    private final ExchangeClient exchangeClient;
    private final CurrencyConverter currencyConverter;

    public MenuUI() {
        this.scanner = new Scanner(System.in);
        this.exchangeClient = new ExchangeClient();
        this.currencyConverter = new CurrencyConverter();
    }

    public void displayMenu() {
        int option = -1;

        while (option != 7) {
            System.out.println("*********************************************************");
            System.out.println("Sea bienvenido/a al Conversor de Monedas =]");
            System.out.println();
            System.out.println("1) Dólar (USD)           => Peso argentino (ARS)");
            System.out.println("2) Peso argentino (ARS)  => Dólar (USD)");
            System.out.println("3) Dólar (USD)           => Real brasileño (BRL)");
            System.out.println("4) Real brasileño (BRL)  => Dólar (USD)");
            System.out.println("5) Dólar (USD)           => Peso colombiano (COP)");
            System.out.println("6) Peso colombiano (COP) => Dólar (USD)");
            System.out.println("7) Salir");
            System.out.println("Elija una opción válida:");
            System.out.println("*********************************************************");

            try {
                option = scanner.nextInt();

                switch (option) {
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
                System.out.println("Ocurrió un error inesperado: " + e.getMessage() + "\n");
            }
        }

        scanner.close();
    }

    private void processConversion(String baseCurrency, String targetCurrency) {
        System.out.println("Ingrese el valor que deseas convertir:");

        try {
            double amount = scanner.nextDouble();

            System.out.println("Obteniendo tasas de cambio...");
            String jsonResponse = exchangeClient.fetchConversionData(baseCurrency, targetCurrency);

            ConversionRate rateData = currencyConverter.parseConversionData(jsonResponse);
            double convertedAmount = amount * rateData.conversion_rate();

            System.out.printf("El valor %.2f [%s] corresponde al valor final de <=> %.2f [%s]\n\n",
                    amount, baseCurrency, convertedAmount, targetCurrency);

        } catch (InputMismatchException e) {
            System.out.println("Error: Tipo de valor inválido. Se esperaba un número Decimal/Entero.\n");
            scanner.nextLine(); // Limpiar el buffer
        } catch (RuntimeException e) {
            System.out.println(e.getMessage() + "\n");
        }
    }
}
