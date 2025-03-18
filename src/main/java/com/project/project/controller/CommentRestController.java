package com.project.project.controller;


import com.project.project.dto.CommentDTO;
import com.project.project.service.CommentService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@Slf4j
public class CommentRestController {

    // 의존성 주입
    private final CommentService commentService;



    // 댓글 작성
    @PostMapping("/comments/new")
    public ResponseEntity<Map<String,Object>> comment(@RequestBody CommentDTO commentDTO, HttpSession session){
        // @RequestBody를 CommentDTO 타입에 commentDTO 객체로 표현 >> userNo, boardId, nickname, comment 다 존재

        // 응답할 데이터
        Map<String,Object> response = new HashMap<>();

        // 데이터 베이스에 댓글 저장 성공 여부
        boolean success = commentService.addComment(commentDTO);
        


        // 데이터 베이스 성공시
        if(success){
            // 댓글 저장 성공 시 응답 데이터 준비
            response.put("success",true);
            response.put("userNickname",commentDTO.getUserNickname());
            response.put("comment",commentDTO.getComment());
            response.put("formattedCreateTime",commentDTO.getFormattedCreateTime());
            return ResponseEntity.ok(response);
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }



    // 내가 쓴 댓글 수정
    @PatchMapping("/comments/{commentId}")
    public ResponseEntity<Map<String,Object>> getComments(@PathVariable Long commentId ,@RequestBody Map<String,String> data, HttpSession session){

        // 요청 받은 데이터
        String comment = data.get("comment");

        // 응답할 데이터
        Map<String,Object> response = new HashMap<>();

        // 댓글 저장
        boolean update = commentService.updateComment(commentId,comment,session);

        if (update){
            response.put("success",true);
            return ResponseEntity.ok(response);
        }else{
            response.put("success",false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }



    // 내가 쓴 댓글 삭제
    @DeleteMapping("/users/comment")
    public ResponseEntity<Map<String,Object>> deleteComment(@RequestBody Map<String,List<Long>> data, HttpSession session){

        // 요청 데이터 가져오기 (많은 개수의 데이터)
        List<Long> commentIds = data.get("commentIds");

        // 응답할 데이터 생성
        Map<String,Object> response = new HashMap<>();

        // 댓글 삭제
        boolean delete = commentService.deleteComments(commentIds,session);

        if(delete){
            response.put("success",true);
            return ResponseEntity.ok(response);
        }else{
            response.put("success",false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }




}
