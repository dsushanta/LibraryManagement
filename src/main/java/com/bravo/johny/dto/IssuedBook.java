package com.bravo.johny.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class IssuedBook extends RepresentationModel<IssuedBook> implements Serializable {

    private int bookId;
    private String title;
    private String author;
    private String genre;
    private long numberOfCopiesIssued;
}
