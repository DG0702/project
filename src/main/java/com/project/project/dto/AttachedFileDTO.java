package com.project.project.dto;

import com.project.project.entity.AttachedFile;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString

// 파일 관련 데이터 전달
public class AttachedFileDTO {
    private Long file_no;
    private String uploadId;
    private Long boardId;
    private String path1;
    private String path2;
    private String savedName;
    private String fileSize;
    private LocalDateTime create_time;
    private Long menuId;

    // DTO -> Entity 변환 메서드
    public AttachedFile toEntity(){
        if(create_time == null){
            create_time = LocalDateTime.now();
        }
        return new AttachedFile(file_no,uploadId,boardId,path1,path2,savedName,fileSize,create_time,menuId);
    }
}
