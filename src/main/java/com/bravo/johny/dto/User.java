package com.bravo.johny.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString(includeFieldNames = true)
public class User extends RepresentationModel<User> implements Serializable {

    private String username;
    private String firstname;
    private String lastname;
    private String email;
    @ToString.Exclude
    private String password;
    private String favGenre;
    private int bookCount;
    private int fine;
    private boolean active;
    private String role;

    public User(String username, String firstname, String lastname) {
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
    }
}
