package com.alura.conversor.service;

import com.alura.conversor.model.ConversionRate;
import com.google.gson.Gson;

/**
 * Encargada unicamente de la lógica matemática de conversión y parseo de JSON
 */
public class CurrencyConverter {

    private final Gson gson;

    public CurrencyConverter() {
        this.gson = new Gson();
    }

    /**
     * Convierte el JSON recibido por la API en un objeto ConversionRate
     * y retorna el valor de la moneda objetivo que equivale a $1 de la moneda base.
     * 
     * @param jsonResponse Respuesta JSON de la API de ExchangeRate
     * @return ConversionRate mapeado automáticamente con Gson
     */
    public ConversionRate parseConversionData(String jsonResponse) {
        return gson.fromJson(jsonResponse, ConversionRate.class);
    }

    /**
     * Calcula la conversión de una cantidad específica en base a la tasa
     * 
     * @param jsonResponse Respuesta JSON de la API
     * @param amount       Cantidad a convertir
     * @return Monto final convertido
     */
    public double convert(String jsonResponse, double amount) {
        ConversionRate data = parseConversionData(jsonResponse);
        double conversionRate = data.conversion_rate();
        return amount * conversionRate;
    }
}
