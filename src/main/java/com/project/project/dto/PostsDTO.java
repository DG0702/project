package com.project.project.dto;

import com.project.project.entity.AttachedFile;
import com.project.project.entity.Board;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
// DTO : 데이터를 전달하는 객체
public class PostsDTO {
    // 기본값
    private Long boardId;
    // 세션정보로 설정
    private Long userNo;
    private String user_nickname;
    // 입력값으로 설정
    private String title;
    private String contents;
    // 어노테이션으로 설정
    private LocalDateTime create_time;
    private LocalDateTime update_time;
    private Long viewCount;

    // 첨부파일 정보 추가 (여러 첨부파일 처리)
    private List<AttachedFileDTO> attachedFileList;

    // 게시물 엔티티 생성
    public Board toEntity(){
        // Board 객체 생성
        Board board =  new Board(boardId,userNo,user_nickname,title,contents,create_time,update_time,viewCount);


        return board;
    }
}

