package com.project.project.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_no;
    @Column(name = "user_id")
    private String userId;
    @Column(name = "user_name")
    private String userName;
    @Column(name = "password")
    private String password;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "user_nickname")
    private String nickName;
    @Column(name = "user_birthday")
    private String birthday;
    @Column(name = "user_addr")
    private String addr;
    @Column(name = "user_gender")
    private String gender;
    @Column(name = "grade")
    private String grade;

}
