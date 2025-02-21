package com.project.project.repository;

import com.project.project.entity.Comment;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 게시물 댓글
    int countByBoardId(Long boardId);

    // 게시물 댓글 리스트
    List<Comment> findByBoardId(Long BoardId);

    // 내가 쓴 댓글 리스트
    Page<Comment> findByUserNo(Long userNo, Pageable pageable);

    // 게시물 삭제 시 댓글 삭제
    @Transactional
    void deleteByBoardId(Long boardId);


    // 댓글 검색 (작성자 , 댓글 내용)
    Page<Comment> findByUserNicknameContainingOrCommentContaining(String userNickname, String content, Pageable pageable);

}
