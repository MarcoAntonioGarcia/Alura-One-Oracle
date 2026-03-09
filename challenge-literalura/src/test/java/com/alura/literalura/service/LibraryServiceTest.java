package com.alura.literalura.service;

import com.alura.literalura.model.ApiResponse;
import com.alura.literalura.model.Author;
import com.alura.literalura.model.AuthorData;
import com.alura.literalura.model.Book;
import com.alura.literalura.model.BookData;
import com.alura.literalura.repository.AuthorRepository;
import com.alura.literalura.repository.BookRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LibraryServiceTest {

    @Mock
    private GutendexService gutendexService;

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private LibraryService libraryService;

    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private final PrintStream standardOut = System.out;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    void tearDown() {
        System.setOut(standardOut);
    }

    @Test
    void searchAndSaveBook_NewBook_SavesSuccessfully() {
        // Arrange
        String title = "Frankenstein";

        AuthorData authorData = new AuthorData("Shelley, Mary", 1797, 1851);
        BookData bookData = new BookData("Frankenstein", List.of(authorData), List.of("en"), 90000.0);
        ApiResponse apiResponse = new ApiResponse(List.of(bookData));

        when(gutendexService.searchBooksByTitle(title)).thenReturn(apiResponse);
        when(bookRepository.findByTitleIgnoreCase("Frankenstein"))
                .thenReturn(Optional.empty()) // Check if exist: not found
                .thenReturn(Optional.of(new Book(bookData))); // After save: found
        when(authorRepository.findByName("Shelley, Mary")).thenReturn(Optional.empty()); // Author does not exist

        Author savedAuthor = new Author(authorData);
        when(authorRepository.save(any(Author.class))).thenReturn(savedAuthor);

        // Act
        libraryService.searchAndSaveBook(title);

        // Assert
        verify(gutendexService, times(1)).searchBooksByTitle(title);
        verify(bookRepository, times(2)).findByTitleIgnoreCase("Frankenstein"); // First to check, second to display
        verify(authorRepository, times(2)).save(any(Author.class)); // First author alone, then auth with book due to
                                                                    // cascade logic

        assertTrue(outputStreamCaptor.toString().contains("Libro guardado exitosamente:"));
    }

    @Test
    void searchAndSaveBook_BookAlreadyExists_DoesNotSave() {
        // Arrange
        String title = "Frankenstein";

        AuthorData authorData = new AuthorData("Shelley, Mary", 1797, 1851);
        BookData bookData = new BookData("Frankenstein", List.of(authorData), List.of("en"), 90000.0);
        ApiResponse apiResponse = new ApiResponse(List.of(bookData));

        Book existingBook = new Book(bookData);

        when(gutendexService.searchBooksByTitle(title)).thenReturn(apiResponse);
        when(bookRepository.findByTitleIgnoreCase("Frankenstein")).thenReturn(Optional.of(existingBook)); // Exists

        // Act
        libraryService.searchAndSaveBook(title);

        // Assert
        verify(gutendexService, times(1)).searchBooksByTitle(title);
        verify(bookRepository, times(1)).findByTitleIgnoreCase("Frankenstein");
        verify(authorRepository, never()).save(any());

        assertTrue(outputStreamCaptor.toString().contains("El libro ya se encuentra registrado en la base de datos."));
    }

    @Test
    void searchAndSaveBook_NoResults_DisplaysMessage() {
        // Arrange
        String title = "Unknown Book XYZ";
        ApiResponse apiResponse = new ApiResponse(Collections.emptyList());

        when(gutendexService.searchBooksByTitle(title)).thenReturn(apiResponse);

        // Act
        libraryService.searchAndSaveBook(title);

        // Assert
        verify(gutendexService, times(1)).searchBooksByTitle(title);
        verifyNoInteractions(bookRepository);
        verifyNoInteractions(authorRepository);

        assertTrue(
                outputStreamCaptor.toString().trim().contains("No se encontraron resultados para el libro buscado."));
    }
}
