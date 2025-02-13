package com.project.project.dto;

import com.project.project.entity.Member;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

// DTO : 데이터 전송을 위한 객체
// 클라이언트에서 서버로 , 서버에서 클라이언트로
// 데이터를 선택적으로 제한 가능
public class MemberDTO {
    private Long user_no;
    private String userId;
    private String userName;
    private String password;
    private String phoneNumber;
    private String nickName;
    private String birthday;
    private String addr;
    private String gender;
    private String grade;

    public Member toEntity(){
        Member member = new Member(user_no,userId,userName,password,phoneNumber,nickName,birthday,addr,gender,grade);

        return member;
    }
}
