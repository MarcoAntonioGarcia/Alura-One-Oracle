package com.alura.literalura.principal;

import com.alura.literalura.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.InputMismatchException;
import java.util.Scanner;

@Component
public class Principal implements CommandLineRunner {

    private final LibraryService libraryService;
    private final Scanner scanner;

    @Autowired
    public Principal(LibraryService libraryService) {
        this.libraryService = libraryService;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void run(String... args) throws Exception {
        showMenu();
    }

    public void showMenu() {
        int option = -1;
        while (option != 0) {
            System.out.println("----------------------------------------");
            System.out.println("Elija la opción a través de su número:");
            System.out.println("1 - Buscar libro por título");
            System.out.println("2 - Listar libros registrados");
            System.out.println("3 - Listar autores registrados");
            System.out.println("4 - Listar autores vivos en un determinado año");
            System.out.println("5 - Listar libros por idioma");
            System.out.println("0 - Salir");
            System.out.println("----------------------------------------");
            System.out.print("Opción: ");

            try {
                option = scanner.nextInt();
                scanner.nextLine(); // Consumir nueva línea

                switch (option) {
                    case 1:
                        searchBookByTitle();
                        break;
                    case 2:
                        listAllBooks();
                        break;
                    case 3:
                        listAllAuthors();
                        break;
                    case 4:
                        listAliveAuthorsInYear();
                        break;
                    case 5:
                        listBooksByLanguage();
                        break;
                    case 0:
                        System.out.println("Cerrando la aplicación...");
                        break;
                    default:
                        System.out.println("Opción inválida.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, ingrese un número.");
                scanner.nextLine(); // Limpiar el buffer
            } catch (Exception e) {
                System.out.println("Ocurrió un error inesperado al procesar la opción: " + e.getMessage());
            }
        }
    }

    private void searchBookByTitle() {
        System.out.print("Ingrese el nombre del libro que desea buscar: ");
        String title = scanner.nextLine();
        libraryService.searchAndSaveBook(title);
    }

    private void listAllBooks() {
        libraryService.listAllBooks();
    }

    private void listAllAuthors() {
        libraryService.listAllAuthors();
    }

    private void listAliveAuthorsInYear() {
        System.out.print("Ingrese el año a consultar: ");
        try {
            int year = scanner.nextInt();
            scanner.nextLine();
            libraryService.listAliveAuthorsInYear(year);
        } catch (InputMismatchException e) {
            System.out.println("Año inválido. Debe ingresar un número.");
            scanner.nextLine();
        }
    }

    private void listBooksByLanguage() {
        System.out.println("Ingrese el idioma para buscar los libros:");
        System.out.println("es - español");
        System.out.println("en - inglés");
        System.out.println("fr - francés");
        System.out.println("pt - portugués");
        System.out.print("Idioma: ");
        String language = scanner.nextLine();
        libraryService.countBooksByLanguage(language);
    }
}
