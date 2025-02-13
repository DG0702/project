package com.project.project.service;

import com.project.project.dto.BoardWithInfo;
import com.project.project.dto.PostsDTO;
import com.project.project.entity.AttachedFile;
import com.project.project.entity.Board;
import com.project.project.entity.Member;
import com.project.project.repository.AttachedFileRepository;
import com.project.project.repository.BoardRepository;
import com.project.project.repository.CommentRepository;
import com.project.project.repository.LikesRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private LikesService likesService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private AttachedFileRepository attachedFileRepository;

    @Autowired
    private FileService fileService;

    @Autowired
    private LikesRepository likesRepository;

    @Autowired
    private CommentRepository commentRepository;




    // 게시물 쓰기
    public Board createPost(Board board){
        return boardRepository.save(board);
    }


    // 생성된 게시물에 첨부파일 업데이트
    public void addFile(Board board){

        // 존재하는 게시물을 찾기
        Board existingBoard = boardRepository.findById(board.getBoardId()).orElse(null);


        // 게시물이 없으면 null 반환 또는 예외 처리
        if (existingBoard == null) {
            throw new IllegalArgumentException("게시물을 찾을 수 없습니다.");
        }

        // 첨부파일 존재 시
        if(existingBoard.getAttachedFileList() != null){
            // 첨부 파일 추가
            for(AttachedFile file : board.getAttachedFileList()){
                // 게시물에 첨부파일 추가, 첨부파일에 게시물 아이디 설정
                if (file != null) {
                    existingBoard.addAttachedFile(file);
                }
            }
        }

        boardRepository.save(existingBoard);
    }



    // 게시물 수정할 내용 가져오기
    public Board getPost(Long boardId){
        
        // 게시물 찾기
        Board getPost = boardRepository.findById(boardId).orElse(null);

        if(getPost == null){
            return null;
        }

        return getPost;

    }



    // 게시물 수정
    @Transactional
    public Board updatePost(Long boardId, String title, String content, MultipartFile [] files, HttpSession session){

        // 사용자 정보 가져오기
        Member member = (Member) session.getAttribute("member");

        if(member == null){
            throw new RuntimeException("로그인이 필요합니다");
        }

        // 게시물 찾기
        Board existingBoard = boardRepository.findById(boardId).orElse(null);

        if(existingBoard == null){
            throw new IllegalArgumentException("게시물을 찾을 수 없습니다");
        }

        // 수정 권한 체크
        if(!existingBoard.getUserNo().equals(member.getUserNo())){
            throw new RuntimeException("본인 게시물만 수정 할 수 있습니다");
        }

        
        // 제목, 내용 수정
        existingBoard.setTitle(title);
        existingBoard.setContents(content);

        // 첨부파일 처리
       try {
           // 수정할 첨부파일을 선택했을 경우
           if (files != null && files.length > 0) {
               // 기존 파일 삭제 후 새 파일 추가
               fileService.updateFiles(boardId, files, session);
           }
       }
       catch(IOException e){
            throw new RuntimeException("파일 처리 중 오류 발생 ", e);
       }

        return boardRepository.save(existingBoard);
    }






    // Page<>인터페이스 : 페이징 된 결과를 담고 있는 인터페이스 , 쿼리 실행 결과를 페이지 단위로 담겨 있는 객체 >> 결과 
    // Pageable 인터페이스 >> 페이징 요청에 대한 정보를 담고 있는 인터페이스 (페이지 번호, 한페이지당 개수) >> 정보


    // 해당 페이지 게시물 가져오기
    public Page<Board> getBoardPage(int page, int size){

        // PageRequest는 Pageable의 구현 클래스
        // PageRequest.of() 메서드는 PageRequest클래스의 객체를 생성해주는 메서드
        
        // 페이징 요청을 처리
        Pageable pageable = PageRequest.of( page - 1, size);

        // 해당 페이지에 해당하는 데이터 가져오기
        // 페이징 처리를 위해 findAll(Pageable pageable) 메서드 사용 -> 페이징된 결과(객체) 반환
        return boardRepository.findAll(pageable);
    }




    // 게시판 리스트
    public List<BoardWithInfo> getBoardWithInfo(int page, int size){

        // 게시물 리스트 가져오기
        Page<Board> boardPage = getBoardPage(page,size);

        List<BoardWithInfo> boardWithInfos = new ArrayList<>();

        for(Board board : boardPage.getContent()){
            // 게시물마다 좋아요 수 가져오기
            int likesCount = likesService.getLikeCount(board.getBoardId());
            // 게시물마다 댓글 수 가져오기
            int commentCount = commentService.getCommentCount(board.getBoardId());

            // 게시판 정보 (board,likesCount,viewCount)
            boardWithInfos.add(new BoardWithInfo(board,likesCount,commentCount));
        }

        return boardWithInfos;
    }





    // 내가 쓴 게시물 페이지 처리
    public Page<Board> getMyPosts(Long userNo, int page, int size){

        // 페이징 요청 처리 -> Pageable 객체 생성
        Pageable pageable = PageRequest.of( page - 1, size);

        // 내가 쓴 게시물 페이징 요청 처리 -> userNo 게시물 조회 , 페이징된 결과 반환
        return boardRepository.findByUserNo(userNo,pageable);
    }




    // 내가 쓴 게시물 리스트
    public List<BoardWithInfo> getMyBoardWithInfo(Long userNo, int page, int size){

        Page<Board> boardPage = getMyPosts(userNo,page,size);

        // 게시판 리스트(목록)
        List<BoardWithInfo> boardWithInfos = new ArrayList<>();


        for(Board board : boardPage.getContent()){
            // 좋아요 수
            int likesCount = likesService.getLikeCount(board.getBoardId());
            // 댓글 수
            int commentCount = commentService.getCommentCount(board.getBoardId());

            // 게시물 리스트에 추가
            boardWithInfos.add(new BoardWithInfo(board,likesCount,commentCount));
        }

        return boardWithInfos;
    }




    // 검색한 단어 가져오기, 페이징 처리(Pageable 페이징 객체)
    public Page<Board> searchBoard(String keyword, Pageable pageable){
        return boardRepository.findByTitleContainingOrContentsContainingOrUserNicknameContaining(keyword,keyword,keyword,pageable);
    }

    
    // 검색한 단어 ->  좋아요, 조회 수 추가
    public Page<BoardWithInfo> searchBoardWithInfo(String keyword, Pageable pageable){
        // 검색한 단어 추출
        Page<Board> searchBoard = searchBoard(keyword,pageable);

        List<BoardWithInfo> boardWithInfos = new ArrayList<>();

        for(Board board : searchBoard.getContent()){
            int likesCount = likesService.getLikeCount(board.getBoardId());
            int commentCount = commentService.getCommentCount(board.getBoardId());
            boardWithInfos.add(new BoardWithInfo(board,likesCount,commentCount));
        }


        return new PageImpl<BoardWithInfo>(boardWithInfos,pageable,searchBoard.getTotalElements());
    }



    // 게시글 조회
    public Board findByPost(Long id){
        return boardRepository.findById(id).orElse(null);
    }




    // 조회 수 증가
    public boolean incrementViewCount(Long id){
        // 게시물을 조회
        Board board = boardRepository.findById(id).orElse(null);

        if(board != null){
            board.setViewCount(board.getViewCount() + 1);
            boardRepository.save(board);
            return true;
        }else{
            return false;
        }
    }



    // 첨부파일 모두삭제
    public boolean deleteAllFiles(Long boardId){

        // 게시물 조회
        Board existingBoard = boardRepository.findById(boardId).orElse(null);

        // 게시물이 존재하지 않을 경우
        if(existingBoard == null){
            log.info("게시물이 존재하지 않습니다. boardId : {}",boardId);
            return false;
        }

        // 게시물에 첨부된 파일 조회
        List<AttachedFile> files =attachedFileRepository.findByBoardId(boardId);

        // 파일 삭제 처리
        for (AttachedFile file : files){
            // 서버에서 파일 삭제
            fileService.deleteAllFiles(file);

            // 데이터베이스에서 삭제
            attachedFileRepository.delete(file);
        }

        // 게시물의 첨부파일 목록 초기화
        existingBoard.getAttachedFileList().clear();
        return true;
    }



    // 사용자 게시물 삭제
    @Transactional
    public boolean deletePost (List<Long> boardIds, HttpSession session){

        Member member = (Member) session.getAttribute("member");

        try {
            for (Long boardId : boardIds){

                // 게시물 찾기
                Board board = boardRepository.findById(boardId).orElse(null);

                if(board == null){
                    throw new IllegalArgumentException("게시물을 찾을 수 없습니다");
                }

                if(!board.getUserNo().equals(member.getUserNo())){
                    throw new RuntimeException("본인 게시물만 삭제 할 수 있습니다");
                }



                // 1. 댓글 삭제
                commentRepository.deleteByBoardId(boardId);

                // 2. 좋아요 삭제
                likesRepository.deleteByBoardId(boardId);
                
                // 3. 첨부파일 삭제

                // 게시물에 첨부된 파일 조회
                List<AttachedFile> files =attachedFileRepository.findByBoardId(boardId);

                // 파일 삭제 처리
                for (AttachedFile file : files){
                    // 서버에서 파일 삭제
                    fileService.deleteAllFiles(file);

                    // 데이터베이스에서 삭제
                    attachedFileRepository.delete(file);
                }


                // 게시물 삭제
                boardRepository.deleteById(boardId);
            }
            return true;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    // 관리자 게시물 삭제
    @Transactional
    public boolean deleteBoard(Long boardId, HttpSession session){

        // 관리자 정보 가져오기
        Member member = (Member) session.getAttribute("member");

        try{

            if(!member.getGrade().equals("ADMIN")){
                throw new RuntimeException("관리자만 모든 게시물을 삭제 할 수 있습니다");
            }

            Board board = boardRepository.findById(boardId).orElse(null);

            if(board == null){
                throw new IllegalArgumentException("게시물을 찾을 수 없습니다");
            }

            // 1. 댓글 삭제
            commentRepository.deleteByBoardId(boardId);

            // 2. 좋아요 삭제
            likesRepository.deleteByBoardId(boardId);

            // 3. 첨부파일 삭제
            List<AttachedFile> files =attachedFileRepository.findByBoardId(boardId);

            // 파일 삭제 처리
            for (AttachedFile file : files){
                // 서버에서 파일 삭제
                fileService.deleteAllFiles(file);

                // 데이터베이스에서 삭제
                attachedFileRepository.delete(file);
            }

            // 게시물 삭제
            boardRepository.deleteById(boardId);

            return true;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }


}
