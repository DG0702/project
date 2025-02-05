package com.project.project.controller;

import com.project.project.dto.CommentWithInfo;
import com.project.project.dto.CommentWithTitle;
import com.project.project.entity.AttachedFile;
import com.project.project.entity.Comment;
import com.project.project.entity.Member;
import com.project.project.entity.MenuCategory;
import com.project.project.repository.MenuCategoryRepository;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
public class ManagerController {

    @Autowired
    private MenuCategoryRepository menuCategoryRepository;

    @Autowired
    private FileService fileService;

    @Autowired
    private CommentService commentService;

    // 뉴스 수정 페이지 이동 
    @GetMapping("/manager/news")
    public String news(Model model) {

        // 메뉴 아아디 가져오기
        Long menuId = 1L;

        // 뉴스 데이터 가져오기
        MenuCategory menuCategory = menuCategoryRepository.findById(menuId).orElse(null);

        // 첨부파일 가져오기
        List<AttachedFile> attachedFileList = fileService.getFilesByMenuId(menuId);

        // 첨부파일 존재 유무
        boolean hasAttachedFiles = !attachedFileList.isEmpty();

        // 파일 구분하기
        List<String> images = new ArrayList<>();
        List<String> videos = new ArrayList<>();
        List<String> documents = new ArrayList<>();
        List<String> unknowns = new ArrayList<>();

        // 각 파일마다 타입 설정
        for(AttachedFile file : attachedFileList){
            // 파일 타입 설정
            String fileType = file.getFileType();
            String filePath = file.getPath2();

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

        model.addAttribute("menuCategory", menuCategory);
        model.addAttribute("hasAttachedFiles", hasAttachedFiles);
        model.addAttribute("images", images);
        model.addAttribute("videos", videos);
        model.addAttribute("documents", documents);
        model.addAttribute("unknowns", unknowns);

        return "/menu/edit_news";
    }



    // 모든 댓글 이동
    @GetMapping("/comment")
    public String Comment(@RequestParam (defaultValue = "1")int page, Model model  ){

        // 한 페이지에 보여줄 댓글의 수
        int size = 10;


        // 해당 페이지 댓글 리스트
        Page<Comment> commentPage = commentService.getCommentPage(page,size);

        int totalPage = commentPage.getTotalPages();

        List<Integer> pages = new ArrayList<>();

        for(int i = 1; i <= totalPage; i++){
            pages.add(i);
        }

        // 댓글 목록
        List<CommentWithInfo> commentWithInfos = commentService.getCommentInfo(page,size);


        // 뷰페이지
        model.addAttribute("commentWithInfos",commentWithInfos);
        model.addAttribute("pages",pages);
        model.addAttribute("hasPrevious",commentPage.hasPrevious());
        model.addAttribute("hasNext",commentPage.hasNext());
        model.addAttribute("previousPage", page -1);
        model.addAttribute("nextPage", page +1);


        return "/board/comment";

    }


    // 댓글 검색 페이지 이동
    @GetMapping("/comments/search")
    public String commentsSearch(@RequestParam String keyword, @RequestParam (defaultValue = "1") int page, Model model){

        // 페이지 단위 맞추기
        Pageable pageable = PageRequest.of(page-1 , 10);
        // 검색한 댓글 가져오기
        Page<CommentWithTitle> commentPage = commentService.searchCommentWithBoardInfo(keyword,pageable);

        int totalPage = commentPage.getTotalPages();

        List<Integer> pages = new ArrayList<>();
        for(int i = 1; i <= totalPage; i++){
            pages.add(i);
        }

        model.addAttribute("commentWithInfos",commentPage.getContent());
        model.addAttribute("pages",pages);
        model.addAttribute("hasPrevious",commentPage.hasPrevious());
        model.addAttribute("hasNext",commentPage.hasNext());
        model.addAttribute("previousPage", page -1);
        model.addAttribute("nextPage", page +1);
        model.addAttribute("keyword", keyword);


        return "/board/comment_search";
    }
}
