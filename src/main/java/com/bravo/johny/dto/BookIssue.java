package com.bravo.johny.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BookIssue extends RepresentationModel<BookIssue> implements Serializable {

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
    private boolean fineCleared;
    private BookLifeCycleOperation operation;


}
