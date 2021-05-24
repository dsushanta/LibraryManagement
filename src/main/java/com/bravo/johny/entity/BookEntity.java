package com.bravo.johny.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="LI_BOOKS")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BookEntity implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "BookId")
    private Integer bookId;

    @Column(name = "Title")
    private String title;

    @Column(name = "Description")
    private String description;

    @Column(name = "Author")
    private String author;

    @Column(name = "Genre")
    private String genre;

    @Column(name = "Likes")
    private Integer likes;

    @Column(name = "Available")
    private boolean available;

    public BookEntity(String title, String description, String author, String genre) {
        this.title = title;
        this.description = description;
        this.author = author;
        this.genre = genre;
    }

    /*@OneToMany(targetEntity = CopyEntity.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "BookId", referencedColumnName = "BookId")
    List<CopyEntity> copies;

    @OneToMany(targetEntity = BookLifeCycleEntity.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "BookId", referencedColumnName = "BookId")
    List<BookLifeCycleEntity> bookLifeCycleEntities;*/

    /*@OneToMany(targetEntity = CopyEntity.class, mappedBy = "books", fetch = FetchType.LAZY)
    private Set<CopyEntity> copies;

    @OneToMany(targetEntity = BookLifeCycleEntity.class, mappedBy = "books", fetch = FetchType.LAZY)
    private Set<BookLifeCycleEntity> bookLifeCycleEntities;*/
}
