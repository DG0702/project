package com.project.project.dto;

import com.project.project.entity.Board;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class BoardWithInfo {
    private Board board;
    private int likesCount;
    private int commentCount;

}
