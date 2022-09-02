package com.example.portfolio.entity;

import lombok.Data;

import javax.persistence.Id;

@Data
public class UserEntity {

    private Long id;
    private String email;
    private String pw;
    private String name;
    private String phone;
    private String addr;

}
