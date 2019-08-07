package com.learning.dto;

import java.sql.Date;

public class UserIssuedBook {

    private int bookIssueId;
    private String title;
    private String author;
    private String genre;
    private Date issueDate;
    private Date expectedReturnDate;
    private Date actualReturnDate;
    private Date reIssueDate;

    public UserIssuedBook() {
    }

    public int getBookIssueId() {
        return bookIssueId;
    }

    public void setBookIssueId(int bookIssueId) {
        this.bookIssueId = bookIssueId;
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
}
