package com.bravo.johny.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@NoArgsConstructor
@ToString
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

    public UserIssuedBook(int bookIssueId, int bookId, String title, String author, String genre,
                          Date issueDate, Date expectedReturnDate, Date actualReturnDate,
                          Date reIssueDate, int fine, boolean fineCleared) {
        this.bookIssueId = bookIssueId;
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.issueDate = issueDate;
        this.expectedReturnDate = expectedReturnDate;
        this.actualReturnDate = actualReturnDate;
        this.reIssueDate = reIssueDate;
        this.fine = fine;
        this.fineCleared = fineCleared;
    }
}
