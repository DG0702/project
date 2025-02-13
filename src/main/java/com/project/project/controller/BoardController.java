package com.project.project.controller;

import com.project.project.dto.AttachedFileDTO;
import com.project.project.dto.BoardWithInfo;
import com.project.project.dto.PostsDTO;
import com.project.project.entity.AttachedFile;
import com.project.project.entity.Board;
import com.project.project.entity.Comment;
import com.project.project.entity.Member;
import com.project.project.service.BoardService;
import com.project.project.service.CommentService;
import com.project.project.service.FileService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
public class BoardController {

    // 의존성 주입
    @Autowired
    private BoardService boardService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private FileService fileService;

    // 게시판 이동 (게시물 정보 조회)
    @GetMapping("/board")
    // URL에서 전달된 page 파라미터 값이 없을 경우 기본값 1로 사용
    public String board(@RequestParam(defaultValue = "1")int page, Model model,HttpSession session){

        // 관리자 로그인 시
        Member member = (Member) session.getAttribute("member");

        // 관리자
        Long userId = member.getUserNo();

        // Page<Board>는 페이징된 결과를 의미 
        
        // 한 페이지에 보여줄 게시물 수
        int size = 10;

        // 해당 페이지의 게시물 리스트
        Page<Board> boardPage = boardService.getBoardPage(page,size);

        // 전체 페이지 수 계산
        int totalPages = boardPage.getTotalPages();

        // 페이지 번호 배열 생성  --> Integer : 객체의 순서 있는 컬렉션
        List<Integer> pages = new ArrayList<>();
        for(int i = 1; i <=totalPages; i++){
            pages.add(i);
        }



        // 게시판 목록

        List<BoardWithInfo> boardWithInfos = boardService.getBoardWithInfo(page,size);

        // 뷰 템플릿에 넘기기

        // 게시판 정보
        model.addAttribute("boardWithInfos",boardWithInfos);

        // 페이지 번호 생성
        model.addAttribute("pages",pages);

        // 이전 버튼 활성화 여부
        model.addAttribute("hasPrevious",boardPage.hasPrevious());

        // 다음 버튼 활성화 여부
        model.addAttribute("hasNext",boardPage.hasNext());

        // 이전 페이지 번호
        model.addAttribute("previousPage",page -1 );

        // 다음 페이지 번호
        model.addAttribute("nextPage",page +1);

        // 관리자가 로그인 하였을 경우
        model.addAttribute("isAdmin",userId == 1);



        return "/board/board";
    }


    // 게시물 검색 페이지 이동
    @GetMapping("/posts/search")
    public String search(@RequestParam String keyword, @RequestParam (defaultValue = "1") int page , Model model){

        // 페이지 단위 맞추기 (필요)
        Pageable pageable = PageRequest.of(page - 1, 10);
        // 검색한 단어 데이터로 가져오기 (페이징할 객체도 가져오기)
        Page<BoardWithInfo> boardPage = boardService.searchBoardWithInfo(keyword,pageable);

        // 전체 페이지 수
        int totalPage = boardPage.getTotalPages();

        // 페이지 수 배열로 생성
        List<Integer> pages = new ArrayList<>();
        for(int i = 1; i <= totalPage; i++){
            pages.add(i);
        }
        // 페이징 된 객체에 게시물 리스트
        model.addAttribute("boardWithInfos",boardPage.getContent());

        // 이전 페이지 활성화
        model.addAttribute("hasPrevious",boardPage.hasPrevious());

        // 다음 페이지 활성화
        model.addAttribute("hasNext",boardPage.hasNext());

        // 전체 페이지 수
        model.addAttribute("pages",pages);

        // 이전 페이지
        model.addAttribute("previousPage",page -1);

        // 다음 페이지
        model.addAttribute("nextPage",page + 1);

        model.addAttribute("keyword",keyword);



        return "/board/board_search";
    }







    // 글쓰기(게시물 작성) 이동
    @GetMapping ("/posts/new")
    public String posts_new(){
        return "/board/post_write";
    }








