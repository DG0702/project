package com.project.project.controller;


import com.project.project.entity.AttachedFile;
import com.project.project.entity.Member;
import com.project.project.entity.MenuCategory;
import com.project.project.repository.MenuCategoryRepository;
import com.project.project.service.FileService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
public class HomeController {

    private final MenuCategoryRepository menuCategoryRepository;

    private final FileService fileService;

    // 의존성 주입
    public HomeController(MenuCategoryRepository menuCategoryRepository, FileService fileService) {
        this.menuCategoryRepository = menuCategoryRepository;
        this.fileService = fileService;
    }




    // 홈화면 이동
    @GetMapping("/")
    public String mainPage(){
        return "/home/home";
    }

    // 메뉴 Meaning 이동
    @GetMapping("/home/meaning")
    public String meaning(){
        return "/menu/meaning";
    }

    // 메뉴 Heterology 이동
    @GetMapping("/home/heterology")
    public String heterology(){
        return "/menu/heterology";
    }

    // 메뉴 Forecast 이동
    @GetMapping("/home/forecast")
    public String forecast(){
        return "/menu/forecast";
    }

    // 메뉴 Solution_plan 이동
    @GetMapping("/home/solution_plan")
    public String solution_plan(){
        return "/menu/solution_plan";
    }

    // 메뉴 News 이동
    @GetMapping("/home/news")
    public String news(Model model){


        // 메뉴 아이디
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



        return "/menu/news";
    }


    // 로그인 후 메뉴 Meaning 이동
    @GetMapping("/after/meaning")
    public String after_meaning(){
        return "/menu/after_meaning";
    }

    // 로그인 후 메뉴 Heterology 이동
    @GetMapping("/after/heterology")
    public String after_heterology(){
        return "/menu/after_heterology";
    }

    // 로그인 후 메뉴 Forecast 이동
    @GetMapping("/after/forecast")
    public String after_forecast(){
        return "/menu/after_forecast";
    }

    // 로그인 후 메뉴 Solution_plan 이동
    @GetMapping("/after/solution_plan")
    public String after_solution_plan(){
        return "/menu/after_solution_plan";
    }

    // 로그인 후 메뉴 News 이동
    @GetMapping("/after/news")
    public String after_news(HttpSession session, Model model){
        // 회원 정보 가져오기
        Member member = (Member) session.getAttribute("member");

        if(member == null){
            return "redirect:/login-page";
        }

        // 로그인 한 아이디 번호
        Long userId = member.getUserNo();
        
        // 메뉴 아이디 
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

        // 로그인한 아이디가 1일때만
        model.addAttribute("isAdmin",userId ==1);
        return "/menu/after_news";
    }



    // 로그인 후 Mypage 이동
    @GetMapping("/users/mypage")
    public String mypage(HttpSession session,Model model){

        // 회원정보 가져오기
        Member member = (Member) session.getAttribute("member");

        if (member == null){
            return "redirect:/login-page";
        }

        Long userId = member.getUserNo();

        model.addAttribute("userId", userId);
        return "/mypage/mypage";
    }

    // 이용 약관 및 개인정보보호법
    @GetMapping("/infomation")
    public String info(){
        return "/layouts/terms_personal_Info";
    }








}
