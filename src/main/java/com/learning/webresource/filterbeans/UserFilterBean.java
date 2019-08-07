package com.learning.webresource.filterbeans;

import javax.ws.rs.QueryParam;

public class UserFilterBean {

    private @QueryParam("lastName") String lastName;
    private @QueryParam("offset") int offset;
    private @QueryParam("limit") int limit;

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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
