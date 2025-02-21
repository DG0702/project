package com.project.project.controller;

import com.project.project.dto.BoardWithInfo;
import com.project.project.entity.Board;
import com.project.project.service.BoardService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class BoardRestController {


    private final BoardService boardService;

    // 의존성 주입
    public BoardRestController(BoardService boardService) {
        this.boardService = boardService;
    }



    // 게시물 검색, 페이징처리
    @GetMapping("/board/search")
    public ResponseEntity<Map<String,Object>> searchBoard(@RequestParam ("keyword") String keyword , Pageable pageable ){
        // 검색한 단어 데이터로 가져오기 (페이징할 객체도 가져오기)
        Page<BoardWithInfo> boardPage = boardService.searchBoardWithInfo(keyword,pageable);

        // 전체 페이지 수
        int totalPage = boardPage.getTotalPages();

        // 페이지 수 배열로 생성
        List<Integer> pages = new ArrayList<>();
        for(int i = 1; i <= totalPage; i++){
            pages.add(i);
        }

        // 응답할 데이터 생성
        Map<String,Object> response = new HashMap<>();

        // 페이징 된 객체에 게시물 리스트
        response.put("content",boardPage.getContent());

        // 이전 페이지 활성화
        response.put("hasPrevious",boardPage.hasPrevious());

        // 다음 페이지 활성화
        response.put("hasNext",boardPage.hasNext());

        // 전체 페이지 수
        response.put("pages",pages);

        // 키워드
        response.put("keyword",keyword);

        return ResponseEntity.ok(response);
    }






    // 게시물 조회 수
    @PostMapping("/posts/{boardId}/incrementViewCount")
    public ResponseEntity<Map<String,Object>> incrementViewCount(@PathVariable("boardId") Long boardId){
        // 조회 수 1 증가 여부
        boolean success = boardService.incrementViewCount(boardId);
        
        // 성공 시
        if(success){
            // build() 응답 본문이 비워 두고 응답
            return ResponseEntity.ok().build();
        }
        // 실패 시 
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }






    // 내가 쓴 게시물 수정
    @PatchMapping("/posts/{boardId}")
    public ResponseEntity<Board> updateBoard(@PathVariable("boardId") Long boardId , @RequestParam String title, @RequestParam String contents, @RequestParam(required = false) MultipartFile [] files , HttpSession session){

        // 파일의 아무것도 담지 않더라도  files 기본값은 1 -> files 존재할 경우에만 받는다

        try {
            // 게시물 수정
            Board updateBoard = boardService.updatePost(boardId,title,contents,files,session);
            return ResponseEntity.ok(updateBoard);
        }
        // 잘못된 게시물 ID 등이 들어왔을 때 처리
        catch (IllegalArgumentException e){
            log.error("잘못된 게시물 ID : {}",boardId,e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        catch(Exception e){
            log.error("게시물 수정 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }





    // 내가 쓴 게시물 첨부파일 모두 삭제
    @DeleteMapping("/posts/files")
    public ResponseEntity<Map<String,Object>> deleteFile(@RequestBody Map<String,Long> data){

        // 게시물 아이디 가져오기
        Long boardId = data.get("boardId");

        // 응답할 데이터
        Map<String,Object> response = new HashMap<>();

        // 첨부파일 삭제
        boolean delete = boardService.deleteAllFiles(boardId);

        if(delete){
            response.put("success",true);
            return ResponseEntity.ok().body(response);
        }else {
            response.put("success",false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }



    // 게시물 삭제
    @DeleteMapping("/users/posts")
    public ResponseEntity<Map<String,Object>> deletePost(@RequestBody Map<String,List<Long>> data, HttpSession session){

        // 게시물 아이디 가져오기
        List<Long> boardIds = data.get("boardIds");

        if(boardIds == null || boardIds.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        // 응답할 데이터
        Map<String,Object> response = new HashMap<>();

        boolean delete = boardService.deletePost(boardIds,session);

        if(delete){
            response.put("success",true);
            return ResponseEntity.ok().body(response);
        }else{
            response.put("success",false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



}


