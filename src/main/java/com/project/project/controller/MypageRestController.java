package com.project.project.controller;

import com.project.project.entity.Board;
import com.project.project.entity.Member;
import com.project.project.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class MypageRestController {

    @Autowired
    private MemberService memberService;
    
    // 회원 정보 수정 유저확인
    @PostMapping("/validUser")
    public ResponseEntity<Map<String,Object>> validuser(@RequestBody Map<String, String> data){
        // 요청 본문 값 반환
        String userId = data.get("userId");
        String password = data.get("password");

        // 데이터베이스에 조회하기
        Member member = memberService.login(userId,password);

        // 응답데이터
        Map<String,Object> response = new HashMap<>();

        // 응답 데이터 넣기
        if(member != null){
            response.put("success", true);
            response.put("userId", member.getUserId());
            return ResponseEntity.ok(response);
        }else{
            response.put("success", false);
            
            // 아이디 존재 확인
            Boolean existMember = memberService.IDcheck(userId);

            // 아이디가 존재 하지 않을 경우
            if(!existMember){
                response.put("idValid",false);
                response.put("pwValid",false);
            }
            // 아이디는 존재하지만 비밀번호가 틀렸을 경우
            else{
                response.put("idValid",true);
                response.put("pwValid",false);
            }

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    // 회원 정보 가져오기
    @GetMapping("/getUserInfo")
    public ResponseEntity<Map<String,Object>> getUserInfo(@RequestParam String userId){
        Member member = memberService.findBymember(userId);

        // 반환할 응답 데이터 프로퍼티 설정
        Map<String,Object> response = new HashMap<>();
        
        // 회원 정보가 있을 경우
        if(member != null){
            response.put("userId", member.getUserId());
            response.put("password", member.getPassword());
            response.put("phone", member.getPhoneNumber());
            response.put("addr",member.getAddr());
            response.put("gender",member.getGender());
            return ResponseEntity.ok(response);
        }
        // 회원 정보가 없을 경우
        else{
            response.put("message","데이터를 찾을 수가 없습니다");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    // 회원 전화번호 중복체크
    @GetMapping("/check-Phone-Duplicate")
    public ResponseEntity<Map<String,Object>> checkPhoneDuplicate (@RequestParam (name = "phoneNumber") String phoneNumber, HttpSession session){

        Member member = (Member) session.getAttribute("member");

        if(member == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // 응답할 데이터 생성
        Map<String,Object> response = new HashMap<>();

        String existingPhone = member.getPhoneNumber();

        if(existingPhone.equals(phoneNumber)){
            response.put("isDuplicate",false);
            return ResponseEntity.ok(response);
        }else{
            // 전화번호 중복 체크
            Boolean isDuplicate = memberService.Phonecheck(phoneNumber);
            // 전화번호가 중복일 경우
            if(isDuplicate){
                response.put("isDuplicate",true);
            }
            return ResponseEntity.ok(response);
        }
    }


    // 회원 정보 수정
    @PutMapping("/users/{userId}")
    public ResponseEntity<Map<String,Object>> editUserInfo(@RequestBody Map<String,String> data){

        // 응답할 데이터
        Map<String,Object> response = new HashMap<>();

        
        // 비밀번호 유효성검사
        String password = data.get("password");
        if(password != null && password.trim().isEmpty()){
            response.put("success",false);
            response.put("message","비밀번호 입력해주세요!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } else if (password.length() < 4 || password.length() > 20) {
            response.put("success",false);
            response.put("message","비밀번호는 최소 4자리 최대 20자리 이하 입력가능합니다");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        // 전화번호 유효성검사
        String phoneNumber = data.get("phoneNumber");
        if(!phoneNumber.matches("^010-\\d{4}-\\d{4}$")){
            response.put("success",false);
            response.put("message","전화번호 형식이 올바르지 않습니다 \n 올바른 형식 : 010-xxxx-xxxx");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        if(phoneNumber != null && phoneNumber.trim().isEmpty()){
            response.put("success",false);
            response.put("message","전화번호를 입력해주세요!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        
        // 주소 유효성 검사
        String addr = data.get("addr");
        if(addr != null && addr.trim().isEmpty()){
            response.put("success",false);
            response.put("message","주소를 입력해주세요!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        // 성별 유효성 검사
        String gender = data.get("gender");
        if(gender != null && gender.trim().isEmpty()){
            response.put("success",false);
            response.put("message","성별을 선택해주세요!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        // 기존 회원 정보 가져오기
        Member member = memberService.findBymember(data.get("userId"));

        // 회원 정보 수정
        if(member != null){
            if(password != null && !password.trim().isEmpty()){
                member.setPassword(password);
            }
            if(phoneNumber != null && !phoneNumber.trim().isEmpty()){
                member.setPhoneNumber(phoneNumber);
            }
            if(addr != null && !addr.trim().isEmpty()){
                member.setAddr(addr);
            }
            if(gender != null && !gender.trim().isEmpty()){
                member.setGender(gender);
            }

            // 수정된 회원 정보 저장
            memberService.edit(member);

            // 성공 응답
            response.put("success",true);
            return ResponseEntity.ok(response);
            
        }
        // 회원 정보가 없는 경우
        else{
            response.put("success",false);
            response.put("message","회원 정보가 없습니다!");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

    }

    // 회원 탈퇴 체크
    @PostMapping("/users/unregistration")
    public ResponseEntity<Map<String,Object>> unregisterCheck(@RequestBody Map<String,String> data){

        // 요청본문으로 받은 데이터 반환하기
        String userId = data.get("userId");
        String password = data.get("password");

        // 데이터베이스에 조회
        Member member = memberService.login(userId,password);

        // 응답할 데이터
        Map<String,Object> response = new HashMap<>();

        if(member != null){
            response.put("success",true);
            return ResponseEntity.ok(response);
        }else{
            response.put("success",false);
            response.put("message","아이디 또는 비밀번호가 잘못되었습니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }


    // 회원 탈퇴
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Map<String,Object>> unregister (@PathVariable String userId){

        // 회원 탈퇴 처리
        Boolean isDeleted = memberService.delete(userId);

        // 응답할 데이터
        Map<String,Object> response = new HashMap<>();

        if(isDeleted){
            response.put("success",true);
            response.put("message","회원 탈퇴가 완료되었습니다");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }else{
            response.put("success",false);
            response.put("message","회원 정보가 존재하지 않습니다");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

    }


}
