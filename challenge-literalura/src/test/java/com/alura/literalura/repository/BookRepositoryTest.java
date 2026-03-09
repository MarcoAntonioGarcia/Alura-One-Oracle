package com.alura.literalura.repository;

import com.alura.literalura.model.Author;
import com.alura.literalura.model.AuthorData;
import com.alura.literalura.model.Book;
import com.alura.literalura.model.BookData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void findByTitleIgnoreCase_ReturnsBookWhenFound() {
        // Arrange
        AuthorData authorData = new AuthorData("Cervantes, Miguel de", 1547, 1616);
        BookData bookData = new BookData("Don Quijote de la Mancha", List.of(authorData), List.of("es"), 1500.0);

        Author author = new Author(authorData);
        Book book = new Book(bookData);
        author.addBook(book);

        // Persist via EntityManager
        entityManager.persist(author); // Book will be cascaded
        entityManager.flush();

        // Act
        Optional<Book> foundBook = bookRepository.findByTitleIgnoreCase("don quijote DE LA MANCHA");

        // Assert
        assertTrue(foundBook.isPresent());
        assertEquals("Don Quijote de la Mancha", foundBook.get().getTitle());
    }

    @Test
    void countByLanguage_ReturnsCorrectCount() {
        // Arrange
        AuthorData authorData1 = new AuthorData("Author Es", 1900, 2000);
        BookData bookData1 = new BookData("Libro Castellano", List.of(authorData1), List.of("es"), 50.0);
        Author author1 = new Author(authorData1);
        author1.addBook(new Book(bookData1));
        entityManager.persist(author1);

        AuthorData authorData2 = new AuthorData("Author En", 1900, 2000);
        BookData bookData2 = new BookData("English Book", List.of(authorData2), List.of("en"), 60.0);
        Author author2 = new Author(authorData2);
        author2.addBook(new Book(bookData2));
        entityManager.persist(author2);

        entityManager.flush();

        // Act
        int esCount = bookRepository.countByLanguage("es");
        int enCount = bookRepository.countByLanguage("en");
        int frCount = bookRepository.countByLanguage("fr");

        // Assert
        assertEquals(1, esCount);
        assertEquals(1, enCount);
        assertEquals(0, frCount);
    }
}
