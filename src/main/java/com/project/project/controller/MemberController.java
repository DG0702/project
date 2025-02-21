package com.project.project.controller;


import com.project.project.dto.MemberDTO;
import com.project.project.entity.Member;
import com.project.project.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@Slf4j
public class MemberController {

    private final MemberService memberService;

    // 의존성 주입
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    // 로그인 페이지 이동
    @GetMapping("/login-page")
    public String showLoginPage(){
        return "/login/login";
    }
    
    // 로그인 과정
    @PostMapping("/login")
    public String processLogin(@RequestParam String userId, @RequestParam String password, HttpSession session, RedirectAttributes rttr){
        Member member = memberService.login(userId,password);
        //        log.info(member.toString());
        // 아이디,비밀번호 불일치시
        if(member == null){
            // 리다이렉트 후 메시지 전송
            rttr.addFlashAttribute("errorMessage","아이디와 비밀번호가 일치하지 않습니다.");
            return "redirect:/login-page";
        }else {
            // 로그인 성공시 세션에 사용자 정보 저장
            session.setAttribute("member",member);
            // 로그인 후 페이지로 리다이렉트
            return "redirect:/login-after";
        }
    }

    // 로그인 후 홈페이지 이동
    @GetMapping("/login-after")
    public String showAfterLoginPage(HttpSession session,Model model){

        Member member = (Member) session.getAttribute("member");
        if(member == null){
            return "redirect:/login-page";
        }

        Long userId = member.getUserNo();

        // 로그인한 아이디가 1일때만
        model.addAttribute("isAdmin",userId ==1);

        return "/login/login_after";
    }

    // 로그아웃
    @PostMapping("/logout")
    public String logout(HttpSession session){
        // 세션 종료하고 세션에 저장된 모든 데이터 삭제 (세션 초기화)
        session.invalidate();
        return "redirect:/";
    }




    // 아이디 찾기 이동
    @GetMapping("/users/find-id")
    public String findId(){
        return "/find/find_id";
    }

    // 비밀번호 찾기 이동
    @GetMapping("/users/find-pw")
    public String findPw(){
        return "/find/find_pw";
    }

    // 회원가입 이동
    @GetMapping("/users/membership")
    public String join(){
        return "/login/join";
    }

    // 회원가입
    @PostMapping("/users/membership")
    public String processJoin(MemberDTO memberData){
        Member saved = memberService.join(memberData);
//        log.info(saved.toString());
        return "redirect:/";
    }
}
