package com.alura.conversor;

import com.alura.conversor.ui.MenuUI;

/**
 * Punto de entrada principal para el Conversor de Monedas.
 * Esta clase no contiene lógica de negocio, solo se encarga de iniciar el menú
 * interactivo.
 */
public class Principal {

    public static void main(String[] args) {
        MenuUI ui = new MenuUI();
        ui.displayMenu();
    }
}
