package com.bravo.johny.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name="LI_USERS")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserEntity implements Serializable {

    @Id
    @Column(name = "UserName")
    private String userName;

    @Column(name = "FirstName")
    private String firstName;

    @Column(name = "LastName")
    private String lastName;

    @Column(name = "Email")
    private String email;

    @Column(name = "Pwd")
    private String password;

    @Column(name = "FavGenre")
    private String favouriteGenre;

    @Column(name = "Fine")
    private Integer fine;

    @Column(name = "BookCount")
    private Integer bookCount;

    @Column(name = "UserImage")
    private byte[] userImage;

    @Column(name = "Active")
    private boolean active;

    @Column(name = "Role")
    private String role;

    /*@OneToMany(targetEntity = BookLifeCycleEntity.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "UserName", referencedColumnName = "UserName")
    List<BookLifeCycleEntity> bookLifeCycleEntities;*/

    public UserEntity(String userName, String firstName, String lastName, String email,
                      String password, String favouriteGenre, Integer fine, Integer bookCount,
                      boolean active, String role) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.favouriteGenre = favouriteGenre;
        this.fine = fine;
        this.bookCount = bookCount;
        this.active = active;
        this.role = role;
    }

    /*@OneToMany(targetEntity = BookLifeCycleEntity.class, mappedBy = "users", fetch = FetchType.LAZY)
    private Set<BookLifeCycleEntity> bookLifeCycleEntities;*/
}
