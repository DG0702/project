package com.project.project.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long boardId;
    @Column (name = "user_no")
    private Long userNo;
    @Column(name = "user_nickname")
    private String userNickname;
    @Column
    private String title;
    @Column
    private String contents;
    @Column
    private LocalDateTime create_time;
    @Column
    private LocalDateTime update_time;
    @Column (name = "view_count")
    private Long viewCount;

    // 게시물이 저장되기 전에 create_time, update_time 설정
    @PrePersist
    public void prePersist() {
        create_time = LocalDateTime.now(); // create_time 현재 시간으로 설정
        update_time = LocalDateTime.now(); // update_time 현재 시간으로 설정
    }
    
    // 게시물 수정시 update_time 설정
    @PreUpdate
    public void preUpdate() {
        update_time = LocalDateTime.now(); // update_time 현재 시간으로 설정
    }

    // LocalDateTime.now() : yyyy-MM-ddTHH:mm:ss.SSSSSS 이기 때문에 포맷팅으로 변경
    // create_time 포맷팅: yyyy-MM-dd 형태로 변환 => mustache에서 formattedCreateTime 사용

    public String getFormattedCreateTime() {
        if (create_time != null) {
            return create_time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        return "";
    }

    // update_time 포맷팅: yyyy-MM-dd 형태로 변환 mustache에서 formattedUpdateTime 사용
    public String getFormattedUpdateTime() {
        if (update_time != null) {
            return update_time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        return "";
    }


    // 댓글
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Comment> commentList ;

    // 좋아요
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Likes> likesList ;



    // 게시물과 첨부파일 간의 관계 설정 (여러개의 첨부파일을 가질 수 있도록 설정)
    // -> 게시물에 첨부파일을 추가하지 않아도 자동으로 게시물에 첨부파일이 추가된다 , fetch 의해 getAttachedFileList()로 호출됨
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<AttachedFile> attachedFileList;



    // 게시물에 첨부파일을 추가하는 메서드 (안전하게 관리, 관계 설정 하기 위해 필요)
    public void addAttachedFile(AttachedFile attachedFile) {
        // attachedFileList : 여러 첨부파일을 가지고 있는 목록
        // 따라서 ArrayList 형태로 변경(초기화) 시킴 (첨부파일이 없을 경우 add 메서드 호출 시 오류 발생 가능하기 때문)
        // this 는 Board 클래스를 의미
        if (this.attachedFileList == null) {
            this.attachedFileList = new ArrayList<>();
        }
        this.attachedFileList.add(attachedFile);

        // 어떤 게시물에 어떤 첨부파일이 들어가는지 설정
        attachedFile.setBoardId(this.boardId);
    }








    // toEntity 생성자 설정
    public Board (Long boardId, Long userNo,String userNickname, String title, String contents, LocalDateTime create_time, LocalDateTime update_time,Long viewCount) {
        this.boardId = boardId;
        this.userNo = userNo;
        this.userNickname = userNickname;
        this.title = title;
        this.contents = contents;
        this.create_time = create_time;
        this.update_time = update_time;
        this.viewCount = viewCount;
    }


}
