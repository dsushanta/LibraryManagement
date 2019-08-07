package com.learning.dto;

import java.util.ArrayList;
import java.util.List;

public class Book {

    private int bookId;
    private String title;
    private String description;
    private String author;
    private String genre;
    private int likes;
    private boolean available;
    private List<Link> links = new ArrayList<>();

    public Book() {
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public void addLink(String url, String rel) {
        Link l = new Link();
        l.setLink(url);
        l.setRel(rel);
        links.add(l);
    }

    @Override
    public String toString() {
        return "Book{" +
                "bookId=" + bookId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", author='" + author + '\'' +
                ", genre='" + genre + '\'' +
                ", likes=" + likes +
                ", available=" + available +
                ", links=" + links +
                '}';
    }
}
