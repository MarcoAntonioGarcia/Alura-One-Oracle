package com.alura.literalura.repository;

import com.alura.literalura.model.Author;
import com.alura.literalura.model.AuthorData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class AuthorRepositoryTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void findByName_ReturnsAuthorWhenFound() {
        // Arrange
        AuthorData authorData = new AuthorData("Poe, Edgar Allan", 1809, 1849);
        Author author = new Author(authorData);

        entityManager.persist(author);
        entityManager.flush();

        // Act
        Optional<Author> foundAuthor = authorRepository.findByName("Poe, Edgar Allan");

        // Assert
        assertTrue(foundAuthor.isPresent());
        assertEquals("Poe, Edgar Allan", foundAuthor.get().getName());
    }

    @Test
    void findAliveAuthorsInYear_ReturnsCorrectAuthors() {
        // Arrange
        Author author1 = new Author(new AuthorData("Author Past", 1800, 1850)); // Dead before 1900
        Author author2 = new Author(new AuthorData("Author Alive", 1850, 1950)); // Alive in 1900
        Author author3 = new Author(new AuthorData("Author Future", 1910, 1980)); // Born after 1900
        Author author4 = new Author(new AuthorData("Author Still Alive", 1880, null)); // Alive in 1900, still alive

        entityManager.persist(author1);
        entityManager.persist(author2);
        entityManager.persist(author3);
        entityManager.persist(author4);
        entityManager.flush();

        // Act
        List<Author> aliveIn1900 = authorRepository.findAliveAuthorsInYear(1900);

        // Assert
        assertEquals(2, aliveIn1900.size());
        assertTrue(aliveIn1900.stream().anyMatch(a -> a.getName().equals("Author Alive")));
        assertTrue(aliveIn1900.stream().anyMatch(a -> a.getName().equals("Author Still Alive")));
    }
}
