package com.example.portfolio.dto;

import lombok.*;
import org.springframework.stereotype.Repository;

import javax.persistence.*;

@NoArgsConstructor
@Repository
@Getter @Setter
@ToString
@Entity(name = "USERACCOUNT")
@SequenceGenerator(
        name = "USERACCOUNT_SEQ_GENERATOR",
        sequenceName = "useraccount_userid",
        initialValue = 1,
        allocationSize = 1
)
public class UserAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USERACCOUNT_SEQ_GENERATOR")
    @Column(name = "userid") private Long userid;
    @Column(name = "email") private String email;
    @Column(name = "pw") private String pw;
    @Column(name = "name") private String name;
    @Column(name = "phone") private String phone;
    @Column(name = "addr") private String addr;
    @Column(name = "Role") private Role role;

}
