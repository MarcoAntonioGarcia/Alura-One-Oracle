package com.alura.literalura.service;

import com.alura.literalura.model.*;
import com.alura.literalura.repository.AuthorRepository;
import com.alura.literalura.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LibraryService {

    private final GutendexService gutendexService;
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    @Autowired
    public LibraryService(GutendexService gutendexService, AuthorRepository authorRepository,
            BookRepository bookRepository) {
        this.gutendexService = gutendexService;
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    public void searchAndSaveBook(String title) {
        ApiResponse apiResponse = gutendexService.searchBooksByTitle(title);

        if (apiResponse == null || apiResponse.results() == null || apiResponse.results().isEmpty()) {
            System.out.println("No se encontraron resultados para el libro buscado.");
            return;
        }

        try {
            BookData firstBookData = apiResponse.results().get(0);

            // Validate author
            if (firstBookData.authors() == null || firstBookData.authors().isEmpty()) {
                System.out.println("El libro encontrado no cuenta con información de autores. No se puede guardar.");
                return;
            }

            AuthorData firstAuthorData = firstBookData.authors().get(0);

            // Check if book already exists
            Optional<Book> existingBook = bookRepository.findByTitleIgnoreCase(firstBookData.title());
            if (existingBook.isPresent()) {
                System.out.println("El libro ya se encuentra registrado en la base de datos.");
                System.out.println(existingBook.get());
                return;
            }

            // Deal with author logic
            Author author;
            Optional<Author> existingAuthor = authorRepository.findByName(firstAuthorData.name());

            if (existingAuthor.isPresent()) {
                author = existingAuthor.get();
            } else {
                author = new Author(firstAuthorData);
                // Saving is optional here if cascade is on and we save from author, but let's
                // save author first explicitly
                author = authorRepository.save(author);
            }

            Book book = new Book(firstBookData);
            // Bidirectional add
            author.addBook(book);

            // Let JPA persist the book due to cascade or direct save.
            authorRepository.save(author); // Since author is managed, saving it cascades down

            // To display correctly
            Book savedBook = bookRepository.findByTitleIgnoreCase(book.getTitle()).orElse(book);

            System.out.println("Libro guardado exitosamente:");
            System.out.println(savedBook);

        } catch (IndexOutOfBoundsException e) {
            System.out.println("Error procesando los datos devueltos por la API (listas vacías o con error).");
        } catch (NullPointerException e) {
            System.out.println("Error procesando los datos: se encontró un valor nulo inesperado.");
        } catch (Exception e) {
            System.out.println("Error inesperado al intentar guardar el libro: " + e.getMessage());
        }
    }

    public void listAllBooks() {
        List<Book> books = bookRepository.findAll();
        if (books.isEmpty()) {
            System.out.println("Aún no hay libros registrados en la base de datos.");
        } else {
            books.forEach(System.out::println);
        }
    }

    public void listAllAuthors() {
        List<Author> authors = authorRepository.findAll();
        if (authors.isEmpty()) {
            System.out.println("Aún no hay autores registrados en la base de datos.");
        } else {
            authors.forEach(System.out::println);
        }
    }

    public void listAliveAuthorsInYear(Integer year) {
        List<Author> authors = authorRepository.findAliveAuthorsInYear(year);
        if (authors.isEmpty()) {
            System.out.println("No se encontraron autores vivos en ese año registrados en la base de datos.");
        } else {
            System.out.println("--- Autores vivos en el año " + year + " ---");
            authors.forEach(System.out::println);
        }
    }

    public void countBooksByLanguage(String language) {
        int count = bookRepository.countByLanguage(language);
        System.out.println("Existen " + count + " libros en el idioma '" + language + "' en la base de datos.");
    }
}
