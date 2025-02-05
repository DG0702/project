package com.project.project.dto;


import com.project.project.entity.Comment;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class CommentDTO {
    private Long comment_id;
    private Long userNo;
    private String userNickname;
    private Long boardId;
    private String comment;
    private LocalDateTime create_time;
    private LocalDateTime update_time;


    public Comment toEntity(){
        // DTO에서 엔티티로 변환할 때 create_time, update_time 값이 없을 경우
        if(create_time == null){
            create_time = LocalDateTime.now();
        }
        if(update_time == null){
            update_time = LocalDateTime.now();
        }
        return new Comment(comment_id,userNo,userNickname,boardId,comment,create_time,update_time);
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
}
