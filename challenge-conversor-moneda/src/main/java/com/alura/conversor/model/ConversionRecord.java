package com.alura.conversor.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Representa una única conversión realizada por el usuario.
 * Utiliza un record de Java para almacenar la información de forma inmutable.
 */
public record ConversionRecord(
        String baseCurrency,
        String targetCurrency,
        double amount,
        double convertedAmount,
        LocalDateTime timestamp) {

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formattedTime = timestamp.format(formatter);
        return String.format("[%s] %.2f %s -> %.2f %s",
                formattedTime, amount, baseCurrency, convertedAmount, targetCurrency);
    }
}
