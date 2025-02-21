package com.project.project.controller;
import com.project.project.dto.BoardWithInfo;
import com.project.project.dto.CommentWithInfo;
import com.project.project.entity.AttachedFile;
import com.project.project.entity.Board;
import com.project.project.entity.Comment;
import com.project.project.entity.Member;
import com.project.project.service.BoardService;
import com.project.project.service.CommentService;
import com.project.project.service.FileService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
public class MypageController {

    private final BoardService boardService;

    private final CommentService commentService;

    private final FileService fileService;

    // 의존성 주입
    public MypageController(BoardService boardService, CommentService commentService, FileService fileService) {
        this.boardService = boardService;
        this.commentService = commentService;
        this.fileService = fileService;
    }

    // 회원 정보 수정 이동
    @GetMapping("/users/modification")
    public String edit(){
        return "/mypage/edit_mypage";
    }

    // 회원 탈퇴 이동
    @GetMapping("/users/unregistration")
    public String unregister(){
        return "/mypage/unregister_mypage";
    }

    // 내가 쓴 게시물 이동
    @GetMapping("/users/{userId}/posts")
    public String posts(@RequestParam (defaultValue = "1") int page, Model model, HttpSession session){

        // 회원 정보 가져오기
        Member member = (Member) session.getAttribute("member");

        Long userNo = member.getUserNo();

        // 페이징된 결과

        int size = 10;

        // 내가 쓴 게시물 리스트
        Page<Board> boardpage = boardService.getMyPosts(userNo,page,size);

        // 전체 페이지 수
        int totalPage = boardpage.getTotalPages();

        // 페이지 번호 생성 -> Integer(순서)
        List<Integer> pages = new ArrayList<>();
        for(int i = 1; i <= totalPage; i++){
            pages.add(i);
        }

        List<BoardWithInfo> boardWithInfos = boardService.getMyBoardWithInfo(userNo,page,size);

        model.addAttribute("boardWithInfos",boardWithInfos);
        model.addAttribute("pages",pages);
        model.addAttribute("hasPrevious",boardpage.hasPrevious());
        model.addAttribute("hasNext",boardpage.hasNext());
        model.addAttribute("previousPage",page-1);
        model.addAttribute("nextPage",page+1);

        return "/mypage/my_post";
    }


    // 내가 쓴 게시물 수정 페이지 이동
    @GetMapping("/my/posts/{boardId}")
    public String editPost(@PathVariable Long boardId, Model model){

        Board post = boardService.getPost(boardId);

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
        model.addAttribute("post",post);
        model.addAttribute("images", images);
        model.addAttribute("videos", videos);
        model.addAttribute("documents", documents);
        model.addAttribute("unknowns", unknowns);
        return "/mypage/edit_post";
    }







    // 내가 쓴 댓글 이동
    @GetMapping("/users/{userId}/comments")
    public String comments(@RequestParam (defaultValue = "1")int page, Model model, HttpSession session){

        // 회원 정보 가져오기
        Member member = (Member) session.getAttribute("member");

        Long userNo = member.getUserNo();

        int size = 10;

        // 내가 쓴 댓글 리스트
        Page<Comment> commentPage = commentService.getMyComments(userNo,page,size);


        // 전체페이지
        int totalPages = commentPage.getTotalPages();

        List<Integer> pages = new ArrayList<>();
        for(int i = 1; i<= totalPages; i++){
            pages.add(i);
        }
        
        // 댓글 리스트, 게시글 정보 가져오기
       List<CommentWithInfo> commentWithInfos = commentService.getCommentWithBoardInfo(userNo,page,size);

        model.addAttribute("commentWithInfos",commentWithInfos);
        model.addAttribute("pages",pages);
        model.addAttribute("hasPrevious",commentPage.hasPrevious());
        model.addAttribute("hasNext",commentPage.hasNext());
        model.addAttribute("previousPage",page-1);
        model.addAttribute("nextPage",page+1);



        return "/mypage/my_comment";
    }



    // 내가 쓴 댓글 수정 페이지 이동
    @GetMapping("/my/comments/{commentId}")
    public String editComment (@PathVariable Long commentId, Model model){

        // 댓글 가져오기
        Comment comment = commentService.getComment(commentId);

        // 게시물 가져오기
        Board post = boardService.getPost(comment.getBoardId());

        // 첨부파일 가져오기
        List<AttachedFile> attachedFiles = fileService.getFilesByBoarId(comment.getBoardId());

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


        model.addAttribute("comment",comment);
        model.addAttribute("hasAttachedFiles", hasAttachedFiles);
        model.addAttribute("post",post);
        model.addAttribute("images", images);
        model.addAttribute("videos", videos);
        model.addAttribute("documents", documents);
        model.addAttribute("unknowns", unknowns);

        return "/mypage/edit_comment";
    }


}
