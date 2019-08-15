package com.learning.dto;

public class LibraryConfig {

    private int NO_OF_DAYS_A_USER_CAN_KEEP_A_BOOK;
    private int FINE_PER_DAY;
    private int NUMBER_OF_BOOKS_PER_USER;

    public LibraryConfig() {
    }

    public int getNO_OF_DAYS_A_USER_CAN_KEEP_A_BOOK() {
        return NO_OF_DAYS_A_USER_CAN_KEEP_A_BOOK;
    }

    public void setNO_OF_DAYS_A_USER_CAN_KEEP_A_BOOK(int NO_OF_DAYS_A_USER_CAN_KEEP_A_BOOK) {
        this.NO_OF_DAYS_A_USER_CAN_KEEP_A_BOOK = NO_OF_DAYS_A_USER_CAN_KEEP_A_BOOK;
    }

    public int getFINE_PER_DAY() {
        return FINE_PER_DAY;
    }

    public void setFINE_PER_DAY(int FINE_PER_DAY) {
        this.FINE_PER_DAY = FINE_PER_DAY;
    }

    public int getNUMBER_OF_BOOKS_PER_USER() {
        return NUMBER_OF_BOOKS_PER_USER;
    }

    public void setNUMBER_OF_BOOKS_PER_USER(int NUMBER_OF_BOOKS_PER_USER) {
        this.NUMBER_OF_BOOKS_PER_USER = NUMBER_OF_BOOKS_PER_USER;
    }

    @Override
    public String toString() {
        return "LibraryConfig{" +
                "NO_OF_DAYS_A_USER_CAN_KEEP_A_BOOK=" + NO_OF_DAYS_A_USER_CAN_KEEP_A_BOOK +
                ", FINE_PER_DAY=" + FINE_PER_DAY +
                ", NUMBER_OF_BOOKS_PER_USER=" + NUMBER_OF_BOOKS_PER_USER +
                '}';
    }
}