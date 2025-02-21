package com.project.project.service;

import com.project.project.dto.MemberDTO;
import com.project.project.entity.Member;
import com.project.project.entity.Role;
import com.project.project.entity.UserRole;
import com.project.project.repository.MemberRepository;
import com.project.project.repository.RoleRepository;
import com.project.project.repository.UserRoleRepository;
import jakarta.transaction.Transactional;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@Slf4j
@Builder
public class MemberService {

    private final MemberRepository memberRepository;

    private final RoleRepository roleRepository;

    private final UserRoleRepository userRoleRepository;

    // 의존성 주입
    public MemberService(MemberRepository memberRepository, RoleRepository roleRepository, UserRoleRepository userRoleRepository) {
        this.memberRepository = memberRepository;
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
    }


    // 로그인
    public Member login(String userId, String password){
        return memberRepository.findByUserIdAndPassword(userId, password).orElse(null);
    }

    // 아이디 중복체크
    public Boolean IDcheck (String userId){
        if(userId == null || userId.isEmpty()) {
            return false;
        }
        return memberRepository.existsByUserId(userId);
    }

    // 전화번호 중복체크
    public Boolean Phonecheck (String phoneNumber){
        if(phoneNumber == null || phoneNumber.isEmpty()){
            return false;
        }
        return memberRepository.existsByPhoneNumber(phoneNumber);
    }

    // 닉네임 중복체크
    public Boolean Nickcheck (String nickName){
        if(nickName == null || nickName.isEmpty()){
            return false;
        }
        return memberRepository.existsByNickName(nickName);
    }

    // 회원가입 (예외처리 때문에 userRole 저장 안될수도 있기 때문에 트랜잭션 처리)
    @Transactional
    public Member join (MemberDTO memberData){
        // 회원정보 저장
        Member member = memberData.toEntity();
        memberRepository.save(member);

        // 회원 권한 (예외처리 -> MEMBER 권한이 없을 경우)
        System.out.println("findByRoleName 실행 시작");
        Role memberRole = roleRepository.findByRoleName("MEMBER").orElseThrow(() -> new RuntimeException("회원 권한이 없습니다"));
        System.out.println("ROLE 조회 완료: " + memberRole);

        UserRole userRole = UserRole.builder()
                .member(member)
                .role(memberRole)
                .build();
        System.out.println("userRole 저장 전 " + userRole);
        userRoleRepository.save(userRole);
        System.out.println("userRole 저장 완료");


        return member;
    }

    // 아이디 찾기
    public Member findById (String userName, String phoneNumber){
        return memberRepository.findByUserNameAndPhoneNumber(userName,phoneNumber).orElse(null);
    }

    // 비밀번호 찾기
    public Member findByPw(String userName, String userId){
        return memberRepository.findByUserNameAndUserId(userName,userId).orElse(null);
    }

    // 회원 찾기
    public Member findBymember (String userId) {
        return memberRepository.findByUserId(userId).orElse(null);
    }

    // 회원 정보 수정
    public Member edit (Member member ){
        return memberRepository.save(member);
    }





    // 회원 삭제 시 데이터베이스에서 트리거를 이용
    // 회원과 연관된 게시물,댓글,첨부파일 삭제을 트랜잭션으로 관리
    @Transactional
    // 회원삭제
   public Boolean delete(String userId){
        // Optional<Member> 타입으로 불러오기
        Optional<Member> member = memberRepository.findByUserId(userId);
        // Optional을 이용하였기 때문에 null을 사용하지않고
        // isPresent()를 통해 값이 있는지 확인

       // 회원이 존재하는 경우
       if(member.isPresent()){
           // 값을 삭제
           userRoleRepository.deleteByMember_UserNo(member.get().getUserNo());
           memberRepository.delete(member.get());
           return true;
       }else{
           return false;
       }

   }


}
