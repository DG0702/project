package com.project.project.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_no")
    private Long userNo;
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

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL ,fetch = FetchType.LAZY)
    @JsonBackReference
    private List<UserRole> userRoleList;

    public Member(Long userNo, String userId, String userName, String password, String phoneNumber, String nickName, String birthday, String addr, String gender, String grade) {
        this.userNo = userNo;
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.nickName = nickName;
        this.birthday = birthday;
        this.addr = addr;
        this.gender = gender;
        this.grade = grade;
    }


    // 생성자

}
