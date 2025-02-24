package com.project.project.service;

import com.project.project.dto.CommentDTO;
import com.project.project.dto.CommentWithInfo;
import com.project.project.dto.CommentWithTitle;
import com.project.project.entity.Board;
import com.project.project.entity.Comment;
import com.project.project.entity.Member;
import com.project.project.repository.BoardRepository;
import com.project.project.repository.CommentRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {


    private final CommentRepository commentRepository;

    private final BoardRepository boardRepository;

    // 의존성 주입
    public CommentService(CommentRepository commentRepository, BoardRepository boardRepository) {
        this.commentRepository = commentRepository;
        this.boardRepository = boardRepository;
    }

    // 댓글 등록
    public Boolean addComment(CommentDTO commentDTO) {
        Comment comment = commentDTO.toEntity();
        Comment savedComment = commentRepository.save(comment);
        return savedComment != null;
    }



    // 댓글 개수 파악
    public int getCommentCount(Long boardId){
        return commentRepository.countByBoardId(boardId);
    }

    // 게시물에 댓글 전체 리스트
    public List<Comment> getComments(Long boardId){
        return commentRepository.findByBoardId(boardId);
    }


    // 내가 쓴 댓글 가져오기
    public Page<Comment> getMyComments (Long userNo, int page, int size){

        // 페이징 요청 처리 -> 객체 생성
        Pageable pageable = PageRequest.of(page - 1 ,size);

        return commentRepository.findByUserNo(userNo,pageable);

    }

    // 댓글에 해당하는 게시글 함께 반환
    public List<CommentWithInfo> getCommentWithBoardInfo(Long userNo, int page, int size){

        // 내가 쓴 댓글 리스트 가져오기
        Page<Comment> commentPage = getMyComments(userNo,page,size);

        // 빈 리스트 생성
        List<CommentWithInfo> commentWithInfos = new ArrayList<>();

        for(Comment comment : commentPage.getContent()){
            Board board = boardRepository.findById(comment.getBoardId()).orElse(null);

            // 생성자 생성하여 값을 넣어주기
            commentWithInfos.add(new CommentWithInfo(board,comment));
        }

        return commentWithInfos;
    }


    // 댓글 가져오기
    public Comment getComment(Long commentId){
        return commentRepository.findById(commentId).orElse(null);
    }



    // 댓글 수정
    public boolean updateComment(Long commentId, String comment, HttpSession session){

        Member member = (Member) session.getAttribute("member");

        // 댓글 찾기
        Comment existingComment = commentRepository.findById(commentId).orElse(null);

        // 댓글 존재 유무에 따라
        if(existingComment == null){
            throw new IllegalArgumentException("댓글을 찾을 수 없습니다 ");
        }
        
        if(!existingComment.getUserNo().equals(member.getUserNo())){
            throw new RuntimeException("본인 댓글만 수정 할 수 있습니다");
        }

        // 댓글 수정
        existingComment.setComment(comment);
        commentRepository.save(existingComment);
        return true;
    }


    // 사용자 댓글 삭제
    public boolean deleteComments (List<Long> commentIds,HttpSession session){

        // 사용자 정보 가져오기
        Member member = (Member) session.getAttribute("member");

        try {
            // 댓글 찾기
            for(Long commentId : commentIds){
                Comment existingComment = commentRepository.findById(commentId).orElse(null);

                if(existingComment == null){
                    throw new IllegalArgumentException("댓글을 찾을 수 없습니다");
                }

                if(!existingComment.getUserNo().equals(member.getUserNo())){
                  throw new RuntimeException("본인 댓글만 삭제 할 수 있습니다");
                }


                commentRepository.delete(existingComment);

            }
            return true;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 관리자 댓글 삭제
    public boolean deleteComment(Long commentId, HttpSession session){

        // 관리자 정보 가져오기
        Member member = (Member) session.getAttribute("member");

        try{

            if(!member.getGrade().equals("ADMIN")){
                throw new RuntimeException("관리자만 모든 댓글이 삭제 가능합니다");
            }

            Comment existingComment = commentRepository.findById(commentId).orElse(null);

            if(existingComment == null){
                throw new IllegalArgumentException("삭제할 댓글을 찾지 못하였습니다");
            }

            commentRepository.delete(existingComment);
            return true;
        }
        catch (IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }
    }



    // 해당 페이지로 모든 댓글 가져오기
    public Page<Comment> getCommentPage(int page, int size){
        Pageable pageable = PageRequest.of(page - 1 ,size);
        return commentRepository.findAll(pageable);
    }


    // 댓글 리스트
    public List<CommentWithInfo> getCommentInfo(int page, int size){
        Pageable pageable = PageRequest.of(page -1 , size);

        Page<Comment> commentPage = getCommentPage(page,size);

        List<CommentWithInfo> commentWithInfos = new ArrayList<>();

        for(Comment comment : commentPage.getContent()){

            Board board = boardRepository.findById(comment.getBoardId()).orElse(null);

            commentWithInfos.add(new CommentWithInfo(board,comment));
        }

        return commentWithInfos;
    }
    
    
    
    // 검색한 댓글 가져오기
    public Page<Comment> searchComment(String keyword, Pageable pageable){
        return commentRepository.findByUserNicknameContainingOrCommentContaining(keyword,keyword,pageable);
    }



    // 검색한 댓글 + 관련된 게시물 가져오기
    public Page<CommentWithTitle> searchCommentWithBoardInfo(String keyword, Pageable pageable){

        // 검색한 댓글 가져오기
        Page<Comment> searchComment = searchComment(keyword,pageable);

        List<CommentWithTitle> commentWithTitles = new ArrayList<>();

        for(Comment comment : searchComment.getContent()){
            Board board = boardRepository.findById(comment.getBoardId()).orElse(null);
            String title = board.getTitle();
            commentWithTitles.add(new CommentWithTitle(comment,title));
        }

        return new PageImpl<CommentWithTitle>(commentWithTitles,pageable,searchComment.getTotalElements());
    }
}
