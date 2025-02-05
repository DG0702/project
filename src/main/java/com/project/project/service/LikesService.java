package com.project.project.service;

import com.project.project.entity.Likes;
import com.project.project.repository.LikesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LikesService {

    @Autowired
    private LikesRepository likesRepository;

    // Optional<> 반환값은 객체
    // isPresent() 객체에 값이 존재하는지 안하는지 true,false 표현

    // 회원이 조회한 해당 게시물에 좋아요를 눌렸는지 확인
    public boolean isLiked (Long userNo, Long boardId){
        Optional<Likes> likesData = likesRepository.findLikesByUserNoAndBoardId(userNo, boardId);
        return likesData.isPresent() && likesData.get().isLikesStatus();
    }

    // 좋아요 상태 toggle
    public boolean togglelikes(Long userNo, Long boardId){

        // 좋아요 데이터 확인
        Optional<Likes> likesData = likesRepository.findLikesByUserNoAndBoardId(userNo,boardId);

        // 회원이 좋아요를 한번이라도 눌렸다면 데이터가 존재
        if(likesData.isPresent()){
            // 기존 좋아요 데이터 가져오기
            Likes likes = likesData.get();
            // 좋아요 상태를 반전 (좋아요를 다시 누르거나 취소하거나)
            likes.setLikesStatus(!likes.isLikesStatus());

            // 변경된 좋아요 상태 데이터베이스에 저장
            likesRepository.save(likes);
            return likes.isLikesStatus();
            // likes.isLIKES_STATUS(); >> true >> "좋아요" 상태
            // likes.isLIKES_STATUS(); >> false >> "좋아요" 취소 상태
        }
        // '좋아요'를 한번이라도 누르지 않았다면
        else{
            // 데이터베이스에 데이터를 추가하기 위해 newlikes로 설정
            Likes newlikes = new Likes();
            newlikes.setUserNo(userNo);
            newlikes.setBoardId(boardId);
            newlikes.setLikesStatus(true);
            likesRepository.save(newlikes);
            return true;
        }
    }

    // 해당 게시물에 대한 총 좋아요 수 가져오기
    public int getLikeCount (Long boardId){
        return likesRepository.countByBoardIdAndLikesStatus(boardId,true);
    }

}
