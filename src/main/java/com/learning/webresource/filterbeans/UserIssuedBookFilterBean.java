package com.learning.webresource.filterbeans;

import javax.ws.rs.QueryParam;

public class UserIssuedBookFilterBean {

    private @QueryParam("bookId") int bookId;
    private @QueryParam("offset") int offset;
    private @QueryParam("limit") int limit;

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
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
