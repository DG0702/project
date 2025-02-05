package com.project.project.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "BOARD_COMMENT" ,schema = "dbo")
public class Comment {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long commentId;
    @Column (name = "user_no")
    private Long userNo;
    @Column (name = "user_nickname")
    private String userNickname;
    @Column (name = "board_id")
    private Long boardId;
    @Column (name = "comment_contents")
    private String comment;
    @Column
    private LocalDateTime create_time;
    @Column
    private LocalDateTime update_time;


    // 외래키 설정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOARD_ID", insertable = false, updatable = false)
    @JsonBackReference

    private Board board;





    // 댓글 생성 시 현재시간, 수정시간 설정
    @PrePersist
    public void prePersist() {
        create_time = LocalDateTime.now();
        update_time = LocalDateTime.now();
    }
    
    
    // 댓글 수정 시 수정시간 설정
    @PreUpdate
    public void preUpdate() {
        update_time = LocalDateTime.now();
    }

    // 생성 시간 포맷팅
    public String getFormattedCreateTime(){
        if(create_time != null) {
            return create_time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        return "";
    }

    // 수정 시간 포맷팅
    public String getFormattedUpdateTime(){
        if(update_time != null){
            return update_time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        return "";
    }


    // 생성자 생성
    public Comment (Long commentId, Long userNo, String userNickname, Long boardId, String comment, LocalDateTime create_time, LocalDateTime update_time) {
        this.commentId = commentId;
        this.userNo = userNo;
        this.userNickname = userNickname;
        this.boardId = boardId;
        this.comment = comment;
        this.create_time = create_time;
        this.update_time = update_time;
    }


}
