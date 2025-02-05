package com.project.project.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Likes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long likes_no;
    @Column (name = "user_no")
    private Long userNo;
    @Column (name = "board_id")
    private Long boardId;
    @Column (name = "likes_status", columnDefinition = "boolean default false")
    private boolean likesStatus;
    @Column
    private LocalDateTime create_time;


    // 외래키 설정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOARD_ID", insertable = false, updatable = false)
    @JsonBackReference

    private Board board;






    // 시간 설정
    @PrePersist
    public void prePersist() {
        create_time = LocalDateTime.now();
    }


}
