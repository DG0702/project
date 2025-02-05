package com.project.project.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "MENUCATEGORY")

public class MenuCategory {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "menu_id")
    private Long menuId;
    @Column(name = "menu_no")
    private Long menuNo;
    @Column
    private String title;
    @Column
    private String contents;
    @Column (name = "create_time")
    private LocalDateTime createTime;
    @Column (name = "update_time")
    private LocalDateTime updateTime;

    @PrePersist
    public void prePersist(){
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
    }

    @PreUpdate
    public void perUpdate(){
        updateTime = LocalDateTime.now();
    }


    public String formattedCreateTime(){
        if(createTime != null){
            return createTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        return "";
    }

    public String formattedUpdateTime(){
        if(updateTime != null){
            return updateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        return "";
    }

    @OneToMany(mappedBy = "menuCategory" ,cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<AttachedFile> attachedFileList;




    // 메뉴에 첨부파일을 추가하는 메서드 (안전하게 관리, 관계 설정 하기 위해 필요)
    public void addAttachedFile(AttachedFile attachedFile) {
        // attachedFileList : 여러 첨부파일을 가지고 있는 목록
        // 따라서 ArrayList 형태로 변경(초기화) 시킴 (첨부파일이 없을 경우 add 메서드 호출 시 오류 발생 가능하기 때문)
        // this 는 Board 클래스를 의미
        if (this.attachedFileList == null) {
            this.attachedFileList = new ArrayList<>();
        }
        this.attachedFileList.add(attachedFile);

        // 메뉴에 어떤 첨부파일이 들어가는지 설정
        attachedFile.setMenuId(this.menuId);
    }





}
