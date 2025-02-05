package com.project.project.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString

// 실제 데이터베이스 테이블과 매핑
public class AttachedFile {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long file_no;
    @Column (name = "upload_id")
    private String uploadId;
    @Column (name = "board_id")
    private Long boardId;
    @Column
    private String path1;
    @Column
    private String path2;
    @Column (name = "saved_name")
    private String savedName;
    @Column (name = "file_size")
    private String fileSize;
    @Column
    private LocalDateTime create_time;
    @Column (name = "menu_id")
    private Long menuId;





    // AttachedFile 엔티티와 Board 엔티티의 관계 설정



    // 다대일 관계
    @ManyToOne(fetch = FetchType.LAZY)
    // 외래키 설정, AttachedFile 엔티티에서 (외래키) 값을 설정 (단 데이터베이스의 컬럼이랑 동일한 이름)
    @JoinColumn(name = "BOARD_ID" ,insertable = false, updatable = false)
    @JsonBackReference
    // boardId 값을 가져오기 위해 Board 객체 참조
    private Board board;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MENU_ID", insertable = false, updatable = false)
    @JsonBackReference
    private MenuCategory menuCategory;



    // boardId 설정 메서드
    public void setBoard(Board board) {
        this.board = board;
        if (board != null) {
            // 첨부파일마다 게시물 ID 설정 (어떤 게시물에 어떤 첨부파일이 들어가는지 설정)
            this.boardId = board.getBoardId();
        }
    }



    // 파일 생성 시간 설정
    @PrePersist
    private void prePersist(){
        if(create_time == null){
            create_time = LocalDateTime.now();
        }
    }

    // 생성 시간 포맷팅
    public String getFormattedCreateTime(){
        if(create_time != null){
            return create_time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        return "";
    }

    // toEntity 생성자 설정
    public AttachedFile(Long file_no, String uploadId, Long boardId, String path1, String path2, String savedName, String fileSize ,LocalDateTime create_time, Long menuId) {
        this.file_no = file_no;
        this.uploadId = uploadId;
        this.boardId = boardId;
        this.path1 = path1;
        this.path2 = path2;
        this.savedName = savedName;
        this.fileSize = fileSize;
        this.create_time = create_time;
        this.menuId = menuId;
    }


    // 파일 타입 설정
    public String getFileType(){
        String fileName = this.savedName;
        // 각 파일마다 fileType 구분
        if(fileName.endsWith(".jpg") || fileName.endsWith(".png")){
            return  "image";
        } else if (fileName.endsWith(".mp4") || fileName.endsWith(".avi")) {
            return  "video";
        } else if (fileName.endsWith(".txt") || fileName.endsWith(".doc") || fileName.endsWith(".pdf") || fileName.endsWith(".hwp")) {
            return  "document";
        }else{
            return "unknown";
        }
    }
}
