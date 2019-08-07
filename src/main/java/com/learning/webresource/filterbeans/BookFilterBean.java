package com.learning.webresource.filterbeans;

import javax.ws.rs.QueryParam;

public class BookFilterBean {

    private @QueryParam("author") String author;
    private @QueryParam("title") String title;
    private @QueryParam("genre") String genre;
    private @QueryParam("offset") int offset;
    private @QueryParam("limit") int limit;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
