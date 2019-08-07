package com.learning.dto;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class BookIssue {

    private int bookIssueId;
    private int copyId;
    private int bookId;
    private String userName;
    private boolean isReturned;
    private boolean isReissued;
    private Date issueDate;
    private Date expectedReturnDate;
    private Date actualReturnDate;
    private Date reIssueDate;
    private int fine;
    private List<Link> links = new ArrayList<>();
    private BookLifeCycleOperation operation;

    public int getBookIssueId() {
        return bookIssueId;
    }

    public void setBookIssueId(int bookIssueId) {
        this.bookIssueId = bookIssueId;
    }

    public int getCopyId() {
        return copyId;
    }

    public void setCopyId(int copyId) {
        this.copyId = copyId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isReturned() {
        return isReturned;
    }

    public void setReturned(boolean returned) {
        isReturned = returned;
    }

    public boolean isReissued() {
        return isReissued;
    }

    public void setReissued(boolean reissued) {
        isReissued = reissued;
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

    public BookLifeCycleOperation getOperation() {
        return operation;
    }

    public void setOperation(BookLifeCycleOperation operation) {
        this.operation = operation;
    }

    public void addLink(String url, String rel) {
        Link l = new Link();
        l.setLink(url);
        l.setRel(rel);
        links.add(l);
    }

    @Override
    public String toString() {
        return "BookIssue{" +
                "bookIssueId=" + bookIssueId +
                ", copyId=" + copyId +
                ", bookId=" + bookId +
                ", userName=" + userName +
                ", isReturned=" + isReturned +
                ", isReissued=" + isReissued +
                ", issueDate=" + issueDate +
                ", expectedReturnDate=" + expectedReturnDate +
                ", actualReturnDate=" + actualReturnDate +
                ", reIssueDate=" + reIssueDate +
                ", fine=" + fine +
                '}';
    }
}
