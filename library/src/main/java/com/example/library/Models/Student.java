package com.example.library.Models;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String rollNo;

    private String name;

    private Integer age;

    @OneToMany(fetch = FetchType.EAGER)
    @JsonIgnoreProperties({"student","author","book","admin","transaction"})
    private List<Request> requestList;

    @CreationTimestamp
    private Date createdOn;

    @OneToOne
    @JoinColumn
    @JsonIgnoreProperties({"admin","password","student"})
    private User user;

}
