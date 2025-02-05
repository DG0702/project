package com.project.project.repository;

import com.project.project.entity.AttachedFile;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AttachedFileRepository extends JpaRepository<AttachedFile, Long> {

    // 게시물에 첨부된 파일 목록 조회
    public List<AttachedFile> findByBoardId(Long boardId);

    // 메뉴 뉴스에 첨부된 파일 목록조회
    public List<AttachedFile> findByMenuId(Long menuId);

    // 게시물 ID로 첨부파일 삭제
    @Modifying
    @Transactional
    @Query("DELETE FROM AttachedFile af WHERE af.boardId = :boardId")
    void deleteByBoardId(Long boardId);
}
