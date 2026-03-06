package com.alura.conversor.model;

/**
 * Record representing the JSON response from ExchangeRate-API.
 * Gson will automatically map the fields to this record.
 */
public record ConversionRate(
        String result,
        String documentation,
        String terms_of_use,
        long time_last_update_unix,
        String time_last_update_utc,
        long time_next_update_unix,
        String time_next_update_utc,
        String base_code,
        String target_code,
        double conversion_rate,
        double conversion_result) {
}
