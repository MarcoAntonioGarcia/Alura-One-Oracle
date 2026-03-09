package com.alura.literalura;

import com.alura.literalura.principal.Principal;
import com.alura.literalura.service.LibraryService;
import com.alura.literalura.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class LiteraluraApplicationTests {

    // We mock Principal so the command line runner doesn't block waiting for
    // scanner input
    @MockBean
    private Principal principal;

    @Autowired
    private LibraryService libraryService;

    @Autowired
    private BookRepository bookRepository;

    @Test
    void contextLoads() {
        // Just verify the context loads successfully
        assertTrue(true);
    }

    @Test
    void searchAndSaveBook_EndToEndFlow() {
        // Arrange
        String title = "Dracula";
        long initialCount = bookRepository.count();

        // Act
        // This will hit the real Gutendex API and save to the real in-memory H2 DB
        libraryService.searchAndSaveBook(title);

        // Assert
        long postCount = bookRepository.count();
        assertTrue(postCount >= initialCount);
        assertTrue(bookRepository.findByTitleIgnoreCase(title).isPresent() ||
                bookRepository.findByTitleIgnoreCase("Dracula").isPresent());
    }
}
