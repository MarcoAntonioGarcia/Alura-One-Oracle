package com.alura.literalura.model;

import jakarta.persistence.*;

@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String title;

    private String language;

    private Double downloads;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

    public Book() {
    }

    public Book(BookData bookData) {
        this.title = bookData.title();
        if (bookData.languages() != null && !bookData.languages().isEmpty()) {
            this.language = bookData.languages().get(0);
        }
        this.downloads = bookData.downloadCount();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Double getDownloads() {
        return downloads;
    }

    public void setDownloads(Double downloads) {
        this.downloads = downloads;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    @Override
    public String toString() {
        String authorName = (author != null) ? author.getName() : "Desconocido";
        return "---------- LIBRO ----------\n" +
                "Título: " + title + "\n" +
                "Autor: " + authorName + "\n" +
                "Idioma: " + language + "\n" +
                "Descargas: " + downloads + "\n" +
                "---------------------------";
    }
}
