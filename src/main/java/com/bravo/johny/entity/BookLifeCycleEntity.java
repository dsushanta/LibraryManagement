package com.bravo.johny.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name="LI_BOOK_LIFE_CYCLE")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BookLifeCycleEntity {

    @Id
    @GeneratedValue
    @Column(name = "BookIssueId")
    private Integer bookIssueId;

    @Column(name = "IsReturned")
    private boolean isReturned;

    @Column(name = "IsRenewed")
    private boolean isRenewed;

    @Column(name = "IssueDate")
    private Date issueDate;

    @Column(name = "ExpectedReturnDate")
    private Date expectedReturnDate;

    @Column(name = "ActualReturnDate")
    private Date actualReturnDate;

    @Column(name = "RenewDate")
    private Date renewDate;

    @Column(name = "Fine")
    private Integer fine;

    @Column(name = "FineCleared")
    private boolean fineCleared;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "UserName")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "BookId")
    private BookEntity book;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "CopyId")
    private CopyEntity copy;

    public BookLifeCycleEntity(UserEntity user, BookEntity book, CopyEntity copy, Date issueDate, Date expectedReturnDate) {
        this.issueDate = issueDate;
        this.expectedReturnDate = expectedReturnDate;
        this.user = user;
        this.book = book;
        this.copy = copy;
    }
}
