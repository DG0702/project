package com.project.project.repository;

import com.project.project.dto.BoardWithInfo;
import com.project.project.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

public interface BoardRepository extends JpaRepository<Board,Long> {
    // Board 엔티티 모든 데이터 조회 >> 데이터베이스 Board 테이블에 저장된 모든 행을 조회
    // Board 엔티티를 ArrayList 담아 반환
    // ArrayList 동적 배열 형태 컬렉션 >> 크기가 자동 조정 , 배열(고정크기)과 차이로 크기가 조절 가능


    // 내가 쓴 게시물 페이징 처리
    Page<Board> findByUserNo(long userNo, Pageable pageable);

    // 제목이나 내용 검색어(keyword)가 포함된 게시물을 페이징 처리 반환
    // findBy : 주어진 속성을 기준으로 데이터를 찾는 쿼리 자동 생성
    // Title, Content , Author : (엔티티 클래스 속성)
    // Containing : JPA 키워드로 해당 필드(게시물)가 주어진 값(검색어)을 포함하는지 확인하는 조건
    // Or : 또는
    // Pageable : 페이징 처리를 위해 Pageable 객체를 전달받아 검색 결과를 페이징하여 반환
    Page<Board> findByTitleContainingOrContentsContainingOrUserNicknameContaining(String title, String content, String userNickname , Pageable pageable);


}
