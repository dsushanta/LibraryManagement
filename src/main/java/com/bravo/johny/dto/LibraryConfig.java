package com.bravo.johny.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LibraryConfig {

    private int NO_OF_DAYS_A_USER_CAN_KEEP_A_BOOK;
    private int FINE_PER_DAY;
    private int NUMBER_OF_BOOKS_PER_USER;
}