package com.project.project.controller;

import com.project.project.entity.Member;
import com.project.project.repository.MemberRepository;
import com.project.project.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@Slf4j
public class MemberRestController {

    @Autowired
    private MemberService memberService;

    // 아이디 중복검사
    @GetMapping("/users/check-id")
    // @RequestParam에서 name 사용시 fetch메서드에 url에 쿼리 매개변수와 이름이 같아야한다 !!
    public Map<String, Boolean>IDcheckDuplication(@RequestParam(name="user_id") String userId){
        boolean isDuplicate = memberService.IDcheck(userId);
        Map<String, Boolean> response = new HashMap<>();
        response.put("isDuplicate",isDuplicate);
        return response;
    }

    // 전화번호 중복검사
    @GetMapping("/users/check-phone")
    // @RequestParam에서 name 사용시 fetch메서드에 url에 쿼리 매개변수와 이름이 같아야한다 !!
    public Map<String,Boolean> PhonecheckDuplicateion(@RequestParam(name = "phone_number") String phoneNumber){
        boolean isDuplicate = memberService.Phonecheck(phoneNumber);
        Map<String,Boolean> response = new HashMap<>();
        response.put("isDuplicate",isDuplicate);
        return response;
    }
    
    // 닉네임 중복검사
    @GetMapping("/users/check-nickname")
    public Map<String,Boolean> NickcheckDuplication(@RequestParam (name="nick_name") String nickName){
        boolean isDuplicate = memberService.Nickcheck(nickName);
        Map<String,Boolean> response = new HashMap<>();
        response.put("isDuplicate",isDuplicate);
        return response;
    }


    // 아이디 찾기 조회
    @PostMapping("/users/find-id")
    // ResponseEntity<Map<String,Object>> 사용 이유 : 응답 본문에 다양한 값을 넣기 위해
    // @RequestBody을 이용해 요청 본문에 값을 가지고 옴
    public ResponseEntity<Map<String,Object>> findId(@RequestBody Map<String,String> data){

        // 요청 본문 값 반환하기 get("키") >> 이 때 키의 값은 JSON.stringify()에서 설정한 값과 같아야함
        String userName = data.get("name");
        String phoneNumber = data.get("phone");

        // 이름, 전화번호 데이터베이스 조회 >> 값 찾기
        Member findId = memberService.findById(userName,phoneNumber);

        // 응답 데이터
        Map<String, Object> response = new HashMap<>();

        // 데이터 응답


        if(findId != null){
            // 성공시 데이터 반환
            response.put("success",true);
            response.put("id",findId.getUserId());
            return ResponseEntity.ok(response);
        }else{
            response.put("success",false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }



    // 비밀번호 찾기 조회
    @PostMapping("/users/find-pw")
    public ResponseEntity<Map<String,Object>> findPw(@RequestBody Map<String,String> data){

        // 요청 본문 값 반환
        String userName = data.get("name");
        String userId = data.get("id");

        // 데이터베이스에 조회
        Member findPw = memberService.findByPw(userName,userId);

        // 응답 할 데이터
        Map<String,Object> response = new HashMap<>();

        // 응답 본문에 들어갈 데이터 응답
        if(findPw != null){
            response.put("success",true);
            response.put("password",findPw.getPassword());
            return ResponseEntity.ok(response);
        }else{
            response.put("success",false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

    }
    
    
}
