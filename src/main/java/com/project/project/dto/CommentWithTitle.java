package com.project.project.dto;

import com.project.project.entity.Comment;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class CommentWithTitle {
    private Comment comment;
    private String title;
}
