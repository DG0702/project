package com.project.project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_role_no")
    private Long userRoleNo;

    @ManyToOne
    @JoinColumn(name = "user_no")
    private Member member;


    @ManyToOne
    @JoinColumn(name = "role_no")
    private Role role;


    private LocalDateTime createTime;
    private LocalDateTime updateTime;


    @PrePersist
    public void prePersist() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updateTime = LocalDateTime.now();
    }

    public String getFormattedCreateTime(){
        if(createTime != null){
            return createTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        return "";
    }

    public String getFormattedUpdateTime(){
        if(updateTime != null){
            return updateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        return "";
    }


}


