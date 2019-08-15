package com.learning.dto;

import java.sql.Date;

public class UserIssuedBook {

    private int bookIssueId;
    private int bookId;
    private String title;
    private String author;
    private String genre;
    private Date issueDate;
    private Date expectedReturnDate;
    private Date actualReturnDate;
    private Date reIssueDate;
    private int fine;
    private boolean fineCleared;
    private boolean returned;

    public UserIssuedBook() {
    }

    public int getBookIssueId() {
        return bookIssueId;
    }

    public void setBookIssueId(int bookIssueId) {
        this.bookIssueId = bookIssueId;
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

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public Date getExpectedReturnDate() {
        return expectedReturnDate;
    }

    public void setExpectedReturnDate(Date expectedReturnDate) {
        this.expectedReturnDate = expectedReturnDate;
    }

    public Date getActualReturnDate() {
        return actualReturnDate;
    }

    public void setActualReturnDate(Date actualReturnDate) {
        this.actualReturnDate = actualReturnDate;
    }

    public Date getReIssueDate() {
        return reIssueDate;
    }

    public void setReIssueDate(Date reIssueDate) {
        this.reIssueDate = reIssueDate;
    }

    public int getFine() {
        return fine;
    }

    public void setFine(int fine) {
        this.fine = fine;
    }

    public boolean isFineCleared() {
        return fineCleared;
    }

    public void setFineCleared(boolean fineCleared) {
        this.fineCleared = fineCleared;
    }

    public boolean isReturned() {
        return returned;
    }

    public void setReturned(boolean returned) {
        this.returned = returned;
    }
}
