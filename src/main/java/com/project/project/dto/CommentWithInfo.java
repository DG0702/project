package com.project.project.dto;

import com.project.project.entity.Board;
import com.project.project.entity.Comment;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class CommentWithInfo {
    private Board board;
    private Comment comment;
}
