package com.bravo.johny.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="LI_CONFIG")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ConfigEntity {

    @Id
    @Column(name = "NoOfDaysAUserCanKeepABook")
    private Integer noOfDaysAUserCanKeepABook;

    @Column(name = "FinePerDay")
    private Integer finePerDay;

    @Column(name = "NoOfBooksPerUser")
    private Integer noOfBooksPerUser;
}
