package com.project.project.controller;

import com.project.project.dto.CommentWithInfo;
import com.project.project.dto.CommentWithTitle;
import com.project.project.entity.Member;
import com.project.project.entity.MenuCategory;
import com.project.project.repository.UserRoleRepository;
import com.project.project.service.BoardService;
import com.project.project.service.CommentService;
import com.project.project.service.MenuCategoryService;
import jakarta.servlet.http.HttpSession;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class ManagerRestController {

    @Autowired
    private MenuCategoryService menuCategoryService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private BoardService boardService;
    @Autowired
    private UserRoleRepository userRoleRepository;

    // 뉴스 메뉴 수정
    @PatchMapping("/manager/news")
    public ResponseEntity<Map<String,Object>> updateNews(@RequestParam Long menuId , @RequestParam String title, @RequestParam String contents, @RequestParam(required = false) MultipartFile [] files, HttpSession session){

        // 응답할 데이터
        Map<String,Object> response = new HashMap<>();

        // 사용자 정보 가져오기
        Member member = (Member) session.getAttribute("member");

        boolean isAdmin = userRoleRepository.existsByMemberAndRole_RoleName(member,"ADMIN");

        if(!isAdmin){
            response.put("success",false);
            response.put("message", "관리자만 수정 가능합니다.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }


        try{
            menuCategoryService.updateNews(menuId,title,contents,files,session);
            response.put("success",true);
            return ResponseEntity.ok(response);
        }
        catch (Exception e){
            response.put("success",false);
            response.put("message", "뉴스 수정에 실패 하였습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }




    // 댓글 검색
    @GetMapping("/comment/search")
    public ResponseEntity<Map<String,Object>> searchComment(@RequestParam ("keyword") String keyword, Pageable pageable){

        // 검색한 단어 가져오기
        Page<CommentWithTitle> commentPage = commentService.searchCommentWithBoardInfo(keyword,pageable);

        // 전체 페이지 수
        int totalPage = commentPage.getTotalPages();

        List<Integer> pages = new ArrayList<>();
        for(int i = 1; i <= totalPage; i++){
            pages.add(i);
        }

        // 응답할 데이터
        Map<String,Object> response = new HashMap<>();

        response.put("comments", commentPage.getContent());
        response.put("pages", pages);
        response.put("hasPrevious", commentPage.hasPrevious());
        response.put("hasNext", commentPage.hasNext());
        response.put("keyword",keyword);

        return ResponseEntity.ok(response);
    }


    // 관리자 게시물 삭제
    @DeleteMapping("/manager/posts")
    public ResponseEntity<Map<String,Object>> deletePost (@RequestBody Map<String,Long> data, HttpSession session){
        // 요청 데이터 가져오기
        Long boardId = data.get("boardId");

        // 응답할 데이터 생성
        Map<String,Object> response = new HashMap<>();

        boolean delete = boardService.deleteBoard(boardId, session);

        if(delete){
            response.put("success",true);
            return ResponseEntity.ok(response);
        }else{
            response.put("success",false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    
    // 관리자 댓글 삭제
    @DeleteMapping("/manager/comment")
    public ResponseEntity<Map<String,Object>> deleteComment (@RequestBody Map<String,Long> data, HttpSession session){
        // 요청 데이터 가져오기
        Long commentId = data.get("commentId");

        // 응답 데이터 생성
        Map<String,Object> response = new HashMap<>();

        boolean delete = commentService.deleteComment(commentId,session);

        if(delete){
            response.put("success",true);
            return ResponseEntity.ok(response);
        }else{
            response.put("success",false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

    }
}
