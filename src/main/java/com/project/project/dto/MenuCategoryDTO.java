package com.project.project.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MenuCategoryDTO {
    private Long menuId;
    private Long menuNo;
    private String title;
    private String contents;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;


}