    // 게시물 등록
    @PostMapping("/posts")
    public String createPosts(@RequestParam("file")MultipartFile [] files, @ModelAttribute PostsDTO postsData, HttpSession session, RedirectAttributes rttr){

        // 세션에서 로그인한 사용자 정보 가져오기
        Member member = (Member) session.getAttribute("member");
        
        // 세션에서 가지고 온 정보가 없을 경우 로그인 페이지로 이동
        if(member == null){
            return "redirect:/login";
        }

        // userNo, nickName, 사용자 아이디(uploadId) 설정
        Long userNo = member.getUserNo();
        String userNickname = member.getNickName();


        // 데이터를 전달할 객체 DTO에 저장할 데이터에 직접 입력
        postsData.setUserNo(userNo);
        postsData.setUser_nickname(userNickname);
        postsData.setViewCount(0L);


        // 데이터베이스에 저장할 객체 생성 (DTO를 엔티티로 변경)
        Board board = postsData.toEntity();

        // 데이터베이스에 데이터를 저장하여 게시물을 생성 -> boardId 생성 (boardId 생성하기 위해 사용)
        Board save = boardService.createPost(board);





        // 첨부파일 존재 시 파일 업로드 처리

        if (files != null && files.length > 0 && !files[0].isEmpty()) {
            // DTO 생성
            AttachedFileDTO attachedFileDTO = new AttachedFileDTO();
            try{
                // boardId 값 넣어주기
                attachedFileDTO.setBoardId(save.getBoardId());

                // FileService 사용하여 파일을 서버에 업로드, 데이터베이스에 저장
                fileService.uploadFile(files,attachedFileDTO,session);

                // 게시물에 첨부파일 추가 후 게시물을 업데이트
                boardService.addFile(save);


            }
            catch(IOException e){
                // 리다이렉트 후 메시지 전달
                rttr.addFlashAttribute("errorMessage","파일 업로드 오류 발생 " + (e.getMessage() != null ? e.getMessage() : "알 수 없는 오류 "));
                return "redirect:/posts/new";
            }
        }

        return "redirect:/board";

    }




    // 게시물 조회
    @GetMapping("/posts/{boardId}")
    public String showPost(@PathVariable Long boardId, Model model, HttpSession session){
        // 게시글 찾기
        Board boardEntity = boardService.findByPost(boardId);

        // 첨부파일 목록을 가져오기
        List<AttachedFile> attachedFiles = fileService.getFilesByBoarId(boardId);



        // 첨부파일 존재하는지 (true,false)
        boolean hasAttachedFiles = !attachedFiles.isEmpty();

        // 파일 구분하기
        List<String> images = new ArrayList<>();
        List<String> videos = new ArrayList<>();
        List<String> documents = new ArrayList<>();
        List<String> unknowns = new ArrayList<>();

        // 각 파일마다 타입 설정
        for(AttachedFile file : attachedFiles){
            // 파일 타입 설정
            String fileType = file.getFileType();
            String filePath = file.getPath1();

            if("image".equals(fileType)){
                images.add(filePath);
            } else if ("video".equals(fileType)){
                videos.add(filePath);
            } else if ("document".equals(fileType)) {
                documents.add(filePath);
            }else {
                unknowns.add(filePath);
            }
        }

        // 게시글,첨부파일 뷰 페이지에 넘기기
        model.addAttribute("hasAttachedFiles", hasAttachedFiles);
        model.addAttribute("post",boardEntity);
        model.addAttribute("images", images);
        model.addAttribute("videos", videos);
        model.addAttribute("documents", documents);
        model.addAttribute("unknowns", unknowns);

        // 로그인 세션 정보 가져오기
        Member member = (Member)session.getAttribute("member");

        // 세션 정보를 가지고 오면
        if(member != null){
            // userNo는 board 테이블에 담기지 않기 때문에 수동으로 설정
            model.addAttribute("userNo",member.getUserNo());
            // 닉네임 가져오기
            model.addAttribute("userNickName",member.getNickName());
        }

        // 댓글 리스트
        List<Comment> commentList = commentService.getComments(boardId);

        // 댓글 개수 파악
        int commentCount = commentService.getCommentCount(boardId);

        // 뷰 페이지 넘기기
        model.addAttribute("commentList",commentList);
        model.addAttribute("commentCount",commentCount);




        return "/board/post";
    }
}
