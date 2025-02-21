package com.project.project.repository;

import com.project.project.entity.Likes;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes, Long> {

    // 현재 로그인한 회원이 특정 게시물에 대해 좋아요를 눌렀는지
    Optional<Likes> findLikesByUserNoAndBoardId(Long userNo, Long boardId);

    // 해당 게시물의 좋아요 수 계산
   int countByBoardIdAndLikesStatus(Long boardId, boolean status);


    // 해당 게시물에 대한 좋아요 상태를 업데이트 하는 메서드
    Likes save(Likes likes);

    // 게시물 ID로 좋아요 삭제
    @Transactional
    void deleteByBoardId(Long boardId);
}
