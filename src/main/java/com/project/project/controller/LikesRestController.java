package com.project.project.controller;

import com.project.project.service.LikesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class LikesRestController {


    private final LikesService likesService;

    // 의존성 주입
    public LikesRestController(LikesService likesService) {
        this.likesService = likesService;
    }

    // 게시글 조회 시 좋아요 상태
    @GetMapping("/posts/{boardId}/likes-status")
    public Map<String,Object> getLikesStatus(@PathVariable Long boardId, @RequestParam Long userNo){

        // 응답할 데이터 생성
        Map<String,Object> response = new HashMap<>();

        //게시글 조회 시 좋아요 상태 확인
        boolean isLiked = likesService.isLiked(userNo, boardId);

        // 게시글에 대한 좋아요 총 개수
        int likeCount = likesService.getLikeCount(boardId);

        response.put("isLiked", isLiked);
        response.put("likeCount", likeCount);
        return response;
    }

    // 좋아요 누를 시
    @PostMapping("/posts/{boardId}/likes")
    public ResponseEntity<Map<String,Object>> toggleLike(@RequestBody Map<String,Long> data){

        // 응답할 데이터
        Map<String,Object> response = new HashMap<>();

        // 요청 본문으로 온 데이터 반환
        Long userNo = data.get("userNo");
        Long boardId = data.get("boardId");

        // 좋아요 토클
        boolean toggled = likesService.togglelikes(userNo,boardId);

        if(toggled){
            response.put("isLiked", true);
            return ResponseEntity.ok(response);
        }else{
            response.put("isLiked", false);
            return ResponseEntity.ok(response);
        }


    }
}
