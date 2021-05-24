package com.bravo.johny.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name="LI_BOOK_COPIES")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CopyEntity {

    @Id
    @GeneratedValue
    @Column(name = "CopyId")
    private Integer copyId;

    @Column(name = "IsIssued")
    private boolean isIssued;

    /*@OneToMany(targetEntity = BookLifeCycleEntity.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "CopyId", referencedColumnName = "CopyId")
    List<BookLifeCycleEntity> bookLifeCycleEntities;*/

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, targetEntity = BookEntity.class)
    @JoinColumn(name = "BookId")
    private BookEntity book;

    public CopyEntity(BookEntity bookEntity) {
        this.book = bookEntity;
    }
}
