package com.project.project.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LikesDTO {
    private Long likes_no;
    private Long user_no;
    private Long boardId;
    private boolean likes_status;
    private LocalDateTime create_time;
}
