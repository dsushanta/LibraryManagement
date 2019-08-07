package com.learning.dto;

import java.util.ArrayList;
import java.util.List;

public class Copy {

    private int copyId;
    private int bookId;
    private boolean isIssued;
    private List<Link> links = new ArrayList<>();

    public Copy() {
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

    public boolean isIssued() {
        return isIssued;
    }

    public void setIssued(boolean issued) {
        isIssued = issued;
    }

    public void addLink(String url, String rel) {
        Link l = new Link();
        l.setLink(url);
        l.setRel(rel);
        links.add(l);
    }

    @Override
    public String toString() {
        return "Copy{" +
                "copyId=" + copyId +
                ", bookId=" + bookId +
                ", isIssued=" + isIssued +
                '}';
    }
}
