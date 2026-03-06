package com.alura.conversor.service;

import com.alura.conversor.model.ConversionRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Servicio encargado de gestionar el historial de conversiones.
 */
public class HistoryService {

    private final List<ConversionRecord> historyList;

    public HistoryService() {
        this.historyList = new ArrayList<>();
    }

    /**
     * Añade un nuevo registro de conversión al historial.
     * 
     * @param record El registro a guardar.
     */
    public void addRecord(ConversionRecord record) {
        historyList.add(record);
    }

    /**
     * Muestra en consola todo el historial de conversiones realizadas en la sesión
     * actual.
     */
    public void displayHistory() {
        System.out.println("====== HISTORIAL DE CONVERSIONES ======");
        if (historyList.isEmpty()) {
            System.out.println("Aún no has realizado ninguna conversión en esta sesión.");
        } else {
            for (int i = 0; i < historyList.size(); i++) {
                System.out.println((i + 1) + ". " + historyList.get(i).toString());
            }
        }
        System.out.println("=======================================\n");
    }
}
